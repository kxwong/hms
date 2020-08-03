<%@page import="Model.Roommapping"%>
<%@page import="Controller.Crypto"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="java.util.Base64"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Hostel"%>
<%@page import="java.util.List"%>
<%@page import="Model.Hosteller"%>
<%@page import="Model.Room"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/room/currentRoomStatus.css">
        <title>Hostel Management System</title>
    </head>
    <script>
        function closePopUp() {
            var fail = document.getElementById("roomStatusPopUp");
            fail.style.display = "none";
        }
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
        Hosteller hosteller = new Hosteller();
        String roomStatus = new String();
        int hostellerAmt = 0;
        Room currentRoom = new Room();
        Crypto encrypt = new Crypto();

        String location = new String();
        String image = new String();
        List<Hosteller> currentRoomHostellerList = new ArrayList();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("roomStatus") == null || session.getAttribute("roomStatus") == "false") {
                    roomStatus = "false";
                }

            }
    %>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>   
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveCurrentRoomStatus'><div class="hBG"><div class="hOption hChoose"><p>Current Room</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/retrieveFloorPlanList'><div class="hBG"><div class="hOption "><p>Room Booking</p></div></div></a>                
                <a href='/retrieveRoomBooking'><div class="hBG"><div class="hOption "><p>Booking History</p></div></div></a>
                <div class="topRightUserDiv">
                    <p style="position:fixed;right:60px; font-size: 20px; color: white;"><%=hosteller.getFirstName()%> <%=hosteller.getMiddleName()%> <%=hosteller.getLastName()%></p>
                    <p style="position:fixed; right:15px;"><a href="/hostellerLogout"><img class="logoutIcon" title="Logout" style="width: 35px; height: 35px;" onclick="logout()" src="../../../hostellerSide/source/logout_icon.png"></a></p>                   
                </div>
            </div>
        </div>
        <div class="sidePanel">
            <div class="spCover">
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveCurrentRoomStatus'><div class="spoRoom spoBg"><div class="spOption spoChoose"><center><div class="spImg" style="background-image:url(../../../hostellerSide/source/s_room.png)"></div><p>Room</p></center></div></div></a> 
                <div class="spSubcover"><div class="spSpaceDn"></div></div>           
                <a href='/retrieveHosteller'><div class="spoTenant spoBg"><div class="spOption "><center><div class="spImg" ></div></center>Profile</div></div></a>          
                <a href='/retrieveCurrentBillingList'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg"></div></center>Rental</div></div></a>
                <a href='/retrieveAllFacilitiesWithinBranch'><div class="spoFacilities spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Facility</div></div></a>
                <a href='/retrieveAllAnnouncement'><div class="spoNotice spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Notice</div></div></a>
                <a href='/retrieveAllCase'><div class="spoReporting spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Report</div></div></a>            
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle"><a href="/retrieveCurrentRoomStatus">Your room details</a></div>
            </div>
            <div class="cBody">                               
                <%if (session.getAttribute("bookSuccess") != null && session.getAttribute("bookSuccess") == "false" && session.getAttribute("message") != null) {
                        String message = (String) session.getAttribute("message");
                        session.removeAttribute("bookSuccess");
                        session.removeAttribute("message");
                %>
                <div onclick="closeFail()" class="popup" id="failPopUp">
                    <center>
                        <div class="cover">
                            <img class="popUpicon" src="../../../hostellerSide/source/failed.png"/>
                            <div class="message"><%=message%></div>
                        </div>
                    </center>
                </div>                    
                <%}%>

                <% if (session.getAttribute("bookSuccess") != null && session.getAttribute("bookSuccess") == "true" && session.getAttribute("message") != null) {
                        String message = (String) session.getAttribute("message");
                        session.removeAttribute("bookSuccess");
                        session.removeAttribute("message");
                %>
                <div onclick="closeSuccess()" class="popup" id="successPopUp">
                    <center>
                        <div class="cover">
                            <img class="popUpicon" src="../../../hostellerSide/source/success.png"/>
                            <div class="message"><%=message%></div>
                        </div>
                    </center>
                </div>                    
                <%}%>                


                <% if (session.getAttribute("roomStatus") == "false") {%>
                <div onclick="closePopUp()" class="popup" id="roomStatusPopUp">
                    <div class="cover">
                        <img class="popUpicon" src="../../../hostellerSide/source/alert.png"/>
                        <div class="message">No current room available, please navigate to Room Booking section for reservation</div>
                    </div>
                </div> 
                <% session.removeAttribute("roomStatus");
                    }%>                      

                <%
                    boolean isRoom = true;

                    try {
                        currentRoom = hosteller.getStayRoom();
                        Hostel currentHostel = currentRoom.getFloorplanID().getHostelID();
                        location = "Level " + String.valueOf(currentRoom.getFloorplanID().getFloor()) + " of " + (String) currentHostel.getBuilding() + " in " + currentHostel.getLocation();
                        image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(currentRoom.getImage()));
                    } catch (Exception ex) {
                        isRoom = false;
                    }

                    if (!isRoom) { %>
                <div class='cbContent roomDetail1'>
                    <img src="../../../hostellerSide/source/notAvailable.jpg" style="width:350px;height:350px;display: inline-block;border-radius: 12px;box-shadow: 3px 3px 5px #aaa;" />
                    <div style="width:60%;display: inline-block;margin-left: 5%;padding-bottom: 100px;" class="facilitiesList">
                        <table>
                            <tr><td class="txtLbl">Room ID</td><td>N/A</td></tr>
                            <tr><td class="txtLbl">Room Type</td><td>N/A</td></tr>
                            <tr><td class="txtLbl">Location</td><td>N/A</td></tr>
                            <tr><td class="txtLbl">Status</td><td>N/A</td></tr>
                            <tr><td class="txtLbl" rowspan="1" style="vertical-align: top;">Current tenant</td><td>N/A</td></tr>
                        </table>
                    </div>                                       
                </div>                        
                <%
                } else {
                    currentRoomHostellerList = (List) session.getAttribute("hostellerList");
                    hostellerAmt = currentRoomHostellerList.size();
                %>
                <div class='cbContent roomDetail1'>

                    <div style="width:34%; height: 100%; display: inline-block;">

                        <div class="roomPic" style="background-image: url(<%=image%>)">                       
                        </div>

                        <form action="/moveOutRequest" method="post">
                            <button id="submitBtn" hidden="" type="submit"></button>
                        </form>
                        <center><button style="width: 100%;" onclick="callDecisionBox()" >Move Out</button></center>                    
                    </div>                  
                    <div style="width:60%;display: inline-block;margin-left: 5%; padding-bottom: 150px;" class="facilitiesList">
                        <table>
                            <tr><td class="txtLbl">Room ID</td><td><%=currentRoom.getRoomNo()%></td></tr>                           
                            <%
                                int capacity = currentRoom.getCapacity();
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
                            <tr><td class="txtLbl">Room Type</td><td><%=roomType%></td></tr>
                            <tr><td class="txtLbl">Location</td><td><%=location%></td></tr>
                            <tr><td class="txtLbl">Status</td><td>Available</td></tr>
                            <% int a = 0;
                                for (int i = 0; i < currentRoomHostellerList.size(); i++) {
                                    a += 1;
                                    if (i == 0) {%>
                            <tr>
                                <td style="background-color: #aaa; color: white;" rowspan="<%=hostellerAmt%>" style="vertical-align: top;">Current tenant</td>
                                <td><%=a%>. <%=currentRoomHostellerList.get(i).getFirstName()%> <%=currentRoomHostellerList.get(i).getMiddleName()%> <%=currentRoomHostellerList.get(i).getLastName()%></td>
                            </tr>
                            <% } else {%>
                            <tr><td><%=a%>. <%=currentRoomHostellerList.get(i).getFirstName()%> <%=currentRoomHostellerList.get(i).getMiddleName()%> <%=currentRoomHostellerList.get(i).getLastName()%></td></tr>
                            <%}%>
                            <%}%>
                        </table>
                    </div>                                       
                </div>

            </div>
            <div class="cBody">    
                <h2>Floor Plan of <%=currentRoom.getFloorplanID().getHostelID().getBuilding()%>, Level <%=currentRoom.getFloorplanID().getFloor()%></h2>
                <hr />
                <center>                                             
                    <%
                        byte[] picture = currentRoom.getFloorplanID().getImage();
                        InputStream in = new ByteArrayInputStream(picture);
                        BufferedImage buf = ImageIO.read(in);
                        double transfromFact = (double) 500 / (double) buf.getHeight();
                    %>
                    <div style="height:540px; width: <%=Math.round(transfromFact*buf.getWidth()) + 40%>px;"class="cbFPdetails">
                        <div style="background-image:url('<%="data:image/jpeg;base64," + new String(Base64.getEncoder().encode(currentRoom.getFloorplanID().getImage()))%>');" class='roomMappingArea'>                       
                            <%
                                Roommapping roomMapping = currentRoom.First();
                                String assignedCoor = "";
                                do {
                                    assignedCoor += roomMapping.getCoordinateX() * 100 + "% " + roomMapping.getCoordinateY() * 100 + "%";
                                    if (!roomMapping.IsLast()) {
                                        assignedCoor += ",";
                                    }
                                    roomMapping = roomMapping.Next();
                                } while (!roomMapping.IsFirst());
                            %>
                            <div class="room" style="-clip-path:polygon(<%=assignedCoor%>);
                                 -webkit-clip-path:polygon(<%=assignedCoor%>);" title="Room No : <%=currentRoom.getRoomNo().substring(4)%>">
                            </div>  
                        </div>
                    </div>
                </center>
            </div>
            <%}%>
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
            if (confirm('Are you sure that you want to move out?')) {
                document.getElementById("submitBtn").click();
            }
        }
    </script>
</html>
