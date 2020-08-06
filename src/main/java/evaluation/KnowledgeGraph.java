package evaluation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * knowledge graph class, just store the metadata
 *
 * 1. store the meta-data of knowledge graph
 * 2. when a specified triple needed, use the database
 *
 * assume a triple's metadata has 100 bytes(that's quite large for a triple),
 * then 1 million triples will occupied just approximately 100MB
 */
public class KnowledgeGraph {

    // metadata will not change when draw
    public double accuracy;
    public Integer numberOfEntityClusters;
    public Integer numberOfTriples;
    public List<Integer> numberOfEntityClustersTriples;
    public List<Integer> startIndicesOfClusters;
    public List<Triple> triples;

    KnowledgeGraph() {}

    // fixme: now we randomly generate knowledge graph with suggested accuracy and clusters, you can try factory
    public void init(double accuracy, int clusters) {
        numberOfEntityClusters = clusters;
        numberOfTriples = 0;

        // we use LinkedList here because List.remove() is called frequently
        numberOfEntityClustersTriples = new LinkedList<Integer>();
        startIndicesOfClusters = new ArrayList<Integer>();
        triples = new LinkedList<Triple>();
        int correctTriples = 0;

        for (int i = 0; i < clusters; i++) {
            // a cluster contains 1~30 triples
            int min = 1, max = 30;
            int clusterTriples = min + (int)(Math.random() * (max - min + 1));

            // the variance of accuracy
            double var = (Math.random() - 0.5) * 5 / 100;
            double entityAccuracy = accuracy + var;
            numberOfEntityClustersTriples.add(clusterTriples);
            startIndicesOfClusters.add(numberOfTriples);

            for (int j = 0; j < clusterTriples; j++) {
                double cur = Math.random();
                if (cur < entityAccuracy) {
                    triples.add(new Triple(numberOfTriples, i, true));
                    correctTriples = correctTriples + 1;
                }
                else {
                    triples.add(new Triple(numberOfTriples, i, false));
                }
                numberOfTriples = numberOfTriples + 1;
            }
        }
        // add an sentinel to simplify code
        // so never access the size of startIndicesOfClusters to get clusters count
        // use numberOfEntityClusters instead
        startIndicesOfClusters.add(numberOfTriples);
        this.accuracy = (double)correctTriples / numberOfTriples;
    }
}

/**
 * knowledge graph Triple
 */
class Triple {
    // In fact, we don't read the data of triple
    public String entity;
    public String relation;
    public String object;

    public int id;
    public int entityId;
    public boolean correct;

    Triple(int id, int entityId, boolean correct) {
        this.id = id;
        this.entityId = entityId;
        this.correct = correct;
    }
}
