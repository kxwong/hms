<%@page import="java.util.Date"%>
<%@page import="Controller.Crypto"%>
<%@page import="Model.Hosteller"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Bill"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/rental/viewCurrentBilling.css">
        <title>Hostel Management System</title>
    </head>
    <script>
        function closeSuccess() {
            var success = document.getElementById("successPopUp");
            success.style.display = "none";
        }
        function closeFail() {
            var fail = document.getElementById("failPopUp");
            fail.style.display = "none";
        }
        function closeAlert() {
            var fail = document.getElementById("alertPopUp");
            fail.style.display = "none";
        }        
    </script>             
    <%
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatDay = new SimpleDateFormat("DDD");
        List<Bill> billList = new ArrayList<Bill>();
        Hosteller hosteller = new Hosteller();
        Crypto encrypt = new Crypto();
        Date curDate = new Date();
        int amountOfUpcomingBill = 0;
        int amountOfOverdueBill = 0;

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occur, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("billList") != null) {
                    billList = (List<Bill>) session.getAttribute("billList");
                } else {
                    throw new Exception("An error has occured, please re-login again.");
                }
                
                int billDay = 0;
                int currentDay = Integer.parseInt(formatDay.format(curDate));

                for (int i = 0; i < billList.size(); i++) {
                    billDay = Integer.parseInt(formatDay.format(billList.get(i).getDueDate()));
                    if ((billDay - currentDay) < 0) {
                        amountOfUpcomingBill++;
                    }else if((billDay - currentDay) > 0 & (billDay - currentDay) < 7){
                        amountOfOverdueBill++;
                    }
                }
            }
    %>    
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveCurrentBillingList'><div class="hBG"><div class="hOption hChoose"><p>Current Billing</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/retrieveMonthlySummary'><div class="hBG"><div class="hOption"><p>Payment History</p></div></div></a>
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
                <div class="chTitle">Current Billing</div>
            </div>
            <div class="cBody">
                <%if (session.getAttribute("successMsg") != null) {%>
                <center>
                    <div onclick="closeSuccess()" class="popup" id="successPopUp">
                        <div class="cover">
                            <img class="popUpicon" src="../../../hostellerSide/source/success.png"/>
                            <div class="message"><%=String.valueOf(session.getAttribute("successMsg").toString())%></div>
                        </div>
                    </div>
                </center>
                <%  session.removeAttribute("successMsg");}%>
                <%if (session.getAttribute("errMsg") != null) {%>
                <center>
                    <div onclick="closeFail()" class="popup" id="failPopUp">
                        <div class="cover">
                            <img class="popUpicon" src="../../../hostellerSide/source/failed.png"/>
                            <div class="message"><%=String.valueOf(session.getAttribute("errMsg").toString())%></div>
                        </div>
                    </div>
                </center>
                <%  session.removeAttribute("errMsg");}%>  
                <%if (amountOfUpcomingBill > 0){%>
                <center>
                    <div onclick="closeAlert()" class="popup" id="alertPopUp">
                        <div class="cover">
                            <img class="popUpicon" src="../../../hostellerSide/source/alert.png"/>
                            <div class="message">
                                You have <%=amountOfUpcomingBill%> bill to be paid in this week                                 
                                <% 
                                    if(amountOfOverdueBill>0){ %>
                                        and <%=amountOfOverdueBill%> overdue bill.
                                  <%  }
                                %>                                                                                      
                            </div>
                        </div>
                    </div>
                </center>
                <%}%> 
                <div class="cbContent cbFunction">
                    <input class="cbSearch" id="billNoSearchBox" onkeyup="billNoFilter()" type="text" maxlength="11" placeholder="Bill Ref. No."/>
                    <input class="yearSearch" id="issuedDateSearchBox" type="text" onchange="issuedDateFilter()"  onmouseout="changeToTextSelector()" onmouseover="changeToDateSelector()" placeholder="Issued Date"/>
                </div> 
                <div class="cbContent contentBilling">
                    <h2>All your unpaid bill is shown below.</h2>
                    <table id="billingRecordTbl">
                        <%  String seq = "desc";
                            if (session.getAttribute("seq") == null) {
                                seq = "asc";
                            } else if (session.getAttribute("seq").equals("desc")) {
                                seq = "asc";
                            } else {
                                seq = "desc";
                            }
                        %> 
                        <thead class="theadA">
                        <th><a href="/retrieveCurrentBillingList?ob=<%=encrypt.UNEncode("b.billNo")%>&seq=<%=encrypt.UNEncode(seq)%>">Billing Number</a></th>
                        <th><a href="/retrieveCurrentBillingList?ob=<%=encrypt.UNEncode("b.description")%>&seq=<%=encrypt.UNEncode(seq)%>">Description</a></th>
                        <th><a href="/retrieveCurrentBillingList?ob=<%=encrypt.UNEncode("b.totalAmount")%>&seq=<%=encrypt.UNEncode(seq)%>">Amount (RM)</a></th>
                        <th><a href="/retrieveCurrentBillingList?ob=<%=encrypt.UNEncode("b.issueDate")%>&seq=<%=encrypt.UNEncode(seq)%>">Issue Date</a></th>
                        <th><a href="/retrieveCurrentBillingList?ob=<%=encrypt.UNEncode("b.dueDate")%>&seq=<%=encrypt.UNEncode(seq)%>">Due Date</a></th>
                        </thead>
                        <tbody>
                            <%
                                if (billList.size() > 0) {
                                    for (int i = 0; i < billList.size(); i++) {
                            %>
                            <tr></td><td><a href="/retrieveSelectedBillDetails?bno=<%=encrypt.UNEncode(billList.get(i).getBillNo())%>"><%=billList.get(i).getBillNo()%></a></td><td><%=billList.get(i).getDescription()%></td><td><%=billList.get(i).getTotalAmount()%></td><td><%=simpleDateFormat.format(billList.get(i).getIssueDate())%></td><td><%=simpleDateFormat.format(billList.get(i).getDueDate())%></td></tr>
                                    <%}%>
                                    <%} else {%>
                            <tr><td colspan="6"><center>No billing pending for payment</center></td></tr>
                            <%}%>
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

            function billNoFilter() {
                var input, filter, table, tr, td, i, txtValue;
                input = document.getElementById("billNoSearchBox");
                filter = input.value;
                table = document.getElementById("billingRecordTbl");
                tr = table.getElementsByTagName("tr");
                for (i = 0; i < tr.length; i++) {
                    td = tr[i].getElementsByTagName("td")[0];
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
                if (!(filter === "")) {
                    dateString = filter.substring(8, 10) + "/" + filter.substring(5, 7) + "/" + filter.substring(0, 4);
                } else {
                    dateString = "";
                }
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
    <%        } catch (Exception ex) {
            session.invalidate();
            response.sendRedirect("/login/hosteller");
        }

    %>
</html>
