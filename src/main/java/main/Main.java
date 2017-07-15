package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import logic.Schiffeversenken;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/Schiffeversenken.fxml"));
		Parent root = loader.load();
		new Schiffeversenken(loader.getController());

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setMinHeight(700);
		stage.setMinWidth(1200);
		stage.getIcons().add(new Image("/images/icon.jpg"));
		stage.setTitle("Schiffeversenken");
		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
		stage.show();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
