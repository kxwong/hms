
<%@page import="Model.Entrecord"%>
<%@page import="java.util.Date"%>
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
        List<Entrecord> entRecordList = (List) session.getAttribute("entRecordList");

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
                <u><h2>Entrance Record Transaction Report <%=dateString.ToStringMonth(entRecordList.get(0).getAccessTime().getMonth())%> <%=entRecordList.get(0).getAccessTime().getYear() + 1900%></h2></u>
                <table>
                    <thead>
                        <tr>
                            <th width="14%">Accessed Date</th>
                            <th width="1%"></th>
                            <th width="14%" style="text-align: right">Accessed Time</th>
                            <th width="5%"></th>
                            <th width="14%" style="text-align: right">Accessed Card</th>
                            <th width="8%"></th>
                            <th width="14%">Accessed By</th>
                            <th width="2%"></th>
                            <th width="10%">Hostel</th>
                            <th width="2%"></th>
                            <th width="10%">Gate</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (int j = 0; j < entRecordList.size(); j++) {
                                Entrecord entrecord = entRecordList.get(j);
                        %>
                        <tr>
                            <td><%=j == 0 ? dateString.ToFormatDate(entRecordList.get(j).getAccessTime()) : dateString.IsSameDate(entRecordList.get(j).getAccessTime(), entRecordList.get(j - 1).getAccessTime()) ? "" : dateString.ToFormatDate(entRecordList.get(j).getAccessTime())%></td>
                            <td></td>
                            <td style="text-align: right"><%=dateString.DateTimeWithSecToTime(entrecord.getAccessTime())%></td>
                            <td></td>
                            <td style="text-align: right"><%=entrecord.getEntCardNo().getEntCardNo().split("_").length > 1 ? entrecord.getEntCardNo().getEntCardNo().split("_")[1] : entrecord.getEntCardNo().getEntCardNo()%></td>
                            <td></td>
                            <td><%=entrecord.getEntCardNo().getHosteller() == null ? entrecord.getEntCardNo().getVisitor().getVisitorID() : entrecord.getEntCardNo().getHosteller().getHostellerID()%></td>
                            <td></td>
                            <td><%=entrecord.getHostel().getHostelID()%></td>
                            <td></td>
                            <td><%=entrecord.getGate()%></td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                    <thead>
                        <tr>
                            <th width="14%"></th>
                            <th width="1%"></th>
                            <th width="14%" style="text-align: right"></th>
                            <th width="5%"></th>
                            <th width="14%" style="text-align: right"></th>
                            <th width="8%"></th>
                            <th width="14%"></th>
                            <th width="2%"></th>
                            <th width="10%">Total</th>
                            <th width="2%"></th>
                            <th width="10%"><%=entRecordList.size()%></th>
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