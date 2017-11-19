package io.swagger.api;

import io.swagger.annotations.ApiParam;
import io.swagger.model.Answer;
import org.librairy.survey.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-11-22T13:07:43.858Z")

@Controller
public class AnswersApiController implements AnswersApi {

    private static final Logger LOG = LoggerFactory.getLogger(AnswersApiController.class);

    @Autowired
    SurveyService service;

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");


    @Override
    public ResponseEntity<String> answersPost(@ApiParam(value = "answer of the survey", required = true) @RequestBody Map<String, Object> answer) {
        LOG.info("New Answers received");

        String email = (String) answer.get("email");
        if(!isValidEmail(email)){
            LOG.warn("Email is not valid:" + email);
            return new ResponseEntity<String>("{}",HttpStatus.BAD_REQUEST);
        }

        answer.entrySet().stream().forEach(entry -> {
            if (!entry.getKey().equalsIgnoreCase("email")){
                Answer a = new Answer();
                a.setUser(email);
                a.setTime(df.format(new Date()));
                a.setQuestion(entry.getKey());
                a.setResult((String) entry.getValue());
                a.setId(getMD5(a.getUser()+a.getQuestion()+a.getTime()));
                service.saveAnswer(a);
            }
        });
        return new ResponseEntity<String>("{}",HttpStatus.OK);
    }

    private boolean isValidEmail(String email) {
        boolean isValid = false;
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            isValid = true;
        } catch (AddressException e) {
            LOG.warn(e.getMessage());
        }
        return isValid;
    }


    private static String getMD5(String text){
        String id;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(text.getBytes(),0,text.length());
            id = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            id = UUID.randomUUID().toString();
            LOG.warn("Error calculating MD5 from text. UUID will be used: " + id);
        }
        return id;
    }


}
