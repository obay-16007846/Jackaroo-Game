package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import engine.Game;
import engine.board.Cell;
import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Colour;
import model.card.Card;
import model.card.standard.Standard;
import model.player.Marble;
import model.player.Player;

public class Main extends Application implements EventHandler<ActionEvent> {
	private static final Map<String, Image> imageCache = new HashMap<>();
	private ImageView getCachedImage(String imageName) {
	    if (!imageCache.containsKey(imageName)) {
	        imageCache.put(imageName, new Image(getClass().getResourceAsStream("/" + imageName)));
	    }
	    ImageView imageView = new ImageView(imageCache.get(imageName));
	    imageView.setFitWidth(12);
	    imageView.setFitHeight(12);
	    return imageView;
	}

	private Game game;
	private Stage primaryStage;
	private AnchorPane an;
	private Label nextPlayerLabel;
	private Scene playerNameScene;
	private Label nameLabel;
	private TextField nameField;
	private Button startGame;

	private Scene playScene;
	private HBox track0;
	private VBox track1;
	private HBox track2;
	private VBox track3;
	private ArrayList<Button> trackButtons;

	private HBox homeZone0;
	private VBox homeZone1;
	private HBox homeZone2;
	private VBox homeZone3;
	
	private VBox safeZone0;
	private HBox safeZone1;
	private VBox safeZone2;
	private HBox safeZone3;

	private Label infoPlayer0;
	private Label infoPlayer1;
	private Label infoPlayer2;
	private Label infoPlayer3;

	private Label currentPlayerLabel;
	private Label turnLabel;
	private Label firePitLabel;

	private VBox humanHand;
	private ArrayList<Button> cardsButtons;

	private Button clearSelection;
	private Button endTurn;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Jackaroo");

		an = new AnchorPane();
		playerNameScene = new Scene(an, 900, 900);
		primaryStage.setScene(playerNameScene);
		primaryStage.show();

		nameLabel = new Label("Player Name");
		addControl(nameLabel, 30, 50, 150, 30);

		nameField = new TextField("Human");
		addControl(nameField, 150, 50, 200, 30);
		nameField.setPromptText("Player name");
		nameField.getParent().requestFocus();

