/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseOperations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import signup.DbHelper;
import admin.PanelColors;
import com.mysql.cj.jdbc.CallableStatement;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import signup.DynamiclyEvents;
import java.sql.Types;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author RECEP
 */
public class DBManagementUser {
    
    DbHelper dbHelper = new DbHelper();
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    Connection connection = null;
    ResultSet resultSet;
    CallableStatement callStatement = null; 
    private Pattern pattern;
    private Matcher matcher;
    
    public DBManagementUser()
    {
        
    }
    private void openConnection()
    {
        try{
            //if(connection.isClosed())
            //{
                 connection = dbHelper.getConnection();
                statement = connection.createStatement();
           // }
           
        }
        catch(SQLException ex){
            Date now = new Date();
            callStoredError(ex.getClass().getName(), ex.getErrorCode(),ex.getMessage(),now.toString());
            dbHelper.showErrorMessage(ex);
        }
    }
    //Veri Tabanı Bağlantı Kapatma
    private void closeConnection()
    {
        try {
            connection.close();
        } catch (SQLException ex) {
            Date now = new Date();
            callStoredError(ex.getClass().getName(), ex.getErrorCode(),ex.getMessage(),now.toString());
            dbHelper.showErrorMessage(ex);
        }
    }    
    
    private void reSizeImage(JLabel label, String path)
    {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
            Image dimg = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            label.setIcon(imageIcon);
        } catch (IOException ex) {
            Date now = new Date();
            callStoredError(ex.getClass().getName(), -1,ex.getMessage(),now.toString());
            ex.printStackTrace();
        }
      
    }
    
    public int checkUser(String userName,String password) throws SQLException
    {
        int isTrue = 0;
        try{
            connection = dbHelper.getConnection();
            System.out.println("Bağlantı Oluşturuldu.");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM users");
            while(resultSet.next())
            {           
                if(userName.equals(resultSet.getString("username")) && password.equals(resultSet.getString("password")))
                {
                    callStoredLogin(userName);
                    if(resultSet.getInt("isAdmin") == 0)
                    {
                        System.out.println(resultSet.getString("username")+" kullanıcısı giriş yaptı.");
                        isTrue = 1;
                        break;
                    }
                    else
                    {
                        System.out.println(resultSet.getString("username")+" admini giriş yaptı.");
                        isTrue = 2;
                        break;
                    }
                }
                else
                {
                    isTrue = 0;
                }  
            }
        }
        catch(SQLException exception)
        {
            Date now = new Date();
            callStoredError(exception.getClass().getName(), exception.getErrorCode(),exception.getMessage(),now.toString());
            dbHelper.showErrorMessage(exception);
            isTrue = 0;
        }
        finally
        {
            closeConnection();
            return isTrue;
        }
        
    }

    public void displayTemplates(JPanel jPanel, JTextField jTextField, JLabel label, String search)
    {
        openConnection();
        DynamiclyEvents dynamiclyEvents;
        
        try{
            int x = 22, xImage = 20;
            int y = 50, yImage = 10;
            int page = 1, count = 0;
            resultSet = statement.executeQuery("SELECT * FROM templates WHERE templateName LIKE \"%"
                    + search + "%\"");
            jPanel.removeAll();
            jPanel.validate();
            while(resultSet.next()){
                
                JPanel panel = new JPanel();
                panel.setName(resultSet.getInt("templateID") + "Panel" + page);
                panel.setLayout(null);
                panel.setSize(340, 270);
                panel.setLocation(x, y);
                panel.setBackground(new Color(240,240,240));
                panel.setVisible(true);


                JLabel image = new JLabel();
                image.setName(resultSet.getInt("templateID") + "LabelImage" + page);
                image.setText("");
                image.setLocation(xImage, yImage);
                image.setSize(300, 220);
                image.setHorizontalAlignment(JLabel.CENTER);
                reSizeImage(image, resultSet.getString("templatePath"));
                panel.add(image);

                JLabel templateName = new JLabel();
                templateName.setName(resultSet.getInt("templateID") + "LabelName" + page);
                templateName.setText(resultSet.getString("templateName") + " Template");
                templateName.setLocation(0, 230);
                templateName.setSize(340, 40);
                templateName.setHorizontalAlignment(JLabel.CENTER);
                templateName.setForeground(new Color(122,22,221));
                panel.add(templateName);
                if( x != 1102)
                {
                    x += 360;
                }
                else
                {
                    x = 22;
                    y += 290;
                    if(y == 630)
                    {
                        page++;
                        x = 22;
                        y = 50;
                    }
                }
                dynamiclyEvents = new DynamiclyEvents();
                panel.addMouseListener(dynamiclyEvents);
                dynamiclyEvents.setNames(panel, templateName, resultSet.getString("templateName"));
                String newName = "";
                pattern = Pattern.compile("Panel");
                matcher = pattern.matcher(panel.getName());
                matcher.find();//33Panel22
                int indexLast = matcher.end();
                newName = panel.getName().substring(indexLast);

                if(newName.equals(jTextField.getText())){
                    //System.out.println(newName +"="+jTextField.getText());
                    jPanel.add(panel, BorderLayout.CENTER);
                    jPanel.validate();
                }
                else{
                   panel.removeAll();
                }
                count++;
            }    
            count /= 8;
            label.setText(String.valueOf(++count));
            
        }
        catch(SQLException ex){
            dbHelper.showErrorMessage(ex);
            Date now = new Date();
            callStoredError(ex.getSQLState(), ex.getErrorCode(),ex.getMessage(),now.toString());
        }
        finally
        {
            closeConnection();
        }
    }
    public void callStoredError(String name, int id,String message, String time)
    {
        try {
            openConnection();
            CallableStatement callable = (CallableStatement) connection.prepareCall("{call StoredError(?, ? ,?, ?, ?)}");
            callable.setString(1, name);
            callable.setInt(2,id);
            callable.setString(3, message);
            callable.setString(4, time);
            callable.setString(5, signup.SignUp.userID);               
            callable.executeUpdate();
        } catch (SQLException ex) {
            dbHelper.showErrorMessage(ex);
        }
    }
    
    public void callStoredLogin(String userName)
    {
        try {
            openConnection();
            Date now = new Date();
            CallableStatement callable = (CallableStatement) connection.prepareCall("{call StoredLogin(?, ?)}");
            callable.setString(1, userName);       
            callable.setString(2, now.toString());
            callable.executeUpdate();
        } catch (SQLException ex) {
            Date now = new Date();
            callStoredError(ex.getSQLState(), ex.getErrorCode(),ex.getMessage(),now.toString());
            dbHelper.showErrorMessage(ex);
        }
    }
}
