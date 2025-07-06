package Affichage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class Fonction {
    public void sauvegarder(String info){
        String sauvegarde = "C:/xampp/tomcat/webapps/Prog/sauvegarder.t";
        try (PrintWriter writer = new PrintWriter(new FileWriter(sauvegarde, true))) {
            writer.println(info);     
            System.out.println("Sauvegarde réussie !");
        } catch (IOException e) {
            System.out.println("Erreur de sauvegarde : " + e.getMessage());
        }
    }
    
    public Vector<Object> lire(String cheminFichier) {
        Vector<Object> objets = new Vector<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                // Ignorer les lignes vides
                if (ligne.trim().isEmpty()) {
                    continue;
                }
                // Parser la ligne en un objet
                Object objet = ligne_Object(ligne);
                if (objet != null) {
                    objets.add(objet);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier : " + e.getMessage());
        }
        return objets;
    }

    private Object ligne_Object(String ligne) {
        try {
            Map<String, String> donnees = separer_ligne(ligne);
            String nomClasse = donnees.get("class_type");

            Class<?> classe = Class.forName(nomClasse);
            Object instance = classe.getDeclaredConstructor().newInstance();


            Field[] fields_classe = classe.getDeclaredFields();
            for (Field champ : fields_classe) {
                champ.setAccessible(true);
                String nomChamp = champ.getName();
                String valeur = donnees.get(nomChamp);

                if (valeur == null || nomChamp.equals("btn")) {
                    continue;
                }

                Class<?> typeChamp = champ.getType();
                if (typeChamp.equals(String.class)) {
                    champ.set(instance, valeur);
                } else if (typeChamp.equals(int.class) || typeChamp.equals(Integer.class)) {
                    champ.set(instance, Integer.parseInt(valeur));
                } else if (typeChamp.equals(double.class) || typeChamp.equals(Double.class)) {
                    champ.set(instance, Double.parseDouble(valeur));
                } else if (typeChamp.getName().contains("Affichage.")) {
                    Object instanceSousClasse = Class.forName(typeChamp.getName())
                            .getDeclaredConstructor()
                            .newInstance();
                    Field sousChamp = typeChamp.getDeclaredFields()[0]; 
                    sousChamp.setAccessible(true);
                    if (sousChamp.getType().equals(int.class)) {
                        sousChamp.set(instanceSousClasse, Integer.parseInt(valeur));
                    } else {
                        sousChamp.set(instanceSousClasse, valeur);
                    }
                    champ.set(instance, instanceSousClasse);
                }
            }
            return instance;
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'objet à partir de la ligne : " + ligne);
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, String> separer_ligne(String ligne) {
        Map<String, String> donnees = new HashMap<>();
        String[] paires = ligne.split(";");
        for (String paire : paires) {
            if (paire.isEmpty()) {
                continue;
            }
            String[] parts = paire.split(":", 2);
            if (parts.length == 2) {
                String cle = parts[0].trim();
                String valeur = parts[1].trim();
                /*if (cle.contains("Affichage.")) {
                    cle = cle.substring(cle.lastIndexOf(".") + 1).toLowerCase();
                }*/
                donnees.put(cle, valeur);
            }
        }
        return donnees;
    }

}
