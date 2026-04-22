import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrapezoidDialog extends JDialog {
    public JTextField equationField, aField, bField, nField;
    public JTable resultTable;
    public TrapezoidTable tableModel;
    public JButton solveButton, clearButton;

    // Summary labels
    private JLabel lblN, lblA, lblB, lblH;
    private JLabel lblSumFxi, lblIntegral;

    // Header color
    private static final Color HEADER_COLOR   = new Color(0, 100, 160);
    private static final Color ACCENT_COLOR   = new Color(0, 140, 210);
    private static final Color FOOTER_COLOR   = new Color(240, 248, 255);

    public TrapezoidDialog(JFrame parent) {
        super(parent, "Trapezoidal Rule Solver", true);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 250, 255));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 250, 255));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0; c.gridwidth = 4;
        JLabel titleLabel = new JLabel("Trapezoidal Rule Solver");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        titleLabel.setForeground(HEADER_COLOR);
        inputPanel.add(titleLabel, c);

        c.gridy = 1;
        JLabel formulaLabel = new JLabel("Formula:  I ≈ (h/2) · [f(x₀) + 2f(x₁) + 2f(x₂) + … + f(xₙ)]  =  (h/2) · Σf(xi)");
        formulaLabel.setFont(new Font("Monospaced", Font.ITALIC, 11));
        formulaLabel.setForeground(new Color(0, 80, 140));
        inputPanel.add(formulaLabel, c);

        c.gridy = 2; c.gridwidth = 1; c.gridx = 0;
        inputPanel.add(makeLabel("f(x) ="), c);
        c.gridx = 1; c.gridwidth = 3;
        equationField = new JTextField(35);
        styleTextField(equationField);
        inputPanel.add(equationField, c);

        c.gridy = 3; c.gridx = 0; c.gridwidth = 4;
        JLabel hint = new JLabel("  Example: 0.2 + 25*x - 200*x^2 + 675*x^3 - 900*x^4 + 400*x^5");
        hint.setFont(new Font("Arial", Font.ITALIC, 10));
        hint.setForeground(Color.GRAY);
        inputPanel.add(hint, c);

        c.gridy = 4; c.gridwidth = 1;
        c.gridx = 0; inputPanel.add(makeLabel("Lower limit  a ="), c);
        c.gridx = 1;
        aField = new JTextField(8); styleTextField(aField);
        inputPanel.add(aField, c);

        c.gridx = 2; inputPanel.add(makeLabel("Upper limit  b ="), c);
        c.gridx = 3;
        bField = new JTextField(8); styleTextField(bField);
        inputPanel.add(bField, c);

        c.gridy = 5;
        c.gridx = 0; inputPanel.add(makeLabel("Number of segments  n ="), c);
        c.gridx = 1;
        nField = new JTextField(8); styleTextField(nField);
        inputPanel.add(nField, c);

        c.gridy = 6; c.gridx = 0; c.gridwidth = 4;
        JLabel trigNote = new JLabel("  NOTE: Trig functions use RADIANS. You may use 'pi' or 'π' in expressions.");
        trigNote.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 10));
        trigNote.setForeground(new Color(180, 0, 0));
        inputPanel.add(trigNote, c);

        c.gridy = 7; c.gridwidth = 4; c.gridx = 0;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnPanel.setBackground(new Color(245, 250, 255));
        solveButton = makeButton("  Solve  ", HEADER_COLOR);
        clearButton = makeButton("  Clear  ", new Color(150, 150, 160));
        btnPanel.add(solveButton);
        btnPanel.add(clearButton);
        inputPanel.add(btnPanel, c);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new TrapezoidTable();
        resultTable = new JTable(tableModel);
        resultTable.setRowHeight(24);
        resultTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultTable.setGridColor(new Color(200, 215, 230));
        resultTable.setShowGrid(true);
        resultTable.setSelectionBackground(new Color(190, 220, 255));

        resultTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int col) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(232, 244, 255));
                }
                setHorizontalAlignment(col == 0 ? LEFT : CENTER);
                return comp;
            }
        });

        JTableHeader header = resultTable.getTableHeader();
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(700, 260));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1), " Iteration Table ",
                0, 0, new Font("Arial", Font.BOLD, 11), ACCENT_COLOR));
        add(scrollPane, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new GridBagLayout());
        summaryPanel.setBackground(FOOTER_COLOR);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, ACCENT_COLOR),
                BorderFactory.createEmptyBorder(8, 15, 10, 15)));

        GridBagConstraints sc = new GridBagConstraints();
        sc.insets = new Insets(2, 10, 2, 10);
        sc.anchor = GridBagConstraints.WEST;

        sc.gridy = 0;
        sc.gridx = 0; summaryPanel.add(makeSummaryKey("n ="), sc);
        sc.gridx = 1; lblN = makeSummaryVal("—"); summaryPanel.add(lblN, sc);
        sc.gridx = 2; summaryPanel.add(makeSummaryKey("a ="), sc);
        sc.gridx = 3; lblA = makeSummaryVal("—"); summaryPanel.add(lblA, sc);
        sc.gridx = 4; summaryPanel.add(makeSummaryKey("b ="), sc);
        sc.gridx = 5; lblB = makeSummaryVal("—"); summaryPanel.add(lblB, sc);
        sc.gridx = 6; summaryPanel.add(makeSummaryKey("h = (b−a)/n ="), sc);
        sc.gridx = 7; lblH = makeSummaryVal("—"); summaryPanel.add(lblH, sc);


        sc.gridy = 1;
        sc.gridx = 0; summaryPanel.add(makeSummaryKey("Σf(xi) ="), sc);
        sc.gridx = 1; lblSumFxi = makeSummaryVal("—"); summaryPanel.add(lblSumFxi, sc);

        sc.gridx = 2; sc.gridwidth = 2;
        JLabel integralKey = new JLabel("I = (h/2) · Σf(xi) =");
        integralKey.setFont(new Font("Arial", Font.BOLD, 13));
        integralKey.setForeground(HEADER_COLOR);
        summaryPanel.add(integralKey, sc);

        sc.gridx = 4; sc.gridwidth = 3;
        lblIntegral = new JLabel("—");
        lblIntegral.setFont(new Font("Georgia", Font.BOLD, 20));
        lblIntegral.setForeground(new Color(0, 120, 0));
        summaryPanel.add(lblIntegral, sc);

        sc.gridx = 7; sc.gridwidth = 1;
        JLabel sqUnit = new JLabel("sq. u");
        sqUnit.setFont(new Font("Arial", Font.PLAIN, 11));
        sqUnit.setForeground(Color.DARK_GRAY);
        summaryPanel.add(sqUnit, sc);

        add(summaryPanel, BorderLayout.SOUTH);

        solveButton.addActionListener(e -> solveTrapezoidal());
        clearButton.addActionListener(e -> clearAll());

        setSize(850, 680);
        setLocationRelativeTo(parent);
        setResizable(true);
    }


    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.PLAIN, 12));
        return l;
    }

    private JLabel makeSummaryKey(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        l.setForeground(new Color(60, 60, 80));
        return l;
    }

    private JLabel makeSummaryVal(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Monospaced", Font.PLAIN, 12));
        l.setForeground(new Color(0, 80, 140));
        return l;
    }

    private void styleTextField(JTextField f) {
        f.setFont(new Font("Monospaced", Font.PLAIN, 12));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 190, 230)),
                BorderFactory.createEmptyBorder(3, 5, 3, 5)));
    }

    private JButton makeButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }


    private void clearAll() {
        equationField.setText("");
        aField.setText("");
        bField.setText("");
        nField.setText("");
        tableModel.clearTable();
        lblN.setText("—"); lblA.setText("—"); lblB.setText("—"); lblH.setText("—");
        lblSumFxi.setText("—"); lblIntegral.setText("—");
    }

    private void solveTrapezoidal() {
        try {
            String equation = equationField.getText().trim();
            if (equation.isEmpty()) {
                error("Please enter a function f(x)!");
                return;
            }

            double a = Double.parseDouble(aField.getText().trim());
            double b = Double.parseDouble(bField.getText().trim());
            int n    = Integer.parseInt(nField.getText().trim());

            if (n < 1) {
                error("Number of segments n must be at least 1.");
                return;
            }
            if (b <= a) {
                error("Upper limit b must be greater than lower limit a.");
                return;
            }

            double h = (b - a) / n;

            tableModel.clearTable();

            double sumFxi = 0.0;

            for (int i = 0; i <= n; i++) {
                double xi = a + i * h;
                double fxi_raw = ExpressionEvaluator.evaluate(equation, xi);

                if (Double.isNaN(fxi_raw)) {
                    error("f(x) could not be evaluated at x = " + xi + ". Check your expression.");
                    return;
                }

                int trapMult = (i == 0 || i == n) ? 1 : 2;
                double contribution = trapMult * fxi_raw;
                sumFxi += contribution;

                String label = "x" + i;
                tableModel.addRow(new TrapezoidalIteration(i, label, xi, fxi_raw, trapMult, contribution));
            }

            double integral = (h / 2.0) * sumFxi;

            lblN.setText(String.valueOf(n));
            lblA.setText(String.format("%.4f", a));
            lblB.setText(String.format("%.4f", b));
            lblH.setText(String.format("%.4f", h));
            lblSumFxi.setText(String.format("%.4f", sumFxi));
            lblIntegral.setText(String.format("%.6f", integral));

        } catch (NumberFormatException ex) {
            error("Please enter valid numeric values for a, b, and n.");
        } catch (Exception ex) {
            error("Calculation error: " + ex.getMessage());
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}