package io.swagger.api;

import io.swagger.model.Question;
import io.swagger.model.Survey;
import org.librairy.survey.service.SurveyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-11-22T13:07:43.858Z")

@Controller
public class QuestionsApiController implements QuestionsApi {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionsApiController.class);

    @Autowired
    SurveyService service;

    @Override
    public ResponseEntity<String> questionsGet() {
        String json = service.getSurvey().toJson();
        return new ResponseEntity<String>(json, HttpStatus.OK);
    }
}
