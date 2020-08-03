<%@page import="java.util.Base64"%>
<%@page import="Model.Hosteller"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Model.Facilitybooking"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/facilities/facilitiesSelectedBookingRecord.css">
        <title>Hostel Management System</title>
    </head>
    <%
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm aaaaa");
        Facilitybooking selectedFaciBooking = new Facilitybooking();
        Hosteller hosteller = new Hosteller();
        String image;

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, pleas re-login.");
            } else if (session.getAttribute("selectedFaciBooking") == null) {
                throw new Exception("An error has occured, please try again.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                selectedFaciBooking = (Facilitybooking) session.getAttribute("selectedFaciBooking");

                image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(selectedFaciBooking.getBookFacility().getImage()));
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
                <div class="chTitle"><a href="/hosteller/facilities/facilityRecord">Booking Records</a> > Record Details</div>
            </div>
            <div class="cBody">                         
                <div class='cbContent facilityDetail'>
                    <div class="facilityPic" style="background-image: url(<%=image%>)">
                    </div>                                                       
                    <div class="facilitiesList">
                        <table>
                            <%
                                String facilityName = "";
                                if (selectedFaciBooking.getBookFacility().getDescription().split("@").length > 1) {
                                    facilityName = selectedFaciBooking.getBookFacility().getDescription().split("@")[0] + " of " + selectedFaciBooking.getBookFacility().getDescription().split("@")[1];
                                } else {
                                    facilityName = selectedFaciBooking.getBookFacility().getDescription();
                                }

                            %>

                            <tr><td class="tblLabel">Booking ID</td><td><%=selectedFaciBooking.getBookingID()%></td></tr>
                            <tr><td class="tblLabel">Facilities Type</td><td><%=facilityName%></td></tr>
                            <tr><td class="tblLabel">Location</td><td><%=selectedFaciBooking.getBookFacility().getHostelID().getBuilding()%>, of <%=selectedFaciBooking.getBookFacility().getHostelID().getLocation()%></td></tr>        
                            <tr><td class="tblLabel">Requested Date</td><td><%=dateFormat.format(selectedFaciBooking.getRequestTime())%></td></tr>
                            <tr><td class="tblLabel">Booking Date</td><td><%=timeFormat.format(selectedFaciBooking.getBookTime())%></td></tr>
                            <tr><td class="tblLabel">Status</td><td><%=selectedFaciBooking.getStatus()%></td></tr>
                            <tr><td class="tblLabel">Book quantity</td><td><%=selectedFaciBooking.getBookQuantity()%></td></tr>
                        </table>
                    </div>
                    <% if (selectedFaciBooking.getStatus().equals("Pending")) {%>
                    <button onclick="callDecisionBox()">Cancel Booking</button> 
                    <form action="/cancelFacilityBooking" method="post">
                        <input type="text" name="fbid" hidden="" value="<%=selectedFaciBooking.getBookingID()%>">
                        <button hidden="" id="submitBtn" type="submit"></button>
                    </form>
                    <% }%>
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
