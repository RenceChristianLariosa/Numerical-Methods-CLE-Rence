import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class SecantTable extends AbstractTableModel {
    ArrayList<SecantIteration> secantIterations;
    String[] columns = {"Iteration", "X0", "f(X0)", "X1", "f(X1)", "X2", "f(X2)", "Ea"};

    public SecantTable() {
        secantIterations = new ArrayList<>();
    }

    public void clearTable() {
        secantIterations.clear();
        fireTableDataChanged();
    }

    public void addSecantIteration(SecantIteration iteration) {
        secantIterations.add(iteration);
        fireTableRowsInserted(secantIterations.size() - 1, secantIterations.size() - 1);
    }

    @Override
    public int getRowCount() {
        return secantIterations.size();
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
        SecantIteration iteration = secantIterations.get(rowIndex);
        switch (columnIndex) {
            case 0: return iteration.iteration;
            case 1: return String.format("%.2f", iteration.getxZero());
            case 2: return String.format("%.2f", iteration.getFxZero());
            case 3: return String.format("%.2f", iteration.getxOne());
            case 4: return String.format("%.2f", iteration.getFxOne());
            case 5: return String.format("%.2f", iteration.getxTwo());
            case 6: return String.format("%.2f", iteration.getFunctionOfxTwo());
            case 7:
                if (iteration.iteration == 1) {
                    return "";
                } else {
                    return String.format("%.2f", iteration.getMarginError());
                }
            default: return "";
        }
    }
}