/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firesystem;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lenovo
 */
public class Zone extends Thread {
    private String zoneTitle;
    private String ledPath;
    private String tempPath;
    private volatile int setTemp;
    private int currentTemp;
    private volatile Boolean fireState;
    private FileWriter fileWrite_f = null;
    public Zone(String zoneTitle, String ledPath, String tempPath, int setTemp) {
        this.zoneTitle = zoneTitle;
        this.ledPath = ledPath;
        this.tempPath = tempPath;
        this.setTemp = setTemp;
        this.fireState = false;
        this.start();
    }

    public Boolean getFireState() {
        return fireState;
    }
    
    @Override
    public void run()
    {
       FileInputStream inputStream = null;
          
        
        while(true)
        {
           try {
               inputStream = new FileInputStream(tempPath);
               //outputStream = new FileOutputStream(ledPath);
                //  BufferedWriter writer = new BufferedWriter(new FileWriter(ledPath, false));
               fileWrite_f = new FileWriter(ledPath,false);
        
                byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String stringValue = new String(buffer, "UTF-8");
            if (stringValue.length() != 0)
            {
                if (stringValue != "NO") {
                    currentTemp = Integer.parseInt(stringValue);
                    if(currentTemp > setTemp)
                    {
                        if (fireState == false)
                        {
                            fireState = true ;
                            fileWrite_f.write("1");
                            fileWrite_f.close();
                            //turnonthebuzzer
                             // writer.write('1');
                             // writer.flush(); // Ensure data is written immediately
                            // outputStream.write('1');
                        }
                    }
                    else 
                    {
                     fireState = false;
                     //turnoffthebuzzer
                     fileWrite_f.write("0");
                     fileWrite_f.close();
                    }
                } else {
                    //reportLog(zoneTitle+" Sensor is down")
                }
            }
           } catch (FileNotFoundException ex) {
               Logger.getLogger(Zone.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException ex) {
               Logger.getLogger(Zone.class.getName()).log(Level.SEVERE, null, ex);
           } finally{
               if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Failed to close the file: " + e.getMessage());
                }
            if (fileWrite_f != null) {
                try {
                    fileWrite_f.close();
                } catch (IOException e) {
                    System.out.println("Failed to close the file: " + e.getMessage());
                }
           }
        }
        
        
    }
    
}
}
    }
