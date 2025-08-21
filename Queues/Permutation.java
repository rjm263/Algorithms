/* *****************************************************************************
 *  Synopsis: performs random sampling of k words in a string of unknown length
 *  Dependencies: RandomizedQueue.java
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        int i = 0;
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (i < k) rq.enqueue(s);
            else {
                int r = StdRandom.uniformInt(i + 1);
                if (r < k) {
                    rq.dequeue();
                    rq.enqueue(s);
                }
            }
            i++;
        }
        for (String s : rq) StdOut.println(s);
    }
}
