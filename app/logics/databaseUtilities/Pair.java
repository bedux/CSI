package logics.databaseUtilities;

/**
 * Created by bedux on 29/04/16.
 */
public class Pair<T,S> {
    final T key;
    final S value;

    public T getKey() {
        return key;
    }

    public S getValue() {
        return value;
    }

    public Pair(T key, S value) {

        this.key = key;
        this.value = value;
    }
}
