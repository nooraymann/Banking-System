/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;

import java.sql.*;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ayman
 */
@WebServlet(urlPatterns = {"/AddAccount"})
public class AddAccount extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/project_schema?useSSL=false";

            Connection conn = DriverManager.getConnection(url, "root", "webproject");
            String id = request.getSession().getAttribute("IdSession").toString();
            int ID = Integer.parseInt(id);
            Random random = new Random(); //if he doent have an account give him initially a random id and balance of 1000 egp
            int randomID = random.nextInt(1000);
            float initial_balance = 1000;

            String query = "INSERT INTO `bankaccount`(`BankAccountID`,`BACreationDate`,`BACurrentBalance`,`CustomerID`) VALUES (?, current_date(),?,?);";
            PreparedStatement stat = conn.prepareStatement(query);
            stat.setInt(1, randomID);
            stat.setFloat(2, initial_balance);
            stat.setInt(3, ID);
            stat.execute();
            conn.close();

        } catch (ClassNotFoundException | SQLException theException) {

            System.err.println(theException);

        }
        response.sendRedirect("Customer.jsp");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
