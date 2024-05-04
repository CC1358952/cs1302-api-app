package cs1302.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;
import java.util.Locale.IsoCountryCode;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.google.gson.annotations.SerializedName;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.util.Locale;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */

public class ApiApp extends Application {

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();
    public static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();
    public static final int HEIGHT = 300;
    public static final int WIDTH = 450;
    public static final String KEY = "04ef81b3a35e42ccb9e144659240205";
    public static final String NHLBASE =
        "https://search.d3.nhle.com/api/v1/search/player?culture=en-us&limit=200&q=";
    public static final String WEATHERBASE = "http://api.weatherapi.com/v1/current.json?key=" +
        KEY + "&q=";

    public static final Font LABEL = Font.font("Verdana", FontWeight.BOLD, 20);
    public static final Font CONTENT = new Font("Verdana", 15);

    Stage stage;
    Scene scene;
    VBox root;
    TextField input;
    Button search;
    HBox infoTab;
    Text logoCredit;
    HBox teamContent;
    ImageView teamImg;
    HBox weatherContent;
    ImageView weatherImg;
    HBox noticeBox;
    //Objects to display player information
    VBox playerInfo;
    Text playerNameLabel;
    Text playerName;
    Text playerTeamLabel;
    Text playerTeam;
    Text playerPositionLabel;
    Text playerPosition;
    Text playerCityLabel;
    Text playerCity;
    //Objects to display weather information
    VBox weatherInfo;
    Text tempLabel;
    Text temp;
    Text windLabel;
    Text wind;
    Text precipLabel;
    Text precip;
    Text humidityLabel;
    Text humidity;
    Text descriptionLabel;
    Text description;
    VBox content;
    Label notice;
    ComboBox teamSelect;
    ObservableList<String> teamList;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
        root.setPrefWidth(WIDTH);
        root.setPrefHeight(HEIGHT);
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void init() {
        loadGeneral();
        loadNHL();
        loadWeather();
    }

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        buildNHL();
        buildWeather();
        buildElse();
    } // start

    /**Function that searches and displays the information requested
     * based on the search term.
     * @param term the String requested.
     * @param team the team of the desired player.*/
    public void search(String term, String team) {
        try {
            URI nURI = createURI(NHLBASE, term);
            NHLResponse player = nhlRequest(nURI, team);
            String city;
            if (player.birthCity.equals("Cole Harbour")) {
                city = "Dartmouth";
                System.out.println("=======================================================");
                System.out.println("THE SIDNEY CROSBY EXCEPTION");
                System.out.println("YOU ARE BEING RELOCATED TO DARTMOUTH, NOVA SCOTIA");
                System.out.println("=======================================================");
                System.out.println("I have NO idea why the WeatherApi changes " +
                    "Cole Harbour, Nova Scotia to Marsh Harbour, Bahamas XD");
                System.out.println("=======================================================");
            } else {
                city = player.birthCity;
            }
            URI wURI = createURI(WEATHERBASE, city);
            WeatherResponse wResponse = weatherRequest(wURI);
            //Set display information
            Runnable r = () -> {
                setInfo(player, wResponse);
            };
            Platform.runLater(r);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            Runnable r = () -> {
                Alert alert = new Alert(AlertType.ERROR, "Exception: " + e.getClass() +
                    "\nThat didn't work! Try this:\n-->Check your team" +
                    "\n-->Make sure your player is active");
                alert.showAndWait();
            };
            Platform.runLater(r);
        } catch (IllegalArgumentException e) {
            Runnable r = () -> {
                Alert alert = new Alert(AlertType.ERROR, "Exception: " + e.getClass() +
                    "\nThat didn't work! Try this:\n-->Check your spelling." +
                    "\n-->Please remove symbols such as slashes." +
                    "\n[The only acceptable symbols are dashes (-) and apostrophes (').]");
                alert.showAndWait();
            };
            Platform.runLater(r);
        }
    }

    /**Creates URIs for both NHL and Weather requests.
     * @param base the String of the base URL for the desired API.
     * @param term the information to put into the new URI.
     * @return uri the URI created.*/
    public static URI createURI(String base, String term) {
        String t = URLEncoder.encode(term, StandardCharsets.UTF_8);
        URI uri = URI.create(base + t);
        //System.out.println(uri);
        return uri;
    }

    /**Requests information from the NHL API.
     * @param uri the URI to request.
     * @param team the team of the desired player.
     * @return resp the NHLResponse object created.
     * @throws IOException.
     * @throws InterruptedException.
     * @throws IllegalArgumentException. */
    public static NHLResponse nhlRequest(URI uri, String team) throws IOException,
        InterruptedException, IllegalArgumentException {
        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
        NHLResponse[] respArr = GSON.fromJson(response.body(), NHLResponse[].class);
        if (respArr.length == 0) {
            throw new IllegalArgumentException();
        } else {
            ArrayList<NHLResponse> players = new ArrayList<NHLResponse>();
            for (int i = 0; i < respArr.length; i++) {
                if (respArr[i].active && respArr[i].teamAbbrev.equals(team)) {
                    players.add(respArr[i]);
                }
            }
            NHLResponse resp = players.get(0);
            return resp;
        }
    }

    /**Requests information from the Weather API.
     * @param uri the URI of the city to request.
     * @return resp the WeatherResponse object of the city.
     * @throws IOException.
     * @throws InterruptedException. */
    public static WeatherResponse weatherRequest (URI uri) throws IOException,
        InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
        WeatherResponse resp =  GSON.fromJson(response.body(), WeatherResponse.class);
        return resp;
    }

    /**Helper method to set info for labels.
     * @param player the NHLResponse to get info from.
     * @param wResponse the WeatherResponse to get info from.*/
    public void setInfo(NHLResponse player, WeatherResponse wResponse) {
        teamImg.setImage(new Image(String.format("file:resources/NHL_Logos/%s.jpg",
            player.teamAbbrev)));
        playerName.setText(player.name);
        playerTeam.setText(player.teamAbbrev);
        playerPosition.setText(player.positionCode);
        playerCity.setText(player.birthCity + ", " + player.birthCountry);
        String code = wResponse.current.condition.icon.substring(20);
        weatherImg.setImage(new Image("file:resources" + code));
        temp.setText("" + wResponse.current.tempF + "\u00B0F");
        wind.setText("" + wResponse.current.windMPH + " mph");
        precip.setText("" + wResponse.current.precipIN + " in");
        humidity.setText("" + wResponse.current.humidity + "\u0025");
        description.setText("" + wResponse.current.condition.text);
    }

