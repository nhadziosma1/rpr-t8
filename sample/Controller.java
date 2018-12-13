package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.File;

import javafx.application.Platform;

import javafx.scene.control.Button;
import javafx.scene.input.TouchEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

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

        prekini.setDisable(true);
        trazi.setDisable(false);


        spisak.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                FXMLLoader loader = new FXMLLoader( getClass().getResource("ProzorZaSlanje.fxml") );
                Parent root = null;

                try
                {
                    root = loader.load();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                Stage prozor_za_slanje = new Stage();
                prozor_za_slanje.setTitle("Prozor za slanje");

                prozor_za_slanje.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                prozor_za_slanje.show();
            }
        });

    }

    /*ubacuje u atribut observable list svaki fajl na kopu koji ima tekts ako sto je unesen u texfield*/
    private void IzvrsavanjePretrage(String put, String nalazilo_se_u_trenutku_slanja)
    {
        /*mora se dodati i ovaj parametar da bi korisnik mogao brisati sadrzaj text fielda,
         a da se svejedno trazi po onome sto je bilo u trenutku pritiska na trazi*/

        trazi.setDisable(false);
        prekini.setDisable(true);

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
        trazi.setDisable(true);
        prekini.setDisable(false);

        lista_pregleda.clear();

        nit1 = new Thread(()->{
                Platform.runLater(()-> {
                                            IzvrsavanjePretrage(korijenski_direktorij.getAbsolutePath(), podstring.getText());
                });
        });
        nit1.start();

        trazi.setDisable(false);
        prekini.setDisable(true);
    }

    public void Prekini(ActionEvent actionEvent)
    {
        if(prekini.isDisable() && nit1.getState() != Thread.State.RUNNABLE)
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
