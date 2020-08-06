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
        kg.init(0.53, 1000);

        Evaluation evaluation1 = new Evaluation(0.05, 0.95, Method.SRS);
        double acc1 = evaluation1.evaluate(kg);
        System.out.println(acc1);

        Evaluation evaluation2 = new Evaluation(0.05, 0.95, Method.TWCS);
        double acc2 = evaluation2.evaluate(kg);
        System.out.println(acc2);

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
