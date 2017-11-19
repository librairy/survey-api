package org.librairy.survey.bb.tasks;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.librairy.survey.bb.service.*;
import org.librairy.survey.bb.data.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public class EvaluateCollection {

    public static void main(String[] args){

        String googleDocFilePath = "src/test/resources/Recomendaciones JUMP - Gold Standard - Gold Standard.tsv";

        PairsFromGoogleDocAsCSV service = new PairsFromGoogleDocAsCSV();

        int sampleSize = 100;
        List<PairCandidate> pairs = service.createCandidates(googleDocFilePath,sampleSize);

        System.out.println("Total number of pairs: " + pairs.size());


        // Check order
        List<Item> references = service.getReferences(googleDocFilePath, sampleSize);

        Map<Item, List<PairCandidate>> pairsPerReference = pairs.stream().collect(Collectors.groupingBy(PairCandidate::getItem1));

        List<Item> keyReferences = pairsPerReference.keySet().stream().sorted((a, b) -> Integer.valueOf(references.indexOf(a)).compareTo(Integer.valueOf(references.indexOf(b)))).collect(Collectors.toList());

        for(Item reference : keyReferences){

            PairCandidate pair = pairsPerReference.get(reference).get(0);

            System.out.println(pair.getItem1().getName() + " <-> "+ pair.getItem2().getName() );
        }



        // Check candidates per reference
        pairs.stream().collect(Collectors.groupingBy(PairCandidate::getId)).entrySet().stream().forEach(e -> System.out.println("Pairs from " + e.getKey() + ": " + e.getValue().size()));


        Map<Item, List<PairCandidate>> pairsByReference = pairs.stream().collect(Collectors.groupingBy(PairCandidate::getItem1));

        int candidatesPerItem = 50;
        int incompletedBooks = 0;
        for(Item item : pairsByReference.keySet()){
            List<PairCandidate> candidates = pairsByReference.get(item);

            List<PairCandidate> distinctCandidates = candidates.stream().distinct().collect(Collectors.toList());

            long numDistinctCandidates = distinctCandidates.size();


            // Check repeated in the same reference
            if (numDistinctCandidates != candidatesPerItem){
                incompletedBooks+=1;
                System.out.println("Reference '" + item.getName()+"'["+item.getId()+"] has not all candidates ("+numDistinctCandidates+")");

                for(PairCandidate pairCandidate : distinctCandidates){

                    List<PairCandidate> repeated = candidates.stream().filter(c -> c.equals(pairCandidate)).collect(Collectors.toList());

                    if (repeated.size() > 1){
                        System.out.println("\t [Repeated "+(repeated.size()-1)+" times] -> " + repeated.get(0).getItem2() + " by " + repeated.stream().map(p -> p.getId()).collect(Collectors.toList()));
                    }
                }

            }

            Map<String, List<PairCandidate>> candidatesPerAlgorithm = candidates.stream().collect(Collectors.groupingBy(PairCandidate::getId));

            // Check number of candidates
            for (String algorithm : candidatesPerAlgorithm.keySet()){

                long num = candidatesPerAlgorithm.get(algorithm).stream().count();

                if (num != 10){
                    System.out.println("\t '" + algorithm + "' has only " + num + " candidates");
                }

            }

        }

        System.out.println("Incomplete Books: " + incompletedBooks);

        // Check if exist on database
        List<Item> items = pairs.stream().flatMap(p -> Arrays.asList(new Item[]{p.getItem1(), p.getItem2()}).stream()).distinct().collect(Collectors.toList());

        System.out.println("Checking if exists " + items.size() + " ..");


        List<Item> missingItems = items.stream().filter(i -> !existsOnLibrairy(i)).collect(Collectors.toList());

        System.out.println("Missing Items: " + missingItems.size());

        for(Item item : missingItems){
            System.out.println("[MISSING] " + item.getName() + "[" + item.getId()+"]");
        }




    }


    private static boolean existsOnLibrairy(Item item){

        try {
            if (item.getId().contains(" ")) return true;
            HttpResponse<JsonNode> response = Unirest.get("http://zavijava.librairy.linkeddata.es/api/items/" + item.getId()).basicAuth("librairy", "l1brA1ry").asJson();
            return response.getStatus() == 200;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return false;

    }

}
