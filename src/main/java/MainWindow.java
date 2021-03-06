import math_expressions.ExpressionParser;
import math_expressions.FunctionBuilder;
import math_expressions.Node;
import parsers_lib.CharStream;
import parsers_lib.ParseResult;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class MainWindow extends JFrame {
    public MainWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800,600);
        setLayout(new BorderLayout());
        Field field=new Field();
        add(field);
        JPanel line=new JPanel(new FlowLayout());
        add(line,BorderLayout.AFTER_LAST_LINE);

        Font font=new Font("",0,17);
        JLabel fxLabel=new JLabel("f(X)=");
        fxLabel.setFont(font);
        line.add(fxLabel,FlowLayout.LEFT);
        JTextField fx=new JTextField();
        fx.setFont(font);
        fx.setPreferredSize(new Dimension(500,30));
        line.add(fx);
        JButton run=new JButton("run");
        run.setFont(font);
        line.add(run);
        JLabel label=new JLabel("ok");
        label.setFont(font);
        line.add(label);

        run.addActionListener(e->{
            final CharStream stream=new CharStream(fx.getText());
            final ParseResult<Node> exp= ExpressionParser.expression.parse(stream);
            if(exp.isError()){
                throw new RuntimeException();
            }
            field.update(FunctionBuilder.build(exp.result));
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}

class Field extends JPanel{
    private Function<Double,Double> fx=x->0.0;
    private double mx=5;
    private double my=5;
    private double dx=0.01;
    @Override
    public void paint(Graphics g) {
        g.clearRect(0,0,getWidth(),getHeight());
        g.translate(getWidth()/2,getHeight()/2);
        final double xScale=getWidth()/2.0;
        final double yScale=getHeight()/2.0;
        g.drawLine(0,(int)(-yScale),0,(int) (yScale));
        g.drawLine((int)(-xScale),0,(int)(xScale),0);

        for(double x=-mx;x<mx;x+=dx){
            g.drawLine(
                    (int) (x*xScale/mx),
                    (int) -(fx.apply(x)*yScale/my),
                    (int) ((x+dx)*xScale/mx),
                    (int) -(fx.apply(x+dx)*yScale/my)
            );
        }

    }

    public void update(Function<Double,Double> fx){
        this.fx=fx;
        updateUI();
    }
}