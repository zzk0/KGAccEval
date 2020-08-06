package evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * store the random variables (samples) of the estimator
 */
public class Estimation {

    private List<Double> samples;
    private double[] normalDistributionData;
    private double alpha;

    Estimation(double alpha) {
        this.alpha = alpha;
        this.generateNormalDistribution();
    }

    public double getAccuracy() {
        double accuracy = 0;
        for (double sample : samples) {
            accuracy = accuracy + sample;
        }
        return accuracy / samples.size();
    }

//    public abstract double getVariance();

    public double getMarginOfError() {
        return 0;
    }

    /**
     * To compute z_{\alpha_2}, we generate data that follows normal distribution
     * given alpha, we can determine z_{\alpha_2} by computing the fraction of numbers surpass alpha
     *
     * todo: you can optimize the code by binary search to insert the number and count the number
     */
    private void generateNormalDistribution() {
        int COUNT = 10000000;
        normalDistributionData = new double[COUNT];
        Random r = new Random();
        for (int i = 0; i < COUNT; i++) {
            normalDistributionData[i] = r.nextGaussian();
        }
    }

    /**
     * compute z_{\alpha_2}
     * @param alpha
     * @return
     */
    private double zx(double alpha) {
        int numberBelowAlpha = 0;
        for (int i = 0; i < normalDistributionData.length; i++) {
            if (this.normalDistributionData[i] < alpha) {
                numberBelowAlpha = numberBelowAlpha + 1;
            }
        }
        return (double)numberBelowAlpha / normalDistributionData.length;
    }

}
