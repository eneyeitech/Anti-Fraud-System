package antifraud.business;

import antifraud.persistence.StatusRepositoryService;

import java.util.function.BiFunction;

public abstract class LimitChanger {
    private final StatusRepositoryService statusRepository;
    private final TransactionStatus status;
    private LimitChanger next;

    public LimitChanger(StatusRepositoryService statusRepository, TransactionStatus status) {
        this.statusRepository = statusRepository;
        this.status = status;
    }

    public LimitChanger setNextLimitChanger(LimitChanger next) {
        this.next = next;
        return this.next;
    }

    private void changeLimitIfStatusDESCDirection(TransactionStatus from, TransactionStatus to, long transactionAmount) {
        if (status.getPriority() < from.getPriority() && status.getPriority() >= to.getPriority()) {
            BiFunction<Long, Long, Long> calculationMethod = this::calculateIncreasedLimit;
            updateLimit(transactionAmount, calculationMethod);
        }
        if (next != null) {
            next.changeLimitIfStatusDESCDirection(from, to, transactionAmount);
        }
    }

    private void changeLimitIfStatusASCDirection(TransactionStatus from, TransactionStatus to, long transactionAmount) {
        if (status.getPriority() >= from.getPriority() && status.getPriority() < to.getPriority()) {
            BiFunction<Long, Long, Long> calculationMethod = this::calculateDecreasedLimit;
            updateLimit(transactionAmount, calculationMethod);
        }
        if (next != null) {
            next.changeLimitIfStatusASCDirection(from, to, transactionAmount);
        }
    }

    private void updateLimit(long transactionAmount, BiFunction<Long, Long, Long> limitCalculationMethod) {
        TransactionStatusEntity current = statusRepository.get(status);
        long newLimit = limitCalculationMethod.apply(current.getMaxAmount(), transactionAmount);
        current.setMaxAmount(newLimit);
        statusRepository.update(current);
    }

    public void changeLimit(TransactionStatus from, TransactionStatus to, long transactionAmount) {
        if (from.getPriority() > to.getPriority()) {
            changeLimitIfStatusDESCDirection(from, to, transactionAmount);
        }
        if (from.getPriority() < to.getPriority()) {
            changeLimitIfStatusASCDirection(from, to, transactionAmount);
        }
    }

    abstract long calculateIncreasedLimit(long oldAmount, long amount);

    abstract long calculateDecreasedLimit(long oldAmount, long amount);
}