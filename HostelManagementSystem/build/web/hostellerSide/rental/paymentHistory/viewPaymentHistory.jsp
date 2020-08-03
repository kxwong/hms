<%@page import="java.util.Date"%>
<%@page import="Controller.Crypto"%>
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
        List<Receipt> receiptList = new ArrayList();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatMonthYear = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat formatInWordForm = new SimpleDateFormat("MMMMMMMM yyyy");
        Hosteller hosteller = new Hosteller();
        Date selectedDate = new Date();
        String selectedMonth = "";
        Crypto encrypt = new Crypto();
        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("receiptList") == null || session.getAttribute("selectedMonth") == null) {
                    throw new Exception("An error has occured, please try again");
                } else {
                    receiptList = (List<Receipt>) session.getAttribute("receiptList");
                    selectedMonth = (String) session.getAttribute("selectedMonth");
                    selectedDate = formatMonthYear.parse(selectedMonth);
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
                <div class="chTitle"><a href="/retrieveMonthlySummary">Monthly Payment Summary</a> > Selected Month</div>
            </div>
            <div class="cBody">
                <div class="cbContent cbFunction">
                    <input class="cbSearch" id="receiptNoSearchBox" onkeyup="receiptNoFilter()" type="text" maxlength="11" placeholder="Receipt No."/>
                    <input class="yearSearch" id="issuedDateSearchBox" type="text" onchange="issuedDateFilter()"  onmouseout="changeToTextSelector()" onmouseover="changeToDateSelector()" placeholder="Issued Date"/>
                </div>               
                <h2 style="margin-left: 2%;">Receipt issued in <%=formatInWordForm.format(selectedDate)%></h2>
                <div class="cbContent flexibleD">   
                    <table class="billingRecordTbl" id="billingRecordTbl">

                        <thead class="theadA">
                            <%  String seq = "desc";
                                if (session.getAttribute("seq") == null) {
                                    seq = "asc";
                                } else if (session.getAttribute("seq").equals("desc")) {
                                    seq = "asc";
                                } else {
                                    seq = "desc";
                                }
                            %>                                                                                                               
                        <th>No.</th>
                        <th style="width:20%;"><a href="/retrievePaymentReceiptList?ob=<%=encrypt.UNEncode("r.receiptNo")%>&seq=<%=encrypt.UNEncode(seq)%>">Receipt No.</a></th>
                        <th><a href="/retrievePaymentReceiptList?ob=<%=encrypt.UNEncode("r.bill.billNo")%>&seq=<%=encrypt.UNEncode(seq)%>">Bill No.</a></th>
                        <th><a href="/retrievePaymentReceiptList?ob=<%=encrypt.UNEncode("r.bill.issueDate")%>&seq=<%=encrypt.UNEncode(seq)%>">Issue Date</a></th>
                        <th><a href="/retrievePaymentReceiptList?ob=<%=encrypt.UNEncode("r.paidDate")%>&seq=<%=encrypt.UNEncode(seq)%>">Paid Date</a></th>
                        <th><a href="/retrievePaymentReceiptList?ob=<%=encrypt.UNEncode("r.requestNo")%>&seq=<%=encrypt.UNEncode(seq)%>">Total Amount (RM)</a></th>
                        </thead>

                        <tbody>
                            <%     if (receiptList.size() > 0) {
                                    for (int i = 0; i < receiptList.size(); i++) {
                            %>
                            <tr><td><%=i + 1%></td><td><a href="/retrieveSelectedReceiptDetails?rid=<%=encrypt.UNEncode(receiptList.get(i).getReceiptNo())%>&bid=<%=encrypt.UNEncode(receiptList.get(i).getBill().getBillNo())%>"><%=receiptList.get(i).getReceiptNo()%></a></td><td><%=receiptList.get(i).getBill().getBillNo()%></td><td><%=simpleDateFormat.format(receiptList.get(i).getBill().getDueDate())%></td><td><%=simpleDateFormat.format(receiptList.get(i).getPaidDate())%></td><td><%=receiptList.get(i).getBill().getTotalAmount()%></td></tr> 
                                    <% } %>
                                    <% } else {%>                             
                            <tr><td colspan="6"><center>No record found</center></td></tr>                           
                            <% }%>

                        </tbody>
                    </table>                                                            
                </div>                
            </div>
        </div>
        <script>
            function changeToDateSelector() {
                document.getElementById("issuedDateSearchBox").type = 'date';
            }

            function changeToTextSelector() {
                document.getElementById("issuedDateSearchBox").type = 'text';
            }

            function receiptNoFilter() {
                var input, filter, table, tr, td, i, txtValue;
                input = document.getElementById("receiptNoSearchBox");
                filter = input.value;
                table = document.getElementById("billingRecordTbl");
                tr = table.getElementsByTagName("tr");
                for (i = 0; i < tr.length; i++) {
                    td = tr[i].getElementsByTagName("td")[1];
                    if (td) {
                        txtValue = td.textContent || td.innerText;
                        if (txtValue.indexOf(filter) > -1) {
                            tr[i].style.display = "";
                        } else {
                            tr[i].style.display = "none";
                        }
                    }
                }
            }
            function issuedDateFilter() {
                var date, dateString, filter, table, tr, td, i, txtValue;
                date = document.getElementById("issuedDateSearchBox");
                filter = date.value.toUpperCase();
                dateString = filter.substring(8, 10) + "/" + filter.substring(5, 7) + "/" + filter.substring(0, 4);
                table = document.getElementById("billingRecordTbl");
                tr = table.getElementsByTagName("tr");
                for (i = 0; i < tr.length; i++) {
                    td = tr[i].getElementsByTagName("td")[3];
                    if (td) {
                        txtValue = td.textContent || td.innerText;
                        if (txtValue.toUpperCase().indexOf(dateString) > -1) {
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
