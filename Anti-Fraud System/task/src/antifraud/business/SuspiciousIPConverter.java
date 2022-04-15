package antifraud.business;

import org.springframework.stereotype.Component;

@Component
public class SuspiciousIPConverter implements Converter<SuspiciousIPEntity, SuspiciousIpDTO> {

    @Override
    public SuspiciousIpDTO toDTO(SuspiciousIPEntity entity) {
        return new SuspiciousIpDTO(entity.getId(), entity.getIp());
    }

    @Override
    public SuspiciousIPEntity toEntity(SuspiciousIpDTO dto) {
        return new SuspiciousIPEntity(dto.getId(), dto.getIp());
    }
}
