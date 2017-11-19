package org.librairy.survey.questions;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import io.swagger.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public class LoadQuestions {

    private static final Logger LOG = LoggerFactory.getLogger(LoadQuestions.class);

    public static void main (String[] args){


        Cluster cluster = Cluster.builder().addContactPoints("localhost").withPort(9042).build();
        Session session = cluster.connect();
        session.execute("use survey;");

        List<Question> questions = Arrays.asList(new Question[]{
           new Question("b1b2", "{\n" +
                   "      \"type\": \"panel\",\n" +
                   "      \"innerIndent\": 1,\n" +
                   "      \"name\": \"pb1b2\",\n" +
                   "      \"title\": \"B1 <-> B2\",\n" +
                   "      \"elements\": [\n" +
                   "        {\n" +
                   "          \"type\": \"html\",\n" +
                   "          \"name\": \"hb1b2\",\n" +
                   "          \"html\": \"<table><body><row><td><img src='http://bit.ly/2zMxn2P' width='100px' /></td><td style='padding:20px'>You may put here any html code. For example images, <b>text</b> or <a href='#'  target='_blank'>links</a></td><td><img src='http://bit.ly/2A6nX30' width='100px' /></td><td style='padding:20px'>You may put here any html code. For example images, <b>text</b> or <a href='#'  target='_blank'>links</a></td></row></body></table>\"\n" +
                   "        },\n" +
                   "        {\n" +
                   "          \"type\": \"radiogroup\",\n" +
                   "          \"name\": \"b1b2\",\n" +
                   "          \"title\": \"Are these books related?\",\n" +
                   "          \"isRequired\": true,\n" +
                   "          \"colCount\": 1,\n" +
                   "          \"choices\": [\n" +
                   "            \"yes\",\n" +
                   "            \"no\"\n" +
                   "          ]\n" +
                   "        }\n" +
                   "      ]\n" +
                   "    }")
        });



        questions.forEach( q -> {
            PreparedStatement statement = session.prepare("insert into questions ( id, json, time) values (?, ?, ?)");
            session.executeAsync(statement.bind(q.getId(), q.getJson(), q.getTime()));
            LOG.info("saved question '" + q.getId()+"'");
        });

        session.close();

        cluster.close();

    }

}
