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
public class QuestionCounterDao extends AbstractDao {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionCounterDao.class);

    @Override
    public boolean initialize(Session session) {
        session.execute("create table if not exists counters_by_question(question text, answered counter, primary key(question)) WITH gc_grace_seconds = '3600' and compaction = { 'class' :  'LeveledCompactionStrategy'  } and compression = { 'sstable_compression' : '' } and caching = { 'keys' : 'ALL', 'rows_per_partition' : '120' };");
        return true;
    }

    public Long getValue(String question){
        PreparedStatement statement = database.getSession().prepare("select answered from counters_by_question where question = ?");
        ResultSet result = database.getSession().execute(statement.bind(question));
        Row row = result.one();
        if (row == null) return 0l;
        return row.getLong(0);

    }

    public void increment(String question){
        LOG.info("+1 to question '" + question + "'");
        PreparedStatement statement = database.getSession().prepare("update counters_by_question set answered = answered + 1 where question = ?");
        database.getSession().executeAsync(statement.bind(question));
    }

    public void decrement(String question){
        LOG.info("-1 to question '" + question + "'");
        PreparedStatement statement = database.getSession().prepare("update counters_by_question set answered = answered - 1 where question = ?");
        database.getSession().executeAsync(statement.bind(question));
    }

    public boolean exists(String question){
        PreparedStatement statement = database.getSession().prepare("select answered from counters_by_question where question = ?");
        ResultSet result = database.getSession().execute(statement.bind(question));
        return result.one() != null;
    }


}
