package signup;

import admin.PanelColors;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class DynamiclyEvents implements MouseListener {

	JPanel panelGlobal = null;
        JLabel labelGlobal = null;
        String panelNameGlobal = "";
        public static String webSiteID = "";
        public DynamiclyEvents()
        {
            
        }
        
        public void setNames(JPanel panel, JLabel label, String panelName){
            panelGlobal = panel;
            labelGlobal = label;
            panelNameGlobal = panelName;
        }
        public void setColor(JLabel label, Color color)
        {
            label.setForeground(color);
        }
        public void callClick()
        {
            
        }
        @Override
        public void mouseClicked(MouseEvent arg0) {
           System.out.println("Clicked");
           //System.out.println(arg0.getID() + " " + arg0.getLocationOnScreen());
           webSiteID = panelNameGlobal;
           signup.webSite site = new webSite();
           site.show();
        }
        public void mousePerformed(MouseEvent arg0) {
           System.out.println("Performed");
        }
        @Override
        public void mousePressed(MouseEvent arg0) {
            System.out.println("Pressed");
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
            System.out.println("Released");
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
          //  System.out.println("Entred" + arg0.getID());
           setColor(labelGlobal,new Color(240,240,240));
            panelGlobal.setBackground(new Color(122,22,221));
            
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
             panelGlobal.setBackground(new Color(240,240,240));
             setColor(labelGlobal,new Color(122,22,221));
            // System.out.println("Exited" + arg0.getID());
        }
		

}
