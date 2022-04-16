package antifraud.business;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class FieldDistincter<T> implements Predicate<T> {
    private final Function<T, Object> function;
    private final Set<Object> seenObjects;
    public FieldDistincter(Function<T, Object> function) {
        this.function = function;
        this.seenObjects = new HashSet<>();
    }
    public boolean test(T t) {
        return seenObjects.add(function.apply(t));
    }
}
