package parsers_lib;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Parser<T>{
    T tryParse(CharStream charStream);

    default T parse(CharStream charStream){
        final CharStream.Save save=charStream.save();
        T t= Parser.this.tryParse(charStream);
        if(t!=null)return t;
        charStream.restore(save);
        return null;
    }
    default <P,R> Parser<R> next(Parser<P> parser, BiFunction<T, P, R> f){
        return charStream -> {
            final T t= Parser.this.parse(charStream);
            if(t==null)return null;
            final P p=parser.parse(charStream);
            if(p==null)return null;
            return f.apply(t,p);
        };
    }
    default Parser<T> or(Parser<T> parser){
        return charStream -> {
            final T t= Parser.this.parse(charStream);
            if(t!=null)return t;
            return parser.parse(charStream);
        };
    }
    default <R>Parser<R> map(Function<T, R> f){
        return charStream -> {
            T t=Parser.this.tryParse(charStream);
            if(t==null)return null;
            return f.apply(t);
        };
    }
}
