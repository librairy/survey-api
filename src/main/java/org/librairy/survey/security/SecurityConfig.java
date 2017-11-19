package org.librairy.survey.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.StringTokenizer;

import static org.springframework.security.web.access.channel.ChannelDecisionManagerImpl.ANY_CHANNEL;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("#{environment['LIBRAIRY_SURVEY_USERS']?:'${librairy.survey.users}'}")
    String users;


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().anyRequest().fullyAuthenticated();
//        http.httpBasic();
//        http.csrf().disable();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requiresChannel().antMatchers("/*").requires(ANY_CHANNEL).and()
                .authorizeRequests().antMatchers("/survey/surveys/**").permitAll()
                .antMatchers("/survey/questions").permitAll()
                .antMatchers("/survey/**").permitAll()
                .antMatchers("/**").permitAll().and()
                .addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
                .csrf().disable();
    }

    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // you USUALLY want this
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("OPTIONS"); // I added this in a second phase, but nothing changes
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//
//        UserDetailsManagerConfigurer.UserDetailsBuilder userDetails = auth.inMemoryAuthentication()
//                .withUser("librairy").password("l1brA1ry").roles("ADMIN");
//
//
//        StringTokenizer tokenizer = new StringTokenizer(users,";");
//
//        while(tokenizer.hasMoreTokens()){
//            String user = tokenizer.nextToken();
//            String name = StringUtils.substringBefore(user,":");
//            String pwd = StringUtils.substringAfter(user,":");
//            userDetails = userDetails.and().withUser(name).password(pwd).roles("USER");
//            LOG.info("Added user: " + user);
//
//        }
//    }



}
