package math_expressions;

import java.util.HashMap;

public class Node {
    public final String val;
    public final Node left;
    public final Node right;

    public Node(String val){
        this(val,null,null);
    }

    public Node(String val, Node l){
        this(val,l,null);
    }

    public Node(String val, Node left, Node right) {
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