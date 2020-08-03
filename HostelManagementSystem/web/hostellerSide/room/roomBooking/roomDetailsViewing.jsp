<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="Model.Hosteller"%>
<%@page import="java.util.Base64"%>
<%@page import="Model.Room"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/room/roomDetailsViewing.css">
        <title>Hostel Management System</title>
    </head>
    <%
        Room roomSelected = new Room();
        Hosteller hosteller = new Hosteller();
        List<Hosteller> hostellerList = new ArrayList();
        String image = new String();
        String roomNo = new String();

        try {

            if (session.getAttribute("roomSelected") == null || session.getAttribute("curHosteller") == null || session.getAttribute("hostellerList")==null) {
                throw new Exception("An error has occured, please re-login again.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                roomSelected = (Room) session.getAttribute("roomSelected");
                image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(roomSelected.getImage()));
                roomNo = roomSelected.getRoomNo().substring(4);
                hostellerList = (List)session.getAttribute("hostellerList");
            }
    %>
    <script>
        function closePopUp() {
            var fail = document.getElementById("roomStatusPopUp");
            fail.style.display = "none";
        }
    </script>    

    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>                
                <a href='/retrieveCurrentRoomStatus'><div class="hBG"><div class="hOption "><p>Current Room</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveFloorPlanList'><div class="hBG"><div class="hOption hChoose"><p>Room Booking</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/retrieveRoomBooking'><div class="hBG"><div class="hOption "><p>Booking History</p></div></div></a>     
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
                <div class="chTitle"><a href="/hosteller/room/floorPlanListing">Floor Plan Listing</a> > <a href="/hosteller/room/roomSelection">Room Selection</a> > Room Details</div>
            </div>
            <div class="cBody">                         
                <div class='cbContent roomDetail1'>
                    <%if (roomSelected.getStatus().equals("Not Available")) {%>
                    <div onclick="closePopUp()" class="popup" id="roomStatusPopUp">
                        <div class="cover">
                            <img class="popUpicon" src="../../../hostellerSide/source/alert.png"/>
                            <div class="message">The room is currently not available, please choose another room from the floorplan.</div>
                        </div>
                    </div>   
                    <%}%> 
                    <div class="roomPic" style="background-image: url(<%=image%>)">                       
                    </div>
                    <div class="roomList">
                        <table>
                            <tr><td>Room ID</td><td><%=roomNo%></td></tr>
                            <%
                                int capacity = roomSelected.getCapacity();
                                String roomType = "";
                                switch (capacity) {
                                    case 1:
                                        roomType = "Single-Bed Room";
                                        break;
                                    case 2:
                                        roomType = "Double-Bed Room";
                                        break;
                                    case 3:
                                        roomType = "Triple-Bed Room";
                                        break;
                                    case 4:
                                        roomType = "Two Double-Deck Room";
                                        break;
                                    case 5:
                                        roomType = "Two Double-Deck with One Bed Room";
                                        break;
                                    default:
                                        roomType = "Dormitory";
                                        break;
                                }

                            %>
                            <tr><td>Room Type</td><td><%=roomType%></td></tr>
                            <tr><td>Location</td><td><%=roomSelected.getFloorplanID().getHostelID().getBuilding()%>, Level <%=roomSelected.getFloorplanID().getFloor()%></td></tr>
                            <tr><td>Status</td><td><%=roomSelected.getStatus()%></td></tr>
                            <tr><td>Current room tenants</td><td><%=hostellerList.size()%></td></tr>
                            <tr><td>Rental Fees</td><td>RM <%=roomSelected.getRentalFee()%></td></tr>
                        </table>

                        <%
                            if (roomSelected.getStatus().equals("Unavailable")) {%>

                        <% } else if (hosteller.getStayRoom() == null) {%>
                        <form action="/roomBookingConfirmation" method="post">
                            <input type="hidden" name="requestType" value="New Entry">
                            <button type="submit" id="submitBtn1" hidden=""></button>
                        </form>
                        <button onclick="callDecisionBox1()">Book</button>
                        <%} else {%>
                        <form action="/roomBookingConfirmation" method="post">
                            <input type="hidden" name="requestType" value="Room Allocation">
                            <button type="submit" id="submitBtn2" hidden=""></button>
                        </form>
                        <button onclick="callDecisionBox2()">Room Allocation</button>  
                        <%}%>
                    </div>                                       
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
        function callDecisionBox1() {
            if (confirm('Are you sure that you want to book this room?')) {
                document.getElementById("submitBtn1").click();
            }
        }
        function callDecisionBox2() {
            if (confirm('Are you sure that you want to relocate to this room?')) {
                document.getElementById("submitBtn2").click();
            }
        }
    </script>    
</html>
