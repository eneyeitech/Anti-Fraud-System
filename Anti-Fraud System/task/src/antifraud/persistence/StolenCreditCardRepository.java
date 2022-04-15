package antifraud.persistence;

import antifraud.business.StolenCreditCardEntity;
import org.springframework.data.repository.CrudRepository;

public interface StolenCreditCardRepository extends CrudRepository<StolenCreditCardEntity, Long> {
    boolean existsByNumber(String number);

    StolenCreditCardEntity findFirstByNumber(String cardNumber);
}
