package org.librairy.survey.bb.data;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Data
@ToString
@RequiredArgsConstructor
public class Candidate {

    @NonNull  String id;

    @NonNull  Item item;
}
