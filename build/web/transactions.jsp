<%-- 
Document   : transactions
Created on : Dec 24, 2020, 3:30:05 PM
Author     : Ayman
--%>
<%@page import="java.sql.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>transactions</title>
        <style>
            body {
                background-image: url('money.jpg');
                background-repeat: no-repeat;
                background-size: cover;

            }
            .td{
                color:white;
                font-family: Helvetica;

            }
            .table{
                position: relative; 
                color: white;

            }
            .no_trans{
                position: relative; 
                color: white;

            }
            .list{

                position: relative; 
                color: white;
            }
            .button{position: relative; 
                    width: 6em;
                    border-radius:25px; 
                    border:2px ;
                    padding:7px;
            }
            .id{ position: relative; 
                 border-radius: 25px; 
                 border: 2px ; 
                 padding: 6px; 
                 width: 15em ;
            }
            .amount{
                position: relative;   
                border-radius: 25px; 
                border: 2px ; 
                padding: 6px;
                width: 15em }
            .transfer{
                position: relative; 
                color: white;
            }
            .error
            { 
                position: relative; 


                color:red;}
            .confirm
            { 
                position: relative; 
                color:green;
            }
            .cancel{
                position:relative; 
                color: white;    

            }
            .cid{
                position: relative; 

                border-radius: 25px; 
                border: 2px ; 
                padding: 6px; 
                width: 15em 
            }
            .cbutton{
                position: relative; 
                width: 6em;
                border-radius:25px; 
                border:2px ;
                padding:7px;
            }


        </style>
    </head>
    <body>
        <%
            String id = request.getSession().getAttribute("IdSession").toString();
            String bankid = request.getSession().getAttribute("AccountID").toString();
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/project_schema?useSSL=false";

                Connection conn = DriverManager.getConnection(url, "root", "webproject");
                PreparedStatement stat = conn.prepareStatement("SELECT * FROM `banktransaction` where BTFromAccount=? OR BTToAccount=?;");
                stat.setString(1, bankid);
                stat.setString(2, bankid);

                ResultSet result = stat.executeQuery();// this query to show the transaction table


        %>
    <center>  <h2 class="list">Transactions </h2></center>

    <table class="table" border="1">
        <tr>

            <th class="td">ID</th>
            <th class="td">Creation date</th>
            <th class="td">Amount</th>
            <th class="td">From account no.</th>
            <th class="td">To account no.</th>

        </tr>

        <%           int found = 0;
            while (result.next()) {
                found = 1;
        %>
        <tr>
            <td class="td"> <%out.println(result.getInt("BankTransactionID"));%></td>
            <td class="td"> <%out.println(result.getString("BTCreationDate"));%></td>
            <td class="td"> <%out.println(result.getFloat("BTAmount"));%></td>
            <td class="td"> <%out.println(result.getInt("BTFromAccount"));%></td>
            <td class="td"> <%out.println(result.getInt("BTToAccount"));%></td>

        </tr>
        <%
            }
            if (found == 0) {%><span class="no_trans"><%out.println("You have no transactions to view");
                    }%></span>
        <h3 class="transfer"> To transfer transaction : </h3>
        <form action="transfer" method="post" >
            <input class="id" type="number" placeholder="Enter account ID to transfere" name="toId">
            <br><br>
            <input class="amount" type="number" placeholder="Enter amount" name="amount">
            <br><br>
            <input class="button" type="submit" value="Transfer">
        </form>
        <p class="confirm">${message}</p>
        <c:remove var="message" scope="session" /> 
        <p class="error">${message2}</p>
        <c:remove var="message2" scope="session" />
        <h3 class="cancel"> To cancel transaction : </h3>
        <form action="cancel" method="post" >
            <input class="cid" type="number" placeholder="Enter account ID to cancel" name="transid">
            <br><br>
            <input class="cbutton" type="submit" value="Cancel">
        </form>
        <p class="confirm">${message3}</p>
        <c:remove var="message3" scope="session" /> 
        <p class="error">${message4}</p>
        <c:remove var="message4" scope="session" />
        <h3 class="list">Transaction List</h3>
        <%

            } catch (Exception theException) {

                System.err.println(theException);

            }


        %>
    </table>
</body>
</html>
