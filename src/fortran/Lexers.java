package fortran;

import parsers_lib.Parser;

import static parsers_lib.Parsers.*;

public class Lexers {
    public static Parser<String> name(){
        return letter.or(character('_')).next(
                repeat1(letter.or(character('_')).map(c->""+c).or(digit.map(i->""+i)), (a,b)->a+b,"").or(empty("")),(c,s)->c+s);
    }

    public static Parser<String> nextLine(){
        return optionalSpace().next(oneOf(";","\n"))
                .next(repeat(oneOf(";","\n").or(space()),(a,b)->a+b,""));
    }

    public static Parser<String> space(){return repeat1(oneOf(" ","\t"),(a, b)->a+b,"");}

    public static Parser<String> optionalSpace(){
        return space().or(empty(""));
    }
}
