
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%><%@page import="Controller.Crypto"%>
<%@page import="Model.Roombooking"%>
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
        Roombooking roomBooking = (Roombooking) session.getAttribute("roomBooking");
        Crypto crypto = new Crypto();
        boolean nextStep = false;
        String status = roomBooking.getStatus().toLowerCase();
        if (status.equals("pending") || status.equals("paid")) {
            nextStep = true;
        }
        if (roomBooking.getRequestType().toLowerCase().equals("move out") && (status.equals("approved") || status.equals("recjected"))) {
            nextStep = true;
        }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/viewRoomRequest.css">
        <title>Hostel Management System</title>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <a href='/retrieveFloorplanListing'>
                    <div class="hBG">
                        <div class="hOption">
                            <p>Hostel Setup</p>
                        </div>
                    </div>
                </a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveRoomRequestListing?t=0'>
                    <div class="hBG">
                        <div class="hOption hChoose">
                            <p>Room Allocation</p>
                        </div>
                    </div>
                </a>
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
                <div class="chTitle">Room's Request Overview &#10148; View Room's Request </div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <form action="/updateRoomBooking" methos="post">
                        <div class="cbFPdetails">
                            <table>
                                <tr>
                                    <td><div class="cbLabel">Request No.</div><div></div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=roomBooking.getRequestNo()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Request Type</div><div></div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=roomBooking.getRequestType()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Request Date</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=roomBooking.getRequestDate().toLocaleString()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Update Date</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=roomBooking.getUpdateDate().toLocaleString()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Status</div></td>
                                    <td> : </td>
                                    <td>
                                        <%
                                            if (nextStep) {
                                        %>
                                        <select name="status" class="cbLabel">
                                            <%
                                                if (!roomBooking.getRequestType().toLowerCase().equals("move out")) {
                                                    if (status.equals("pending")) {
                                            %>
                                            <option>Pending</option>
                                            <option>Approved</option>
                                            <option>Rejected</option>
                                            <%
                                            } else if (status.equals("paid")) {
                                            %>
                                            <option>Paid</option>
                                            <option>Assigned</option>
                                            <%
                                                }
                                            } else {
                                            %>
                                            <option>Pending</option>
                                            <option>Approved</option>
                                            <option>Rejected</option>
                                            <%
                                                }
                                            %>
                                        </select>
                                        <%
                                        } else {
                                        %>
                                        <div class="cbLabel"><%=roomBooking.getStatus()%></div>
                                        <%
                                            }
                                        %>
                                    </td>
                                </tr>
                                <%if (status.equals("paid")) {%>
                                <tr>
                                    <td><div class="cbLabel">Entrance Card No</div></td>
                                    <td> : </td>
                                    <%
                                        String ecValue = "";
                                        if (roomBooking.getRequestBy().getEntCardNo() != null) {
                                            ecValue = roomBooking.getRequestBy().getEntCardNo().getEntCardNo();
                                        }%>
                                    <td><input type="text" name="entCardNo" class="cbLabel" required="" value="<%=ecValue%>"></td>
                                </tr>
                                <%}%>
                                <tr>
                                    <td><div class="cbLabel">Remark</div></td>
                                    <td> : </td>
                                    <td>
                                        <%
                                            if (nextStep) {
                                        %>
                                        <input type="text" name="remark" class="cbLabel" 
                                               <%
                                                   if (roomBooking.getRemark() != null) {
                                               %>
                                               value="<%=roomBooking.getRemark()%>"
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
                                                if (roomBooking.getRemark() != null) {
                                            %>
                                            <%=roomBooking.getRemark()%>
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
                                <tr>
                                    <td><div class="cbLabel">Request Room</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><a href="/retrieveRoomDetail?p=<%=crypto.REncode(roomBooking.getRequestRoom().getRoomNo())%>" class="cbrlRequest"><%=roomBooking.getRequestRoom().getRoomNo()%></a></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Request By</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><a href="/retrieveTenantDetails?p=<%=crypto.HTEncode(roomBooking.getRequestBy().getHostellerID())%>" class="cbrlRequest"><%=roomBooking.getRequestBy().getFirstName()%> <%=roomBooking.getRequestBy().getMiddleName()%> <%=roomBooking.getRequestBy().getLastName()%></a></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Bill No.</div></td>
                                    <td> : </td>
                                    <td>
                                        <div class="cbLabel">
                                            <%
                                                try {
                                                    if (roomBooking.getBillNo().getBillNo() != null) {
                                            %>
                                            <a href="/retrieveBillDetails?p=<%=crypto.BEncode(roomBooking.getBillNo().getBillNo())%>" class="cbrlRequest"><%=roomBooking.getBillNo().getBillNo()%></a>
                                            <%
                                            } else {
                                            %>
                                            Not Assigned
                                            <%
                                                }
                                            } catch (Exception ex) {
                                            %>
                                            Not Assigned
                                            <%
                                                }
                                            %>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <a href="/retrieveRoomRequestListing?t=0"><button type="button" class="cbAdd">&#9166; Back</button></a> 
                        <%
                            if (nextStep) {
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
