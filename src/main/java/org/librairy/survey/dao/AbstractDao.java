package org.librairy.survey.dao;

import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public abstract class AbstractDao {

    @Autowired
    protected DatabaseConfig database;

    abstract boolean initialize(Session session);
}
