package fortran.util;

import lombok.Value;

@Value
public class Tuple<U,V> {
    U fst;
    V snd;
}
