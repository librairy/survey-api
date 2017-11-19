package io.swagger.api;

import io.swagger.annotations.*;
import io.swagger.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-11-22T13:07:43.858Z")

@Api(value = "answers", description = "answers of a survey", tags = "/answers", position = 1)
public interface AnswersApi {

    @ApiOperation(value = "", notes = "Add a set of  `Answer`s to a `Survey`. ", response = String.class, tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "answer added to survey", response = String.class) })
    @RequestMapping(value = "/answers",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<String> answersPost(
            @ApiParam(value = "answer of the survey" ,required=true ) @RequestBody Map<String, Object> answer);

}
