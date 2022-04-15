package antifraud.business;

import antifraud.persistence.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<UserEntity, UserDTO> {
    @Override
    public UserDTO toDTO(UserEntity entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .username(entity.getUsername())
                .build();
    }

    @Override
    public UserEntity toEntity(UserDTO dto) {
        return new UserEntity(
                dto.getName(),
                dto.getUsername(),
                dto.getPassword()
        );
    }
}
