package org.librairy.survey.bb.data;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
@Data
@ToString
@RequiredArgsConstructor
public class GroupCandidate {

    @NonNull final Item item;

    List<Candidate> candidates = new ArrayList<>();

    public List<PairCandidate> getPairCandidates(){
        return candidates.stream().map(i -> new PairCandidate(i.id,item,i.item)).filter(p -> !p.isEmpty()).collect(Collectors.toList());
    }
}
