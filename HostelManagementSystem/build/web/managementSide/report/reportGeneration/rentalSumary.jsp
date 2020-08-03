
<%@page import="Model.Bill"%>
<%@page import="Model.BillManager"%>
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
        DateToString dateString = new DateToString();
        String time = (String) session.getAttribute("time");
        BillManager billManager = new BillManager((EntityManager) session.getAttribute("mgr"));
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
                <u><h2>Rental Issued Summary Report <%=time%></h2></u>
                <table>
                    <thead>
                        <tr>
                            <th width="5%">Month</th>
                            <th width="1%"></th>
                            <th style="text-align: right" width="13%">Issued Qty</th>
                            <th width="1%"></th>
                            <th style="text-align: right" width="18%">Issued Amount</th>
                            <th width="1%"></th>
                            <th style="text-align: right" width="11%">Paid Qty</th>
                            <th width="1%"></th>
                            <th style="text-align: right" width="18%">Paid Amount</th>
                            <th width="1%"></th>
                            <th style="text-align: right" width="12%">Unpaid Qty</th>
                            <th width="1%"></th>
                            <th style="text-align: right" width="18%">Unpaid Amount</th>
                        </tr>
                    </thead>
                    <%
                        int issuedBillCount = 0;
                        int paidBillCount = 0;
                        int unpaidBillCount = 0;
                        double issuedTotalAmount = 0;
                        double paidTotalAmount = 0;
                        double unpaidTotalAmount = 0;
                        for (int i = 0; i < 12; i++) {
                            List<Bill> billList = billManager.findAllByDateRange(dateString.ToStartMonth(i + 1 < 10 ? "0" + String.valueOf(i + 1) + "-" + time : String.valueOf(i + 1) + "-" + time), dateString.ToEndMonth(i + 1 < 10 ? "0" + String.valueOf(i + 1) + "-" + time : String.valueOf(i + 1) + "-" + time));
                            int issuedBill = billList.size();
                            int paidBill = 0;
                            int unpaidBill = 0;
                            double issuedAmount = 0;
                            double paidAmount = 0;
                            double unpaidAmount = 0;
                            for (int j = 0; j < billList.size(); j++) {
                                Bill bill = billList.get(j);
                                if (bill.getStatus().toLowerCase().equals("paid")) {
                                    paidBill++;
                                    paidAmount += bill.getTotalAmount().doubleValue();
                                } else {
                                    unpaidBill++;
                                    unpaidAmount += bill.getTotalAmount().doubleValue();
                                }
                            }
                            issuedAmount = paidAmount + unpaidAmount;
                            if (!billList.isEmpty()) {
                                issuedBillCount += issuedBill;
                                paidBillCount += paidBill;
                                unpaidBillCount += unpaidBill;
                                issuedTotalAmount += issuedAmount;
                                paidTotalAmount += paidAmount;
                                unpaidTotalAmount += unpaidAmount;
                    %>
                    <tbody>
                        <tr>
                            <td><%=dateString.ToStringMonth(i)%></td>
                            <td></td>
                            <td style="text-align: right"><%=issuedBill%></td>
                            <td></td>
                            <td style="text-align: right"><%=issuedAmount%></td>
                            <td></td>
                            <td style="text-align: right"><%=paidBill%></td>
                            <td></td>
                            <td style="text-align: right"><%=paidAmount%></td>
                            <td></td>
                            <td style="text-align: right"><%=unpaidBill%></td>
                            <td></td>
                            <td style="text-align: right"><%=unpaidAmount%></td>
                        </tr>
                    </tbody>
                    <%
                            }
                        }
                    %>
                    <thead>
                        <tr>
                            <th width="5%">Total: </th>
                            <th width="1%"></th>
                            <th style="text-align: right" width="13%"><%=issuedBillCount%></th>
                            <th width="1%"></th>
                            <th style="text-align: right" width="18%"><%=issuedTotalAmount%></th>
                            <th width="1%"></th>
                            <th style="text-align: right" width="11%"><%=paidBillCount%></th>
                            <th width="1%"></th>
                            <th style="text-align: right" width="18%"><%=paidTotalAmount%></th>
                            <th width="1%"></th>
                            <th style="text-align: right" width="12%"><%=unpaidBillCount%></th>
                            <th width="1%"></th>
                            <th style="text-align: right" width="18%"><%=unpaidTotalAmount%></th>
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