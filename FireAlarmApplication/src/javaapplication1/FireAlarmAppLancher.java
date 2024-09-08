/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author sheri
 */
public class FireAlarmAppLancher {

    private static MainWindowUI appUI;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                appUI = new MainWindowUI();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FireAlarmAppLancher.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FireAlarmAppLancher.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
