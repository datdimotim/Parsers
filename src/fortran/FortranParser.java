package fortran;

import fortran.util.Tuple;
import fortran.util.Util;
import parsers_lib.Parser;

import java.util.*;
import java.util.function.BiFunction;

import static parsers_lib.Parsers.*;

public class FortranParser {

    public static <T,R>Parser<R> programObject(String keyword, Parser<T> body, BiFunction<String,T,R> nameAdder){
        return ignoreCase(keyword).leftBind(Lexers.space()).rightBind(Lexers.name()).msg(keyword+": decl error",false)
                .bind(name -> body.leftBind(Lexers.nextLine()).msg(keyword+": body err",true)
                        .next(
                                ignoreCase("END").next(Lexers.nextLine()
                                        .or(Lexers.space().next(ignoreCase(keyword).next(Lexers.nextLine()
                                                .or(Lexers.space().next(ignoreCase(name).next(Lexers.nextLine())))))))
                                        .msg(keyword+": end error",false)
                                ,(a,b)->a)
                        .map(b->nameAdder.apply(name,b))
                );
    }

    public static Parser<String> module() {
        return programObject("module", Lexers.nextLine().next(exact("...")),(name, body)->name);
    }

    public static Parser<Subroutine> subroutine(){
        return programObject(
                "subroutine", Lexers.optionalSpace().rightBind(listParams()).leftBind(Lexers.nextLine()).next(exact("..."), Tuple::new),
                (name,paramsAndBody)->new Subroutine(name,paramsAndBody.getFst())
        );
    }

    public static class Subroutine{
        private final String name;
        private final List<String> params;

        public Subroutine(String name, List<String> params) {
            this.name = name;
            this.params = params;
        }

        public String getName() {
            return name;
        }

        public List<String> getParams() {
            return params;
        }
    }

    public static Parser<List<String>> listParams(){
        Parser<String> delimeter= Lexers.space().or(empty("")).rightBind(exact(",").rightBind(Lexers.space().or(empty(""))));
        Parser<List<String>> lasts=repeat(delimeter.rightBind(Lexers.name()), Util::append,new ArrayList<>());
        Parser<List<String>> listParser= Lexers.name().next(lasts, Util::prepend);

        return exact("(").rightBind(Lexers.space().or(empty(""))).rightBind(listParser).leftBind(Lexers.space().or(empty(""))).leftBind(exact(")"))
                .or(empty(new ArrayList<>()))
                .bind(l->{
                    Set<String> names=new HashSet<>();
                    for(String name:l){
                        if(names.contains(name))return fail("listParams: multiple declaration params: "+name).map(v->null);
                        names.add(name);
                    };
                    return empty(l);
                });
    }

}


