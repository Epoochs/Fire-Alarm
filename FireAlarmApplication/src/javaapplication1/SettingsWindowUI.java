package javaapplication1;

import javax.swing.*;
import java.awt.*;

public class SettingsWindowUI {

    /* Settings Window UI */
    private JDialog settings_dia;
    private JTextField textField_t;
    private JButton oneButton_b, twoButton_b, threeButton_b, fourButton_b, fiveButton_b, sixButton_b, sevenButton_b, eightButton_b, nineButton_b;
    private JButton zeroButton_b, clearButton_b, insertButton_b, backButton_b;
    private JLabel labelStatus_l;

    /* Main Window UI */
    private JFrame mainFrame;
    private MainWindowUI mainUI;

    /* Status to check the Corner cases */
    private boolean insertionStatus_b = false;
    private int setPoint_i;

    public SettingsWindowUI(MainWindowUI mU) {

        this.mainFrame = mU.getMainWindow();

        if (settings_dia == null || !settings_dia.isShowing()) {

            settings_dia = new JDialog(this.mainFrame, "Settings", true);

            settings_dia.setSize(600, 400);
            settings_dia.setLocationRelativeTo(this.mainFrame);
            settings_dia.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            settings_dia.setLayout(null);

            // Back Button
            backButton_b = new JButton("OK");
            backButton_b.setBounds(14, 355, 105, 31); // Layout similar to layoutX and layoutY in JavaFX
            backButton_b.addActionListener(e -> {
                if (insertionStatus_b) {
                    // Back to mainWindowUI
                    settings_dia.dispose();
                } else {
                    System.out.println("Insert the set point");
                }
            });
            settings_dia.add(backButton_b);

            // TextField
            textField_t = new JTextField();
            textField_t.setBounds(83, 185, 100, 25);
            settings_dia.add(textField_t);

            // Label for "Set Point"
            JLabel setPointLabel = new JLabel("Set Point:");
            setPointLabel.setBounds(14, 185, 100, 25);
            settings_dia.add(setPointLabel);

            // GridPane equivalent using GridLayout for buttons (3x3 grid)
            JPanel buttonPanel = new JPanel(new GridLayout(3, 3));
            buttonPanel.setBounds(334, 124, 187, 152);

            oneButton_b = new JButton("1");
            twoButton_b = new JButton("2");
            threeButton_b = new JButton("3");
            fourButton_b = new JButton("4");
            fiveButton_b = new JButton("5");
            sixButton_b = new JButton("6");
            sevenButton_b = new JButton("7");
            eightButton_b = new JButton("8");
            nineButton_b = new JButton("9");

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
                textField_t.setText(textField_t.getText() + "1");
            });
            twoButton_b.addActionListener(e -> {
                textField_t.setText(textField_t.getText() + "2");
            });
            threeButton_b.addActionListener(e -> {
                textField_t.setText(textField_t.getText() + "3");
            });
            fourButton_b.addActionListener(e -> {
                textField_t.setText(textField_t.getText() + "4");
            });
            fiveButton_b.addActionListener(e -> {
                textField_t.setText(textField_t.getText() + "5");
            });
            sixButton_b.addActionListener(e -> {
                textField_t.setText(textField_t.getText() + "6");
            });
            sevenButton_b.addActionListener(e -> {
                textField_t.setText(textField_t.getText() + "7");
            });
            eightButton_b.addActionListener(e -> {
                textField_t.setText(textField_t.getText() + "8");
            });
            nineButton_b.addActionListener(e -> {
                textField_t.setText(textField_t.getText() + "9");
            });

            settings_dia.add(buttonPanel);

            // Enter Button
            insertButton_b = new JButton("Enter");
            insertButton_b.setBounds(521, 123, 65, 203);
            insertButton_b.addActionListener(e -> {
                if (textField_t.getText().trim().isEmpty()) {
                    /* Check if label is added before */
                    if (labelStatus_l == null) {
                        labelStatus_l = new JLabel("Nothing Entered");
                        labelStatus_l.setForeground(Color.red);
                        labelStatus_l.setBounds(269, 50, 62, 31);
                        settings_dia.add(labelStatus_l);
                    }
                } else {
                    if (labelStatus_l != null) {
                        settings_dia.remove(labelStatus_l);
                        labelStatus_l = null;
                    }
                    /* Setting the set point */
                    setPoint_i = Integer.parseInt(textField_t.getText());
                    mU.setSetPoint(setPoint_i);
                    System.out.println("Set point is inserted successfully" + setPoint_i);
                    insertionStatus_b = true;
                }
            });
            settings_dia.add(insertButton_b);

            // Zero Button
            zeroButton_b = new JButton("0");
            zeroButton_b.setBounds(334, 276, 124, 50);
            zeroButton_b.addActionListener(e -> {
                if (textField_t.getText().trim().isEmpty()) {
                    zeroButton_b.disable();

                } else {
                    textField_t.setText(textField_t.getText() + "0");
                }
            });
            settings_dia.add(zeroButton_b);

            // Clear Button
            clearButton_b = new JButton("CLR");
            clearButton_b.setBounds(457, 275, 65, 50);
            clearButton_b.addActionListener(e -> {
                textField_t.setText("");
                zeroButton_b.setEnabled(true);
            });
            settings_dia.add(clearButton_b);

            // Settings Label
            JLabel settingsLabel = new JLabel("Settings", SwingConstants.CENTER);
            settingsLabel.setBounds(269, 14, 62, 31);
            settings_dia.add(settingsLabel);
        }else{
            settings_dia.requestFocus();
        }
    }

    public int getSetPoint() {
        return setPoint_i;
    }

    public boolean getInsertionState() {
        return insertionStatus_b;
    }
    
    public JDialog getFoucs(){
        return settings_dia;
    }
}