		startGame = new Button("Start Game");
		startGame.setOnAction(this);
		addControl(startGame, 30, 150, 320, 30);
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == startGame) {
			if (nameField.getText().isEmpty()) {
				showErrorAlert("Player name cannot be empty");
			} else {
				goToNextScene();
			}
		}
		if (e.getSource() == clearSelection) {
			game.deselectAll();
		}
		if (e.getSource() == endTurn) {
			Player p = game.getPlayers().get(0);
			if (p.getSelectedCard() == null) {
				int r = (int) (Math.random() * p.getHand().size());
				try {
					p.selectCard(p.getHand().get(r));
				} catch (InvalidCardException e1) {
					showErrorAlert(e1.getMessage());
				}
			}
			game.endPlayerTurn();
			for (int j = 1; j < 4; j++) {
				if (game.canPlayTurn()) {
					try {
						game.playPlayerTurn();
					} catch (GameException z) {
						showErrorAlert(z.getMessage());
					}
				}
				game.endPlayerTurn();
			}
		}
		
		for (int i = 0; i < trackButtons.size(); i++) {
			if (e.getSource() == trackButtons.get(i)) {
				Player p = game.getPlayers().get(0);
				try {
					Cell cell = game.getBoard().getTrack().get(i);
					if (cell.getMarble() != null) {
						p.selectMarble(cell.getMarble());
					}
				} catch (InvalidMarbleException z) {
					showErrorAlert(z.getMessage());
				}
			}
		}

		for (int i = 0; i < cardsButtons.size(); i++) {
			if (e.getSource() == cardsButtons.get(i)) {
				Card c = game.getPlayers().get(0).getHand().get(i);
				try {
					game.getPlayers().get(0).selectCard(c);
					System.out.println("Selected card: " + c.getName());
					for (int j = 0; j < 4; j++) {
						if (game.canPlayTurn()) {
							game.playPlayerTurn();
						}
						game.endPlayerTurn();
					}
				} catch (GameException z) {
					showErrorAlert(z.getMessage());
				}
			}
		}
		updateGameState();
	}

	private void updateGameState() {
		updateTrack();
		updateHomeZones();
		updateSafeZones();
		updatePlayerInfo();
		updateHumanHand();
	}

	private void updateTrack() {
		// Update track0 (bottom)
		track0.getChildren().clear();
		for (int i = 12; i >= -12; i--) {
			int index = (i + 100) % 100;  // Handle negative indices
			Cell cell = game.getBoard().getTrack().get(index);
			String imageName = getImageName(cell.getMarble());
			ImageView image = getCachedImage(imageName);
			image.setFitWidth(12);
			image.setFitHeight(12);
			trackButtons.get(index).setGraphic(image);
			track0.getChildren().add(trackButtons.get(index));
		}

		// Update track1 (left)
		track1.getChildren().clear();
		for (int i = 37; i >= 13; i--) {
			Cell cell = game.getBoard().getTrack().get(i);
			String imageName = getImageName(cell.getMarble());
			ImageView image = getCachedImage(imageName);
			image.setFitWidth(12);
			image.setFitHeight(12);
			trackButtons.get(i).setGraphic(image);
			track1.getChildren().add(trackButtons.get(i));
		}

		// Update track2 (top)
		track2.getChildren().clear();
		for (int i = 38; i <= 62; i++) {
			Cell cell = game.getBoard().getTrack().get(i);
			String imageName = getImageName(cell.getMarble());
			ImageView image = getCachedImage(imageName);
			image.setFitWidth(12);
			image.setFitHeight(12);
			trackButtons.get(i).setGraphic(image);
			track2.getChildren().add(trackButtons.get(i));
		}

		// Update track3 (right)
		track3.getChildren().clear();
		for (int i = 63; i <= 87; i++) {
			Cell cell = game.getBoard().getTrack().get(i);
			String imageName = getImageName(cell.getMarble());
			ImageView image = getCachedImage(imageName);
			image.setFitWidth(12);
			image.setFitHeight(12);
			trackButtons.get(i).setGraphic(image);
			track3.getChildren().add(trackButtons.get(i));
		}
	}

	private void updateHomeZones() {
		// Update homeZone0 (bottom)
		homeZone0.getChildren().clear();
		for (Marble marble : game.getPlayers().get(0).getMarbles()) {
			String imageName = getImageName(marble);
			ImageView image = getCachedImage(imageName);
			image.setFitWidth(12);
			image.setFitHeight(12);
			homeZone0.getChildren().add(image);
		}

		// Update homeZone1 (left)
		homeZone1.getChildren().clear();
		for (Marble marble : game.getPlayers().get(1).getMarbles()) {
			String imageName = getImageName(marble);
			ImageView image = getCachedImage(imageName);
			image.setFitWidth(12);
			image.setFitHeight(12);
			homeZone1.getChildren().add(image);
		}

		// Update homeZone2 (top)
		homeZone2.getChildren().clear();
		for (Marble marble : game.getPlayers().get(2).getMarbles()) {
			String imageName = getImageName(marble);
			ImageView image = getCachedImage(imageName);
			image.setFitWidth(12);
			image.setFitHeight(12);
			homeZone2.getChildren().add(image);
		}

		// Update homeZone3 (right)
		homeZone3.getChildren().clear();
		for (Marble marble : game.getPlayers().get(3).getMarbles()) {
			String imageName = getImageName(marble);
			ImageView image = getCachedImage(imageName);
			image.setFitWidth(12);
			image.setFitHeight(12);
			homeZone3.getChildren().add(image);
		}
	}

	private void updateSafeZones() {
		// Update safeZone0 (bottom)
		safeZone0.getChildren().clear();
		for (Cell cell : game.getBoard().getSafeZones().get(0).getCells()) {
			String imageName = getImageName(cell.getMarble());
			ImageView image = getCachedImage(imageName);
			image.setFitWidth(12);
			image.setFitHeight(12);
			safeZone0.getChildren().add(image);
		}

		// Update safeZone1 (left)
		safeZone1.getChildren().clear();
		for (Cell cell : game.getBoard().getSafeZones().get(1).getCells()) {
			String imageName = getImageName(cell.getMarble());
			ImageView image = getCachedImage(imageName);
			image.setFitWidth(12);
			image.setFitHeight(12);
			safeZone1.getChildren().add(image);
		}

		// Update safeZone2 (top)
		safeZone2.getChildren().clear();
		for (Cell cell : game.getBoard().getSafeZones().get(2).getCells()) {
			String imageName = getImageName(cell.getMarble());
			ImageView image = getCachedImage(imageName);
			image.setFitWidth(12);
			image.setFitHeight(12);
			safeZone2.getChildren().add(image);
		}

		// Update safeZone3 (right)
		safeZone3.getChildren().clear();
		for (Cell cell : game.getBoard().getSafeZones().get(3).getCells()) {
			String imageName = getImageName(cell.getMarble());
			ImageView image = getCachedImage(imageName);
			image.setFitWidth(12);
			image.setFitHeight(12);
			safeZone3.getChildren().add(image);
		}
	}

	private void updatePlayerInfo() {
		for (int i = 0; i < 4; i++) {
			Player player = game.getPlayers().get(i);
			String info = player.getName() + " (" + player.getColour() + ") / Cards: " + player.getHand().size();
			switch (i) {
				case 0: infoPlayer0.setText(info); break;
				case 1: infoPlayer1.setText(info); break;
				case 2: infoPlayer2.setText(info); break;
				case 3: infoPlayer3.setText(info); break;
			}
		}
		
		Colour current = game.getActivePlayerColour();
		currentPlayerLabel.setText("Current: " + current.name());
		turnLabel.setText("Turn: " + (4 - game.getPlayers().get(0).getHand().size()));
		
		if (game.getFirePit().size() > 0) {
			int lastIndex = game.getFirePit().size() - 1;
			if (game.getFirePit().get(lastIndex) != null) {
				firePitLabel.setText(game.getFirePit().get(lastIndex).getName());
			}
		}
	}

	private void updateHumanHand() {
		humanHand.getChildren().clear();
		cardsButtons = new ArrayList<>();
		
		for (Card c : game.getPlayers().get(0).getHand()) {
			String cardInfo = c instanceof Standard ? 
				((Standard)c).getName() + "/ " + ((Standard)c).getRank() :
				c.getName();
				
			Button b = new Button(cardInfo);
			b.setOnAction(this);
			cardsButtons.add(b);
			humanHand.getChildren().add(b);
		}
	}

	private String getImageName(Marble marble) {
		System.out.println("Getting image for marble: " + marble);
		if (marble == null) {
			return "Empty.png";
		}
		switch (marble.getColour()) {
			case RED: return "Red.png";
			case BLUE: return "Blue.png";
			case GREEN: return "Green.png";
			case YELLOW: return "Yellow.png";
			default: return "Black.png";
		}
	}

	public void addControl(Region c, int x, int y, int w, int h) {
		c.setLayoutX(x);
		c.setLayoutY(y);
		c.setPrefWidth(w);
		c.setPrefHeight(h);
		an.getChildren().add(c);
	}

	public void showErrorAlert(String message) {
	    Stage popup = new Stage();
	    popup.setTitle("Error");

	    VBox box = new VBox(10);
	    box.setStyle("-fx-background-color: #fefefe; -fx-padding: 20px; -fx-alignment: center;");

	    // Add error image
	    ImageView errorIcon = new ImageView(new Image(getClass().getResourceAsStream("/error.png")));
	    errorIcon.setFitWidth(40);
	    errorIcon.setFitHeight(40);

	    Label label = new Label(message);
	    label.setFont(new Font("Courier New", 19));
	    label.setStyle("-fx-text-fill: red;");

	    Button ok = new Button("OK");
	    ok.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 5 15 5 15; -fx-background-radius: 5;");
	    ok.setOnAction(e -> popup.close());

	    box.getChildren().addAll(errorIcon, label, ok);

	    Scene scene = new Scene(box, 550, 200);
	    popup.setScene(scene);
	    popup.setResizable(false);
	    popup.showAndWait();
	}

	
	public void goToNextScene() {
		try {
			game = new Game(nameField.getText());
		} catch (IOException e) {
			showErrorAlert("Error creating game: " + e.getMessage());
			return;
		}
		
		an = new AnchorPane();
		playScene = new Scene(an, 1050, 620);

		primaryStage.setScene(playScene);
		primaryStage.setX(0);
		primaryStage.setY(0);

		setupGameComponents();
		updateGameState();
	}

	private void setupGameComponents() {
		// Initialize track buttons
		nextPlayerLabel = new Label();
		addControl(nextPlayerLabel, 50, 130, 200, 30);
		trackButtons = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Button b = new Button();
			b.setMinSize(12, 12);
			b.setMaxSize(12, 12);
			b.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1px;");
			b.setOnAction(this);
			trackButtons.add(b);
		}

		// Initialize tracks with spacing
		track0 = new HBox(2);  // 2 pixels spacing between cells
		track0.setStyle("-fx-background-color: lightgray; -fx-padding: 2px;");
		addControl(track0, 584, 452, 300, 16);  //Bottom Horizontal row cells
		
		track1 = new VBox(2);
		track1.setStyle("-fx-background-color: lightgray; -fx-padding: 2px;");
		addControl(track1, 584, 100, 16, 300); //Left Vertical column of cells
		
		track2 = new HBox(2);
		track2.setStyle("-fx-background-color: lightgray; -fx-padding: 2px;");
		addControl(track2, 600, 100, 300, 16);//top horizontal row of cells
		
		track3 = new VBox(2);
		track3.setStyle("-fx-background-color: lightgray; -fx-padding: 2px;");
		addControl(track3, 937, 116, 16, 300); //right vertical column of cells
