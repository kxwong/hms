
<%@page import="Model.Hosteller"%>
<%@page import="Model.HostellerManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Room"%>
<%@page import="Model.RoomManager"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Controller.Crypto"%>
<%@page import="java.util.Base64"%>
<%@page import="Model.Hostel"%>
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
        List<Floorplan> floorplanList = (List) session.getAttribute("floorplanList");
        List<String> locationList = (List) session.getAttribute("locationList");
        List<List<String>> buildingList = (List) session.getAttribute("buildingList");
        Crypto crypto = new Crypto();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/floorplanlisting.css">
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
                <div class="chTitle">Floor Plan's Overview</div>
            </div>
            <div class="cBody">
                <div class="cbContent cbFunction">
                    <a href="/initializeAddFloorplan"><div class="cbAdd"><center>Add New</center></div></a>
                    <input class="cbSearch" type='text' maxlength="30" placeholder="Search" onkeyup="searchFloorplan(this)">
                </div>
                <%
                    for (int i = 0; i < locationList.size(); i++) {
                %>
                <div class="location">
                    <div class="cbContent cbTitle"><%=locationList.get(i)%></div>
                    <%
                        for (int j = 0; j < buildingList.get(i).size(); j++) {
                    %>
                    <div class="building" id="<%=locationList.get(i)%> <%=buildingList.get(i).get(j)%>">
                        <div class="cbContent cbSubtitle"><%=buildingList.get(i).get(j)%></div>
                        <%
                            for (int k = 0; k < floorplanList.size(); k++) {
                                if (floorplanList.get(k).getHostelID().getLocation().equals(locationList.get(i)) && floorplanList.get(k).getHostelID().getBuilding().equals(buildingList.get(i).get(j))) {
                                    int tenantQuantity = 0;
                                    int actualRoom = 0;
                                    RoomManager roomManager = new RoomManager((EntityManager) session.getAttribute("mgr"));
                                    List<Room> roomList = roomManager.findAllByFloorplan(floorplanList.get(k));
                                    for(int x = 0;x<roomList.size();x++){
                                        HostellerManager hostellerManager = new HostellerManager((EntityManager) session.getAttribute("mgr"));
                                        List<Hosteller> hostellerList = hostellerManager.findByRoom(roomList.get(x).getRoomNo());
                                        tenantQuantity += hostellerList.size();
                                        if(roomList.get(x).getStatus().toLowerCase().equals("available")){
                                            actualRoom++;
                                        }
                                    }
                                    String image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(floorplanList.get(k).getImage()));
                        %>
                        <div class="cbContent cbFloorplan">
                            <div class="cbfImage"><img width="100%" height="100%" src="<%=image%>"></div>
                            <div class="cbfBreif">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td class="first" >Floor</td>
                                            <td>:</td>
                                            <td class="last"><%=floorplanList.get(k).getFloor()%></td>
                                        </tr>
                                        <tr>
                                            <td class="first">Max Tenant(s)</td>
                                            <td>:</td>
                                            <td class="last"><%=floorplanList.get(k).getPeopleCapacity()%></td>
                                        </tr>
                                        <tr>
                                            <td class="first">Current Tenant(s)</td>
                                            <td>:</td>
                                            <td class="last"><%=tenantQuantity%></td>
                                        </tr>
                                        <tr>
                                            <td class="first">Max Room(s)</td>
                                            <td>:</td>
                                            <td class="last"><%=floorplanList.get(k).getRoomCapacity()%></td>
                                        </tr>
                                        <tr>
                                            <td class="first">Allocated Room(s)</td>
                                            <td>:</td>
                                            <td class="last"><%=actualRoom%></td>
                                        </tr>
                                    </tbody>
                                </table>
                                <div class="cbfAction">
                                    <a href="/retrieveFloorplanDetails?p=<%=crypto.FPEncode(floorplanList.get(k).getFloorplanID())%>"><div><center>View</center></div></a>
                                    <a href="/initializeAddRoomStep1?p=<%=crypto.FPEncode(floorplanList.get(k).getFloorplanID())%>"><div><center>Add Room</center></div></a>
                                </div>
                            </div>
                        </div>
                        <%
                                }
                            }
                        %>
                    </div>
                    <%
                        }
                    %>
                </div>
                <%
                    }
                %>
                <% if (locationList.isEmpty()) {%>
                <div class="cbContent cbSubtitle"><center>Sorry, there are no any matching data currently.</center></div>
                <%}%>
            </div>
        </div>
    </body><script type="text/javascript">
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
        function searchFloorplan(input) {
            var x = document.getElementsByClassName("location");
            for (i = 0; i < x.length; i++) {
                var y = x[i].children;
                var appear = 0;
                for (j = 0; j < y.length; j++) {
                    if (y[j].className.indexOf("building") !== -1) {
                        appear = appear + 1;
                    }
                }
                for (j = 0; j < y.length; j++) {
                    if (y[j].className.indexOf("building") !== -1) {
                        if (!(y[j].id.toLowerCase().indexOf(input.value.toLowerCase()) !== -1)) {
                            y[j].className = "building hidden";
                            appear = appear - 1;
                        } else {
                            y[j].className = "building";
                        }
                    }
                }
                if (appear === 0) {
                    x[i].className = "location hidden";
                } else {
                    x[i].className = "location";
                }
            }
        }
    </script> 
</html>
<%
    } catch (Exception ex) {
        session.invalidate();
        session = request.getSession();
        session.setAttribute("error", ex.getLocalizedMessage());
        response.sendRedirect("/login/admin");
    }
%>