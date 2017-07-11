package com.n26.stats.ds;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by rohitverma on 11/07/17.
 */
public class MinMaxQueue<T> extends ConcurrentLinkedQueue<T> {

    private Deque<T> maxValuesQueue;
    private Deque<T> minValuesQueue;
    private Comparator<T> comparator;

    public MinMaxQueue(Comparator<T> comparator) {
        maxValuesQueue = new ArrayDeque<>();
        minValuesQueue = new ArrayDeque<>();
        this.comparator = comparator;
    }

    public boolean add(T e) {
        if (super.add(e)) {
            while (maxValuesQueue.size()>0 && comparator.compare(max(),e)<0) maxValuesQueue.pollLast();
            maxValuesQueue.addLast(e);

            while (minValuesQueue.size()>0 && comparator.compare(min(),e)>0) minValuesQueue.pollLast();
            minValuesQueue.addLast(e);
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
        return minValuesQueue.peekLast();
    }

    public T max() {
        return maxValuesQueue.peekLast();
    }

}
