package antifraud.persistence;

import antifraud.business.SuspiciousIPEntity;
import org.springframework.data.repository.CrudRepository;

public interface SuspiciousIPRepository extends CrudRepository<SuspiciousIPEntity, Long> {
    boolean existsByIp(String ip);

    SuspiciousIPEntity findFirstByIp(String ip);
}
