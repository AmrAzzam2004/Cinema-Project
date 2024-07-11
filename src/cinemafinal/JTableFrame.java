package cinemafinal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class JTableFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public JTableFrame(DefaultTableModel tableModel, int x, int y, int width, int height) {
        this.tableModel = tableModel;

        setSize(width, height);
        setLocation(x, y);
        setLayout(null);

        // Create the table and scroll pane
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, width - 30, height - 50); // Adjusted bounds to fit within frame
        getContentPane().add(scrollPane);
    }

    // Method to get the table
    public JTable getTable() {
        return table;
    }

    // Method to get the table model
    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    // Method to set the table model
    public void setTableModel(DefaultTableModel newModel) {
        table.setModel(newModel);
        tableModel = newModel;
    }

    // Method to add a row to the table model
    public void addRow(Object[] rowData) {
        tableModel.addRow(rowData);
    }

    // Method to remove a row from the table model
    public void removeRow(int rowIndex) {
        tableModel.removeRow(rowIndex);
    }

    // Method to clear all rows from the table model
    public void clearTable() {
        tableModel.setRowCount(0);
    }

    // Main method for testing

}
