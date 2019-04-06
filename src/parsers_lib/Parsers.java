package parsers_lib;

public class Parsers {

    public static final <T>Parser<T> empty(T t){
      return charStream -> new ParseResult<>(t);
    }

    public static final Parser<Void> eof=charStream -> charStream.isEnd()?new ParseResult<>(null):new ParseResult<>("this is not eof",charStream.getPos());

    public static final Parser<Integer> digit= charStream -> {
        if(charStream.isEnd())return new ParseResult<>("end of stream", charStream.getPos());
        if(!Character.isDigit(charStream.peek()))return new ParseResult<>("not digit",charStream.getPos());
        return new ParseResult<>(charStream.get()-'0');
    };

    public static final Parser<Integer> integer=charStream -> new RepeatParser<>(digit, (a, t) -> a * 10 + t, 0).parse(charStream);

    public static Parser<Character> character(char c){
        return charStream -> {
            if(charStream.isEnd())return new ParseResult<>("end of stream", charStream.getPos());
            char n = charStream.get();
            if (n == c) return new ParseResult<>(c);
            else return new ParseResult<>("this char is not equal "+c,charStream.getPos());
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
