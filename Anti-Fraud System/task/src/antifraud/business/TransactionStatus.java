package antifraud.business;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    ALLOWED(1, 200),
    MANUAL_PROCESSING(201, 1500),
    PROHIBITED(1501, Long.MAX_VALUE);

    private final long min;
    private final long max;

    TransactionStatus(long min, long max) {
        this.min = min;
        this.max = max;
    }
}
