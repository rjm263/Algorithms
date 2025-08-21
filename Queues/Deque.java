/* *****************************************************************************
 * Synopsis: Generates an iterable deque data structure with
 *      -- isEmpty(): checks if the deck is empty
 *      -- size(): returns the current size of the deck
 *      -- addFirst()/removeFirst(): adds/removes first element in the deck
 *      -- addLast()/removeLast(): adds/removes last element in the deck
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    private Node first, last;
    private int count;

    // construct an empty deque
    public Deque() {
        first = last = null;
        count = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (count == 0);
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Please provide an item to add!");
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        if (isEmpty()) last = first;
        else {
            first.next = oldfirst;
            oldfirst.prev = first;
            first.prev = null;
        }
        count++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Please provide an item to add!");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        if (isEmpty()) first = last;
        else {
            last.prev = oldlast;
            oldlast.next = last;
            last.next = null;
        }
        count++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("The deque is empty!");
        Item item = first.item;
        first = first.next;
        if (first == null) last = null;
        else first.prev = null;
        count--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("The deque is empty!");
        Item item = last.item;
        last = last.prev;
        if (last == null) first = null;
        else last.next = null;
        count--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("There are no more items to iterate!");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove is not supported!");
        }
    }

    // unit testing
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (!s.equals("-")) {
                deque.addLast(s);
            }
            else {
                StdOut.println(deque.removeFirst());
            }
        }
        deque.addFirst("A");
        deque.removeLast();
        if (!deque.isEmpty())
            StdOut.printf("This deque is not empty! Its size is %d.%n", deque.size());
        for (String s : deque) StdOut.println(s);
    }

}
