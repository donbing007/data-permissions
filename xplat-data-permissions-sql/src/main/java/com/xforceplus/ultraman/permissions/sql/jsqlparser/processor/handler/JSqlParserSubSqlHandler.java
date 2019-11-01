package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.handler;

import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.JSubSql;
import com.xforceplus.ultraman.permissions.sql.processor.handler.SubSqlHandler;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理所有子查询,包含 union 和 union all.
 *
 * @version 0.1 2019/10/31 13:56
 * @auth dongbin
 * @since 1.8
 */
public class JSqlParserSubSqlHandler extends AbstractJSqlParserHandler implements SubSqlHandler {

    public JSqlParserSubSqlHandler(Statement statement) {
        super(statement, Select.class);
    }

    public JSqlParserSubSqlHandler(PlainSelect plainSelect) {
        super(plainSelect);
    }

    @Override
    public List<Sql> list() {
        List<Sql> subSqls = new ArrayList<>();
        if (isSelect()) {

            getSubSelect().accept(new ListSelectVisitorImpl(subSqls));

        } else {
            getSelect().getSelectBody().accept(new ListSelectVisitorImpl(subSqls));
        }

        return subSqls;
    }

    private static class ListSelectVisitorImpl extends SelectVisitorAdapter {

        private List<Sql> subSqls;

        public ListSelectVisitorImpl(List<Sql> subSqls) {
            this.subSqls = subSqls;
        }

        @Override
        public void visit(PlainSelect plainSelect) {
            doSubFromItem(subSqls, plainSelect.getFromItem());
            doSubJoins(subSqls, plainSelect.getJoins());
            doSubWhere(subSqls, plainSelect.getWhere());
        }

        @Override
        public void visit(SetOperationList setOpList) {
            doSubSetOperatorList(subSqls, setOpList);
        }
    }

    private static void doSubWhere(List<Sql> subSqls, Expression where) {
        if (where == null) {
            return;
        }

        where.accept(new ExpressionVisitorAdapter() {

            @Override
            public void visit(EqualsTo expr) {
                doProcessComparisonOperator(subSqls, expr);
            }

            @Override
            public void visit(GreaterThan expr) {
                doProcessComparisonOperator(subSqls, expr);
            }

            @Override
            public void visit(GreaterThanEquals expr) {
                doProcessComparisonOperator(subSqls, expr);
            }


            @Override
            public void visit(MinorThan expr) {
                doProcessComparisonOperator(subSqls, expr);
            }

            @Override
            public void visit(MinorThanEquals expr) {
                doProcessComparisonOperator(subSqls, expr);
            }

            @Override
            public void visit(NotEqualsTo expr) {
                doProcessComparisonOperator(subSqls, expr);
            }

            @Override
            public void visit(ExistsExpression expr) {
                if (SubSelect.class.isInstance(expr.getRightExpression())) {
                    doAddPlainSelect(subSqls, (SubSelect) expr.getRightExpression());
                }
            }

            @Override
            public void visit(InExpression expr) {
                if (SubSelect.class.isInstance(expr.getRightItemsList())) {
                    doAddPlainSelect(subSqls, (SubSelect) expr.getRightItemsList());
                }
            }
        });
    }

    private static void doSubSetOperatorList(List<Sql> subSqls, SetOperationList setOpList) {
        List<SelectBody> bodyList = setOpList.getSelects();
        for (int i = 0; i < bodyList.size(); i++) {
            subSqls.add(new JSubSql((PlainSelect) bodyList.get(i)));
        }
    }


    private static void doSubJoins(List<Sql> subSqls, List<Join> joins) {
        if (joins != null) {
            for (Join join : joins) {
                join.getRightItem().accept(new FromItemVisitorAdapter() {
                    @Override
                    public void visit(SubSelect subSelect) {
                        doAddPlainSelect(subSqls, subSelect);
                    }
                });
            }
        }
    }

    private static void doSubFromItem(List<Sql> subSqls, FromItem fromItem) {
        if (fromItem != null) {
            fromItem.accept(new FromItemVisitorAdapter() {
                @Override
                public void visit(SubSelect subSelect) {
                    doAddPlainSelect(subSqls, subSelect);
                }
            });
        }
    }

    private static void doProcessComparisonOperator(List<Sql> subs, ComparisonOperator expr) {
        if (AnyComparisonExpression.class.isInstance(expr.getRightExpression())) {
            AnyComparisonExpression any = (AnyComparisonExpression) expr.getRightExpression();
            doAddPlainSelect(subs,any.getSubSelect());
        }
    }

    private static void doAddPlainSelect(List<Sql> subSqls, SubSelect subSelect) {
        subSelect.getSelectBody().accept(new SelectVisitorAdapter() {
            @Override
            public void visit(PlainSelect plainSelect) {
                subSqls.add(new JSubSql(plainSelect));
            }

            @Override
            public void visit(SetOperationList setOpList) {
                java.util.List<SelectBody> selectBodyList = setOpList.getSelects();
                for (SelectBody opBody : selectBodyList) {
                    subSqls.add(new JSubSql((PlainSelect) opBody));
                }
            }
        });
    }

}
