package math_expressions;

import parsers_lib.Parser;
import static parsers_lib.Parsers.*;

public class ExpressionParser{
    public static final Parser<Node> expression= charStream -> {
        Node r= sumOfProducts().parse(charStream);
        if(charStream.isEnd())return r;
        return null;
    };
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
        return number().or(variable()).or(function1()).or(expInBrakets());
    }

    private static Parser<Node> number(){
        return integer.map(i->new Node(i+""));
    }

    private static Parser<Node> variable(){
        return exact("x").map(Node::new);
    }

    private static Parser<Node> function1(){
        return oneOf("sin","sinh","cos","cosh").next(expInBrakets(), Node::new);
    }

    private static Parser<Node> expInBrakets(){
        return bracket(exact("("), sumOfProducts(),exact(")"));
    }

    private static Parser<Node> product(){
        return charStream -> {
            final Parser<Node> first = terminal();
            final Parser<Node> others = oneOf("*", "/").next(terminal(), (a, b) -> new Node(a, new Node("1"), b));

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
