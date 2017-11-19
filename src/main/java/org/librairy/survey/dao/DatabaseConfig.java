package org.librairy.survey.dao;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class DatabaseConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("#{environment['LIBRAIRY_SURVEY_DB_HOSTS']?:'${librairy.survey.db.hosts}'}")
    String hosts;

    @Value("#{environment['LIBRAIRY_SURVEY_DB_PORT']?:${librairy.survey.db.port}}")
    Integer port;

    @Autowired
    List<AbstractDao> daos;

    private Cluster cluster;
    private Session session;

    private void initialize(){
        LOG.info("Trying to connect to database at: " + hosts + " : " + port + " ...");
        cluster = Cluster.builder().addContactPoints(StringUtils.split(hosts,",")).withPort(port).build();
        session = cluster.connect();
        session.execute("create keyspace if not exists survey with replication = { 'class' : 'SimpleStrategy' , 'replication_factor' : 1 } and durable_writes = true;");
        session.execute("use survey");
        LOG.info("Connection established. Initializing column families..");
        daos.forEach(dao -> dao.initialize(session));
        LOG.info("All tables initialized");
    }


    public Session getSession(){
        if (session == null) initialize();
        return session;
    }

    public Cluster getCluster(){
        if (cluster == null) initialize();
        return cluster;
    }

    @PreDestroy
    public void close(){
        LOG.info("Disconnecting from database ..");
        if (session != null) session.close();
        if (cluster != null) cluster.close();
        LOG.info("Database disconnected");
    }
}
