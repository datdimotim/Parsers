package parsers_lib;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Parser<T>{
    T tryParse(CharStream charStream);

    default T parse(CharStream charStream){
        if(charStream.isEnd())return null;
        final CharStream.Save save=charStream.save();
        T t= Parser.this.tryParse(charStream);
        if(t!=null)return t;
        charStream.restore(save);
        return null;
    }
    default <P,R> Parser<R> next(Parser<P> parser, BiFunction<T, P, R> f){
        return new Parser<R>() {
            @Override
            public R tryParse(CharStream charStream) {
                final T t= Parser.this.parse(charStream);
                if(t==null)return null;
                final P p=parser.parse(charStream);
                if(p==null)return null;
                return f.apply(t,p);
            }
        };
    }
    default Parser<T> or(Parser<T> parser){
        return new Parser<T>() {
            @Override
            public T tryParse(CharStream charStream) {
                final T t= Parser.this.parse(charStream);
                if(t==null){
                    final T t1=parser.parse(charStream);
                    if(t1==null)return null;
                    return t1;
                }
                return t;
            }
        };
    }
    default <R>Parser<R> map(Function<T, R> f){
        return new Parser<R>() {
            @Override
            public R tryParse(CharStream charStream) {
                T t=Parser.this.tryParse(charStream);
                if(t==null)return null;
                return f.apply(t);
            }
        };
    }
}
