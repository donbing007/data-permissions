package com.xforceplus.ultraman.permissions.pojo.auth;

import java.io.Serializable;
import java.util.*;

/**
 * @version 0.1 2019/11/19 19:58
 * @author dongbin
 * @since 1.8
 */
public class Authorizations implements Serializable {

    private Set<Authorization> authorizations;

    public Authorizations() {
        this(Collections.emptyList());
    }

    public Authorizations(Authorization authorization) {
        this(Arrays.asList(authorization));
    }

    public Authorizations(List<Authorization> authorizations) {
        this.authorizations = new HashSet(authorizations);
    }

    public void add(Authorization authorization) {
        this.authorizations.add(authorization);
    }

    public Set<Authorization> getAuthorizations() {
        return authorizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Authorizations)) return false;
        Authorizations that = (Authorizations) o;
        return Objects.equals(getAuthorizations(), that.getAuthorizations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthorizations());
    }

    @Override
    public String toString() {
        return "Authorizations{" +
            "authorizations=" + authorizations +
            '}';
    }
}
