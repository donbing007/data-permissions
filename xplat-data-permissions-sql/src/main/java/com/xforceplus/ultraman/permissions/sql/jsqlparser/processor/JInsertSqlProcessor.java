package com.xforceplus.ultraman.permissions.sql.jsqlparser.processor;

import com.xforceplus.ultraman.permissions.sql.define.Field;
import com.xforceplus.ultraman.permissions.sql.define.From;
import com.xforceplus.ultraman.permissions.sql.define.Item;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability.JSqlParserFromAbility;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability.JSqlParserInsertItemAbility;
import com.xforceplus.ultraman.permissions.sql.jsqlparser.processor.ability.JSqlParserInsertValueAbility;
import com.xforceplus.ultraman.permissions.sql.processor.InsertSqlProcessor;
import com.xforceplus.ultraman.permissions.sql.processor.ProcessorException;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FieldFromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.InsertValueAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.FromAbility;
import com.xforceplus.ultraman.permissions.sql.processor.ability.InsertItemAbility;
import net.sf.jsqlparser.statement.Statement;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * insert
 *
 * @author dongbin
 * @version 0.1 2019/10/25 18:55
 * @since 1.8
 */
public class JInsertSqlProcessor extends JSqlProcessor implements InsertSqlProcessor {
    public JInsertSqlProcessor(Statement statement) {
        super(statement);
    }

    @Override
    public FromAbility buildFromAbility() {
        return new JSqlParserFromAbility(getStatement());
    }

    @Override
    public InsertValueAbility buildInsertValueAbility() {
        return new JSqlParserInsertValueAbility(getStatement());
    }

    @Override
    public InsertItemAbility buildInsertItemAbility() {
        return new JSqlParserInsertItemAbility(getStatement());
    }

    @Override
    public FieldFromAbility buildFieldFromAbility() {
        return item -> {
            FromAbility fromAbility = buildFromAbility();
            From from = fromAbility.list().stream().findFirst().get();


            return Arrays.asList(new AbstractMap.SimpleEntry<>((Field) item, from));
        };
    }

}
