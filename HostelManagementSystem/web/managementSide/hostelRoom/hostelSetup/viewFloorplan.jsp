
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Model.Hosteller"%>
<%@page import="Model.HostellerManager"%>
<%@page import="Model.RoomManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Model.Room"%>
<%@page import="Controller.Crypto"%>
<%@page import="java.util.Base64"%>
<%@page import="Model.Floorplan"%>
<%@page import="java.util.List"%>
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
        EntityManager mgr = (EntityManager) session.getAttribute("mgr");
        Floorplan floorplan = (Floorplan) session.getAttribute("floorplan");
        RoomManager roomManager = new RoomManager(mgr);
        HostellerManager hostellerManager = new HostellerManager(mgr);
        List<Room> activeRoomList = roomManager.findByFloorplanStatus(floorplan.getFloorplanID(), "Available");
        int actualRoom = activeRoomList.size();
        List<Room> roomList = roomManager.findAllByFloorplan(floorplan);
        int tenantQuantity = 0;
        for (int i = 0; i < roomList.size(); i++) {
            List<Hosteller> tempoHostellerList = hostellerManager.findByRoom(roomList.get(i).getRoomNo());
            tenantQuantity += tempoHostellerList.size();
        }
        Crypto crypto = new Crypto();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/viewFloorplan.css">
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
                <a href='/retrieveBillListing'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Rental</div></div></a>
                <a href='/retrieveFacilityListing'><div class="spoFacilities spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Facility</div></div></a>
                <a href='/retrieveNoticeListing'><div class="spoNotice spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Notice</div></div></a>
                <a href='/retrieveIssueListing?t=0'><div class="spoIssue spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Issue</div></div></a>
                <a href='/retrieveTenantEntryListing?t=0'><div class="spoEntry spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Entry</div></div></a>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Floor Plan's Overview &#10148; View Floor Plan</div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <div class="cbFPdetails">
                        <div class="cbfImage"><img width="100%" height="100%" src="<%="data:image/jpeg;base64," + new String(Base64.getEncoder().encode(floorplan.getImage()))%>"></div>
                        <table>
                            <tr>
                                <td><div class="cbLabel">Hostel</div><div></div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=floorplan.getHostelID().getLocation()%> <%=floorplan.getHostelID().getBuilding()%> Floor <%=floorplan.getFloor()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">People Capacity</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=floorplan.getPeopleCapacity()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Current Tenant</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=tenantQuantity%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Room Capacity</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=floorplan.getRoomCapacity()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Occupied Room</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=actualRoom%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Status</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=floorplan.getStatus()%></div></td>
                            </tr>
                        </table>
                    </div>
                    <a href="/retrieveFloorplanListing"><button type="button" class="cbAdd">&#9166; Back</button></a>
                    <a href='/initializeEditFloorplan?p=<%=crypto.FPEncode(floorplan.getFloorplanID())%>'><button class="cbAdd cBAddright">Edit Floorplan</button></a>
                    <a href='/initializeAddRoomStep1?p=<%=crypto.FPEncode(floorplan.getFloorplanID())%>'><button class="cbAdd cBAddright">Add Room</button></a>
                    <div class="cbList">
                        <table class="cbRoomList">
                            <tr>
                                <th>Room</th>
                                <th>Tenant</th>
                            </tr>
                            <%
                                for (int i = 0; i < roomList.size(); i++) {
                                    List<Hosteller> tempoHostellerList = hostellerManager.findByRoom(roomList.get(i).getRoomNo());
                                    for (int j = 0; j < tempoHostellerList.size(); j++) {
                                        if (j == 0) {
                            %>
                            <tr>
                                <td><a href="/retrieveRoomDetail?p=<%=crypto.REncode(roomList.get(i).getRoomNo())%>" class="cbrlRequest"><%=roomList.get(i).getRoomNo()%></a></td>
                                <td><a href="/retrieveTenantDetails?p=<%=crypto.HTEncode(tempoHostellerList.get(j).getHostellerID())%>" class="cbrlRequest"><%=tempoHostellerList.get(j).getFirstName()%> <%=tempoHostellerList.get(j).getMiddleName()%> <%=tempoHostellerList.get(j).getLastName()%> </a></td>
                            </tr>
                            <%
                            } else {
                            %>
                            <tr>
                                <td></td>
                                <td><a href="/retrieveTenantDetails?p=<%=crypto.REncode(tempoHostellerList.get(j).getHostellerID())%>" class="cbrlRequest"><%=tempoHostellerList.get(j).getFirstName()%> <%=tempoHostellerList.get(j).getMiddleName()%> <%=tempoHostellerList.get(j).getLastName()%> </a></td>
                            </tr>
                            <%
                                    }
                                }
                                if (tempoHostellerList.size() == 0) {
                            %>
                            <tr>
                                <td><a href="/retrieveRoomDetail?p=<%=crypto.REncode(roomList.get(i).getRoomNo())%>" class="cbrlRequest"><%=roomList.get(i).getRoomNo()%></a></td>
                                <td>No Tenants</td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                            <% if (roomList.isEmpty()) {%>
                            <tr>
                                <td colspan="2"><center>Sorry, there are no any matching data currently.</center></td>
                            </tr>
                            <%}%>
                        </table>
                    </div>
                </div>
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
