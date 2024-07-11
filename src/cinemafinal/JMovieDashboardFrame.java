package cinemafinal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JMovieDashboardFrame extends JFrame {
    private JLabel headerLabel;
    private JLabel moviesLabel;
    private JLabel totalMoviesLabel;
    private JTable movieTable;
    private DefaultTableModel movieTableModel;

    private JLabel totalTicketsSoldLabel;
    private JLabel totalMoneyMadeLabel;
    private JTable customerTable;
    private DefaultTableModel customerTableModel;
    private JButton backButton;

    public JMovieDashboardFrame() {
        // Set frame properties
        setTitle("Movie Dashboard");
        setSize(500, 800);
        setLocationRelativeTo(null);
        setLayout(null);

        // Header label
        headerLabel = new JLabel("Movie Dashboard");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBounds(20, 20, 200, 30);
        add(headerLabel);

        // Movies label
        moviesLabel = new JLabel("Movies:");
        moviesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moviesLabel.setBounds(20, 70, 200, 30);
        add(moviesLabel);

        // Movie table
        movieTableModel = new DefaultTableModel();
        movieTableModel.addColumn("ID");
        movieTableModel.addColumn("Name");
        movieTableModel.addColumn("Time");
        movieTableModel.addColumn("Ticket Amount");
        movieTableModel.addColumn("Category");
        movieTable = new JTable(movieTableModel);
        JScrollPane movieTableScrollPane = new JScrollPane(movieTable);
        movieTableScrollPane.setBounds(20, 110, 450, 200);
        add(movieTableScrollPane);

        // Total movies label
        totalMoviesLabel = new JLabel("Total Movies: 0");
        totalMoviesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalMoviesLabel.setBounds(20, 320, 200, 30);
        add(totalMoviesLabel);

        // Total tickets sold label
        totalTicketsSoldLabel = new JLabel("Total Tickets Sold: 0");
        totalTicketsSoldLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalTicketsSoldLabel.setBounds(20, 360, 200, 30);
        add(totalTicketsSoldLabel);

        // Total money made label
        totalMoneyMadeLabel = new JLabel("Total Money Made: $0.00");
        totalMoneyMadeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalMoneyMadeLabel.setBounds(20, 400, 250, 30);
        add(totalMoneyMadeLabel);

        // Customer table
        customerTableModel = new DefaultTableModel();
        customerTableModel.addColumn("Customer ID");
        customerTableModel.addColumn("ticket sold");
        customerTableModel.addColumn("amount of mouney");
        customerTable = new JTable(customerTableModel);
        JScrollPane customerTableScrollPane = new JScrollPane(customerTable);
        customerTableScrollPane.setBounds(20, 440, 450, 200);
        add(customerTableScrollPane);

 
        backButton = new JButton("Back");
        backButton.addActionListener(new BackButtonWatcher());
        add(backButton);
        backButton.setBounds(20, 700, 100, 30);

        fetchMovieData();

        fetchCustomerData();
    }

    private void fetchMovieData() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
            PreparedStatement selectStatement = con.prepareStatement("SELECT * FROM film_table");
            ResultSet rs = selectStatement.executeQuery();
            while (rs.next()) {
                String[] rowData = {
                        rs.getString("film_id"),
                        rs.getString("film_name"),
                        rs.getString("films_time"),
                        rs.getString("films_ticket_amount"),
                        rs.getString("films_category_id")
                };
                movieTableModel.addRow(rowData);
            }
            totalMoviesLabel.setText("Total Movies: " + movieTableModel.getRowCount());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void fetchCustomerData() {
    int totalTicketsSold = 0;
    double totalMoneyMade = 0.0;

    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
        PreparedStatement selectStatement = con.prepareStatement("SELECT * FROM ammount_ticket_sell");
        ResultSet rs = selectStatement.executeQuery();
        while (rs.next()) {
            String[] rowData = {
                    rs.getString("id"),
                    rs.getString("ticket_sell"),
                    rs.getString("the_cost")
            };
            customerTableModel.addRow(rowData);

          
            totalTicketsSold += Integer.parseInt(rs.getString("ticket_sell"));

            
            totalMoneyMade += Double.parseDouble(rs.getString("the_cost"));
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

   
    totalTicketsSoldLabel.setText("Total Tickets Sold: " + totalTicketsSold);

    totalMoneyMadeLabel.setText("Total Money Made: $" + totalMoneyMade);
}



    private class BackButtonWatcher implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
}
