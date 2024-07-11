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

public class JUserEditor extends JFrame {
    private JLabel signupLabel;
    private JLabel usernameLabel;
    private JTextField username;

    private JLabel passwordLabel;
    private JPasswordField passwordField;

    private JLabel emailLabel;
    private JTextField emailField;

    private JLabel phoneNumberLabel;
    private JTextField phoneNumber;

    private ImageIcon logoImage;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public JUserEditor() {
        setSize(1000, 700);
        setLayout(null);
        setTitle("User Editor");
        setResizable(false);
        logoImage = new ImageIcon("logo.jpg");
        setIconImage(logoImage.getImage());
        
        // Header
        signupLabel = new JLabel("Signup");
        signupLabel.setFont(new Font(null, Font.CENTER_BASELINE, 30));
        getContentPane().add(signupLabel);

        // Username
        usernameLabel = new JLabel("Username");
        username = new JTextField();
        getContentPane().add(usernameLabel);
        getContentPane().add(username);

        // Password
        passwordLabel = new JLabel("Password");
        passwordField = new JPasswordField();
        getContentPane().add(passwordLabel);
        getContentPane().add(passwordField);

        // Email
        emailLabel = new JLabel("Email");
        emailField = new JTextField();
        getContentPane().add(emailLabel);
        getContentPane().add(emailField);

        // Phone Number
        phoneNumberLabel = new JLabel("Phone Number");
        phoneNumber = new JTextField("");
        getContentPane().add(phoneNumberLabel);
        getContentPane().add(phoneNumber);

        // Table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Username");
        tableModel.addColumn("Password");
        tableModel.addColumn("Email");
        tableModel.addColumn("Phone Number");

        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBounds(550, 100, 430, 550);
        getContentPane().add(scrollPane);
        table_update();
        
        // Add RowSelectionListener to the userTable
        userTable.getSelectionModel().addListSelectionListener(new RowSelectionListener());

        // Buttons
        editButton = new JButton("Edit");
        editButton.addActionListener(new EditButtonWatcher());
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteButtonWatcher());
        backButton = new JButton("Back");
        backButton.addActionListener(new BackButtonWatcher());

        // Set bounds for existing components
        signupLabel.setBounds(50, 30, 300, 30);
        usernameLabel.setBounds(50, 100, 150, 30);
        username.setBounds(250, 100, 250, 30);
        passwordLabel.setBounds(50, 150, 150, 30);
        passwordField.setBounds(250, 150, 250, 30);
        emailLabel.setBounds(50, 200, 150, 30);
        emailField.setBounds(250, 200, 250, 30);
        phoneNumberLabel.setBounds(50, 250, 150, 30);
        phoneNumber.setBounds(250, 250, 250, 30);
        editButton.setBounds(100, 450, 100, 30);
        deleteButton.setBounds(250, 450, 100, 30);
        backButton.setBounds(400, 450, 100, 30);

        getContentPane().add(editButton);
        getContentPane().add(deleteButton);
        getContentPane().add(backButton);
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
            DefaultTableModel model = (DefaultTableModel) userTable.getModel();
            int selectedIndex = userTable.getSelectedRow();
            if (selectedIndex != -1) {
                int id = Integer.parseInt(model.getValueAt(selectedIndex, 0).toString());
                int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Warning", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
                        PreparedStatement deleteStatement = con.prepareStatement("DELETE FROM user_table WHERE user_id = ?");
                        deleteStatement.setInt(1, id);
                        deleteStatement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Record Deleted");

                        // Clear input fields
                        username.setText("");
                        passwordField.setText("");
                        emailField.setText("");
                        phoneNumber.setText("");

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
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
            PreparedStatement insert = con.prepareStatement("SELECT * FROM user_table");
            ResultSet rs = insert.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            DefaultTableModel model = (DefaultTableModel) userTable.getModel();
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
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        int selectedIndex = userTable.getSelectedRow();
        try {
            int id = Integer.parseInt(model.getValueAt(selectedIndex, 0).toString());
            String name = username.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            String phone = phoneNumber.getText();

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
            PreparedStatement updateStatement = con.prepareStatement("UPDATE user_table SET user_name= ?, user_password= ?, user_email= ?, phone_number= ? WHERE user_id= ?");
            updateStatement.setString(1, name);
            updateStatement.setString(2, password);
            updateStatement.setString(3, email);
            updateStatement.setString(4, phone);
            updateStatement.setInt(5, id);
            updateStatement.executeUpdate();

            // Clear input fields
            username.setText("");
            passwordField.setText("");
            emailField.setText("");
            phoneNumber.setText("");

            table_update();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private class RowSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Get data from the selected row and populate text fields
                    String usernameValue = userTable.getValueAt(selectedRow, 1).toString();
                    String password = userTable.getValueAt(selectedRow, 2).toString();
                    String email = userTable.getValueAt(selectedRow, 3).toString();
                    String phone = userTable.getValueAt(selectedRow, 4).toString();
                    username.setText(usernameValue);
                    passwordField.setText(password);
                    emailField.setText(email);
                    phoneNumber.setText(phone);
                }
            }
        }
    }
}
