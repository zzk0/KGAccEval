package evaluation;

import java.util.ArrayList;
import java.util.List;

class ReservoirIncrementalEvaluation {

    private List<List<Triple>> reservoir;
    private List<Double> samples;
    private List<Double> keys;

    ReservoirIncrementalEvaluation(Evaluation evaluation) {}

    public boolean init(Evaluation evaluation) {
        if (evaluation.getMethod() == Method.SRS) {
            System.out.println("[Warning] You are not allowed to use SRS Evaluation results");
            return false;
        }
        reservoir = new ArrayList<List<Triple>>(evaluation.getSampleClusterSet());
        samples = new ArrayList<Double>(evaluation.getEstimationSamples());
        return true;
    }

    public double evaluate(KnowledgeGraph kg) {
        return 0.0;
    }
}

class StratifiedIncrementalEvaluation {

    StratifiedIncrementalEvaluation() {}

    public boolean init(Evaluation evaluation) {
        return false;
    }

    public double evalute(KnowledgeGraph kg) {
        return 0.0;
    }
}