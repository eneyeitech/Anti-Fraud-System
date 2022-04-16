package antifraud.business;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    ALLOWED(1),
    MANUAL_PROCESSING(2),
    PROHIBITED(3);

    private final int priority;

    TransactionStatus(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
