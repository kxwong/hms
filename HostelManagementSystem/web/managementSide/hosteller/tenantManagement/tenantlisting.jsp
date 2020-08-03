
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Controller.DateToString"%>
<%@page import="Controller.Crypto"%>
<%@page import="Model.Hosteller"%>
<%@page import="java.util.List"%>
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
        List<Hosteller> hostellerList = (List) session.getAttribute("hostellerList");
        Crypto crypto = new Crypto();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/tenantlisting.css">
        <title>Hostel Management System</title>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <a href='/retrieveRegistrationListing?t=0'><div class="hBG"><div class="hOption"><p>Tenant Registration</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveTenantListing?t=0'><div class="hBG"><div class="hOption hChoose"><p>Tenant Management</p></div></div></a>
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
                <div class="chTitle">Tenant's Overview</div>
            </div><div class="cBody">
                <div class="cbContent cbSearchCriteria">
                    <form action="/retrieveTenantListing?t=2" method="post">
                        <input class="cbscOption" type='text' maxlength="30" placeholder="Name" name="name">
                        <input class="cbscOption" type='text' maxlength="30" placeholder="Room" name="room">
                        <input class="cbscOption" type='text' maxlength="30" placeholder="Email" name="email">
                        <input class="cbscOption" type='text' maxlength="30" placeholder="Contact No." name="contact">
                        <input class="cbscOption" type='text' maxlength="30" placeholder="Request No." name="requestno">
                        <input class="cbscOption" type='text' maxlength="30" placeholder="Status" name="status">
                        <div class="thridLine">
                            <input class="cbscOption cbscSearch" type='submit' value="Search" >
                        </div>
                    </form>
                </div>
                <div class="cbContent cbList">
                    <table>
                        <thead>
                            <tr>
                                <th><a href="/retrieveTenantListing?t=3&o=<%=crypto.CEncode("h.firstName, h.middleName, h.lastName")%>">Name</a></th>
                                <th><a href="/retrieveTenantListing?t=3&o=<%=crypto.CEncode("h.stayRoom")%>">Room</a></th>
                                <th><a href="/retrieveTenantListing?t=3&o=<%=crypto.CEncode("h.contact.email")%>">Email</a></th>
                                <th><a href="/retrieveTenantListing?t=3&o=<%=crypto.CEncode("h.contact.mobilePhone")%>">Contact No.</a></th>
                                <th><a href="/retrieveTenantListing?t=3&o=<%=crypto.CEncode("h.regReqNo")%>">Request No.</a></th>
                                <th><a href="/retrieveTenantListing?t=3&o=<%=crypto.CEncode("h.status")%>">Status</a></th>
                            </tr>
                        </thead>
                        <tbody>

                            <%
                                for (int i = 0; i < hostellerList.size(); i++) {
                                    Hosteller hosteller = hostellerList.get(i);
                                    String room = "Not assigned";
                                    try {
                                        if (!hosteller.getStayRoom().getRoomNo().equals("")) {
                                            room = "<a href='/retrieveRoomDetail?p=" + crypto.REncode(hosteller.getStayRoom().getRoomNo()) + "' class='cbrlRequest'>" + hosteller.getStayRoom().getRoomNo() + "</a>";
                                        }
                                    } catch (Exception ex) {
                                    }
                            %>
                            <tr>
                                <td><a href="/retrieveTenantDetails?p=<%=crypto.HTEncode(hosteller.getHostellerID())%>" class="cbrlRequest"><%=hosteller.getFirstName()%> <%=hosteller.getMiddleName()%> <%=hosteller.getLastName()%> </a></td>
                                <td><%=room%></td>
                                <td><%=hosteller.getContact().getEmail()%></td>
                                <td><%=hosteller.getContact().getMobilePhone()%></td>
                                <td><a href="/retrieveRegistrationDetails?p=<%=crypto.RREncode(hosteller.getRegReqNo().getRequestNo())%>" class="cbrlRequest"><%=hosteller.getRegReqNo().getRequestNo()%></a></td>
                                <td><%=hosteller.getStatus()%></td>
                            </tr>
                            <%
                                }
                            %>
                            <% if (hostellerList.isEmpty()) {%>
                            <tr>
                                <td colspan="6"><center>Sorry, there are no any matching data currently.</center></td>
                            </tr>
                            <%}%>
                        </tbody>
                    </table>
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
