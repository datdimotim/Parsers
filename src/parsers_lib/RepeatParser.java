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
    public ParseResult<A> tryParse(CharStream charStream) {
        A st=ini;
        ParseResult<T> t=parser.parse(charStream);
        if(t.isError())return new ParseResult<>(t.errInf,t.posErr);
        st=accum.apply(st,t.result);
        while (!(t=parser.parse(charStream)).isError())st=accum.apply(st,t.result);
        return new ParseResult<>(st);
    }
}
