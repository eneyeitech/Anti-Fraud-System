package antifraud.persistence;

import antifraud.business.TransactionEntity;
import antifraud.business.WorldRegion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public
interface TransactionHistoryRepository extends CrudRepository<TransactionEntity, Long> {
    List<TransactionEntity> findAllByNumberAndRegionNotAndDateBetween(
            String number,
            WorldRegion region,
            LocalDateTime publicationDateStart,
            LocalDateTime publicationDateEnd
    );

    List<TransactionEntity> findAllByNumberAndIpNotAndDateBetween(
            String number,
            String ip,
            LocalDateTime publicationDateStart,
            LocalDateTime publicationDateEnd
    );
}