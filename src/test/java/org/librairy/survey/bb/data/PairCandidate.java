package org.librairy.survey.bb.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Data
@AllArgsConstructor
@ToString
public class PairCandidate {

    String id;
    Item item1;
    Item item2;


    public boolean isEmpty(){
        return item1.isEmpty() || item2.isEmpty();
    }


}
