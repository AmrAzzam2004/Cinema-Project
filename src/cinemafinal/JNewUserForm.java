package cinemafinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JNewUserForm extends JFrame {

    private JLabel signupLabel;
    private JLabel usernameLabel;
    private JTextField username;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JLabel emailLabel;
    private JTextField emailField;
    private JLabel phoneNumberLabel;
    private JTextField phoneNumber;
    private JButton submit;
    private JButton backButton;
    private ImageIcon logoImage;

    public JNewUserForm() {
        setSize(700, 500);
        setLayout(null);
        setTitle("RegistrationForm");
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

        // Phone Number (optional)
        phoneNumberLabel = new JLabel("Phone Number");
        phoneNumber = new JTextField("");
        getContentPane().add(phoneNumberLabel);
        getContentPane().add(phoneNumber);

        // Submit button
        submit = new JButton("SUBMIT");
        getContentPane().add(submit);
        submit.addActionListener(new SubmitWatcher());

        // Back button
        backButton = new JButton("BACK");
        getContentPane().add(backButton);
        backButton.addActionListener(new BackButtonWatcher());

        // Set bounds for components
        signupLabel.setBounds(280, -100, 300, 300);
        usernameLabel.setBounds(50, 100, 300, 30);
        username.setBounds(200, 100, 300, 30);
        passwordLabel.setBounds(50, 150, 300, 30);
        passwordField.setBounds(200, 150, 300, 30);
        emailLabel.setBounds(50, 200, 300, 30);
        emailField.setBounds(200, 200, 300, 30);
        phoneNumberLabel.setBounds(50, 250, 300, 30);
        phoneNumber.setBounds(200, 250, 300, 30);
        submit.setBounds(400, 350, 100, 30);
        backButton.setBounds(100, 350, 150, 30);
    }

    private class SubmitWatcher implements ActionListener {
        @SuppressWarnings("deprecation")
        @Override
        public void actionPerformed(ActionEvent e) {
            if (username.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must enter the username");
            } else if (passwordField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Enter your password");
            } else if (!emailField.getText().contains("@")) {
                JOptionPane.showMessageDialog(null, "Enter a valid email");
            } else if (!phoneNumber.getText().matches("\\d*")) {
                JOptionPane.showMessageDialog(null, "Enter your phone number as numbers");
            } else {
                DBFetch_Elements x= new DBFetch_Elements("user_table");
                int primaryKey = x.fetchMaxID("film_id") + 1;
                DBInsert_Data inserter = new DBInsert_Data("user_table", String.valueOf(primaryKey), username.getText(), passwordField.getText(), emailField.getText(), phoneNumber.getText());
                inserter.insert_data_to_data_base();
                JOptionPane.showMessageDialog(null, "Registration Successful");
            }
        }
    }

    private class BackButtonWatcher implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose(); // Close the current registration form
        }
    }
}
