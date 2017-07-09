package com.n26.stats.repository;

import com.n26.stats.models.TransactionDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by payal on 7/9/17.
 */
@Component
public class TransactionRepository {
    private ConcurrentLinkedQueue<TransactionDto> queue = new ConcurrentLinkedQueue<>();
    private AtomicLong count = new AtomicLong(0);
    private AtomicReference<BigDecimal> sum = new AtomicReference<>(BigDecimal.ZERO);

    private BigDecimal max = BigDecimal.valueOf(Double.MIN_VALUE);
    private BigDecimal min = BigDecimal.valueOf(Double.MAX_VALUE);


    @Override
    public String toString() {
        return super.toString();
    }

    public void add(TransactionDto transaction) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        while (!queue.isEmpty() && queue.peek().getTimestamp().compareTo(now.minusSeconds(60)) < 0) {
            TransactionDto poll = queue.poll();
            sum.updateAndGet(curSum -> curSum.subtract(poll.getAmount()));
            count.decrementAndGet();
        }
        queue.add(transaction);
        count.incrementAndGet();
        sum.updateAndGet(curSum -> curSum.add(transaction.getAmount()));
    }

    public Map<String, BigDecimal> getStats() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        while (!queue.isEmpty() && queue.peek().getTimestamp().compareTo(now.minusSeconds(60)) < 0) {
            TransactionDto poll = queue.poll();
            sum.updateAndGet(curSum -> curSum.subtract(poll.getAmount()));
            count.decrementAndGet();
        }
        
        Map<String, BigDecimal> sm = new HashMap<>();
        sm.put("sum", sum.get());
        sm.put("avg", count.get()==0?BigDecimal.ZERO:sum.get().divide(BigDecimal.valueOf(count.get())));
        sm.put("max", BigDecimal.ONE);
        sm.put("min", BigDecimal.ONE);
        sm.put("count", BigDecimal.valueOf(count.get()));
        return sm;
    }


}
