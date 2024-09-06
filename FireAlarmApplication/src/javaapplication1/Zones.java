/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author sheri
 */
public class Zones extends Thread {

    private String zoneName_s;
    private Color zoneColor_c;
    private int zoneTemp_i;
    private int setPoint_i = 70;
    private boolean zoneFireState_b = false;
    private String zoneSensorPath_s;
    private String zoneAlarmPath_s;
    BufferedReader readTemp_buff;
    private final File file_f;
    private boolean sensorCond_b = true;
    private MainWindowUI mainUI;
    
    private int ID;

    public Zones(String filePath_s, String zoneName_s, int s, MainWindowUI m, Color col, int ID) throws FileNotFoundException {
        this.zoneName_s = zoneName_s;
        file_f = new File(filePath_s);
        setPoint_i = s;
        this.mainUI = m;
        readTemp_buff = new BufferedReader(new FileReader(file_f));
        this.ID = ID;
        zoneColor_c = col;
    }

    @Override
    public void run() {
        while (true) {
            try {
                readTemp();
                System.out.println("SetPoint for zones: " + setPoint_i);
                System.out.println("Zone Temp: " + zoneTemp_i);
                if (zoneTemp_i > setPoint_i) {
                    zoneFireState_b = true;
                    updateColor(Color.WHITE);
                    sleep(100);
                    updateColor(zoneColor_c);
                    sleep(100);
                }
            } catch (IOException ex) {
                Logger.getLogger(Zones.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Zones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void readTemp() throws IOException {
        String read = readTemp_buff.readLine();

        if (read != null && !read.equals("N")) {
            zoneTemp_i = Integer.parseInt(read);
        } else {
            sensorCond_b = false;
        }
    }

    public void updateColor(Color c) {

        SwingUtilities.invokeLater(()-> {
            switch (ID) {
                case 1:
                    mainUI.getZoneOne().setBackground(c);
                    break;
                case 2:
                    mainUI.getZoneTwo().setBackground(c);
                    break;
                case 3:
                    mainUI.getZoneThree().setBackground(c);
                    break;
                case 4:
                    mainUI.getZoneFour().setBackground(c);
                    break;
                default:
                    // Do nothing;
            }
        });
    }
    
    public void setSetPoint(int s){
        setPoint_i = s;
    }
    
    public int getSetPoint(){
        return setPoint_i;
    }
}

