/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import DataBaseOperations.DBManagementUser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import signup.BasePattern;

/**
 *
 * @author RECEP
 */
public class DynamicLabelMotor {
    
    private StringBuffer stringBuffer;
    private Scanner scanner;
    private Pattern patternStart, patternEnd;
    private Matcher matcherStart, matcherEnd;
    private File fileGlobal;
    DBManagementUser dbManagementUser = new DBManagementUser();
    
    public DynamicLabelMotor(File file, String regex){
        String rePlacedStart = " keepHTML=\"startAMenu\" ";
        stringBuffer = new StringBuffer();
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException ex) {
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), -1,ex.getMessage(),now.toString());
            Logger.getLogger(BasePattern.class.getName()).log(Level.SEVERE, null, ex);
        }
       fileGlobal = file;
       while(scanner.hasNextLine()){
           stringBuffer.append(scanner.nextLine());
       }
       setAll(stringBuffer, regex, rePlacedStart, false);
      //setTitle(stringBuffer);
    }
    
    //regexStart: a, h1, h2, p..
    //regexFinish: </
    //rePlaced changing field in file
    private void setAll(StringBuffer buffer, String regex, String rePlaced, boolean isStart){
    try{
        int count = 0, index = 0 ,i = 0;
        patternStart = Pattern.compile(regex);
        matcherStart = patternStart.matcher(buffer);
        //matcherStart.find();
        rePlaced = "<!--" + rePlaced +"-->";
        while(matcherStart.find())
        {       
            if (isStart){
                index = matcherStart.end();
                buffer.replace(index-1 ,index , rePlaced);
                }
            if(i % 2 == 0){
                if (!isStart){
                    index = matcherStart.start();
                    System.out.println("" + index);
                    String lastChar = buffer.substring(index-1, index);
                    String reRePlaced = lastChar + rePlaced;
                    buffer.replace(index-1 ,index ,reRePlaced);
                } 
                
            }
            count++;
            i++;
            //System.out.println("" + index);
        }
        writeFile(buffer);
        System.out.println(count + " farklı yerde değişiklik yapıldı.");
        scanner.close();
    }
    catch(Exception ex){
        System.out.println(ex);
        Date now = new Date();
        dbManagementUser.callStoredError(ex.getClass().getName(), -1,ex.getMessage(),now.toString());
    }}
    
    private void setTitle(StringBuffer buffer){
        
    }
    
    public void writeFile(StringBuffer stringBuffer)
    {
        try {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileGlobal))) {
                bufferedWriter.write(stringBuffer.toString());
                System.out.println("Dosyaya Yazıldı");
            }
        } catch (IOException ex) {          
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), -1,ex.getMessage(),now.toString());
        }
    }
}
