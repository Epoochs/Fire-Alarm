/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firesystem;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author sheri
 */
public class Buzzer {
    
    public volatile static boolean buzzerState;
    
    
    public static void setBuzzOn(String buzzPath) throws IOException {
        buzzerState = true;
        FileWriter buzzWrite_fw = new FileWriter(buzzPath);
        buzzWrite_fw.write("1");
        buzzWrite_fw.close();
    }

    public static void setBuzzOff(String buzzPath) throws IOException {
        buzzerState = false;
        FileWriter buzzWrite_fw = new FileWriter(buzzPath);
        buzzWrite_fw.write("0");
        buzzWrite_fw.close();
    }
}
