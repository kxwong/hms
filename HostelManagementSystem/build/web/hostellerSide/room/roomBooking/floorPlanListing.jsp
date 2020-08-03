<%@page import="Controller.Crypto"%>
<%@page import="java.util.Base64"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Room"%>
<%@page import="Model.Floorplan"%>
<%@page import="java.util.List"%>
<%@page import="Model.Hosteller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/room/floorPlanListing.css">
        <title>Hostel Management System</title>
    </head>
    <%
        String floorPlanExist = new String();
        Hosteller hosteller = new Hosteller();

        List<Floorplan> floorplanList = new ArrayList<Floorplan>();
        List<Room> roomList = new ArrayList<Room>();
        List<String> blockList = new ArrayList<String>();
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("floorPlanList") != null) {
                    floorplanList = (List<Floorplan>) session.getAttribute("floorPlanList");
                    floorPlanExist = "true";
                } else {
                    floorPlanExist = "false";
                }

                if (session.getAttribute("roomList") != null) {
                    roomList = (List<Room>) session.getAttribute("roomList");
                    floorPlanExist = "true";
                } else {
                    floorPlanExist = "false";
                }
            }
    %>
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
                <div class="chTitle">Floor Plan Listing</div>
            </div>
            <div class="cBody">    
                <p>Current branch is <%=hosteller.getEmpdetails().getBranch()%></p>
                <hr />
                <div class='cbContent roomDetail1'>
                    <%
                        for (int j = 0; j < floorplanList.size(); j++) {
                            if (floorplanList.get(j).getStatus().equals("Active")) {
                                if (!blockList.contains(floorplanList.get(j).getHostelID().getBuilding())) {
                                    blockList.add(floorplanList.get(j).getHostelID().getBuilding());
                                }
                            }
                        }
                    %> 
                    <% for (int h = 0; h < blockList.size(); h++) {%>
                    <div>
                        <p><%=blockList.get(h)%></p>
                        <%
                            for (int i = 0; i < floorplanList.size(); i++) {
                                String image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(floorplanList.get(i).getImage()));
                                if (floorplanList.get(i).getHostelID().getBuilding().equals(blockList.get(h))) {
                        %> 
                        <% if (floorplanList.get(i).getRoomList().size() > 0) {%>
                        <div class="roomDiv">
                            <a href="/retrieveFloorPlanDetails?fid=<%=encrypt.UNEncode(floorplanList.get(i).getFloorplanID())%>" >
                                <img class="floorPlanItem" src="<%=image%>" style="float:left;">
                                <center><p>Level <%=floorplanList.get(i).getFloor()%></p></center>
                            </a>
                        </div>                                 
                        <%}%>
                        <%}%> 
                        <%}%>
                    </div>
                    <hr />
                    <%}%>
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
</html>
