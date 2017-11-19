package org.librairy.survey.dao;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import io.swagger.model.Question;
import io.swagger.model.QuestionCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class QuestionsDao extends AbstractDao {

    @Autowired
    QuestionCounterDao questionCounterDao;

    @Override
    public boolean initialize(Session session) {
        session.execute("create table if not exists questions(id text, json text, time text, primary key (id)) WITH gc_grace_seconds = '3600' and compaction = { 'class' :  'LeveledCompactionStrategy'  } and compression = { 'sstable_compression' : '' } and caching = { 'keys' : 'ALL', 'rows_per_partition' : '120' };");
        return true;
    }


    public List<Question> getTopQuestions(Integer num){
        List<QuestionCounter> questions = new ArrayList<>();
        Integer size                    = 100;
        Optional<String> offset         = Optional.empty();
        Boolean completed               = false;

        while (!completed){
            StringBuilder query = new StringBuilder().append("select id from questions");
            if (offset.isPresent()) query.append(" where token(id) > token('"+offset.get()+"')");
            query.append(" limit ").append(size).append(";");

            ResultSet result = database.getSession().execute(query.toString());
            List<Row> rows = result.all();

            if (rows == null) return new ArrayList<>();

            completed = rows.size() < size;

            rows.forEach( r -> questions.add(new QuestionCounter(r.getString(0), questionCounterDao.getValue(r.getString(0)))));

        }

        return questions.stream().sorted( (a,b) -> a.getAnswered().compareTo(b.getAnswered())).limit(num).map(qc -> getQuestion(qc.getId())).collect(Collectors.toList());
    }

    public List<Question> getRandomQuestions(Integer num){

        List<Question> questions = new ArrayList<>();
        ResultSet result = database.getSession().execute("select id, json from questions limit " + num + ";");
        List<Row> elements = result.all();
        if (elements == null) return new ArrayList<>();
        return elements.stream().map(row -> new Question(row.getString(0), row.getString(1))).collect(Collectors.toList());
    }

    public List<Question> getQuestions(Integer num, Optional<String> offset){
        StringBuilder query = new StringBuilder().append("select id, json from questions");
        if (offset.isPresent()) query.append(" where token(id) > token('"+offset.get()+"')");
        query.append(" limit ").append(num).append(";");

        ResultSet result = database.getSession().execute(query.toString());
        List<Row> rows = result.all();

        if (rows == null) return new ArrayList<>();

        return rows.stream().map(row -> new Question(row.getString(0),row.getString(1))).collect(Collectors.toList());
    }

    public Question getQuestion(String id){
        PreparedStatement query = database.getSession().prepare("select json from questions where id = ?");
        ResultSet result = database.getSession().execute(query.bind(id));
        return new Question(id,result.one().getString(0));
    }


}
