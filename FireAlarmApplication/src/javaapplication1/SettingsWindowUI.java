package javaapplication1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Toolkit;
import java.awt.Dimension;

public class SettingsWindowUI {

    /* Settings Window UI */
    private JDialog settings_dia;
    private JTextField textField1_t, textField2_t, textField3_t, textField4_t;
    private JButton oneButton_b, twoButton_b, threeButton_b, fourButton_b, fiveButton_b, sixButton_b, sevenButton_b, eightButton_b, nineButton_b;
    private JButton zeroButton_b, clearButton_b, clearButton2_b, insertButton_b, backButton_b;
    private JLabel labelStatus_l;
    private JTextField activeField_b;

    /* Main Window UI */
    private JFrame mainFrame;
    private MainWindowUI mainUI;

    /* Status to check the Corner cases */
    private boolean insertionStatus1_b = false;
    private boolean insertionStatus2_b = false;
    private boolean insertionStatus3_b = false;
    private boolean insertionStatus4_b = false;
    private int setPoint1_i;
    private int setPoint2_i;
    private int setPoint3_i;
    private int setPoint4_i;

    public SettingsWindowUI(MainWindowUI mU) {

        this.mainFrame = mU.getMainWindow();

        if (settings_dia == null || !settings_dia.isShowing()) {

            settings_dia = new JDialog(this.mainFrame, "Settings", true);
            settings_dia.getContentPane().setBackground(new Color(224, 224, 224));
          //  settings_dia.setUndecorated(true);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            System.out.println(screenSize.width);
            System.out.println(screenSize.height);
            settings_dia.setSize(screenSize.width / 2, screenSize.height / 2);
            settings_dia.setLocationRelativeTo(this.mainFrame);
            settings_dia.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            settings_dia.setLayout(null);

            
            // Back Button
            backButton_b = new JButton("OK");
            backButton_b.setBounds(14, 450, 105, 31);
            backButton_b.addActionListener(e -> {
                if (insertionStatus1_b && insertionStatus2_b && insertionStatus3_b && insertionStatus4_b) {
                    // Back to mainWindowUI
                    settings_dia.dispose();
                } else {
                    System.out.println("Some \"set Points\" are missed");
                }
            });
            settings_dia.add(backButton_b, BorderLayout.SOUTH);
            
            /******************************************************** Image Icon *************************************************************/
            ImageIcon imageIcon = new ImageIcon("C:\\Users\\sheri\\OneDrive\\Documents\\NetBeansProjects\\JavaApplication1\\src\\javaapplication1\\download-removebg-preview.png");           
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setBounds(560, -80, 400, 600);           
            settings_dia.add(imageLabel);

            
            /* TextFields */
            textField1_t = new JTextField();
            textField1_t.setBounds(83, 150, 100, 25);
            textField1_t.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (activeField_b != textField1_t) {
                        System.out.println("Text Field One");
                        activeField_b = textField1_t;
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                }
            });

            settings_dia.add(textField1_t);

