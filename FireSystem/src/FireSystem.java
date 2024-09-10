
import firesystem.Buzzer;
import firesystem.Zone;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.awt.Toolkit;
import java.awt.Dimension;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;

class TextArea {

    private StringBuilder numpadInput;
    private TextField textField;
    private int width;
    private int height;
    private int x;
    private int y;

    public TextArea(StringBuilder numpadInput, TextField textField, int width, int height, int x, int y) {
        this.numpadInput = numpadInput;
        this.textField = textField;
        textField.setPrefSize(width, height);
    }

    public StringBuilder getNumpadInput() {
        return numpadInput;
    }

    public TextField getTextField() {
        return textField;
    }
}

public class FireSystem extends Application {

    private final int zonesNum = 4;
    private boolean errorMsgState = false;
    // Fields for user input
    private TextArea usernameField = new TextArea(new StringBuilder(), new TextField(), 50, 30, 100,50);
    private TextArea passwordField = new TextArea(new StringBuilder(), new TextField(), 50, 30,100,50);
    // private String logstr = new String(" ");
    private TextArea textAreas[] = new TextArea[zonesNum];
    private TextArea activeField; // Track which TextField is active
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension d = toolkit.getScreenSize();

    private String username = "jets";

    private String password = "2024";
    StringBuilder enterdPassword = new StringBuilder();
    private Pane mainLayout;
    private Button sclience_b;
    private final String defaultSetTemp = "50";

    private String[] zonesSetTemp = new String[zonesNum];
    private String srcPath = "/images/";
    private String sensorPath = "/home/pi/sensors";
    private Stage stagee;
    private String filesPath = "/home/pi/platform";

    private double width = d.getWidth();
    private double height = d.getHeight();
    private final int fireScenes = 16;
    private ImageView[] backgroundFires = new ImageView[fireScenes];

    private Label messageLabel;
    private Label usernameLabel;
    //private Label logLabel;
    private Scene mainScene;

    private Image backgroundFireImage;
    private Image backgroundImage;
    private ImageView backgroundView;
    private ImageView backgroundFireView;
    private boolean isFireBackground = false;

    String BuzzerFilePath_s = "/home/pi/platform/Buzzer.txt";

    @Override
    public void start(Stage stage) {
        stage.setFullScreen(true);
        mainLayout = new Pane();

        for (int i = 0; i < fireScenes; i++) {
            backgroundFires[i] = new ImageView(new Image(getClass().getResourceAsStream(srcPath  + i + ".png")));
        }
        for (int i = 0; i < zonesNum; i++) {
            zonesSetTemp[i] = new String();
        }

        Scene welcomeScene = welcomeScene();
        Scene settingsScene = settingsScene();
        Scene loginScene = loginScene(settingsScene);
        mainScene();
        stagee = stage;

        stage.setScene(welcomeScene);
        stage.setTitle("Fire System");
        stage.show();

        // Pause before switching to the login screen
        PauseTransition pause = new PauseTransition(Duration.seconds(1.4));
        pause.setOnFinished(event -> {
            stage.setScene(loginScene);
            activeField = usernameField;
        });
        pause.play();
    }

    // Handle numpad input
    private void handleNumpadInput(String value) {
        if (activeField == null) {
            return; // No active field selected
        }

        switch (value) {
            case "<":
                // Clear the active field
                activeField.getTextField().clear();
                activeField.getNumpadInput().setLength(0);
                break;
            case "Enter":
                int j = 0;
                boolean flag = false;
                String errorZones = new String("Zone :");
                for (TextArea textArea : textAreas) {
                    zonesSetTemp[j++] = textArea.getTextField().getText();
                }
                j = 0;

                for (; j < zonesNum; j++) {
                    if (zonesSetTemp[j].length() == 0) {
                        flag = true;
                        errorZones += String.valueOf(j + 1) + " ";

                        zonesSetTemp[j] = defaultSetTemp;
                    }

                }
                if (flag) {
                    errorZones += "will be set to default value :" + defaultSetTemp + "\n";
                    showConfirmationPopup(errorZones, stagee);

                } else {
                    startApplication();
                }
                break;
            default:
                // Append number to the active field
                activeField.getNumpadInput().append(value);
                activeField.getTextField().setText(activeField.getNumpadInput().toString());
                break;
        }
    }

