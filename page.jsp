<%@ page import="java.util.Vector" %>
<%@ page import="Affichage.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Page 2</title>
</head>
<body>
<h1>Reflection / Generalise
</h1>
<br/>
<%
    Fonction F = new Fonction();
    Vector<Object> liste = F.lire("C:/xampp/tomcat/webapps/Prog/sauvegarder.t");
    Liste l = new Liste(liste);
    PersonneLocalise pl = new PersonneLocalise();
    out.println(pl.construire_formulaire());
    out.println(l.construireHtmlTable());
    //Personne p = new Personne();
    /*tableau.add(new Personne("Jean","Jaques",  30));
    tableau.add(new Personne("Marie","Jeanne",  25));
    tableau.add(new Personne("Paul","Phoenix", 40));*/
//Localite loc = new Localite();
//    Voiture v = new Voiture();
//out.println(p.construireHtmlInsertComposant());
//out.println(loc.construireHtmlInsertComposant());

%>
</body>
</html>