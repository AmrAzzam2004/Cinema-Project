package cinemafinal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

public class JMenuForm extends JFrame{
    private ImageIcon logoImage;
    private JButton usermenubButton;
    private JButton moviemenuButton;
    private JButton adminmenuButton;
    private JButton buytickeButton;
    private JButton dashbourdButton;
    private JGraphicsPanel usermenuPanel;
    private JGraphicsPanel moviemenuPanel;
    private JGraphicsPanel adminmenurPanel;  
    private JGraphicsPanel buytickePanel;
    private JGraphicsPanel dashbourdPanel;
    public JMenuForm(){
        setSize(700, 540);
        setLayout(null);
        setResizable(false);
        setBackground(Color.green);
        setTitle("RegistrationForm");
        logoImage = new ImageIcon("images\\logo.jpg");
        setIconImage(logoImage.getImage());
        
        
        //menu buttons
        usermenubButton = new JButton("User Menu");
        usermenubButton.addActionListener(new UsermenuWatcher());
        usermenubButton.setPreferredSize(new Dimension(130, 50));

        moviemenuButton = new JButton("Moive Menu");
        moviemenuButton.addActionListener(new MoivemenuWatcher());
        moviemenuButton.setPreferredSize(new Dimension(130, 50));

        adminmenuButton = new JButton("Admin menu");
        adminmenuButton.addActionListener(new AdminMenuWatcher());
        adminmenuButton.setPreferredSize(new Dimension(130, 50));
        
        buytickeButton =new JButton("Buy Ticket");
        buytickeButton.addActionListener(new BuytickeWatcher());
        buytickeButton.setPreferredSize(new Dimension(130, 50));

        dashbourdButton = new JButton("DashBourd");
        dashbourdButton.addActionListener(new DashbourdWatcher());
        dashbourdButton.setPreferredSize(new Dimension(130, 50));

        
        usermenuPanel = new JGraphicsPanel(200,180);
        usermenuPanel.setImage("images\\usermenu.png");
        
        moviemenuPanel = new JGraphicsPanel(230,200);
        moviemenuPanel.setImage("images\\moviemenu.jpg");
        
        adminmenurPanel = new JGraphicsPanel(230,200);
        adminmenurPanel.setImage("images\\admineditor.png");
        
        buytickePanel = new JGraphicsPanel(350,200);
        buytickePanel.setImage("images\\buyticket.jpg");
        
        dashbourdPanel = new JGraphicsPanel(330,200);
        dashbourdPanel.setImage("images\\dashboard.png");

        
        //Parameters
        usermenubButton.setBounds(0, 200, 230, 50);
        moviemenuButton.setBounds(230, 200, 230, 50);
        adminmenuButton.setBounds(457, 200, 230, 50);
        buytickeButton.setBounds(0, 450, 350, 50);
        dashbourdButton.setBounds(350, 450, 355, 50);
        usermenuPanel.setBounds(10, 10, 200, 180);
        moviemenuPanel.setBounds(230, 10, 230, 190);
        adminmenurPanel.setBounds(457, 10, 230, 190);
        buytickePanel.setBounds(0, 250, 350, 200);
        dashbourdPanel.setBounds(350, 250, 330, 200);
        getContentPane().add(usermenubButton);
        getContentPane().add(moviemenuButton);
        getContentPane().add(adminmenuButton);
        getContentPane().add(buytickeButton);
        getContentPane().add(dashbourdButton);
        getContentPane().add(usermenuPanel);
        getContentPane().add(moviemenuPanel);
        getContentPane().add(adminmenurPanel);
        getContentPane().add(buytickePanel);
        getContentPane().add(dashbourdPanel);
    }
    private class UsermenuWatcher implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JUserMenu x= new JUserMenu();
            x.setVisible(true);
            x.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        }}
    private class MoivemenuWatcher implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JMovieEditor x = new JMovieEditor();
            x.setVisible(true);
            x.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        }}
    private class AdminMenuWatcher implements ActionListener{
        
        @Override
        public void actionPerformed(ActionEvent e) {
            JAdminEditor x = new JAdminEditor();
            x.setVisible(true);
            x.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        }}
    private class BuytickeWatcher implements ActionListener{
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            JTicketPurchase x = new JTicketPurchase();
            x.setVisible(true);
            x.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        }}
    private class DashbourdWatcher implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JMovieDashboardFrame x = new JMovieDashboardFrame();
            x.setVisible(true);
            x.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        }}
    
        
    }
    