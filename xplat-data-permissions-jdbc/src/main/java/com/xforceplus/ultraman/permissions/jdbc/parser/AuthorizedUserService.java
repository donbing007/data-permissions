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
}
