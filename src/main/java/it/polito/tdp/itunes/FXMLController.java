/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPlaylist"
    private Button btnPlaylist; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<Genre> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtDTOT"
    private TextField txtDTOT; // Value injected by FXMLLoader

    @FXML // fx:id="txtMax"
    private TextField txtMax; // Value injected by FXMLLoader

    @FXML // fx:id="txtMin"
    private TextField txtMin; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPlaylist(ActionEvent event) {
    	try {
    		Integer dTot = Integer.parseInt(this.txtDTOT.getText());
    		this.model.avviaRicorsione(dTot);
    		this.txtResult.appendText("Migliore playlist:\n");
    		for (Track t : this.model.getBestPlaylist()) {
    			this.txtResult.appendText(t + "\n");
    		}
    	} catch (NumberFormatException nfe) {
    		this.txtResult.appendText("Il campo dTot contiene un valore non ammesso.\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Genre genre = this.cmbGenere.getValue();
    	try {
    		Integer min = Integer.parseInt(this.txtMin.getText());
        	Integer max = Integer.parseInt(this.txtMax.getText());
        	this.model.createGraph(genre, min, max);
        	if (this.model.nVertici() != "0") {
        		this.txtResult.appendText("Grafo creato correttamente.\n");
        		this.txtResult.appendText("Numero di vertici: " + this.model.nVertici() + "\n");
            	this.txtResult.appendText("Numero di archi: " + this.model.nArchi() + "\n\n");
            	for (Set<Track> set : this.model.connectedSets()) {
            		this.txtResult.appendText("Componente avente " + set.size() + " vertici, inserita in " + this.model.countPlaylistsConnectedSet(set) + " playlists.\n\n");
            	}
        	}
        	else {
        		this.txtResult.appendText("Non è possibile creare un grafo dati i valori inseriti.\n");
        	}
    	} catch (NumberFormatException nfe) {
    		this.txtResult.appendText("I campi numerici non contengono dei valori ammessi.\n");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPlaylist != null : "fx:id=\"btnPlaylist\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDTOT != null : "fx:id=\"txtDTOT\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMax != null : "fx:id=\"txtMax\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMin != null : "fx:id=\"txtMin\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbGenere.getItems().setAll(this.model.getAllGenres());
    }

}
