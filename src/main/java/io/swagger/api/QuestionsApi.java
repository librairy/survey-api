package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.model.Survey;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-11-22T13:07:43.858Z")

@Api(value = "questions", description = "available surveys", tags = "/questions", position = 1)
public interface QuestionsApi {

    @ApiOperation(value = "", notes = "Get `Question`s of a `Survey`. ", response = String.class, tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "questions of a survey", response = String.class)})
    @RequestMapping(value = "/questions",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<String> questionsGet();

}
