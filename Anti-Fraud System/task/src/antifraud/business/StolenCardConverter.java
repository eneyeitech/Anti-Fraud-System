package antifraud.business;

import org.springframework.stereotype.Component;

@Component
public class StolenCardConverter implements Converter<StolenCreditCardEntity, StolenCreditCardDTO> {
    @Override
    public StolenCreditCardDTO toDTO(StolenCreditCardEntity entity) {
        return new StolenCreditCardDTO(entity.getId(), entity.getNumber());
    }

    @Override
    public StolenCreditCardEntity toEntity(StolenCreditCardDTO dto) {
        return new StolenCreditCardEntity(dto.getId(), dto.getNumber());
    }
}