/**Helper method to load nhl objects.*/
    public void loadNHL() {
        this.teamContent = new HBox(5);
        this.teamImg = new ImageView(new Image("file:resources/NHL_Logos/NHL.jpg"));
        teamImg.setPreserveRatio(true);
        teamImg.setFitWidth(6 * WIDTH / 10);
        this.playerInfo = new VBox();

        this.playerNameLabel = new Text("Name:");
        playerNameLabel.setFont(LABEL);
        this.playerName = new Text("");
        playerName.setFont(CONTENT);
        this.playerTeamLabel = new Text("Team:");
        playerTeamLabel.setFont(LABEL);
        this.playerTeam = new Text("");
        playerTeam.setFont(CONTENT);
        this.playerPositionLabel = new Text("Position:");
        playerPositionLabel.setFont(LABEL);
        this.playerPosition = new Text("");
        playerPosition.setFont(CONTENT);
        this.playerCityLabel = new Text("Birth City:");
        playerCityLabel.setFont(LABEL);
        this.playerCity = new Text("");
        playerCity.setFont(CONTENT);


    }

    /**Helper method that builds out NHL content.*/
    public void buildNHL() {
        playerInfo.getChildren().addAll(playerNameLabel);
        playerInfo.getChildren().addAll(playerName);
        playerInfo.getChildren().addAll(playerTeamLabel);
        playerInfo.getChildren().addAll(playerTeam);
        playerInfo.getChildren().addAll(playerPositionLabel);
        playerInfo.getChildren().addAll(playerPosition);
        playerInfo.getChildren().addAll(playerCityLabel);
        playerInfo.getChildren().addAll(playerCity);

        teamContent.getChildren().addAll(teamImg);
        teamContent.getChildren().addAll(playerInfo);
    }

    /**Helper method to load weather objects.*/
    public void loadWeather() {
        this.weatherInfo = new VBox();
        this.weatherContent = new HBox(5);
        this.weatherImg = new ImageView(new Image("file:resources/default.png"));
        weatherImg.setPreserveRatio(true);
        weatherImg.setFitWidth(5 * WIDTH / 10);

        this.tempLabel = new Text("Temperature:");
        tempLabel.setFont(LABEL);
        this.temp = new Text("");
        temp.setFont(CONTENT);
        this.windLabel = new Text("Wind Speed:");
        windLabel.setFont(LABEL);
        this.wind = new Text("");
        wind.setFont(CONTENT);
        this.precipLabel = new Text("Precipitation:");
        precipLabel.setFont(LABEL);
        this.precip = new Text("");
        precip.setFont(CONTENT);
        this.humidityLabel = new Text("Humidity:");
        humidityLabel.setFont(LABEL);
        this.humidity = new Text("");
        humidity.setFont(CONTENT);
        this.descriptionLabel = new Text("Description:");
        descriptionLabel.setFont(LABEL);
        this.description = new Text("");
        description.setFont(CONTENT);
    }

    /**Helper method to connect weather objects.*/
    public void buildWeather() {
        weatherInfo.getChildren().addAll(tempLabel);
        weatherInfo.getChildren().addAll(temp);
        weatherInfo.getChildren().addAll(windLabel);
        weatherInfo.getChildren().addAll(wind);
        weatherInfo.getChildren().addAll(precipLabel);
        weatherInfo.getChildren().addAll(precip);
        weatherInfo.getChildren().addAll(humidityLabel);
        weatherInfo.getChildren().addAll(humidity);
        weatherInfo.getChildren().addAll(descriptionLabel);
        weatherInfo.getChildren().addAll(description);

        weatherContent.getChildren().addAll(weatherInfo);
        weatherContent.getChildren().addAll(weatherImg);
    }

    /**Helper method to load general objects.*/
    public void loadGeneral() {
        //User input tab:
        this.noticeBox = new HBox();
        this.notice = new Label("Input an active NHL player's last name");
        notice.setTextAlignment(TextAlignment.LEFT);

        this.infoTab = new HBox(8);
        this.teamList = FXCollections.observableArrayList("ANA", "ARI", "BOS", "BUF",
            "CGY", "CAR", "CHI", "COL", "CBJ", "DAL", "DET", "EDM", "FLA", "LAK", "MIN",
            "MTL", "NSH", "NJD", "NYI", "NYR", "OTT", "PHI", "PIT", "SEA", "SJS", "STL",
            "TBL", "TOR", "VAN", "VGK", "WSH", "WPG");
        this.teamSelect = new ComboBox(teamList);
        this.input = new TextField();
        input.setPrefWidth(6 * WIDTH / 10);
        this.search = new Button("SEARCH");

        this.content = new VBox(5);

        this.logoCredit = new Text("All NHL logos used are the property of their teams." +
            "\nNot affiliated with the NHL or any team.\nIcons used were sourced from WeatherAPI.");
        logoCredit.setTextAlignment(TextAlignment.CENTER);

        search.setOnAction(event -> {
            Runnable r = () -> {
                try {
                    if (input.getText().toLowerCase().equals("")) {
                        throw new IllegalStateException();
                    }
                    search(input.getText().toLowerCase(), teamSelect.getValue().toString());
                } catch (NullPointerException e) {
                    Runnable r2 = () -> {
                        Alert alert = new Alert(AlertType.ERROR, "Exception: " + e.getClass()
                            + "\nThat didn't work! Try this:\n -->Please populate the " +
                            "team dropdown, then try again.");
                        alert.showAndWait();
                    };
                    Platform.runLater(r2);
                } catch (IllegalStateException e) {
                    Runnable r3 = () -> {
                        Alert alert = new Alert(AlertType.ERROR, "Exception: " + e.getClass()
                            + "\nThat didn't work! Try this:\n -->Please populate the " +
                            "player name text field, then try again.");
                        alert.showAndWait();
                    };
                    Platform.runLater(r3);
                }
            };
            Thread t = new Thread(r);
            t.start();
        });
    }

    /**Helper method to build the app structure.*/
    public void buildElse() {
        content.getChildren().addAll(teamContent);
        content.getChildren().addAll(weatherContent);
        infoTab.getChildren().addAll(teamSelect);
        infoTab.getChildren().addAll(input);
        infoTab.getChildren().addAll(search);

        root.getChildren().addAll(noticeBox);
        root.getChildren().addAll(infoTab);
        root.getChildren().addAll(content);
        root.getChildren().addAll(logoCredit);
        root.setAlignment(Pos.TOP_CENTER);
        root.setMargin(content, new Insets(6.5));
        root.setStyle("-fx-background-color: #FFFFFF;");

        scene = new Scene(root);
        stage.setTitle("NHL Birth City Weather Tracker");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        Platform.runLater(() -> stage.setResizable(false));
        stage.show();

    }

} // ApiApp
