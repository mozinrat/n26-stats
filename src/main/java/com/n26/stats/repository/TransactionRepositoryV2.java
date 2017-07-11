package com.n26.stats.repository;

import com.n26.stats.ds.MinMaxQueue;
import com.n26.stats.models.TransactionDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by rverma on 7/9/17.
 */
@Component
public class TransactionRepositoryV2 {
    private MinMaxQueue<TransactionDTO> queue = new MinMaxQueue<>(Comparator.comparing(TransactionDTO::getAmount));
    private AtomicReference<BigDecimal> sum = new AtomicReference<>(BigDecimal.ZERO);

    @Override
    public String toString() {
        return super.toString();
    }

    public void add(TransactionDTO transaction) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        while (!queue.isEmpty() && queue.peek().getTimestamp().compareTo(now.minusSeconds(60)) < 0) {
            TransactionDTO poll = queue.poll();
            sum.updateAndGet(curSum -> curSum.subtract(poll.getAmount()));
        }
        queue.add(transaction);
        sum.updateAndGet(curSum -> curSum.add(transaction.getAmount()));
    }

    public Map<String, BigDecimal> getStats() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        while (!queue.isEmpty() && queue.peek().getTimestamp().compareTo(now.minusSeconds(60)) < 0) {
            TransactionDTO poll = queue.poll();
            sum.updateAndGet(curSum -> curSum.subtract(poll.getAmount()));
        }
        Map<String, BigDecimal> sm = new HashMap<>();
        sm.put("sum", sum.get().setScale(2, BigDecimal.ROUND_HALF_UP));
        sm.put("avg", queue.size() == 0 ? BigDecimal.ZERO :
                sum.get().divide(BigDecimal.valueOf(queue.size()), 2, BigDecimal.ROUND_HALF_UP));
        sm.put("count", BigDecimal.valueOf(queue.size()));
        sm.put("max", queue.min().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        sm.put("min", queue.max().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        return sm;
    }

}
