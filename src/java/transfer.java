/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
@WebServlet(urlPatterns = {"/transfer"})
public class transfer extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String toid = request.getParameter("toId"); //get the id to transfer the money to it from the form
            String amount = request.getParameter("amount"); //"" the amount of money """
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/project_schema?useSSL=false";
            Connection conn = DriverManager.getConnection(url, "root", "webproject");
            String bankid = request.getSession().getAttribute("AccountID").toString();
            int toID = Integer.parseInt(toid);
            int fromID = Integer.parseInt(bankid);
            float float_amount = Float.parseFloat(amount);
            PreparedStatement s1 = conn.prepareStatement("SELECT * FROM `bankaccount` where `BankAccountID`=? ;");
            s1.setInt(1, fromID);
            ResultSet res = s1.executeQuery(); //get the bank account record of the user to get his balance to check if he chas enought balance
            if (res.next()) {
                float mybalance = res.getFloat("BACurrentBalance");

                if (mybalance >= float_amount) {

                    PreparedStatement sql = conn.prepareStatement("SELECT * FROM `bankaccount` where `BankAccountID`=? ;");
                    sql.setInt(1, toID);
                    ResultSet Result = sql.executeQuery();
                    if (Result.next()) {
                        float new_my_balance = mybalance - float_amount;
                        PreparedStatement s2 = conn.prepareStatement("UPDATE `bankaccount` SET `BACurrentBalance` = ? WHERE BankAccountID = ?;");
                        s2.setFloat(1, new_my_balance);
                        s2.setFloat(2, fromID);
                        s2.executeUpdate();

                        float curr_balance = Result.getFloat("BACurrentBalance");

                        float new_balance = curr_balance + float_amount;
                        PreparedStatement sql2 = conn.prepareStatement("UPDATE `bankaccount` SET `BACurrentBalance` = ? WHERE BankAccountID = ?;");
                        sql2.setFloat(1, new_balance);
                        sql2.setFloat(2, toID);
                        sql2.executeUpdate();
                        Random random = new Random();
                        int randomID = random.nextInt(1000);
                        String query = "INSERT INTO `banktransaction`(`BankTransactionID`,`BTCreationDate`,`BTAmount`,`BTFromAccount`,`BTToAccount`) VALUES (?,current_date(),?,?,?);";
                        PreparedStatement stat = conn.prepareStatement(query);
                        stat.setInt(1, randomID);
                        stat.setFloat(2, float_amount);
                        stat.setInt(3, fromID);
                        stat.setInt(4, toID);
                        stat.execute();
                        String message = "transaction transfered successfully";

                        request.setAttribute("message", message);
                        request.getRequestDispatcher("transactions.jsp").forward(request, response);
                    } else {
                        String message2 = "transfering failled: check the id you entered maybe this id not stored in the database ";

                        request.setAttribute("message2", message2);
                        request.getRequestDispatcher("transactions.jsp").forward(request, response);
                    }
                } else {
                    String message2 = "transfering failled :check you balance maybe it is not enough ";

                    request.setAttribute("message2", message2);
                    request.getRequestDispatcher("transactions.jsp").forward(request, response);
                }

            }

        } catch (ClassNotFoundException | SQLException theException) {

            System.err.println(theException);

        }
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
