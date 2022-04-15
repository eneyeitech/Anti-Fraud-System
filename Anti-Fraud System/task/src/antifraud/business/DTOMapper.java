package antifraud.business;

public interface DTOMapper<E, D> {
    D toDTO(E entity);
}
