package antifraud.business;

import antifraud.exception.TransactionNotFoundException;
import antifraud.persistence.StatusRepositoryService;
import antifraud.persistence.TransactionStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TransactionsStatusRepositoryService implements StatusRepositoryService {
    TransactionStatusRepository transactionStatusRepository;

    @Autowired
    public TransactionsStatusRepositoryService(TransactionStatusRepository transactionStatusRepository) {
        this.transactionStatusRepository = transactionStatusRepository;
    }

    @Override
    public TransactionStatus getStatusWithAmount(long amount) {
        return transactionStatusRepository
                .findFirstByMaxAmountGreaterThanEqualOrderByMaxAmountAsc(amount)
                .getStatus();
    }

    @Override
    public TransactionStatusEntity get(TransactionStatus status) {
        return transactionStatusRepository
                .findById(status)
                .orElseThrow(TransactionNotFoundException::new);
    }

    @Override
    public TransactionStatusEntity update(TransactionStatusEntity statusEntity) {
        return transactionStatusRepository.save(statusEntity);
    }
}
