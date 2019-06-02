package math_expressions;

import java.util.function.Function;

public class FunctionBuilder {
    public static Function<Double,Double> build(Node node){
        if(node.left==null){
            if(node.right!=null)throw new RuntimeException();
            if(node.val.equals("x"))return x->x;
            if(node.val.equals("e"))return x->Math.E;
            if(node.val.equals("pi"))return x->Math.PI;
            double ch=Double.parseDouble(node.val);
            return x->ch;
        }
        if(node.right==null){
            if(node.val.equals("sin"))return x->Math.sin(build(node.left).apply(x));
            if(node.val.equals("asin"))return x->Math.asin(build(node.left).apply(x));
            if(node.val.equals("sinh"))return x->Math.sinh(build(node.left).apply(x));
            if(node.val.equals("cos"))return x->Math.cos(build(node.left).apply(x));
            if(node.val.equals("acos"))return x->Math.acos(build(node.left).apply(x));
            if(node.val.equals("cosh"))return x->Math.cosh(build(node.left).apply(x));
            if(node.val.equals("tan"))return x->Math.tan(build(node.left).apply(x));
            if(node.val.equals("atan"))return x->Math.atan(build(node.left).apply(x));
            if(node.val.equals("tanh"))return x->Math.tanh(build(node.left).apply(x));
            if(node.val.equals("ln"))return x->Math.log(build(node.left).apply(x));
            if(node.val.equals("exp"))return x->Math.exp(build(node.left).apply(x));
            throw new RuntimeException();
        }

        Function<Double,Double> l=build(node.left);
        Function<Double,Double> r=build(node.right);
        if(node.val.equals("^"))return (x->Math.pow(l.apply(x),r.apply(x)));
        if(node.val.equals("+"))return (x->l.apply(x)+r.apply(x));
        if(node.val.equals("-"))return (x->l.apply(x)-r.apply(x));
        if(node.val.equals("*"))return (x->l.apply(x)*r.apply(x));
        if(node.val.equals("/"))return (x->l.apply(x)/r.apply(x));
        throw new RuntimeException();
    }
}
