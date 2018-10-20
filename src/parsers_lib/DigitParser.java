package parsers_lib;

public class DigitParser implements Parser<Integer> {

    @Override
    public Integer tryParse(CharStream charStream) {
        if(!Character.isDigit(charStream.peek()))return null;
        return charStream.get()-'0';
    }
}
