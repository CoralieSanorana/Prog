package Affichage;

import java.io.*;
import java.util.*;


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
    public Map<String, String> lire(String fichier){
        Map<String, String> donnees = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                for(String part : parts){
                    String[] paire = part.split(":", 2);
                    if (paire.length == 2) {
                        String cle = paire[0].trim();
                        String valeur = paire[1].trim();
                        if (cle.contains("Affichage.")) {
                            cle = cle.substring(cle.lastIndexOf(".") + 1).toLowerCase();
                        }
                        donnees.put(cle, valeur);
                    }
                }
            }
        }catch (Exception e) {
            System.err.println("Erreur lors de la lecture du fichier "+fichier);
        }
        return donnees;
    }

}
