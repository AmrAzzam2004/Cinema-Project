package cinemafinal;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JAdminEditor extends JFrame {
    private JLabel signupLabel;
    private JLabel nameLabel;
    private JTextField nameField;

    private JLabel passwordLabel;
    private JPasswordField passwordField;

    private JLabel emailLabel;
    private JTextField emailField;

    private ImageIcon logoImage;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;
    private JButton addButton; // New button
    private JTable adminTable; // New table variable
    private DefaultTableModel tableModel; // Table model for the JTable

    public JAdminEditor() {
        setSize(1000, 700);
        setLayout(null);
        setTitle("Admin Editor");
        setResizable(false);
        logoImage = new ImageIcon("logo.jpg");
        setIconImage(logoImage.getImage());

        // The header
        signupLabel = new JLabel("Signup");
        signupLabel.setFont(new Font(null, Font.CENTER_BASELINE, 30));
        getContentPane().add(signupLabel);

        // Name
        nameLabel = new JLabel("Name");
        nameField = new JTextField();
        getContentPane().add(nameLabel);
        getContentPane().add(nameField);

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

        // Table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Password");
        tableModel.addColumn("Email");

        adminTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(adminTable);
        scrollPane.setBounds(550, 100, 430, 550);
        getContentPane().add(scrollPane);
        table_update();
        adminTable.getSelectionModel().addListSelectionListener(new RowSelectionListener());
        
        // Buttons
        editButton = new JButton("Edit");
        editButton.addActionListener(new EditButtonWatcher());
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteButtonWatcher());
        backButton = new JButton("Back");
        backButton.addActionListener(new BackButtonWatcher());
        addButton = new JButton("Add"); // New button
        addButton.addActionListener(new AddButtonWatcher()); // New action listener

        // Set bounds for components
        signupLabel.setBounds(50, 20, 300, 30);

        nameLabel.setBounds(50, 100, 300, 30);
        nameField.setBounds(200, 100, 250, 30);

        passwordLabel.setBounds(50, 150, 300, 30);
        passwordField.setBounds(200, 150, 250, 30);

        emailLabel.setBounds(50, 200, 300, 30);
        emailField.setBounds(200, 200, 250, 30);

        addButton.setBounds(50, 450, 100, 30); // New button bounds
        editButton.setBounds(200, 450, 100, 30);
        deleteButton.setBounds(350, 450, 100, 30);
        backButton.setBounds(50, 550, 100, 30);

        getContentPane().add(editButton);
        getContentPane().add(deleteButton);
        getContentPane().add(backButton);
        getContentPane().add(addButton); // Add the new button to the content pane
    }

   private class EditButtonWatcher implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = adminTable.getSelectedRow();
            if (selectedIndex != -1) {
                String id = adminTable.getValueAt(selectedIndex, 0).toString();
                String name = nameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
                    PreparedStatement updateStatement = con.prepareStatement("UPDATE admin_table SET admin_name=?, admin_password=?, admin_email=? WHERE admin_id=?");
                    updateStatement.setString(1, name);
                    updateStatement.setString(2, password);
                    updateStatement.setString(3, email);
                    updateStatement.setString(4, id);
                    updateStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Admin updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    table_update();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a row to edit", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteButtonWatcher implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = adminTable.getSelectedRow();
            if (selectedIndex != -1) {
                String id = adminTable.getValueAt(selectedIndex, 0).toString();
                int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this admin?", "Warning", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    try {
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
                        PreparedStatement deleteStatement = con.prepareStatement("DELETE FROM admin_table WHERE admin_id=?");
                        deleteStatement.setString(1, id);
                        deleteStatement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Admin deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        table_update();
                    } catch (SQLException ex) {
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


    private class AddButtonWatcher implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            @SuppressWarnings("deprecation")
            String password = passwordField.getText(); // Assuming movie time is used as password
            String email = emailField.getText(); // Assuming movie category is used as email

            // Check if any field is empty
            if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit method
            }

            // Add to database
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
                PreparedStatement insertStatement = con.prepareStatement("INSERT INTO admin_table (admin_name, admin_password, admin_email) VALUES (?, ?, ?)");
                insertStatement.setString(1, name);
                insertStatement.setString(2, password); // Saving movie time as password
                insertStatement.setString(3, email); // Saving movie category as email
                insertStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Admin added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields(); // Clear input fields
                table_update(); // Refresh table
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    
        private void clearFields() {
            nameField.setText("");
            passwordField.setText("");
            emailField.setText("");
        }
    }
    private void table_update() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
            PreparedStatement selectStatement = con.prepareStatement("SELECT * FROM admin_table");
            ResultSet rs = selectStatement.executeQuery();
            DefaultTableModel model = (DefaultTableModel) adminTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("admin_id"),
                        rs.getString("admin_name"),
                        rs.getString("admin_password"),
                        rs.getString("admin_email")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private class RowSelectionListener implements ListSelectionListener {
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int selectedRow = adminTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get data from the selected row and populate text fields
                String name = adminTable.getValueAt(selectedRow, 1).toString(); // Assuming name is the first column after ID
                String password = adminTable.getValueAt(selectedRow, 2).toString(); // Assuming password is the second column after ID
                String email = adminTable.getValueAt(selectedRow, 3).toString(); // Assuming email is the third column after ID
                nameField.setText(name);
                passwordField.setText(password);
                emailField.setText(email);
            }
        }
    }
}

}