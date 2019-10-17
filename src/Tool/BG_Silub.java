package Tool;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BG_Silub extends JPanel{
    Image gam;
    public BG_Silub(){
        setOpaque(false);
        gam=new ImageIcon(getClass().getResource("back_silub.png")).getImage();
        repaint();
    }

    public void setGambar(String temp){
        setOpaque(false);
        gam=new ImageIcon(getClass().getResource(temp)).getImage();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gg=(Graphics2D)g.create();
        gg.drawImage(gam, 0,0,getWidth(),getHeight(), null);
        gg.dispose();
    }


}