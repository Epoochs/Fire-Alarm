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
    private FileWriter buzzerFile_f;
    private volatile Boolean sensorState = true;
    private String BuzzerFilePath_s;
    private volatile Boolean error = false;
    private static volatile Boolean canDown = false;
    private static volatile Boolean canError = false;

    public Zone(String zoneTitle, String ledPath, String tempPath, int setTemp, String BuzzerFilePath_s) {
        this.zoneTitle = zoneTitle;
        this.ledPath = ledPath;
        this.tempPath = tempPath;
        this.setTemp = setTemp;
        this.fireState = false;
        this.BuzzerFilePath_s = BuzzerFilePath_s;
        this.start();
    }

    public synchronized static Boolean getCanError() {
        return canError;
    }

    public synchronized static void setCanError(Boolean canError) {
        Zone.canError = canError;
    }

    public synchronized void setError(Boolean error) {
        this.error = error;
    }

    public synchronized Boolean getError() {
        return error;
    }

    public synchronized Boolean getSensorState() {
        return sensorState;
    }

    public synchronized Boolean getFireState() {
        return fireState;
    }

    @Override
    public void run() {
        FileInputStream inputStream = null;

        while (true) {
            try {
                inputStream = new FileInputStream(tempPath);
                //outputStream = new FileOutputStream(ledPath);
                //  BufferedWriter writer = new BufferedWriter(new FileWriter(ledPath, false));

                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                String stringValue = new String(buffer, "UTF-8");
                if (stringValue.length() > 0 && !stringValue.isEmpty()) {
                    if ((!"K".equals(stringValue)) && (!"M".equals(stringValue)) && (Character.isDigit(stringValue.charAt(0)))) {
                        if (canDown == true) {
                            canDown = false;
                        }
                        if (sensorState == false) {
                            sensorState = true;

                        }
                        if ((stringValue.length() > 0 )&& (Character.isDigit(stringValue.charAt(0)))&& (!stringValue.contains("M")) && (!stringValue.contains("K")) && (!stringValue.equals("")) && (!stringValue.equals(" "))) {
                            currentTemp = Integer.parseInt(stringValue);
                        }
                        if (currentTemp > setTemp) {
                            if (fireState == false) {
                                fireState = true;
                                fileWrite_f = new FileWriter(ledPath);
                                fileWrite_f.write("1");
                                fileWrite_f.close();
                                //turnonthebuzzer
                                Buzzer.setBuzzOn(BuzzerFilePath_s);

                            }
                        } else {
                            if (fireState == true) {
                                fireState = false;
                                fileWrite_f = new FileWriter(ledPath);
                                fileWrite_f.write("0");
                                fileWrite_f.close();

                            }
                        }
                    } else {
                        if (stringValue.equals("K")) {
                            sensorState = false;
                            error = true;
                        } else {
                            canDown = true;
                            canError = true;
                        }

                        //reportLog(zoneTitle+" Sensor is down")
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Zone.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Zone.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
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
