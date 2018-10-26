package math_expressions;

import java.util.function.Function;

public class FunctionBuilder {
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
