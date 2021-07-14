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
import java.sql.Statement;
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
@WebServlet(urlPatterns = {"/cancel"})
public class cancel extends HttpServlet {

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
            String id = request.getParameter("transid"); //to get the transaction id that entered
            String bankid = request.getSession().getAttribute("AccountID").toString(); // the account id of the current user
            Statement today = conn.createStatement();
            ResultSet todayResult = today.executeQuery("select current_date() AS today_date;"); //get today date
            Statement yesterday = conn.createStatement();
            ResultSet yesterdayResult = yesterday.executeQuery("SELECT DATE_SUB(CURDATE(), INTERVAL 1 DAY) AS yesterday_date;");//get yesterday date

            PreparedStatement sql = conn.prepareStatement("SELECT * FROM banktransaction where `BankTransactionID`=? ;");
            sql.setString(1, id);
            ResultSet Result = sql.executeQuery();
            if (Result.next()) {
                Float amount = Result.getFloat("BTAmount");
                if (todayResult.next() && yesterdayResult.next()) {/*check if the date of the transaction is today or yesterday to be able to cancel*/
                    if ((Result.getString("BTCreationDate").equals(todayResult.getString("today_date"))) || (Result.getString("BTCreationDate").equals(yesterdayResult.getString("yesterday_date")))) {
                        PreparedStatement stat = conn.prepareStatement("DELETE from `banktransaction` where `BankTransactionID` = ? and `BTFromAccount`=?");
                        stat.setString(1, id);
                        stat.setString(2, bankid);
                        stat.execute();
                        String to = Result.getString("BTToAccount");
                        int toID = Integer.parseInt(to);
                        PreparedStatement s1 = conn.prepareStatement("SELECT * FROM `bankaccount` where `BankAccountID`=? ;");
                        s1.setString(1, bankid);
                        ResultSet res = s1.executeQuery();
                        if (res.next()) {
                            Float balance = res.getFloat("BACurrentBalance");
                            Float new_balance = balance + amount;
                            PreparedStatement s2 = conn.prepareStatement("UPDATE `bankaccount` SET BACurrentBalance = ? WHERE BankAccountID = ?;");
                            s2.setFloat(1, new_balance);
                            s2.setString(2, bankid);
                            s2.executeUpdate();
                            PreparedStatement s = conn.prepareStatement("SELECT * FROM `bankaccount` where `BankAccountID`=? ;");
                            s.setInt(1, toID);
                            ResultSet R = s.executeQuery();
                            if (R.next()) {
                                float balance2 = R.getFloat("BACurrentBalance");
                                Float new_balance2 = balance2 - amount;
                                PreparedStatement sql2 = conn.prepareStatement("UPDATE `bankaccount` SET BACurrentBalance = ? WHERE BankAccountID = ?;");
                                sql2.setFloat(1, new_balance2);
                                sql2.setInt(2, toID);
                                sql2.executeUpdate();

                            }
                        }
                        String message3 = "transaction cancelled successfully";

                        request.setAttribute("message3", message3);
                        request.getRequestDispatcher("transactions.jsp").forward(request, response);
                    } else {
                        String message4 = "Err : Cancellation failed, check the id from the below table or this transaction is  too old";

                        request.setAttribute("message4", message4);
                        request.getRequestDispatcher("transactions.jsp").forward(request, response);

                    }
                }

            } else {
                String message4 = "Error : you cannot cancel this transaction";

                request.setAttribute("message4", message4);
                request.getRequestDispatcher("transactions.jsp").forward(request, response);

            }

            conn.close();

        } catch (ClassNotFoundException | SQLException theException) {

            System.err.println(theException);

        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
