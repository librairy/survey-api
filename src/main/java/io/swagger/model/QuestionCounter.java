package io.swagger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Reference
 */
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class QuestionCounter {

  private String id;

  private Long answered;

}

