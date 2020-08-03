
<%@page import="Model.Roommapping"%>
<%@page import="Model.HostellerManager"%>
<%@page import="Model.Hosteller"%>
<%@page import="java.util.List"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="java.util.Base64"%>
<%@page import="Controller.Crypto"%>
<%@page import="Model.Room"%>
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
        Room room = (Room) session.getAttribute("room");
        Crypto crypto = new Crypto();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/viewRoom.css">
        <title>Hostel Management System</title>
    </head>
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
                <div class="chTitle">Floor Plan's Overview &#10148; View Floor Plan &#10148; View Room</div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <div class="cbFPdetails">
                        <div class="cbfImage"><img width="100%" height="100%" src="<%="data:image/jpeg;base64," + new String(Base64.getEncoder().encode(room.getImage()))%>"></div>
                        <table>
                            <tr>
                                <td><div class="cbLabel">RoomNo</div><div></div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=room.getRoomNo()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Description</div><div></div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=room.getDescription()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">People Capacity</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=room.getCapacity()%> </div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Rental Fee</div></td>
                                <td> : </td>
                                <td><div class="cbLabel">RM <%=room.getRentalFee()%> <%=room.getCapacity() > 1 ? " per pax" : "per room"%></div></td>
                            </tr>
                            <%
                                HostellerManager hostellerManager = new HostellerManager((EntityManager) session.getAttribute("mgr"));
                                List<Hosteller> hostellerList = hostellerManager.findByRoom(room.getRoomNo());
                                for (int j = 0; j <= hostellerList.size(); j++) {
                                    if (hostellerList.size() == 0) {
                            %>

                            <tr>
                                <td><div class="cbLabel">Current Tenant</div></td>
                                <td> : </td>
                                <td><div class="cbLabel">No Tenant</div></td>
                            </tr>
                            <%
                            } else {
                                if (j == 0) {
                            %>

                            <tr>
                                <td><div class="cbLabel">Current Tenant</div></td>
                                <td></td>
                                <td><div class="cbLabel"><a href="/retrieveTenantDetails?p=<%=crypto.HTEncode(hostellerList.get(j).getHostellerID())%>" class="cbrlRequest"><%=hostellerList.get(j).getFirstName()%> <%=hostellerList.get(j).getMiddleName()%> <%=hostellerList.get(j).getLastName()%></a></div></td>
                            </tr>
                            <%
                            } else {
                                if (j != hostellerList.size()) {
                            %>
                            <tr>
                                <td></td>
                                <td></td>
                                <td><div class="cbLabel"><a href="/retrieveTenantDetails?p=<%=crypto.HTEncode(hostellerList.get(j).getHostellerID())%>" class="cbrlRequest"><%=hostellerList.get(j).getFirstName()%> <%=hostellerList.get(j).getMiddleName()%> <%=hostellerList.get(j).getLastName()%></a></div></td>
                            </tr>
                            <%
                                            }
                                        }
                                    }
                                }
                            %>
                            <tr>
                                <td><div class="cbLabel">Status</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=room.getStatus()%></div></td>
                            </tr>
                        </table>
                    </div>

                </div>
                <a href="/retrieveFloorplanDetails?p=<%=crypto.FPEncode(room.getFloorplanID().getFloorplanID())%>"><button type="button" class="cbAdd">&#9166; Back</button></a>
                <a href='/initializeEditRoomDetails?p=<%=crypto.REncode(room.getRoomNo())%>'><button class="cbAdd cBAddright">Edit Room Details</button></a>
            </div>

            <div class="cHeader">
                <div class="chTitle">Room <%=room.getRoomNo()%> located at <%=room.getFloorplanID().getHostelID().getLocation()%> <%=room.getFloorplanID().getHostelID().getBuilding()%> Floor <%=room.getFloorplanID().getFloor()%> </div>
            </div>
            <div class="cBody">
                <%
                    byte[] picture = room.getFloorplanID().getImage();
                    InputStream in = new ByteArrayInputStream(picture);
                    BufferedImage buf = ImageIO.read(in);
                    double transfromFact = (double) 500 / (double) buf.getHeight();
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
                <center>
                    <div style="height:540px; width: <%=Math.round(transfromFact * buf.getWidth()) + 40%>px;"class="cbFPdetails">
                        <div style="background-image:url('<%="data:image/jpeg;base64," + new String(Base64.getEncoder().encode(room.getFloorplanID().getImage()))%>');" class='roomMappingArea'>
                            <div class="room" style="-clip-path:polygon(<%=assignedCoor%>);
                                 -webkit-clip-path:polygon(<%=assignedCoor%>);"></div>
                        </div>
                    </div>
                </center>
                <a href='/initializeEditRoomMapping?p=<%=crypto.REncode(room.getRoomNo())%>'><button class="cbAdd">Edit Room Location</button></a>
            </div>
        </div>
    </body>
</html>

<script type="text/javascript">
    window.onload = showMsg;
    function showMsg() {
        var errmsg = '<%=session.getAttribute("error")%>';
        if (errmsg !== 'null') {
            alert(errmsg);
    <%
        session.removeAttribute("error");
    %>
        }
        var successmsg = '<%=session.getAttribute("success")%>';
        if (successmsg !== 'null') {
            alert(successmsg);
    <%
        session.removeAttribute("success");
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