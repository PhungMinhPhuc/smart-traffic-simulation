package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import render.*;
import items.road.*;
import items.utility.*;
import items.map.*;
import items.node.*;
import items.vehicle.*;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("scenes/MainScene.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("scenes/MainSceneStyle.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
