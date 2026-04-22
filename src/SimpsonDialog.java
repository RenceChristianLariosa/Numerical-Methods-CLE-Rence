import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class SimpsonDialog extends JDialog {

    public JTextField equationField, aField, bField, nField;
    public JTable resultTable;
    public SimpsonTable tableModel;
    public JButton solveButton, clearButton;

    private JLabel lblN, lblA, lblB, lblH;
    private JLabel lblSumFxi, lblIntegral;

    // Dynamic UI labels that change when the rule is switched
    private JLabel titleLabel, formulaLabel, nNoteLabel, integralKeyLabel;
    private JScrollPane scrollPane;

    // Rule selection
    private JRadioButton radio13, radio38;

    // Colors for each rule
    private static final Color COLOR_13_HEADER = new Color(0, 120, 80);
    private static final Color COLOR_13_ACCENT = new Color(0, 160, 100);
    private static final Color COLOR_13_FOOTER = new Color(240, 255, 248);
    private static final Color COLOR_13_BG     = new Color(245, 255, 250);
    private static final Color COLOR_13_ROW    = new Color(225, 248, 237);
    private static final Color COLOR_13_GRID   = new Color(180, 220, 200);
    private static final Color COLOR_13_SEL    = new Color(180, 240, 210);

    private static final Color COLOR_38_HEADER = new Color(120, 60, 0);
    private static final Color COLOR_38_ACCENT = new Color(180, 90, 0);
    private static final Color COLOR_38_FOOTER = new Color(255, 250, 240);
    private static final Color COLOR_38_BG     = new Color(255, 252, 245);
    private static final Color COLOR_38_ROW    = new Color(255, 243, 224);
    private static final Color COLOR_38_GRID   = new Color(220, 190, 150);
    private static final Color COLOR_38_SEL    = new Color(255, 220, 170);

    // Panels that need background repainting
    private JPanel inputPanel, btnPanel, summaryPanel;
    private JPanel mainContentPanel;

    public SimpsonDialog(JFrame parent) {
        super(parent, "Simpson's Rule Solver", true);
        setLayout(new BorderLayout(10, 10));

        buildUI(parent);

        // Apply 1/3 theme by default
        applyTheme(true);

        setSize(950, 750);
        setLocationRelativeTo(parent);
        setResizable(true);
    }

    private void buildUI(JFrame parent) {
        mainContentPanel = new JPanel(new BorderLayout(10, 10));
        add(mainContentPanel, BorderLayout.CENTER);

        // ── NORTH: input panel ─────────────────────────────────────────
        inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;

        // Row 0: Title
        c.gridx = 0; c.gridy = 0; c.gridwidth = 4;
        titleLabel = new JLabel("Simpson's 1/3 Rule Solver");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 18));
        inputPanel.add(titleLabel, c);

        // Row 1: Radio buttons for rule selection
        c.gridy = 1; c.gridwidth = 4;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        radioPanel.setOpaque(false);

        JLabel chooseLabel = new JLabel("Select Rule:");
        chooseLabel.setFont(new Font("Arial", Font.BOLD, 12));

        radio13 = new JRadioButton("Simpson's 1/3 Rule  (n must be even)");
        radio38 = new JRadioButton("Simpson's 3/8 Rule  (n must be multiple of 3)");
        radio13.setFont(new Font("Arial", Font.PLAIN, 12));
        radio38.setFont(new Font("Arial", Font.PLAIN, 12));
        radio13.setOpaque(false);
        radio38.setOpaque(false);
        radio13.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(radio13);
        group.add(radio38);

        radioPanel.add(chooseLabel);
        radioPanel.add(radio13);
        radioPanel.add(radio38);
        inputPanel.add(radioPanel, c);

        // Row 2: Formula
        c.gridy = 2; c.gridwidth = 4;
        formulaLabel = new JLabel("Formula:  I = (h/3) · [f(x₀) + 4Σf(xᵢ)odd + 2Σf(xⱼ)even + f(xₙ)]");
        formulaLabel.setFont(new Font("Monospaced", Font.ITALIC, 11));
        inputPanel.add(formulaLabel, c);

        // Row 3: f(x) input
        c.gridy = 3; c.gridwidth = 1; c.gridx = 0;
        inputPanel.add(makeLabel("f(x) ="), c);
        c.gridx = 1; c.gridwidth = 3;
        equationField = new JTextField(35);
        styleTextField(equationField, new Color(100, 180, 140));
        inputPanel.add(equationField, c);

        // Row 4: Hint
        c.gridy = 4; c.gridx = 0; c.gridwidth = 4;
        JLabel hint = new JLabel("  Example: 0.2 + 5*x - 200*x^2 + 675*x^3 - 900*x^4 + 400*x^5");
        hint.setFont(new Font("Arial", Font.ITALIC, 10));
        hint.setForeground(Color.GRAY);
        inputPanel.add(hint, c);

        // Row 5: a and b
        c.gridy = 5; c.gridwidth = 1;
        c.gridx = 0; inputPanel.add(makeLabel("Lower limit  a ="), c);
        c.gridx = 1; aField = new JTextField(8); styleTextField(aField, new Color(100, 180, 140)); inputPanel.add(aField, c);
        c.gridx = 2; inputPanel.add(makeLabel("Upper limit  b ="), c);
        c.gridx = 3; bField = new JTextField(8); styleTextField(bField, new Color(100, 180, 140)); inputPanel.add(bField, c);

        // Row 6: n
        c.gridy = 6; c.gridx = 0; c.gridwidth = 1;
        inputPanel.add(makeLabel("Number of segments  n ="), c);
        c.gridx = 1; nField = new JTextField(8); styleTextField(nField, new Color(100, 180, 140)); inputPanel.add(nField, c);

        // Row 7: n requirement note (dynamic)
        c.gridy = 7; c.gridx = 0; c.gridwidth = 4;
        nNoteLabel = new JLabel("  NOTE: n must be even for Simpson's 1/3 Rule.");
        nNoteLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 10));
        nNoteLabel.setForeground(new Color(180, 0, 0));
        inputPanel.add(nNoteLabel, c);

        // Row 8: Trig note
        c.gridy = 8;
        JLabel trigNote = new JLabel("  NOTE: Trig functions use RADIANS. You may use 'pi' or 'π' in expressions.");
        trigNote.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 10));
        trigNote.setForeground(new Color(180, 0, 0));
        inputPanel.add(trigNote, c);

        // Row 9: Buttons
        c.gridy = 9; c.gridwidth = 4; c.gridx = 0;
        btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnPanel.setOpaque(false);
        solveButton = makeButton("  Solve  ", COLOR_13_HEADER);
        clearButton = makeButton("  Clear  ", new Color(150, 150, 160));
        btnPanel.add(solveButton);
        btnPanel.add(clearButton);
        inputPanel.add(btnPanel, c);

        mainContentPanel.add(inputPanel, BorderLayout.NORTH);

        // ── CENTER: table ──────────────────────────────────────────────
        String[] cols13 = {"", "x", "f(x)", "mod 2", "1/3 rule", "f(xi)"};
        tableModel = new SimpsonTable(cols13);
        resultTable = new JTable(tableModel);
        resultTable.setRowHeight(24);
        resultTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultTable.setShowGrid(true);

        resultTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int col) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    boolean is13 = radio13.isSelected();
                    comp.setBackground(row % 2 == 0 ? Color.WHITE
                            : (is13 ? COLOR_13_ROW : COLOR_38_ROW));
                }
                setHorizontalAlignment(col == 0 ? LEFT : CENTER);
                return comp;
            }
        });

        JTableHeader header = resultTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setForeground(Color.WHITE);

        scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(750, 260));

        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        // ── SOUTH: summary panel ───────────────────────────────────────
        summaryPanel = new JPanel(new GridBagLayout());
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_13_ACCENT),
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
        integralKeyLabel = new JLabel("I = (h/3) · Σf(xi) =");
        integralKeyLabel.setFont(new Font("Arial", Font.BOLD, 13));
        summaryPanel.add(integralKeyLabel, sc);

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

        mainContentPanel.add(summaryPanel, BorderLayout.SOUTH);

        // ── Listeners ──────────────────────────────────────────────────
        solveButton.addActionListener(e -> solveSimpson());
        clearButton.addActionListener(e -> clearAll());

        radio13.addActionListener(e -> applyTheme(true));
        radio38.addActionListener(e -> applyTheme(false));
    }

    // ── Theme switching ───────────────────────────────────────────────────

    private void applyTheme(boolean is13) {
        Color headerColor = is13 ? COLOR_13_HEADER : COLOR_38_HEADER;
        Color accentColor = is13 ? COLOR_13_ACCENT : COLOR_38_ACCENT;
        Color footerColor = is13 ? COLOR_13_FOOTER : COLOR_38_FOOTER;
        Color bgColor     = is13 ? COLOR_13_BG     : COLOR_38_BG;
        Color gridColor   = is13 ? COLOR_13_GRID   : COLOR_38_GRID;
        Color selColor    = is13 ? COLOR_13_SEL    : COLOR_38_SEL;

        // Update title and formula
        titleLabel.setText(is13 ? "Simpson's 1/3 Rule Solver" : "Simpson's 3/8 Rule Solver");
        titleLabel.setForeground(headerColor);

        formulaLabel.setText(is13
                ? "Formula:  I = (h/3) · [f(x₀) + 4Σf(xᵢ)odd + 2Σf(xⱼ)even + f(xₙ)]"
                : "Formula:  I = (3h/8) · [f(x₀) + 3f(x₁) + 3f(x₂) + 2f(x₃) + … + f(xₙ)]");
        formulaLabel.setForeground(is13 ? new Color(0, 90, 60) : new Color(100, 50, 0));

        nNoteLabel.setText(is13
                ? "  NOTE: n must be even for Simpson's 1/3 Rule."
                : "  NOTE: n must be a multiple of 3 for Simpson's 3/8 Rule.");

        integralKeyLabel.setText(is13 ? "I = (h/3) · Σf(xi) =" : "I = (3h/8) · Σf(xi) =");
        integralKeyLabel.setForeground(headerColor);

        // Update table columns
        String[] cols = is13
                ? new String[]{"", "x", "f(x)", "mod 2", "1/3 rule", "f(xi)"}
                : new String[]{"", "x", "f(x)", "mod 3", "3/8 rule", "f(xi)"};
        tableModel.setColumns(cols);
        tableModel.clearTable();

        // Update table colors
        resultTable.setGridColor(gridColor);
        resultTable.setSelectionBackground(selColor);
        resultTable.getTableHeader().setBackground(headerColor);
        resultTable.repaint();

        // Update scroll pane border
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(accentColor, 1), " Iteration Table ",
                0, 0, new Font("Arial", Font.BOLD, 11), accentColor));

        // Update backgrounds
        inputPanel.setBackground(bgColor);
        summaryPanel.setBackground(footerColor);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, accentColor),
                BorderFactory.createEmptyBorder(8, 15, 10, 15)));
        mainContentPanel.setBackground(bgColor);
        getContentPane().setBackground(bgColor);

        // Update solve button color
        solveButton.setBackground(headerColor);

        // Update text field borders
        Color borderColor = is13 ? new Color(100, 180, 140) : new Color(200, 150, 80);
        for (JTextField f : new JTextField[]{equationField, aField, bField, nField}) {
            f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor),
                    BorderFactory.createEmptyBorder(3, 5, 3, 5)));
        }

        // Reset result labels
        lblN.setText("—"); lblA.setText("—"); lblB.setText("—"); lblH.setText("—");
        lblSumFxi.setText("—"); lblIntegral.setText("—");

        repaint();
        revalidate();
    }

    // ── Core Computation ──────────────────────────────────────────────────

    private void solveSimpson() {
        if (radio13.isSelected()) {
            solveSimpson13();
        } else {
            solveSimpson38();
        }
    }

    private void solveSimpson13() {
        try {
            String equation = equationField.getText().trim();
            if (equation.isEmpty()) { error("Please enter a function f(x)!"); return; }

            double a = Double.parseDouble(aField.getText().trim());
            double b = Double.parseDouble(bField.getText().trim());
            int    n = Integer.parseInt(nField.getText().trim());

            if (n < 2 || n % 2 != 0) {
                error("n must be an even number ≥ 2 for Simpson's 1/3 Rule.");
                return;
            }
            if (b <= a) { error("Upper limit b must be greater than lower limit a."); return; }

            double h = (b - a) / n;
            tableModel.clearTable();
            double sumFxi = 0.0;

            for (int i = 0; i <= n; i++) {
                double xi  = a + i * h;
                double fxi = ExpressionEvaluator.evaluate(equation, xi);
                if (Double.isNaN(fxi)) { error("f(x) could not be evaluated at x = " + xi); return; }

                int modVal = i % 2;
                int mult;
                if      (i == 0 || i == n) mult = 1;
                else if (i % 2 == 1)       mult = 4;
                else                       mult = 2;

                double contribution = mult * fxi;
                sumFxi += contribution;
                tableModel.addRow(new SimpsonIteration(i, "x" + i, xi, fxi, modVal, mult, contribution));
            }

            double integral = (h / 3.0) * sumFxi;
            updateSummary(n, a, b, h, sumFxi, integral);

        } catch (NumberFormatException ex) {
            error("Please enter valid numeric values for a, b, and n.");
        } catch (Exception ex) {
            error("Calculation error: " + ex.getMessage());
        }
    }

    private void solveSimpson38() {
        try {
            String equation = equationField.getText().trim();
            if (equation.isEmpty()) { error("Please enter a function f(x)!"); return; }

            double a = Double.parseDouble(aField.getText().trim());
            double b = Double.parseDouble(bField.getText().trim());
            int    n = Integer.parseInt(nField.getText().trim());

            if (n < 3 || n % 3 != 0) {
                error("n must be a multiple of 3 (e.g. 3, 6, 9…) for Simpson's 3/8 Rule.");
                return;
            }
            if (b <= a) { error("Upper limit b must be greater than lower limit a."); return; }

            double h = (b - a) / n;
            tableModel.clearTable();
            double sumFxi = 0.0;

            for (int i = 0; i <= n; i++) {
                double xi  = a + i * h;
                double fxi = ExpressionEvaluator.evaluate(equation, xi);
                if (Double.isNaN(fxi)) { error("f(x) could not be evaluated at x = " + xi); return; }

                int modVal = i % 3;
                int mult;
                if      (i == 0 || i == n) mult = 1;
                else if (i % 3 == 0)       mult = 2;
                else                       mult = 3;

                double contribution = mult * fxi;
                sumFxi += contribution;
                tableModel.addRow(new SimpsonIteration(i, "x" + i, xi, fxi, modVal, mult, contribution));
            }

            double integral = (3.0 * h / 8.0) * sumFxi;
            updateSummary(n, a, b, h, sumFxi, integral);

        } catch (NumberFormatException ex) {
            error("Please enter valid numeric values for a, b, and n.");
        } catch (Exception ex) {
            error("Calculation error: " + ex.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void updateSummary(int n, double a, double b, double h, double sumFxi, double integral) {
        lblN.setText(String.valueOf(n));
        lblA.setText(String.format("%.4f", a));
        lblB.setText(String.format("%.4f", b));
        lblH.setText(String.format("%.4f", h));
        lblSumFxi.setText(String.format("%.4f", sumFxi));
        lblIntegral.setText(String.format("%.6f", integral));
    }

    private void clearAll() {
        equationField.setText(""); aField.setText(""); bField.setText(""); nField.setText("");
        tableModel.clearTable();
        lblN.setText("—"); lblA.setText("—"); lblB.setText("—"); lblH.setText("—");
        lblSumFxi.setText("—"); lblIntegral.setText("—");
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.PLAIN, 12));
        return l;
    }

    private JLabel makeSummaryKey(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        l.setForeground(new Color(40, 60, 50));
        return l;
    }

    private JLabel makeSummaryVal(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Monospaced", Font.PLAIN, 12));
        l.setForeground(new Color(0, 100, 60));
        return l;
    }

    private void styleTextField(JTextField f, Color borderColor) {
        f.setFont(new Font("Monospaced", Font.PLAIN, 12));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
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

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}