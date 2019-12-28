/*
 * File: Main.java Project: A-Team Social Media Project
 * 
 * This file provides the driver for ateam 65's Social Media project
 * 
 * Contributors: Maxwell Kleinsasser Apeksha Maithal Isaac Zaman Matthew Karrmann
 * 
 */

package application;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Optional;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class Main extends Application {
  // store any command-line arguments that were entered.
  // NOTE: this.getParameters().getRaw() will get these also
  private TableView<Person> table = new TableView<Person>();
  private TableView<Person> userFriendsTable = new TableView<Person>();

  SocialNetwork sn = new SocialNetwork();
  String storedCommands = "";

  // window specs
  private static final int WINDOW_WIDTH = 900;
  private static final int WINDOW_HEIGHT = 700;
  private static final String APP_TITLE = "Social Media";

  // observable array list for displaying all users in network
  private ObservableList<Person> data = FXCollections.observableArrayList();

  private void populateObservableList(List<Person> list) {
    data = FXCollections.observableArrayList();
    for (Person p : list) {
      data.add(p);
    }
    table.setItems(data);
  }

  // observable array list for displaying active user friends
  private ObservableList<Person> userFriendsList = FXCollections.observableArrayList();

  private void populateUserFriendsList(Set<Person> set) {
    userFriendsList = FXCollections.observableArrayList();
    for (Person p : set) {
      userFriendsList.add(p);
    }
    userFriendsTable.setItems(userFriendsList);
    userFriendsTable.getColumns().get(0).setText(sn.activeUser.getName() + "'s Friends");
  }

  /*
   * private helper for displaying error messages
   */
  public void makePopup(String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Error Message");
    alert.setHeaderText("The following error occured: ");
    alert.setContentText(message);
    alert.showAndWait();
  }

  // build scene
  @Override
  public void start(Stage primaryStage) throws Exception {

    // Button declarations
    Button exit = new Button("Exit");
    Button clear = new Button("Clear Network");
    Button shortestPath = new Button("Find Shortest Path");
    Button findMutuals = new Button("Find Mutual Friends");
    Button importCommand = new Button("Import Command File");
    Button doAction = new Button("Enter");

    Label lastCommand = new Label();
    HBox hbox = new HBox();
    hbox.getChildren().add(clear);
    hbox.getChildren().add(shortestPath);
    hbox.getChildren().add(findMutuals);
    hbox.getChildren().add(importCommand);
    hbox.getChildren().add(exit);

    HBox commandHbox = new HBox();

    VBox commandBox = new VBox();
    Label commandsLabel = new Label("Commands:");
    TextField commands = new TextField();
    commandBox.getChildren().addAll(commandsLabel, commands);

    commandHbox.getChildren().add(doAction);
    commandHbox.getChildren().add(commandBox);

    doAction.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        // have new dialog to search for a specific string
        String recentCommand = commands.getText();
        lastCommand.setText("Previous command: " + recentCommand);
        commands.setText("");
        if (recentCommand.equals(""))
          return;
        storedCommands = storedCommands + recentCommand + "\n";

        try {
          List<Person> vertices = sn.runAction(recentCommand);
          populateObservableList(vertices);
          populateUserFriendsList(sn.getFriends(sn.activeUser.getName()));
        } catch (ParseException err) {
          makePopup("Failed to process your request. Check syntax.");
        }

      }
    });

    importCommand.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        // have new dialog to search for a specific string
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Import File Name Prompt");
        dialog.setHeaderText("Enter a file name to import");
        dialog.setContentText("Import File:");

        Optional<String> result = dialog.showAndWait();
        String importFile = "";
        if (result.isPresent()) {
          importFile = result.get();
        }
        File toImport = new File(importFile);
        String saveCommands = "";
        try {
          saveCommands = sn.loadFromFile(toImport);
        } catch (ParseException e1) {
          makePopup("Failed to read some or all of your file. Check for proper format\n"
              + e1.getMessage());
        }
        populateObservableList(sn.getGraph().getVertices());
        populateUserFriendsList(sn.getFriends(sn.activeUser.getName()));
        storedCommands = storedCommands + saveCommands;
      }
    });

    // set functionality for findMutuals
    findMutuals.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        // have new dialog to search for a specific string
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Find Mutual Friends Prompt");
        dialog.setHeaderText("Enter a first User");
        dialog.setContentText("First User:");

        Optional<String> result = dialog.showAndWait();
        String user1 = "";
        if (result.isPresent()) {
          user1 = result.get();
        }
        TextInputDialog dialog2 = new TextInputDialog("");
        dialog.setTitle("Find Mutual Friends Prompt");
        dialog.setHeaderText("Enter a second user");
        dialog.setContentText("Second User:");

        Optional<String> result2 = dialog.showAndWait();
        String user2 = "";
        if (result2.isPresent()) {
          user2 = result2.get();
        }
        Set<Person> mutualFriends = sn.getMutualFriends(user1, user2);

        String path = "";
        for (Person p : mutualFriends) {
          path = path + p.getName() + ", ";
        }
        path = path.substring(0, path.length() - 2);

        TextInputDialog pathDisplay = new TextInputDialog("");
        pathDisplay.setTitle("Shortest Path");
        pathDisplay.setHeaderText(path);
        pathDisplay.setContentText("How does this make you feel?");

        pathDisplay.show();

      }
    });

    // set functionality for find Shortest path button
    shortestPath.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        // have new dialog to search for a specific string
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Find Path Prompt");
        dialog.setHeaderText("Enter a first User");
        dialog.setContentText("First User:");

        Optional<String> result = dialog.showAndWait();
        String user1 = "";
        if (result.isPresent()) {
          user1 = result.get();
        }
        TextInputDialog dialog2 = new TextInputDialog("");
        dialog.setTitle("Find Path Prompt");
        dialog.setHeaderText("Enter a second user");
        dialog.setContentText("Second User:");

        Optional<String> result2 = dialog.showAndWait();
        String user2 = "";
        if (result2.isPresent()) {
          user2 = result2.get();
        }
        try {
          List<Person> shortestPath = sn.getShortestPath(user1, user2);

          String path = "";
          for (Person p : shortestPath) {
            if (shortestPath.indexOf(p) == shortestPath.size() - 1) {
              path = path + p.getName();
            } else {
              path = path + p.getName() + " -> ";
            }
          }

          TextInputDialog pathDisplay = new TextInputDialog("");
          pathDisplay.setTitle("Shortest Path");
          pathDisplay.setHeaderText(path);
          pathDisplay.setContentText("How does this make you feel?");

          pathDisplay.show();
        } catch (Exception e1) {
          makePopup("Failed to calculate shortest path. Path may not exist.");
        }

      }
    });

    // set functionality for clear button
    clear.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        // have new dialog to search for a specific string
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Clear confirmation");
        alert.setHeaderText("You are about to clear the current social network, are you sure?");
        alert.setContentText("Choose your option.");

        ButtonType clear = new ButtonType("Clear!");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(clear, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == clear) {
          sn.clear();
          populateObservableList(sn.getGraph().getVertices());
          userFriendsTable.setItems(FXCollections.emptyObservableList());
          userFriendsTable.getColumns().get(0).setText("Set User to View Friends");
        } else {
          // ... user chose CANCEL or closed the dialog
        }
      }
    });

    // set functionality for exit button
    exit.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit confirmation");
        alert.setHeaderText(
            "You are about to exit, would you like to save your current social network?");
        alert.setContentText("Choose your option.");

        ButtonType exitWithoutSaving = new ButtonType("Exit without saving");
        ButtonType saveAndExit = new ButtonType("Save and Exit");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(exitWithoutSaving, saveAndExit, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == exitWithoutSaving) {
          primaryStage.close();
          // ... user chose "One"
        } else if (result.get() == saveAndExit) {
          TextInputDialog dialog = new TextInputDialog("");
          dialog.setTitle("Save Prompt");
          dialog.setHeaderText("Enter a file name");
          dialog.setContentText("File name:");

          Optional<String> result2 = dialog.showAndWait();
          String saveName = "";
          if (result2.isPresent()) {
            saveName = result2.get();
          }

          try {
            sn.saveToFile(storedCommands, saveName);
          } catch (IOException e1) {
            makePopup("Failed to save your file. So sorry");
          }
          primaryStage.close();
          // ... user chose "Two"
        } else {
          // ... user chose CANCEL or closed the dialog
        }
      }
    });

    // Main layout is Border Pane example (top,left,center,right,bottom)
    BorderPane root = new BorderPane();

    // Create a vertical box with Hello labels for each args
    HBox center = new HBox();

    // Table View for All Users List
    table.setEditable(true);

    TableColumn firstNameCol = new TableColumn("All Users");
    firstNameCol.setMinWidth(300);
    firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));

    table.setItems(data);
    table.getColumns().addAll(firstNameCol);
    center.setSpacing(5);
    center.getChildren().addAll(table);

    // Table View for Active User Friends List
    table.setEditable(true);

    TableColumn userFriendsCol = new TableColumn("Your Friends");
    userFriendsCol.setMinWidth(300);
    userFriendsCol.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));

    userFriendsTable.setItems(userFriendsList);
    userFriendsTable.getColumns().addAll(userFriendsCol);
    center.setSpacing(5);
    center.getChildren().addAll(userFriendsTable);

    // Commands pane
    VBox right = new VBox();

    ListView<String> list = new ListView<String>();
    ObservableList<String> actions = FXCollections.observableArrayList("Add Single User: a user",
        "Add Friendship: a user1 user2", "Remove User: r user", "Remove Friendship: r user1 user2",
        "Set Central User: s user");
    list.setItems(actions);

    right.getChildren().addAll(list, commandBox, lastCommand, doAction);

    // Add the vertical box to the center of the root pane
    root.setTop(new Label(APP_TITLE));
    root.setCenter(center);
    root.setRight(right);
    root.setBottom(hbox);
    Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

    // Add the stuff and set the primary stage
    primaryStage.setTitle(APP_TITLE);
    primaryStage.setScene(mainScene);
    primaryStage.show();

    System.out.println(System.getProperty("user.dir"));
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    launch(args);

  }
}