            textField2_t = new JTextField();
            textField2_t.setBounds(83, 180, 100, 25);
            textField2_t.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (activeField_b != textField2_t) {
                        System.out.println("Text Field Two");
                        activeField_b = textField2_t;
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                }
            });

            settings_dia.add(textField2_t);

            textField3_t = new JTextField();
            textField3_t.setBounds(83, 210, 100, 25);
            textField3_t.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (activeField_b != textField3_t) {
                        System.out.println("Text Field Three");
                        activeField_b = textField3_t;
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                }
            });

            settings_dia.add(textField3_t);

            textField4_t = new JTextField();
            textField4_t.setBounds(83, 240, 100, 25);
            textField4_t.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (activeField_b != textField4_t) {
                        System.out.println("Text Field Four");
                        activeField_b = textField4_t;
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                }
            });

            settings_dia.add(textField4_t);

            // Label for "Set Points"
            JLabel setPoint1_l = new JLabel("Set Point 1:");
            setPoint1_l.setBounds(14, 150, 100, 25);
            settings_dia.add(setPoint1_l);

            JLabel setPoint2_l = new JLabel("Set Point 2:");
            setPoint2_l.setBounds(14, 180, 100, 25);
            settings_dia.add(setPoint2_l);

            JLabel setPoint3_l = new JLabel("Set Point 3:");
            setPoint3_l.setBounds(14, 210, 100, 25);
            settings_dia.add(setPoint3_l);

            JLabel setPoint4_l = new JLabel("Set Point 4:");
            setPoint4_l.setBounds(14, 240, 100, 25);
            settings_dia.add(setPoint4_l);

            // GridPane equivalent using GridLayout for buttons (3x3 grid)
            JPanel buttonPanel = new JPanel(new GridLayout(3, 3));
            buttonPanel.setBounds(300, 150, 187, 152);

            oneButton_b = new JButton("1");
            oneButton_b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            twoButton_b = new JButton("2");
            twoButton_b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            threeButton_b = new JButton("3");
            threeButton_b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            fourButton_b = new JButton("4");
            fourButton_b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            fiveButton_b = new JButton("5");
            fiveButton_b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            sixButton_b = new JButton("6");
            sixButton_b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            sevenButton_b = new JButton("7");
            sevenButton_b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            eightButton_b = new JButton("8");
            eightButton_b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            nineButton_b = new JButton("9");
            nineButton_b.setCursor(new Cursor(Cursor.HAND_CURSOR));

            buttonPanel.add(oneButton_b);
            buttonPanel.add(twoButton_b);
            buttonPanel.add(threeButton_b);
            buttonPanel.add(fourButton_b);
            buttonPanel.add(fiveButton_b);
            buttonPanel.add(sixButton_b);
            buttonPanel.add(sevenButton_b);
            buttonPanel.add(eightButton_b);
            buttonPanel.add(nineButton_b);

            oneButton_b.addActionListener(e -> {
                if (activeField_b != null) {
                    activeField_b.setText(activeField_b.getText() + "1");
                } else {
                    System.out.println("Choose a field");
                }
            });
            twoButton_b.addActionListener(e -> {
                if (activeField_b != null) {
                    activeField_b.setText(activeField_b.getText() + "2");
                } else {
                    System.out.println("Choose a field");
                }
            });
            threeButton_b.addActionListener(e -> {
                if (activeField_b != null) {
                    activeField_b.setText(activeField_b.getText() + "3");
                } else {
                    System.out.println("Choose a field");
                }
            });
            fourButton_b.addActionListener(e -> {
                if (activeField_b != null) {
                    activeField_b.setText(activeField_b.getText() + "4");
                } else {
                    System.out.println("Choose a field");
                }
            });
            fiveButton_b.addActionListener(e -> {
                if (activeField_b != null) {
                    activeField_b.setText(activeField_b.getText() + "5");
                } else {
                    System.out.println("Choose a field");
                }
            });
            sixButton_b.addActionListener(e -> {
                if (activeField_b != null) {
                    activeField_b.setText(activeField_b.getText() + "6");
                } else {
                    System.out.println("Choose a field");
                }
            });
            sevenButton_b.addActionListener(e -> {
                if (activeField_b != null) {
                    activeField_b.setText(activeField_b.getText() + "7");
                } else {
                    System.out.println("Choose a field");
                }
            });
            eightButton_b.addActionListener(e -> {
                if (activeField_b != null) {
                    activeField_b.setText(activeField_b.getText() + "8");
                } else {
                    System.out.println("Choose a field");
                }
            });
            nineButton_b.addActionListener(e -> {
                if (activeField_b != null) {
                    activeField_b.setText(activeField_b.getText() + "9");
                } else {
                    System.out.println("Choose a field");
                }
            });
            settings_dia.add(buttonPanel);

            /**
             * **************************************************************
             * Enter Button ***********************************************************************
             */
            // Enter Button
            insertButton_b = new JButton("Enter");
            insertButton_b.setBounds(487, 150, 65, 152);
            insertButton_b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            insertButton_b.addActionListener(e -> {
                if (textField1_t.getText().trim().isEmpty()) {
                    System.out.println("Text Field 1 is empty!");
                } else {
                    setPoint1_i = Integer.parseInt(textField1_t.getText());
                    mU.setSetPointZone1_i(setPoint1_i);
                    System.out.println("Set point is inserted successfully" + setPoint1_i);
                    insertionStatus1_b = true;
                }

                if (textField2_t.getText().trim().isEmpty()) {
                    System.out.println("Text Field 2 is empty!");
                } else {
                    setPoint2_i = Integer.parseInt(textField2_t.getText());
                    mU.setSetPointZone2_i(setPoint2_i);
                    System.out.println("Set point is inserted successfully" + setPoint2_i);
                    insertionStatus2_b = true;
                }

                if (textField3_t.getText().trim().isEmpty()) {
                    System.out.println("Text Field 3 is empty!");
                } else {
                    setPoint3_i = Integer.parseInt(textField3_t.getText());
                    mU.setSetPointZone3_i(setPoint3_i);
                    System.out.println("Set point is inserted successfully" + setPoint3_i);
                    insertionStatus3_b = true;
                }

                if (textField4_t.getText().trim().isEmpty()) {
                    System.out.println("Text Field 4 is empty!");
                } else {
                    setPoint4_i = Integer.parseInt(textField4_t.getText());
                    mU.setSetPointZone4_i(setPoint4_i);
                    System.out.println("Set point is inserted successfully" + setPoint4_i);
                    insertionStatus4_b = true;
                }
            });
            settings_dia.add(insertButton_b);

            /**
             * **************************************************************
             * Zero Button ***********************************************************************
             */
            zeroButton_b = new JButton("0");
            zeroButton_b.setBounds(300, 301, 124, 50);
            zeroButton_b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            zeroButton_b.addActionListener(e -> {
                if (activeField_b != null) {
                    if (activeField_b.getText().trim().isEmpty()) {
                        //activeField_b.disable();
                    } else {
                        activeField_b.setText(activeField_b.getText() + "0");
                    }
                } else {
                    System.out.println("Choose a field");
                }
            });
            settings_dia.add(zeroButton_b);

            /**
             * **************************************************************
             * ClearF Button ***********************************************************************
             */
            clearButton_b = new JButton("CLRF");
            clearButton_b.setBounds(424, 301, 63, 50);
            clearButton_b.setBackground(new Color(255, 69, 0));
            clearButton_b.setFont(new Font("Arial", Font.BOLD, 12));
            clearButton_b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            clearButton_b.addActionListener(e -> {
                activeField_b.setText("");
            });
            
            settings_dia.add(clearButton_b);

            JLabel settingsLabel = new JLabel("Settings", SwingConstants.CENTER);
            settingsLabel.setBounds(440, 14, 100, 60);
            settingsLabel.setFont(new Font("Arial", Font.BOLD,24));
            settings_dia.add(settingsLabel);

            /**
             * **************************************************************
             * ClearAll Button ***********************************************************************
             */
            clearButton2_b = new JButton("CLRA");
            clearButton2_b.setBounds(487, 301, 65, 50);
            clearButton2_b.setBackground(new Color(255, 69, 0));
            clearButton2_b.setFont(new Font("Arial", Font.BOLD, 12));
            clearButton2_b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            clearButton2_b.addActionListener(e -> {
                textField1_t.setText("");
                textField2_t.setText("");
                textField3_t.setText("");
                textField4_t.setText("");
            });
            settings_dia.add(clearButton2_b);
        }
    }

    /* Getters */
    public JDialog getSettings_dia() {
        return settings_dia;
    }

    public JTextField getTextField1_t() {
        return textField1_t;
    }

    public JTextField getTextField2_t() {
        return textField2_t;
    }

    public JTextField getTextField3_t() {
        return textField3_t;
    }

    public JTextField getTextField4_t() {
        return textField4_t;
    }

    public JButton getOneButton_b() {
        return oneButton_b;
    }

    public JButton getTwoButton_b() {
        return twoButton_b;
    }

    public JButton getThreeButton_b() {
        return threeButton_b;
    }

    public JButton getFourButton_b() {
        return fourButton_b;
    }

    public JButton getFiveButton_b() {
        return fiveButton_b;
    }

    public JButton getSixButton_b() {
        return sixButton_b;
    }

    public JButton getSevenButton_b() {
        return sevenButton_b;
    }

    public JButton getEightButton_b() {
        return eightButton_b;
    }

    public JButton getNineButton_b() {
        return nineButton_b;
    }

    public JButton getZeroButton_b() {
        return zeroButton_b;
    }

    public JButton getClearButton_b() {
        return clearButton_b;
    }

    public JButton getInsertButton_b() {
        return insertButton_b;
    }

    public JButton getBackButton_b() {
        return backButton_b;
    }

    public JLabel getLabelStatus_l() {
        return labelStatus_l;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public MainWindowUI getMainUI() {
        return mainUI;
    }

    public boolean isInsertionStatus1_b() {
        return insertionStatus1_b;
    }

    public boolean isInsertionStatus2_b() {
        return insertionStatus2_b;
    }

    public boolean isInsertionStatus3_b() {
        return insertionStatus3_b;
    }

    public boolean isInsertionStatus4_b() {
        return insertionStatus4_b;
    }

    public int getSetPoint1_i() {
        return setPoint1_i;
    }

    public int getSetPoint2_i() {
        return setPoint2_i;
    }

    public int getSetPoint3_i() {
        return setPoint3_i;
    }

    public int getSetPoint4_i() {
        return setPoint4_i;
    }

    /* Setters */
    public void setSettings_dia(JDialog settings_dia) {
        this.settings_dia = settings_dia;
    }

    public void setTextField1_t(JTextField textField1_t) {
        this.textField1_t = textField1_t;
    }

    public void setTextField2_t(JTextField textField2_t) {
        this.textField2_t = textField2_t;
    }

    public void setTextField3_t(JTextField textField3_t) {
        this.textField3_t = textField3_t;
    }

    public void setTextField4_t(JTextField textField4_t) {
        this.textField4_t = textField4_t;
    }

    public void setOneButton_b(JButton oneButton_b) {
        this.oneButton_b = oneButton_b;
    }

    public void setTwoButton_b(JButton twoButton_b) {
        this.twoButton_b = twoButton_b;
    }

    public void setThreeButton_b(JButton threeButton_b) {
        this.threeButton_b = threeButton_b;
    }

    public void setFourButton_b(JButton fourButton_b) {
        this.fourButton_b = fourButton_b;
    }

    public void setFiveButton_b(JButton fiveButton_b) {
        this.fiveButton_b = fiveButton_b;
    }

    public void setSixButton_b(JButton sixButton_b) {
        this.sixButton_b = sixButton_b;
    }

    public void setSevenButton_b(JButton sevenButton_b) {
        this.sevenButton_b = sevenButton_b;
    }

    public void setEightButton_b(JButton eightButton_b) {
        this.eightButton_b = eightButton_b;
    }

    public void setNineButton_b(JButton nineButton_b) {
        this.nineButton_b = nineButton_b;
    }

    public void setZeroButton_b(JButton zeroButton_b) {
        this.zeroButton_b = zeroButton_b;
    }

    public void setClearButton_b(JButton clearButton_b) {
        this.clearButton_b = clearButton_b;
    }

    public void setInsertButton_b(JButton insertButton_b) {
        this.insertButton_b = insertButton_b;
    }

    public void setBackButton_b(JButton backButton_b) {
        this.backButton_b = backButton_b;
    }

    public void setLabelStatus_l(JLabel labelStatus_l) {
        this.labelStatus_l = labelStatus_l;
    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setMainUI(MainWindowUI mainUI) {
        this.mainUI = mainUI;
    }

    public void setInsertionStatus1_b(boolean insertionStatus_b) {
        this.insertionStatus1_b = insertionStatus_b;
    }

    public void setInsertionStatus2_b(boolean insertionStatus_b) {
        this.insertionStatus2_b = insertionStatus_b;
    }

    public void setInsertionStatus3_b(boolean insertionStatus_b) {
        this.insertionStatus3_b = insertionStatus_b;
    }

    public void setInsertionStatus4_b(boolean insertionStatus_b) {
        this.insertionStatus4_b = insertionStatus_b;
    }

    public void setSetPoint1_i(int setPoint1_i) {
        this.setPoint1_i = setPoint1_i;
    }

    public void setSetPoint2_i(int setPoint2_i) {
        this.setPoint2_i = setPoint2_i;
    }

    public void setSetPoint3_i(int setPoint3_i) {
        this.setPoint3_i = setPoint3_i;
    }

    public void setSetPoint4_i(int setPoint4_i) {
        this.setPoint4_i = setPoint4_i;
    }

}
