import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GaussSeidelDialog extends JDialog {
    public JTextField a11Field, a12Field, a13Field, b1Field;
    public JTextField a21Field, a22Field, a23Field, b2Field;
    public JTextField a31Field, a32Field, a33Field, b3Field;
    public JTextField x1Field, x2Field, x3Field, errorField;
    public JTable resultTable;
    public GaussSeidelTable tableModel;
    public JButton solveButton, clearButton;

    public GaussSeidelDialog(JFrame parent) {
        super(parent, "Gauss-Seidel Method Solver", true);

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0; constraints.gridy = 0;
        constraints.gridwidth = 4;
        JLabel titleLabel = new JLabel("Gauss-Seidel Iterative Method Solver");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(51, 153, 255));
        inputPanel.add(titleLabel, constraints);

        // Equation header
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 1;
        inputPanel.add(new JLabel("x₁"), constraints);
        constraints.gridx = 2;
        inputPanel.add(new JLabel("x₂"), constraints);
        constraints.gridx = 3;
        inputPanel.add(new JLabel("x₃"), constraints);
        constraints.gridx = 4;
        inputPanel.add(new JLabel("= b"), constraints);

        // Row 1
        constraints.gridy = 2;
        constraints.gridx = 0;
        inputPanel.add(new JLabel("Equation 1:"), constraints);
        constraints.gridx = 1;
        a11Field = new JTextField(5);
        a11Field.setText("6");
        inputPanel.add(a11Field, constraints);
        constraints.gridx = 2;
        a12Field = new JTextField(5);
        a12Field.setText("-2");
        inputPanel.add(a12Field, constraints);
        constraints.gridx = 3;
        a13Field = new JTextField(5);
        a13Field.setText("1");
        inputPanel.add(a13Field, constraints);
        constraints.gridx = 4;
        b1Field = new JTextField(5);
        b1Field.setText("11");
        inputPanel.add(b1Field, constraints);

        // Row 2
        constraints.gridy = 3;
        constraints.gridx = 0;
        inputPanel.add(new JLabel("Equation 2:"), constraints);
        constraints.gridx = 1;
        a21Field = new JTextField(5);
        a21Field.setText("-2");
        inputPanel.add(a21Field, constraints);
        constraints.gridx = 2;
        a22Field = new JTextField(5);
        a22Field.setText("7");
        inputPanel.add(a22Field, constraints);
        constraints.gridx = 3;
        a23Field = new JTextField(5);
        a23Field.setText("2");
        inputPanel.add(a23Field, constraints);
        constraints.gridx = 4;
        b2Field = new JTextField(5);
        b2Field.setText("5");
        inputPanel.add(b2Field, constraints);

        // Row 3
        constraints.gridy = 4;
        constraints.gridx = 0;
        inputPanel.add(new JLabel("Equation 3:"), constraints);
        constraints.gridx = 1;
        a31Field = new JTextField(5);
        a31Field.setText("1");
        inputPanel.add(a31Field, constraints);
        constraints.gridx = 2;
        a32Field = new JTextField(5);
        a32Field.setText("2");
        inputPanel.add(a32Field, constraints);
        constraints.gridx = 3;
        a33Field = new JTextField(5);
        a33Field.setText("-5");
        inputPanel.add(a33Field, constraints);
        constraints.gridx = 4;
        b3Field = new JTextField(5);
        b3Field.setText("-1");
        inputPanel.add(b3Field, constraints);

        // Initial guesses
        constraints.gridy = 5;
        constraints.gridwidth = 4;
        JLabel initLabel = new JLabel("Initial Guesses:");
        initLabel.setFont(new Font("Arial", Font.BOLD, 12));
        inputPanel.add(initLabel, constraints);

        constraints.gridy = 6;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        inputPanel.add(new JLabel("x₁₍₀₎ ="), constraints);
        constraints.gridx = 1;
        x1Field = new JTextField(8);
        x1Field.setText("0");
        inputPanel.add(x1Field, constraints);
        constraints.gridx = 2;
        inputPanel.add(new JLabel("x₂₍₀₎ ="), constraints);
        constraints.gridx = 3;
        x2Field = new JTextField(8);
        x2Field.setText("0");
        inputPanel.add(x2Field, constraints);
        constraints.gridx = 4;
        inputPanel.add(new JLabel("x₃₍₀₎ ="), constraints);
        constraints.gridx = 5;
        x3Field = new JTextField(8);
        x3Field.setText("0");
        inputPanel.add(x3Field, constraints);

        // Error tolerance
        constraints.gridy = 7;
        constraints.gridx = 0;
        inputPanel.add(new JLabel("Error Tolerance (Ea ≤):"), constraints);
        constraints.gridx = 1;
        errorField = new JTextField(8);
        errorField.setText("0.0001");
        inputPanel.add(errorField, constraints);

        // Formulas display - NOTE: Gauss-Seidel uses updated values immediately
        constraints.gridy = 8;
        constraints.gridwidth = 6;
        JLabel formulaLabel = new JLabel("Gauss-Seidel Formulas (using updated values): x₁' = (b₁ - a₁₂x₂ - a₁₃x₃)/a₁₁,  x₂' = (b₂ - a₂₁x₁' - a₂₃x₃)/a₂₂,  x₃' = (b₃ - a₃₁x₁' - a₃₂x₂')/a₃₃");
        formulaLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        formulaLabel.setForeground(new Color(0, 102, 204));
        inputPanel.add(formulaLabel, constraints);

        constraints.gridy = 9;
        JLabel noteLabel = new JLabel("NOTE: Gauss-Seidel converges faster than Jacobi because it uses updated values immediately");
        noteLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 11));
        noteLabel.setForeground(new Color(255, 0, 0));
        inputPanel.add(noteLabel, constraints);

        // Buttons
        JPanel buttonPanel = new JPanel();
        solveButton = new JButton("Solve");
        solveButton.setBackground(new Color(51, 153, 255));
        solveButton.setForeground(Color.WHITE);
        clearButton = new JButton("Clear");
        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);

        constraints.gridy = 10;
        inputPanel.add(buttonPanel, constraints);

        // Table
        tableModel = new GaussSeidelTable();
        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JTableHeader header = resultTable.getTableHeader();
        header.setBackground(new Color(51, 153, 255));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(1100, 400));

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveGaussSeidel();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearInputs();
            }
        });

        setSize(1200, 750);
        setLocationRelativeTo(parent);
    }

    private void clearInputs() {
        // Reset coefficients to example values
        a11Field.setText("6"); a12Field.setText("-2"); a13Field.setText("1"); b1Field.setText("11");
        a21Field.setText("-2"); a22Field.setText("7"); a23Field.setText("2"); b2Field.setText("5");
        a31Field.setText("1"); a32Field.setText("2"); a33Field.setText("-5"); b3Field.setText("-1");
        x1Field.setText("0");
        x2Field.setText("0");
        x3Field.setText("0");
        errorField.setText("0.0001");
        tableModel.clearTable();
    }

    private double roundTo2Decimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private void solveGaussSeidel() {
        try {
            // Get coefficients
            double a11 = Double.parseDouble(a11Field.getText().trim());
            double a12 = Double.parseDouble(a12Field.getText().trim());
            double a13 = Double.parseDouble(a13Field.getText().trim());
            double b1 = Double.parseDouble(b1Field.getText().trim());

            double a21 = Double.parseDouble(a21Field.getText().trim());
            double a22 = Double.parseDouble(a22Field.getText().trim());
            double a23 = Double.parseDouble(a23Field.getText().trim());
            double b2 = Double.parseDouble(b2Field.getText().trim());

            double a31 = Double.parseDouble(a31Field.getText().trim());
            double a32 = Double.parseDouble(a32Field.getText().trim());
            double a33 = Double.parseDouble(a33Field.getText().trim());
            double b3 = Double.parseDouble(b3Field.getText().trim());

            // Check diagonal elements aren't zero
            if (a11 == 0 || a22 == 0 || a33 == 0) {
                JOptionPane.showMessageDialog(this,
                        "Diagonal elements (a₁₁, a₂₂, a₃₃) cannot be zero!\n" +
                                "Rearrange your equations so each has a non-zero diagonal coefficient.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get initial guesses
            double x1 = roundTo2Decimals(Double.parseDouble(x1Field.getText().trim()));
            double x2 = roundTo2Decimals(Double.parseDouble(x2Field.getText().trim()));
            double x3 = roundTo2Decimals(Double.parseDouble(x3Field.getText().trim()));

            double desiredError = Double.parseDouble(errorField.getText().trim());

            tableModel.clearTable();

            int iteration = 1;
            double newX1, newX2, newX3;
            double ea1 = 0, ea2 = 0, ea3 = 0;
            double prevX1, prevX2, prevX3;

            while (iteration <= 100) {
                // Store previous values for error calculation
                prevX1 = x1;
                prevX2 = x2;
                prevX3 = x3;

                // Gauss-Seidel: Use updated values immediately
                // Calculate x1' using previous x2 and x3
                newX1 = roundTo2Decimals((b1 - a12 * x2 - a13 * x3) / a11);

                // Calculate x2' using UPDATED x1' and previous x3
                newX2 = roundTo2Decimals((b2 - a21 * newX1 - a23 * x3) / a22);

                // Calculate x3' using UPDATED x1' and UPDATED x2'
                newX3 = roundTo2Decimals((b3 - a31 * newX1 - a32 * newX2) / a33);

                // Calculate errors (rounded to 2 decimals)
                if (iteration > 1) {
                    ea1 = roundTo2Decimals(Math.abs(newX1 - prevX1));
                    ea2 = roundTo2Decimals(Math.abs(newX2 - prevX2));
                    ea3 = roundTo2Decimals(Math.abs(newX3 - prevX3));
                }

                // Add to table
                tableModel.addIteration(new GaussSeidelIteration(
                        iteration, prevX1, prevX2, prevX3, newX1, newX2, newX3, ea1, ea2, ea3
                ));

                // Check convergence
                if (iteration > 1 && ea1 <= desiredError && ea2 <= desiredError && ea3 <= desiredError) {
                    JOptionPane.showMessageDialog(this,
                            String.format("Solution converged at iteration %d!\n\n" +
                                            "x₁ = %.2f\n" +
                                            "x₂ = %.2f\n" +
                                            "x₃ = %.2f\n\n" +
                                            "Errors:\n" +
                                            "Ea₁ = %.4f\n" +
                                            "Ea₂ = %.4f\n" +
                                            "Ea₃ = %.4f",
                                    iteration, newX1, newX2, newX3, ea1, ea2, ea3),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Update for next iteration
                x1 = newX1;
                x2 = newX2;
                x3 = newX3;
                iteration++;
            }

            JOptionPane.showMessageDialog(this,
                    "Maximum iterations reached (100).\nSolution may not have converged.\n" +
                            "Try different initial guesses or check diagonal dominance.",
                    "Limit Reached", JOptionPane.WARNING_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values for all coefficients!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Calculation Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}