package org.librairy.survey.bb.data;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.ToString;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Data
@ToString
public class Item {

    String name;

    String id;

    public boolean isEmpty(){
        return Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(id);
    }

}
