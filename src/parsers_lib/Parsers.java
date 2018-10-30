package parsers_lib;

public class Parsers {

    public static final <T>Parser<T> empty(T t){
      return charStream -> t;
    }

    public static final Parser<Object> eof=charStream -> charStream.isEnd()?null:new Object();

    public static final Parser<Integer> digit= charStream -> {
        if(charStream.isEnd())return null;
        if(!Character.isDigit(charStream.peek()))return null;
        return charStream.get()-'0';
    };

    public static final Parser<Integer> integer=charStream -> new RepeatParser<>(digit, (a, t) -> a * 10 + t, 0).parse(charStream);

    public static final Parser<Character> character(char c){
        return charStream -> {
            if(charStream.isEnd())return null;
            char n = charStream.get();
            if (n == c) return c;
            else return null;
        };
    };

    public static <T>Parser<T> bracket(Parser<?> l,Parser<T> body,Parser<?> r){
        return charStream->l.next(body,(a,b)->b).next(r,(a,b)->a).parse(charStream);
    }

    public static Parser<String> exact(String s){
        Parser<String> p=empty("");
        for (char c:s.toCharArray()) {
            p=p.next(character(c),(a,b)->a+b);
        }
        return p;
    }

    public static Parser<String> oneOf(String... s){
        Parser<String> p=exact(s[0]);
        for(int i=1;i<s.length;i++)p=p.or(exact(s[i]));
        return p;
    }
}
