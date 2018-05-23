/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author gr005972
 */
public class CursoServlet extends HttpServlet{
    
    private String lista;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       resp.setContentType("text/html");
        
        String id = req.getParameter("id");
        String curso = req.getParameter("nome");
        String turno = req.getParameter("turno");
        String quantidadeEstudantes = req.getParameter("quantidadeEstudante");
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date dataCadastro = new java.util.Date();
        
        try {
             dataCadastro = f.parse(req.getParameter("dataCadastro"));
        } catch (ParseException ex) {
            Logger.getLogger(CursoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        PrintWriter saida = resp.getWriter();
        
        try{
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/erpcolegio","erpcolegio","masterkey");
            PreparedStatement p = connection.prepareStatement("INSERT INTO CURSO(ID, NOME, TURNO, QUANTIDADE_ESTUDANTES, DATA_CADASTRO) VALUES (?,?,?,?,?)");
            p.setInt(1, Integer.parseInt(id));
            p.setString(2, curso);
            p.setString(3, turno);
            p.setInt(4, Integer.parseInt(quantidadeEstudantes));
            java.sql.Date d = new java.sql.Date (dataCadastro.getTime());
            p.setDate(5, d);
            p.execute();
            saida.println("Concluído");
        }catch (SQLException ex){
            throw new ServletException(ex);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter saida = resp.getWriter();
        
        try{
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/erpcolegio","erpcolegio","masterkey");
            PreparedStatement p = connection.prepareStatement("SELECT * FROM CURSO");
            ResultSet rs = p.executeQuery();
            lista = "<ul>";
            while (rs.next()) {
                lista += "<li>"+"Nome                  - "+rs.getString("NOME")+"</li>";
                lista += "<li>"+"Id                    - "+rs.getString("ID")+"</li>";
                lista += "<li>"+"Turno                 - "+rs.getString("TURNO")+"</li>";
                lista += "<li>"+"Quantidade Estudantes - "+rs.getString("QUANTIDADE_ESTUDANTES")+"</li>";
                lista += "<li>"+"Data Cadastro         - "+rs.getString("DATA_CADASTRO")+"</li>";
                lista += "<hr>";
            }
            lista += "</ul>"; 
            rs.close();
            p.close();
            saida.println(lista);
            System.out.println(lista);
        }catch (SQLException ex){
            throw new ServletException(ex);
        }
        
    }
    
}
