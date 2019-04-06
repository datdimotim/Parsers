package math_expressions;


import parsers_lib.ParseResult;
import parsers_lib.Parser;
import parsers_lib.RepeatParser;

import static parsers_lib.Parsers.*;

public class ExpressionParser{
    public static final Parser<Node> expression= sumOfProducts().next(eof,(a,b)->a);

    private static Parser<Node> sumOfProducts(){
        return charStream-> {
            final Parser<Node> first = (exact("-").or(empty("+")).next(product(), (a, b) -> new Node(a, new Node("0"), b)));
            final Parser<Node> others = oneOf("+", "-").next(product(), (a, b) -> new Node(a, new Node("0"), b));

            ParseResult<Node> f=first.parse(charStream);
            if(f.isError())return f;

            ParseResult<Node> next;
            while(!(next=others.parse(charStream)).isError()){
                f=new ParseResult<>(new Node("+",f.result,next.result));
            }

            return f;
        };
    }

    private static Parser<NodeEx> terminal(){
        return skipSpaces(
                number().map(node->new NodeEx(node,HT.DIGIT,HT.DIGIT))
                        .or(variable().map(node -> new NodeEx(node,HT.LETTER,HT.LETTER)))
                        .or(function1().map(node -> new NodeEx(node,HT.LETTER,HT.BRACKET)))
                        .or(constant().map(node -> new NodeEx(node,HT.LETTER,HT.LETTER)))
                        .or(expInBrakets().map(node -> new NodeEx(node,HT.BRACKET,HT.BRACKET))));
    }

    private static Parser<NodeEx> power(){
        return charStream-> {
            ParseResult<NodeEx> first=terminal().parse(charStream);
            if(first.isError())return first;
            if(exact("^").parse(charStream).isError())return first;
            ParseResult<NodeEx> last=terminal().parse(charStream);
            if(last.isError())return last;
            return new ParseResult<>(new NodeEx(new Node("^",first.result.node,last.result.node),first.result.startsWith,last.result.endsWith));
        };
    }

    private static Parser<Node> number(){
        return integer.map(i->new Node(i+""));
    }

    private static Parser<Node> constant(){return oneOf("e","pi").map(Node::new);}

    private static Parser<Node> variable(){
        return exact("x").map(Node::new);
    }

    private static Parser<Node> function1(){
        return exact("sin").next(expInBrakets(), Node::new)
                .or(exact("sinh").next(expInBrakets(),Node::new))
                .or(exact("cos").next(expInBrakets(),Node::new))
                .or(exact("cosh").next(expInBrakets(),Node::new))
                .or(exact("tan").next(expInBrakets(),Node::new))
                .or(exact("tanh").next(expInBrakets(),Node::new))
                .or(exact("asin").next(expInBrakets(),Node::new))
                .or(exact("acos").next(expInBrakets(),Node::new))
                .or(exact("atan").next(expInBrakets(),Node::new))
                .or(exact("exp").next(expInBrakets(),Node::new))
                .or(exact("ln").next(expInBrakets(),Node::new));
    }

    private static Parser<Node> expInBrakets(){
        return skipSpaces(bracket(exact("("), sumOfProducts(),exact(")")));
    }

    private static <T>Parser<T> skipSpaces(Parser<T> parser){
        Parser<Object> p=new RepeatParser<>(oneOf(" ","\t","\r","\n"),(a,b)->new Object(),new Object()).or(empty(new Object()));
        return p.next(parser, (a,b)->b).next(p,(a,b)->a);
    }

    private static Parser<Node> product(){
        return charStream -> {
            final Parser<NodeEx> first = power();
            final Parser<NodeEx> others = (oneOf("*", "/").or(empty(""))).next(power(),
                    (a, b) -> new NodeEx(
                            new Node(a.equals("")?"*":a, new Node("1"), b.node),
                            a.equals("")?b.startsWith:HT.SIGN,
                            b.endsWith
                    ));

            ParseResult<NodeEx> f=first.parse(charStream);
            if(f.isError())return new ParseResult<>(f.errInf,f.posErr);

            ParseResult<NodeEx> next;
            while (!(next=others.parse(charStream)).isError()){
                if(f.result.endsWith==HT.LETTER&&next.result.startsWith==HT.LETTER)return new ParseResult<>("letter after letter",charStream.getPos());
                if(f.result.endsWith==HT.DIGIT&&next.result.startsWith==HT.DIGIT)return new ParseResult<>("digit after digit",charStream.getPos());
                f=new ParseResult<>(new NodeEx(
                        new Node("*",f.result.node,next.result.node),
                        f.result.startsWith,
                        next.result.endsWith
                ));
            }

            return new ParseResult<>(f.result.node);
        };
    }
}

enum HT{
    LETTER,BRACKET,DIGIT,SIGN
}

class NodeEx{
    final Node node;
    final HT startsWith;
    final HT endsWith;

    NodeEx(Node node, HT startsWith, HT endsWith) {
        this.node = node;
        this.startsWith = startsWith;
        this.endsWith = endsWith;
    }
}
