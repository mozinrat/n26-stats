package com.n26.stats.repository;

import com.n26.stats.models.TransactionDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by payal on 7/9/17.
 */
@Component
public class TransactionRepository {
    private ConcurrentLinkedQueue<TransactionDto> queue = new ConcurrentLinkedQueue<>();
    private AtomicReference<BigDecimal> sum = new AtomicReference<>(BigDecimal.ZERO);

    @Override
    public String toString() {
        return super.toString();
    }

    public void add(TransactionDto transaction) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        while (!queue.isEmpty() && queue.peek().getTimestamp().compareTo(now.minusSeconds(60)) < 0) {
            TransactionDto poll = queue.poll();
            sum.updateAndGet(curSum -> curSum.subtract(poll.getAmount()));
        }
        queue.add(transaction);
        sum.updateAndGet(curSum -> curSum.add(transaction.getAmount()));
    }

    public Map<String, BigDecimal> getStats() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        while (!queue.isEmpty() && queue.peek().getTimestamp().compareTo(now.minusSeconds(60)) < 0) {
            TransactionDto poll = queue.poll();
            sum.updateAndGet(curSum -> curSum.subtract(poll.getAmount()));
        }
        Map<String, BigDecimal> sm = new HashMap<>();
        sm.put("sum", sum.get());
        sm.put("avg", queue.size() == 0 ? BigDecimal.ZERO : sum.get().divide(BigDecimal.valueOf(queue.size())));
        sm.put("count", BigDecimal.valueOf(queue.size()));
        sm.putAll(getMinMax(queue));
        return sm;
    }

    private Map<String, BigDecimal> getMinMax(ConcurrentLinkedQueue<TransactionDto> queue) {
        BigDecimal min = queue.peek().getAmount();
        BigDecimal max = queue.peek().getAmount();
        for (TransactionDto tx : queue) {
            if (tx.getAmount().compareTo(max) > 0) {
                max = tx.getAmount();
            }
            if (tx.getAmount().compareTo(min) < 0) {
                min = tx.getAmount();
            }
        }
        Map<String, BigDecimal> sm = new HashMap<>();
        sm.put("max", max);
        sm.put("min", min);
        return sm;
    }

}
