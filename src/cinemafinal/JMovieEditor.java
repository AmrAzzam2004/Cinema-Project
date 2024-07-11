package cinemafinal;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class JMovieEditor extends JFrame {
    private JLabel idLabel;
    private JTextField idTextField;
    private JLabel movieNameLabel;
    private JTextField movieNameTextField;
    private JLabel movieTimeLabel;
    private JTextField movieTimeTextField;
    private JLabel movieCategoryLabel;
    private JTextField movieCategoryTextField;
    private JLabel ticketAmountLabel;
    private JTextField ticketAmountTextField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;
    private ImageIcon logoImage;
    private Connection con1;
    private PreparedStatement insert;
    private JTable movieTable;
    private DefaultTableModel tableModel;

    public JMovieEditor() {
        setSize(1000, 700);
        setLayout(null);
        setTitle("Movie Editor");
        setResizable(false);
        logoImage = new ImageIcon("logo.jpg");
        setIconImage(logoImage.getImage());
        
        // ID Label
        idLabel = new JLabel("ID");
        idLabel.setFont(new Font("", Font.PLAIN, 20));
        getContentPane().add(idLabel);
        idTextField = new JTextField();
      
        getContentPane().add(idTextField);
        
        // Movie name
        movieNameLabel = new JLabel("Movie Name");
        movieNameLabel.setFont(new Font(null, Font.PLAIN, 20));
        movieNameTextField = new JTextField();
        getContentPane().add(movieNameLabel);
        getContentPane().add(movieNameTextField);

        // Movie time
        movieTimeLabel = new JLabel("Movie Time");
        movieTimeLabel.setFont(new Font(null, Font.PLAIN, 20));
        movieTimeTextField = new JTextField();
        getContentPane().add(movieTimeLabel);
        getContentPane().add(movieTimeTextField);

        // Movie category
        movieCategoryLabel = new JLabel("Movie Category");
        movieCategoryLabel.setFont(new Font(null, Font.PLAIN, 20));
        movieCategoryTextField = new JTextField();
        getContentPane().add(movieCategoryLabel);
        getContentPane().add(movieCategoryTextField);

        // Ticket amount
        ticketAmountLabel = new JLabel("Ticket Amount");
        ticketAmountLabel.setFont(new Font(null, Font.PLAIN, 20));
        ticketAmountTextField = new JTextField();
        getContentPane().add(ticketAmountLabel);
        getContentPane().add(ticketAmountTextField);

        // Table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Time");
        tableModel.addColumn("Ticket Amount");
        tableModel.addColumn("Category");

        movieTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(movieTable);
        scrollPane.setBounds(550, 100, 430, 550);
        getContentPane().add(scrollPane);
        table_update();
        
        // Add RowSelectionListener to the movieTable
        movieTable.getSelectionModel().addListSelectionListener(new RowSelectionListener());

        // Buttons
        addButton = new JButton("Add");
        addButton.addActionListener(new AddButtonWatcher());
        editButton = new JButton("Edit");
        editButton.addActionListener(new EditButtonWatcher());
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteButtonWatcher());
        backButton = new JButton("Back");
        backButton.addActionListener(new BackButtonWatcher());

        // Set bounds for existing components
        movieNameLabel.setBounds(50, 100, 150, 30);
        movieNameTextField.setBounds(250, 100, 250, 30);
        movieTimeLabel.setBounds(50, 150, 150, 30);
        movieTimeTextField.setBounds(250, 150, 250, 30);
        movieCategoryLabel.setBounds(50, 250, 150, 30);
        movieCategoryTextField.setBounds(250, 250, 250, 30);
        ticketAmountLabel.setBounds(50, 200, 150, 30);
        ticketAmountTextField.setBounds(250, 200, 250, 30);
        addButton.setBounds(100, 450, 100, 30);
        editButton.setBounds(250, 450, 100, 30);
        deleteButton.setBounds(400, 450, 100, 30);
        backButton.setBounds(60, 580, 100, 30);
        idLabel.setBounds(50, 50, 150, 30);
        idTextField.setBounds(250, 50, 250, 30);

        getContentPane().add(addButton);
        getContentPane().add(editButton);
        getContentPane().add(deleteButton);
        getContentPane().add(backButton);
    }

    private class AddButtonWatcher implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = movieNameTextField.getText();
            String time = movieTimeTextField.getText();
            String ticketAmount = ticketAmountTextField.getText();
            String category = movieCategoryTextField.getText();

            // Check if any field is empty
            if (name.isEmpty() || time.isEmpty() || ticketAmount.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Something is wrong. Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit method
            }
            DBFetch_Elements x=new DBFetch_Elements("film_table");
             int primaryKey = x.fetchMaxID("film_id") + 1;
            // Add to database
            DBInsert_Data FE = new DBInsert_Data("film_table",String.valueOf(primaryKey), name, time, ticketAmount, category);
            FE.insert_data_to_data_base();

            // Show success message
            JOptionPane.showMessageDialog(null, "Movie is added!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear input fields
            idTextField.setText("");
            movieNameTextField.setText("");
            movieTimeTextField.setText("");
            ticketAmountTextField.setText("");
            movieCategoryTextField.setText("");

            // Refresh table
            table_update();
        }
    }

    private class EditButtonWatcher implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            update();
            JOptionPane.showMessageDialog(null, "The edit is done successfully");
        }
    }

    private class DeleteButtonWatcher implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultTableModel model = (DefaultTableModel) movieTable.getModel();
            int selectedIndex = movieTable.getSelectedRow();
            if (selectedIndex != -1) {
                int id = Integer.parseInt(model.getValueAt(selectedIndex, 0).toString());
                int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Warning", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
                        PreparedStatement deleteStatement = con.prepareStatement("DELETE FROM film_table WHERE film_id = ?");
                        deleteStatement.setInt(1, id);
                        deleteStatement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Record Deleted");
                        
                        // Clear input fields
                        idTextField.setText("");
                        movieNameTextField.setText("");
                        movieTimeTextField.setText("");
                        ticketAmountTextField.setText("");
                        movieCategoryTextField.setText("");

                        // Refresh table
                        table_update();
                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class BackButtonWatcher implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    private void table_update() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
            insert = con1.prepareStatement("SELECT * FROM film_table");
            ResultSet rs = insert.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            DefaultTableModel model = (DefaultTableModel) movieTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Vector<String> vector = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    vector.add(rs.getString(i));
                }
                model.addRow(vector);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void update() {
        DefaultTableModel model = (DefaultTableModel) movieTable.getModel();
        int selectedIndex = movieTable.getSelectedRow();
        try {
            int id = Integer.parseInt(model.getValueAt(selectedIndex, 0).toString());
            String name = movieNameTextField.getText();
            String time = movieTimeTextField.getText();
            String ticketAmount = ticketAmountTextField.getText();
            String category = movieCategoryTextField.getText();

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
            
            PreparedStatement updateStatement = con.prepareStatement("UPDATE film_table SET film_name= ?, films_time= ?, films_ticket_amount= ?, films_category_id= ? WHERE film_id= ?");
            updateStatement.setString(1, name);
            updateStatement.setString(2, time);
            updateStatement.setString(3, ticketAmount);
            updateStatement.setString(4, category);
            updateStatement.setInt(5, id);
            updateStatement.executeUpdate();
            

            // Clear input fields
            idTextField.setText("");
            movieNameTextField.setText("");
            movieTimeTextField.setText("");
            ticketAmountTextField.setText("");
            movieCategoryTextField.setText("");

            table_update();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private class RowSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = movieTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Get data from the selected row and populate text fields
                    String id = movieTable.getValueAt(selectedRow, 0).toString();
                    String movieName = movieTable.getValueAt(selectedRow, 1).toString();
                    String movieTime = movieTable.getValueAt(selectedRow, 2).toString();
                    String movieCategory = movieTable.getValueAt(selectedRow, 4).toString();
                    String ticketAmount = movieTable.getValueAt(selectedRow, 3).toString();
                    idTextField.setText(id);
                    movieNameTextField.setText(movieName);
                    movieTimeTextField.setText(movieTime);
                    movieCategoryTextField.setText(movieCategory);
                    ticketAmountTextField.setText(ticketAmount);
                }
            }
        }
    }

   
    
}
