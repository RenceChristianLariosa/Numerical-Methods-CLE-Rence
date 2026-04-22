import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TrapezoidTable extends AbstractTableModel {
    ArrayList<TrapezoidalIteration> rows;
    String[] columns = {"", "x", "f(x)", "Trap Rule", "f(xi)"};

    public TrapezoidTable() {
        rows = new ArrayList<>();
    }

    public void clearTable() {
        rows.clear();
        fireTableDataChanged();
    }

    public void addRow(TrapezoidalIteration row) {
        rows.add(row);
        fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
    }

    @Override public int getRowCount()    { return rows.size(); }
    @Override public int getColumnCount() { return columns.length; }
    @Override public String getColumnName(int col) { return columns[col]; }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        TrapezoidalIteration row = rows.get(rowIndex);
        switch (colIndex) {
            case 0: return row.getLabel();
            case 1: return String.format("%.4f", row.getX());
            case 2: return String.format("%.4f", row.getFx());
            case 3: return row.getTrapRule();
            case 4: return String.format("%.4f", row.getFxi());
            default: return "";
        }
    }
}