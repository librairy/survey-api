/*
 * Copyright (c) 2016. Universidad Politecnica de Madrid
 *
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 *
 */

package org.librairy.survey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created on 21/05/16:
 *
 * @author cbadenes
 */
@SpringBootApplication
@EnableSwagger2
@ComponentScan({
        "io.swagger",
        "org.librairy.survey"
})
//@PropertySource({"classpath:boot.properties"})
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    static int port = 8080;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public static EmbeddedServletContainerFactory getTomcatEmbeddedFactory(){
        TomcatEmbeddedServletContainerFactory servlet = new TomcatEmbeddedServletContainerFactory();
        servlet.setPort(port);
        return servlet;
    }

    public static void main(String[] args){
        try {

            if (args != null && args.length > 0){

                port = Integer.valueOf(args[0]);
            }

            ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

            LOG.info("  ٩(͡๏̯͡๏)۶  librairy survey is up and listening on port: " + port + "!!");


        } catch (Exception e) {
            LOG.error("Error executing test",e);
            System.exit(-1);
        }

    }
}