package math_expressions;


import parsers_lib.Parser;
import parsers_lib.RepeatParser;

import static parsers_lib.Parsers.*;

public class ExpressionParser{
    public static final Parser<Node> expression= sumOfProducts().next(eof,(a,b)->a);

    private static Parser<Node> sumOfProducts(){
        return charStream-> {
            final Parser<Node> first = (exact("-").or(empty("+")).next(product(), (a, b) -> new Node(a, new Node("0"), b)));
            final Parser<Node> others = oneOf("+", "-").next(product(), (a, b) -> new Node(a, new Node("0"), b));

            Node f=first.parse(charStream);
            if(f==null)return null;

            Node next;
            while (null!=(next=others.parse(charStream))){
                f=new Node("+",f,next);
            }

            return f;
        };
    }

    private static Parser<Node> terminal(){
        return skipSpaces(number().or(variable()).or(function1()).or(constant()).or(expInBrakets()));
    }

    private static Parser<Node> power(){
        return terminal().next((exact("^").next(terminal(),(a,b)->b)).or(empty(new Node("1"))),(a, b)->new Node("^",a,b));
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
            final Parser<Node> first = power();
            final Parser<Node> others = (oneOf("*", "/").or(empty("*"))).next(power(), (a, b) -> new Node(a, new Node("1"), b));

            Node f=first.parse(charStream);
            if(f==null)return null;

            Node next;
            while (null!=(next=others.parse(charStream))){
                f=new Node("*",f,next);
            }

            return f;
        };
    }
}
