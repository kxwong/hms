<%@page import="Model.Roommapping"%>
<%@page import="Controller.Crypto"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.util.Base64"%>
<%@page import="Model.Floorplan"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Room"%>
<%@page import="Model.Room"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="Model.Hosteller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/room/roomSelection.css">
        <title>Hostel Management System</title>
    </head>
    <%
        Hosteller hosteller = new Hosteller();
        List<Room> roomList = new ArrayList<Room>();
        Map<String, List> coordinateMap = new HashMap<String, List>();
        Floorplan curFloorplan = new Floorplan();
        String image = "empty";
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("floorplan") == null) {
                    throw new Exception();
                } else {
                    curFloorplan = (Floorplan) session.getAttribute("floorplan");
                    image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(curFloorplan.getImage()));
                    if (session.getAttribute("roomList") == null) {
                        throw new Exception();
                    } else {
                        roomList = (List<Room>) session.getAttribute("roomList");
                    }
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
                <a href='/retrieveAllFacilitiesWithinBranch'><div class="spoFacilities spoBg"><div class="spOption "><center><div class="spImg"></div></center>Facility</div></div></a>           
                <a href='/retrieveAllAnnouncement'><div class="spoNotice spoBg"><div class="spOption "><center><div class="spImg"></div></center>Notice</div></div></a>   
                <a href='/retrieveAllCase'><div class="spoReporting spoBg"><div class="spOption"><center><div class="spImg "></div></center>Report</div></div></a>             
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle"><a href="/hosteller/room/floorPlanListing" style="text-decoration: none;">Floor Plan Listing</a> > Room Selection</div>
            </div>
            <div class="cBody">    
                <p>Floor Plan of <%=curFloorplan.getHostelID().getBuilding()%>, Level <%=curFloorplan.getFloor()%></p>
                <hr />
                <center>                                             
                    <%
                        byte[] picture = roomList.get(0).getFloorplanID().getImage();
                        InputStream in = new ByteArrayInputStream(picture);
                        BufferedImage buf = ImageIO.read(in);
                        double transfromFact = (double) 500 / (double) buf.getHeight();
                    %>
                    <div style="height:540px; width: <%=Math.round(transfromFact*buf.getWidth()) + 40%>px;"class="cbFPdetails">
                        <div style="background-image:url('<%="data:image/jpeg;base64," + new String(Base64.getEncoder().encode(roomList.get(0).getFloorplanID().getImage()))%>');" class='roomMappingArea'>                       
                            <%
                                for (int i = 0; i < roomList.size(); i++) {
                                    Room room = roomList.get(i);
                                    Roommapping roomMapping = room.First();
                                    String assignedCoor = "";
                                    do {
                                        assignedCoor += roomMapping.getCoordinateX() * 100 + "% " + roomMapping.getCoordinateY() * 100 + "%";
                                        if (!roomMapping.IsLast()) {
                                            assignedCoor += ",";
                                        }
                                        roomMapping = roomMapping.Next();
                                    } while (!roomMapping.IsFirst());
                            %>       
                            <a href="/retrieveRoomDetails?rid=<%=encrypt.UNEncode(roomList.get(i).getRoomNo())%>">
                                <div class="room" style="-clip-path:polygon(<%=assignedCoor%>);
                                     -webkit-clip-path:polygon(<%=assignedCoor%>);" title="Room No : <%=roomList.get(i).getRoomNo().substring(4)%> <%=roomList.get(i).getStatus()%>" onclick="/retrieveRoomDetails" href="/retrieveRoomDetails">
                                </div>  
                            </a>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </center>
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