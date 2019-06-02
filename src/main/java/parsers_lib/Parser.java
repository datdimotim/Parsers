package parsers_lib;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Parser<T>{
    ParseResult<T> tryParse(CharStream charStream);

    default ParseResult<T> parse(CharStream charStream){
        final CharStream.Save save=charStream.save();
        ParseResult<T> t= Parser.this.tryParse(charStream);
        if(!t.isError())return t;
        charStream.restore(save);
        return t;
    }
    default <P,R> Parser<R> next(Parser<P> parser, BiFunction<T, P, R> f){
        return bind(r->parser.map(rr->(f.apply(r,rr))));
    }

    default Parser<T> or(Parser<T> parser){
        return charStream -> {
            final ParseResult<T> t= Parser.this.parse(charStream);
            if(!t.isError())return t;
            return parser.parse(charStream);
        };
    }
    default <R>Parser<R> map(Function<T, R> f){
        return charStream -> tryParse(charStream).map(f);
    }

    default Parser<T> msg(String msg, boolean includePrev){
        return charStream -> {
            ParseResult<T> res=tryParse(charStream);
            if(res.isError())return new ParseResult<>(msg,charStream.getPos(),includePrev?res:null);
            return res;
        };
    }

    default <R,U>Parser<U> nextOrElse(Parser<R> next, BiFunction<T,R,U> f, Parser<U> or){
        return charStream -> {
            ParseResult<T> res=parse(charStream);
            if(res.isError())return or.parse(charStream);
            else return next.map(r->f.apply(res.result,r)).parse(charStream);
        };
    }

    default <R>Parser<R> bind(Function<T,Parser<R>> k){
        return charStream -> {
            final ParseResult<T> t= parse(charStream);
            if(t.isError())return new ParseResult<>(t.errInf,t.posErr,t.causeError);
            return k.apply(t.result).parse(charStream);
        };
    }

    default <R>Parser<R> rightBind(Parser<R> p){
        return next(p,(a,b)->b);
    }

    default <R>Parser<T> leftBind(Parser<R> p){
        return next(p,(a,b)->a);
    }
}
