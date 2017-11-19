package org.librairy.survey.dao;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class UserCounterDao extends AbstractDao {

    private static final Logger LOG = LoggerFactory.getLogger(UserCounterDao.class);

    @Override
    public boolean initialize(Session session) {
        session.execute("create table if not exists counters_by_user(user text, answered counter, primary key(user)) WITH gc_grace_seconds = '3600' and compaction = { 'class' :  'LeveledCompactionStrategy'  } and compression = { 'sstable_compression' : '' } and caching = { 'keys' : 'ALL', 'rows_per_partition' : '120' };");
        return true;
    }

    public Long getValue(String user){
        PreparedStatement statement = database.getSession().prepare("select answered from counters_by_user where user = ?");
        ResultSet result = database.getSession().execute(statement.bind(user));
        Row row = result.one();
        if (row == null) return 0l;
        return row.getLong(0);

    }

    public void increment(String user){
        LOG.info("+1 to user '" + user + "'");
        PreparedStatement statement = database.getSession().prepare("update counters_by_user set answered = answered + 1 where user = ?");
        database.getSession().executeAsync(statement.bind(user));
    }

    public void decrement(String user){
        LOG.info("-1 to user '" + user + "'");
        PreparedStatement statement = database.getSession().prepare("update counters_by_user set answered = answered - 1 where user = ?");
        database.getSession().executeAsync(statement.bind(user));
    }

    public boolean exists(String user){
        PreparedStatement statement = database.getSession().prepare("select answered from counters_by_user where user = ?");
        ResultSet result = database.getSession().execute(statement.bind(user));
        return result.one() != null;
    }


}
