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
        KnowledgeGraph kg = new KnowledgeGraph();
        kg.init(0.90, 100000);
        System.out.println(kg.accuracy);

        int time1 = 0, time2 = 0;
        for (int i = 0; i < 10; i++) {
            Evaluation evaluation1 = new Evaluation(0.02, 0.95, Method.SRS);
            double acc1 = evaluation1.evaluate(kg);
            time1 += evaluation1.evaluationCost();
            System.out.println(acc1 + " " + evaluation1.sampleNumber() + " " + evaluation1.evaluationCost());

            Evaluation evaluation2 = new Evaluation(0.02, 0.95, Method.TWCS);
            double acc2 = evaluation2.evaluate(kg);
            time2 += evaluation2.evaluationCost();
            System.out.println(acc2 + " " + evaluation2.sampleNumber() + " " + evaluation2.evaluationCost());
        }

        System.out.println(time1 / 10);
        System.out.println(time2 / 10);
//
//        SampleCollector sc = new TwcsSampleCollector();
//        sc.setKg(kg);
//        SamplePool sp = new SamplePool();
//        for (int i = 0; i < 100; i++) {
//            List<Triple> triples = sc.sample(10);
//            sp.add(triples);
//            System.out.println(sp.getAccuracy());
//        }

//        long a = System.currentTimeMillis();
//        System.out.println("\r<br> 执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
    }
}
