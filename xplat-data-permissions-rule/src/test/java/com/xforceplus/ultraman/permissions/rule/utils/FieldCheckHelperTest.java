package com.xforceplus.ultraman.permissions.rule.utils;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.auth.Authorizations;
import com.xforceplus.ultraman.permissions.pojo.rule.FieldRule;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import com.xforceplus.ultraman.permissions.sql.Sql;
import com.xforceplus.ultraman.permissions.sql.SqlParser;
import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.define.UpdateSet;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.JSqlParser;
import com.xforceplus.ultraman.permissions.sql.processor.*;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * FieldCheckHelper Tester.
 *
 * @author <Authors name>
 * @version 1.0 12/04/2019
 * @since <pre>Dec 4, 2019</pre>
 */
public class FieldCheckHelperTest {

    private SqlParser sqlParser = JSqlParser.getInstance();

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }


    /**
     * Method: checkFieldsRule(FieldFromAbility fieldFromAbility, Authorizations authorizations, List<Field> fields, Searcher searcher)
     */
    @Test
    public void testCheckFieldsRule() throws Exception {

        List<Pack> caseData = buildCase();

        caseData.stream().forEach(pack -> {

            Authorizations authorizations = new Authorizations();
            pack.rules.keySet().stream().forEach(authorizationPack -> authorizations.add(authorizationPack.authorization));

            Searcher searcher = mock(Searcher.class);
            for (AuthorationEntityPack aPack : pack.rules.keySet()) {

                List<FieldRule> rules = pack.rules.get(aPack);
                when(searcher.searchFieldRule(aPack.authorization, aPack.entity)).thenReturn(rules);
            }

            Sql sql = sqlParser.parser(pack.sql);

            sql.visit(new SqlProcessorVisitorAdapter() {
                @Override
                public void visit(SelectSqlProcessor processor) {
                    List<Item> items = processor.buildSelectItemAbility().list();
                    List<Field> fields = items.stream().map(i -> (Field) i).collect(Collectors.toList());

                    processor(fields, processor.buildFieldFromAbility());
                }

                @Override
                public void visit(InsertSqlProcessor processor) {
                    List<Field> fields = processor.buildInsertItemAbility().list();
                    processor(fields, processor.buildFieldFromAbility());
                }

                @Override
                public void visit(DeleteSqlProcessor processor) {

                }

                @Override
                public void visit(UpdateSqlProcessor processor) {
                    List<UpdateSet> updateSets = processor.buildUpdateSetAbility().list();
                    List<Field> fields =
                        updateSets.stream().map(updateSet -> updateSet.getField()).collect(Collectors.toList());
                    processor(fields, processor.buildFieldFromAbility());
                }

                private void processor(List<Field> targetFields, FieldFromAbility fieldFromAbility) {
                    Collection<Field> remaining = FieldCheckHelper.checkFieldsRule(
                        fieldFromAbility, authorizations, targetFields, searcher);

                    Assert.assertEquals(pack.sql, false, remaining.retainAll(pack.expectedFields));

                }

            });
        });

    }

    private List<Pack> buildCase() {
        List<Pack> data = new ArrayList<>();

        data.add(
            new Pack(
                "select t.c1, t.c2 from t1 t",
                new HashMap() {{
                    put(
                        new AuthorationEntityPack(new Authorization("r1", "t1"), "t1"),
                        Arrays.asList(
                            new FieldRule("t1", "c1")
                        )
                    );

                    put(
                        new AuthorationEntityPack(new Authorization("r2", "t1"), "t1"),
                        Arrays.asList(
                            new FieldRule("t1", "c2")
                        )
                    );
                }},
                Arrays.asList()
            )
        );

        data.add(
            new Pack(
                "select t.c1, t.c2 from t1 t",
                new HashMap() {{
                    put(
                        new AuthorationEntityPack(new Authorization("r1", "t1"), "t1"),
                        Arrays.asList()
                    );

                    put(
                        new AuthorationEntityPack(new Authorization("r2", "t1"), "t1"),
                        Arrays.asList(
                            new FieldRule("t1", "c2"),
                            new FieldRule("t1", "c1")
                        )
                    );
                }},
                Arrays.asList()
            )
        );

        data.add(
            new Pack(
                "select t.c1, t.c2, t.c3 from t1 t",
                new HashMap() {{
                    put(
                        new AuthorationEntityPack(new Authorization("r1", "t1"), "t1"),
                        Arrays.asList()
                    );

                    put(
                        new AuthorationEntityPack(new Authorization("r2", "t1"), "t1"),
                        Arrays.asList(
                            new FieldRule("t1", "c2"),
                            new FieldRule("t1", "c1")
                        )
                    );
                }},
                Arrays.asList(
                    new Field("t", "c3")
                )
            )
        );

        data.add(
            new Pack(
                "update t1 set c1=20,c2=20",
                new HashMap() {{
                    put(
                        new AuthorationEntityPack(new Authorization("r1", "t1"), "t1"),
                        Arrays.asList()
                    );

                    put(
                        new AuthorationEntityPack(new Authorization("r2", "t1"), "t1"),
                        Arrays.asList(
                            new FieldRule("t1", "c2"),
                            new FieldRule("t1", "c1")
                        )
                    );
                }},
                Arrays.asList(
                )
            )
        );

        data.add(
            new Pack(
                "update t1 set t1.c1=20,t1.c2=20",
                new HashMap() {{
                    put(
                        new AuthorationEntityPack(new Authorization("r1", "t1"), "t1"),
                        Arrays.asList()
                    );

                    put(
                        new AuthorationEntityPack(new Authorization("r2", "t1"), "t1"),
                        Arrays.asList(
                        )
                    );
                }},
                Arrays.asList(
                    new Field("t1", "c1"),
                    new Field("t1", "c2")
                )
            )
        );

        return data;
    }

    private static class Pack {
        private String sql;
        private Map<AuthorationEntityPack, List<FieldRule>> rules;
        private List<Field> expectedFields;

        public Pack(String sql, Map<AuthorationEntityPack, List<FieldRule>> rules, List<Field> expectedFields) {
            this.sql = sql;
            this.rules = rules;
            this.expectedFields = expectedFields;
        }
    }

    private static class AuthorationEntityPack {
        private Authorization authorization;
        private String entity;

        public AuthorationEntityPack(Authorization authorization, String entity) {
            this.authorization = authorization;
            this.entity = entity;
        }
    }
} 
