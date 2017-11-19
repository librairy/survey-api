package org.librairy.survey.bb.tasks;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import org.librairy.survey.bb.data.PairCandidate;
import org.librairy.survey.bb.service.PairsFromGoogleDocAsCSV;
import org.librairy.survey.questions.QuestionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public class LoadQuestionsToSurvey {

    private static final Logger LOG = LoggerFactory.getLogger(LoadQuestionsToSurvey.class);

    public static void main(String[] args) throws IOException {

        String googleDocFilePath = "src/test/resources/Recomendaciones JUMP - Gold Standard - Gold Standard.tsv";

        PairsFromGoogleDocAsCSV service = new PairsFromGoogleDocAsCSV();

        int sampleSize = 100;
        List<PairCandidate> pairs = service.createCandidates(googleDocFilePath,sampleSize);

        System.out.println("Total number of pairs: " + pairs.size());


        Cluster cluster = Cluster.builder().addContactPoints("wiig.dia.fi.upm.es").withPort(9542).build();
        Session session = cluster.connect();
        session.execute("use survey;");


        pairs.stream().map(p -> new QuestionTemplate(p).getQuestion()).forEach( q -> {
            PreparedStatement statement = session.prepare("insert into questions ( id, json, time) values (?, ?, ?)");
            session.executeAsync(statement.bind(q.getId(), q.getJson(), q.getTime()));
            LOG.info("saved question '" + q.getId()+"'");
        });

        session.close();

        cluster.close();


    }

}
