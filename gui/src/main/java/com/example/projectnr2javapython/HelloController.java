package com.example.projectnr2javapython;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HelloController {

    @FXML
    private ComboBox WyborDnia;

    @FXML
    private ListView HarmonogramList;

    @FXML
    private ListView DetaleList;

    @FXML
    private Button PomocButton;

    private int wybranyElement;
    private String dzien;
    private static JSONObject json = null;

    public void setJson(String text){
        json = new JSONObject(text);
    }
    public JSONObject getJson(){
        return json;
    }
    public void setWybranyElement(int we){
        this.wybranyElement = we;
    }
    public int getWybranyElement(){
        return wybranyElement;
    }
    public void setDzien(String dzien) {
        this.dzien = dzien;
    }
    public String getDzien(){
        return dzien;
    }

    @FXML
    public void initialize(){
        WyborDnia.getItems().add("Poniedziałek");
        WyborDnia.getItems().add("Wtorek");
        WyborDnia.getItems().add("Środa");
        WyborDnia.getItems().add("Czwartek");
        WyborDnia.getItems().add("Piątek");
        WyborDnia.getItems().add("Sobota");
        WyborDnia.getItems().add("Niedziela");
        WyborDnia.getItems().add("Wszystkie dni");
    }

    @FXML
    private void onHelpButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HelpScene.fxml"));

        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Pomoc");
        primaryStage.setScene(scene);

        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.show();
    }

    @FXML
    public void DzienWybrany(Event e){
        setWybranyElement(WyborDnia.getSelectionModel().getSelectedIndex());
        System.out.println(getWybranyElement());
        if(getWybranyElement() == 7){
            setDzien("");
        }else {
            setDzien(WyborDnia.getItems().get(getWybranyElement()).toString());
        }
        System.out.println("Wybrany dzień to " + getDzien());
    }
    
    public void DrukujDetali(JSONObject json1){
        if(json1 == null){
            System.out.println("JSON pusty");
            return;
        }
        DetaleList.getItems().clear();
        int a = HarmonogramList.getSelectionModel().getSelectedIndex();
        JSONArray jsonGrafik = json1.getJSONArray("grafik");
        if(!jsonGrafik.isEmpty()){
            JSONObject jsonObject = jsonGrafik.getJSONObject(a);
            System.out.println(jsonObject.toString());
            DetaleList.getItems().add("Instruktor: " + jsonObject.getString("instruktor"));
            DetaleList.getItems().add("Poziom: " + jsonObject.getString("poziom"));
            DetaleList.getItems().add("Zapisy: " + jsonObject.getString("zapisy"));
            JSONArray uwagi = jsonObject.getJSONArray("uwagi");
            int liczbaUwag = uwagi.length();
            String uwaga = null;
            for (int i=0; i<liczbaUwag; i++){
                if(i>0){
                    uwaga = uwaga + ", " + uwagi.getString(i);
                }else{
                    uwaga = uwagi.getString(i);
                }
            }
            uwaga = uwaga.substring(4);
            DetaleList.getItems().add("Uwagi: " + uwaga);
        }
    }

    public String getTekstStrony() throws IOException{
        String adres = "http://127.0.0.1:5000/";
        String adresZDniom;
        String dzienDlaURL;
        boolean wszystkiedni = false;
        if(getDzien() == null){
            adresZDniom = adres;
        }else{
            switch (getDzien()){
                case "Poniedziałek" -> dzienDlaURL = "Poniedzia%C5%82ek";
                case "Wtorek" -> dzienDlaURL = "Wtorek";
                case "Środa" -> dzienDlaURL = "%C5%9Aroda";
                case "Czwartek" -> dzienDlaURL = "Czwartek";
                case "Piątek" -> dzienDlaURL = "Pi%C4%85tek";
                case "Sobota" -> dzienDlaURL = "Sobota";
                case "Niedziela" -> dzienDlaURL = "Niedziela";
                default -> {
                    dzienDlaURL = "";
                    wszystkiedni = true;
                }
            }
            if (wszystkiedni){
                adresZDniom = adres;
            }else{
                adresZDniom = adres + "?dzien=" + dzienDlaURL;
            }
        }
        System.out.println(adresZDniom);
        URL url = new URL(adresZDniom);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream inputConn = connection.getInputStream();
        Scanner sc = new Scanner(inputConn);
        String text = sc.nextLine();
        return text;
    }

    public void PokazGrafik(String text){
        setJson(text);
        JSONArray jsonGrafik = getJson().getJSONArray("grafik");
        int liczbaZajec = jsonGrafik.length();
        System.out.println("liczba zajec = "+ liczbaZajec);
        HarmonogramList.getItems().clear();
        DetaleList.getItems().clear();
        if(!(liczbaZajec == 0)){
            for(int a = 0; a< liczbaZajec; a++){
                JSONObject jsonObject = jsonGrafik.getJSONObject(a);
                HarmonogramList.getItems().add(jsonObject.getString("dzien") + "  " + jsonObject.getString("godziny") + "  " + jsonObject.getString("kurs"));
            }
        }else{
            HarmonogramList.getItems().add("W wybranum dniu nie ma zajęć.");
        }
    }

    @FXML
    public void handleMouseClicked(MouseEvent arg0){
        System.out.println("clicked!");
        DrukujDetali(getJson());
    }

    @FXML
    protected void onWczytajButtonClick() throws IOException{
        String text = getTekstStrony();
        PokazGrafik(text);
    }
}