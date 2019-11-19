package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.Condition;
import com.xforceplus.ultraman.permissions.sql.define.ConditionOperator;
import com.xforceplus.ultraman.permissions.sql.define.Conditional;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.relationship.Relationship;
import com.xforceplus.ultraman.permissions.sql.define.values.NullValue;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ConversionHelper;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;
import com.xforceplus.ultraman.permissions.sql.processor.ability.ConditionAbility;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;

import java.util.*;
import java.util.stream.Collectors;

/**
 * where 子句.
 * 只支持 select update 和 delete 语句.
 *
 * @author dongbin
 * @version 0.1 2019/10/25 18:57
 * @since 1.8
 */
public class JSqlParserConditionAbility extends AbstractJSqlParserHandler implements ConditionAbility {

    private static final Expression ROOT_FLAG = new RootExpression();

    private Expression where;

    public JSqlParserConditionAbility(PlainSelect plainSelect) {
        super(plainSelect);
        where = plainSelect.getWhere();
    }

    public JSqlParserConditionAbility(Statement statement) {
        super(statement);
        getStatement().accept(new StatementVisitorAdapter() {
            @Override
            public void visit(Select select) {
                SelectBody body = select.getSelectBody();
                if (body != null) {
                    body.accept(new SelectVisitorAdapter() {
                        @Override
                        public void visit(PlainSelect plainSelect) {
                            where = plainSelect.getWhere();
                        }
                    });
                }
            }

            @Override
            public void visit(Delete delete) {
                where = delete.getWhere();
            }

            @Override
            public void visit(Update update) {
                where = update.getWhere();
            }
        });
    }

    @Override
    public void add(Condition condition, Conditional conditional, boolean isolation) throws ProcessorException {
        addAll(condition, conditional, isolation);
    }

    @Override
    public void add(Relationship conditions, Conditional conditional, boolean isolation) throws ProcessorException {
        addAll(conditions, conditional, isolation);
    }

    private void addAll(Item conditions, Conditional conditional, boolean isolation) throws ProcessorException {
        Expression newCondition = buildExpression(conditions);

        switch (conditional) {
            case AND: {
                doAdd(true, where, newCondition, isolation);
                break;
            }
            case OR: {
                doAdd(false, where, newCondition, isolation);
                break;
            }
            default: {
                throw new ProcessorException("Can not add condition!");
            }
        }
    }

    @Override
    public void remove(Condition condition) throws ProcessorException {
        if (where != null) {
            Expression targetExpression = buildExpression(condition);

            // 表示只有一个条件
            if (ComparisonOperator.class.isInstance(where)) {
                if (equalsExpr(where, targetExpression)) {
                    setWhere(null);
                }
            } else {

                Map<Expression, FatherInfo> familyBook = new HashMap();
                familyBook.put(ROOT_FLAG, new FatherInfo(true, where));
                RemoveExprVisit visitor = new RemoveExprVisit(targetExpression, familyBook);
                where.accept(visitor);

                setWhere(familyBook.get(ROOT_FLAG).getExpr());
            }
        }
    }

