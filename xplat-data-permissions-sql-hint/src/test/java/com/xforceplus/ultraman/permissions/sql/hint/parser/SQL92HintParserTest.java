package com.xforceplus.ultraman.permissions.sql.hint.parser;

import com.xforceplus.ultraman.permissions.sql.hint.Hint;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * SQL92HintParser Tester.
 *
 * @author <Authors name>
 * @version 1.0 06/11/2020
 * @since <pre>Jun 11, 2020</pre>
 */
public class SQL92HintParserTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void test() throws Exception {
        SQL92HintParser parser = new SQL92HintParser();
        buildCases().stream().forEach(c -> {
            Hint hint = null;
            try {
                hint = parser.parse(c.target);
            } catch (SQLException e) {
                Assert.assertTrue(c.exception);
            }

            if (!c.exception) {
                Assert.assertEquals(c.expectHint, hint);
            }
        });
    }

    private Collection<Case> buildCases() {
        return Arrays.asList(
            new Case(
                "select t.id from table t /* XDP:HINT ignore=true */",
                () -> {
                    Hint hint = new Hint();
                    hint.setIgnore(true);
                    return hint;
                },
                false
            )
            ,
            new Case(
                "/* XDP:HINT ignore=true */select t.id from table t",
                () -> {
                    Hint hint = new Hint();
                    hint.setIgnore(true);
                    return hint;
                },
                false
            )
            ,
            new Case(
                "select t.id /* XDP:HINT ignore=true */ from table t",
                () -> {
                    Hint hint = new Hint();
                    hint.setIgnore(true);
                    return hint;
                },
                false
            )
            ,
            new Case(
                "select t.id /* XDP:HINT ignore=true from table t",
                () -> {
                    Hint hint = new Hint();
                    hint.setIgnore(false);
                    return hint;
                },
                false
            )
            ,
            new Case(
                "select t.id /* XDP:HINT =true */ from table t",
                () -> {
                    Hint hint = new Hint();
                    hint.setIgnore(false);
                    return hint;
                },
                true
            )
            ,
            new Case(
                "select t.id /* XDP:HINT ignore= */ from table t",
                () -> {
                    Hint hint = new Hint();
                    hint.setIgnore(false);
                    return hint;
                },
                true
            )
            ,
            new Case(
                "select t.id /* XDP:HINT ignore=true */ from table t",
                () -> {
                    Hint hint = new Hint();
                    hint.setIgnore(true);
                    return hint;
                },
                false
            )
            ,
            new Case(
                "select t.id /* XDP:HINT ignore=false */ from table t /* XDP:HINT ignore=true */",
                () -> {
                    Hint hint = new Hint();
                    hint.setIgnore(true);
                    return hint;
                },
                false
            )
            ,
            new Case(
                "select t.id /* TDDL:HINT ignore=false */ from table t",
                () -> {
                    Hint hint = new Hint();
                    hint.setIgnore(false);
                    return hint;
                },
                false
            )
            ,
            new Case(
                "select t.id /* TDDL:HINT ignore=false */ from table t /* XDP:HINT ignore=true */",
                () -> {
                    Hint hint = new Hint();
                    hint.setIgnore(true);
                    return hint;
                },
                false
            )
        );
    }

    private static class Case {
        public String target;
        public Hint expectHint;
        public boolean exception;

        public Case(String target, Supplier<Hint> builder, boolean exception) {
            this.target = target;
            this.expectHint = builder.get();
            this.exception = exception;
        }
    }

} 
