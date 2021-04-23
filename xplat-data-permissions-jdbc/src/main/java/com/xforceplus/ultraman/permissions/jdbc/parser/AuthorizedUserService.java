package com.xforceplus.ultraman.permissions.jdbc.parser;


import java.util.Set;

/**
 * AuthorizedUserService
 */
public interface AuthorizedUserService {

    /**
     * get user's tax no collection
     *
     * @param user
     * @return
     */
    Set<String> getUserTaxNums(Long user);


    /**
     * get user's company id collection
     * @param user
     * @return
     */
    Set<Long> getUserCompanyIds(Long user);


    /**
     * Get user's org id collection
     * @param userId
     * @return
     */
    Set<Long> getUserOrgIds(Long userId);
}
