import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class SimpsonTable extends AbstractTableModel {
    ArrayList<SimpsonIteration> rows;
    String[] columns;

    // Pass column headers so same model works for 1/3 and 3/8
    public SimpsonTable(String[] columns) {
        this.columns = columns;
        rows = new ArrayList<>();
    }

    public void setColumns(String[] newColumns) {
        this.columns = newColumns;
        fireTableStructureChanged();
    }

    public void clearTable() {
        rows.clear();
        fireTableDataChanged();
    }

    public void addRow(SimpsonIteration row) {
        rows.add(row);
        fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
    }

    @Override public int getRowCount()    { return rows.size(); }
    @Override public int getColumnCount() { return columns.length; }
    @Override public String getColumnName(int col) { return columns[col]; }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        SimpsonIteration row = rows.get(rowIndex);
        switch (colIndex) {
            case 0: return row.getLabel();
            case 1: return String.format("%.4f", row.getX());
            case 2: return String.format("%.4f", row.getFx());
            case 3: return row.getMod();
            case 4: return row.getSimpsonRule();
            case 5: return String.format("%.4f", row.getFxi());
            default: return "";
        }
    }
}