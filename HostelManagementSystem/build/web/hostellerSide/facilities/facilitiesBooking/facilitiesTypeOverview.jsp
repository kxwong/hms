<%@page import="Controller.Crypto"%>
<%@page import="java.util.Base64"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Model.Hosteller"%>
<%@page import="Model.Facility"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/facilities/facilitiesViewing.css">
        <title>Hostel Management System</title>
    </head>
    <%
        List<Facility> indoorFacilityList = new ArrayList<Facility>();
        List<Facility> outdoorFacilityList = new ArrayList<Facility>();
        Hosteller hosteller = new Hosteller();
        SimpleDateFormat formatSelectDate = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date();
        Date maxDate = new Date();
        maxDate.setDate(maxDate.getDate() + 14);
        List<String> facilityTypeList = new ArrayList();
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller") == null && session.getAttribute("facilityList") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                indoorFacilityList = (List<Facility>) session.getAttribute("facilityIndoorList");
                outdoorFacilityList = (List<Facility>) session.getAttribute("facilityOutdoorList");

                curDate.setDate(curDate.getDate() + 1);
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
                <div class="chTitle">Facilities Overview</div>
            </div>
            <div class="cBody">                         
                <div class='cbContent criteriaSearching'>
                    <h2>Current facilities available in branch <%=hosteller.getEmpdetails().getBranch()%></h2>
                </div>
                <div class="cbContent"> 
                    <div style="text-align: center; font-size: 25px; margin-top: 10px; margin-bottom: 5px; margin-top: 40px; font-weight: bold;">Indoor</div>
                    <hr style="margin-bottom:25px;"/>
                    <%
                        if (indoorFacilityList.size() > 0) {
                    %>                  
                    <%
                        for (int n = 0; n < indoorFacilityList.size(); n++) {
                            String image = new String();
                            if (indoorFacilityList.get(n).getStatus().equals("Available")) {
                                image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(indoorFacilityList.get(n).getImage()));
                    %>
                    <a href="/retrieveFacilityTimetable?fid=<%=encrypt.UNEncode(indoorFacilityList.get(n).getFacilityID())%>&curDate=<%=formatSelectDate.format(curDate)%> ">
                        <div class="selectionContainer">
                            <div class="container" style="background-image: url(<%=image%>);" title="<%=indoorFacilityList.get(n).getStatus()%>">
                            </div> 
                            <div class="centered" id="center<%=indoorFacilityList.get(n).getFacilityID()%>"><%=indoorFacilityList.get(n).getDescription().split("@")[0]%></div>  
                        </div>
                    </a>
                    <% }
                        }
                    } else { %>
                    <h3>Currently there are no facility for this category in this branch.</h3>
                    <%}%>
                    <div style="text-align: center; font-size: 25px; margin-top: 10px; margin-bottom: 5px; margin-top: 40px; font-weight: bold;">Outdoor</div>
                    <hr style="margin-bottom:25px;"/>
                    <%
                        if (outdoorFacilityList.size() > 0) {
                    %>                                            
                    <%
                        for (int n = 0; n < outdoorFacilityList.size(); n++) {
                            String image = new String();
                            if (outdoorFacilityList.get(n).getStatus().equals("Available")) {
                                image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(outdoorFacilityList.get(n).getImage()));
                    %>
                    <a href="/retrieveFacilityTimetable?fid=<%=encrypt.UNEncode(outdoorFacilityList.get(n).getFacilityID())%>&curDate=<%=formatSelectDate.format(curDate)%> ">
                        <div class="selectionContainer">
                            <div class="container" style="background-image: url(<%=image%>);" title="<%=outdoorFacilityList.get(n).getStatus()%>">
                            </div> 
                            <div class="centered" id="center<%=outdoorFacilityList.get(n).getFacilityID()%>"><%=outdoorFacilityList.get(n).getDescription().split("@")[0]%></div>  
                        </div>
                    </a>
                    <% }
                        }
                    } else { %>
                    <h3>Currently there are no facility for this category in this branch.</h3>
                    <%}%>                   



                </div>
                <div>
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
    function setBgBlur(picID) {

        var id = picID;
        id.style.opacity = 1.0;

        console.log(id.style.opacity);
    }

</script>

</html>
