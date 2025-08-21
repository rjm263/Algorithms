/* *****************************************************************************
 *  Synopsis: Generates queue data structure with a random iterator.
 *      -- isEmpty(): checks if the deck is empty
 *      -- size(): returns the current size of the deck
 *      -- enqueue(): adds item in front
 *      -- dequeue(): removes item and returns a random item (different to ordinary queue)
 *      -- sample(): returns random item (but doesn't remove it)
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] s;
    private int N;

    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[1];
        N = 0;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            copy[i] = s[i];
        }
        s = copy;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("No argument provided!");
        if (s.length == N) resize(2 * s.length);
        s[N++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("The queue is empty!");
        int j = StdRandom.uniformInt(N);
        Item item = s[j];
        s[j] = s[--N];
        s[N] = null;
        if (N > 0 && N == s.length / 4) resize(s.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("The queue is empty!");
        int j = StdRandom.uniformInt(N);
        return s[j];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {

        private int[] index;
        private int current;

        public RandomIterator() {
            index = new int[N];
            for (int i = 0; i < N; i++) index[i] = i;
            StdRandom.shuffle(index);
            current = 0;
        }

        public boolean hasNext() {
            return current < N;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more items to iterate!");
            Item item = s[index[current++]];
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove is not supported!");
        }
    }

    // unit testing
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        if (rq.isEmpty()) StdOut.println("The queue is empty.");
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            rq.enqueue(s);
        }
        StdOut.println("The queue size is currently " + rq.size());
        StdOut.println(rq.dequeue());
        StdOut.println(rq.sample());
        StdOut.println(rq.dequeue());
        StdOut.println("The queue size is currently " + rq.size());
        for (String s : rq) StdOut.println(s);
    }

}
