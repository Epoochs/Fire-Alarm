
import firesystem .Zone;
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
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.util.Duration;

class TextArea {

    private StringBuilder numpadInput;
    private TextField textField;

    public TextArea(StringBuilder numpadInput, TextField textField) {
        this.numpadInput = numpadInput;
        this.textField = textField;
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
    // Fields for user input
    private TextArea usernameField = new TextArea(new StringBuilder(), new TextField());
    private TextArea passwordField = new TextArea(new StringBuilder(), new TextField());

    private TextArea textAreas[] = new TextArea[zonesNum];
    private TextArea activeField; // Track which TextField is active

    private String username;
    private String password;

    private StackPane mainLayout;

    private final String defaultSetTemp = "50";

    private String[] zonesSetTemp = new String[zonesNum];

    private Stage stagee;

    private int width = 1024;
    private int height = 600;
    private Label messageLabel;
    private Scene mainScene;

    Image  backgroundFireImage ;
    Image backgroundImage;
    ImageView backgroundView;
    ImageView backgroundFireView;
    private boolean isFireBackground = false;
    
    @Override
    public void start(Stage stage) {
        stagee = stage;
        for (int i = 0; i < zonesNum; i++) {
            zonesSetTemp[i] = new String();
        }
        for (int i = 0; i < zonesNum; i++) {
            textAreas[i] = new TextArea(new StringBuilder(), new TextField());
        }
        messageLabel = new Label();
        // Load images
        Image gifImage = new Image("file:///C:/Users/lenovo/Desktop/xoft-ezgif.com-video-to-gif-converter.gif");
        ImageView imageView = new ImageView(gifImage);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

        // Welcome Screen with GIF
        StackPane welcomeLayout = new StackPane();
        welcomeLayout.getChildren().add(imageView);
        Scene welcomeScene = new Scene(welcomeLayout, width, height);
        for (TextArea textArea : textAreas) {
            textArea.getTextField().setOnMouseClicked(event -> activeField = textArea);
        }

        usernameField.getTextField().setOnMouseClicked(event -> activeField = usernameField); // Prevent numpad interaction
        passwordField.getTextField().setOnMouseClicked(event -> activeField = passwordField); // Prevent numpad interaction

// Username and password fields (not controlled by numpad)
        usernameField.getTextField().setPromptText("Username");
        passwordField.getTextField().setPromptText("Password");

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
                numButton.setMinSize(60, 60);
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
        Scene settingsScene = new Scene(settingsLayout, width, height);
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
                keyButton.setMinSize(60, 60);
                loginKeyboard.add(keyButton, col, row);
                keyButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
                keyButton.setOnMouseEntered(e -> keyButton.setStyle("-fx-background-color: #CC0000; -fx-text-fill: white;"));
                keyButton.setOnMouseExited(e -> keyButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;"));

                // Handle keyboard input for login screen
                keyButton.setOnAction(e -> handleLoginKeyboardInput(value));
                indexx++;
            }
        }
        // Login Screen Layout
        //error message 
        messageLabel.setStyle("-fx-text-fill: red;");

        VBox loginLayout = new VBox(15);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(20));
        loginLayout.getChildren().addAll(usernameField.getTextField(), passwordField.getTextField(), loginKeyboard, messageLabel);

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #CC0000;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #FF0000;"));

        // Switch to settings screen when login is pressed
        loginButton.setOnAction(e -> {
            username = usernameField.getTextField().getText();
            password = passwordField.getTextField().getText();
            if (username.length() == 0 && password.length() == 0) {
                messageLabel.setText("please enter username and password");
            } else if (username.length() == 0) {
                messageLabel.setText("please enter username ");
            } else if (password.length() == 0) {
                messageLabel.setText("please enter password");
            } else {
                stage.setScene(settingsScene);
            }
            activeField = textAreas[0]; // Default active field
        });

        loginLayout.getChildren().add(loginButton);
        Scene loginScene = new Scene(loginLayout, width, height);

        // Main Screen Layout (Background only)
        backgroundImage = new Image("file:///C:/Users/lenovo/zones.png");
         backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(width);
        backgroundView.setFitHeight(height);
        backgroundView.setPreserveRatio(true);
        backgroundFireImage = new Image("file:///C:/Users/lenovo/Desktop/SCENE-3/8.png");
         backgroundFireView = new ImageView(backgroundFireImage);
        backgroundFireView.setFitWidth(width);
        backgroundFireView.setFitHeight(height);
        backgroundFireView.setPreserveRatio(true);
        
         mainLayout = new StackPane();
        mainLayout.getChildren().add(backgroundView);
        mainScene = new Scene(mainLayout, width, height);

        // Set the initial scene to the Welcome Screen
        stage.setScene(welcomeScene);
        stage.setTitle("Fire System");
        stage.show();

        // Pause before switching to the login screen
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
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

