import parsers_lib.CharParser;
import parsers_lib.CharStream;
import parsers_lib.IntegerParser;
import parsers_lib.Parser;

import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        Parser<String> p=new CharParser('a').or(new CharParser('b')).or(new CharParser('c'))
                .next(new CharParser('b'),(Character aa,Character bb)->new String(new char[]{aa,bb}));

        System.out.println(new IntegerParser().next(p,(a, b)->a+b).parse(new CharStream("1234cba")));
        System.out.println(new Exp().parse(new CharStream("(1+(2+x*4))/(54*6)")).toString());

        {
            Function<Integer, Integer> f = build(new Exp().parse(new CharStream("x*x*x+3*x*x+3*x+1")));
            for (int i = 0; i < 10; i++) System.out.println("x=" + i + " f(x)=" + f.apply(i));
        }
        System.out.println("========================");
        {
            Function<Integer, Integer> f = build(new Exp().parse(new CharStream("(x-1)*(x*x+x+1)")));
            for (int i = 0; i < 10; i++) System.out.println("x=" + i + " f(x)=" + f.apply(i));
        }
    }

    private static Function<Integer,Integer> build(Node node){
        if(node.left==null){
            if(node.right!=null)throw new RuntimeException();
            if(node.val.equals("x"))return (x)->x;
            int ch=Integer.parseInt(node.val);
            return (x)->ch;
        }
        if(node.right==null)throw new RuntimeException();

        Function<Integer,Integer> l=build(node.left);
        Function<Integer,Integer> r=build(node.right);
        if(node.val.equals("+"))return (x->l.apply(x)+r.apply(x));
        if(node.val.equals("-"))return (x->l.apply(x)-r.apply(x));
        if(node.val.equals("*"))return (x->l.apply(x)*r.apply(x));
        if(node.val.equals("/"))return (x->l.apply(x)/r.apply(x));
        throw new RuntimeException();
    }
}



class Exp implements Parser<Node>{

    @Override
    public Node tryParse(CharStream charStream) {
        final Node l=new P().parse(charStream);
        if(l==null)return null;
        final Node res=(new CharParser('+').or(new CharParser('-'))).next(new Exp(),(a,b)->new Node(a+"",l,b)).parse(charStream);
        if(res!=null)return res;
        return l;
    }
}

class P implements Parser<Node>{

    @Override
    public Node tryParse(CharStream charStream) {
        final Node l=new T().parse(charStream);
        if(l==null)return null;
        final Node res=(new CharParser('*').or(new CharParser('/'))).next(new P(),(a,b)->new Node(a+"",l,b)).parse(charStream);
        if(res!=null)return res;
        return l;
    }
}

class T implements Parser<Node>{
    @Override
    public Node tryParse(CharStream charStream) {
        final Node ch= (new IntegerParser().map(i->new Node(i+"",null,null)))
                .or(new CharParser('x').map(x->new Node("x",null,null)))
                .parse(charStream);
        if(ch!=null)return ch;
        return new CharParser('(').next(new Exp(),(a,b)->b).next(new CharParser(')'),(a,b)->a).parse(charStream);
    }
}


class Node{
    final String val;
    final Node left;
    final Node right;

    Node(String val, Node left, Node right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        if(left==null){
            if(right!=null)throw new RuntimeException();
            return val;
        }
        if(right==null){
            return val+"("+left.toString()+")";
        }
        return "("+left.toString()+val+right.toString()+")";
    }
}