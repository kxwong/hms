<%@page import="java.util.Date"%>
<%@page import="Controller.Crypto"%>
<%@page import="Model.Hosteller"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="Model.Bill"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Billitem"%>
<%@page import="java.util.List"%>
<%@page import="java.util.concurrent.TimeUnit"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/rental/viewSelectedBillingDetails.css">
        <script src="https://www.paypal.com/sdk/js?client-id=AdIZztjM7rktU_DN60qh5C1DlyaJGHJwOVLMtOUAdGlY3YxnQXy_OIXdAdwO8fP_TeppddWozWKf8G-J&disable-funding=credit,card&currency=MYR"></script>
        <title>Hostel Management System</title>
    </head>
    <%

        List<Billitem> billItemList = new ArrayList();
        Bill selectedBill = new Bill();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMMMMMM yyyy");
        Hosteller hosteller = new Hosteller();
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("selectedBill") == null || session.getAttribute("billItemList") == null) {
                    throw new Exception("An error has occured, please try again.");
                } else {
                    billItemList = (List<Billitem>) session.getAttribute("billItemList");
                    selectedBill = (Bill) session.getAttribute("selectedBill");
                }
            }
            int overdueDay = 0;
            if (selectedBill.getDueDate().before(new Date())) {
                long diffInMillies = Math.abs(new Date().getTime() - selectedBill.getDueDate().getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                overdueDay = Integer.valueOf(String.valueOf(diff));
            }
                session.setAttribute("overdueDay", overdueDay);
    %>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>   
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveCurrentBillingList'><div class="hBG"><div class="hOption hChoose"><p>Current Billing</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/retrieveMonthlySummary'><div class="hBG"><div class="hOption "><p>Payment History</p></div></div></a>  
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
                <div class="chTitle"><a href="/retrieveCurrentBillingList">Current Billing</a> > Selected Bill</div>
            </div>
            <div class="cBody">              

                <div class="cbContent contentBilling">

                    <div class="billingHeader">
                        <table>
                            <caption>Billing for <%=selectedBill.getDescription()%></caption>
                            <tr><td>Bill Number : </td><td><%=selectedBill.getBillNo()%></td></tr>
                            <tr><td>Issue Date : </td><td><%=dateFormat.format(selectedBill.getIssueDate())%></td></tr>
                            <tr><td>Due Date : </td><td><%=dateFormat.format(selectedBill.getDueDate())%></td></tr>
                        </table>
                    </div>
                    <table class="billItem">
                        <thead><th style="width: 10%; background-color: #2c478e; color: white;">No.</th><th style="background-color: #2c478e; color: white;">Billing Item</th><th style="width: 20%; background-color: #2c478e; color: white;">Amount (RM)</th></thead>
                        <tbody>
                            <%
                                BigDecimal totalFee = new BigDecimal("0");
                                if (billItemList.size() > 0) {
                                    int no = 0;
                                    for (int i = 0; i < billItemList.size(); i++) {
                                        totalFee = totalFee.add(billItemList.get(i).getFee());
                                        no = i;
                            %>
                            <tr><td><center><%=i + 1%></center></td><td><center><%=billItemList.get(i).getDescription()%></center></td><td><center><%=billItemList.get(i).getFee()%></center></td></tr>
                            <% }
                                if (overdueDay > 0) {
                                    totalFee = totalFee.add(BigDecimal.valueOf((overdueDay / 7 + 1) * 50));
                            %>
                        <tr><td><center><%=no + 2%></center></td><td><center><%="Overdue penalty for " + overdueDay + " day"%></center></td><td><center><%=(overdueDay / 7 + 1) * 50%></center></td></tr>
                            <%
                                }%>
                        <tr><td colspan="2" style="text-align: center;">Total : </td><td><center><%=String.valueOf(totalFee)%></center></td></tr>
                            <%
                            } else {%>
                        <tr><td colspan="3"><center>No Record Found</center></td></tr>
                            <% }%>
                        </tbody>
                    </table>    
                    <div id="paypal-button-container" class="payBtn"></div>
                    <script>
                        paypal.Buttons({
                            style: {
                                layout: 'vertical',
                                color: 'blue',
                                shape: 'rect',
                                label: 'pay'
                            },
                            createOrder: function (data, actions) {
                                return actions.order.create({
                                    purchase_units: [{
                                            amount: {
                                                value: '<%=totalFee%>'
                                            }
                                        }]
                                });
                            },
                            onApprove: function (data, actions) {
                                return actions.order.capture().then(function (details) {
                                    alert('Bill is paid successfully');
                                    location.replace('/billingPayment');
                                });
                            }
                        }).render('#paypal-button-container');

                    </script>        
                </div>
            </div>
        </div>
    </body>
    <%
        }catch (Exception ex) {
            session.invalidate();
            response.sendRedirect("/login/hosteller");
        }%>
</html>


