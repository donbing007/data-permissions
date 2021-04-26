package com.xforceplus.ultraman.permissions.jdbc.parser.http.dto;

import java.util.List;

/**
 * 项目名称: 票易通
 * JDK 版本: JDK1.8
 * 说明:
 * 作者(@author): liwei
 * 创建时间: 4/26/21 7:49 PM
 */
public class OrgResult {
    int size;
    List<OrgInfo> content;
    long totalPages;
    boolean last;

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<OrgInfo> getContent() {
        return content;
    }

    public void setContent(List<OrgInfo> content) {
        this.content = content;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }
}
