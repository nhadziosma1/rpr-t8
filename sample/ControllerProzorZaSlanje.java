package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

//import org.apache.commons.validator.routines.EmailValidator;

public class ControllerProzorZaSlanje implements Initializable {

    public TextField tfIme;
    public TextField tfPrezime;
    public ComboBox cbGrad;
    public TextField tfAdresa;
    public TextField tfEmail;
    public TextField tfPostanskiBroj;

    @Override          /*pokrece se odmah pri kreiranju*/
    public void initialize(URL location, ResourceBundle resources)
    {
        cbGrad.setItems(FXCollections.observableArrayList("Sarajevo", "Tuzla", "Mostar", "Brcko", "Trebinje", "Zenica", "Banja Luka", "Novi Pazar", "Foca"));
        cbGrad.getSelectionModel().selectFirst();

        tfIme.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean o, Boolean n) {
                if (!n) {
                    if (validnoImePrezime(tfIme.getText())) {
                        tfIme.getStyleClass().removeAll("poljeNijeIspravno");
                        tfIme.getStyleClass().add("poljeIspravno");
                    } else {
                        tfIme.getStyleClass().removeAll("poljeIspravno");
                        tfIme.getStyleClass().add("poljeNijeIspravno");
                    }
                }
            }
        });
        tfPrezime.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean o, Boolean n) {
                if (!n) {
                    if (validnoImePrezime(tfPrezime.getText())) {
                        tfPrezime.getStyleClass().removeAll("poljeNijeIspravno");
                        tfPrezime.getStyleClass().add("poljeIspravno");
                    } else {
                        tfPrezime.getStyleClass().removeAll("poljeIspravno");
                        tfPrezime.getStyleClass().add("poljeNijeIspravno");
                    }
                }
            }
        });

        tfEmail.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean o, Boolean n) {
                if (!n) {
                    if (validanEmail(tfEmail.getText())) {
                        tfEmail.getStyleClass().removeAll("poljeNijeIspravno");
                        tfEmail.getStyleClass().add("poljeIspravno");
                    } else {
                        tfEmail.getStyleClass().removeAll("poljeIspravno");
                        tfEmail.getStyleClass().add("poljeNijeIspravno");
                    }
                }
            }
        });

        /*tfEmail.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean o, Boolean n) {
                EmailValidator validator = EmailValidator.getInstance();
                if (!n)
                {
                    if (validator.isValid(tfEmail.getText())
                    {
                        tfEmail.getStyleClass().removeAll("poljeNijeIspravno");
                        tfEmail.getStyleClass().add("poljeIspravno");
                    }
                    else {
                        tfEmail.getStyleClass().removeAll("poljeIspravno");
                        tfEmail.getStyleClass().add("poljeNijeIspravno");
                    }
                }
            }
        });*/

        new Thread( ()-> {
            tfPostanskiBroj.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> obs, Boolean o, Boolean n) {
                    if (!n) {
                        if (validanPostanskiBroj(tfPostanskiBroj.getText())) {
                            tfPostanskiBroj.getStyleClass().removeAll("poljeNijeIspravno");
                            tfPostanskiBroj.getStyleClass().add("poljeIspravno");
                        } else {
                            tfPostanskiBroj.getStyleClass().removeAll("poljeIspravno");
                            tfPostanskiBroj.getStyleClass().add("poljeNijeIspravno");
                        }
                    }
                }
            });

        }).start();

        cbGrad.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean o, Boolean n) {
                if (!n) {
                    if (validanGrad(cbGrad.getValue().toString()))
                    {
                        cbGrad.getStyleClass().removeAll("poljeNijeIspravno");
                        cbGrad.getStyleClass().add("poljeIspravno");
                    }
                    else {
                        cbGrad.getStyleClass().removeAll("poljeIspravno");
                        cbGrad.getStyleClass().add("poljeNijeIspravno");
                    }
                }
            }
        });

        tfAdresa.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean o, Boolean n) {
                if (!n) {
                    if (validnaAdresa(tfAdresa.getText())) {
                        tfAdresa.getStyleClass().removeAll("poljeNijeIspravno");
                        tfAdresa.getStyleClass().add("poljeIspravno");
                    } else {
                        tfAdresa.getStyleClass().removeAll("poljeIspravno");
                        tfAdresa.getStyleClass().add("poljeNijeIspravno");
                    }
                }
            }
        });

    }///////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean validanEmail(String rijec)
    {
        if (rijec.contains("@") == false)
            return false;

        String prije_la = rijec.substring(0, rijec.indexOf('@'));
        String poslije_la = rijec.substring(rijec.indexOf('@'));


        if (poslije_la.contains(".") == false)
            return false;

        String extenzija = poslije_la.substring(poslije_la.indexOf('.'));

        extenzija = extenzija.substring(1);

        if (extenzija.matches("[a-z]+") == false)
            return false;

        return true;
    }

    private boolean validanGrad(String rijec)
    {
        /*kada stoji bez plusa podrazumijevalo bi se da se string "rijec" sastoji od jednog karaktera koji je u zadanom rasponu,
          dok sa plusaom podrazumijevamo da je jedan karakter ili vise, dok bi sa "*" i string koji nema nijedan iz intervala vratio true*/
        if (rijec.matches("[a-zA-Z]+") == false)
            return false;

        if (Character.isUpperCase(rijec.charAt(0)) == false)
            return false;

        for (int i = 1; i < rijec.length(); i++) {
            if (Character.isUpperCase(rijec.charAt(i)) == true)
                return false;
        }
        return true;
    }

    private boolean validnaAdresa(String rijec)
    {
        if (rijec.length() == 0)
            return false;

        if (rijec.contains(" ") == false)
            return false;

        String ime_ulice = rijec.substring(0, rijec.indexOf(' '));
        String broj_ulice = rijec.substring(rijec.indexOf(' ') + 1, rijec.length());

        if (ime_ulice.matches("[a-zA-Z]+") == false || broj_ulice.matches("[0-9]+") == false)
            return false;

        return true;
    }

    private boolean validnoImePrezime(String rijec)
    {
        if (rijec.length() == 0 || rijec.length() > 20)
            return false;

        if (rijec.matches("[a-zA-Z]+") == false)
            return false;

        if (Character.isLowerCase(rijec.charAt(0)))
            return false;

        for (int i = 1; i < rijec.length(); i++) {
            if (Character.isUpperCase(rijec.charAt(i)))
                return false;
        }

        return true;
    }

    private boolean validanPostanskiBroj(String rijec)
    {
        try
        {
            String link = new String("http://c9.etf.unsa.ba/proba/postanskiBroj.php?postanskiBroj=");
            URL link_url = new URL(link+tfPostanskiBroj.getText().trim());

            BufferedReader ulaz = new BufferedReader(new InputStreamReader(link_url.openStream(), StandardCharsets.UTF_8));

            String sadrzaj = ulaz.readLine(); //posto i ima fajl samo jednu liniju teksta

            if(sadrzaj.equals("OK"))
            return true;
            else
                return false;

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return true;
    }

    private void upozori(ActionEvent actionEvent) {
        // LINK, procitaj: 1. https://code.makery.ch/blog/javafx-dialogs-official/
        //                 2. https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Greška");
        alert.setHeaderText("Forma nije validna!");
        alert.setContentText("Unesite ispravne podatke!");
        alert.showAndWait();
    }

    public void potvrdi(ActionEvent actionEvent) {
        if (!jeLiSveValidno()) {
            upozori(actionEvent);
        }
        else {

        }

    }

    private boolean jeLiSveValidno() {
        if (validnoImePrezime(tfIme.getText()) == false || validnoImePrezime(tfPrezime.getText()) == false ||
                tfEmail.getText().length() == 0)
            return false;

        ArrayList<ObservableList<String>> validnost = new ArrayList<>();

        validnost.add(tfIme.getStyleClass());
        validnost.add(tfPrezime.getStyleClass());
        validnost.add(tfAdresa.getStyleClass());
        validnost.add(tfEmail.getStyleClass());
        validnost.add(cbGrad.getStyleClass());

        for (ObservableList<String> o : validnost) {
            if (o.contains("poljeNijeIspravno"))
                return false;
        }
        return true;
    }

}