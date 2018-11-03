package math_expressions.tests;

import math_expressions.ExpressionParser;
import math_expressions.FunctionBuilder;
import math_expressions.Node;
import parsers_lib.CharStream;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Tests {
    public static void main(String[] args) {
        final double x=3;
        final double eps=0.000000000001;
        String[] accept={
                "x", // 3
                "0", // 0
                "-1",// -1
                "(x)", // 3
                "((x))", // 3
                "x^2", // 9
                "x+1+x", // 7
                "x*1*x", // 9
                "3*2^5*5", // 15*32
                "2/3/4",   // 1.0/6.0
                "1-2-4",   // -5
                "3-3/2^3", // 3-3.0/8.0
                "(x+3)^2*(x^2-x*3+3)^2", //  324
                "sin(x)^2+cos(x)^2", // 1
                "cosh(x)^2-sinh(x)^2", // 1
                "(x^2+2*x+1)/(x+1)",  // 4
                "(x^2+2*x+1)/(x+1)/(x+1)",  // 1
                "(x^2+2*x+1)/(x+1)^2",  // 4
                "(x)(x)", // 9
                "(x+1)(x+1)", // 16
                "2sin(x)cos(x)", // 2*sin(3)*cos(3)
                "2sin(3x)4cos(3x)", // 8*sin(9)*cos(9)
                "25", // 25
                "exp(ln(x^2))", // 9
                "ln(exp(x))", // 3
                "pi-e", //PI-E
                "asin(x)+acos(x)-pi/2", // 0
                "tan(atan(1))", // 1
                " \t \r \n x \t\t\r\r\n", // 3
                " x + 1 ", // 4
                " x * 2 ^ 4\n", // 48
                " asin ( sin   ( x / 6) ) ", // 0.5
                "\t(  ( x))\n(x)" // 9
        };

        String[] wrong={
                "2 2",
                "x x",
                "xx",
                "xsin(x)",
                "ex",
                "xpi",
                "epi"
        };

        double[] results={3,0,-1,3,3,9,7,9,15*32,1.0/6,-5,3-3.0/8,324,1,1,4,1,1,9,16,
                2*sin(3)*cos(3),8*sin(9)*cos(9),25,9,3,Math.PI-Math.E,0,1,3,4,48,0.5,9};

        if(results.length!=accept.length)throw new RuntimeException("incorrect data tests");

        final long st=System.currentTimeMillis();

        for(int i=0;i<accept.length;i++)test(accept[i],x,results[i],eps);
        for(String w:wrong)if(ExpressionParser.expression.parse(new CharStream(w))!=null)throw new RuntimeException("wrong: "+w);

        System.out.println("time="+(System.currentTimeMillis()-st));
    }

    private static void test(String task, double x, double expected, double eps){
        Node node=ExpressionParser.expression.parse(new CharStream(task));
        if(node==null)throw new RuntimeException("accept:  "+task);
        double res=FunctionBuilder.build(node).apply(x);
        final double delta=Math.abs(res-expected);
        if(delta>eps)throw new RuntimeException("accept: task= "+task+" delta= "+delta);
    }
}
