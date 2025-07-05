<%@ page import="Affichage.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Fonction F = new Fonction();
    java.util.Enumeration<String> noms = request.getParameterNames();
    String info = "";
    while (noms.hasMoreElements()) {
        String champ = noms.nextElement();
        String valeur = request.getParameter(champ);
        info += champ + ":" + valeur;
        info += ";";
    }
    String message;
    try {
        F.sauvegarder(info);
        message = "Sauvegarde réussie !";
    } catch (Exception e) {
        message = "Erreur de sauvegarde : " + e.getMessage();
    }
    session.setAttribute("message", message);
    response.sendRedirect("page.jsp");
%>