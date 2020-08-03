<%@page import="Controller.Crypto"%>
<%@page import="java.util.Base64"%>
<%@page import="Model.Hosteller"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Facility"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/facilities/facilitiesBooking.css">
        <title>Hostel Management System</title>
    </head>
    <%
        SimpleDateFormat formatSelectDate = new SimpleDateFormat("yyyy-MM-dd EEEEEE");
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm aaaaa");

        Date selectedDate = new Date();
        Date selectedTime = new Date();
        Facility selectedFacility = new Facility();
        Hosteller hosteller = new Hosteller();
        String image = new String();
        String[] reminderList;

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured. please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("selectedDate") != null && session.getAttribute("selectedFacility") != null && session.getAttribute("selectedTime") != null) {
                    selectedDate = (Date) session.getAttribute("selectedDate");
                    selectedTime = (Date) session.getAttribute("selectedTime");
                    selectedFacility = (Facility) session.getAttribute("selectedFacility");

                    String reminder = selectedFacility.getReminder();
                    reminderList = reminder.split("\\.");

                    image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(selectedFacility.getImage()));
                } else {
                    throw new Exception("An error has occured, please choose date and facility before proceed.");
                }
            }


    %>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveAllFacilitiesWithinBranch'><div class="hBG"><div class="hOption hChoose"><p>Facilities Booking</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/retrieveAllBookingRecord'><div class="hBG"><div class="hOption"><p>Booking History</p></div></div></a>
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
                <div class="chTitle"><a href="/retrieveAllFacilitiesWithinBranch">Facilities Overview</a> > <a href="/hosteller/facilities/facilitiesBooking">Facility Selection</a> > Facility Booking</div>
            </div>
            <div class="cBody">                         
                <div class='cbContent'>
                    <h2>Facility Booking Summary</h2>
                    <div class="upperPart">
                        <div class="facilityPicPanel" style="background-image: url(<%=image%>); margin-right:5%;margin-bottom:5%;">
                        </div>
                        <div class="reminderPanel">
                            <strong>Selected Facility</strong><br/>
                            <%
                                String facilityName = "";
                                if(selectedFacility.getDescription().split("@").length>1){
                                    facilityName = selectedFacility.getDescription().split("@")[0] +" at "+ selectedFacility.getDescription().split("@")[1];
                                }else{
                                    facilityName = selectedFacility.getDescription();
                                }
                                
                            %>
                                                       
                            <p style="font-size: 20px;"><%=facilityName%></p>
                            <label style="font-size: 20px;"><strong>Reminder</strong></label>                        
                            <% int j = 0;
                                for (int i = 0; i < reminderList.length; i++) {
                                    j += 1;%>
                            <p style="font-size: 18px;"><%=j%>. <%=reminderList[i]%></p>
                            <%  }%>
                        </div>
                    </div>
                    <div>
                        <form action="/bookFacility" method="post">
                            <table class="detailTable" >
                                <tbody>
                                    <tr><td class="labelTd">Operating Day</td><td><%=selectedFacility.getOperatingDay()%></td></tr>
                                    <tr><td class="labelTd">Operating Hours</td><td><%=formatTime.format(selectedFacility.getStartHour())%> - <%=formatTime.format(selectedFacility.getEndHour())%></td></tr>
                                    <tr><td class="labelTd">Selected Date</td><td><%=formatSelectDate.format(selectedDate)%></td></tr>
                                    <tr><td class="labelTd">Selected Time</td><td><%=formatTime.format(selectedTime)%></td></tr>
                                    <tr><td class="labelTd">Location</td><td><%=selectedFacility.getHostelID().getBuilding()%> of <%=selectedFacility.getHostelID().getLocation()%></td></tr>
                                    <tr><td class="labelTd">Status</td><td><%=selectedFacility.getStatus()%></td></tr>

                                    <% if (selectedFacility.getStatus().equals("Available")) { %>
                                    <tr><td class="labelTd"><strong>How much would you like to book?</strong></td>
                                        <td>                            
                                            <select class="bookCapacityOpt" name="bookCapacity">
                                                <option value="1" selected="">1</option>
                                                <option value="2">2</option>
                                                <option value="3">3</option>
                                                <option value="4">4</option>
                                                <option value="5">5</option>
                                                <option value="6">6</option>
                                                <option value="7">7</option>
                                                <option value="8">8</option>
                                                <option value="9">9</option>
                                            </select>
                                        </td>
                                    </tr>                                        
                                    <%}%>
                                </tbody>
                            </table>
                            <input type="hidden" name="sid" value="<%=selectedFacility.getFacilityID()%>">
                            <input type="hidden" name="selectedDate" value="<%=formatSelectDate.format(selectedDate)%>">  
                            <input type="hidden" name="selectedTime" value="<%=formatTime.format(selectedTime)%>">                     
                            <button type="submit" id="submitBtn" class="bookBtn" style="display: none;"></button>
                        </form>
                        <% if (selectedFacility.getStatus().equals("Available")) { %>                           
                        <button onclick="addConfirmation()" class="bookBtn">Book</button>
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
        function addConfirmation() {
            if (confirm("Are you sure you want to add this booking ?")) {
                document.getElementById("submitBtn").click();
            }
        }
    </script>
</html>
