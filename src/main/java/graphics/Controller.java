package graphics;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;

import connection.Peer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logic.Schiffeversenken;

public class Controller implements Initializable{
	
	private Schiffeversenken ss;
	private Thread spieldauerThread;
	private final Image icon = new Image("/images/icon.jpg");
	
	@FXML private Label verbindungLabel;
	@FXML private Label ipLabel;
	@FXML private Label spieldauerLabel;
	@FXML private Label zugaufforderungLabel;
	
	@FXML private GridPane eigeneMap;
	@FXML private GridPane gegnMap;
	
	@FXML private ImageView loadingImageView;
	
	public void setSchiffeversenken(Schiffeversenken ss) {
		this.ss = ss;
	}
	
	public void askForPeer() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Schiffeversenken");
		alert.setHeaderText("Neues Spiel er\u00f6ffnen oder Spiel beitreten?");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(icon);

		ButtonType buttonTypeOne = new ButtonType("Neues Spiel");
		ButtonType buttonTypeTwo = new ButtonType("Spiel beitreten");

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == buttonTypeOne){
			Alert alertServer = new Alert(AlertType.INFORMATION);
			alertServer.setTitle("Schiffeversenken");
			alertServer.setHeaderText("IP-Adresse f\u00fcr Client");
			alertServer.setContentText("Diese Adresse auf anderem Computer eingeben, "
					+ "um verbinden zu k\u00f6nnen: " + Peer.getIP());
			Stage stageServer = (Stage) alertServer.getDialogPane().getScene().getWindow();
	        stageServer.getIcons().add(icon);
			alertServer.showAndWait();
			ss.verbindungHerstellen(null);
		} else {
			askForIPDialog("");
		} 
	}
	
	private void askForIPDialog(String hinweis) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Text Input Dialog");
		dialog.setContentText("Bitte gib eine IPv4-Adresse (mit Punkten) ein, um dich mit einem anderen Spiel zu verbinden:" + hinweis);
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(icon);
		Optional<String> result1 = dialog.showAndWait();
		if(result1.get().isEmpty()) {
			askForIPDialog("\nHinweis: IP-Adresse eingeben");
		} else if (result1.isPresent() && ss.proofIP(result1.get())){
		    ss.verbindungHerstellen(result1.get());
		} else {
			askForIPDialog("\nHinweis: Falsche IP-Adresse");
		}
	}
	
	public void zeigeVerbindungsstatus(String status) {
		verbindungLabel.setText("Verbindung: " + status);
	}
	
	public void zeigeIPAdresse(String ip) {
		ipLabel.setText("IP-Adresse: " + ip);
	}
	
	public void zeigeSpieldauer() {
		spieldauerThread.start();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				Button button = new Button();
				button.setOpacity(0.9);
				button.setMinHeight(50.0);
				button.setMinWidth(50.0);
				button.setOnAction(new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            	Button button = (Button)event.getSource();
		            	ss.beschiessen(GridPane.getColumnIndex(button), GridPane.getRowIndex(button));
		            }
		        });
				gegnMap.add(button, i, j);
			}
		}
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				ImageView imageView = new ImageView();
				imageView.setOpacity(0.9);
				eigeneMap.add(imageView, i, j);
			}
		}
		
		Task<Void> task = new Task<Void>() {
			final DecimalFormat format = new DecimalFormat("00"); 
			  @Override
			  public Void call() throws Exception {
			    int i = 0;
			    while (true) {
			      final int finalI = i;
			      Platform.runLater(new Runnable() {
			        @Override
			        public void run() {
			        	spieldauerLabel.setText("Spieldauer: " + format.format(finalI/60) + ":" + format.format(finalI%60) + " Min");
			        }
			      });
			      i++;
			      Thread.sleep(1000);
			    }
			  }
			};
		spieldauerThread = new Thread(task);
		spieldauerThread.setDaemon(true);
		
		buttonsSchalten(false);
	}

	public void paintGegnMap(Image image, int index) {
		((Button)gegnMap.getChildren().get(index + 1)).setBackground(new Background(
					new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT))); // Index + 1, da das erste Element der ObservableList
	}					
	// ein Objekt der Klasse Group ist und kein Button

	public void paintEigeneMap(Image image, int index) {
		((ImageView)eigeneMap.getChildren().get(index + 1)).setImage(image);
	}
	
	public void buttonsSchalten(boolean amZug) {
		if(amZug) {
			for(Node button : gegnMap.getChildren()) {
				button.setDisable(false);
			}
		} else {
			for(Node button : gegnMap.getChildren()) {
				button.setDisable(true);
			}
		}
	}
	
	public void setZugstatus(String zugstatus) {
		zugaufforderungLabel.setText(zugstatus);
	}
	
	public void spielendeAlert(boolean gewonnen) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Schiffeversenken");
		if(gewonnen) {
			alert.setHeaderText("Sie haben gewonnen!");
		} else {
			alert.setHeaderText("Sie haben verloren!");
		}
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(icon);

		ButtonType buttonTypeOne = new ButtonType("Nochmal spielen");
		ButtonType buttonTypeTwo = new ButtonType("Beenden");

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == buttonTypeOne) {
			ss.neuesSpiel();
		} else {
			Platform.exit();
		}
	}
}
