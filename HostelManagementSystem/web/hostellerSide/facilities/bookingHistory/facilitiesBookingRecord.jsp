<%@page import="Controller.Crypto"%>
<%@page import="Model.Hosteller"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Facilitybooking"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/facilities/facilitiesBookingRecord.css">
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
    </script>     

    <%
        List<Facilitybooking> facilityBookingList = new ArrayList();
        List<String> descriptionList = new ArrayList();
        SimpleDateFormat formatSelectDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm aa");
        Hosteller hosteller = new Hosteller();
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login again.");
            } else if (session.getAttribute("facilityBookingList") == null) {
                throw new Exception("An error has occured, pelase try again.");
            } else {
                facilityBookingList = (List) session.getAttribute("facilityBookingList");
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                for (int i = 0; i < facilityBookingList.size(); i++) {

                    String facilityName = "";

                    if (facilityBookingList.get(i).getBookFacility().getDescription().split("@").length > 0) {
                        facilityName = facilityBookingList.get(i).getBookFacility().getDescription().split("@")[0];
                    } else {
                        facilityName = facilityBookingList.get(i).getBookFacility().getDescription();
                    }

                    if (!descriptionList.contains(facilityName)) {
                        descriptionList.add(facilityName);
                    }
                }
            }
            
    %>

    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>            
                <a href='/retrieveAllFacilitiesWithinBranch'><div class="hBG"><div class="hOption"><p>Facilities Booking</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveAllBookingRecord'><div class="hBG"><div class="hOption hChoose"><p>Booking History</p></div></div></a>
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
                <a href='/retrieveCurrentBillingList'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg"></div></center>Rental</div></div></a>
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveAllFacilitiesWithinBranch'><div class="spoFacilities spoBg"><div class="spOption spoChoose"><center><div class="spImg"  style="background-image:url(../../../hostellerSide/source/s_facilities.png)"></div></center>Facility</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveAllAnnouncement'><div class="spoNotice spoBg"><div class="spOption "><center><div class="spImg"></div></center>Notice</div></div></a>       
                <a href='/retrieveAllCase'><div class="spoReporting spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Report</div></div></a>            
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Booking Records</div>
            </div>
            <div class="cBody"> 
                <%if (session.getAttribute("successMsg")!=null) { %>
                <center>
                    <div onclick="closeSuccess()" class="popup" id="successPopUp">
                        <div class="cover">
                            <img class="popUpicon" src="../../../hostellerSide/source/success.png"/>
                            <div class="message"><%=String.valueOf(session.getAttribute("successMsg")).toString()%></div>
                        </div>
                    </div>
                </center>
                <%  session.removeAttribute("successMsg");
                        }%>
                <%if (session.getAttribute("errMsg")!=null) { %>
                <center>
                    <div onclick="closeFail()" class="popup" id="failPopUp">
                        <div class="cover">
                            <img class="popUpicon" src="../../../hostellerSide/source/failed.png"/>
                            <div class="message"><%=String.valueOf(session.getAttribute("errMsg")).toString()%></div>
                        </div>
                    </div>
                </center>
                <%  session.removeAttribute("errMsg");
                        }%>                                               
                <div class='cbContent criteriaSearching'>
                    <p>All your booking record is shown below. <button target="_blank" onclick="var ans = ''; ans = window.prompt('Enter the year (MM-YYYY):');if (ans === null) {
                                return false;
                            } else if (ans !== '') {
                                window.open('/printFacilityBookingYearly?year=' + ans);
                            } else {
                                return false;
                            }" style="margin-left: 40%;">Print Monthly Statistic</button>                                                        
                    </p>
                    <table>
                        <tr><td>Book Date</td><td><input type='date' class="customDropdown" onchange="filterTableType()" id="bookDate"/></td><td>Last Update Date</td><td><input type='date' class="customDropdown" onchange="filterTableType()" id="updateDate"/></td></tr>
                        <tr>
                            <td>Facility Type</td>
                            <td>
                                <form action="/filterBookingTable" method="post">
                                    <select class="customDropdown" required="" onchange="filterTableType()" name="description" id="typeFilter">
                                        <option value="All">All</option>
                                        <% for (int i = 0; i < descriptionList.size(); i++) {%>
                                        <option value="<%=descriptionList.get(i)%>"><%=descriptionList.get(i)%></option>
                                        <% }%>                                   
                                    </select>
                                    <button type="submit" id="descBtn" hidden></button>
                                </form>

                            </td>
                            <td>Status</td>
                            <td>
                                <form action="/filterBookingTable" method="post">
                                    <select class="customDropdown" required="" id="statusFilter" onchange="filterTableType()" name="status">
                                        <option value="All" selected="">All</option>
                                        <option value="Pending">Pending</option>
                                        <option value="Approved">Approved</option>                                   
                                        <option value="Rejected">Rejected</option>
                                        <option value="Cancelled">Cancelled</option> 
                                    </select>
                                    <button type="submit" id="statusBtn" hidden></button>                                    
                                </form>

                            </td>
                        </tr>
                    </table>                        

                </div>
                <div class="cbContent facilityBookingTable">
                    <table id="facilityBookingTable">
                        <tbody>
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
                        <th><a href="/retrieveAllBookingRecord?ob=<%=encrypt.UNEncode("f.bookingID")%>&seq=<%=encrypt.UNEncode(seq)%>">Booking ID</a></th>
                        <th><a href="/retrieveAllBookingRecord?ob=<%=encrypt.UNEncode("f.bookFacility")%>&seq=<%=encrypt.UNEncode(seq)%>">Facilities Type</a></th>
                        <th><a href="/retrieveAllBookingRecord?ob=<%=encrypt.UNEncode("f.bookTime")%>&seq=<%=encrypt.UNEncode(seq)%>">Book Date</a></th>
                        <th><a href="/retrieveAllBookingRecord?ob=<%=encrypt.UNEncode("f.updateTime")%>&seq=<%=encrypt.UNEncode(seq)%>">Update Date</a></th>
                        <th><a href="/retrieveAllBookingRecord?ob=<%=encrypt.UNEncode("f.status")%>&seq=<%=encrypt.UNEncode(seq)%>">Status</a></th>
                        </thead>
                        <%for (int i = 0; i < facilityBookingList.size(); i++) {
                            String facilityDesc = "";
                            if(facilityBookingList.get(i).getBookFacility().getDescription().split("@").length > 1 ){
                                facilityDesc = facilityBookingList.get(i).getBookFacility().getDescription().split("@")[0];
                            }else{
                                facilityDesc = facilityBookingList.get(i).getBookFacility().getDescription();
                            }                        
                        %>
                        <tr>
                            <td><center><a href="/retrieveSelectedBookingRecord?bid=<%=encrypt.UNEncode(facilityBookingList.get(i).getBookingID())%>"><%=facilityBookingList.get(i).getBookingID()%></a></center></td>
                        <td><center><%=facilityDesc%></center></td>
                        <td><center><%=formatSelectDate.format(facilityBookingList.get(i).getBookTime())%></center></td>
                        <td><center><%=formatSelectDate.format(facilityBookingList.get(i).getRequestTime())%></center></td>                       
                        <td><center><%=facilityBookingList.get(i).getStatus()%></center></td>
                        </tr>
                        <% }%>
                        </tbody>
                    </table> 
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
    <script>
        function filterTableType() {
            var table, rows, cells, tableTypeValue, tableStatusValue, tableBookDateValue, tableUpdateDateValue;
            var typeDropdown = document.getElementById("typeFilter");
            var statusDropdown = document.getElementById("statusFilter");
            var bookDate = document.getElementById("bookDate");
            var updateDate = document.getElementById("updateDate");

            table = document.getElementById("facilityBookingTable");
            rows = table.getElementsByTagName("tr");
            var typeValue = typeDropdown.value;
            var statusValue = statusDropdown.value;
            var bookDateValue = bookDate.value;
            var updateDateValue = updateDate.value;

            if (!(bookDateValue === "")) {
                bookDateValue = bookDateValue.substring(8, 10) + "/" + bookDateValue.substring(5, 7) + "/" + bookDateValue.substring(0, 4);
            } else {
                bookDateValue = "";
            }

            if (!(updateDateValue === "")) {
                updateDateValue = updateDateValue.substring(8, 10) + "/" + updateDateValue.substring(5, 7) + "/" + updateDateValue.substring(0, 4);
            } else {
                updateDateValue = "";
            }

            for (var i = 0; i < rows.length; i++) {
                cells = rows[i].getElementsByTagName("td");
                tableTypeValue = cells[1] || null;
                tableStatusValue = cells[4] || null;
                tableBookDateValue = cells[2] || null;
                tableUpdateDateValue = cells[3] || null;

                if (!tableTypeValue || !tableStatusValue || !tableBookDateValue || !tableUpdateDateValue) {
                    rows[i].style.display = "";
                } else if (bookDateValue === "" && updateDateValue === "") {
                    if (typeValue === "All" && statusValue === tableStatusValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === "All" && typeValue === tableTypeValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === "All" && typeValue === "All") {
                        rows[i].style.display = "";
                    } else if (statusValue === tableStatusValue.textContent && typeValue === tableTypeValue.textContent) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                } else if (bookDateValue === "" && !(updateDateValue === "")) {
                    if (typeValue === "All" && statusValue === tableStatusValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === "All" && typeValue === tableTypeValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === "All" && typeValue === "All" && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === tableStatusValue.textContent && typeValue === tableTypeValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                } else if (updateDateValue === "" && !(bookDateValue === "")) {
                    if (typeValue === "All" && statusValue === tableStatusValue.textContent && bookDateValue === tableBookDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === "All" && typeValue === tableTypeValue.textContent && bookDateValue === tableBookDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === "All" && typeValue === "All" && bookDateValue === tableBookDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === tableStatusValue.textContent && typeValue === tableTypeValue.textContent && bookDateValue === tableBookDateValue.textContent) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                } else if (!(bookDateValue === "") && !(updateDateValue === "")) {
                    if (typeValue === "All" && statusValue === tableStatusValue.textContent && bookDateValue === tableBookDateValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === "All" && typeValue === tableTypeValue.textContent && bookDateValue === tableBookDateValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === "All" && typeValue === "All" && bookDateValue === tableBookDateValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === tableStatusValue.textContent && typeValue === tableTypeValue.textContent && bookDateValue === tableBookDateValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                } else {
                    rows[i].style.display = "none";
                }
            }
        }
    </script>
</html>
