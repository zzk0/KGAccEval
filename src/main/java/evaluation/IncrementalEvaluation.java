package evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class ReservoirIncrementalEvaluation {

    private List<Double> samples;
    private List<Double> keys;

    ReservoirIncrementalEvaluation() {}

    public boolean init(Evaluation evaluation, KnowledgeGraph base) {
        if (evaluation.getMethod() != Method.TWCS) {
            System.out.println("[Warning] You are only allowed to use TWCS Evaluation results");
            return false;
        }
        samples = new ArrayList<Double>(evaluation.getEstimationSamples());
        keys = evaluation.getTwcsSampleKeys();
        return true;
    }

    // fixme: you need to calculate MoE to determine whether stop or not, so hard...
    public double evaluate(KnowledgeGraph update) {
        // flags are used to determine which keys are substitute
        boolean[] flags = new boolean[this.keys.size()];
        int smallestIndex = this.findSmallestKeyIndex();
        Random rand = new Random();
        for (int i = 0; i < update.numberOfEntityClustersTriples.size(); i++) {
            int size = update.numberOfEntityClustersTriples.get(i);
            double key = Math.pow(rand.nextDouble(), 1.0 / size);
            if (key > keys.get(smallestIndex)) {
                flags[smallestIndex] = true;
                keys.set(smallestIndex, key);
                List<Triple> triples = new ArrayList<Triple>();
                for (int j = update.startIndicesOfClusters.get(i); j < update.startIndicesOfClusters.get(i+1); j++) {
                    triples.add(update.triples.get(j));
                }
                List<Triple> srsSamples = this.srsTriples(triples);
                int correctTriples = 0;
                for (Triple sample : srsSamples) {
                    if (sample.correct) {
                        correctTriples = correctTriples + 1;
                    }
                }
                this.samples.set(smallestIndex, (double)correctTriples/srsSamples.size());

                // update smallest index
                smallestIndex = this.findSmallestKeyIndex();
            }
        }

        double accuracy = 0;
        for (double sample : this.samples) {
            accuracy = accuracy + sample;
        }
        return accuracy / this.samples.size();
    }

    private int findSmallestKeyIndex() {
        int smallestIndex = 0;
        for (int i = 1; i < this.keys.size(); i++) {
            if (keys.get(smallestIndex) > keys.get(i)) {
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }

    private List<Triple> srsTriples(List<Triple> triples) {
        List<Triple> samples = new ArrayList<Triple>();
        Collections.shuffle(triples);
        for (int i = 0; i < 4 && i < triples.size(); i++) {
            samples.add(triples.get(i));
        }
        return samples;
    }
}


class StratifiedIncrementalEvaluation {

    private int baseKnowledgeGraphSize;
    private List<Integer> updateSizes;
    private double epsilon;
    private double alpha;

    // the zero index element is base knowledge graph
    private List<Double> accuracy;
    // private List<Double> variation;

    StratifiedIncrementalEvaluation() {}

    public boolean init(Evaluation evaluation, KnowledgeGraph base) {
        this.baseKnowledgeGraphSize = base.numberOfTriples;
        this.updateSizes = new ArrayList<Integer>();
        this.epsilon = evaluation.getEpsilon();
        this.alpha = evaluation.getAlpha();
        this.accuracy = new ArrayList<Double>();

        this.accuracy.add(evaluation.getAccuracy());
        return false;
    }

    public double evaluate(KnowledgeGraph update) {
        this.updateSizes.add(update.numberOfTriples);
        Evaluation evaluation = new Evaluation(this.epsilon, this.alpha, Method.TWCS);
        accuracy.add(evaluation.evaluate(update));

        int totalSize = baseKnowledgeGraphSize;
        for (Integer size : updateSizes) {
            totalSize = totalSize + size;
        }
        double stratifiedAccuracy = (double)baseKnowledgeGraphSize / totalSize * accuracy.get(0);
        for (int i = 0; i < updateSizes.size(); i++) {
            stratifiedAccuracy = stratifiedAccuracy + (double)updateSizes.get(i) / totalSize * accuracy.get(i + 1);
        }

        return stratifiedAccuracy;
    }
}