////////////////////////////////////////////////////////////////////////////////////////////////
		// Initialize zones with styling
		homeZone0 = new HBox(2);
		homeZone0.setStyle("-fx-background-color: #cecdd4; -fx-padding: 2px;");  // Light green
		addControl(homeZone0, 738, 468, 48, 16);
		
		homeZone1 = new VBox(2);
		homeZone1.setStyle("-fx-background-color: #cecdd4; -fx-padding: 2px;");  // Light blue
		addControl(homeZone1, 568, 284, 16, 48);
		
		homeZone2 = new HBox(2);
		homeZone2.setStyle("-fx-background-color: #cecdd4; -fx-padding: 2px;");  // Light red
		addControl(homeZone2, 740, 85, 48, 16);
		
		homeZone3 = new VBox(2);
		homeZone3.setStyle("-fx-background-color: #cecdd4; -fx-padding: 2px;");  // Light yellow
		addControl(homeZone3, 952, 284, 16, 48);
		
		safeZone0 = new VBox(2);
		safeZone0.setStyle("-fx-background-color: #90EE90; -fx-padding: 2px;");
		addControl(safeZone0, 0, 0, 16, 48);
		
		safeZone1 = new HBox(2);
		safeZone1.setStyle("-fx-background-color: #ADD8E6; -fx-padding: 2px;");
		addControl(safeZone1, 0, 0, 48, 16);
		
		safeZone2 = new VBox(2);
		safeZone2.setStyle("-fx-background-color: #FFB6C1; -fx-padding: 2px;");
		addControl(safeZone2, 0, 0, 16, 48);
		
		safeZone3 = new HBox(2);
		safeZone3.setStyle("-fx-background-color: #FFFFE0; -fx-padding: 2px;");
		addControl(safeZone3, 0, 0, 48, 16);

		// Firepit image (120x120)
		ImageView firePitImage = new ImageView(new Image(getClass().getResourceAsStream("/firepit.png")));
		firePitImage.setFitWidth(120);
		firePitImage.setFitHeight(180);

		// Top card label (placed above firepit)
		firePitLabel = new Label("FirePit");
		firePitLabel.setFont(new Font("Courier New", 24));
		firePitLabel.setStyle("-fx-text-fill: black;");

		VBox firePitBox = new VBox(5);
		firePitBox.getChildren().addAll(firePitLabel, firePitImage);

		firePitBox.setLayoutX(710); 
		firePitBox.setLayoutY(186);

		an.getChildren().add(firePitBox);


		infoPlayer0 = new Label();
		infoPlayer0.setFont(new Font("Courier New", 22));
		addControl(infoPlayer0, 625, 490, 320, 35); // wider and lower

		infoPlayer1 = new Label();
		infoPlayer1.setFont(new Font("Courier New", 22));
		addControl(infoPlayer1, 265, 296, 320, 35); // shifted left and higher

		infoPlayer2 = new Label();
		infoPlayer2.setFont(new Font("Courier New", 22));
		addControl(infoPlayer2, 635, 30, 320, 35); // wider and higher

		infoPlayer3 = new Label();
		infoPlayer3.setFont(new Font("Courier New", 22));
		addControl(infoPlayer3, 1000, 296, 390, 35); // wider and higher


		currentPlayerLabel = new Label();
		addControl(currentPlayerLabel, 50, 50, 100, 30);
		
		turnLabel = new Label();
		addControl(turnLabel, 50, 100, 100, 30);
		
		humanHand = new VBox();
		addControl(humanHand, 50, 150, 100, 400);

		clearSelection = new Button("Clear Selection");
		addControl(clearSelection, 170, 150, 100, 30);
		clearSelection.setOnAction(this);

		endTurn = new Button("End Turn");
		addControl(endTurn, 170, 200, 100, 30);
		endTurn.setOnAction(this);
	}

	public static void main(String[] args) {
		launch(args);
	}
}


