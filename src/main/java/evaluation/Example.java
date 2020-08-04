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
        List<Integer> arr = new ArrayList<Integer>();
        long a = System.currentTimeMillis();
        for(int i = 0; i < 10000000; i++) {
            arr.add(i);
        }
        System.out.println("\r<br> 执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");

        a = System.currentTimeMillis();
        Collections.shuffle(arr);
        System.out.println("\r<br> 执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
    }
}
