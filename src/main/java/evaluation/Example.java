package evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Example
 *
 * show how to use to evaluate a knowledge graph
 */
public class Example {

    public static void main(String[] args) {
        double suggestedAccuracy = 0.73;

        KnowledgeGraph kg = new KnowledgeGraph();
        kg.init(suggestedAccuracy, 100000);
        System.out.println(kg.accuracy);

        double acc = kg.accuracy;
        double epsilon = 0.05;
        double alpha = 0.95;

        int time1 = 0, time2 = 0;
        int in1 = 0, in2 = 0;
        int total = 100;
        for (int i = 0; i < total; i++) {
            Evaluation evaluation1 = new Evaluation(epsilon, alpha, Method.SRS);
            double acc1 = evaluation1.evaluate(kg);
            time1 += evaluation1.evaluationCost();
            System.out.print(acc1 + " " + evaluation1.sampleNumber() + " " + evaluation1.evaluationCost());
            if (Math.abs(acc - acc1) < epsilon) {
                in1 = in1 + 1;
                System.out.println(" yes");
            }
            else {
                System.out.println(" no");
            }

            Evaluation evaluation2 = new Evaluation(epsilon, alpha, Method.TWCS);
            double acc2 = evaluation2.evaluate(kg);
            time2 += evaluation2.evaluationCost();
            System.out.print(acc2 + " " + evaluation2.sampleNumber() + " " + evaluation2.evaluationCost());
            if (Math.abs(acc - acc2) < epsilon) {
                System.out.println(" yes");
                in2 = in2 + 1;
            }
            else {
                System.out.println(" no");
            }
        }

        System.out.println("The probability of the predict accuracy for SRS in the CI: " + (double)in1 / total);
        System.out.println("The probability of the predict accuracy for TWCS in the CI: " + (double)in2 / total);
        System.out.println(time1 / 10);
        System.out.println(time2 / 10);

//        long a = System.currentTimeMillis();
//        System.out.println("\r<br> 执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
    }
}
