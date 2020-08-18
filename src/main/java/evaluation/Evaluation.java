package evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Evaluation class
 *
 */
public class Evaluation {

    private double epsilon;
    private double alpha;
    private Method method;
    private SamplePool samplePool;
    private Estimation estimation;
    private SampleCollector sampleCollector;

    Evaluation(double epsilon, double alpha, Method method) {
        this.epsilon = epsilon;
        this.alpha = alpha;
        this.method = method;
        this.samplePool = new SamplePool();
        switch (method) {
            case SRS:
                this.estimation = new SrsEstimation(alpha);
                this.sampleCollector = new SrsSampleCollector();
                break;
            case TWCS:
                this.estimation = new TwcsEstimation(alpha);
                this.sampleCollector = new TwcsSampleCollector();
                break;
        }
    }

    public double evaluate(KnowledgeGraph kg) {
        this.sampleCollector.setKg(kg);
        boolean remainTriples = true;
        do {
            List<Triple> triples;
            List<Double> samples =  new ArrayList<Double>();

            // draw triples based on method
            switch (method) {
                case SRS:
                    triples = this.sampleCollector.sample(30);
                    if (triples.size() == 0) {
                        remainTriples = false;
                        break;
                    }
                    for (Triple triple : triples) {
                        triple.correct = checkTriple(triple);
                        samples.add(triple.correct ? 1.0 : 0.0);
                    }
                    this.samplePool.add(triples);
                    this.estimation.addSamples(samples);
                    break;
                case TWCS:
                    // we will draw 10 ~ 40 triples every time
                    triples = this.sampleCollector.sample(10);
                    int numberOfCorrect = 0;
                    int numberOfCluster = 0;
                    if (triples.size() == 0) {
                        remainTriples = false;
                        break;
                    }
                    int lastClusterId = triples.get(0).entityId;
                    for (Triple triple : triples) {
                        if (triple.entityId != lastClusterId) {
                            lastClusterId = triple.entityId;
                            samples.add((double)numberOfCorrect / numberOfCluster);
                            numberOfCorrect = 0;
                            numberOfCluster = 0;
                        }
                        triple.correct = checkTriple(triple);
                        if (triple.correct) {
                            numberOfCorrect = numberOfCorrect + 1;
                        }
                        numberOfCluster = numberOfCluster + 1;
                    }
                    if (numberOfCluster != 0) {
                        samples.add((double)numberOfCorrect / numberOfCluster);
                    }
                    this.samplePool.add(triples);
                    this.estimation.addSamples(samples);
                    break;
            }
        } while (this.estimation.getMarginOfError() > this.epsilon && remainTriples);
        return this.estimation.getAccuracy();
    }

    public int evaluationCost() {
        return this.samplePool.evaluationCost();
    }

    public int sampleNumber() {
        return this.samplePool.sampleNumber();
    }

    /**
     * Currently just return the correctness of randomly generated triple
     * In fact, we need human to check triple correct or not, and it will cost time
     *
     * @param triple
     * @return
     */
    private boolean checkTriple(Triple triple) {
        return triple.correct;
    }

    public List<Integer> getSampleClusterIdSet() {
        return samplePool.getSampleClusterIdSet();
    }

    public List<Double> getEstimationSamples() {
        return estimation.getSamples();
    }

    public Method getMethod() {
        return method;
    }

    public List<Double> getTwcsSampleKeys() {
        if (this.method != Method.TWCS) {
            System.out.println("[Warning] you are only allowed to call with TWCS evaluation");
            return null;
        }
        return ((TwcsSampleCollector)this.sampleCollector).getSampleKeys();
    }

    public double getAlpha() {
        return alpha;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public double getAccuracy() {
        return this.estimation.getAccuracy();
    }
}
