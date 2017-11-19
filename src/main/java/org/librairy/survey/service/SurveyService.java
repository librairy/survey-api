package org.librairy.survey.service;

import io.swagger.model.Answer;
import io.swagger.model.Question;
import io.swagger.model.Survey;
import org.librairy.survey.dao.AnswerDao;
import org.librairy.survey.dao.QuestionsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Component
public class SurveyService {

    private static final Logger LOG = LoggerFactory.getLogger(SurveyService.class);

    @Value("#{environment['LIBRAIRY_SURVEY_MAX_QUESTIONS']?:${librairy.survey.max.questions}}")
    Integer maxQuestions;

    @Autowired
    QuestionsDao questionsDao;

    @Autowired
    AnswerDao answerDao;

    Optional<String> lastQuestion = Optional.empty();

    public Survey getSurvey(){
        LOG.info("Composing a new survey from " + maxQuestions + " questions less answered ..");
        Survey survey = new Survey();
        List<Question> questions = questionsDao.getQuestions(maxQuestions,lastQuestion);
        if (lastQuestion.isPresent() && questions.size() < maxQuestions){
            lastQuestion = Optional.empty();
        }
        if (questions.size()>0) lastQuestion = Optional.of(questions.get(questions.size()-1).getId());
        questions.forEach(q -> survey.addQuestionElement(q.getJson()));
        LOG.info("Survey ready with questions: " + questions.stream().map(q -> q.getId()).collect(Collectors.joining(",")));
        return survey;
    }

    public boolean saveAnswer(Answer answer){
        return answerDao.save(answer);
    }

}
