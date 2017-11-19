package org.librairy.survey.bb.service;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.librairy.survey.bb.data.Candidate;
import org.librairy.survey.bb.data.GroupCandidate;
import org.librairy.survey.bb.data.Item;
import org.librairy.survey.bb.data.PairCandidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public class PairsFromGoogleDocAsCSV {

    private static final Logger LOG = LoggerFactory.getLogger(PairsFromGoogleDocAsCSV.class);

    private static final String separator = "\t";

    public List<Item> getReferences(String csvFilePath, Integer size){
        List<Item> references = new ArrayList<>();
        try{
            File inputF = new File(csvFilePath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

            String line         = br.readLine();
            references = getItems(line,size);

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return references;
    }


    public List<PairCandidate> createCandidates(String csvFilePath, Integer size){

        Map<Integer,GroupCandidate> sample      = new ConcurrentHashMap<>();

        try{
            File inputF = new File(csvFilePath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

            int lineNumber      = 0;
            int partialNumber   = 0;
            String line         = br.readLine();
            String pairOwner    = "";

            while(!Strings.isNullOrEmpty(line)) {

                lineNumber += 1;

                if (lineNumber == 1) {

                    List<Item> references = getItems(line,size);


                    for (int i=0;i<references.size();i++) {
                        sample.put(i, new GroupCandidate(references.get(i)));
                    }
                } else{
                    partialNumber += 1;

                    if (partialNumber == 1) {
                        pairOwner = StringUtils.substringBefore(line, separator);
                        LOG.info("Owner: " + pairOwner);
                    } else if (partialNumber == 12){
                        //empty line
                        partialNumber = 0;
                        pairOwner = "";
                    } else{
                        final String candidateOwner = pairOwner;
                        List<Candidate> candidates = getItems(line,size).stream().map(i -> new Candidate(candidateOwner, i)).collect(Collectors.toList());
                        for(int i=0; i<candidates.size();i++){
                            sample.get(i).getCandidates().add(candidates.get(i));
                        }
                    }

                }

                LOG.info("line " + lineNumber + " processed");
                line = br.readLine();
            }


            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sample.entrySet().stream().flatMap(e -> e.getValue().getPairCandidates().stream()).collect(Collectors.toList());

    }

    private List<Item> getItems(String line,Integer size){

        int columnIndex = 0;
        try{
            StringTokenizer tokenizer = new StringTokenizer(line,separator);
            List<Item> items = new ArrayList<>();
            while(tokenizer.hasMoreTokens() && columnIndex < (size*2)){
                Item item = new Item();
                String name = tokenizer.nextToken();
                if (!Strings.isNullOrEmpty(name)) item.setName(name);
                if (!tokenizer.hasMoreTokens()) continue;
                String id = tokenizer.nextToken();
                if (!Strings.isNullOrEmpty(id)) item.setId(id);
                items.add(item);
                columnIndex +=2;
            }
            return items;
        }catch (Exception e){
            LOG.error("Exception error in column " + columnIndex+"  on line: " + line, e);
            throw e;
        }
    }


}
