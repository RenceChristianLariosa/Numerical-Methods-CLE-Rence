import javax.swing.*;
import java.awt.*;

public class mainFrame extends JFrame {
    JLabel description;
    JButton binomial,regulaFalsi,secant,newton,gaussJacobi,gaussSeidel,simpsonsRule,trapezoidalRule;
    Container container;
    GridBagConstraints constraints;
    GridBagLayout layout;

    public mainFrame (){
    description=new JLabel("Please Select a Method to Solve your Equation");
    description.setFont(new Font("Arial", Font.BOLD, 14));
    description.setHorizontalAlignment(SwingConstants.CENTER);

    binomial=new JButton("Binomial");
    regulaFalsi=new JButton("Regula-Falsi");
    secant=new JButton("Secant");
    newton=new JButton("Newton");
    gaussJacobi=new JButton("Gauss-Jacobi");
    gaussSeidel=new JButton("Gauss-Seidel");
    simpsonsRule=new JButton("Simpsons Rule");
    trapezoidalRule=new JButton("Trapezoidal Rule");

    container=this.getContentPane();
    constraints=new GridBagConstraints();
    layout=new GridBagLayout();
    container.setLayout(layout);
    constraints.insets=new Insets(10,10,10,10);
    constraints.weightx=1;

    constraints.gridx=0;
    constraints.gridy=0;
    constraints.gridwidth=4;
    constraints.gridheight=1;
    constraints.fill=GridBagConstraints.NONE;
    constraints.anchor=GridBagConstraints.CENTER;
    container.add(description,constraints);

    constraints.gridx=0;
    constraints.gridy=1;
    constraints.gridwidth=1;
    constraints.gridheight=1;
    constraints.fill=GridBagConstraints.HORIZONTAL;
    container.add(binomial,constraints);

    constraints.gridx=1;
    constraints.gridy=1;
    constraints.gridwidth=1;
    constraints.gridheight=1;
    constraints.fill=GridBagConstraints.HORIZONTAL;
    container.add(regulaFalsi,constraints);

    constraints.gridx=2;
    constraints.gridy=1;
    constraints.gridwidth=1;
    constraints.gridheight=1;
    constraints.fill=GridBagConstraints.HORIZONTAL;
    container.add(secant,constraints);

    constraints.gridx=3;
    constraints.gridy=1;
    constraints.gridwidth=1;
    constraints.gridheight=1;
    constraints.fill=GridBagConstraints.HORIZONTAL;
    container.add(newton,constraints);

    constraints.gridx=0;
    constraints.gridy=2;
    constraints.gridwidth=1;
    constraints.gridheight=1;
    constraints.fill=GridBagConstraints.HORIZONTAL;
    container.add(gaussJacobi,constraints);

    constraints.gridx=1;
    constraints.gridy=2;
    constraints.gridwidth=1;
    constraints.gridheight=1;
    constraints.fill=GridBagConstraints.HORIZONTAL;
    container.add(gaussSeidel,constraints);

    constraints.gridx=2;
    constraints.gridy=2;
    constraints.gridwidth=1;
    constraints.gridheight=1;
    constraints.fill=GridBagConstraints.HORIZONTAL;
    container.add(simpsonsRule,constraints);

    constraints.gridx=3;
    constraints.gridy=2;
    constraints.gridwidth=1;
    constraints.gridheight=1;
    constraints.fill=GridBagConstraints.HORIZONTAL;
    container.add(trapezoidalRule,constraints);

    this.setVisible(true);
    this.setSize(800,600);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
