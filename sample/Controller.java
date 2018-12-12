package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.File;

import javafx.application.Platform;

import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller
{
    public TextField podstring;
    public Button trazi;
    public ListView spisak;
    public File korijenski_direktorij;


    private ObservableList<String> lista_pregleda;

    public Controller()
    {
        korijenski_direktorij = new File(System.getProperty("user.home"));
        lista_pregleda = FXCollections.observableArrayList();
    }

    /*"initializ" metoda sluzi za databinding*/
    public void initialize()
    {
        spisak.setItems(lista_pregleda);
    }

    /*ubacuje u atribut observable list svaki fajl na kopu koji ima tekts ako sto je unesen u texfield*/
    private void IzvrsavanjePretrage(String put)
    {
        File trazeni = new File(put);

        if( trazeni.isDirectory() )
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
        else if(trazeni.isFile())
        {
            if(trazeni.getName().contains( podstring.toString() ))
            lista_pregleda.add(trazeni.getAbsolutePath());
        }
    }

    public void Pretrazi(ActionEvent actionEvent)
    {
        new Thread(()->{
                Platform.runLater(()-> {
                                            IzvrsavanjePretrage(korijenski_direktorij.getAbsolutePath());
                });
        }).start();
    }
}


 /*   private void pretraga(String put, String uzorak) {
        File file = new File(put);
        if (file.isDirectory()) {
            try {
                for (File file1 : file.listFiles()) {
                    pretraga(file1.getAbsolutePath(), uzorak);
                }
            } catch (Exception exception) {
            }
        }
        else if (file.isFile()) {
            if (file.getName().contains(uzorak)) {
                observabilnaLista.add(file.getAbsolutePath());
            }
        }
    }

    public void pretragaF(ActionEvent actionEvent) {
        new Thread(() -> {
            try {
                Platform.runLater(() -> pretraga(korijenskiDirektorij.getAbsolutePath(), tekst.getText()));
                //Thread.sleep(500);
            } catch (Exception e) {

            }
        }).start();
    }
}*/