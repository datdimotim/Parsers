package fortran;

import fortran.util.Tuple;
import fortran.util.Util;
import parsers_lib.Parser;
import parsers_lib.Parsers;

import java.util.*;
import java.util.function.BiFunction;

import static fortran.Lexers.*;
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
                "subroutine", optionalSpace().rightBind(listParams()).leftBind(Lexers.nextLine()).next(exact("..."), Tuple::new),
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

    public static Parser<Map<String,FullType>> declarationList(){
        Parser<FortranType> integer=ignoreCase("integer").map(e->FortranType.INTEGER);
        Parser<FortranType> real=ignoreCase("real").map(e->FortranType.REAL);
        Parser<FortranType> logical=ignoreCase("logical").map(e->FortranType.LOGICAL);
        Parser<FortranType> character=ignoreCase("character").map(e->FortranType.CHARACTER);
        Parser<Integer> nonStringKinds=oneOf("1","2","4","8").map(Integer::parseInt);

        Parser<FullType> nonStringTypesParser=integer.or(real).or(logical)
                .leftBind(optionalSpace())
                .leftBind(exact("*"))
                .leftBind(optionalSpace())
                .next(nonStringKinds,(t,k)->new FullType(t,k));

        Parser<FullType> stringTypeParser=character
                .leftBind(optionalSpace())
                .leftBind(exact("*"))
                .leftBind(optionalSpace())
                .next(Parsers.unsignedInteger,(t, k)->new FullType(t,k));

        Parser<FullType> typeParser=nonStringTypesParser.or(stringTypeParser);
        Parser<List<String>> names=repeat(name(),optionalSpace().rightBind(exact(",")).rightBind(optionalSpace()));

        Parser<Tuple<FullType,List<String>>> line=typeParser
                .leftBind(optionalSpace()).leftBind(exact("::")).leftBind(optionalSpace())
                .next(names,Tuple::new);

        Parser<List<Tuple<FullType,List<String>>>>raw=repeat(line,nextLine());

        return raw.bind(r->{
            Map<String,FullType> params=new HashMap<>();
            for(Tuple<FullType,List<String>> typeLine:r){
                for(String varName:typeLine.getSnd()){
                    if(params.containsKey(varName))return fail("declarationList: double declaration: "+varName);
                    params.put(varName,typeLine.getFst());
                }
            }
            return empty(params);
        });
    }

    public static class FullType{
        public final FortranType type;
        public final int kind;

        public FullType(FortranType type, int kind) {
            this.type = type;
            this.kind = kind;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FullType fullType = (FullType) o;
            return kind == fullType.kind &&
                    type == fullType.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, kind);
        }
    }

    public enum  FortranType{
        INTEGER,CHARACTER,LOGICAL,REAL
    }
}


