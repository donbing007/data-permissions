package com.xforceplus.ultraman.permissions.rule.assembly;

import com.xforceplus.ultraman.permissions.rule.check.Checker;
import com.xforceplus.ultraman.permissions.rule.context.Context;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义了流水线执行的方式和异常处理.
 *
 * @version 0.1 2019/11/13 12:12
 * @author dongbin
 * @since 1.8
 */
public abstract class AbstractLine implements Line {

    private List<Checker> selectCheckers;

    public AbstractLine(List<Checker> checkers) {
        List<Class<? extends Checker>> selected = selectChecker();
        selectCheckers = new ArrayList<>(selected.size());

        boolean found;
        for (Class<? extends Checker> clazz : selected) {
            found = false;
            for (Checker checker : checkers) {
                if (clazz.equals(checker.getClass())) {
                    selectCheckers.add(checker);
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalStateException("There is no proper Checker![" + clazz.toString() + "]");
            }
        }
    }

    @Override
    public void start(Context context) throws Throwable {
        if (!context.isAnauthorized()) {
            context.refused("Unauthorized access.");
            return;
        }

        for (Checker checker : selectCheckers) {
            checker.check(context);

            if (context.isRefused()) {
                return;
            }
        }
    }

    public List<Checker> getSelectCheckers() {
        return selectCheckers;
    }

    protected abstract List<Class<? extends Checker>> selectChecker();
}
