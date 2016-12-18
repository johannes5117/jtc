package com.johannes.lsctic.settings;

import com.johannes.lsctic.LicenseVerification;
import com.johannes.lsctic.OptionsStorage;
import com.johannes.lsctic.deployment.Intern;
import com.johannes.lsctic.deployment.SqlLiteDeployment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author johannesengler
 *
 * Example key for 3 Interns: 039060098005330738313
 */
public class DeploymentSettingsField extends SettingsField {

    ArrayList<Intern> interns;

    public DeploymentSettingsField(OptionsStorage storage) {
        super("Deployment", storage);
    }

    @Override
    public void expand() {
        final VBox v = new VBox();
        v.setSpacing(3);
        VBox.setMargin(v, new Insets(6, 0, 3, 0));
        final TextField f = new TextField();
        f.setPromptText("Lizenzcode (Beispiel: 1909213145910)");
        final TextField f2 = new TextField();
        f2.setPromptText("Anzahl gekaufter Lizenzen (Beispiel: 10)");

        Button b = new Button();
        b.setOnAction((ActionEvent event) -> {
            interns = loadCSV((Stage) v.getParent().getScene().getWindow());
        });
        b.setText("Deployment Datei");

        Button b2 = createButton(f, f2, v);
        b.setMaxWidth(Double.MAX_VALUE);
        b2.setMaxWidth(Double.MAX_VALUE);
        v.getChildren().addAll(f, f2, b, b2);
        this.getChildren().add(v);
        super.expand();

    }

    @Override
    public void collapse() {
        this.getChildren().remove(this.getChildren().size() - 1);
        super.collapse();
    }

    public static ArrayList<Intern> loadCSV(Stage stage) {
        ArrayList<Intern> out = new ArrayList();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Mitarbeiter Liste (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] splittet = line.split(";");
                    out.add(new Intern(splittet[0], Integer.valueOf(splittet[1])));
                }
            } catch (FileNotFoundException e) {
                Logger.getLogger("DeploymentSettingsField/loadCsv").info(e.getMessage());
            } catch (IOException e) {
                Logger.getLogger("DeploymentSettingsField/loadCsv").info(e.getMessage());
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        Logger.getLogger("DeploymentSettingsField/loadCsv").info(e.getMessage());
                    }
                }
            }

            return out;
        }
        return null;
    }

    public Button createButton(TextField f, TextField f2, VBox v) {
        Button b2 = new Button();
        b2.setOnAction((ActionEvent event) -> {
            getStorage().accept();
            String g = f.getText().trim();
            LicenseVerification veri = new LicenseVerification(g);
            int i = 0;
            try {
                i = Integer.valueOf(f2.getText().trim());
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).info(e.getMessage());
            }
            if (i == veri.getLicenseCount() & veri.checkValid()) {
                Logger.getLogger(getClass().getName()).info("Lizenz ist gültig");
            } else {
                Logger.getLogger(getClass().getName()).info("Lizenz ist nicht gültig");
            }
            boolean activated = veri.checkValid();
            DirectoryChooser c = new DirectoryChooser();
            File d = c.showDialog((Stage) v.getParent().getScene().getWindow());
            if (d != null) {
                interns.stream().forEach((mit) -> {
                    boolean success = (new File(d.getAbsolutePath() + "/" + mit.getName() + " (" + mit.getExtension() + ")")).mkdirs();
                    if (!success) {
                        Logger.getLogger(getClass().getName()).info("Konnte einen Ordner nicht erstellen");
                    }
                    SqlLiteDeployment sql = new SqlLiteDeployment(d.getAbsolutePath() + "/" + mit.getName() + " (" + mit.getExtension() + ")/settingsAndData.db");
                    try {
                        sql.writeInternsToDatabase(interns);
                    } catch (SQLException ex) {
                        Logger.getLogger(getClass().getName()).info("Konnte nicht in die Interns Datenbank schreiben");
                        Logger.getLogger(getClass().getName()).info(ex.getMessage());
                    }
                    HashMap<String, String> settings = new HashMap<>();
                    settings.put("amiAdress", getStorage().getAmiAdress());
                    settings.put("amiServerPort", "" + getStorage().getAmiServerPort());  //AMI Server Port
                    settings.put("amiLogIn", getStorage().getAmiLogIn());  //AMI LogingetStorage().getAmiLogIn());  //AMI Login
                    settings.put("amiPassword", getStorage().getAmiPassword());  //AMI Password
                    settings.put("ldapAdress", getStorage().getLdapAdress());  //LDAP Server Adresse
                    settings.put("ldapServerPort", "" + getStorage().getLdapServerPort());  //LDAP Server Port
                    settings.put("ldapSearchBase", getStorage().getLdapSearchBase());  //LDAP Suchbasis
                    settings.put("ldapBase", getStorage().getLdapBase());  //LDAP Basis
                    settings.put("ownExtension", "" + mit.getExtension());  // ownExtension
                    settings.put("activated", "" + activated);  // Aktiv
                    settings.put("time", "" + System.currentTimeMillis());  // time
                    try {
                        sql.writeSettingsToDatabase(settings);
                    } catch (SQLException ex) {
                        Logger.getLogger(getClass().getName()).info("Konnte nicht in die Interns Datenbank schreiben");
                        Logger.getLogger(getClass().getName()).info(ex.getMessage());
                    }
                });

            }
        });
        b2.setText("Start");
        return b2;
    }
}
