<%@page import="Controller.Crypto"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.math.BigInteger"%>
<%@page import="Model.Bill"%>
<%@page import="java.util.Date"%>
<%@page import="Model.Hosteller"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Receipt"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/rental/viewPaymentHistory.css">
        <title>Hostel Management System</title>
    </head>
    <%
        List<Receipt> receiptList = new ArrayList<Receipt>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatMonth = new SimpleDateFormat("MMMMMMMM");
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        Hosteller hosteller = new Hosteller();
        List<Bill> billList = new ArrayList();
        List<String> dateList = new ArrayList();
        SimpleDateFormat formatMonthDate = new SimpleDateFormat("yyyy-MM");
        List<String> yearList = new ArrayList();
        List<String> monthList = new ArrayList();
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("billList") == null || session.getAttribute("dateList") == null) {
                    throw new Exception("An error has occured, please try again");
                } else {
                    billList = (List) session.getAttribute("billList");
                    dateList = (List) session.getAttribute("dateList");
                }
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
                <div class="chTitle">Monthly Payment Summary</div>
            </div>
            <div class="cBody">
                <form action="/printYearlyStatistic" method="post" target="_blank">
                    <div class="cbContent cbFunction" style="width:100%;">
                        <select class="yearSearch" style="padding: 10px; margin-left: 0%;" onchange="issuedDateYearFilter()" id="issuedYearSearchBox" name="yearSelected">
                            <%for (int i = 0; i < dateList.size(); i++) {
                                    if (!(yearList.contains(dateList.get(i).substring(0, 4)))) {
                                        yearList.add(dateList.get(i).substring(0, 4));
                                    }
                                }%>
                            <%for (int k = 0; k < yearList.size(); k++) {%>
                            <option value="<%=yearList.get(k)%>"><%=yearList.get(k)%></option>
                            <% }%>
                        </select>
                        <button type="submit" style="display:block; width: 20%; margin-left: 5%;">Print Yearly Statement</button>
                    </div>
                </form>               

                <div class="cbContent flexibleD">                                                                                                   
                    <table class="billingRecordTbl" id="billingRecordTbl">
                        <thead><th>Year</th><th style="width:20%;">Month</th><th>Amount of Bill Issued</th><th>Remaining Bill</th><th>Total Amount</th><th>Unpaid Amount</th></thead>                                      
                        <tbody>
                            <%
                                for (int i = 0; i < dateList.size(); i++) {

                                    String date = dateList.get(i);
                                    Date thisMonth = formatMonthDate.parse(date);
                                    BigDecimal totalAmount = new BigDecimal(0);
                                    BigDecimal unpaidAmount = new BigDecimal(0);
                                    int billAmt = 0;
                                    int remainBill = 0;

                                    for (int j = 0; j < billList.size(); j++) {
                                        if (formatMonthDate.format(billList.get(j).getIssueDate()).equals(date)) {
                                            totalAmount = totalAmount.add(billList.get(j).getTotalAmount());
                                            billAmt++;

                                            if (billList.get(j).getReceiptNo() == null) {
                                                unpaidAmount = unpaidAmount.add(billList.get(j).getTotalAmount());
                                                remainBill++;
                                            }
                                        }
                                    }%>
                            <tr><td><%=formatYear.format(thisMonth)%></td><td><a href="/retrievePaymentReceiptList?date=<%=encrypt.UNEncode(dateList.get(i))%>"><%=formatMonth.format(thisMonth)%></a></td><td><%=billAmt%></td><td><%=remainBill%></td><td><%=totalAmount%></td><td><%=unpaidAmount%></td></tr>                          
                                    <% }%>                                                                                                               
                        </tbody>
                    </table>                                                            
                </div>                
            </div>
        </div>
        <script>
            function changeToDateSelector() {
                document.getElementById("issuedDateSearchBox").type = 'month';
            }

            function changeToTextSelector() {
                document.getElementById("issuedDateSearchBox").type = 'text';
            }

            function issuedDateYearFilter() {
                var date, table, tr, td, i, txtValue;
                date = document.getElementById("issuedYearSearchBox");
                table = document.getElementById("billingRecordTbl");
                tr = table.getElementsByTagName("tr");
                for (i = 0; i < tr.length; i++) {
                    td = tr[i].getElementsByTagName("td")[0];
                    if (td) {
                        txtValue = td.textContent || td.innerText;
                        if (txtValue === date) {
                            tr[i].style.display = "";
                        } else {
                            tr[i].style.display = "none";
                        }
                    }
                }
            }
        </script>                            
    </body>
    <%
        } catch (Exception ex) {
            session.invalidate();
            response.sendRedirect("/login/hosteller");
        }
    %>
</html>