    /**
     * 对于in 如果值是了查询,将用转换为字符串表示整个子句.
     * 对于 exists 将忽略,因为有专门的子句处理器.
     * 对于 any 和 some 会被当做一个普通的处理函数.
     */
    @Override
    public List<Condition> list() throws ProcessorException {
        if (where == null) {
            return Collections.EMPTY_LIST;
        }

        List<Condition> conditions = new ArrayList();
        where.accept(new ExpressionVisitorAdapter() {

            @Override
            public void visit(Between expr) {
                Column c = (Column) expr.getLeftExpression();

                List<Item> values =
                    Arrays.asList(ConversionHelper.convertSmart(expr.getBetweenExpressionStart()),
                        ConversionHelper.convertSmart(expr.getBetweenExpressionEnd()));

                conditions.add(buildCondition(c, values, ConditionOperator.BETWEEN));
            }

            @Override
            public void visit(InExpression expr) {
                Column c = (Column) expr.getLeftExpression();

                // 子查询,使用字符串值表示,原因是有专门的子查询处理器.
                if (SubSelect.class.isInstance(expr.getRightItemsList())) {
                    conditions.add(
                        buildCondition(
                            c,
                            Arrays.asList(
                                new com.xforceplus.ultraman.permissions.sql.define.values.StringValue(
                                    ((SubSelect) expr.getRightItemsList()).getSelectBody().toString())),
                            expr.isNot() ? ConditionOperator.NOT_IN : ConditionOperator.IN));
                } else {

                    ExpressionList expressionList = (ExpressionList) expr.getRightItemsList();
                    List<Expression> expressions = expressionList.getExpressions();
                    List<Item> values = expressions.stream()
                        .map(e -> ConversionHelper.convertSmart(e)).collect(Collectors.toList());

                    conditions.add(
                        buildCondition(
                            c, values, (expr.isNot() ? ConditionOperator.NOT_IN : ConditionOperator.IN)));
                }

            }


            @Override
            public void visit(IsNullExpression expr) {
                conditions.add(
                    buildCondition(
                        expr.getLeftExpression(),
                        Arrays.asList(NullValue.getInstance()),
                        ConditionOperator.IS_NUll));
            }

            @Override
            public void visit(LikeExpression expr) {
                conditions.add(
                    buildCondition(
                        expr.getLeftExpression(),
                        Arrays.asList(ConversionHelper.convertSmart(expr.getRightExpression())),
                        ConditionOperator.LIKE));
            }

            @Override
            public void visit(EqualsTo expr) {
                doAddComparisionCondition(expr, ConditionOperator.EQUALS);
            }

            @Override
            public void visit(GreaterThan expr) {
                doAddComparisionCondition(expr, ConditionOperator.GREATER_THAN);
            }

            @Override
            public void visit(GreaterThanEquals expr) {
                doAddComparisionCondition(expr, ConditionOperator.GREATER_THAN_EQUALS);
            }


            @Override
            public void visit(MinorThan expr) {
                doAddComparisionCondition(expr, ConditionOperator.MINOR_THAN);
            }

            @Override
            public void visit(MinorThanEquals expr) {
                doAddComparisionCondition(expr, ConditionOperator.MINOR_THAN_EQUALS);
            }

            @Override
            public void visit(NotEqualsTo expr) {
                doAddComparisionCondition(expr, ConditionOperator.NOT_EQUALS);
            }

            private void doAddComparisionCondition(ComparisonOperator expr, ConditionOperator operator) {

                conditions.add(
                    buildCondition(
                        expr.getLeftExpression(),
                        Arrays.asList(ConversionHelper.convertSmart(expr.getRightExpression())), operator));

            }

        });

        return conditions;
    }


    private void doAdd(boolean and, Expression oldWhere, Expression newCondition, boolean isolation) {
        if (oldWhere == null) {
            setWhere(isolation ? new Parenthesis(newCondition) : newCondition);
            return;
        }

        Expression newWhere;
        Expression oldConditions;
        if (isolation) {
            if (Parenthesis.class.isInstance(oldWhere)) {
                oldConditions = oldWhere;
            } else {
                oldConditions = new Parenthesis(oldWhere);
            }
        } else {
            oldConditions = oldWhere;
        }

        if (and) {
            newWhere = new AndExpression(oldConditions, isolation ? new Parenthesis(newCondition) : newCondition);
        } else {
            newWhere = new OrExpression(oldConditions, isolation ? new Parenthesis(newCondition) : newCondition);
        }

        setWhere(newWhere);
    }

    private void setWhere(Expression newWhere) {

        if (isSubSelect()) {

            getSubSelect().setWhere(newWhere);

        } else {
            getStatement().accept(new StatementVisitorAdapter() {
                @Override
                public void visit(Select select) {
                    SelectBody selectBody = select.getSelectBody();
                    if (selectBody != null) {
                        selectBody.accept(new SelectVisitorAdapter() {
                            @Override
                            public void visit(PlainSelect plainSelect) {
                                plainSelect.setWhere(newWhere);
                            }
                        });
                    }
                }

                @Override
                public void visit(Delete delete) {
                    delete.setWhere(newWhere);
                }

                @Override
                public void visit(Update update) {
                    update.setWhere(newWhere);
                }
            });
        }

        this.where = newWhere;
    }

