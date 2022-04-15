package antifraud.business;

public interface Converter<E, D> extends DTOMapper<E, D>, EntityMapper<D, E> { }
