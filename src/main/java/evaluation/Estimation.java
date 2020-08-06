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
        this.zx = this.computeZx(alpha/2);
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

    /**
     * To compute z_{\alpha_2}, we generate data that follows normal distribution
     * given alpha, we can determine z_{\alpha_2} by computing the fraction of numbers surpass alpha
     */
    private double computeZx(double alpha) {
        int COUNT = 10000000;
        Random r = new Random();
        int numberBelowAlpha = 0;
        for (int i = 0; i < COUNT; i++) {
            if (r.nextGaussian() < alpha) {
                numberBelowAlpha = numberBelowAlpha + 1;
            }
        }
        return (double)numberBelowAlpha / COUNT;
    }

}

class SrsEstimation extends Estimation {

    SrsEstimation(double alpha) {
        super(alpha);
    }

    @Override
    public double getVariance() {
        return this.getAccuracy() * (1 - this.getAccuracy());
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
