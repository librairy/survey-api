package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

/**
 * Reference
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-11-22T13:07:43.858Z")

@Data
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Answer {

    //{"b1b2":"Yes","b1b3":"Yes","email":"sample"}

    @JsonProperty("id")
    private String id= null;

    @JsonProperty("user")
    private String user = null;

    @JsonProperty("question")
    private String question = null;

    @JsonProperty("result")
    private String result = null;

    @JsonProperty("time")
    private String time = null;

    public Answer id(String id) {
        this.id = id;
        return this;
    }


    public Answer user(String user) {
      this.user = user;
        return this;
    }

    public Answer question(String question) {
        this.question = question;
        return this;
    }

    public Answer result(String result) {
        this.result = result;
        return this;
    }

    public Answer time(String time) {
        this.time = time;
        return this;
    }
}

