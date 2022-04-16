package antifraud.persistence;

import antifraud.business.TransactionStatus;
import antifraud.business.TransactionStatusEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionStatusRepository extends CrudRepository<TransactionStatusEntity, TransactionStatus> {
    TransactionStatusEntity findFirstByMaxAmountGreaterThanEqualOrderByMaxAmountAsc(long amount);
}