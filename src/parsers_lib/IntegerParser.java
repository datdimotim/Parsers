package parsers_lib;

public class IntegerParser implements Parser<Integer> {

    @Override
    public Integer tryParse(CharStream charStream) {
        return new RepeatParser<>(new DigitParser(), (a, t) -> a * 10 + t, 0).tryParse(charStream);
    }
}
