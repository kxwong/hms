
<%@page import="Model.Facilitybooking"%>
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
        Facilitybooking facilityBooking = (Facilitybooking) session.getAttribute("facilityBooking");
        boolean done = true;
        if (facilityBooking.getStatus().toLowerCase().equals("pending")) {
            done = false;
        }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/viewFacilityBooking.css">
        <title>Hostel Management System</title>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <a href='/retrieveFacilityListing'><div class="hBG"><div class="hOption "><p>Facility Management</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveBookingListing?t=0'><div class="hBG"><div class="hOption hChoose"><p>Booking Management</p></div></div></a>
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
                <div class="chTitle">Booking's Overview &#10148; View Booking </div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <form action="/updateFacilityBooking" methos="post">
                        <div class="cbFPdetails">
                            <table>
                                <tr>
                                    <td><div class="cbLabel">Booking ID</div><div></div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=facilityBooking.getBookingID()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Booked Facility</div><div></div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><a href="/retrieveFacilityDetails?p=<%=crypto.FAEncode(facilityBooking.getBookFacility().getFacilityID())%>" class="cbrlRequest"><%=facilityBooking.getBookFacility().getFacilityID()%></a></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Booked By</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><a href="/retrieveTenantDetails?p=<%=crypto.HTEncode(facilityBooking.getBookBy().getHostellerID())%>" class="cbrlRequest"><%=facilityBooking.getBookBy().getFirstName()%> <%=facilityBooking.getBookBy().getMiddleName()%> <%=facilityBooking.getBookBy().getLastName()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Book Time</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=facilityBooking.getBookTime().toLocaleString()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Request Time</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=facilityBooking.getRequestTime().toLocaleString()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Update Time</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=facilityBooking.getUpdateTime().toLocaleString()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Status</div></td>
                                    <td> : </td>
                                    <td>
                                        <%
                                            if (done) {
                                        %>
                                        <div class="cbLabel"><%=facilityBooking.getStatus()%></div>
                                        <%  } else {
                                        %>
                                        <select name="status" class="cbLabel">
                                            <option>Pending</option>
                                            <option>Approved</option>
                                            <option>Rejected</option>
                                        </select>
                                        <%
                                            }
                                        %>
                                    </td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Remark</div></td>
                                    <td> : </td>
                                    <td>
                                        <%
                                            if (!done) {
                                        %>
                                        <input type="text" name="remark" class="cbLabel" 
                                               <%
                                                   if (facilityBooking.getRemark() != null) {
                                               %>
                                               value="<%=facilityBooking.getRemark()%>"
                                               <%
                                               } else {
                                               %>
                                               value="No Remark"
                                               <%
                                                   }
                                               %>
                                               >
                                        <%
                                        } else {
                                        %>
                                        <div class="cbLabel">
                                            <%
                                                if (facilityBooking.getRemark() != null) {
                                            %>
                                            <%=facilityBooking.getRemark()%>
                                            <%
                                            } else {
                                            %>
                                            No Remark
                                            <%
                                                }
                                            %>
                                        </div>
                                        <%
                                            }
                                        %>
                                    </td>
                                </tr>

                            </table>
                        </div>
                        <a href="/retrieveBookingListing?t=0"><button type="button" class="cbAdd">&#9166; Back</button></a>
                        <%
                            if (!done) {
                        %>
                        <input type="submit" value="Update" class="cbAdd cBAddright">
                        <%
                            }
                        %>
                    </form>
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
            if (successmsg !== 'null') {
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
