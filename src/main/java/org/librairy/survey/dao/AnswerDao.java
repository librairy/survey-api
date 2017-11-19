package org.librairy.survey.dao;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import io.swagger.model.Answer;
import io.swagger.model.QuestionCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class AnswerDao extends AbstractDao {

    private static final Logger LOG = LoggerFactory.getLogger(AnswerDao.class);

    @Autowired
    UserCounterDao userCounterDao;

    @Autowired
    QuestionCounterDao questionCounterDao;

    @Override
    public boolean initialize(Session session) {
        session.execute("create table if not exists answers(user text, question text, value text, time text, primary key ((user),question)) WITH gc_grace_seconds = '3600' and compaction = { 'class' :  'LeveledCompactionStrategy'  } and compression = { 'sstable_compression' : '' } and caching = { 'keys' : 'ALL', 'rows_per_partition' : '120' };");
        return false;
    }


    public boolean save(Answer answer){
        LOG.info("Saving answer: " + answer);

        boolean alreadyExists = exists(answer);

        PreparedStatement statement = database.getSession().prepare("insert into answers (user, question, value, time) values (?, ?, ?, ?)");
        database.getSession().executeAsync(statement.bind(answer.getUser(), answer.getQuestion(), answer.getResult(), answer.getTime()));

        if (!alreadyExists){
            // increment question counter
            userCounterDao.increment(answer.getUser());

            // increment user counter
            questionCounterDao.increment(answer.getQuestion());
        }

        LOG.info("Answer '"+answer.getQuestion()+"' saved");
        return true;
    }




    public boolean exists(Answer answer){
        PreparedStatement statement = database.getSession().prepare("select time from answers where user = ? and question = ?");
        ResultSet result = database.getSession().execute(statement.bind(answer.getUser(), answer.getQuestion()));
        return result.one() != null;

    }
}
