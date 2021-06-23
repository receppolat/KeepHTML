/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import DataBaseOperations.DBManagementUser;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
/**
 *
 * @author RECEP
 */
public class FolderOperations {
    String path ="";
    DBManagementUser dbManagementUser = new DBManagementUser();
    
    public FolderOperations()
    {
        path = Paths.get("").toAbsolutePath().toString();
    }
    //Klasör Oluşturma
    public void createFolder(String name, String shortPath)
    {
        try{
        // Proje uzantsını pathde alıp users klasörüne username adında klasör oluşturuyoruz.
        System.out.println(path);
        File f = new File(path + shortPath + name);
        if (f.mkdir()) {
            System.out.println("Directory is created");   
        }
        else {
            System.out.println("Directory cannot be created");
        }
        }
        catch(Exception ex)
        {
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), -1,ex.getMessage(),now.toString());
            System.out.println("Exception Hatası: "+ex.getMessage());
            System.out.println("Exception Cause: "+ ex.getCause());
        }
        
    }
    public void renameFolder(String name, String oldName, String shortPath)
    {
        try{
            File dir = new File(path + shortPath + oldName);
            System.out.println(path + shortPath + oldName);
        if (!dir.isDirectory()) {
          System.err.println("There is no directory @ given path");
        } else {
            File newDir = new File(dir.getParent()+ "\\" + name);
            System.out.println(dir.getParent()+"\\"+name);
            dir.renameTo(newDir);
            System.out.println("Directory renamed!");
        }
        }
        catch(Exception ex)
        {   
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), -1,ex.getMessage(),now.toString());
            System.out.println("Exception Hatası: "+ex.getMessage());
            System.out.println("Exception Cause: "+ ex.getCause());
        }
        
    }
    public void deleteFolder(String filePath)
    {
        try{
            Path path = Paths.get(filePath);

            Files.walkFileTree(path,
            new SimpleFileVisitor<>() {

                // delete directories or folders
                @Override
                public FileVisitResult postVisitDirectory(Path dir,IOException exc) throws IOException {
                    Files.delete(dir);
                    System.out.printf("Directory is deleted : %s%n", dir);
                    return FileVisitResult.CONTINUE;
                }
                // delete files
                @Override
                public FileVisitResult visitFile(Path file,
                                                 BasicFileAttributes attrs)
                                                 throws IOException {
                    Files.delete(file);
                    System.out.printf("File is deleted : %s%n", file);
                    return FileVisitResult.CONTINUE;
                }
            }
        );
        }
        catch(Exception ex)
        {
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), -1,ex.getMessage(),now.toString());
            System.out.println("Exception Hatası: "+ex.getMessage());
            System.out.println("Exception Cause: "+ ex.getCause());
        }
        
    }
    
    public void copydir(File src, File dest) throws IOException
    {

        if (src.isDirectory())
        {

            // if directory not exists, create it
            if (!dest.exists())
            {
                dest.mkdir();
                System.out.println("Directory copied from " + src + "  to "
                        + dest);
            }

            // list all the directory contents
            String files[] = src.list();

            for (String fileName : files)
            {
                // construct the src and dest file structure
                File srcFile = new File(src, fileName);
                File destFile = new File(dest, fileName);
                // recursive copy
                copydir(srcFile, destFile);
            }

        }
        else
        {
            // If file, then copy it
            fileCopy(src, dest);
        }
    }

    private void fileCopy(File src, File dest) throws FileNotFoundException, IOException
    {

        InputStream in = null;
        OutputStream out = null;

        try
        {
            // If file, then copy it
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            // Copy the file content in bytes
            while ((length = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, length);
            }

        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
            if (out != null)
            {
                out.close();
            }
        }
        System.out.println("File copied from " + src + " to " + dest);
    }
   public void copyFrom(String pathToPaste, String pathToCopy) throws IOException
   {
       try{
            File f = new File(pathToPaste);
            File srcDir = new File(pathToCopy);
            File destDir = new File(f.getAbsolutePath());
            System.out.println(f.getAbsolutePath());
            if (!srcDir.exists())
            {
                System.out.println("Directory does not exist.");
            }
        else
        {
            FolderOperations fileDemo = new FolderOperations();
            fileDemo.copydir(srcDir, destDir);//srcdir -> destDir
            System.out.println("Copied successfully.");
        }
       }
       catch(Exception ex)
       {
            Date now = new Date();
            dbManagementUser.callStoredError(ex.getClass().getName(), -1,ex.getMessage(),now.toString());
            System.out.println("Exception Hatası: "+ex.getMessage());
            System.out.println("Exception Cause: "+ ex.getCause());
       }
       
   }
}


