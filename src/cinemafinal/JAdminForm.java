package cinemafinal;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class JAdminForm extends JFrame {
    JLabel headerLabel;
    JLabel adminUsernameLabel;
    JTextField adminUsernameField;
    JLabel adminUsernameRequired;

    JLabel adminPasswordLabel;
    JPasswordField adminPasswordField;
    JLabel adminPasswordRequired;
    JButton adminLoginButton;
    ImageIcon logoImage;

    public JAdminForm() {
        setSize(450, 400);
        setLayout(null);
        setTitle("Admin Login");
        setResizable(false);
        logoImage = new ImageIcon("logo.jpg");
        setIconImage(logoImage.getImage());

        headerLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));

        adminUsernameLabel = new JLabel("Admin Username");
        adminUsernameField = new JTextField();
        adminUsernameField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                adminUsernameRequired.setVisible(false);
            }
            public void focusLost(FocusEvent e) {
                adminUsernameRequired.setVisible(adminUsernameField.getText().isEmpty());
            }
        });
        adminUsernameRequired = new JLabel("*Admin username is required*");
        adminUsernameRequired.setForeground(Color.red);
        adminUsernameRequired.setVisible(false);

        adminPasswordLabel = new JLabel("Admin Password");
        adminPasswordField = new JPasswordField();
        adminPasswordField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                adminPasswordRequired.setVisible(false);
            }
            public void focusLost(FocusEvent e) {
                adminPasswordRequired.setVisible(new String(adminPasswordField.getPassword()).isEmpty());
            }
        });
        adminPasswordRequired = new JLabel("*Admin password is required*");
        adminPasswordRequired.setForeground(Color.red);
        adminPasswordRequired.setVisible(false);

        adminLoginButton = new JButton("Login");
        adminLoginButton.addActionListener(new AdminSubmitWatcher());

        headerLabel.setBounds(100, 30, 250, 30);
        adminUsernameLabel.setBounds(50, 100, 150, 30);
        adminUsernameField.setBounds(200, 100, 200, 30);
        adminUsernameRequired.setBounds(200, 130, 200, 20);

        adminPasswordLabel.setBounds(50, 170, 150, 30);
        adminPasswordField.setBounds(200, 170, 200, 30);
        adminPasswordRequired.setBounds(200, 200, 200, 20);

        adminLoginButton.setBounds(150, 250, 150, 30);

        add(headerLabel);
        add(adminUsernameLabel);
        add(adminUsernameField);
        add(adminUsernameRequired);
        add(adminPasswordLabel);
        add(adminPasswordField);
        add(adminPasswordRequired);
        add(adminLoginButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private class AdminSubmitWatcher implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String adminUsername = adminUsernameField.getText();
            String adminPassword = new String(adminPasswordField.getPassword());
            if (validateAdminLogin(adminUsername, adminPassword)) {
                JOptionPane.showMessageDialog(null, "Admin login successful");
                dispose();
                JMenuForm x = new JMenuForm();
                x.setVisible(true);
                x.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid admin username or password");
            }
        }

        private boolean validateAdminLogin(String username, String password) {
            boolean isValid = false;
            Connection con = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
                stmt = con.prepareStatement("SELECT * FROM admin_table WHERE admin_name = ? AND admin_password = ?");
                stmt.setString(1, username);
                stmt.setString(2, password);
                rs = stmt.executeQuery();
                
                if (rs.next()) {
                    isValid = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (con != null) con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return isValid;
        }
    }
}
