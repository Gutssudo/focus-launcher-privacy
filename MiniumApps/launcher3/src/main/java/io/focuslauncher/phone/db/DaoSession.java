package io.focuslauncher.phone.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import io.focuslauncher.phone.db.TableNotificationSms;

/**
 * DaoSession for GreenDAO database
 * Auto-generated stub for build compatibility
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig tableNotificationSmsDaoConfig;

    private final TableNotificationSmsDao tableNotificationSmsDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        tableNotificationSmsDaoConfig = daoConfigMap.get(TableNotificationSmsDao.class).clone();
        tableNotificationSmsDaoConfig.initIdentityScope(type);

        tableNotificationSmsDao = new TableNotificationSmsDao(tableNotificationSmsDaoConfig, this);

        registerDao(TableNotificationSms.class, tableNotificationSmsDao);
    }

    public void clear() {
        tableNotificationSmsDaoConfig.clearIdentityScope();
    }

    public TableNotificationSmsDao getTableNotificationSmsDao() {
        return tableNotificationSmsDao;
    }

}
