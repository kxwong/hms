
<%@page import="java.util.Date"%>
<%@page import="Model.Facilitybooking"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Controller.DateToString"%>
<%@page import="Controller.Crypto"%>
<%@page import="java.util.List"%>
<%
    try {
        AccountManager accountManager = new AccountManager((EntityManager) session.getAttribute("mgr"));
        Account account = (Account) session.getAttribute("account");
        if (!account.equals(accountManager.findAccount(account.getUsername(), account.getPassword()))) {
            throw new Exception("Unauthorized account");
        }
        if (account.getLevel() != 3) {
            throw new Exception("Unauthorized account");
        }
        DateToString dateString = new DateToString();
        List<Facilitybooking> bookingList = (List) session.getAttribute("bookingList");

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">        
        <link rel="stylesheet" href="../../../managementSide/source/css/report.css">
        <title>Hostel Management System</title>
    </head>
    <%
    %>
    <body style="background-color:#525659;">
    <center>
        <div id="content" style="box-shadow: 0px 0px 10px 3px  #46494c;" class="reportContent">
            <div class="reportInside">
                <img src="../../../source/BaikinLogo.png" alt=""/>
                <h1>BAIKIN HOSTEL MANAGEMENT SYSTEM</h1><br/><br/>
                <u><h2>Facility Booking Transaction Report <%=dateString.ToStringMonth(bookingList.get(0).getBookTime().getMonth())%> <%=bookingList.get(0).getBookTime().getYear() + 1900%></h2></u>
                <table>
                    <thead>
                        <tr>
                            <th width="10%">Booked Date</th>
                            <th width="2%"></th>
                            <th width="10%">Booking ID</th>
                            <th width="2%"></th>
                            <th width="20%">Facility</th>
                            <th width="2%"></th>
                            <th width="10%">Booked By</th>
                            <th width="2%"></th>
                            <th width="10%">Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (int j = 0; j < bookingList.size(); j++) {
                                Facilitybooking booking = bookingList.get(j);
                        %>
                        <tr>
                            <td><%=j==0?dateString.ToFormatDate(bookingList.get(j).getBookTime()):dateString.IsSameDate(bookingList.get(j).getBookTime(), bookingList.get(j-1).getBookTime())?"":dateString.ToFormatDate(bookingList.get(j).getBookTime())%></td>
                            <td></td>
                            <td><%=booking.getBookingID()%></td>
                            <td></td>
                            <td><%=booking.getBookFacility().getDescription()%></td>
                            <td></td>
                            <td><%=booking.getBookBy().getHostellerID()%></td>
                            <td></td>
                            <td><%=booking.getStatus()%></td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                    <thead>
                        <tr>
                            <th width="10%"></th>
                            <th width="2%"></th>
                            <th width="10%"></th>
                            <th width="2%"></th>
                            <th width="20%">Total Booking</th>
                            <th width="2%"></th>
                            <th width="10%"></th>
                            <th width="2%"></th>
                            <th width="10%"><%=bookingList.size()%></th>
                        </tr>
                    </thead>
                </table>
                <h4 style="text-align: right">Generated on <%=new Date().toLocaleString()%></h4>
            </div>
        </div>
    </center>
</body>
<script>
    window.onload=PrintElem();

    function PrintElem()
    {
        var mywindow = window.open('', 'PRINT', 'height=768,width=1366');
        mywindow.document.write('<html><head><title>' + '</title>');
        mywindow.document.write('<link rel="stylesheet" href="../../../managementSide/source/css/report.css">');
        mywindow.document.write('</head><body><center><div class="reportContent">');
        mywindow.document.write(document.getElementById("content").innerHTML);
        mywindow.document.write('</div></center></body></html>');
        mywindow.document.close();
        mywindow.focus();
        mywindow.print();
        return true;
    }


</script>
</html>
<%
    } catch (Exception ex) {
        session.invalidate();
        session = request.getSession();
        session.setAttribute("error", ex.getLocalizedMessage());
        response.sendRedirect("/login/admin");
    }
%>