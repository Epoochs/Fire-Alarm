/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.awt.Toolkit;
import java.awt.Dimension;

public class MainWindowUI {

    /* Main Window */
    private JFrame mainWindow;

    /* Zone Threads */
    private Zones zone1;
    private Zones zone2;
    private Zones zone3;
    private Zones zone4;

    /* Sensor Files' Path */
    private String sensorfile1_s = "C:\\Users\\sheri\\OneDrive\\Documents\\NetBeansProjects\\JavaApplication1\\src\\javaapplication1\\Sensor1.txt";
    private String sensorfile2_s = "C:\\Users\\sheri\\OneDrive\\Documents\\NetBeansProjects\\JavaApplication1\\src\\javaapplication1\\Sensor2.txt";
    private String sensorfile3_s = "C:\\Users\\sheri\\OneDrive\\Documents\\NetBeansProjects\\JavaApplication1\\src\\javaapplication1\\Sensor3.txt";
    private String sensorfile4_s = "C:\\Users\\sheri\\OneDrive\\Documents\\NetBeansProjects\\JavaApplication1\\src\\javaapplication1\\Sensor4.txt";
    private String alarmfile_s = "C:\\Users\\sheri\\OneDrive\\Documents\\NetBeansProjects\\JavaApplication1\\src\\javaapplication1\\alarm.wav";

    /* Setting points of the zone's Threads */
    private int setPointZone1_i;
    private int setPointZone2_i;
    private int setPointZone3_i;
    private int setPointZone4_i;

    /* Setting Button */
    private JButton settingsButton;

    /* Setting Window */
    private SettingsWindowUI settingsWindow_dia;

    /* Panels for the zones */
    private JPanel mainPanel;
    private JPanel zoneOne;
    private JPanel zoneTwo;
    private JPanel zoneThree;
    private JPanel zoneFour;

    /* Labels To indicate the Zones */
    private JLabel zoneOneTemp_l;
    private JLabel zoneTwoTemp_l;
    private JLabel zoneThreeTemp_l;
    private JLabel zoneFourTemp_l;

    /**/
    private int setPoint;

    /* Constractor to Initialize the MainWindow */
    public MainWindowUI() throws FileNotFoundException {
        initialize();
    }

    private void initialize() throws FileNotFoundException {

        /* Frame Window */
        mainWindow = new JFrame();
        mainWindow.setUndecorated(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainWindow.setSize(screenSize.width, screenSize.height);
        mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainWindow.setLayout(new BorderLayout());
        // mainWindow.pack();

        /* Lables */
        zoneOneTemp_l = new JLabel("Zone One", JLabel.CENTER);
        zoneOneTemp_l.setFont(new Font("Sans-serif", Font.BOLD, 20));

        zoneTwoTemp_l = new JLabel("Zone Two", JLabel.CENTER);
        zoneTwoTemp_l.setFont(new Font("Sans-serif", Font.BOLD, 20));

        zoneThreeTemp_l = new JLabel("Zone Three", JLabel.CENTER);
        zoneThreeTemp_l.setFont(new Font("Sans-serif", Font.BOLD, 20));

        zoneFourTemp_l = new JLabel("Zone Four", JLabel.CENTER);
        zoneFourTemp_l.setFont(new Font("Sans-serif", Font.BOLD, 20));

        /* Setting Button */
        settingsButton = new JButton("Settings");
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* Show the Settings Dialog */
                if (settingsWindow_dia == null || !settingsWindow_dia.getFoucs().isShowing()) {
                    settingsWindow_dia = new SettingsWindowUI(MainWindowUI.this);
                    settingsWindow_dia.getFoucs().setVisible(true);
                    
                    zone1.setSetPoint(setPoint);
                    zone2.setSetPoint(setPoint);
                    zone3.setSetPoint(setPoint);
                    zone4.setSetPoint(setPoint);
                } else {
                    settingsWindow_dia.getFoucs().requestFocus(); // Bring the settings window to the front
                }
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.add(settingsButton);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(2, 2));


        /* Zones Section */
        zoneOne = new JPanel();
        zoneOne.setBackground(Color.YELLOW);
        gridPanel.add(zoneOne);
        zoneOne.add(zoneOneTemp_l);

        zoneTwo = new JPanel();
        zoneTwo.setBackground(Color.BLUE);
        gridPanel.add(zoneTwo);
        zoneTwo.add(zoneTwoTemp_l);

        zoneThree = new JPanel();
        zoneThree.setBackground(Color.GREEN);
        gridPanel.add(zoneThree);
        zoneThree.add(zoneThreeTemp_l);

        zoneFour = new JPanel();
        zoneFour.setBackground(Color.RED);
        gridPanel.add(zoneFour);
        zoneFour.add(zoneFourTemp_l);

        gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        mainWindow.add(gridPanel, BorderLayout.CENTER);
        mainWindow.add(controlPanel, BorderLayout.SOUTH);
        mainWindow.setVisible(true);
        
        /* Start the SettingsUI to configure the system at first time */
        settingsWindow_dia = new SettingsWindowUI(this);
        settingsWindow_dia.getFoucs().setVisible(true);
        System.out.println(setPoint);
        
        
        /* Start The Zone's Thread */
        zone1 = new Zones(sensorfile1_s, "zone1", setPoint, MainWindowUI.this, Color.YELLOW,1);
        zone1.start();

        zone2 = new Zones(sensorfile2_s, "zone2", setPoint, MainWindowUI.this, Color.BLUE,2);
        zone2.start();
        
        zone3 = new Zones(sensorfile3_s, "zone3", setPoint, MainWindowUI.this, Color.GREEN,3);
        zone3.start();
        
        zone4 = new Zones(sensorfile4_s, "zone4", setPoint, MainWindowUI.this, Color.RED,4);
        zone4.start();

    }

