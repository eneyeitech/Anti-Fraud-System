package antifraud.business;

import org.springframework.stereotype.Component;

@Component
public class TransactionConverter extends TransactionMapper implements Converter<TransactionEntity, TransactionDTO> {
    @Override
    public TransactionDTO toDTO(TransactionEntity entity) {
        return new TransactionDTO(
                entity.getId(),
                entity.getAmount(),
                entity.getIp(),
                entity.getNumber(),
                entity.getRegion().name(),
                entity.getDate(),
                entity.getStatus().name(),
                entity.getFeedback() == null ? "" : entity.getFeedback().name()
        );
    }
}
