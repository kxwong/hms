
<%@page import="Model.Floorplan"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Controller.Crypto"%>
<%
    try {
        AccountManager accountManager = new AccountManager((EntityManager)session.getAttribute("mgr"));
        Account account = (Account)session.getAttribute("account");
        if (!account.equals(accountManager.findAccount(account.getUsername(), account.getPassword()))) {
            throw new Exception("Unauthorized account");
        }
        if (account.getLevel() != 3) {
            throw new Exception("Unauthorized account");
        }
        Crypto crypto = new Crypto();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/addNewFloorplan.css">
        <script type="text/javascript" src="../../managementSide/source/js/storeImage.js"></script>
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
                <div class="chTitle">Floor Plan's Overview &#10148; View Floor Plan &#10148; Add Room</div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <form action="/storeRoom" method="post">
                        <table>
                            <tr>
                                <td><div class="cbLabel">Description</div></td>
                                <td> : </td>
                                <td><input  class="cbInput" name="desc" type="text" min="1"></td>
                                <td rowspan="4">
                                    <img class="cbPreview" id="preview" />
                                </td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Rental</div></td>
                                <td> : </td>
                                <td><input class="cbInput" name="rental" type="number" min="200" step="10" max="1000" value="350" /></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Capacity</div></td>
                                <td> : </td>
                                <td><input  class="cbInput" name="capacity" type="number" min="1" value="1"></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Image</div></td>
                                <td> : </td>
                                <td>
                                    <input type="file" id="files" name="roomImg" onchange="fileValidation(this, event)" accept="image/*" required="">
                                    <input type="text" name="imageByte" id="byte_content" hidden>
                                </td>
                            </tr>
                        </table>
                        <a href="/initializeAddRoomStep1?p=<%=crypto.FPEncode(((Floorplan)session.getAttribute("floorplan")).getFloorplanID())%>"><button type="button" class="cbAdd">&#9166; Back</button></a>
                        <input class="cbAdd cBAddright" type="submit">
                    </form>
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