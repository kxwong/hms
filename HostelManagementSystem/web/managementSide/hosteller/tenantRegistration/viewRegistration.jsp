
<%@page import="Model.Hosteller"%>
<%@page import="Model.HostellerManager"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Controller.Crypto"%>
<%@page import="Model.Registrationreq"%>
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
        Registrationreq registrationreq = (Registrationreq) session.getAttribute("registrationReq");
        boolean done = true;
        if (registrationreq.getStatus().toLowerCase().equals("pending")) {
            done = false;
        }
        HostellerManager hostellerManager = new HostellerManager((EntityManager) session.getAttribute("mgr"));
        Hosteller hosteller = hostellerManager.findHostellerByRegreq(registrationreq.getRequestNo());
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/viewRegistration.css">
        <title>Hostel Management System</title>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveRegistrationListing?t=0'><div class="hBG"><div class="hOption hChoose"><p>Tenant Registration</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/retrieveTenantListing?t=0'><div class="hBG"><div class="hOption"><p>Tenant Management</p></div></div></a>
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
                <a href='/retrieveFloorplanListing'><div class="spoBg spoRoom"><div class="spOption"><center><div class="spImg "></div></center><p>Room</p></div></div></a>
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveRegistrationListing?t=0'><div class="spoTenant spoBg"><div class="spOption spoChoose"><center><div class="spImg" style="background-image:url(../../managementSide/source/s_tenant.png)"></div></center>Tenant</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveBillListing?t=0'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Rental</div></div></a>
                <a href='/retrieveFacilityListing'><div class="spoFacilities spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Facility</div></div></a>
                <a href='/retrieveNoticeListing'><div class="spoNotice spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Notice</div></div></a>
                <a href='/retrieveIssueListing?t=0'><div class="spoIssue spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Issue</div></div></a>
                <a href='/retrieveTenantEntryListing?t=0'><div class="spoEntry spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Entry</div></div></a>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Registration's Overview &#10148; View Registration </div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <form action="/updateRegistrationReq" methos="post">
                        <div class="cbFPdetails">
                            <table>
                                <tr>
                                    <td><div class="cbLabel">Request No.</div><div></div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=registrationreq.getRequestNo()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Registrant</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><a href="/retrieveTenantDetails?p=<%=crypto.HTEncode(hosteller.getHostellerID())%>" class="cbrlRequest"><%=hosteller.getFirstName()%> <%=hosteller.getMiddleName()%> <%=hosteller.getLastName()%></a></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Request Date</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=registrationreq.getRequestDate().toLocaleString()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Update Date</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=registrationreq.getUpdateDate().toLocaleString()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Status</div></td>
                                    <td> : </td>
                                    <td>
                                        <%
                                            if (done) {
                                        %>
                                        <div class="cbLabel"><%=registrationreq.getStatus()%></div>
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
                                                   if (registrationreq.getRemark() != null) {
                                               %>
                                               value="<%=registrationreq.getRemark()%>"
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
                                                if (registrationreq.getRemark() != null) {
                                            %>
                                            <%=registrationreq.getRemark()%>
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
                        <a href="/retrieveRegistrationListing?t=0"><button type="button" class="cbAdd">&#9166; Back</button></a> 
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
