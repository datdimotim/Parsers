package parsers_lib;

import fortran.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class Parsers {

    public static final <T>Parser<T> empty(T t){
      return charStream -> new ParseResult<>(t);
    }

    public static <T>Parser<T> fail(String msg){
        return charStream -> new ParseResult<>(msg,charStream.getPos(),null);
    }

    public static final Parser<Void> eof=charStream -> charStream.isEnd()?new ParseResult<>(null):new ParseResult<>("eof: not eof",charStream.getPos(),null);

    public static final Parser<Integer> digit= charStream -> {
        if(charStream.isEnd())return new ParseResult<>("digit: end of stream", charStream.getPos(),null);
        if(!Character.isDigit(charStream.peek()))return new ParseResult<>("digit: not digit",charStream.getPos(),null);
        return new ParseResult<>(charStream.get()-'0');
    };

    public static final Parser<Integer> unsignedInteger = charStream -> {
        ParseResult<Integer> result=repeat1(digit, (a, t) -> a * 10 + t, 0).parse(charStream);
        if(!result.isError())return result;
        return new ParseResult<>("unsignedInteger: first char is not digit",charStream.getPos(),result);
    };

    public static Parser<Character> character(char c){
        return charStream -> {
            if(charStream.isEnd())return new ParseResult<>("character: end of stream", charStream.getPos(),null);
            char n = charStream.get();
            if (n == c) return new ParseResult<>(c);
            else return new ParseResult<>("character: this char is not equal "+c,charStream.getPos()-1,null);
        };
    };

    public static <T>Parser<T> bracket(Parser<?> l,Parser<T> body,Parser<?> r){
        return charStream->{
            ParseResult<?> left=l.parse(charStream);
            if(left.isError())return new ParseResult<>("bracket: left parser fails",charStream.getPos(),left);

            ParseResult<T> middle=body.parse(charStream);
            if(middle.isError())return new ParseResult<>("bracket: body parser fails", charStream.getPos(),middle);

            ParseResult<?> right=r.parse(charStream);
            if(right.isError())return new ParseResult<>("bracket: right parser fails",charStream.getPos(),right);

            return middle;
        };
    }

    public static Parser<String> exact(String s){
        return charStream -> {
            for (char c:s.toCharArray()){
                ParseResult<Character> r=character(c).parse(charStream);
                if(r.isError())return new ParseResult<>("exact: wrong char",charStream.getPos(),r);
            }
            return new ParseResult<>(s);
        };
    }

    public static Parser<String> ignoreCase(String s){
        return charStream -> {
            for (char c:s.toCharArray()){
                ParseResult<Character> r=character(Character.toLowerCase(c)).or(character(Character.toUpperCase(c))).parse(charStream);
                if(r.isError())return new ParseResult<>("ignoreCase: wrong char",charStream.getPos(),r);
            }
            return new ParseResult<>(s);
        };
    }

    public static Parser<Character> letter=charStream -> {
        if(charStream.isEnd())return new ParseResult<>("letter: end of stream", charStream.getPos(),null);
        if(Character.isLetter(charStream.peek()))return new ParseResult<>(charStream.get());
        return new ParseResult<>("letter: "+charStream.peek()+"is not letter",charStream.getPos(),null);
    };

    public static Parser<String> oneOf(String... s){
        Parser<String> p=exact(s[0]);
        for(int i=1;i<s.length;i++)p=p.or(exact(s[i]));
        Parser<String> f=p;
        return charStream -> {
            ParseResult<String> r=f.parse(charStream);
            if(!r.isError())return r;
            return new ParseResult<>("oneOf: all parsers fails",charStream.getPos(),r);
        };
    }

    public static <T>Parser<List<T>> repeat(Parser<T> body, Parser<?> delimeter){
        Parser<T> lastEntries=delimeter.rightBind(body);
        return body.bind((first)->repeat(lastEntries,Util::append, Collections.singletonList(first)));
    }

    public static <A,T>Parser<A> repeat(Parser<T> parser,BiFunction<A,T,A> accum, A ini){
        return charStream -> {
            A st=ini;
            ParseResult<T> t;
            while (!(t=parser.parse(charStream)).isError())st=accum.apply(st,t.result);
            return new ParseResult<>(st);
        };
    }

    public static <A,T>Parser<A> repeat1(Parser<T> parser,BiFunction<A,T,A> accum, A ini){
        return charStream -> {
            ParseResult<T> first=parser.parse(charStream);
            if(first.isError())return new ParseResult<>("repeat1: first parse fail",charStream.getPos(),first);
            return repeat(parser,accum,accum.apply(ini,first.result)).parse(charStream);
        };
    }
}
