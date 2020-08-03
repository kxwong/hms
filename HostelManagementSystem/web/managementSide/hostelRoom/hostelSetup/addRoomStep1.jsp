
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="java.io.InputStream"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="Model.Room"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Base64"%>
<%@page import="Model.Floorplan"%>
<%@page import="Model.Roommapping"%>
<%@page import="Controller.Crypto"%>
<%
    try {
        AccountManager accountManager = new AccountManager((EntityManager) session.getAttribute("mgr"));
        Account account = (Account) session.getAttribute("account");
        if (!account.equals(accountManager.findAccount(account.getUsername(), account.getPassword()))) {
            throw new Exception("Unauthorized account");
        }
        if (account.getLevel() != 3) {
            throw new Exception("Unauthorized account");
        }
        Floorplan floorplan = (Floorplan) session.getAttribute("floorplan");
        List<Room> roomList = (List) session.getAttribute("roomList");
        byte[] picture = floorplan.getImage();
        InputStream in = new ByteArrayInputStream(picture);
        BufferedImage buf = ImageIO.read(in);
        String RoomArray = "";
        double transfromFact = (double) 1040 / (double) buf.getWidth();
        Crypto crypto = new Crypto();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/addRoom.css">
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.js"></script>
        <script type="text/javascript" src="../../managementSide/source/js/highlightRomm.js"></script>
        <title>Hostel Management System</title>
    </head>
    <style>
        <%
            for (int j = 0; j < roomList.size(); j++) {
                Room room = roomList.get(j);
                Roommapping roomMapping = room.First();
                String assignedCoor = "";
                String xCoor = "";
                String yCoor = "";
                do {
                    int width = (int) (buf.getWidth() * transfromFact);
                    int height = (int) (buf.getHeight() * transfromFact);
                    assignedCoor += roomMapping.getCoordinateX() * 100 + "% " + roomMapping.getCoordinateY() * 100 + "%";
                    xCoor += Math.round(roomMapping.getCoordinateX() * width);
                    yCoor += Math.round(roomMapping.getCoordinateY() * height);
                    if (!roomMapping.IsLast()) {
                        assignedCoor += ",";
                        xCoor += " ";
                        yCoor += " ";
                    }
                    roomMapping = roomMapping.Next();
                } while (!roomMapping.IsFirst());
                xCoor = "'xCoor': '" + xCoor + "',";
                yCoor = "'yCoor': '" + yCoor + "'";
                String roomCoor = "{" + xCoor + yCoor + "}";
                RoomArray += roomCoor;
                if (j + 1 != roomList.size()) {
                    RoomArray += ",";
                }
        %>
        #canvasDiv > div:nth-child(<%=j + 1%>) {
            clip-path:polygon(<%=assignedCoor%>);
            -webkit-clip-path:polygon(<%=assignedCoor%>);
        }
        <%
            }
            RoomArray = "[" + RoomArray + "]";

        %>

    </style>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveFloorplanListing'><div class="hBG"><div class="hOption hChoose"><p>Hostel Setup</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/retrieveRoomRequestListing?t=0'><div class="hBG"><div class="hOption"><p>Room Allocation</p></div></div></a>
            </div>
            <div class="accountCorner"><%=crypto.UNDecode(account.getUsername())%> &#11163;
                <div class="accountSettingList">
                    <a href="/admin/account/changePassword"><div class="accountSettingOption">Change Password</div></a>
                    <a href="/admin/account/authorizeAccount"><div class="accountSettingOption">Authorize Account</div></a>
                    <a href="/accountLogout"><div class="accountSettingOption">Log Out</div></a>
                </div>
            </div>
        </div>
        <div class="sidePanel">
            <div class="spCover">
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveFloorplanListing'><div class="spoRoom spoBg"><div class="spOption spoChoose"><center><div class="spImg " style="background-image:url(../../managementSide/source/s_room.png)" ></div><p>Room</p></center></div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveRegistrationListing?t=0'><div class="spoTenant spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Tenant</div></div></a>
                <a href='/retrieveBillListing?t=0'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Rental</div></div></a>
                <a href='/retrieveFacilityListing'><div class="spoFacilities spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Facility</div></div></a>
                <a href='/retrieveNoticeListing'><div class="spoNotice spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Notice</div></div></a>
                <a href='/retrieveIssueListing?t=0'><div class="spoIssue spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Issue</div></div></a>
                <a href='/retrieveTenantEntryListing?t=0'><div class="spoEntry spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Entry</div></div></a>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Floor Plan's Overview &#10148; View Floor Plan &#10148; Add Room</div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <div class="cbLabel">Highlight the area of room mapping. </div>
                    <form action="/storeRoomStep1" method="post">
                        <input id="rec" type="radio" name="selectType" onchange="getCanvas()" value="R" checked="checked"> <span class="cbLabel">Rectangular</span> 
                        <input id="poly" type="radio" name="selectType" onchange="getCanvas()" value="P"><span class="cbLabel"> Polygon </span>
                        <img id="floorplanImg" style="display: none;" src='<%="data:image/jpeg;base64," + new String(Base64.getEncoder().encode(floorplan.getImage()))%>' >
                        <div id='roomArea'></div>
                        <div id='canvasDiv'>
                            <%
                                for (int j = 0; j < roomList.size(); j++) {
                            %>
                            <div class="room" onmouseover="clearCanvas()" onclick="showRoom(this.id)" id="<%=roomList.get(j).getRoomNo()%>"></div> 
                            <%
                                }
                            %>
                        </div>
                        <input id="coordinate" type="hidden" name="coordinate"> 
                        <a href="/retrieveFloorplanListing"><button type="button" class="cbAdd">&#9166; Back</button></a>
                        <a href="javascript:clear()" ><button type="button" class="cbAdd">Clear</button></a>
                        <input type="submit" value="Next" class="cbAdd cBAddright">
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>

<script type="text/javascript">
    function clear()
    {
        location.reload();
    }
    $(window).resize(function () {
        location.reload();
    });
    var existRoom = <%=RoomArray%>;
    var xLeft = $("#roomArea").offset().left;
    var xTop = $("#roomArea").offset().top;
    getCanvas();
    function getCanvas() {
        var r = document.getElementById("rec");
        var p = document.getElementById("poly");
        if (r.checked) {
            prepareRecCanvas('<%="data:image/jpeg;base64," + new String(Base64.getEncoder().encode(floorplan.getImage()))%>', xLeft, xTop, existRoom);
        } else if (p.checked) {
            preparePolCanvas('<%="data:image/jpeg;base64," + new String(Base64.getEncoder().encode(floorplan.getImage()))%>', xLeft, xTop, existRoom);
        }
    }
    function showRoom(roomNo) {
        alert("Room No : " + roomNo);
    }
    window.onload = showMsg;
    function showMsg() {
        var errmsg = '<%=session.getAttribute("error")%>';
        if (errmsg !== 'null') {
            alert(errmsg);
    <%
        session.removeAttribute("error");
    %>
        }
    }
</script>
<%
    } catch (Exception ex) {
        session.invalidate();
        session = request.getSession();
        session.setAttribute("error", ex.getLocalizedMessage());
        response.sendRedirect("/login/admin");
    }
%>