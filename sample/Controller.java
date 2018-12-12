package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    private Thread nit1;

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

        prekini.getStyleClass().add("neaktivan");
    }

    /*ubacuje u atribut observable list svaki fajl na kopu koji ima tekts ako sto je unesen u texfield*/
    private void IzvrsavanjePretrage(String put, String nalazilo_se_u_trenutku_slanja)
    {
        /*mora se dodati i ovaj parametar da bi korisnik mogao brisati sadrzaj text fielda,
         a da se svejedno trazi po onome sto je bilo u trenutku pritiska na trazi*/

        trazi.getStyleClass().add("neaktivan");
        prekini.getStyleClass().removeAll("neaktivan");

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
                    IzvrsavanjePretrage(novi.getAbsolutePath(), nalazilo_se_u_trenutku_slanja);
            }
            catch (Exception e)
            {
            }
        }

    }

    public void Pretrazi(ActionEvent actionEvent)
    {
        lista_pregleda.clear();
        
        nit1 = new Thread(()->{
                Platform.runLater(()-> {
                                            IzvrsavanjePretrage(korijenski_direktorij.getAbsolutePath(), podstring.getText());
                });
        });
        nit1.start();

        trazi.getStyleClass().removeAll("neaktivan");
        prekini.getStyleClass().add("neaktivan");
    }

    public void Prekini(ActionEvent actionEvent)
    {
        if(prekini.getStyleClass().size() == 0 && nit1.getState()== Thread.State.RUNNABLE)
        {
            nit1.stop();
        }
        else
        {
            Alert upozorenje = new Alert(Alert.AlertType.WARNING);
            upozorenje.setContentText("Ne mozete pritisnuti dugme prekini dok se ne pretrazuje");
            upozorenje.showAndWait();
        }
    }
}
