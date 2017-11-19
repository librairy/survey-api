package org.librairy.survey.questions;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import io.swagger.model.Question;
import org.librairy.survey.bb.data.PairCandidate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public class QuestionTemplate {

    private final PairCandidate pair;

    private final Escaper escaper = Escapers.builder()
            .addEscape('\'',"")
            .addEscape('\"',"")
            .addEscape('('," ")
            .addEscape(')'," ")
            .addEscape('['," ")
            .addEscape(']'," ")
            .build();

    public QuestionTemplate(PairCandidate candidate){
        this.pair = candidate;
    }

    public Question getQuestion() {

        String key = new StringBuilder()
                .append(this.pair.getItem1().getId()).append("#").append(this.pair.getItem2().getId()).toString();




        String b1 = escaper.escape(this.pair.getItem1().getName());
        String b2 = escaper.escape(this.pair.getItem2().getName());

        String url1 = "#";
        try {
            url1 = "https://www.google.com/search?q="+URLEncoder.encode(b1,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url2 = "#";
        try {
            url2 = "https://www.google.com/search?q="+URLEncoder.encode(b2,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return new Question(key, "{\n" +
                "      \"type\": \"panel\",\n" +
                "      \"innerIndent\": 1,\n" +
                "      \"name\": \"p"+key+"\",\n" +
                "      \"title\": \"Q"+key+"\",\n" +
                "      \"elements\": [\n" +
                "        {\n" +
                "          \"type\": \"html\",\n" +
                "          \"name\": \"h"+key+"\",\n" +
                "          \"html\": \"<table><body><row><td><img src='http://bit.ly/2zMxn2P' width='100px' /></td><td style='padding:20px'><a href='"+url1+"'  target='_blank'>'"+b1+"'</a></td><td><img src='http://bit.ly/2A6nX30' width='100px' /></td><td style='padding:20px'><a href='"+url2+"'  target='_blank'>'"+b2+"'</a></td></row></body></table>\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"type\": \"radiogroup\",\n" +
                "          \"name\": \""+key+"\",\n" +
                "          \"title\": \"Are these books related?\",\n" +
                "          \"isRequired\": true,\n" +
                "          \"colCount\": 1,\n" +
                "          \"choices\": [\n" +
                "            \"yes\",\n" +
                "            \"no\"\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }");

    }

}
