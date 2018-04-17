package it.moldoveanu.andrei.contacts.project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DataSource {


    ClassLoader objClassLoader = null;
    Properties commonProperties = new Properties();
    String propertiesFilename = "configuration.properties";
    
    public DataSource() {
        /* Initialize 'objClassLoader' once so same object used for multiple files. */
        objClassLoader = getClass().getClassLoader();
    }
    
   /**
    * Reads the value of a key in the .properties
    * 
    * @param key
    * @return
    */
    public String readKey(String key){
        /* Simple validation */
        if (propertiesFilename != null && !propertiesFilename.trim().isEmpty()
                && key != null && !key.trim().isEmpty()) {
            /* Create an object of FileInputStream */
            FileInputStream objFileInputStream = null;
            
            /**
             * Following try-catch is used to support upto 1.6.
             * Use try-with-resource in JDK 1.7 or above
             */
            try {
                /* Read file from resources folder */
                objFileInputStream = new FileInputStream(objClassLoader.getResource(propertiesFilename).getFile());
                /* Load file into commonProperties */
                commonProperties.load(objFileInputStream);
                /* Get the value of key */
                return String.valueOf(commonProperties.get(key));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }finally{
                /* Close the resource */
                if(objFileInputStream != null){
                    try {
                        objFileInputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
