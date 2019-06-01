package fortran.util;

import java.util.Objects;

public class Tuple<U,V> {
    private final U fst;
    private final V snd;

    public Tuple(U fst, V snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public U getFst() {
        return fst;
    }

    public V getSnd() {
        return snd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(fst, tuple.fst) &&
                Objects.equals(snd, tuple.snd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fst, snd);
    }
}
