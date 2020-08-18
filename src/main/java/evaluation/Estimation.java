package evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * store the random variables (samples) of the estimator
 */
public abstract class Estimation {

    protected List<Double> samples;
    private double zx;

    Estimation(double alpha) {
        samples = new ArrayList<Double>();
        this.zx = this.computeZx((1 - alpha) / 2);
    }

    public double getAccuracy() {
        double accuracy = 0;
        for (double sample : samples) {
            accuracy = accuracy + sample;
        }
        return accuracy / samples.size();
    }

    public abstract double getVariance();

    public double getMarginOfError() {
        return zx * Math.sqrt(getVariance() / samples.size());
    }

    public void addSamples(List<Double> samples) {
        this.samples.addAll(samples);
    }

    public List<Double> getSamples() {
        return samples;
    }

    /**
     * Hastings formula, 0.0 < alpha < 0.5
     *
     * for example, we want a confidence interval 0.95, so we call computeZx(0.025), 0.025 = (1 - 0.95) / 2
     *
     * reference: https://wenku.baidu.com/view/3ca32836581b6bd97f19ea8c.html?re=view
     */
    private double computeZx(double alpha) {
        double[] c = {2.515517, 0.802853, 0.010328};
        double[] d = {0.0, 1.432788, 0.189269, 0.001308};
        double y = Math.sqrt(-2 * Math.log(alpha));
        double cy = 0, dy = 0;
        for (int i = 0; i < 2; i++) {
            cy = cy + c[i] * Math.pow(y, i);
            dy = dy + d[i + 1] * Math.pow(y, i+1);
        }
        return y - cy / (dy + 1);
    }

}

class SrsEstimation extends Estimation {

    SrsEstimation(double alpha) {
        super(alpha);
    }

    @Override
    public double getVariance() {
        double mean = this.getAccuracy();
        return mean * (1 - mean);
    }
}

class TwcsEstimation extends Estimation {

    TwcsEstimation(double alpha) {
        super(alpha);
    }

    @Override
    public double getVariance() {
        double acc = this.getAccuracy();
        double var = 0.0;
        for (double sample : samples) {
            var = var + (sample - acc) * (sample - acc);
        }
        return var / (samples.size() - 1);
    }
}
