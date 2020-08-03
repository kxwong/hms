
<%@page import="Model.Visitor"%>
<%@page import="Model.VisitorManager"%>
<%@page import="Model.Entrecord"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
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
        Crypto crypto = new Crypto();
        Entrecord visitorEntry = (Entrecord) session.getAttribute("visitorEntry");
        VisitorManager visitorManager = new VisitorManager((EntityManager) session.getAttribute("mgr"));
        Visitor visitor = visitorManager.findByCard(visitorEntry.getEntCardNo().getEntCardNo());
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/viewEntry.css">
        <title>Hostel Management System</title>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <a href='/retrieveTenantEntryListing?t=0'><div class="hBG"><div class="hOption"><p>Tenant Entrance</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveVisitorEntryListing?t=0'><div class="hBG"><div class="hOption hChoose"><p>Visitor Entrance</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
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
                <a href='/retrieveFacilityListing'><div class="spoFacilities spoBg"><div class="spOption"><center><div class="spImg"></div></center>Facility</div></div></a>
                <a href='/retrieveNoticeListing'><div class="spoNotice spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Notice</div></div></a>
                <a href='/retrieveIssueListing?t=0'><div class="spoIssue spoBg"><div class="spOption "><center><div class="spImg" ></div></center>Issue</div></div></a>
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveTenantEntryListing?t=0'><div class="spoEntry spoBg"><div class="spOption spoChoose"><center><div class="spImg" style="background-image:url(../../managementSide/source/s_entry.png)" ></div></center>Entry</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Visitor Entrance Record's Overview &#10148; View Visitor Entrance Record </div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <div class="cbFPdetails">
                        <table>
                            <tr>
                                <td><div class="cbLabel">Entrance Record ID</div><div></div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitorEntry.getEntRecordID()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Access Time</div><div></div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitorEntry.getAccessTime().toLocaleString()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Access By</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitor.getName()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Identification No</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitor.getIdentificationNo()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Mobile Phone No</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitor.getMobilePhone()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Entrance Card No.</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitorEntry.getEntCardNo().getEntCardNo()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Entry Reason</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitor.getEntryReason()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Gate</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitorEntry.getGate()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Hostel Location</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitorEntry.getHostel().getLocation()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Hostel Building</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitorEntry.getHostel().getBuilding()%></div></td>
                            </tr>
                        </table>
                    </div>
                    <a href="/retrieveVisitorEntryListing?t=0"><button type="button" class="cbAdd">&#9166; Back</button></a>
                </div>
            </div>
        </div>
    </body>
</html>
<%
    } catch (Exception ex) {
        session.invalidate();
        session = request.getSession();
        session.setAttribute("error", ex.getLocalizedMessage());
        response.sendRedirect("/login/admin");
    }
%>