                for (String zone : zonesSetTemp) {
                    if (zone.length() == 0) {
                        flag = true;
                        errorZones += String.valueOf(j+1) + " ";
                        
                        zone = defaultSetTemp;
                    }
                    j++;
                }
                if (flag) {
                     errorZones += "will be set to default value :" + defaultSetTemp + "\n";
                    showConfirmationPopup(errorZones,stagee);
                   

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
private void showConfirmationPopup(String error,Stage parentStage) {
    // Create a new stage for the popup
    Stage popupStage = new Stage();
    popupStage.setTitle("Confirmation");

    // Block interaction with other windows (make it modal)
    popupStage.initOwner(parentStage);
    popupStage.setResizable(false);

    // Create the message and buttons
    Label messageLabel = new Label(error+" will be set to Do you want to proceed?");
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
private void startApplication ()
{
stagee.setScene(mainScene);
Zone zoneOne = new Zone("Zone 1","C:\\Users\\lenovo\\Desktop\\led1.txt","C:\\Users\\lenovo\\Desktop\\temp1.txt",Integer.valueOf(zonesSetTemp[0]));
new Thread(){@Override
public void run(){
    while(true)
    {
    if (zoneOne.getFireState())
{
    
        try {
            Platform.runLater(() -> switchBackground());
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(FireSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    }
}
}.start();

}
private void switchBackground(/*StackPane pane, ImageView backgroundView, ImageView backgroundFireView*/) {
        if (isFireBackground) {
            mainLayout.getChildren().remove(backgroundFireView);
            mainLayout.getChildren().add(backgroundView);
        } else {
            mainLayout.getChildren().remove(backgroundView);
            mainLayout.getChildren().add(backgroundFireView);
        }
        isFireBackground = !isFireBackground; // Toggle the flag
    }

public static void main(String[] args) {
    launch(args);
}
}


/*    // Create a shadowed text
        Text shadowedText = new Text("Default temp 75");
        shadowedText.setStyle("-fx-font-size: 24px; -fx-fill: white;");
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(javafx.scene.paint.Color.GRAY);
        shadowedText.setEffect(dropShadow);
 */

 /*
 // Settings Screen with a styled button and numpad
       
 Button nextButton = new Button("Next");

        // Style for the next button
        nextButton.setStyle("-fx-background-color: #FF0000; " +  // Red background
                            "-fx-text-fill: white; " +          // White text
                            "-fx-font-size: 20px; " +           // Increased font size
                            "-fx-padding: 5 10; " +             // Increased padding
                            "-fx-border-radius: 10px; " +       // Rounded corners
                            "-fx-background-radius: 10px;");

        // Add hover effect
        nextButton.setOnMouseEntered(e -> nextButton.setStyle("-fx-background-color: #CC0000;" +
                                                              "-fx-text-fill: white; " +
                                                              "-fx-font-size: 20px;" +
                                                              "-fx-padding: 5 10;" +
                                                              "-fx-border-radius: 10px;" +
                                                              "-fx-background-radius: 10px;"));
        nextButton.setOnMouseExited(e -> nextButton.setStyle("-fx-background-color: #FF0000;" +
                                                             "-fx-text-fill: white; " +
                                                             "-fx-font-size: 20px;" +
                                                             "-fx-padding: 5 10;" +
                                                             "-fx-border-radius: 10px;" +
     

// Button action: Switch to the main screen
        nextButton.setOnAction(e -> stage.setScene(mainScene));  "-fx-background-radius: 10px;"));


 */
 /*


 // private TextArea zoneOneField = new TextArea(new StringBuilder(), new TextField ());
 //   private TextArea zoneTwoField = new TextArea(new StringBuilder(), new TextField ());
  //  private TextArea zoneThreeField = new TextArea(new StringBuilder(), new TextField ());
  //  private TextArea zoneFourField = new TextArea(new StringBuilder(), new TextField ());
// Setting up fields and event listeners
       // zoneOneField.getTextField().setOnMouseClicked(event -> activeField = zoneOneField);
       // zoneTwoField.getTextField().setOnMouseClicked(event -> activeField = zoneTwoField);
      //  zoneThreeField.getTextField().setOnMouseClicked(event -> activeField = zoneThreeField);
      //  zoneFourField.getTextField().setOnMouseClicked(event -> activeField = zoneFourField);
/*
        zoneOneField.getTextField().setPromptText("Zone1:default temp " + defaultSetTemp);
        zoneTwoField.getTextField().setPromptText("Zone2:default temp " + defaultSetTemp);
        zoneThreeField.getTextField().setPromptText("Zone3:default temp " + defaultSetTemp);
        zoneFourField.getTextField().setPromptText("Zone4:default temp " + defaultSetTemp);
 /*
    zoneOneSetTemp = zoneOneField.getTextField().getText();
   zoneTwoSetTemp= zoneTwoField.getTextField().getText();
    zoneThreeSetTemp= zoneThreeField.getTextField().getText();
    zoneFourSetTemp = zoneFourField.getTextField().getText();; 
        //   fieldsLayout.getChildren().addAll(zoneOneField.getTextField(), zoneTwoField.getTextField(),zoneThreeField.getTextField(),zoneFourField.getTextField());

 */
