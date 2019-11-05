package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability;

import com.xforceplus.ultraman.permissions.sql.define.*;
import com.xforceplus.ultraman.permissions.sql.define.values.NullValue;
import com.xforceplus.ultraman.permissions.sql.define.values.Value;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ConversionHelper;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.utils.ExceptionHelper;
import com.xforceplus.ultraman.permissions.sql.processor.ability.ConditionAbility;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * where 子句.
 * 只支持 select update 和 delete 语句.
 *
 * @version 0.1 2019/10/25 18:57
 * @auth dongbin
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
    public void add(Condition condition, Conditional conditional, boolean isolation) throws ParseException {
        Expression newCondition = buildExpression(condition);

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
                throw new ParseException("Can not add condition!", -1);
            }
        }
    }

    @Override
    public void remove(Condition condition) throws ParseException {
        if (where != null) {
            Expression targetExpression = buildExpression(condition);
            if (!ComparisonOperator.class.isInstance(targetExpression)
                && !Between.class.isInstance(targetExpression)
                && !InExpression.class.isInstance(targetExpression)
                && !LikeExpression.class.isInstance(targetExpression)
                && !IsNullExpression.class.isInstance(targetExpression)) {
                throw new ParseException(
                    "Only support column=value, column between a and b, column in [a, b, c], column like a", -1);
            }

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

    @Override
    public List<Condition> list() throws ParseException {
        if (where == null) {
            return Collections.EMPTY_LIST;
        }

        List<Condition> conditions = new ArrayList();
        where.accept(new ExpressionVisitorAdapter() {

            @Override
            public void visit(Between expr) {
                Column c = (Column) expr.getLeftExpression();

                List<Value> values =
                    Arrays.asList(ConversionHelper.convertValue(expr.getBetweenExpressionStart()),
                        ConversionHelper.convertValue(expr.getBetweenExpressionEnd()));

                conditions.add(buildConditionFromColumn(c, values, ConditionOperator.BETWEEN));
            }

            @Override
            public void visit(InExpression expr) {
                Column c = (Column) expr.getLeftExpression();

                // 不处理子查询.
                if (!ExpressionList.class.isInstance(expr.getRightItemsList())) {
                    return;
                }

                ExpressionList expressionList = (ExpressionList) expr.getRightItemsList();
                List<Expression> expressions = expressionList.getExpressions();
                List<Value> values = expressions.stream().map(e -> ConversionHelper.convertValue(e)).collect(Collectors.toList());

                if (expr.isNot()) {
                    conditions.add(buildConditionFromColumn(c, values, ConditionOperator.NOT_IN));
                } else {
                    conditions.add(buildConditionFromColumn(c, values, ConditionOperator.IN));
                }
            }


            @Override
            public void visit(IsNullExpression expr) {
                Column c = (Column) expr.getLeftExpression();
                List<Value> values = Arrays.asList(NullValue.getInstance());

                conditions.add(buildConditionFromColumn(c, values, ConditionOperator.IS_NUll));
            }

            @Override
            public void visit(LikeExpression expr) {
                Column c = (Column) expr.getLeftExpression();
                List<Value> values = Arrays.asList(ConversionHelper.convertValue(expr.getRightExpression()));

                conditions.add(buildConditionFromColumn(c, values, ConditionOperator.LIKE));
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
                // 忽略 any some,因为里面只有子查询.
                if (AnyComparisonExpression.class.isInstance(expr.getRightExpression())) {
                    return;
                }

                Expression leftExpr = expr.getLeftExpression();
                if (Column.class.isInstance(leftExpr)) {

                    Column c = (Column) leftExpr;
                    List<Value> values = Arrays.asList(ConversionHelper.convertValue(expr.getRightExpression()));
                    conditions.add(buildConditionFromColumn(c, values, operator));

                } else if (Function.class.isInstance(leftExpr)) {

                    Function f = (Function) leftExpr;
                    List<Value> values = Arrays.asList(ConversionHelper.convertValue(expr.getRightExpression()));
                    conditions.add(buildConditionFromFunction(f, values, operator));
                }

            }

        });

        return conditions;
    }


    private void doAdd(boolean and, Expression oldWhere, Expression newCondition, boolean isolation) {
        if (oldWhere == null) {
            setWhere(newCondition);
            return;
        }

        Expression newWhere;
        Expression oldConditions;
        if (isolation) {
            oldConditions = new Parenthesis(oldWhere);
        } else {
            oldConditions = oldWhere;
        }

        if (and) {
            newWhere = new AndExpression(oldConditions, newCondition);
        } else {
            newWhere = new OrExpression(oldConditions, newCondition);
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
    }

    private Expression buildExpression(Condition condition) throws ParseException {
        String conditionSql = condition.toSqlString();
        Expression newCondition;
        try {
            newCondition = CCJSqlParserUtil.parseCondExpression(conditionSql);
        } catch (JSQLParserException e) {
            throw ExceptionHelper.toParseException(e);
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

    private String findTable(Column column) {
        Table table = column.getTable();
        if (table != null) {
            return table.getFullyQualifiedName();
        }

        return null;
    }

    private Condition buildConditionFromColumn(Column column, List<Value> values, ConditionOperator operator) {

        Field field = ConversionHelper.convert(column);

        return new Condition(field, operator, values);
    }

    private Condition buildConditionFromFunction(Function f, List<Value> values, ConditionOperator operator) {
        Func func = ConversionHelper.convert(f);

        return new Condition(func, operator, values);
    }
}