    private Expression buildExpression(Item condition) throws ProcessorException {
        String conditionSql = condition.toSqlString();
        Expression newCondition;
        try {
            newCondition = CCJSqlParserUtil.parseCondExpression(conditionSql);
        } catch (JSQLParserException e) {
            throw new ProcessorException(e.getMessage(), e);
        }

        return newCondition;
    }

    private static class RemoveExprVisit extends ExpressionVisitorAdapter {

        private Map<Expression, FatherInfo> familyBook;
        private Expression target;

        public RemoveExprVisit(Expression target, Map<Expression, FatherInfo> familyBook) {
            this.target = target;
            this.familyBook = familyBook;
        }

        @Override
        protected void visitBinaryExpression(BinaryExpression expr) {

            Expression l = expr.getLeftExpression();
            Expression r = expr.getRightExpression();

            familyBook.put(l, new FatherInfo(true, expr));
            familyBook.put(r, new FatherInfo(false, expr));

            if (l != null) {
                if (AndExpression.class.isInstance(l)
                    || OrExpression.class.isInstance(l)
                    || Parenthesis.class.isInstance(l)) {

                    l.accept(new RemoveExprVisit(target, familyBook));

                } else {

                    remove(l, target);
                }
            }

            if (r != null) {
                if (AndExpression.class.isInstance(r)
                    || OrExpression.class.isInstance(r)
                    || Parenthesis.class.isInstance(r)) {

                    r.accept(new RemoveExprVisit(target, familyBook));

                } else {

                    remove(r, target);
                }
            }
        }

        private void remove(Expression current, Expression target) {
            if (equalsExpr(current, target)) {
                FatherInfo fatherInfo = familyBook.get(current);
                if (fatherInfo != null) {
                    FatherInfo grandfatherInfo = familyBook.get(fatherInfo.getExpr());
                    if (grandfatherInfo != null) {

                        /**
                         * 父结点和祖父结点同时存在,删除父结点,将父结点的子结点即当前结点的兄弟结点设置为
                         * 祖父结点的子结点.
                         */
                        BinaryExpression grandFatherExpr = (BinaryExpression) grandfatherInfo.getExpr();
                        BinaryExpression fatherExpr = ((BinaryExpression) fatherInfo.getExpr());

                        if (grandfatherInfo.isLeft()) {
                            if (fatherInfo.left) {

                                grandFatherExpr.setLeftExpression(fatherExpr.getRightExpression());

                            } else {

                                grandFatherExpr.setLeftExpression(fatherExpr.getLeftExpression());

                            }
                        } else {
                            if (fatherInfo.left) {

                                grandFatherExpr.setRightExpression(fatherExpr.getRightExpression());

                            } else {

                                grandFatherExpr.setRightExpression(fatherExpr.getLeftExpression());

                            }

                        }
                    } else {

                        BinaryExpression fatherExpr = ((BinaryExpression) fatherInfo.getExpr());
                        if (fatherInfo.isLeft()) {
                            // 新的根结点
                            familyBook.put(ROOT_FLAG, new FatherInfo(true, fatherExpr.getRightExpression()));
                        } else {
                            fatherExpr.getLeftExpression();

                            familyBook.put(ROOT_FLAG, new FatherInfo(false, fatherExpr.getLeftExpression()));
                        }

                    }
                } else {

                    // 唯一条件被删除
                    familyBook.put(ROOT_FLAG, new FatherInfo(true, null));
                }
            }
        }

    }

    private static boolean equalsExpr(Expression c, Expression t) {
        if (!c.getClass().equals(t.getClass())) {
            return false;
        }

        return c.toString().equals(t.toString());
    }

    private static class FatherInfo {
        private boolean left;
        private Expression expr;

        public FatherInfo(boolean left, Expression expr) {
            this.left = left;
            this.expr = expr;
        }

        public boolean isLeft() {
            return left;
        }

        public Expression getExpr() {
            return expr;
        }
    }

    // 只是一个标识 root 结点的占用对象.
    private static class RootExpression implements Expression {

        @Override
        public void accept(ExpressionVisitor expressionVisitor) {
        }

        @Override
        public SimpleNode getASTNode() {
            return null;
        }

        @Override
        public void setASTNode(SimpleNode node) {

        }
    }

    private Condition buildCondition(Expression left, List<Item> values, ConditionOperator operator) {
        return new Condition(
            ConversionHelper.convertSmart(left),
            operator,
            values);
    }
}
