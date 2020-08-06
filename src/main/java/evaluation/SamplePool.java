package evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * To store samples
 */
public class SamplePool {
    private List<Triple> evaluatedTriples;
    private double accuracy;

    SamplePool() {
        this.evaluatedTriples = new ArrayList<Triple>();
        this.accuracy = 0.0;
    }

    /**
     * store new triples and re-evaluate the accuracy
     * the new triples you add must be evaluated, triple.correct must be set
     * @param triples
     */
    public void add(List<Triple> triples) {
        evaluatedTriples.addAll(triples);
        int numberOfCorrect = 0;
        for (Triple triple : evaluatedTriples) {
            if (triple.correct) {
                numberOfCorrect = numberOfCorrect + 1;
            }
        }
        this.accuracy = (double)numberOfCorrect / evaluatedTriples.size();
    }

    public double getAccuracy() {
        return this.accuracy;
    }

    /**
     * cost seconds
     * @return
     */
    public int evaluationCost() {
        int clusters = 0;
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (Triple triple : evaluatedTriples) {
            if (map.get(triple.entityId) == null) {
                clusters = clusters + 1;
                map.put(triple.entityId, 1);
            }
        }
        return clusters * 45 + evaluatedTriples.size() * 25;
    }

    /**
     * sample number
     */
    public int sampleNumber() {
        return this.evaluatedTriples.size();
    }
}
