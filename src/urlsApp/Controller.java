package urlsApp;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import urlsApp.model.currentURL;
import urlsApp.model.myLink;


public class Controller {

    String myURL;
    currentURL crntURL;
    static myData D;
    String prevMyURL;
    ArrayList<String> hisLis;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button GoButton1;

    @FXML
    private Button GoButton2;

    @FXML
    private Button GoButton3;

    @FXML
    private Button PrevButton;

    @FXML
    private ListView<String> historyList;
    private List<String > list2 = new ArrayList<String>();
    private ObservableList<String> historyObservableList=FXCollections.observableList(list2);

    @FXML
    private Label imgCounter;

    @FXML
    private Label linksCounter;

    @FXML
    private ListView<myLink> linksList;
    private List<myLink> list = new ArrayList<myLink>();
    private ObservableList<myLink> linkObservableList=FXCollections.observableList(list);

    @FXML
    private Label sizeOfImg;

    @FXML
    private TextField typeURL;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Label currentSite;

    private void updateNumbersAndLists(){
        imgCounter.setText(String.valueOf(crntURL.getNrOfImages()));
        sizeOfImg.setText(String.valueOf(crntURL.getSizeOfImages())+ " B");
        linksCounter.setText(String.valueOf(crntURL.getNrOfLinks()));
        currentSite.setText(myURL);
        ConcurrentHashMap<myLink,myLink> setOfLinks=crntURL.getSetOfLinks();
        linkObservableList.clear();
        for(myLink key : setOfLinks.keySet()){
            linkObservableList.add(key);
        }
        int counter=0;
        for(String el: historyObservableList){
            if(el.equals(myURL)){
                counter++;
                break;
            }
        }
        if(counter==0) {
            historyObservableList.add(myURL);
            D.insertHistoryLink(myURL);
        }
        if(prevMyURL!=null)
            hisLis.add(prevMyURL);
        if(!hisLis.isEmpty())
            PrevButton.setDisable(false);
        prevMyURL=myURL;
    }


    private void handleStart(){
        if(!(myURL.startsWith("http://") || myURL.startsWith("https://")))
            myURL="http://"+myURL;
        try {
            crntURL=new currentURL(myURL);
        } catch (Exception e) {return; }

        try {
            crntURL.parseURL();
        } catch (IOException e) {
            return;
        }
        updateNumbersAndLists();
    }

    @FXML
    void historyButtonHandler(MouseEvent event) {
        myURL=historyList.getSelectionModel().getSelectedItem();
        handleStart();
    }

    @FXML
    void linksButtonHandler(MouseEvent event) {
        myURL=linksList.getSelectionModel().getSelectedItem().getWholeLink();
        handleStart();
    }

    @FXML
    void newURLButtonHandler(MouseEvent event) {

        myURL=typeURL.getText();
        typeURL.clear();
        handleStart();
    }

    @FXML
    void prevButtonHandler(MouseEvent event) {
        int hSize=hisLis.size();
        if(hSize==1){
            PrevButton.setDisable(true);
        }
        myURL=hisLis.get(hSize-1);
        hisLis.remove(hSize-1);
        prevMyURL=null;
        handleStart();
    }

    @FXML
    void initialize() {
        assert GoButton1 != null : "fx:id=\"GoButton1\" was not injected: check your FXML file 'sample.fxml'.";
        assert GoButton2 != null : "fx:id=\"GoButton2\" was not injected: check your FXML file 'sample.fxml'.";
        assert GoButton3 != null : "fx:id=\"GoButton3\" was not injected: check your FXML file 'sample.fxml'.";
        assert PrevButton != null : "fx:id=\"PrevButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert historyList != null : "fx:id=\"historyList\" was not injected: check your FXML file 'sample.fxml'.";
        assert imgCounter != null : "fx:id=\"imgCounter\" was not injected: check your FXML file 'sample.fxml'.";
        assert linksCounter != null : "fx:id=\"linksCounter\" was not injected: check your FXML file 'sample.fxml'.";
        assert linksList != null : "fx:id=\"linksList\" was not injected: check your FXML file 'sample.fxml'.";
        assert sizeOfImg != null : "fx:id=\"sizeOfImg\" was not injected: check your FXML file 'sample.fxml'.";
        assert typeURL != null : "fx:id=\"typeURL\" was not injected: check your FXML file 'sample.fxml'.";

        PrevButton.setDisable(true);
        linksList.setItems(linkObservableList);
        historyList.setItems(historyObservableList);
        D=new myData();
        List<String> H=D.selectHistoryLinks();
        for(String element:H){
            historyObservableList.add(element);
        }
        hisLis=new ArrayList<String>();

    }

}