    public JFrame getMainWindow() {
        return mainWindow;
    }

    public Zones getZone1() {
        return zone1;
    }

    public Zones getZone2() {
        return zone2;
    }

    public Zones getZone3() {
        return zone3;
    }

    public Zones getZone4() {
        return zone4;
    }

    public String getSensorfile1_s() {
        return sensorfile1_s;
    }

    public String getSensorfile2_s() {
        return sensorfile2_s;
    }

    public String getSensorfile3_s() {
        return sensorfile3_s;
    }

    public String getSensorfile4_s() {
        return sensorfile4_s;
    }

    public int getSetPointZone1_i() {
        return setPointZone1_i;
    }

    public int getSetPointZone2_i() {
        return setPointZone2_i;
    }

    public int getSetPointZone3_i() {
        return setPointZone3_i;
    }

    public int getSetPointZone4_i() {
        return setPointZone4_i;
    }

    public JButton getSettingsButton() {
        return settingsButton;
    }

    public SettingsWindowUI getSettingsWindow_dia() {
        return settingsWindow_dia;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JPanel getZoneOne() {
        return MainWindowUI.this.zoneOne;
    }

    public JPanel getZoneTwo() {
        return zoneTwo;
    }

    public JPanel getZoneThree() {
        return zoneThree;
    }

    public JPanel getZoneFour() {
        return zoneFour;
    }

    public JLabel getZoneOneTemp_l() {
        return zoneOneTemp_l;
    }

    public JLabel getZoneTwoTemp_l() {
        return zoneTwoTemp_l;
    }

    public JLabel getZoneThreeTemp_l() {
        return zoneThreeTemp_l;
    }

    public JLabel getZoneFourTemp_l() {
        return zoneFourTemp_l;
    }

    public int getSetPoint() {
        return setPoint;
    }

    public void setMainWindow(JFrame mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setZone1(Zones zone1) {
        this.zone1 = zone1;
    }

    public void setZone2(Zones zone2) {
        this.zone2 = zone2;
    }

    public void setZone3(Zones zone3) {
        this.zone3 = zone3;
    }

    public void setZone4(Zones zone4) {
        this.zone4 = zone4;
    }

    public void setSensorfile1_s(String sensorfile1_s) {
        this.sensorfile1_s = sensorfile1_s;
    }

    public void setSensorfile2_s(String sensorfile2_s) {
        this.sensorfile2_s = sensorfile2_s;
    }

    public void setSensorfile3_s(String sensorfile3_s) {
        this.sensorfile3_s = sensorfile3_s;
    }

    public void setSensorfile4_s(String sensorfile4_s) {
        this.sensorfile4_s = sensorfile4_s;
    }

    public void setSetPointZone1_i(int setPointZone1_i) {
        this.setPointZone1_i = setPointZone1_i;
    }

    public void setSetPointZone2_i(int setPointZone2_i) {
        this.setPointZone2_i = setPointZone2_i;
    }

    public void setSetPointZone3_i(int setPointZone3_i) {
        this.setPointZone3_i = setPointZone3_i;
    }

    public void setSetPointZone4_i(int setPointZone4_i) {
        this.setPointZone4_i = setPointZone4_i;
    }

    public void setSettingsButton(JButton settingsButton) {
        this.settingsButton = settingsButton;
    }

    public void setSettingsWindow_dia(SettingsWindowUI settingsWindow_dia) {
        this.settingsWindow_dia = settingsWindow_dia;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setZoneOne(JPanel zoneOne) {
        this.zoneOne = zoneOne;
    }

    public void setZoneTwo(JPanel zoneTwo) {
        this.zoneTwo = zoneTwo;
    }

    public void setZoneThree(JPanel zoneThree) {
        this.zoneThree = zoneThree;
    }

    public void setZoneFour(JPanel zoneFour) {
        this.zoneFour = zoneFour;
    }

    public void setZoneOneTemp_l(JLabel zoneOneTemp_l) {
        this.zoneOneTemp_l = zoneOneTemp_l;
    }

    public void setZoneTwoTemp_l(JLabel zoneTwoTemp_l) {
        this.zoneTwoTemp_l = zoneTwoTemp_l;
    }

    public void setZoneThreeTemp_l(JLabel zoneThreeTemp_l) {
        this.zoneThreeTemp_l = zoneThreeTemp_l;
    }

    public void setZoneFourTemp_l(JLabel zoneFourTemp_l) {
        this.zoneFourTemp_l = zoneFourTemp_l;
    }

    public void setSetPoint(int setPoint) {
        this.setPoint = setPoint;
    }

}
