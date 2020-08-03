
<%@page import="Controller.DateToString"%>
<%@page import="java.util.Base64"%>
<%@page import="Model.Facility"%>
<%@page import="java.util.List"%>
<%@page import="Controller.Crypto"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
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
        List<Facility> facilityList = (List) session.getAttribute("facilityList");
        List<String> locationList = (List) session.getAttribute("locationList");
        List<List<String>> buildingList = (List) session.getAttribute("buildingList");
        Crypto crypto = new Crypto();
        DateToString dateString = new DateToString();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/facilityListing.css">
        <title>Hostel Management System</title>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveFacilityListing'><div class="hBG"><div class="hOption hChoose"><p>Facility Management</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/retrieveBookingListing?t=0'><div class="hBG"><div class="hOption"><p>Booking Management</p></div></div></a>
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
                <a href='/retrieveFloorplanListing'><div class="spoRoom spoBg"><div class="spOption"><center><div class="spImg " ></div><p>Room</p></center></div></div></a>
                <a href='/retrieveRegistrationListing?t=0'><div class="spoTenant spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Tenant</div></div></a>
                <a href='/retrieveBillListing?t=0'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Rental</div></div></a>
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveFacilityListing'><div class="spoFacilities spoBg"><div class="spOption spoChoose"><center><div class="spImg" style="background-image:url(../../managementSide/source/s_facilities.png)"  ></div></center>Facility</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveNoticeListing'><div class="spoNotice spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Notice</div></div></a>
                <a href='/retrieveIssueListing?t=0'><div class="spoIssue spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Issue</div></div></a>
                <a href='/retrieveTenantEntryListing?t=0'><div class="spoEntry spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Entry</div></div></a>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Facility's Overview</div>
            </div>
            <div class="cBody">
                <div class="cbContent cbFunction">
                    <a href="/initializeAddNewFacility"><div class="cbAdd"><center>Add New</center></div></a>
                    <input class="cbSearch" type='text' maxlength="30" placeholder="Search" onkeyup="searchFacility(this)">
                </div><%
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
                            for (int k = 0; k < facilityList.size(); k++) {
                                Facility facility = facilityList.get(k);
                                if (facility.getHostelID().getLocation().equals(locationList.get(i)) && facility.getHostelID().getBuilding().equals(buildingList.get(i).get(j))) {
                                    String image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(facility.getImage()));
                        %>

                        <div class="facility" id="<%=locationList.get(i)%> <%=buildingList.get(i).get(j)%> <%=facility.getDescription()%>">
                            <div class="cbContent cbFloorplan" >
                                <div class="cbfImage"><img width="100%" height="100%" src="<%=image%>"></div>
                                <div class="cbfBreif">
                                    <table>
                                        <tbody>
                                            <tr>
                                                <td class="first">Description</td>
                                                <td>:</td>
                                                <td class="last"><%=facility.getDescription()%></td>
                                            </tr>
                                            <tr>
                                                <td class="first">Operating Day</td>
                                                <td>:</td>
                                                <td class="last"><%=facility.getOperatingDay()%></td>
                                            </tr>
                                            <tr>
                                                <td class="first">Operating Hours</td>
                                                <td>:</td>
                                                <td class="last"><%=dateString.DateToStringTime(facility.getStartHour())%> - <%=dateString.DateToStringTime(facility.getEndHour())%></td>
                                            </tr>
                                            <tr>
                                                <td class="first">Capacity</td>
                                                <td>:</td>
                                                <td class="last"><%=facility.getCapacity()%></td>
                                            </tr>
                                            <tr>
                                                <td class="first">Status</td>
                                                <td>:</td>
                                                <td class="last"><%=facility.getStatus()%></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <div class="cbfAction">
                                        <a href="/retrieveFacilityDetails?p=<%=crypto.FAEncode(facility.getFacilityID())%>"><div><center>View</center></div></a>
                                        <a href="/initializeEditFacility?p=<%=crypto.FAEncode(facility.getFacilityID())%>"><div><center>Edit</center></div></a>
                                    </div>
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
    </body>
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
        function searchFacility(input) {
            var x = document.getElementsByClassName("location");
            for (i = 0; i < x.length; i++) {
                var y = x[i].children;
                var appearBuilding = 0;
                for (j = 0; j < y.length; j++) {
                    if (y[j].className.indexOf("building") !== -1) {
                        appearBuilding = appearBuilding + 1;
                    }
                }
                for (j = 0; j < y.length; j++) {
                    if (y[j].className.indexOf("building") !== -1) {
                        var a = y[j].children;
                        var appearFacility = 0;
                        for (k = 0; k < a.length; k++) {
                            if (a[k].className.indexOf("facility") !== -1) {
                                appearFacility = appearFacility + 1;
                                if (!(a[k].id.toLowerCase().indexOf(input.value.toLowerCase()) !== -1)) {
                                    a[k].className = "facility hidden";
                                    appearFacility = appearFacility - 1;
                                } else {
                                    a[k].className = "facility";
                                }
                            }
                        }
                        if (appearFacility === 0) {
                            y[j].className = "building hidden";
                            appearBuilding--;
                        }else{
                            y[j].className = "building";
                        }
                    }
                }
                if (appearBuilding === 0) {
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
