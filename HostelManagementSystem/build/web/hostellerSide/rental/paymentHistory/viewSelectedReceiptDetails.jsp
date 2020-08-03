<%@page import="Controller.Crypto"%>
<%@page import="Model.Hosteller"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Billitem"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Model.Receipt"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/rental/viewSelectedReceiptDetails.css">
        <title>Hostel Management System</title>
    </head>
    <%

        Receipt selectedReceipt = new Receipt();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMMMMMM yyyy");
        List<Billitem> itemList = new ArrayList<Billitem>();
        Hosteller hosteller = new Hosteller();
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("selectedReceipt") == null) {
                throw new Exception("An error has occured, please try again.");
            } else {
                selectedReceipt = (Receipt) session.getAttribute("selectedReceipt");

                if (session.getAttribute("curHosteller") == null) {
                    throw new Exception("An error has occured, please re-login.");
                } else {
                    hosteller = (Hosteller) session.getAttribute("curHosteller");
                }

                itemList = selectedReceipt.getBill().getBillitemList();
            }
    %>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>             
                <a href='/retrieveCurrentBillingList'><div class="hBG"><div class="hOption "><p>Current Billing</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveMonthlySummary'><div class="hBG"><div class="hOption hChoose"><p>Payment History</p></div></div></a>   
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <div style="float:right;">
                    <p style="position:fixed;right:60px; font-size: 20px; color: white;"><%=hosteller.getFirstName()%> <%=hosteller.getMiddleName()%> <%=hosteller.getLastName()%></p>
                    <p style="position:fixed; right:15px;"><a href="/hostellerLogout"><img class="logoutIcon" title="Logout" style="width: 35px; height: 35px;" onclick="logout()" src="../../../hostellerSide/source/logout_icon.png"></a></p>                   
                </div>
            </div>
        </div>
        <div class="sidePanel">
            <div class="spCover">
                <a href='/retrieveCurrentRoomStatus'><div class="spoRoom spoBg"><div class="spOption"><center><div class="spImg" ></div><p>Room</p></center></div></div></a>               
                <a href='/retrieveHosteller'><div class="spoTenant spoBg"><div class="spOption "><center><div class="spImg"></div></center>Profile</div></div></a>
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveCurrentBillingList'><div class="spoRental spoBg"><div class="spOption spoChoose"><center><div class="spImg" style="background-image:url(../../../hostellerSide/source/s_rental.png)"></div></center>Rental</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveAllFacilitiesWithinBranch'><div class="spoFacilities spoBg"><div class="spOption"><center><div class="spImg"  ></div></center>Facility</div></div></a>            
                <a href='/retrieveAllAnnouncement'><div class="spoNotice spoBg"><div class="spOption "><center><div class="spImg"></div></center>Notice</div></div></a>       
                <a href='/retrieveAllCase'><div class="spoReporting spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Report</div></div></a>            
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle"><a href="/retrieveMonthlySummary">Monthly Payment Summary</a> > <a href="/hosteller/rental/viewPaymentHistory">Selected Month</a> > Selected Receipt</div>
            </div>
            <div class="cBody">              

                <div class="cbContent contentBilling">

                    <div class="billingHeader">
                        <table>
                            <caption>Billing for <%=selectedReceipt.getDescription()%></caption>
                            <tr><td class="labelTd">Receipt number : </td><td><%=selectedReceipt.getReceiptNo()%></td></tr>
                            <tr><td class="labelTd">Generated on : </td><td><%=simpleDateFormat.format(selectedReceipt.getGenerateDate())%></td></tr>
                            <tr><td class="labelTd">Bill number : </td><td><%=selectedReceipt.getBill().getBillNo()%></td></tr>
                            <tr><td class="labelTd">Issue Date : </td><td><%=simpleDateFormat.format(selectedReceipt.getBill().getIssueDate())%></td></tr>
                            <tr><td class="labelTd">Paid Date:</td><td><%=simpleDateFormat.format(selectedReceipt.getPaidDate())%></td></tr>
                        </table>
                    </div>

                    <table>
                        <thead class="theadDiv"><th width="10%">No.</th><th>Billing Item</th><th width="20%">Amount (RM)</th></thead>
                        <tbody>
                            <%
                                BigDecimal total = new BigDecimal("0");
                                for (int i = 0; i < itemList.size(); i++) {
                                    total = total.add(itemList.get(i).getFee());
                            %>
                            <tr><td width="10%"><center><%=i + 1%></center></td><td><center><%=itemList.get(i).getDescription()%></center></td><td><%=itemList.get(i).getFee()%></td></tr>
                            <% }%>                            
                        <tr><td colspan="2">Total : </td><td><%=total%></td></tr>
                        </tbody>
                    </table>
                        <a href="/printBillStatement?bid=<%=encrypt.UNEncode(selectedReceipt.getBill().getBillNo())%>" target="_blank"><button class="printBtn">Print Statement</button></a>
                </div>               
            </div>
        </div>
    </body>
    <%
        } catch (Exception ex) {
            session.invalidate();
            response.sendRedirect("/login/hosteller");
        }
    %>
</html>
