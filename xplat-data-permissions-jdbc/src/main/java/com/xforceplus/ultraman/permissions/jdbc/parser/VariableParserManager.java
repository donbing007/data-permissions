package com.xforceplus.ultraman.permissions.jdbc.parser;

import com.google.common.collect.Maps;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 2020/9/8 6:24 PM
 */
public class VariableParserManager implements InitializingBean, ApplicationContextAware {

    private static Map<String, VariableParser> registedParser = Maps.newConcurrentMap();

    private ApplicationContext applicationContext;

    private static void registVariableParser(VariableParser parser) {
        registedParser.put(parser.getName(), parser);
    }

    public String parse(String sql) {
        String newSql = sql;
        for (String key : registedParser.keySet()) {
            if (newSql.contains(key)) {
                VariableParser parser = registedParser.get(key);
                newSql = parser.parse(newSql);
            }
        }
        return newSql;
    }

    @Override
    public void afterPropertiesSet() {
        applicationContext.getBeansOfType(VariableParser.class).entrySet().stream().forEach(entry -> {
            registVariableParser(entry.getValue());
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
