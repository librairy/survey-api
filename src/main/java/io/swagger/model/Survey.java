package io.swagger.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Reference
 */

@Data
@EqualsAndHashCode
@ToString
public class Survey {

  private List<String> questions = new ArrayList<>();

  public Survey addQuestionElement(String element){
    this.questions.add(element);
    return this;
  }

  public String toJson(){
    StringBuilder json = new StringBuilder();
    json.append("{\"showQuestionNumbers\": \"false\", \"elements\": [");
    questions.forEach(q -> json.append(q).append(","));
    json.append("{\"id\": \"email\",\"type\": \"text\",\"name\": \"email\",\"title\": \"Email address:\",\"isRequired\": true } ]}");

    return json.toString();
  }

}

