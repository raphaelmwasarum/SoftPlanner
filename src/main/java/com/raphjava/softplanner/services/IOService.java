package com.raphjava.softplanner.services;//package net.raphjava.studeeconsole.services;
//
////import net.raphjava.studeeconsole.interfaces.Log;
//import net.raphjava.raphtility.logging.interfaces.Log;
//import net.raphjava.raphtility.logging.interfaces.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Component;
//
//import javax.imageio.ImageIO;
//import java.io.*;
//
//@Lazy
//@Component
//public class IOService
//{
//
//    public final String TEXT = "HTML";
//    public final String IMAGE = "IMAGE";
//
//    private final Log logger;
//    private final String loggerName;
//    private final String appPath;
//
//    private String textResourceLocation;
//
//    private String imagesResourceLocation;
//
//    private String stylesResourceLocation;
//
//    public String getStylesResourceLocation()
//    {
//        return stylesResourceLocation;
//    }
//
//    public String getTextResourceLocation()
//    {
//        return textResourceLocation;
//    }
//
//    public void setTextResourceLocation(String textResourceLocation)
//    {
//        this.textResourceLocation = textResourceLocation;
//    }
//
//    public String getImagesResourceLocation()
//    {
//        return imagesResourceLocation;
//    }
//
//    public void setImagesResourceLocation(String imagesResourceLocation)
//    {
//        this.imagesResourceLocation = imagesResourceLocation;
//    }
//
//    @Autowired
//    public IOService(LoggerFactory loggerFactory)
//    {
//        logger = loggerFactory.createLogger(getClass().getSimpleName());
//        loggerName = "[" + getClass().getSimpleName() + "]";
//        appPath = System.getProperty("user.dir");
//        buildImagesResourcePath();
//        buildTextResourcePath();
//        buildStylesResourcePath();
//        initialize();
//    }
//
//
//    private void buildTextResourcePath()
//    {
//        var separator = getCurrentOSFileSeparator();
//        textResourceLocation = appPath + separator + "user_data" + separator + "text";
//    }
//
//    private void buildImagesResourcePath()
//    {
//        var separator = getCurrentOSFileSeparator();
//        imagesResourceLocation = appPath + separator + "user_data" + separator + "images";
//    }
//
//    private void buildStylesResourcePath()
//    {
//        var separator = getCurrentOSFileSeparator();
//        stylesResourceLocation = appPath + separator + "user_data" + separator + "styles";
//    }
//
//    private void debug(String msg)
//    {
//        logger.debug(loggerName + " - " + msg);
//    }
//
//
//    private void initialize()
//    {
//        setupImageResourceLocation();
//        setupTextResourceLocation();
//        setupStylesResourceLocation();
//    }
//
//    private void setupStylesResourceLocation()
//    {
//        var stylesD = new File(stylesResourceLocation);
//        var userStylesDirectory = "User styles directory";
//        if(!stylesD.exists())
//        {
//            if(stylesD.mkdirs()) debug(userStylesDirectory + " successfully created.");
//            else
//            {
//                var m = "Failed to create " + userStylesDirectory;
//                debug(m);
//                throw new RuntimeException(m);
//            }
//        }
//        else debug(userStylesDirectory + " exists.");
//    }
//
//    private void setupTextResourceLocation()
//    {
//        var modelsD = new File(textResourceLocation);
//        var userTextDirectory = "User text directory";
//        if(!modelsD.exists())
//        {
//            if(modelsD.mkdirs()) debug(userTextDirectory + " successfully created.");
//            else
//            {
//                var m = "Failed to create " + userTextDirectory;
//                debug(m);
//                throw new RuntimeException(m);
//            }
//        }
//        else debug(userTextDirectory + " exists.");
//    }
//
//    private void setupImageResourceLocation()
//    {
//        var imageD = new File(imagesResourceLocation);
//        var userImageDirectory = "User image directory";
//        if(!imageD.exists())
//        {
//            if(imageD.mkdirs()) debug(userImageDirectory + " successfully created.");
//            else
//            {
//                var m = "Failed to create " + userImageDirectory;
//                debug(m);
//                throw new RuntimeException(m);
//            }
//        }
//        else debug(userImageDirectory + " exists.");
//    }
//
//    public boolean overWriteFile(String file, String data)
//    {
//        var f = new File(file);
//        deleteFile(f);
//        try(var wr = new BufferedWriter(new FileWriter(f)))
//        {
//            wr.write(data);
//            return true;
//        }
//        catch (IOException | NullPointerException e)
//        {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public void deleteFile(File file)
//    {
//        var msg ="File " + file.getName() + " deletion ";
//
//        if(file.delete())
//        {
//            msg += " successful.";
//        }
//        else
//        {
//            msg += " failed.";
//        }
//        System.out.println(msg);
//
//
//    }
//
//
//
//
//    public String getTextURL(String textFileName)
//    {
//        return textResourceLocation + getCurrentOSFileSeparator() + textFileName;
//    }
//
//    public String getImageURL(String imageFileName)
//    {
//        return imagesResourceLocation + getCurrentOSFileSeparator() + imageFileName;
//    }
//
//    private String fileSeparator;
//
//    private String getCurrentOSFileSeparator()
//    {
//        return fileSeparator != null ? fileSeparator : loadAppropriateFileSeparator();
//    }
//
//    private String loadAppropriateFileSeparator()
//    {
//        if(System.getProperty("os.name").toLowerCase().contains("windows"))
//        {
//            fileSeparator = "\\";
//        }
//        else fileSeparator = "/";
//        return fileSeparator;
//    }
//
//    public String readFile(String file)
//    {
//        try(var br = new BufferedReader(new FileReader(new File(file))))
//        {
//            var currentLine = "";
//            var content = new StringBuilder();
//            while (currentLine != null)
//            {
//                currentLine = br.readLine();
//                if(currentLine != null) content.append(currentLine).append("\n");
//            }
//
//            return content.toString();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public String getStylesUrl(String styles)
//    {
//        return stylesResourceLocation + getCurrentOSFileSeparator() + styles;
//    }
//}
