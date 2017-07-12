package com.n26.stats.ds;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by rohitverma on 11/07/17.
 */
public class MinMaxQueue<T> extends ConcurrentLinkedQueue<T> {

    private Deque<T> maxValuesQueue;
    private Deque<T> minValuesQueue;
    private Comparator<T> comparator;

    public MinMaxQueue(Comparator<T> comparator) {
        maxValuesQueue = new ConcurrentLinkedDeque<>();
        minValuesQueue = new ConcurrentLinkedDeque<>();
        this.comparator = comparator;
    }

    public boolean add(T e) {
        if (super.add(e)) {
            if (!minValuesQueue.isEmpty()  && comparator.compare(min(), e) < 0) {
                // place element in sorted position in queue
                while (comparator.compare(minValuesQueue.peekLast(), e) > 0) minValuesQueue.pollLast() ;
                minValuesQueue.addLast(e);
            } else {
                minValuesQueue.clear();
                minValuesQueue.add(e);
            }

            if (!maxValuesQueue.isEmpty() && comparator.compare(max(), e) > 0) {
                // place element in sorted position in queue
                while (comparator.compare(maxValuesQueue.peekLast(), e) < 0) maxValuesQueue.pollLast();
                maxValuesQueue.addLast(e);
            } else {
                maxValuesQueue.clear();
                maxValuesQueue.add(e);
            }

            return true;
        }
        return false;
    }

    public T remove() {
        T element = super.remove();
        if (element != null) {
            if (maxValuesQueue.element() == element) {
                maxValuesQueue.remove();
            }

            if (minValuesQueue.element() == element) {
                minValuesQueue.remove();
            }
        }
        return element;
    }

    public T min() {
        return minValuesQueue.peekFirst();
    }

    public T max() {
        return maxValuesQueue.peekFirst();
    }

}
