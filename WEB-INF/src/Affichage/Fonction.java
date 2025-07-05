package Affichage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class Fonction {
   public void sauvegarder(String info) {
    String sauvegarde = "C:/xampp/tomcat/webapps/JSP_Coralie/sauvegarde.t";
    try (PrintWriter writer = new PrintWriter(new FileWriter(sauvegarde, true))) {
        writer.println(info);     
        System.out.println("Sauvegarde réussie !");
    } catch (IOException e) {
        System.out.println("Erreur de sauvegarde : " + e.getMessage());
    }
}
}
