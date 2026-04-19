import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewtonDialog extends JDialog {
    public JTextField equationField, derivativeField, x0Field, errorField;
    public JTable resultTable;
    public NewtonTable tableModel;
    public JButton solveButton, clearButton;

    public NewtonDialog(JFrame parent) {
        super(parent, "Newton-Raphson Method Solver", true);

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        JLabel titleLabel = new JLabel("Newton-Raphson Method Solver");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(51, 153, 255));
        inputPanel.add(titleLabel, constraints);

        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        inputPanel.add(new JLabel("Equation f(x) ="), constraints);
        constraints.gridx = 1;
        equationField = new JTextField(20);
        equationField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        equationField.setText("");
        inputPanel.add(equationField, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        JLabel exampleLabel = new JLabel("Examples: sin(10*x)+cos(3*x), x^2-4, sin(x), cos(pi/2), sqrt(x)+2");
        exampleLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        exampleLabel.setForeground(Color.GRAY);
        inputPanel.add(exampleLabel, constraints);

        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        inputPanel.add(new JLabel("Derivative f'(x) ="), constraints);
        constraints.gridx = 1;
        derivativeField = new JTextField(20);
        derivativeField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        derivativeField.setText("");
        inputPanel.add(derivativeField, constraints);

        constraints.gridy = 4;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        JLabel derivExampleLabel = new JLabel("Example derivative: 3*x^2-8*x+1 for f(x)=x^3-4x^2+x-10");
        derivExampleLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        derivExampleLabel.setForeground(Color.GRAY);
        inputPanel.add(derivExampleLabel, constraints);

        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        inputPanel.add(new JLabel("X0 (initial guess):"), constraints);
        constraints.gridx = 1;
        x0Field = new JTextField(10);
        x0Field.setText("");
        inputPanel.add(x0Field, constraints);

        constraints.gridy = 6;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        inputPanel.add(new JLabel("Margin of Error (Ea <):"), constraints);
        constraints.gridx = 1;
        errorField = new JTextField(10);
        errorField.setText("");
        inputPanel.add(errorField, constraints);

        constraints.gridy = 7;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        JLabel formulaLabel = new JLabel("Formula: X₁ = X₀ - f(X₀)/f'(X₀)");
        formulaLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        formulaLabel.setForeground(new Color(0, 102, 204));
        inputPanel.add(formulaLabel, constraints);

        constraints.gridy = 8;
        JLabel errorNote = new JLabel("Note: Error = |new X₁ - previous X₁| (Ea column is blank for iteration 1)");
        errorNote.setFont(new Font("Arial", Font.ITALIC, 10));
        errorNote.setForeground(new Color(0, 102, 204));
        inputPanel.add(errorNote, constraints);

        constraints.gridy = 9;
        JLabel noteLabel = new JLabel("IMPORTANT: All trigonometric functions use RADIANS");
        noteLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 11));
        noteLabel.setForeground(new Color(255, 0, 0));
        inputPanel.add(noteLabel, constraints);

        constraints.gridy = 10;
        JLabel piLabel = new JLabel("You can use 'pi' or 'π' for π (e.g., sin(pi/2) = sin(1.57) = 1)");
        piLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        piLabel.setForeground(new Color(255, 102, 0));
        inputPanel.add(piLabel, constraints);

        JPanel buttonPanel = new JPanel();
        solveButton = new JButton("Solve");
        solveButton.setBackground(new Color(51, 153, 255));
        solveButton.setForeground(Color.WHITE);
        clearButton = new JButton("Clear");
        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);

        constraints.gridy = 11;
        inputPanel.add(buttonPanel, constraints);

        tableModel = new NewtonTable();
        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JTableHeader header = resultTable.getTableHeader();
        header.setBackground(new Color(51, 153, 255));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveNewton();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearInputs();
            }
        });

        setSize(900, 800);
        setLocationRelativeTo(parent);
        clearAllFields();
    }

    public void clearAllFields() {
        equationField.setText("");
        derivativeField.setText("");
        x0Field.setText("");
        errorField.setText("");
        tableModel.clearTable();
    }

    public void clearInputs() {
        equationField.setText("");
        derivativeField.setText("");
        x0Field.setText("");
        errorField.setText("");
        tableModel.clearTable();
    }

    public double evaluateFunction(String equation, double x) {
        try {
            return ExpressionEvaluator.evaluate(equation, x);
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    public void solveNewton() {
        try {
            String equation = equationField.getText().trim();
            String derivative = derivativeField.getText().trim();

            if (equation.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an equation f(x)!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (derivative.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the derivative f'(x)!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double x0 = Double.parseDouble(x0Field.getText().trim());
            double desiredError = Double.parseDouble(errorField.getText().trim());

            tableModel.clearTable();

            int iteration = 1;
            double x1 = 0;
            double fx0 = 0;
            double derivativeFx0 = 0;
            double previousX1 = 0;

            double currentX0 = x0;

            while (iteration <= 100) {
                fx0 = evaluateFunction(equation, currentX0);
                derivativeFx0 = evaluateFunction(derivative, currentX0);

                if (Double.isNaN(fx0) || Double.isNaN(derivativeFx0)) {
                    JOptionPane.showMessageDialog(this, "Error evaluating function or derivative!", "Calculation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (Math.abs(derivativeFx0) < 1e-10) {
                    JOptionPane.showMessageDialog(this,
                            "Derivative is zero at X0 = " + String.format("%.2f", currentX0) + "!\n" +
                                    "Newton-Raphson method cannot continue.\n" +
                                    "Try a different initial guess.",
                            "Calculation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                x1 = currentX0 - (fx0 / derivativeFx0);

                double roundedX0 = Math.round(currentX0 * 100.0) / 100.0;
                double roundedX1 = Math.round(x1 * 100.0) / 100.0;
                double roundedFx0 = Math.round(fx0 * 100.0) / 100.0;
                double roundedDerivative = Math.round(derivativeFx0 * 100.0) / 100.0;

                double error = 0;
                if (iteration > 1) {
                    error = Math.abs(roundedX1 - previousX1);
                }

                double roundedError = Math.round(error * 100.0) / 100.0;

                tableModel.addNewtonIteration(new NewtonIteration(
                        iteration, roundedX0, roundedX1,
                        roundedFx0, roundedDerivative, roundedError
                ));

                if (iteration > 1 && roundedError <= desiredError) {
                    JOptionPane.showMessageDialog(this,
                            "Solution found at iteration " + iteration + "!\n" +
                                    "Root = " + String.format("%.2f", roundedX1) + "\n" +
                                    "f(x) = " + String.format("%.2f", roundedFx0) + "\n" +
                                    "Error = " + String.format("%.2f", roundedError),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (Math.abs(fx0) < 1e-10) {
                    JOptionPane.showMessageDialog(this,
                            "Solution found at iteration " + iteration + "!\n" +
                                    "Root = " + String.format("%.2f", roundedX0) + "\n" +
                                    "f(x) = 0.00",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                currentX0 = roundedX1;
                previousX1 = roundedX1;
                iteration++;
            }

            JOptionPane.showMessageDialog(this, "Maximum iterations reached (100).", "Limit", JOptionPane.WARNING_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}