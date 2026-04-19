import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class NewtonTable extends AbstractTableModel {
    ArrayList<NewtonIteration> newtonIterations;
    String[] columns = {"Iteration", "X0", "f(X0)", "f'(X0)", "X1", "Ea"};

    public NewtonTable() {
        newtonIterations = new ArrayList<>();
    }

    public void clearTable() {
        newtonIterations.clear();
        fireTableDataChanged();
    }

    public void addNewtonIteration(NewtonIteration iteration) {
        newtonIterations.add(iteration);
        fireTableRowsInserted(newtonIterations.size() - 1, newtonIterations.size() - 1);
    }

    @Override
    public int getRowCount() {
        return newtonIterations.size();
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
        NewtonIteration iteration = newtonIterations.get(rowIndex);
        switch (columnIndex) {
            case 0: return iteration.iteration;
            case 1: return String.format("%.2f", iteration.getxZero());
            case 2: return String.format("%.2f", iteration.getFxZero());
            case 3: return String.format("%.2f", iteration.getDerivativeFxZero());
            case 4: return String.format("%.2f", iteration.getxOne());
            case 5:
                if (iteration.iteration == 1) {
                    return "";
                } else {
                    return String.format("%.2f", iteration.getMarginError());
                }
            default: return "";
        }
    }
}