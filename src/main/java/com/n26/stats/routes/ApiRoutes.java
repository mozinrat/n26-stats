package com.n26.stats.routes;

import com.n26.stats.models.TransactionDto;
import com.n26.stats.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Created by payal on 7/8/17.
 */
@RestController
public class ApiRoutes {
    private static final Logger LOG = LoggerFactory.getLogger(ApiRoutes.class);

    @Resource
    private TransactionRepository transactionRepository;

    @PostMapping("/transactions")
    public void transact(@RequestBody @Valid TransactionDto transaction) {
        transactionRepository.add(transaction);
        LOG.info("Transaction is valid current is " + ZonedDateTime.now(transaction.getTimestamp().getZone()).toString()
                + " recorded at " + transaction.getTimestamp());
    }

    @GetMapping("/statistics")
    public Map<String,BigDecimal> getStats(){
        return transactionRepository.getStats();
    }
}
