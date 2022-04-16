package antifraud.persistence;

import antifraud.business.TransactionStatus;
import antifraud.business.TransactionStatusEntity;

public interface StatusRepositoryService {
    TransactionStatus getStatusWithAmount(long amount);

    TransactionStatusEntity get(TransactionStatus status);

    TransactionStatusEntity update(TransactionStatusEntity statusEntity);
}
