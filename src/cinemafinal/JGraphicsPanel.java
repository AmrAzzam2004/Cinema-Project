package cinemafinal;
import java.awt.*;
import javax.swing.JPanel;

public class JGraphicsPanel extends JPanel {
   
   private Image image;
   private int imageWidth; 
   private int imageHeight; 
   public JGraphicsPanel(int imageWidth, int imageHeight) {
       
       this.imageWidth = imageWidth;
       this.imageHeight = imageHeight;
   }

   public void setImage(String imagePath) {
       image = Toolkit.getDefaultToolkit().getImage(imagePath);
       // Repaint the panel to reflect the new image
       repaint();
   }

   @Override
   protected void paintComponent(Graphics g) {
       super.paintComponent(g);
       if (image != null) {
           g.drawImage(image, 0,0, imageWidth, imageHeight, this);
       }
   } 
}
