package antifraud.business;

import antifraud.persistence.StatusRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatusLimitProcessor {
    private StatusRepositoryService statusRepositoryService;
    private LimitChanger limitChanger;

    @Autowired
    public StatusLimitProcessor(StatusRepositoryService statusRepositoryService) {
        this.statusRepositoryService = statusRepositoryService;
        init();
    }

    private void init() {
        limitChanger = new StatusLimitChanger(statusRepositoryService, TransactionStatus.ALLOWED);
        LimitChanger manualLimitChanger = new StatusLimitChanger(statusRepositoryService, TransactionStatus.MANUAL_PROCESSING);
        LimitChanger prohibitedLimitChanger = new StatusLimitChanger(statusRepositoryService, TransactionStatus.PROHIBITED);
        limitChanger.setNextLimitChanger(manualLimitChanger)
                .setNextLimitChanger(prohibitedLimitChanger);
    }

    public void recalculateLimits(TransactionStatus from,
                                  TransactionStatus to,
                                  long transactionAmount) {
        limitChanger.changeLimit(from, to, transactionAmount);
    }
}