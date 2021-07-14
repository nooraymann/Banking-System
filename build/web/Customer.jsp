<%-- 
    Document   : Customer
    Created on : Dec 23, 2020, 6:51:43 PM
    Author     : Ayman
--%>

<%@page import="java.sql.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Customer home</title>
        <style>

            body {
                background-image: url('money.jpg');
                background-repeat: no-repeat;
                background-size: cover;

            }
            .balance{

                position:absolute; 
                left:140px; 
                top:150px;
                color: white;
                font-size:30px;
                font-family: Helvetica;
            }
            .failed{

                position:absolute; 
                left:140px; 
                top:150px;
                color: white;
                font-size:30px;
                font-family: Helvetica;

            }
            .button{position:absolute; 
                    left:240px; 
                    top:200px;
                    width: 13em;
                    width:9em;
                    border-radius:25px; 
                    border:2px ;
                    padding:11px;
            }

            .welc{
                position:absolute; 
                left:140px; 
                top:100px;
                color: white;
                font-size:30px;
                font-family: Helvetica;
            }
        </style>
    </head>
    <body>

        <%
            String id = request.getSession().getAttribute("IdSession").toString();

            ResultSet result = null;
            try {

                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/project_schema?useSSL=false";

                Connection conn = DriverManager.getConnection(url, "root", "webproject");
                Statement stat = conn.createStatement();
                result = stat.executeQuery("Select* FROM `bankaccount`");
                int found = 0;
                while (result.next()) {
                    if (id.equals(result.getString("CustomerID"))) { //show the account id its balance

        %><span class="welc"><%out.println("Account number : " + result.getInt("BankAccountID"));
           session.setAttribute("AccountID", result.getInt("BankAccountID"));%></span>
        <span class="balance"><%out.println("Your balance : " + result.getFloat("BACurrentBalance") + " EGP");%></span>
        <form action="transactions.jsp" method="post">
            <input class="button" type="submit" value="Transactions">
        </form>
        <%  found = 1;
                    break;
                }
            }
            if (found == 0) {

        %><span class="failed"><%out.println("You do not have account");%></span>


        <form action="AddAccount" method="post">
            <input class="button" type="submit" value="Add account">
        </form>
        <%

                }

            } catch (Exception excep) {

                System.err.println(excep);

            }

        %>
    </body>
</html>
