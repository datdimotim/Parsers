import parsers_lib.*;

import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        {
            Function<Double, Double> f = build(Exp.expression.parse(new CharStream("x*x*x+3*x*x+3*x+1")));
            for (int i = 0; i < 10; i++) System.out.println("x=" + i + " f(x)=" + f.apply((double)i));
        }
        System.out.println("========================");
        {
            Function<Double, Double> f = build(Exp.expression.parse(new CharStream("(x-1)*(x*x+x+1)")));
            for (int i = 0; i < 10; i++) System.out.println("x=" + i + " f(x)=" + f.apply((double)i));
        }
        System.out.println("========================");
        {
            Function<Double, Double> f = build(Exp.expression.parse(new CharStream("(sin(x)+cos(x))*(sin(x)+cos(x))-2*sin(x)*cos(x)")));
            for (int i = 0; i < 10; i++) System.out.println("x=" + i + " f(x)=" + f.apply((double)i));
        }

        System.out.println(Exp.expression.parse(new CharStream("sin(x)")).toString());
        System.out.println(
                Parsers.bracket(Parsers.exact("sin("),Parsers.character('x'),Parsers.exact(")"))
                        .or(Parsers.bracket(Parsers.exact("cos("),Parsers.character('x'),Parsers.exact(")")))
                        .parse(new CharStream("sin(x)")));
    }

    public static Function<Double,Double> build(Node node){
        if(node.left==null){
            if(node.right!=null)throw new RuntimeException();
            if(node.val.equals("x"))return x->x;
            double ch=Double.parseDouble(node.val);
            return x->ch;
        }
        if(node.right==null){
            if(node.val.equals("sin"))return x->Math.sin(build(node.left).apply(x));
            if(node.val.equals("sinh"))return x->Math.sinh(build(node.left).apply(x));
            if(node.val.equals("cos"))return x->Math.cos(build(node.left).apply(x));
            throw new RuntimeException();
        }

        Function<Double,Double> l=build(node.left);
        Function<Double,Double> r=build(node.right);
        if(node.val.equals("+"))return (x->l.apply(x)+r.apply(x));
        if(node.val.equals("-"))return (x->l.apply(x)-r.apply(x));
        if(node.val.equals("*"))return (x->l.apply(x)*r.apply(x));
        if(node.val.equals("/"))return (x->l.apply(x)/r.apply(x));
        throw new RuntimeException();
    }
}

class Exp{
    public static final Parser<Node> expression=charStream -> {
        Node r=exp().parse(charStream);
        if(charStream.isEnd())return r;
        return null;
    };
    private static Parser<Node> exp(){
        return charStream -> {
            final Node l=p().parse(charStream);
            if(l==null)return null;
            final Node res=(Parsers.character('+').or(Parsers.character('-'))).next(exp(),(a,b)->new Node(a+"",l,b)).parse(charStream);
            if(res!=null)return res;
            return l;
        };
    }


    private static Parser<Node> t(){
        return (dble.map(i->new Node(i+"",null,null)))
                .or(Parsers.character('x').map(x->new Node("x",null,null)))
                .or(Parsers.bracket(Parsers.exact("sin("),exp(),Parsers.exact(")")).map(a->new Node("sin",a,null)))
                .or(Parsers.bracket(Parsers.exact("sinh("),exp(),Parsers.exact(")")).map(a->new Node("sinh",a,null)))
                .or(Parsers.bracket(Parsers.exact("cos("),exp(),Parsers.exact(")")).map(a->new Node("cos",a,null)))
                .or(Parsers.character('(').next(exp(),(a,b)->b).next(Parsers.character(')'),(a,b)->a));
    }


    private static Parser<Node> p(){
        return charStream -> {
            final Node l=t().parse(charStream);
            if(l==null)return null;
            final Node res=(Parsers.character('*').or(Parsers.character('/'))).next(p(),(a,b)->new Node(a+"",l,b)).parse(charStream);
            if(res!=null)return res;
            return l;
        };
    }
    public static final Parser<Double> dble=Parsers.integer.map(Integer::doubleValue);
}




enum Type{
    X,
    SUM,SUB,MUL,DIV,
    SIN,SINH,
    COS,COSH
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