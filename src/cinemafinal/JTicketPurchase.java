package cinemafinal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

public class JTicketPurchase extends JFrame {
    private JComboBox<String> adultTicketsCombo;
    private JComboBox<String> childTicketsCombo;
    private JButton backButton;
    private JButton buyButton;
    private JTextArea receiptArea;
    private JLabel tendoller;
    private JLabel twentydoller;
    private JLabel selectedmoviLabel;
    private JLabel adultLabel;
    private JLabel childLabel;
    private JLabel movieLabel;
    private JTable movieTable;
    private DefaultTableModel tableModel;
    private Connection con1;
    private PreparedStatement insert;

    private String movieName;
    private double adultPrice = 20.0;
    private double childPrice = 10.0;

    public JTicketPurchase() {
        setSize(1000, 700);
        setLayout(null);
        setTitle("Ticket Purchase");
        setResizable(false);

        movieLabel = new JLabel();
        movieLabel.setFont(new Font("Arial", Font.BOLD, 20));
        movieLabel.setBounds(250, 10, 400, 30);
        getContentPane().add(movieLabel);
        selectedmoviLabel = new JLabel("the selected movie :");
        selectedmoviLabel.setFont(new Font("Arial", Font.BOLD, 20));
        getContentPane().add(selectedmoviLabel);
        selectedmoviLabel.setBounds(50, 10, 400, 30);
        adultLabel = new JLabel("Adult Tickets:");
        adultLabel.setBounds(50, 50, 100, 30);
        getContentPane().add(adultLabel);

        adultTicketsCombo = new JComboBox<>();
        for (int i = 0; i <= 100; i++) {
            adultTicketsCombo.addItem(String.valueOf(i));
        }
        adultTicketsCombo.setBounds(150, 50, 100, 30);
        getContentPane().add(adultTicketsCombo);

        childLabel = new JLabel("Child Tickets:");
        childLabel.setBounds(50, 100, 100, 30);
        getContentPane().add(childLabel);
        twentydoller = new JLabel("20$");
        tendoller = new JLabel("10$");
        tendoller.setFont(new Font("Arial", Font.BOLD, 20));
        twentydoller.setFont(new Font("Arial", Font.BOLD, 20));
        tendoller.setBounds(300, 100, 100, 30);
        twentydoller.setBounds(300, 50, 100, 30);
        getContentPane().add(tendoller);
        getContentPane().add(twentydoller);
        
        childTicketsCombo = new JComboBox<>();
        for (int i = 0; i <= 100; i++) {
            childTicketsCombo.addItem(String.valueOf(i));
        }
        childTicketsCombo.setBounds(150, 100, 100, 30);
        getContentPane().add(childTicketsCombo);

        buyButton = new JButton("Buy");
        buyButton.setBounds(100, 200, 100, 30);
        buyButton.addActionListener(new BuyButtonListener());
        getContentPane().add(buyButton);

        backButton = new JButton("Back");
        backButton.setBounds(250, 200, 100, 30);
        backButton.addActionListener(new BackButtonListener());
        getContentPane().add(backButton);

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        scrollPane.setBounds(50, 300, 400, 250);
        getContentPane().add(scrollPane);

        // Table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Time");
        tableModel.addColumn("Ticket Amount");
        tableModel.addColumn("Category");

        movieTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(movieTable);
        tableScrollPane.setBounds(550, 100, 430, 550);
        getContentPane().add(tableScrollPane);
        table_update();
    }

    private class BuyButtonListener implements ActionListener {
        private int receiptNumber = 1; // Initial receipt number

        @Override
        public void actionPerformed(ActionEvent e) {
            int adultTickets = Integer.parseInt(adultTicketsCombo.getSelectedItem().toString());
            int childTickets = Integer.parseInt(childTicketsCombo.getSelectedItem().toString());

            double totalCost = (adultTickets * adultPrice) + (childTickets * childPrice);
            String movieWithSelection = movieName + " (You selected)";
            receiptArea.setText("Movie: " + movieWithSelection + "\n" +
                    "Adult Tickets: " + adultTickets + " x $" + adultPrice + " = $" + (adultTickets * adultPrice) + "\n" +
                    "Child Tickets: " + childTickets + " x $" + childPrice + " = $" + (childTickets * childPrice) + "\n" +
                    "Total Cost: $" + totalCost);

            // Save receipt to file
            String receiptFileName = "receipt(" + receiptNumber + ").txt";
            try (FileWriter writer = new FileWriter(receiptFileName)) {
                writer.write(receiptArea.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            int option = JOptionPane.showConfirmDialog(null, "Payment Method: Cash (OK) or Credit (Cancel)?", "Payment Method", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                JOptionPane.showMessageDialog(null, "Payment is complete. Enjoy the movie!");
            } else if (option == JOptionPane.CANCEL_OPTION) {
                JFrame creditFrame = new JFrame("Credit Card Payment");
                JPanel panel = new JPanel();
                JLabel cvcLabel = new JLabel("CVC:");
                JTextField cvcField = new JTextField(10);
                JLabel codeLabel = new JLabel("Code:");
                JTextField codeField = new JTextField(10);
                JButton submitButton = new JButton("Submit");

                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null, "Payment is complete. Enjoy the movie!");
                        creditFrame.dispose();
                    }
                });

                panel.add(cvcLabel);
                panel.add(cvcField);
                panel.add(codeLabel);
                panel.add(codeField);
                panel.add(submitButton);

                creditFrame.getContentPane().add(panel);
                creditFrame.pack();
                creditFrame.setVisible(true);
                creditFrame.setLocationRelativeTo(null);
            }

            // Update the ticket amount in the database
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
                PreparedStatement updateStatement = con.prepareStatement("UPDATE film_table SET films_ticket_amount = films_ticket_amount - ? WHERE film_name = ?");
                updateStatement.setInt(1, adultTickets + childTickets);
                updateStatement.setString(2, movieName);
                updateStatement.executeUpdate();
                table_update();
                // Insert ticket sale information
                PreparedStatement insertSaleStatement = con.prepareStatement("INSERT INTO ammount_ticket_sell (ticket_sell, the_cost) VALUES (?, ?)");
                insertSaleStatement.setInt(1, adultTickets + childTickets);
                insertSaleStatement.setDouble(2, totalCost);
                insertSaleStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // Increment receipt number for the next receipt
            receiptNumber++;
        }
    }


    private class BackButtonListener implements ActionListener {
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
            movieTable.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = movieTable.getSelectedRow();
                    if (selectedRow != -1) {
                        movieName = movieTable.getValueAt(selectedRow, 1).toString();
                        movieLabel.setText(movieName);
                    }
                }
            });
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }
}