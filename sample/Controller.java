package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.File;

import javafx.application.Platform;

import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller
{
    //atributi
    public TextField podstring;
    public Button trazi;
    public ListView spisak;
    public File korijenski_direktorij;
    public Button prekini;

    private ObservableList<String> lista_pregleda;

    //konstruktor
    public Controller()
    {
        korijenski_direktorij = new File(System.getProperty("user.home"));
        lista_pregleda = FXCollections.observableArrayList();
    }

    /*"initialize" metoda sluzi za databinding*/
    @FXML
    public void initialize()
    {
        /*povezivanje tipova observableList i TextView*/
        spisak.setItems(lista_pregleda);

        prekini.getStyleClass().add("prekidanje");
    }

    /*ubacuje u atribut observable list svaki fajl na kopu koji ima tekts ako sto je unesen u texfield*/
    private void IzvrsavanjePretrage(String put)
    {
        File trazeni = new File(put);

        if(trazeni.isFile())
        {
            if(trazeni.getName().contains( podstring.getText() ))
                lista_pregleda.add(trazeni.getAbsolutePath());
        }
        else if( trazeni.isDirectory() )
        {
            /*ulazi dok ne prodjes svaki podfolder, kada ce doci do izuzetka*/
            try
            {
                for(File novi : trazeni.listFiles())
                    IzvrsavanjePretrage(novi.getAbsolutePath());
            }
            catch (Exception e)
            {
            }
        }

    }

    public void Pretrazi(ActionEvent actionEvent)
    {
        lista_pregleda.clear();

        Thread novi = new Thread(()->{
                Platform.runLater(()-> {
                                            IzvrsavanjePretrage(korijenski_direktorij.getAbsolutePath());
                });
        });

        novi.start();

        while(novi.getState() == Thread.State.RUNNABLE )
        {
            trazi.getStyleClass().add("trazenje");
            prekini.getStyleClass().removeAll("prekidanje");

        }

        trazi.getStyleClass().removeAll("trazenje");
        prekini.getStyleClass().add("prekidanje");

    }

    public void Prekini(ActionEvent actionEvent)
    {

    }
}
