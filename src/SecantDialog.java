import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecantDialog extends JDialog {
    public JTextField equationField, x0Field, x1Field, errorField;
    public JTable resultTable;
    public SecantTable tableModel;
    public JButton solveButton, clearButton;

    public SecantDialog(JFrame parent) {
        super(parent, "Secant Method Solver", true);

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        JLabel titleLabel = new JLabel("Secant Method Solver");
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
        inputPanel.add(new JLabel("X0 (first initial guess):"), constraints);
        constraints.gridx = 1;
        x0Field = new JTextField(10);
        x0Field.setText("");
        inputPanel.add(x0Field, constraints);

        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        inputPanel.add(new JLabel("X1 (second initial guess):"), constraints);
        constraints.gridx = 1;
        x1Field = new JTextField(10);
        x1Field.setText("");
        inputPanel.add(x1Field, constraints);

        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        inputPanel.add(new JLabel("Margin of Error (Ea <):"), constraints);
        constraints.gridx = 1;
        errorField = new JTextField(10);
        errorField.setText("");
        inputPanel.add(errorField, constraints);

        constraints.gridy = 6;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        JLabel formulaLabel = new JLabel("Formula: X2 = X0 - f(X0) * [(X1 - X0)/(f(X1) - f(X0))]");
        formulaLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        formulaLabel.setForeground(new Color(0, 102, 204));
        inputPanel.add(formulaLabel, constraints);

        constraints.gridy = 7;
        JLabel errorNote = new JLabel("Note: Error = |new X2 - previous X2| (Ea column is blank for iteration 1)");
        errorNote.setFont(new Font("Arial", Font.ITALIC, 10));
        errorNote.setForeground(new Color(0, 102, 204));
        inputPanel.add(errorNote, constraints);

        constraints.gridy = 8;
        JLabel noteLabel = new JLabel("IMPORTANT: All trigonometric functions use RADIANS");
        noteLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 11));
        noteLabel.setForeground(new Color(255, 0, 0));
        inputPanel.add(noteLabel, constraints);

        constraints.gridy = 9;
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

        constraints.gridy = 10;
        inputPanel.add(buttonPanel, constraints);

        tableModel = new SecantTable();
        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JTableHeader header = resultTable.getTableHeader();
        header.setBackground(new Color(51, 153, 255));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(750, 300));

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSecant();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearInputs();
            }
        });

        setSize(850, 750);
        setLocationRelativeTo(parent);
        clearAllFields();
    }

    public void clearAllFields() {
        equationField.setText("");
        x0Field.setText("");
        x1Field.setText("");
        errorField.setText("");
        tableModel.clearTable();
    }

    public void clearInputs() {
        equationField.setText("");
        x0Field.setText("");
        x1Field.setText("");
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

    public void solveSecant() {
        try {
            String equation = equationField.getText().trim();
            if (equation.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an equation!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double x0 = Double.parseDouble(x0Field.getText().trim());
            double x1 = Double.parseDouble(x1Field.getText().trim());
            double desiredError = Double.parseDouble(errorField.getText().trim());

            tableModel.clearTable();

            int iteration = 1;
            double x2 = 0;
            double fx2 = 0;
            double previousX2 = 0;

            double currentX0 = x0;
            double currentX1 = x1;
            double currentFx0 = evaluateFunction(equation, currentX0);
            double currentFx1 = evaluateFunction(equation, currentX1);

            if (Double.isNaN(currentFx0) || Double.isNaN(currentFx1)) {
                JOptionPane.showMessageDialog(this, "Invalid equation or initial values!", "Equation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            while (iteration <= 100) {
                double denominator = currentFx1 - currentFx0;

                if (Math.abs(denominator) < 1e-10) {
                    JOptionPane.showMessageDialog(this,
                            "Division by zero detected! f(X1) - f(X0) is too close to zero.\n" +
                                    "Try different initial guesses.",
                            "Calculation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                x2 = currentX0 - (currentFx0 * (currentX1 - currentX0)) / denominator;

                double roundedX2 = Math.round(x2 * 100.0) / 100.0;

                fx2 = evaluateFunction(equation, roundedX2);

                double roundedX0 = Math.round(currentX0 * 100.0) / 100.0;
                double roundedX1 = Math.round(currentX1 * 100.0) / 100.0;

                double roundedFx0 = Math.round(currentFx0 * 100.0) / 100.0;
                double roundedFx1 = Math.round(currentFx1 * 100.0) / 100.0;
                double roundedFx2 = Math.round(fx2 * 100.0) / 100.0;

                if (Double.isNaN(fx2)) {
                    JOptionPane.showMessageDialog(this, "Error evaluating function at X2!", "Calculation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double error = 0;
                if (iteration > 1) {
                    error = Math.abs(roundedX2 - previousX2);
                }

                double roundedError = Math.round(error * 100.0) / 100.0;

                tableModel.addSecantIteration(new SecantIteration(
                        iteration, roundedX0, roundedX1, roundedX2,
                        roundedFx0, roundedFx1, roundedFx2, roundedError
                ));

                if (iteration > 1 && roundedError <= desiredError) {
                    JOptionPane.showMessageDialog(this,
                            "Solution found at iteration " + iteration + "!\n" +
                                    "Root = " + String.format("%.2f", roundedX2) + "\n" +
                                    "f(x) = " + String.format("%.2f", roundedFx2) + "\n" +
                                    "Error = " + String.format("%.2f", roundedError),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                currentX0 = currentX1;
                currentFx0 = currentFx1;
                currentX1 = roundedX2;
                currentFx1 = roundedFx2;

                previousX2 = roundedX2;
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