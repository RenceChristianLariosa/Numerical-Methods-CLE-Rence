import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class GaussSeidelTable extends AbstractTableModel {
    ArrayList<GaussSeidelIteration> iterations;
    String[] columns = {"Iteration", "x₁", "x₂", "x₃", "x'₁", "x'₂", "x'₃", "Ea₁", "Ea₂", "Ea₃"};

    public GaussSeidelTable() {
        iterations = new ArrayList<>();
    }

    public void clearTable() {
        iterations.clear();
        fireTableDataChanged();
    }

    public void addIteration(GaussSeidelIteration iteration) {
        iterations.add(iteration);
        fireTableRowsInserted(iterations.size() - 1, iterations.size() - 1);
    }

    @Override
    public int getRowCount() {
        return iterations.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        GaussSeidelIteration iter = iterations.get(rowIndex);
        switch (columnIndex) {
            case 0: return iter.getIteration();
            case 1: return String.format("%.2f", iter.getX1());
            case 2: return String.format("%.2f", iter.getX2());
            case 3: return String.format("%.2f", iter.getX3());
            case 4: return String.format("%.2f", iter.getNewX1());
            case 5: return String.format("%.2f", iter.getNewX2());
            case 6: return String.format("%.2f", iter.getNewX3());
            case 7:
                if (iter.getIteration() == 1) return "-";
                return String.format("%.2f", iter.getEa1());
            case 8:
                if (iter.getIteration() == 1) return "-";
                return String.format("%.2f", iter.getEa2());
            case 9:
                if (iter.getIteration() == 1) return "-";
                return String.format("%.2f", iter.getEa3());
            default: return "";
        }
    }
}