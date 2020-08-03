<%@page import="Model.Hosteller"%>
<%@page import="java.util.Base64"%>
<%@page import="Model.Roombooking"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/room/selectedBookingRecord.css">
        <title>Hostel Management System</title>
    </head>
    <%
        Roombooking curRoomBooking = new Roombooking();
        String dateBooked = new String();
        String image = new String();
        Hosteller hosteller = new Hosteller();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("curRoomBooking") == null) {
                    throw new Exception("An error has occured");
                } else {
                    curRoomBooking = (Roombooking) session.getAttribute("curRoomBooking");
                    dateBooked = String.valueOf(curRoomBooking.getRequestDate().getDate()) + "/" + String.valueOf(curRoomBooking.getRequestDate().getMonth() + 1) + "/" + String.valueOf(curRoomBooking.getRequestDate().getYear() + 1900);
                    image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(curRoomBooking.getRequestRoom().getImage()));
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
                <div class="chTitle"><a href="/hosteller/room/roomBookingRecord">All Room Booking Records</a> > Selected Record</div>
            </div>
            <div class="cBody">                         
                <div class='cbContent facilityDetail'>
                    <div style="height: 100%; width: 40%; float:left;">
                        <div class="roomPic" style="background-image: url(<%=image%>)">                       
                        </div>
                        <%if (curRoomBooking.getStatus().equals("Pending")) {%>
                        <center><button class="btn" onclick="callDecisionBox()">Cancel Booking</button></center>
                        <form action="/cancelRoomBooking" method="post">
                            <input type="text" hidden="" name="bid" value="<%=curRoomBooking.getRequestNo()%>">
                            <button type="submit" id="submitBtn" hidden=""></button>
                        </form>
                        <% }%>                        
                    </div>
                    <div class="roomList">
                        <table>
                            <tr><td class="labelTd">Booking ID</td><td><%=curRoomBooking.getRequestNo()%></td></tr>
                            <tr><td class="labelTd">Request Type</td><td><%=curRoomBooking.getRequestType()%></td></tr>
                            <tr><td class="labelTd">Room ID</td><td><%=curRoomBooking.getRequestRoom().getRoomNo().substring(4)%></td></tr>        
                            <tr><td class="labelTd">Room Type</td><td><%=curRoomBooking.getRequestRoom().getDescription()%></td></tr>
                            <tr><td class="labelTd">Branch</td><td><%=curRoomBooking.getRequestRoom().getFloorplanID().getHostelID().getLocation()%></td></tr>
                            <tr><td class="labelTd">Room Location</td><td><%=curRoomBooking.getRequestRoom().getFloorplanID().getHostelID().getBuilding()%>, Level <%=curRoomBooking.getRequestRoom().getFloorplanID().getFloor()%></td></tr>
                            <tr><td class="labelTd">Book Date</td><td><%=dateBooked%></td></tr>
                            <tr><td class="labelTd">Status</td><td><%=curRoomBooking.getStatus()%></td></tr>
                        </table>
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
        function callDecisionBox() {
            if (confirm('Are you sure that you want to cancel?')) {
                document.getElementById("submitBtn").click();
            }
        }
    </script>


</html>
