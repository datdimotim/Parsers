package fortran;

import fortran.util.Tuple;
import fortran.util.Util;
import lombok.Value;
import parsers_lib.Parser;
import parsers_lib.Parsers;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static fortran.Lexers.*;
import static parsers_lib.Parsers.*;

public class FortranParser {

    public static <T,R>Parser<R> programObject(String keyword, Parser<T> body, BiFunction<String,T,R> nameAdder){
        return ignoreCase(keyword).leftBind(Lexers.space()).rightBind(Lexers.name()).msg(keyword+": decl error",false)
                .bind(name -> body.leftBind(Lexers.nextLine()).msg(keyword+": body err",true)
                        .next(
                                ignoreCase("END").leftBind(Lexers.nextLine()
                                        .or(Lexers.space().leftBind(ignoreCase(keyword).leftBind(Lexers.nextLine()
                                                .or(Lexers.space().rightBind(ignoreCase(name).leftBind(Lexers.nextLine())))))))
                                        .msg(keyword+": end error",false)
                                ,(a,b)->a)
                        .map(b->nameAdder.apply(name,b))
                );
    }

    public static Parser<String> module() {
        return programObject("module", Lexers.nextLine().leftBind(exact("...")),(name, body)->name);
    }

    public static Parser<Subroutine> subroutine(){
        BiPredicate<List<String>, Set<String>> isAllParamsDeclared=(params, declarations)->{
            for(String param:params)if(!declarations.contains(param))return false;
            return true;
        };
        return programObject(
                "subroutine", optionalSpace().rightBind(listParams()).leftBind(Lexers.nextLine()).next(
                        (declarationList().leftBind(nextLine()).or(empty(new HashMap<>()))).next(exact("..."),Tuple::new),
                        Tuple::new),Tuple::new)
                .valid(
                        raw->isAllParamsDeclared.test(raw.getSnd().getFst(),raw.getSnd().getSnd().getFst().keySet())
                        ,"subroutine: not all parameters are declared")
                .map(raw->new Subroutine(raw.getFst(),raw.getSnd().getFst(),raw.getSnd().getSnd().getFst()));
    }

    @Value
    public static class Subroutine{
        String name;
        List<String> params;
        Map<String,FullType> variables;

        public Subroutine(String name, List<String> params, Map<String,FullType> variables) {
            this.name = name;
            this.params = params;
            this.variables=variables;
            params.forEach(p->{
                if(!variables.containsKey(p))throw new RuntimeException();
            });
        }
    }

    public static Parser<List<String>> listParams(){
        Parser<String> delimeter= optionalSpace().rightBind(exact(",").rightBind(optionalSpace()));
        Parser<List<String>> lasts=repeat(delimeter.rightBind(Lexers.name()), Util::append,new ArrayList<>());
        Parser<List<String>> listParser= Lexers.name().next(lasts, Util::prepend);

        return exact("(").rightBind(optionalSpace()).rightBind(listParser).leftBind(optionalSpace()).leftBind(exact(")"))
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

    @Value
    public static class FullType{
        FortranType type;
        int kind;
    }

    public enum  FortranType{
        INTEGER,CHARACTER,LOGICAL,REAL
    }
}