    private void handleLoginKeyboardInput(String value) {
        if (activeField == null) {
            return; // No active field selected
        }

        switch (value) {
            case "<":
                // Clear the active field
                activeField.getTextField().clear();
                activeField.getNumpadInput().setLength(0);
                break;
            default:
                // Append the value to the active field
                activeField.getNumpadInput().append(value);
               
                if (activeField == passwordField) {
                    StringBuilder maskedPassword = new StringBuilder();
                    
                    
                    for (int i = 0; i < activeField.getNumpadInput().length(); i++) {
                        maskedPassword.append("*");
                    }
                    activeField.getTextField().setText(maskedPassword.toString());
                } else {
                    activeField.getTextField().setText(activeField.getNumpadInput().toString());
                }
                break;
        }
    }

    private void showConfirmationPopup(String error, Stage parentStage) {
        // Create a new stage for the popup
        Stage popupStage = new Stage();
        popupStage.setTitle("Confirmation");

        // Block interaction with other windows (make it modal)
        popupStage.initOwner(parentStage);
        popupStage.setResizable(false);

        // Create the message and buttons
        Label messageLabel = new Label(error + " will be set to Do you want to proceed?");
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            System.out.println("User chose to proceed.");
            popupStage.close();
            startApplication();

        });

        noButton.setOnAction(e -> {
            System.out.println("User chose not to proceed.");
            popupStage.close();
            // You can add additional actions for "No" here
        });

        // Layout for the popup
        HBox buttonLayout = new HBox(10, yesButton, noButton);
        buttonLayout.setAlignment(Pos.CENTER);

        VBox popupLayout = new VBox(20, messageLabel, buttonLayout);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setPadding(new Insets(20));

        // Set the scene for the popup
        Scene popupScene = new Scene(popupLayout, 300, 150);
        popupStage.setScene(popupScene);

        // Show the popup
        popupStage.showAndWait(); // Wait until the popup is closed

    }

    private void showSensorDownPopup(String error, Stage parentStage) {
        // Create a new stage for the popup
        if (!errorMsgState) {
            Platform.runLater(() -> {
                errorMsgState = true;
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Sensor Disconnected");
                alert.setContentText(error);
                alert.show();
            });
        } else {
        }
    }

    private void startApplication() {

        stagee.setScene(mainScene);
        usernameLabel.setText(username);
        Zone zoneOne = new Zone("Zone 1", filesPath + "/led1.txt", sensorPath + "/temp1.txt", Integer.valueOf(zonesSetTemp[0]), BuzzerFilePath_s);
        Zone zoneTwo = new Zone("Zone 2", filesPath +"/led2.txt", sensorPath + "/temp2.txt", Integer.valueOf(zonesSetTemp[1]), BuzzerFilePath_s);
        Zone zoneThree = new Zone("Zone 3", filesPath + "/led3.txt", sensorPath + "/temp3.txt", Integer.valueOf(zonesSetTemp[2]), BuzzerFilePath_s);
        Zone zoneFour = new Zone("Zone 4", filesPath + "/led4.txt", sensorPath + "/temp4.txt", Integer.valueOf(zonesSetTemp[3]), BuzzerFilePath_s);

        new Thread() {

            String str = new String("");

            @Override
            public void run() {
                while (true) {
                    //  System.out.println(zoneOne.getSensorState());

                    if (zoneOne.getError() || zoneTwo.getError() || zoneThree.getError() || zoneFour.getError() || Zone.getCanError()) {
                        if (zoneOne.getError()) {
                            str += "Sensor one down\n";
                            zoneOne.setError(false);
                        }
                        if (zoneTwo.getError()) {
                            str += "Sensor Two down\n";
                            zoneTwo.setError(false);
                        }
                        if (zoneThree.getError()) {
                            str += "Sensor Three down\n";
                            zoneThree.setError(false);
                        }
                        if (zoneFour.getError()) {
                            str += "Sensor Four down\n";
                            zoneFour.setError(false);
                        }
                        if (Zone.getCanError()) {
                            str += "Can down\n";
                            zoneFour.setCanError(false);
                        }
                        showSensorDownPopup(str, stagee);
                        str = " ";
                    }
                    if ((zoneOne.getFireState() == false) && (zoneTwo.getFireState() == false) && (zoneThree.getFireState() == false) && (zoneFour.getFireState() == false)) {
                        switchFireBackground(0);
                        if (Buzzer.buzzerState) {
                            try {
                                Buzzer.setBuzzOff(BuzzerFilePath_s);
                            } catch (IOException ex) {
                                Logger.getLogger(FireSystem.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else if ((zoneOne.getFireState() == false) && (zoneTwo.getFireState() == false) && (zoneThree.getFireState() == false) && (zoneFour.getFireState() == true)) {
                        switchFireBackground(1);

                    } else if ((zoneOne.getFireState() == false) && (zoneTwo.getFireState() == false) && (zoneThree.getFireState() == true) && (zoneFour.getFireState() == false)) {
                        switchFireBackground(2);

                    } else if ((zoneOne.getFireState() == false) && (zoneTwo.getFireState() == false) && (zoneThree.getFireState() == true) && (zoneFour.getFireState() == true)) {
                        switchFireBackground(3);

                    } else if ((zoneOne.getFireState() == false) && (zoneTwo.getFireState() == true) && (zoneThree.getFireState() == false) && (zoneFour.getFireState() == false)) {
                        switchFireBackground(4);

                    } else if ((zoneOne.getFireState() == false) && (zoneTwo.getFireState() == true) && (zoneThree.getFireState() == false) && (zoneFour.getFireState() == true)) {
                        switchFireBackground(5);

                    } else if ((zoneOne.getFireState() == false) && (zoneTwo.getFireState() == true) && (zoneThree.getFireState() == true) && (zoneFour.getFireState() == false)) {
                        switchFireBackground(6);

                    } else if ((zoneOne.getFireState() == false) && (zoneTwo.getFireState() == true) && (zoneThree.getFireState() == true) && (zoneFour.getFireState() == true)) {
                        switchFireBackground(7);

                    } else if ((zoneOne.getFireState() == true) && (zoneTwo.getFireState() == false) && (zoneThree.getFireState() == false) && (zoneFour.getFireState() == false)) {
                        switchFireBackground(8);

                    } else if ((zoneOne.getFireState() == true) && (zoneTwo.getFireState() == false) && (zoneThree.getFireState() == false) && (zoneFour.getFireState() == true)) {
                        switchFireBackground(9);

                    } else if ((zoneOne.getFireState() == true) && (zoneTwo.getFireState() == false) && (zoneThree.getFireState() == true) && (zoneFour.getFireState() == false)) {
                        switchFireBackground(10);

                    } else if ((zoneOne.getFireState() == true) && (zoneTwo.getFireState() == false) && (zoneThree.getFireState() == true) && (zoneFour.getFireState() == true)) {
                        switchFireBackground(11);

                    } else if ((zoneOne.getFireState() == true) && (zoneTwo.getFireState() == true) && (zoneThree.getFireState() == false) && (zoneFour.getFireState() == false)) {
                        switchFireBackground(12);

                    } else if ((zoneOne.getFireState() == true) && (zoneTwo.getFireState() == true) && (zoneThree.getFireState() == false) && (zoneFour.getFireState() == true)) {
                        switchFireBackground(13);

                    } else if ((zoneOne.getFireState() == true) && (zoneTwo.getFireState() == true) && (zoneThree.getFireState() == true) && (zoneFour.getFireState() == false)) {
                        switchFireBackground(14);

                    } else if ((zoneOne.getFireState() == true) && (zoneTwo.getFireState() == true) && (zoneThree.getFireState() == true) && (zoneFour.getFireState() == true)) {
                        switchFireBackground(15);

                    }
                    try {
                        Platform.runLater(() -> switchBackground());
                        Thread.sleep(250);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FireSystem.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }
            }
        }.start();
    }

    private void switchFireBackground(int i) {
        backgroundFireView = backgroundFires[i];
        backgroundFireView.setFitWidth(width);
        backgroundFireView.setFitHeight(height);
        backgroundFireView.setPreserveRatio(true);
    }

    private void switchBackground(/*StackPane pane, ImageView backgroundView, ImageView backgroundFireView*/) {
        mainLayout.getChildren().remove(usernameLabel);
        mainLayout.getChildren().remove(sclience_b);

        if (isFireBackground) {
            mainLayout.getChildren().remove(backgroundFireView);
            mainLayout.getChildren().add(backgroundView);
        } else {
            mainLayout.getChildren().remove(backgroundView);
            mainLayout.getChildren().add(backgroundFireView);
        }
        mainLayout.getChildren().add(usernameLabel);

        mainLayout.getChildren().add(sclience_b);
        isFireBackground = !isFireBackground; // Toggle the flag
    }

    private void mainScene() {
// Main Screen Layout (Background only)
        //backgroundImage = new Image("file:///C:/Users/lenovo/zones.png");
        backgroundImage = new Image(srcPath + "0.png");

        sclience_b = new Button("Sclience");
        sclience_b.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
        sclience_b.setOnMouseEntered(e -> sclience_b.setStyle("-fx-background-color: #CC0000; -fx-text-fill: white;"));
        sclience_b.setOnMouseExited(e -> sclience_b.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;"));
        sclience_b.setOnAction(e -> {
            FileWriter buzzWrite_fw = null;
            try {
                Buzzer.setBuzzOff(BuzzerFilePath_s);
            } catch (IOException ex) {
                Logger.getLogger(FireSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        sclience_b.setMinSize(150, 30);
        sclience_b.setLayoutX(d.width - 155);
        sclience_b.setLayoutY(2);

        backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(true);
        usernameLabel = new Label(username);
        usernameLabel.setStyle("-fx-text-fill: red; -fx-font-size: 20px;");
        usernameLabel.setLayoutX(30);  // Set X position
        usernameLabel.setLayoutY(140); // Set Y position

        // Use a Pane to allow absolute positioning of children
        mainLayout.getChildren().add(backgroundView);
        mainLayout.getChildren().add(usernameLabel);
        mainLayout.getChildren().add(sclience_b);
        mainScene = new Scene(mainLayout, width, height);

    }

    private Scene settingsScene() {
        for (int i = 0; i < zonesNum; i++) {
            textAreas[i] = new TextArea(new StringBuilder(), new TextField(),200,100,100,300+(i*20));
        }
        for (TextArea textArea : textAreas) {
            textArea.getTextField().setOnMouseClicked(event -> activeField = textArea);
        }

        for (TextArea textArea : textAreas) {
            textArea.getTextField().setPromptText("default temp " + defaultSetTemp);
        }
        // Numpad Creation (Using GridPane)
        GridPane numpad = new GridPane();
        numpad.setAlignment(Pos.CENTER);
        numpad.setVgap(10);
        numpad.setHgap(10);

        // Numpad button values
        String[] numbers = {
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "<", "0", "Enter"
        };

        // Add buttons to the numpad
        int index = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 3; col++) {
                String value = numbers[index];
                Button numButton = new Button(value);
                numButton.setMinSize(120, 120);
                numpad.add(numButton, col, row);
                numButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
                numButton.setOnMouseEntered(e -> numButton.setStyle("-fx-background-color: #CC0000; -fx-text-fill: white;"));
                numButton.setOnMouseExited(e -> numButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;"));

                // Handle numpad input
                numButton.setOnAction(e -> handleNumpadInput(value));
                index++;
            }
        }

        // Combine text fields and numpad
        VBox fieldsLayout = new VBox(10);
        for (TextArea textArea : textAreas) {
            fieldsLayout.getChildren().addAll(textArea.getTextField());
        }
        fieldsLayout.setAlignment(Pos.CENTER_LEFT);

        VBox numpadLayout = new VBox(10);
        numpadLayout.getChildren().addAll(numpad);
        numpadLayout.setAlignment(Pos.CENTER_RIGHT);

        HBox settingsLayout = new HBox(20);
        settingsLayout.getChildren().addAll(fieldsLayout, numpadLayout);
        settingsLayout.setAlignment(Pos.CENTER);

        // Settings Scene
        return new Scene(settingsLayout, width, height);
    }

    private Scene loginScene(Scene settingsScene) {
        messageLabel = new Label();
        usernameField.getTextField().setOnMouseClicked(event -> activeField = usernameField); // Prevent numpad interaction
        passwordField.getTextField().setOnMouseClicked(event -> activeField = passwordField); // Prevent numpad interaction

        usernameField.getTextField().setPromptText("Username");
        passwordField.getTextField().setPromptText("Password");
        // Alphanumeric Keyboard for login screen
        GridPane loginKeyboard = new GridPane();
        loginKeyboard.setAlignment(Pos.CENTER);
        loginKeyboard.setVgap(10);
        loginKeyboard.setHgap(10);

        // Keyboard button values (A-Z, 0-9, and special keys)
        String[] keys = {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
            "A", "S", "D", "F", "G", "H", "J", "K", "L",
            "Z", "X", "C", "V", "B", "N", "M", "#", "*", "_", "<",};

        // Create the keyboard layout (3 rows of letters and 1 row for numbers)
        int indexx = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 10; col++) {
                if (indexx >= keys.length) {
                    break;
                }
                String value = keys[indexx];
                Button keyButton = new Button(value);
                keyButton.setMinSize(70, 70);
                loginKeyboard.add(keyButton, col, row);
                keyButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
                keyButton.setOnMouseEntered(e -> keyButton.setStyle("-fx-background-color: #CC0000; -fx-text-fill: white;"));
                keyButton.setOnMouseExited(e -> keyButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;"));

                // Handle keyboard input for login screen
                keyButton.setOnAction(e -> handleLoginKeyboardInput(value));
                indexx++;
            }
        }
        messageLabel.setStyle("-fx-text-fill: red;");

        VBox loginLayout = new VBox(15);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(20));
        loginLayout.getChildren().addAll(usernameField.getTextField(), passwordField.getTextField(), loginKeyboard, messageLabel);

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setPrefSize(70, 30);
        loginButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #CC0000;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #FF0000;"));

        // Switch to settings screen when login is pressed
        loginButton.setOnAction(e -> {
            String usernameCheck_s = usernameField.getTextField().getText().toLowerCase().trim();
            String passwordCheck_s = passwordField.getTextField().getText().toLowerCase().trim();
            if (username.length() == 0 && password.length() == 0) {
                messageLabel.setText("please enter username and password");
            } else if (username.length() == 0) {
                messageLabel.setText("please enter username ");
            } else if (password.length() == 0) {
                messageLabel.setText("please enter password");
            } else {
                if (usernameCheck_s.equals(username)) {
                     activeField = textAreas[0]; // Default active field
                    stagee.setScene(settingsScene);
                }else{
                    messageLabel.setText("username or password is wrong " + passwordCheck_s);
                }
                //usernameStr = username;
            }
           
        });

        loginLayout.getChildren().add(loginButton);
        return new Scene(loginLayout, width, height);

    }

    private Scene welcomeScene() {

        Image gifImage = new Image(getClass().getResourceAsStream(srcPath + "xoft-ezgif.com-video-to-gif-converter.gif"));

        ImageView imageView = new ImageView(gifImage);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        imageView.setPreserveRatio(true);

        // Welcome Screen with GIF
        StackPane welcomeLayout = new StackPane();
        welcomeLayout.getChildren().add(imageView);
        return new Scene(welcomeLayout, width, height);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
