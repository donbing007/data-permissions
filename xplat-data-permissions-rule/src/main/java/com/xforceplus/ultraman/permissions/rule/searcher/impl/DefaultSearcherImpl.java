package com.xforceplus.ultraman.permissions.rule.searcher.impl;

import com.xforceplus.ultraman.permissions.pojo.auth.Authorization;
import com.xforceplus.ultraman.permissions.pojo.rule.*;
import com.xforceplus.ultraman.permissions.repository.DataScopeSubConditionRepository;
import com.xforceplus.ultraman.permissions.repository.FieldScopeRepository;
import com.xforceplus.ultraman.permissions.repository.entity.DataScopeSubCondition;
import com.xforceplus.ultraman.permissions.repository.entity.DataScopeSubConditionExample;
import com.xforceplus.ultraman.permissions.repository.entity.FieldScope;
import com.xforceplus.ultraman.permissions.repository.entity.FieldScopeExample;
import com.xforceplus.ultraman.permissions.rule.searcher.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    final Logger logger = LoggerFactory.getLogger(DefaultSearcherImpl.class);

    @Resource
    private FieldScopeRepository fieldScopeRepository;

    @Resource
    private DataScopeSubConditionRepository dataScopeSubConditionRepository;


    @Cacheable(keyGenerator = "ruleSearchKeyGenerator")
    @Override
    public List<FieldRule> searchFieldRule(Authorization auth, String entity) {

        if (logger.isDebugEnabled()) {
            logger.debug("Loads the {} field rules in {}-{} from persistence.", entity, auth.getTenant(), auth.getRole());
        }
        FieldScopeExample example = new FieldScopeExample();
        example.createCriteria().andEntityEqualTo(entity)
            .andRoleEqualTo(auth.getRole())
            .andTenantEqualTo(auth.getTenant());
        example.setOrderByClause("id ASC");
        List<FieldScope> scopes = fieldScopeRepository.selectByExample(example);

        return scopes.stream().map(
            scope -> new FieldRule(scope.getEntity(), scope.getField())).collect(Collectors.toList());
    }

    @Cacheable(keyGenerator = "ruleSearchKeyGenerator")
    @Override
    public List<DataRule> searchDataRule(Authorization auth, String entity) {
        if (logger.isDebugEnabled()) {
            logger.debug("Loads the {} data rules in {}-{} from persistence.", entity, auth.getTenant(), auth.getRole());
        }

        DataScopeSubConditionExample example = new DataScopeSubConditionExample();
        example.createCriteria().andEntityEqualTo(entity)
            .andRoleEqualTo(auth.getRole())
            .andTenantEqualTo(auth.getTenant());
        example.setOrderByClause("conditions_id ASC, `index` ASC");

        List<DataScopeSubCondition> conditions = dataScopeSubConditionRepository.selectByExampleWithBLOBs(example);

        // field 为 key.
        Map<String, DataRule> buffer = new LinkedHashMap<>();

        conditions.stream().forEach(c -> {

            DataRuleCondition ruleCondition = new DataRuleCondition();
            ruleCondition.setType(RuleConditionValueType.getInstance(c.getValueType()));
            ruleCondition.setLink(RuleConditionRelationship.getInstance(c.getLink()));
            ruleCondition.setOperation(RuleConditionOperation.getInstance(c.getOperation()));
            ruleCondition.setValue(c.getValue());

            DataRule rule = buffer.get(c.getField());
            if (rule == null) {

                rule = new DataRule(entity, c.getField());
                rule.setId(c.getConditionsId());
                buffer.put(c.getField(), rule);

            }

            rule.addDataRuleCondition(ruleCondition);

        });

        return new ArrayList<>(buffer.values());
    }
}
