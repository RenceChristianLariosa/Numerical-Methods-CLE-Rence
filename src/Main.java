import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        mainFrame frame=new mainFrame();

        frame.binomial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BisectionSolverDialog dialog = new BisectionSolverDialog(frame);
                dialog.setVisible(true);
            }
        });
        frame.regulaFalsi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegulaFalsiDialog dialog = new RegulaFalsiDialog(frame);
                dialog.setVisible(true);
            }
        });
        frame.secant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SecantDialog dialog = new SecantDialog(frame);
                dialog.setVisible(true);
            }
        });
        frame.newton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewtonDialog dialog = new NewtonDialog(frame);
                dialog.setVisible(true);
            }
        });
        frame.gaussJacobi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GaussJacobiDialog dialog = new GaussJacobiDialog(frame);
                dialog.setVisible(true);
            }
        });
        frame.gaussSeidel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GaussSeidelDialog dialog = new GaussSeidelDialog(frame);
                dialog.setVisible(true);
            }
        });
        frame.simpsonsRule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpsonDialog dialog = new SimpsonDialog(frame);
                dialog.setVisible(true);
            }
        });
        frame.trapezoidalRule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TrapezoidDialog dialog = new TrapezoidDialog(frame);
                dialog.setVisible(true);
            }
        });
    }
}