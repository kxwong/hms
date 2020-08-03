<%@page import="Controller.Crypto"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Model.Hosteller"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="Model.Roombooking"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/room/roomBookingRecord.css">
        <title>Hostel Management System</title>
    </head>
    <%
        List<Roombooking> roomBookingList = new ArrayList<Roombooking>();
        Hosteller curHosteller = new Hosteller();
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
        String curDateFormatted = simpleDate.format(new Date());
        Hosteller hosteller = new Hosteller();
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("roomBookingList") == null || session.getAttribute("curHosteller") == null) {
                    throw new Exception("An error has encountered, please try again.");
                } else {
                    roomBookingList = (List<Roombooking>) session.getAttribute("roomBookingList");
                    curHosteller = (Hosteller) session.getAttribute("curHosteller");
                }
            }
    %>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>                
                <a href='/retrieveCurrentRoomStatus'><div class="hBG"><div class="hOption "><p>Current Room</p></div></div></a>
                <a href='/retrieveFloorPlanList'><div class="hBG"><div class="hOption "><p>Room Booking</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveRoomBooking'><div class="hBG"><div class="hOption hChoose"><p>Booking History</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <div style="float:right;">
                    <p style="position:fixed;right:60px; font-size: 20px; color: white;"><%=hosteller.getFirstName()%> <%=hosteller.getMiddleName()%> <%=hosteller.getLastName()%></p>
                    <p style="position:fixed; right:15px;"><a href="/hostellerLogout"><img class="logoutIcon" title="Logout" style="width: 35px; height: 35px;" onclick="logout()" src="../../../hostellerSide/source/logout_icon.png"></a></p>                   
                </div>
            </div>
        </div>
        <div class="sidePanel">
            <div class="spCover">                
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveCurrentRoomStatus'><div class="spoRoom spoBg"><div class="spOption spoChoose"><center><div class="spImg "  style="background-image:url(../../../hostellerSide/source/s_room.png)"></div><p>Room</p></center></div></div></a>               
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveHosteller'><div class="spoTenant spoBg"><div class="spOption "><center><div class="spImg"></div></center>Profile</div></div></a>           
                <a href='/retrieveCurrentBillingList'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg " ></div></center>Rental</div></div></a>           
                <a href='/retrieveAllFacilitiesWithinBranch'><div class="spoFacilities spoBg"><div class="spOption "><center><div class="spImg"  ></div></center>Facility</div></div></a>           
                <a href='/retrieveAllAnnouncement'><div class="spoNotice spoBg"><div class="spOption "><center><div class="spImg"></div></center>Notice</div></div></a>   
                <a href='/retrieveAllCase'><div class="spoReporting spoBg"><div class="spOption"><center><div class="spImg "></div></center>Report</div></div></a>             
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">All Room Booking Records</div>
            </div>
            <div class="cBody">                         
                <div class='cbContent criteriaSearching'>
                    <h2>All your room booking are shown below.</h2>
                    <form action="/retrieveRoomBookingSorted" method="post">
                        <table>                        
                            <tr>
                                <td>Request Type</td>
                                <td style="width:27%;">                           
                                    <select class="customDropdown" name="requestType" onchange="filterTable()" required="1" id="requestTypeFilter">
                                        <option value="New Entry">New Entry</option>
                                        <option value="Room Allocation">Room Allocation</option>
                                        <option value="Move Out">Move Out</option>
                                        <option value="All" selected="">All</option>
                                    </select>
                                </td>                            
                                <td>Status</td>
                                <td>                            
                                    <select class="customDropdown" name="roomType" onchange="filterTable()" required="1" id="statusFilter">
                                        <option value="Pending">Pending</option>
                                        <option value="Approved">Approved</option>
                                        <option value="Rejected">Rejected</option>
                                        <option value="Paid">Paid</option>
                                        <option value="Cancelled">Cancelled</option>
                                        <option value="All" selected="">All</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:27%;">Book Date</td>
                                <td><input type='date' name="requestDate" onchange="filterTable()" id="bookDateFilter"/></td>
                                </td>
                            </tr>
                            <input type="hidden" name="parameterizedSorted" value="true">
                        </table>                        
                    </form>                        
                </div>
                <div class="cbContent caseHistorytb">
                    <table id="roomBookingTable">
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
                        <thead>
                        <th width="15%;"><a href="/retrieveRoomBooking?ob=<%=encrypt.UNEncode("r.requestNo")%>&seq=<%=encrypt.UNEncode(seq)%>">Booking ID</a></th>
                        <th class="theadTxt"><a href="/retrieveRoomBooking?ob=<%=encrypt.UNEncode("r.requestType")%>&seq=<%=encrypt.UNEncode(seq)%>">Request Type</a></th>
                        <th class="theadTxt"><a href="/retrieveRoomBooking?ob=<%=encrypt.UNEncode("r.requestRoom")%>&seq=<%=encrypt.UNEncode(seq)%>">Room ID</a></th>
                        <th class="theadTxt"><a href="/retrieveRoomBooking?ob=<%=encrypt.UNEncode("r.requestDate")%>&seq=<%=encrypt.UNEncode(seq)%>">Request Time</a></th>
                        <th class="theadTxt"><a href="/retrieveRoomBooking?ob=<%=encrypt.UNEncode("r.status")%>&seq=<%=encrypt.UNEncode(seq)%>">Status</a></th>
                        </thead>

                        <% if (roomBookingList.size() == 0) { %>
                        <tr><td colspan="5" style="text-align: center;"><center>No room booking record available.</center></td></tr>
                            <%} else {%>                        
                            <% for (int i = 0; i < roomBookingList.size(); i++) {%>
                        <tr>
                            <td><center><a href="/retrieveRoomBookingRecordDetails?brid=<%=roomBookingList.get(i).getRequestNo()%>"><%=roomBookingList.get(i).getRequestNo()%></a></center></td>
                        <td><center><%=roomBookingList.get(i).getRequestType()%></center></td>
                        <td><center><%=roomBookingList.get(i).getRequestRoom().getRoomNo().substring(4)%></center> </td>
                        <td><center><%=simpleDate.format(roomBookingList.get(i).getRequestDate())%></center></td>
                        <td><center><%=roomBookingList.get(i).getStatus()%></center></td>
                        </tr>
                        <%}%>
                        <%}%>
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
        function filterTable() {
            var table, rows, cells, tableTypeValue, tableStatusValue, tableBookDateValue;
            var typeDropdown = document.getElementById("requestTypeFilter");
            var statusDropdown = document.getElementById("statusFilter");
            var bookDate = document.getElementById("bookDateFilter");

            table = document.getElementById("roomBookingTable");
            rows = table.getElementsByTagName("tr");
            var typeValue = typeDropdown.value;
            var statusValue = statusDropdown.value;
            var bookDateValue = bookDate.value;

            if (!(bookDateValue === "")) {
                var dateString = bookDateValue.substring(8, 10) + "/" + bookDateValue.substring(5, 7) + "/" + bookDateValue.substring(0, 4);
            }else{
                var dateString = "";
            }

            for (var i = 0; i < rows.length; i++) {
                cells = rows[i].getElementsByTagName("td");
                tableTypeValue = cells[1] || null;
                tableStatusValue = cells[4] || null;
                tableBookDateValue = cells[3] || null;
                
                if (!tableTypeValue || !tableStatusValue || !tableBookDateValue) {
                    rows[i].style.display = "";
                } else if (dateString === "") {
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
                } else if (!(dateString === "")) {
                    if (typeValue === "All" && statusValue === tableStatusValue.textContent && dateString === tableBookDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === "All" && typeValue === tableTypeValue.textContent && dateString === tableBookDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === "All" && typeValue === "All" && dateString === tableBookDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (statusValue === tableStatusValue.textContent && typeValue === tableTypeValue.textContent && dateString === tableBookDateValue.textContent) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                }
            }
        }
    </script>              
</html>
