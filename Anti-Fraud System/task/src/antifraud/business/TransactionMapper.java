package antifraud.business;

import org.springframework.stereotype.Component;

@Component
public class TransactionMapper implements EntityMapper<TransactionDTO, TransactionEntity> {
    @Override
    public TransactionEntity toEntity(TransactionDTO dto) {
        return new TransactionEntity(
                dto.getAmount(),
                dto.getIp(),
                dto.getNumber(),
                WorldRegion.valueOf(dto.getRegion()),
                dto.getDate()
        );
    }
}