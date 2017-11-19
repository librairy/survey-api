package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Reference
 */
@Data
@EqualsAndHashCode
@ToString
public class Question {

  @JsonProperty("id")
  private String id= null;

  @JsonProperty("json")
  private String json = null;

  @JsonProperty("time")
  private String time = null;

  private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

  public Question(String id, String json){
    this.id   = id;
    this.json = json;
    this.time = df.format(new Date());
  }

}

