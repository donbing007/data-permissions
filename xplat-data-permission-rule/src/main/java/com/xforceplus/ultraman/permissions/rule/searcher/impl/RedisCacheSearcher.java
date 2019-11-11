package com.xforceplus.ultraman.permissions.rule.searcher.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xforceplus.ultraman.perissions.pojo.Authorization;
import com.xforceplus.ultraman.perissions.pojo.EntityInfo;
import com.xforceplus.ultraman.perissions.pojo.rule.*;
import com.xforceplus.ultraman.permissions.repository.ScopeSelectRepository;
import com.xforceplus.ultraman.permissions.repository.entity.DataScopeSubCondition;
import com.xforceplus.ultraman.permissions.repository.entity.FieldScope;
import com.xforceplus.ultraman.permissions.repository.entity.SelectDataScopeExample;
import com.xforceplus.ultraman.permissions.repository.entity.SelectFieldScopeExample;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 使用 redis 进行缓存的规则信息搜索器.
 * @version 0.1 2019/11/6 16:22
 * @auth dongbin
 * @since 1.8
 */
public class RedisCacheSearcher implements Searcher {

    private static final String FIELD_RULE_KEY_PREFIX = "rule.field";
    private static final String DATA_RULE_KEY_PREFIX = "rule.data";

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ScopeSelectRepository scopeSelectRepository;

    @Override
    public List<FieldRule> searchFieldRule(Authorization auth, String entity) {

        EntityInfo entityInfo = new EntityInfo(entity, null);

        List<FieldRule> rules = searchCache(auth, entityInfo, true);
        if (rules == null) {
            rules = loadFieldRuleFromDb(auth, entityInfo);
        }

        return rules;
    }


    @Override
    public List<DataRule> searchDataRule(Authorization auth, EntityInfo entity) {
        List<DataRule> rules = searchCache(auth, entity, false);
        if (rules == null) {
            rules = loadDataRuleFromDb(auth, entity);
        }

        return rules;
    }


    private <T> List<T> searchCache(Authorization auth, EntityInfo entity, boolean field) {
        String cacheKey = buildCacheKey(field ? FIELD_RULE_KEY_PREFIX : DATA_RULE_KEY_PREFIX ,auth, entity);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String cacheValue = ops.get(cacheKey);
        if (cacheValue == null) {
            return null;
        } else {

            return unserialize(cacheValue, field);
        }
    }

    private void saveToCache(Authorization auth, EntityInfo entity, boolean field, List value) {
        String cacheKey = buildCacheKey(field ? FIELD_RULE_KEY_PREFIX : DATA_RULE_KEY_PREFIX ,auth, entity);
        String serializeValue = serialize(value);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(cacheKey, serializeValue);
    }

    // 查询没有需要返回空列表.
    private List<FieldRule> loadFieldRuleFromDb(Authorization auth, EntityInfo entity) {
        SelectFieldScopeExample example = new SelectFieldScopeExample();
        example.setRoleId(auth.getRole());
        example.setTenantId(auth.getTenant());
        example.setEntity(entity.getEntity());
        List<FieldScope> scopes = scopeSelectRepository.selectFieldScopeByExample(example);

        List<FieldRule> filterRules = scopes.parallelStream().map(scope -> new FieldRule(scope.getEntity(), scope.getField()))
            .collect(Collectors.toList());

        saveToCache(auth, entity, true, filterRules);

        return filterRules;
    }

    // 查询没有需要返回空列表.
    private List<DataRule> loadDataRuleFromDb(Authorization auth, EntityInfo entity) {
        SelectDataScopeExample example = new SelectDataScopeExample();
        example.setRoleId(auth.getRole());
        example.setTenantId(auth.getTenant());
        example.setEntity(entity.getEntity());
        example.setField(entity.getField());
        List<DataScopeSubCondition> conditions = scopeSelectRepository.selectDataScopeConditionsByExample(example);
        List<DataRuleCondition> dataRuleConditions = conditions.parallelStream().map(c -> {

            DataRuleCondition ruleCondition = new DataRuleCondition();
            ruleCondition.setType(RuleConditionValueType.getInstance(Math.toIntExact(c.getValueTypeId())));
            ruleCondition.setLink(RuleConditionRelationship.getInstance(c.getLink()));
            ruleCondition.setOperation(RuleConditionOperation.getInstance(c.getOperation()));
            ruleCondition.setValue(c.getValue());
            return ruleCondition;

        }).collect(Collectors.toList());

        List<DataRule> filterRules = Arrays.asList(new DataRule(entity.getEntity(), entity.getField(), dataRuleConditions));
        saveToCache(auth, entity, false, filterRules);

        return filterRules;
    }

    private String serialize(List value) {
        return JSON.toJSONString(value);
    }

    private <T> List<T> unserialize(String value, boolean field) {
        if (field) {
            return (List<T>) JSON.parseObject(value, new TypeReference<List<FieldRule>>(){});
        } else {
            return (List<T>) JSON.parseObject(value, new TypeReference<List<DataRule>>(){});
        }
    }

    private String buildCacheKey(String prefix, Authorization auth, EntityInfo entity) {
        StringBuilder key = new StringBuilder();
        key.append(prefix)
            .append(":")
            .append(auth.getTenant())
            .append(".")
            .append(auth.getRole())
            .append(".")
            .append(entity.toString());
        return key.toString();
    }
}
