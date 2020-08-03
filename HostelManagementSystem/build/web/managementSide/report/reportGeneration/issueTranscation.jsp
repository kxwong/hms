
<%@page import="Model.Issue"%>
<%@page import="Model.IssueManager"%>
<%@page import="Model.FacilitybookingManager"%>
<%@page import="Model.Facility"%>
<%@page import="Model.FacilityManager"%>
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
        IssueManager issueManager = new IssueManager((EntityManager) session.getAttribute("mgr"));
        DateToString dateString = new DateToString();
        List<String> catList = (List) session.getAttribute("catList");
        String time = (String) session.getAttribute("time");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">        
        <link rel="stylesheet" href="../../../managementSide/source/css/report.css">
        <title>Hostel Management System</title>
    </head>
    <body style="background-color:#525659;">
    <center>
        <div id="content" style="box-shadow: 0px 0px 10px 3px  #46494c;" class="reportContent">
            <div class="reportInside">
                <img src="../../../source/BaikinLogo.png" alt=""/>
                <h1>BAIKIN HOSTEL MANAGEMENT SYSTEM</h1><br/><br/>
                <u><h2>Issue Transaction Report <%=dateString.ToStringMonth(dateString.ToStartMonth(time).getMonth())%> <%=dateString.ToStartMonth(time).getYear() + 1900%></h2></u>
                    <%
                        int total = 0;
                        for (int i = 0; i < catList.size(); i++) {
                            List<Issue> issueList = issueManager.findAllByDateRangeCategory(dateString.ToStartMonth(time), dateString.ToEndMonth(time), catList.get(i));
                    %>
                <table>
                    <br/><h3 style="text-align: left">Category: <%=catList.get(i)%></h3>
                    <thead>
                        <tr>
                            <th width="10%">Date</th>
                            <th width="4%"></th>
                            <th width="20%">Case No</th>
                            <th width="4%"></th>
                            <th width="20%">Issue Type</th>
                            <th width="4%"></th>
                            <th width="10%">Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            total += issueList.size();
                            for (int j = 0; j < issueList.size(); j++) {
                        %>
                        <tr>
                            <td><%=j == 0 ? dateString.ToFormatDate(issueList.get(j).getIssueDate()) : dateString.IsSameDate(issueList.get(j).getIssueDate(), issueList.get(j - 1).getIssueDate()) ? " " : dateString.ToFormatDate(issueList.get(j).getIssueDate())%></td>
                            <td></td>
                            <td><%=issueList.get(j).getCaseNo()%></td>
                            <td></td>
                            <td><%=issueList.get(j).getIssueType()%></td>
                            <td></td>
                            <td><%=issueList.get(j).getStatus()%></td>
                        </tr>
                        <%
                            }
                        %>
                        <tr>
                            <td colspan="4"></td>
                            <td>Sub Total Issue: </td>
                            <td></td>
                            <td><%=issueList.size()%></td>
                        </tr>
                    </tbody>
                    <%
                        }
                    %>
                    <thead>
                        <tr>
                            <th width="10%">Date</th>
                            <th width="4%"></th>
                            <th width="20%">Case No</th>
                            <th width="4%"></th>
                            <th width="20%">Total Issue:</th>
                            <th width="4%"></th>
                            <th width="10%"><%=total%></th>
                        </tr>
                    </thead>
                </table>
                <h4 style="text-align: right">Generated on <%=new Date().toLocaleString()%></h4>
            </div>
        </div>
    </center>
</body>
<script>
    window.onload = PrintElem();

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