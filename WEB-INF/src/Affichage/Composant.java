package Affichage;

import java.lang.reflect.Field;
import java.util.Vector;

public class Composant {
    private Vector<Object> data;

    public Vector<Object> getData() {
        return data;
    }

    public void setData(Vector<Object> data) {
        this.data = data;
    }

    public String construireHtmlInsertComposant()throws Exception{
        String html = "";
        Field[] fields = this.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            Class<?> type = f.getType();
            html += "<label>" + f.getName() + "</label> : ";
            if(f.getType().getName().contains("Affichage.")){
                html+="</br>";
                Class<? extends Composant> classModel = (Class<? extends Composant>) Class.forName(f.getType().getName());
                Composant instance = classModel.newInstance();
                html += instance.construireHtmlInsertComposant();
            }
            if (type.equals(String.class)) {
                html += "<input type='text' name='" + f.getName() + "' />\n";
            }
            if (type.equals(int.class) || type.equals(Integer.class)
                    || type.equals(double.class) || type.equals(Double.class)) {
                html += "<input type='number' name='" + f.getName() + "' />\n";
            }

            html+="</br>";
        }

        return html;
    }

    public String construireHtmlTable() {
        if (getData() == null || getData().isEmpty()) {
            System.out.println("Aucune donnée disponible pour construire la table");
            return "<table style='border-collapse: collapse; border: 1px solid black;'></table>";
        }

        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append("<table style='border-collapse: collapse; border: 1px solid black;'>\n");
        htmlTable.append("<tr>\n");

        // Ajouter les en-têtes de colonnes
        Field[] fields = getData().get(0).getClass().getDeclaredFields();
        for (Field field : fields) {
            htmlTable.append("<th style='border: 1px solid black; padding: 5px;'>")
                     .append(escapeHtml(field.getName()))
                     .append("</th>\n");
        }
        htmlTable.append("</tr>\n");

        // Ajouter les lignes de données
        for (Object data : getData()) {
            htmlTable.append("<tr>\n");
            for (Field field : fields) {
                field.setAccessible(true);
                htmlTable.append("<td style='border: 1px solid black; padding: 5px;'>");
                try {
                    if (Composant.class.isAssignableFrom(field.getType())) {
                        System.out.println("Champ spécial détecté: " + field.getName() + ", Type: " + field.getType().getName());
                        Composant component = (Composant) field.get(data);
                        if (component != null) {
                            String sousTable = component.construireHtmlTable();
                            System.out.println("Sous-table générée: " + sousTable);
                            htmlTable.append(sousTable);
                        } else {
                            System.out.println("Champ spécial est null pour: " + field.getName());
                            htmlTable.append("");
                        }
                    } else {
                        Object value = getValField(data, field);
                        System.out.println("Valeur champ non spécial: " + field.getName() + " = " + value);
                        htmlTable.append(value);
                    }
                } catch (IllegalAccessException e) {
                    System.err.println("Erreur d'accès au champ " + field.getName() + ": " + e.getMessage());
                    htmlTable.append("Erreur d'accès au champ");
                } catch (Exception e) {
                    System.err.println("Erreur inattendue pour le champ " + field.getName() + ": " + e.getMessage());
                    htmlTable.append("Erreur: ").append(escapeHtml(e.getMessage()));
                }
                htmlTable.append("</td>\n");
            }
            htmlTable.append("</tr>\n");
        }
        htmlTable.append("</table>\n");

        return htmlTable.toString();
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
    /*public String construireHtmlTable(){
        if(getData()!=null && getData().size()>0) {
            String htmlTable = "";
            htmlTable += "<table border='1'>\n";
            htmlTable += "<tr>\n";

            // Ajouter les en-têtes de colonnes
            Field[] tableau_champ = getData().get(0).getClass().getDeclaredFields();
            for (int i = 0;i<tableau_champ.length;i++) {
                htmlTable += "<th>";
                htmlTable += tableau_champ[i].getName();
                htmlTable += "</th>\n";
            }
            htmlTable+="</tr>\n";

            // Ajouter les lignes de données
            for (int i = 0;i<getData().size();i++) {
                htmlTable+="<tr>\n";
                for (int j = 0 ; j < tableau_champ.length ; j++ ) {
                    Field f = tableau_champ[j];
                    f.setAccessible(true);
                    htmlTable+= "<td>";
                    if(f.getType().getName().contains("Affichage.")){
                        try {
                            Class<? extends Composant> classModel = (Class<? extends Composant>) Class.forName(f.getType().getName());
                            Composant instance = classModel.newInstance();
                            htmlTable += instance.construireHtmlTable();
                        } catch (Exception e) {
                            htmlTable += " Erreur !!!! ";
                        }
                    } else{
                        htmlTable+=getValField(getData().get(i),f);
                    }
                    htmlTable+= "</td>\n";
                }
                htmlTable+="</tr>\n";
            }
            htmlTable+="</table>";

            return htmlTable;
        }
        return "";
    }*/

    public static String convertDebutMajuscule(String autre) {
        char[] c = autre.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }

    public static Object getValField(Object classe, Field f) {
        try {
            String nomMethode = "get" + convertDebutMajuscule(f.getName());
            Object o = classe.getClass().getMethod(nomMethode).invoke(classe);
            if (o == null)
                return "";
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }
    public String construire_formulaire(){
        String retour = "";
        retour += "<form action=\"traitement.jsp\" method=\"get\">";
        try {
            retour += this.construireHtmlInsertComposant();
        } catch (Exception e) {
            retour += "Erreur de la mise en formulaire !!!";
        }
        retour += "<input type=\"hidden\" name=\"class_type\" value='"+ this.getClass().getName() +"'/>\n";
        retour += "<input type=\"submit\" value=\"Valider\">";
        retour += "</form>";

        return retour;
    }
}
