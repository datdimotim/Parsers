package parsers_lib;

public class CharParser implements Parser<Character> {
    private final char c;
    public CharParser(char c){
        this.c=c;
    }
    @Override
    public Character tryParse(CharStream charStream) {
        if(charStream.peek()!=c)return null;
        charStream.get();
        return c;
    }
}
