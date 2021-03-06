package com.n26.stats.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.n26.stats.configuration.ZonedDateTimeDeserializer;
import com.n26.stats.utils.validations.annotations.Past;
import com.n26.stats.utils.validations.annotations.Recent;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Created by rohit on 7/8/17.
 */
public class TransactionDTO {

    private BigDecimal amount;

    @JsonFormat(timezone = "UTC")
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @NotNull(message = "timestamp should not be null")
    @Past(message = "timestamp should not be future")
    @Recent(timeFromNowInSeconds = 60,message = "timestamp should not be less than 60 seconds from now")
    // taking in account that this is financial data, we may need to manage timezone which is not possible if we receive time in millis/long
    private ZonedDateTime timestamp;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
