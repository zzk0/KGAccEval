package evaluation;

import java.util.*;

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
 */
class SrsSampleCollector extends SampleCollector {
    private List<Integer> allTripleIds;
    private int index;

    SrsSampleCollector() {}

    @Override
    public void setKg(KnowledgeGraph kg) {
        this.kg = kg;
        allTripleIds = new ArrayList<Integer>();
        index = 0;
        for (int i = 0; i < kg.numberOfTriples; i++) {
            allTripleIds.add(i);
        }
        Collections.shuffle(allTripleIds);
    }

    @Override
    public List<Triple> sample(int n) {
        List<Triple> triples = new ArrayList<Triple>();
        for (int i = 0; i < n && index < allTripleIds.size(); i++) {
            triples.add(kg.triples.get(allTripleIds.get(index)));
            index = index + 1;
        }
        return triples;
    }
}


/**
 * Two-Stage Weighted Cluster Sampling
 *
 */
class TwcsSampleCollector extends SampleCollector {

    class Tuple {
        double key;
        int index;

        Tuple(double key, int index) {
            this.key = key;
            this.index = index;
        }
    }

    private List<Integer> clusters;
    private int lastOne;
    private static final int M = 4;
    private PriorityQueue<Tuple> keys;
    private List<Double> sampleKeys;

    TwcsSampleCollector() {}

    @Override
    public void setKg(KnowledgeGraph kg) {
        this.kg = kg;
        clusters = kg.startIndicesOfClusters;
        lastOne = kg.triples.size() - 1;
        keys = new PriorityQueue<Tuple>(100, new Comparator<Tuple>() {
            public int compare(Tuple o1, Tuple o2) {
                return -Double.compare(o1.key, o2.key);
            }
        });
        sampleKeys = new ArrayList<Double>();
        Random rand = new Random();
        int i = 0;
        for (Integer size : kg.numberOfEntityClustersTriples) {
            keys.add(new Tuple(Math.pow(rand.nextDouble(), 1.0 / size), i));
            i = i + 1;
        }
    }

    @Override
    public List<Triple> sample(int n) {
        List<Triple> triples = new ArrayList<Triple>();
        for (int i = 0; i < n && this.keys.peek() != null; i++) {
            Tuple tuple = this.keys.poll();
            int clusterId = tuple.index;
            sampleKeys.add(tuple.key);
            List<Triple> clusterTriples = new ArrayList<Triple>();
            for (int j = kg.startIndicesOfClusters.get(clusterId);
                 j < kg.startIndicesOfClusters.get(clusterId + 1); j++) {
                clusterTriples.add(kg.triples.get(j));
            }
            List<Triple> sampleTriples = srsTriples(clusterTriples);
            triples.addAll(sampleTriples);
        }
        return triples;
    }

    private List<Triple> srsTriples(List<Triple> triples) {
        List<Triple> samples = new ArrayList<Triple>();
        Collections.shuffle(triples);
        for (int i = 0; i < M && i < triples.size(); i++) {
            samples.add(triples.get(i));
        }
        return samples;
    }

    public List<Double> getSampleKeys() {
        return sampleKeys;
    }
}
