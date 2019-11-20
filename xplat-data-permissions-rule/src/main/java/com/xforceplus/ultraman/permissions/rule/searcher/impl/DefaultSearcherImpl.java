package com.xforceplus.ultraman.permissions.rule.searcher.impl;

import com.xforceplus.ultraman.perissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.perissions.pojo.rule.*;
import com.xforceplus.ultraman.permissions.repository.ScopeSelectRepository;
import com.xforceplus.ultraman.permissions.repository.entity.DataScopeSubCondition;
import com.xforceplus.ultraman.permissions.repository.entity.FieldScope;
import com.xforceplus.ultraman.permissions.repository.entity.SelectScopeExample;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 默认规则搜索器实现.
 *
 * @author dongbin
 * @version 0.1 2019/11/6 16:22
 * @since 1.8
 */
@CacheConfig(cacheNames = "rule")
public class DefaultSearcherImpl implements Searcher {

    @Resource
    private ScopeSelectRepository scopeSelectRepository;

    @Cacheable(key = "'rule-field-' + #p0.tenant + '-' + #p0.role + '-' + #entity")
    @Override
    public List<FieldRule> searchFieldRule(Authorization auth, String entity) {

        return loadFieldRuleFromDb(auth, entity);
    }

    @Cacheable(key = "'rule-data-' + #p0.tenant + '-' + #p0.role + '-' + #entity")
    @Override
    public List<DataRule> searchDataRule(Authorization auth, String entity) {
        return loadDataRuleFromDb(auth, entity);
    }


    // 查询没有需要返回空列表.
    private List<FieldRule> loadFieldRuleFromDb(Authorization auth, String entity) {
        SelectScopeExample example = new SelectScopeExample();
        example.setRoleId(auth.getRole());
        example.setTenantId(auth.getTenant());
        example.setEntity(entity);
        List<FieldScope> scopes = scopeSelectRepository.selectFieldScopeByExample(example);

        return scopes.parallelStream().map(
            scope -> new FieldRule(scope.getEntity(), scope.getField())).collect(Collectors.toList());
    }

    // 查询没有需要返回空列表.
    private List<DataRule> loadDataRuleFromDb(Authorization auth, String entity) {
        SelectScopeExample example = new SelectScopeExample();
        example.setRoleId(auth.getRole());
        example.setTenantId(auth.getTenant());
        example.setEntity(entity);
        List<DataScopeSubCondition> conditions = scopeSelectRepository.selectDataScopeConditionsByExample(example);

        // field 为 key.
        Map<String, DataRule> buffer = new HashMap();
        conditions.stream().forEach(c -> {

            DataRuleCondition ruleCondition = new DataRuleCondition();
            ruleCondition.setType(RuleConditionValueType.getInstance(Math.toIntExact(c.getValueTypeId())));
            ruleCondition.setLink(RuleConditionRelationship.getInstance(c.getLink()));
            ruleCondition.setOperation(RuleConditionOperation.getInstance(c.getOperation()));
            ruleCondition.setValue(c.getValue());

            DataRule rule = buffer.get(c.getField());
            if (rule == null) {
                rule = new DataRule(entity, c.getField());
                buffer.put(c.getField(), rule);
            }

            rule.addDataRuleCondition(ruleCondition);

        });

        return new ArrayList<>(buffer.values());
    }
}
