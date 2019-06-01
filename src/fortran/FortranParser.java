package fortran;

import parsers_lib.Parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.function.BiFunction;

import static parsers_lib.Parsers.*;

public class FortranParser {
    public static void main(String[] args) {
        String prog=readFile("fortran.f90");

    }

    public static String readFile(String filename){
        StringBuilder s= new StringBuilder();
        Scanner scanner;
        try {
            scanner = new Scanner(new FileReader(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
        while (scanner.hasNextLine()) s.append(scanner.nextLine()).append("\n");
        scanner.close();
        return s.toString();
    }

    public static Parser<String> name(){
        return letter.or(character('_')).next(
                repeat1(letter.or(character('_')).map(c->""+c).or(digit.map(i->""+i)), (a,b)->a+b,"").or(empty("")),(c,s)->c+s);
    }
    public static Parser<String> nextLine(){
        return optionalSpace().next(oneOf(";","\n"))
                .next(repeat(oneOf(";","\n").or(space()),(a,b)->a+b,""));
    }
    public static Parser<String> space(){return repeat1(oneOf(" ","\t"),(a,b)->a+b,"");}

    public static Parser<String> optionalSpace(){
        return space().or(empty(""));
    }

    public static <T,R>Parser<R> programObject(String keyword, Parser<T> body, BiFunction<String,T,R> nameAdder){
        return ignoreCase(keyword).leftBind(space()).rightBind(name()).msg(keyword+": decl error",false)
                .bind(name -> body.leftBind(nextLine()).msg(keyword+": body err",true)
                        .next(
                                ignoreCase("END").next(nextLine()
                                        .or(space().next(ignoreCase(keyword).next(nextLine()
                                                .or(space().next(ignoreCase(name).next(nextLine())))))))
                                        .msg(keyword+": end error",false)
                                ,(a,b)->a)
                        .map(b->nameAdder.apply(name,b))
                );
    }

    public static Parser<String> module() {
        return programObject("module",nextLine().next(exact("...")),(name,body)->name);
    }

    public static Parser<Subroutine> subroutine(){
        return programObject(
                "subroutine",optionalSpace().rightBind(listParams()).leftBind(nextLine()).next(exact("..."), Tuple::new),
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
        Parser<String> delimeter=space().or(empty("")).rightBind(exact(",").rightBind(space().or(empty(""))));
        Parser<List<String>> lasts=repeat(delimeter.rightBind(name()),FortranParser::append,new ArrayList<>());
        Parser<List<String>> listParser=name().next(lasts, FortranParser::prepend);

        return exact("(").rightBind(space().or(empty(""))).rightBind(listParser).leftBind(space().or(empty(""))).leftBind(exact(")"))
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

    private static <T>List<T> prepend(T t,List<T> list){
        List<T> l=new ArrayList<>();
        l.add(t);
        l.addAll(list);
        return l;
    }

    private static <T>List<T> append(List<T> list, T t){
        List<T> l=new ArrayList<>(list);
        l.add(t);
        return l;
    }
}

class Declaration{
    public final Type type;
    public final String name;
    public final String text;

    Declaration(Type type, String name, String text) {
        this.type = type;
        this.name = name;
        this.text = text;
    }

    enum Type{
        MODULE, SUBROUTINE, FUNCTION, EXTERNAL, VARIABLE
    }
}


