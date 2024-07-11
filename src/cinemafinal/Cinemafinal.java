
package cinemafinal;

import javax.swing.JFrame;


public class Cinemafinal {

    
    public static void main(String[] args) {
        new DBDatabase_Connection();
        JMenuForm x =new JMenuForm();
        x.setVisible(true);
        x.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
}
