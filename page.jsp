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
    PersonneLocalise pl = new PersonneLocalise();
    out.println(pl.construire_formulaire());
    //Personne p = new Personne();
    /* Vector tableau = new Vector();
    tableau.add(new Personne("Jean","Jaques",  30));
    tableau.add(new Personne("Marie","Jeanne",  25));
    tableau.add(new Personne("Paul","Phoenix", 40));*/
//Localite loc = new Localite();
//    Voiture v = new Voiture();
//out.println(p.construireHtmlInsertComposant());
//out.println(loc.construireHtmlInsertComposant());

%>
    <br>
    <h2>Tableau:</h2>
<%
    Vector<Object> liste = F.lire("C:/xampp/tomcat/webapps/Prog/sauvegarder.t");
    PersonneLocalise pl2 = new PersonneLocalise();
    pl2.setInfopers(new Personne("Jean","Jaques",  30));
    pl2.setLoc(new Localite());
    Vector tableau = new Vector();
    tableau.add(pl2);
    //tableau.add(new Personne("Marie","Jeanne",  25));
    //tableau.add(new Personne("Paul","Phoenix", 40));
    Liste l = new Liste(tableau);
    out.println(l.construireHtmlTable());
%>
</body>
</html>