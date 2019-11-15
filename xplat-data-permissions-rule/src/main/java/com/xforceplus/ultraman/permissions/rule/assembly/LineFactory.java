package com.xforceplus.ultraman.permissions.rule.assembly;

import com.xforceplus.ultraman.permissions.sql.Sql;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 0.1 2019/11/14 11:33
 * @author dongbin
 * @since 1.8
 */
public class LineFactory {

    private Line unsupport = new UnsupportLine();

    private List<Line> lines;

    public LineFactory(List<Line> lines) {
        this.lines = lines;
    }

    public Line getLine(Sql sql) {
        for (Line line : lines) {
            if (line.isSupport(sql)) {
                return line;
            }
        }

        return unsupport;
    }
}
