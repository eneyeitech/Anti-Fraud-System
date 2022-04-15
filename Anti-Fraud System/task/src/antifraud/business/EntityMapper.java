package antifraud.business;

public interface EntityMapper<D, E> {
    E toEntity(D dto);
}
