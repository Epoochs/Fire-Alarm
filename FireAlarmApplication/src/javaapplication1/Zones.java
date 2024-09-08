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
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.sound.sampled.*;

/**
 *
 * @author sherif
 */
public class Zones extends Thread {

    /* Zones Identity */
    private final String zoneName_s;
    private final Color zoneColor_c;
    private int zoneTemp_i;
    private int setPoint_i = 70;
    private final int ID;

    /* Paths and I\O Processing */
    private String zoneSensorPath_s;
    private String zoneAlarmPath_s;
    static int zoneAlarmSignal_i = 0;
    BufferedReader readTemp_buff;
    private final File file_f;
    private FileWriter fileWrite_f;
    private Clip clip;
    

    /* States */
    private boolean sensorCond_b = true;
    private boolean zoneFireState_b = false;
    private boolean alarmState_b = false;

    /* Reference to the Main Window */
    private final MainWindowUI mainUI;

    public Zones(String filePath_s, String zoneName_s, int s, MainWindowUI m, Color col, int ID) throws FileNotFoundException, IOException {
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
                    writeOneTo();
                    sleep(100);
                    updateColor(zoneColor_c);
                    writeZeroTo();
                    sleep(100);
                } else {
                    zoneFireState_b = false;
                    stopAlarm(clip);
                }
            } catch (IOException | InterruptedException ex) {
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
    private void writeOneTo() throws IOException {
        fileWrite_f = new FileWriter("C:\\Users\\sheri\\OneDrive\\Documents\\NetBeansProjects\\JavaApplication1\\src\\javaapplication1\\Listen.txt");
        fileWrite_f.write(zoneName_s);
        fileWrite_f.close();
    }
    private void writeZeroTo() throws IOException{
        fileWrite_f = new FileWriter("C:\\Users\\sheri\\OneDrive\\Documents\\NetBeansProjects\\JavaApplication1\\src\\javaapplication1\\Listen.txt");
        fileWrite_f.write("0");
        fileWrite_f.close();
    }

    public void updateColor(Color c) {

        SwingUtilities.invokeLater(() -> {
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

    public void setSetPoint(int s) {
        setPoint_i = s;
    }

    public int getSetPoint() {
        return setPoint_i;
    }

    public Clip startAlarm() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (zoneAlarmSignal_i != 0) {
            File soundFile = new File("C:\\Users\\sheri\\OneDrive\\Documents\\NetBeansProjects\\JavaApplication1\\src\\javaapplication1\\alarm.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        }
        return clip;
    }

    public void stopAlarm(Clip c) {
        if (c != null && c.isRunning()) {
            c.stop();
            clip.close();
        }
    }
}
