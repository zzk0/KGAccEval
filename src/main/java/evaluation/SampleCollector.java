package evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * To sample a knowledge graph
 */
public abstract class SampleCollector {

    protected KnowledgeGraph kg;

    SampleCollector() {}

    public abstract void setKg(KnowledgeGraph kg);

    // todo: throws exception when there is no more samples can be drawn
    public abstract List<Triple> sample(int n);
}

/**
 * Simple Random Sampling
 *
 * shuffle and remove the last n_s items every time
 */
class SrsSampleCollector extends SampleCollector {
    private int lastOne;

    SrsSampleCollector() {}

    @Override
    public void setKg(KnowledgeGraph kg) {
        this.kg = kg;
        Collections.shuffle(kg.triples);
        lastOne = kg.triples.size() - 1;
    }

    // fixme: lastOne may be -1
    @Override
    public List<Triple> sample(int n) {
        List<Triple> triples = new ArrayList<Triple>();
        for (int i = 0; i < n; i++) {
            triples.add(kg.triples.get(lastOne));
            lastOne = lastOne - 1;
        }
        return triples;
    }
}


/**
 * Two-Stage Weighted Cluster Sampling
 *
 * create cluster ids and shuffle, remove last n clusters every time
 * when cluster removed, shuffle cluster's triples, sampling m=4 triples (suggested 3~5 in the paper)
 */
class TwcsSampleCollector extends SampleCollector {
    private List<Integer> clusters;
    private int lastOne;
    private static final int M = 4;

    TwcsSampleCollector() {}

    @Override
    public void setKg(KnowledgeGraph kg) {
        this.kg = kg;
        clusters = new ArrayList<Integer>();
        for (int i = 0; i < kg.numberOfEntityClusters; i++) {
            clusters.add(i);
        }
        lastOne = clusters.size() - 1;
        Collections.shuffle(clusters);
    }

    // fixme: lastOne may be -1
    @Override
    public List<Triple> sample(int n) {
        List<Triple> triples = new ArrayList<Triple>();
        for (int i = 0; i < n; i++) {
            int clusterId = clusters.get(lastOne);
            lastOne = lastOne - 1;

            List<Triple> clusterTriples = new ArrayList<Triple>();
            for (int j = kg.startIndicesOfClusters.get(clusterId);
                    j < kg.startIndicesOfClusters.get(clusterId + 1); j++) {
                clusterTriples.add(kg.triples.get(j));
            }
            List<Triple> sampleTriples = SrsTriples(clusterTriples);
            triples.addAll(sampleTriples);
        }
        return triples;
    }

    private List<Triple> SrsTriples(List<Triple> triples) {
        List<Triple> samples = new ArrayList<Triple>();
        Collections.shuffle(triples);
        for (int i = 0; i < M && i < triples.size(); i++) {
            samples.add(triples.get(i));
        }
        return samples;
    }
}
