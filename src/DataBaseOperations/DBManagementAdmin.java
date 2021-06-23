/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBaseOperations;
import com.mysql.cj.protocol.Resultset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import signup.DbHelper;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import java.sql.*;
import java.util.Date;
/**
 *
 * @author RECEP
 */
public class DBManagementAdmin {

    DbHelper dbHelper = new DbHelper();
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    Connection connection = null;
    ResultSet resultSet;
    JTable table = new JTable();
    JTable tableTemplates = new JTable();
    DefaultTableModel tblModel;
    DefaultTableModel tblModelTemplates;
    DBManagementUser dbManagementUser = new DBManagementUser();
    
    public DBManagementAdmin()
    {
        
    }
    //CONNECTION
    //Veri Tabanı Bağlantı Açma
    private void openConnection()
    {
        try{
          //  if(connection.isClosed())
            //{
                connection = dbHelper.getConnection();
                statement = connection.createStatement();
          //  }
        }
        catch(SQLException ex){
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), ex.getErrorCode(),ex.getMessage(),now.toString());
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
            dbManagementUser.callStoredError(ex.getClass().getName(), ex.getErrorCode(),ex.getMessage(),now.toString());
            dbHelper.showErrorMessage(ex);
        }
    }
    
    //INSERT, UPDATE, DELETE, DISPLAY
    
    //KULLANICI
    //Kullanıcı Ekleme 
    public int addUser(String username, String password, int isAdmin)
    {
        openConnection();
        try{
            statement.executeUpdate("INSERT INTO users (username,password,isAdmin) VALUES('"
                    + username +  "','"
                            + password + "',"
                                    + isAdmin + ")");
            return 1;
        }
        catch(SQLException ex){
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), ex.getErrorCode(),ex.getMessage(),now.toString());
            System.out.println("Kayıt Ekleme Başarısız!");
            dbHelper.showErrorMessage(ex);
            return 0;
        }
        finally
        {
            closeConnection();
        }
    }
    //Kullanıcı Güncelleme
    public int updateUser(String username, String password, String newUsername)
    {
        openConnection();
        try{
            statement.executeUpdate("UPDATE users SET username='" + newUsername +"'," + "password='" + password + "' WHERE username='" + username +"'");
            return 1;
        }
        catch(SQLException ex){
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), ex.getErrorCode(),ex.getMessage(),now.toString());
            System.out.println("Kayıt Güncelleme Başarısız!");
            dbHelper.showErrorMessage(ex);
            return 0;
        }
        finally
        {
            closeConnection();
        }
    }
    //Kullanıcı Silme
    public int deleteUser(String username)
    {
        openConnection();
        try{
            statement.executeUpdate("DELETE FROM users WHERE username='" + username + "'");
             return 1;
        }
        catch(SQLException ex)
        {
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), ex.getErrorCode(),ex.getMessage(),now.toString());
            System.out.println("Kayıt Silme Başarısız!");
            dbHelper.showErrorMessage(ex);
            return 0;
        }
        finally
        {
            closeConnection();
        }
        
    }
    //Kullanıcı Listeleme
    public void displayUsers(JTable jTable, String search)
    {
        
        jTable.setModel(new DefaultTableModel(null,new String[]{"Kullanıcı Adı","Parola"}));
        jTable.getColumnModel().getColumn(1).setMinWidth(0);
        jTable.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable.getColumnModel().getColumn(1).setPreferredWidth(0);
        table = jTable;
        openConnection();
        try{

            resultSet = statement.executeQuery("SELECT * FROM users WHERE username LIKE \"%"
                    + search + "%\" AND isAdmin != 1");
            while(resultSet.next()){
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                String data [] = {username,password};
                tblModel = (DefaultTableModel)jTable.getModel();
                tblModel.addRow(data);
            }
            
        }
        catch(SQLException ex){
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), ex.getErrorCode(),ex.getMessage(),now.toString());
            dbHelper.showErrorMessage(ex);
        }
        finally
        {
            closeConnection();
        }
    }
    
    //TEMPLATE
    //Template Ekleme
    public int addTemplate(String templateName, String templatePath, JLabel label)
    {
        openConnection();
        try{
            preparedStatement = connection.prepareStatement("INSERT INTO templates (templateName,templatePath) VALUES('"
                    + templateName +  "','"
                            + templatePath + "')");
            preparedStatement.executeUpdate();
            //Labela fotoğraf ekleme
            ImageIcon imgThisImg = new ImageIcon(templatePath);
            label.setIcon(imgThisImg);
            return 1;
        }
        catch(SQLException ex){
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), ex.getErrorCode(),ex.getMessage(),now.toString());
            System.out.println("Template Ekleme Başarısız!");
            dbHelper.showErrorMessage(ex);
            return 0;
        }
        catch(Exception exception)
        {
            System.out.println("Exception Hatası: " + exception.getMessage());
            System.out.println("Exception Code: " +  exception.getCause());
            Date now = new Date();
            dbManagementUser.callStoredError(exception.getClass().getName(), -1,exception.getMessage(),now.toString());
            return 0;
        }
        finally
        {
            closeConnection();
        }
    }
    //Template Güncelleme
    public int updateTemplate(String templateName, String templatePath, JLabel label, int ID)
    {
        openConnection();
        try{
            
            statement.executeUpdate("UPDATE templates SET templateName='" + templateName +"'," + "templatePath='" + templatePath + "' WHERE templateID=" + ID +"");
            return 1;
        }
        catch(SQLException ex){
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), ex.getErrorCode(),ex.getMessage(),now.toString());
            System.out.println("Template Güncelleme Başarısız!");
            dbHelper.showErrorMessage(ex);
            return 0;
        }
        catch(Exception exception)
        {
            System.out.println("Exception Hatası: "+exception.getMessage());
            System.out.println("Exception Code: "+ exception.getCause());
            Date now = new Date();
            dbManagementUser.callStoredError(exception.getClass().getName(), -1,exception.getMessage(),now.toString());
            return 0;
        }
        finally
        {
            closeConnection();
        }
    }
    //Template Silme
    public int deleteTemplate(int ID)
    {
        openConnection();
        try{
            statement.executeUpdate("DELETE FROM templates WHERE templateID=" + ID);
            return 1;
        }
        catch(SQLException ex)
        {
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), ex.getErrorCode(),ex.getMessage(),now.toString());
            System.out.println("Kayıt Silme Başarısız!");
            dbHelper.showErrorMessage(ex);
            return 0;
        }
        finally
        {
            closeConnection();
        }
        
    }
    //Template Listeleme
    public void displayTemplates(JTable jTable, String search)
    {
        jTable.setModel(new DefaultTableModel(null,new String[]{"Liste Numarası","Template Adı","Uzantısı"}));
        jTable.getColumnModel().getColumn(0).setMinWidth(100);
        jTable.getColumnModel().getColumn(0).setMaxWidth(100);
        jTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTable.getColumnModel().getColumn(1).setMinWidth(200);
        jTable.getColumnModel().getColumn(1).setMaxWidth(200);
        jTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableTemplates = jTable;
        openConnection();
        try{

            resultSet = statement.executeQuery("SELECT * FROM templates WHERE templateName LIKE \"%"
                    + search + "%\"");
            while(resultSet.next()){
                String templateID = resultSet.getString("templateID");
                String templateName = resultSet.getString("templateName");
                String templatePath = resultSet.getString("templatePath");

                String data [] = {templateID, templateName,templatePath};
                
                tblModelTemplates = (DefaultTableModel)jTable.getModel();
                tblModelTemplates.addRow(data);
            }
            
        }
        catch(SQLException ex){
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), ex.getErrorCode(),ex.getMessage(),now.toString());
            dbHelper.showErrorMessage(ex);
        }
        finally
        {
            closeConnection();
        }
    }
    
    
    //TextField Op.
    //TextField Reset
    public void resetFields(ArrayList<JTextField> fields)
    {
        for(JTextField field : fields)
        {
            field.setText("");
        }
    }
    //TextField Fill
    public void fillFieldsUser(JTextField username, JPasswordField password)
    {
        username.setText(tblModel.getValueAt(table.getSelectedRow(), 0).toString());
        password.setText(tblModel.getValueAt(table.getSelectedRow(), 1).toString());
    }
    public void fillFieldsTemplate(JTextField templatePath, JTextField templateName)
    {
        templatePath.setText(tblModelTemplates.getValueAt(tableTemplates.getSelectedRow(), 2).toString());
        templateName.setText(tblModelTemplates.getValueAt(tableTemplates.getSelectedRow(), 1).toString());
    }
}
