package fortran;

import parsers_lib.CharStream;
import parsers_lib.ParseResult;
import parsers_lib.Parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

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
        return space().or(empty("")).next(oneOf(";","\n"))
                .next(repeat(oneOf(";","\n").or(space()),(a,b)->a+b,""));
    }
    public static Parser<String> space(){return repeat1(oneOf(" ","\t"),(a,b)->a+b,"");};

    public static <T,R>Parser<R> programObject(String keyword, Parser<T> body, BiFunction<String,T,R> nameAdder){
        return ignoreCase(keyword).leftBind(space()).rightBind(name()).msg(keyword+": decl error",false)
                .bind(name -> body.leftBind(nextLine()).msg(keyword+": body err",false)
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

    public static Parser<ArrayList<String>> listParams(){
        Parser<String> delimeter=space().or(empty("")).rightBind(exact(",").rightBind(space().or(empty(""))));
        Parser<ArrayList<String>> lasts=repeat(delimeter.rightBind(name()),FortranParser::append,new ArrayList<>());
        Parser<ArrayList<String>> listParser=name().next(lasts, FortranParser::prepend);

        return exact("(").rightBind(space().or(empty(""))).rightBind(listParser).leftBind(space().or(empty(""))).leftBind(exact(")"))
                .msg("listParams: fail parse list of params",false)
                .bind(l->{
                    Set<String> names=new HashSet<>();
                    for(String name:l){
                        if(names.contains(name))return fail("listParams: multiple declaration params: "+name).map(v->null);
                        names.add(name);
                    };
                    return empty(l);
                });
    }

    private static <T>ArrayList<T> prepend(T t,ArrayList<T> list){
        ArrayList<T> l=new ArrayList<>();
        l.add(t);
        l.addAll(list);
        return l;
    }

    private static <T>ArrayList<T> append(ArrayList<T> list, T t){
        ArrayList<T> l=new ArrayList<>(list);
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


class Test{
    public static void main(String[] args) {
        testName();
        testModule();
        testListParams();
    }

    public static void testModule(){
        Parser<String> module=FortranParser.module();
        String[] accept={
                "MoDule a \n...\neNd mOdulE a\n",
                "module    \t aS_1   \n\n  \n ...\n  \n \n \t end \t module \t as_1\n",
                "module  b\n...\n   end\t module\n",
                "module   ccd_\n \t ...\n\t enD\n",
        };
        final String[] names={"a","as_1","b","ccd_"};

        String[] wrong={"module \n a ... end","module as\n..\nend","module ass\n...\nend module as;"};
        String[] mesgs={"decl","body","end"};

        for(int i=0;i<accept.length;i++){
            final int ii=i;
            checkPositive(module,accept[i],name->name.toLowerCase().equals(names[ii]));
        }
        for(int i=0;i<wrong.length;i++){
            final int ii=i;
            checkNegative(module,wrong[i],err->err.startsWith("module: "+mesgs[ii]));
        }
    }

    public static void testListParams(){
        Parser<ArrayList<String>> module=FortranParser.listParams();
        String[] accept={
                "(a,b,c,d)",
                "(  as , bAs , daa_w    ,c2  )  ",
                "(a)",
                "(  wW_1 \t)",
        };
        final String[][] names={{"a","b","c","d"},{"as","bAs","daa_w","c2"},{"a"},{"ww_1"}};

        String[] wrong={"(a,b,d,a)"};
        String[] mesgs={"decl","body","end"};

        for(int i=0;i<accept.length;i++){
            final int ii=i;
            checkPositive(module,accept[i],list->{
                for(int iname=0;iname<names[ii].length;iname++){
                    if(!names[ii][iname].toLowerCase().equals(list.get(iname).toLowerCase()))return false;
                }
                return true;
            });
        }
        for(int i=0;i<wrong.length;i++){
            final int ii=i;
            checkNegative(module,wrong[i],err->true);
        }
    }

    public static void testName(){
        Parser<String> name=FortranParser.name();
        String[] accept={"func","f","f12","f1f2f32cc3d22","f_12","___asw","ssww2___","s swew 2 2"};
        String[] wrong={" ssa","","2ssq","01qww sdw"};

        for(String a:accept)checkPositive(name,a,s->true);
        for(String w:wrong)checkNegative(name,w,s->true);
    }

    private static <T>void checkPositive(Parser<T> parser, String string, Predicate<T> checker){
        CharStream stream=new CharStream(string);
        ParseResult<T> result=parser.parse(stream);
        if(result.isError()){
            result.printError(stream);
            throw new RuntimeException(string);

        }
        if(!checker.test(result.result))throw new RuntimeException(result.result.toString());
    }

    private static <T>void checkNegative(Parser<T> parser,String string,Predicate<String> checkMesg){
        CharStream stream=new CharStream(string);
        ParseResult<T> result=parser.parse(stream);
        if(!result.isError())throw new RuntimeException(string);
        if(!checkMesg.test(result.errInf))throw new RuntimeException(string+"\n\n"+result.errInf);
    }
}