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
        long a = System.currentTimeMillis();
        Estimation estimation = new Estimation(0.09);
        System.out.println("\r<br> 执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");

        double[] vals = {0.00, 0.09, 0.22, 0.46, 0.85, 1.0, 2.2};
        for (int i = 0; i < vals.length; i++) {
            a = System.currentTimeMillis();
            double zx = estimation.zx(vals[i]);
            System.out.println(zx);
            System.out.println("\r<br> 执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
        }

//        KnowledgeGraph kg = new KnowledgeGraph();
//        kg.init(0.83, 1000);
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
