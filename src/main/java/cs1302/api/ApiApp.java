package cs1302.api;

import java.util.ArrayList;
import java.util.Collection;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    public static final String MLB_URL = "http://lookup-service-prod.mlb.com";
    public static final String FRANK_URL = "https://api.frankfurter.app";

    Stage stage;
    Scene scene;
    VBox root;
    TextField input;
    Button search;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;
        this.input = new TextField();
        this.search = new Button("SEARCH");

        // demonstrate how to load local asset using "file:resources/"
        Image bannerImage = new Image("file:resources/readme-banner.png");
        ImageView banner = new ImageView(bannerImage);
        banner.setPreserveRatio(true);
        banner.setFitWidth(640);

        // some labels to display information
        Label notice = new Label("Modify the starter code to suit your needs.");
        search.setOnAction(event -> {
            search(input.getText());
        });
        // setup scene
        root.getChildren().addAll(banner, notice);
        root.getChildren().addAll(input);
        root.getChildren().addAll(search);
        scene = new Scene(root);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

    /**Function that searches and displays the information requested
     * based on the search term.
     * @param term the String requested.*/
    public void search(String term) {
        String q = URLEncoder.encode(term, StandardCharsets.UTF_8);
        String query = String.format(
            "https://goweather.herokuapp.com/weather/%s",
            q);
        String q2 = String.format(
            "https://search.d3.nhle.com/api/v1/search/player?culture=en-us&limit=10&q=%s", q);
        URI uri = URI.create(q2);
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            System.out.println(uri.toString());
            System.out.println(response.body());

            NHLResponse[] resp = GSON.fromJson(response.body(), NHLResponse[].class);
            System.out.println("================================================================");
            System.out.println(resp[0].name);

            ArrayList<NHLResponse> players = new ArrayList<NHLResponse>();
            for (int i = 0; i < resp.length; i++) {
                if (resp[i].active) {
                    players.add(resp[i]);
                }
            }
            System.out.println("============================================================");
            System.out.println(players.get(0).name);

            String weatherQ = URLEncoder.encode(players.get(0).birthCity, StandardCharsets.UTF_8);
            String weatherQuery = "https://goweather.herokuapp.com/weather/" + weatherQ;
            URI wuri = URI.create(weatherQuery);
            HttpRequest wRequest = HttpRequest.newBuilder().uri(wuri).build();
            HttpResponse<String> wResponse = HTTP_CLIENT.send(wRequest, BodyHandlers.ofString());
            System.out.println("==============================================");
            WeatherResponse w = GSON.fromJson(wResponse.body(), WeatherResponse.class);
            System.out.println(w.temperature);
            System.out.println("============================================");
            System.out.println(String.format("Name: %s, Position: %s, Team: %s, Birthplace: %s",
                players.get(0).name, players.get(0).positionCode, players.get(0).teamAbbrev,
                players.get(0).birthCity));


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**Function that takes a string and turns it into a valid URI.
     * @param term the String requested.
     * @param api the String of the default API path.
     * @return URI of the term and api input.*/
    public URI termToURI(String term, String api) {
        String q = URLEncoder.encode(term, StandardCharsets.UTF_8);
        String query = String.format(
            "/json/named.search_player_all.bam?sport_code='mlb'&active_sw='Y'&name_part='%s'",
            q);
        return URI.create(MLB_URL + query);
    }

    /**Function that turns a URI into a Player object.
     * @param uri the URI to request.
     * @return MLBResponse object. */
    public String uriToPlayer(URI uri) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            //MLBResponse resp = GSON.<MLBResponse>fromJson(response.body(), MLBResponse.class);
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return null;
        }
    }


} // ApiApp
