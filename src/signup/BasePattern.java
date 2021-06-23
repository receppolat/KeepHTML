
package signup;

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


/**
 *
 * @author RECEP
 */

public class BasePattern {
    private StringBuffer stringBuffer;
    private Scanner scanner;
    private Pattern patternStart, patternEnd;
    private Matcher matcherStart, matcherEnd;
    private File file;
    DBManagementUser dbManagementUser = new DBManagementUser();
    
    
    public BasePattern() {
    
    
    }
    public BasePattern(File file) {
       stringBuffer = new StringBuffer();
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException ex) {
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), -1,ex.getMessage(),now.toString());
            Logger.getLogger(BasePattern.class.getName()).log(Level.SEVERE, null, ex);
        }
       this.file = file;
       while(scanner.hasNextLine()){
           stringBuffer.append(scanner.nextLine());
       }
    }
    
    
    public void readFile(String regexStart, String regexFinish, String rePlaced){
        try{
            patternStart = Pattern.compile(regexStart);
            matcherStart = patternStart.matcher(stringBuffer);
            matcherStart.find();
            int last = matcherStart.end();
            patternEnd = Pattern.compile(regexFinish);
            matcherEnd = patternEnd.matcher(stringBuffer);
            matcherEnd.find();
            int first = matcherEnd.start();
            stringBuffer.replace(last+1, first-2, rePlaced);
            writeFile(stringBuffer);
            scanner.close();
        }
        catch(Exception ex){
            System.out.println(ex);
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), -1,ex.getMessage(),now.toString());
        }
    }
    
    public String getData(String regexStart, String regexFinish)
    {
        String result = "";
        try{
            patternStart = Pattern.compile(regexStart);
            matcherStart = patternStart.matcher(stringBuffer);
            matcherStart.find();
            int last = matcherStart.end();
            patternEnd = Pattern.compile(regexFinish);
            matcherEnd = patternEnd.matcher(stringBuffer);
            matcherEnd.find();
            int first = matcherEnd.start();
            result = stringBuffer.substring(last+1, first-2);    
            scanner.close();
        }
        catch(Exception ex){
           Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), -1,ex.getMessage(),now.toString());
            System.out.println(ex);
        }
        finally
        {
            return result;
        }
    }
    
    
    public void writeFile(StringBuffer stringBuffer)
    {
        try {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
                bufferedWriter.write(stringBuffer.toString());
                System.out.println("Dosyaya Yazıldı");
            }
        } catch (IOException ex) {          
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), -1,ex.getMessage(),now.toString());
        }
    }
    
}
