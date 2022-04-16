package antifraud.business;

import antifraud.persistence.StatusRepositoryService;

public class StatusLimitChanger extends LimitChanger{
    public StatusLimitChanger(StatusRepositoryService statusRepository,
                              TransactionStatus status) {
        super(statusRepository, status);
    }

    @Override
    long calculateIncreasedLimit(long oldAmount, long amount) {
        return (long) Math.ceil(0.8 * oldAmount + 0.2 * amount);
    }

    @Override
    long calculateDecreasedLimit(long oldAmount, long amount) {
        return (long) Math.ceil(0.8 * oldAmount - 0.2 * amount);
    }
}
