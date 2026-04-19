import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class Table extends AbstractTableModel {
    ArrayList<BinomialIterations> binomialIterations;
    String [] columns = {"Iteration", "X0", "f(X0)", "X1", "f(X1)", "X2", "f(X2)", "Ea"};

    public Table(){
        binomialIterations = new ArrayList<>();
    }

    public void clearTable() {
        binomialIterations.clear();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return binomialIterations.size();
    }

    public void addBinomialIteration(BinomialIterations binomialIteration){
        binomialIterations.add(binomialIteration);
        fireTableRowsInserted(binomialIterations.size() - 1, binomialIterations.size() - 1);
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
        BinomialIterations binomialIteration = binomialIterations.get(rowIndex);
        switch(columnIndex) {
            case 0: return binomialIteration.iteration;
            case 1: return String.format("%.2f", binomialIteration.getxZero());
            case 2: return String.format("%.2f", binomialIteration.getFxZero());
            case 3: return String.format("%.2f", binomialIteration.getxOne());
            case 4: return String.format("%.2f", binomialIteration.getFxOne());
            case 5: return String.format("%.2f", binomialIteration.getxTwo());
            case 6: return String.format("%.2f", binomialIteration.getFunctionOfxTwo());
            case 7:
                if (binomialIteration.iteration == 1) {
                    return "";
                } else {
                    return String.format("%.2f", binomialIteration.getMarginError());
                }
            default: return "";
        }
    }
}