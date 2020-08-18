package evaluation;

import java.text.DecimalFormat;
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
        kg.init(suggestedAccuracy, 1000);
        System.out.println(kg.accuracy);

        double acc = kg.accuracy;
        double epsilon = 0.03;
        double alpha = 0.90;

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

        System.out.println("SRS 准确率落在置信区间的概率为: " + (double)in1 / total);
        System.out.println("TWCS 准确率落在置信区间的概率为: " + (double)in2 / total);
        System.out.println("SRS 平均消耗的时间：" + time1 / total);
        System.out.println("TWCS 平均消耗的时间：" + time2 / total);

        // base evaluation
        Evaluation evaluation = new Evaluation(epsilon, alpha, Method.TWCS);
        acc = evaluation.evaluate(kg);
        System.out.println(acc);

        ReservoirIncrementalEvaluation incrementalEvaluation = new ReservoirIncrementalEvaluation();
        incrementalEvaluation.init(evaluation, kg);

        StratifiedIncrementalEvaluation incrementalEvaluation1 = new StratifiedIncrementalEvaluation();
        incrementalEvaluation1.init(evaluation, kg);

        double[] accs = {0.27, 0.8, 0.2, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.9};
        for (int i = 0; i < accs.length; i++) {
            // update evaluation
            KnowledgeGraph update = new KnowledgeGraph();
            update.init(accs[i], 1000);

            System.out.println("---------------------------------------");

            double newAccuracy = incrementalEvaluation.evaluate(update);
            System.out.println("Reservoir Incremental Evaluation: " + newAccuracy);

            newAccuracy = incrementalEvaluation1.evaluate(update);
            System.out.println("Stratified Incremental Evaluation: " + newAccuracy);
        }

        for (int i = 0; i < 100; i++) {
            // update evaluation
            KnowledgeGraph update = new KnowledgeGraph();
            update.init(0.9, 100);

            System.out.println("---------------------------------------");

            double newAccuracy = incrementalEvaluation.evaluate(update);
            System.out.println("Reservoir Incremental Evaluation: " + newAccuracy);

            newAccuracy = incrementalEvaluation1.evaluate(update);
            System.out.println("Stratified Incremental Evaluation: " + newAccuracy);
        }

//        long a = System.currentTimeMillis();
//        System.out.println("\r<br> 执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
    }
}
