package evaluation;

import java.util.ArrayList;
import java.util.List;

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

    public int drawCount() {
        return this.evaluatedTriples.size();
    }
}
