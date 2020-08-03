
<%@page import="java.util.Base64"%>
<%@page import="Controller.DateToString"%>
<%@page import="Model.Facility"%>
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
        Crypto crypto = new Crypto();
        DateToString dateString = new DateToString();
        Facility facility = (Facility) session.getAttribute("facility");
        String image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(facility.getImage()));
        String tempoReminder = facility.getReminder().replace(".", "#");
        String[] facilityReminder = tempoReminder.split("#");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/viewFacility.css">
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
                <div class="chTitle">Facility's Overview &#10148; View Facility</div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <div class="cbFacilityDetails">
                        <div class="cbfImage"><img width="100%" height="100%" src="<%=image%>"></div>
                        <table>
                            <tr>
                                <td><div class="cbLabel">Hostel</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=facility.getHostelID().getLocation()%> <%=facility.getHostelID().getBuilding()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Description</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=facility.getDescription()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Category</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=facility.getCategory()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Operating Day</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=facility.getOperatingDay()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Operating Hours</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=dateString.DateToStringTime(facility.getStartHour())%> - <%=dateString.DateToStringTime(facility.getEndHour())%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Reminder</div></td>
                                <td> : </td>
                                <td>
                                    <%
                                        for (int i = 0; i < facilityReminder.length; i++) {
                                    %>
                                    <div class="cbLabel">
                                        <%=facilityReminder[i]%>.<br>
                                    </div>
                                    <%
                                        }
                                    %>
                                </td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Capacity</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=facility.getCapacity()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Status</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=facility.getStatus()%></div></td>
                            </tr>
                        </table>
                    </div>
                    <a href="/retrieveFacilityListing"><button class="cbAdd">&#9166; Back</button></a>
                    <a href="/initializeEditFacility?p=<%=crypto.FAEncode(facility.getFacilityID())%>"><button class="cbAdd cBAddright">Update</button></a>
                </div>
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
            if (successmsg !=='null') {
                alert(successmsg);
        <%
            session.removeAttribute("success");
        %>
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
