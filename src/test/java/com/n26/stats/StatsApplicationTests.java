package com.n26.stats;

import com.n26.stats.models.TransactionDTO;
import com.n26.stats.repository.TransactionRepository;
import com.n26.stats.repository.TransactionRepositoryV2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.concurrent.ThreadLocalRandom.current;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatsApplicationTests {
    private static final Logger LOG = LoggerFactory.getLogger(StatsApplicationTests.class);

    @Resource
    private TransactionRepository transactionRepository;

    @Resource
    private TransactionRepositoryV2 transactionRepositoryV2;


    @Test
    public void contextLoads() {
        Arrays.stream(new int[]{2,3,4,1,2,5,6,7,7,8}).forEach(i -> {
            TransactionDTO tx = new TransactionDTO();
            tx.setAmount(BigDecimal.valueOf(i));
            tx.setTimestamp(ZonedDateTime.now(ZoneId.of("UTC")));
            transactionRepositoryV2.add(tx);
            LOG.debug("Transaction " + i + " is " + ZonedDateTime.now(tx.getTimestamp().getZone()).toString()
                    + " recorded at " + tx.getTimestamp());
        });
        LOG.info(transactionRepositoryV2.getStats().toString());
    }


    @Test
    public void generateAndGetStats() {
        Thread producer = new Thread(() -> IntStream.range(1, 50).parallel().forEach(i -> {
            TransactionDTO tx = new TransactionDTO();
            tx.setAmount(BigDecimal.valueOf(current().nextDouble(1, 100)));
            tx.setTimestamp(ZonedDateTime.now(ZoneId.of("UTC")));
            transactionRepository.add(tx);
            transactionRepositoryV2.add(tx);
            LOG.debug("Transaction " + i + " is " + ZonedDateTime.now(tx.getTimestamp().getZone()).toString()
                    + " recorded at " + tx.getTimestamp());
            try {
                Thread.sleep(current().nextInt(20) * 1000);
            } catch (InterruptedException e) {
                LOG.error("concurrent exception", e);
            }
        }));


        Thread statistics = new Thread(() -> {
            while (producer.isAlive()) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    LOG.error("concurrent exception", e);
                }
                Map<String, BigDecimal> stats = transactionRepository.getStats();
                Map<String, BigDecimal> stats2 = transactionRepositoryV2.getStats();
                LOG.info("Statistics at " + stats.toString());
                LOG.info("Statistics at " + stats2.toString());
                Assert.assertTrue(stats.get("count").compareTo(BigDecimal.ZERO) > 0);
            }
        });
        producer.start();
        statistics.start();

        try {
            producer.join();
            statistics.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
