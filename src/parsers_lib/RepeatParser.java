package parsers_lib;

import java.util.function.BiFunction;

public class RepeatParser<A,T> implements Parser<A>{
    private final Parser<T> parser;
    private final BiFunction<A,T,A> accum;
    private final A ini;
    public RepeatParser(Parser<T> parser,BiFunction<A,T,A> accum,A ini){
        this.parser=parser;
        this.accum=accum;
        this.ini=ini;
    }
    @Override
    public A tryParse(CharStream charStream) {
        A st=ini;
        T t=parser.parse(charStream);
        if(t==null)return null;
        st=accum.apply(st,t);
        while (null!=(t=parser.parse(charStream)))st=accum.apply(st,t);
        return st;
    }
}
