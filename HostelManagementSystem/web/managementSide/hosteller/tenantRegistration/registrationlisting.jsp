
<%@page import="Model.Hosteller"%>
<%@page import="Model.HostellerManager"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Model.RegistrationReqSearchingCriteria"%>
<%@page import="Controller.DateToString"%>
<%@page import="Controller.Crypto"%>
<%@page import="Model.Registrationreq"%>
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
        List<Registrationreq> registrationReqList = (List) session.getAttribute("registrationReqList");
        Crypto crypto = new Crypto();
        DateToString dateString = new DateToString();
        RegistrationReqSearchingCriteria searchingCriteria = (RegistrationReqSearchingCriteria) session.getAttribute("rrsearchingCriteria");
        String allClass = "";
        String pendingClass = "";
        String approvedClass = "";
        String rejectedClass = "";
        if (searchingCriteria.getStatus().equals("")) {
            allClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Pending")) {
            pendingClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Approved")) {
            approvedClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Rejected")) {
            rejectedClass = "cbsChoice";
        }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/registrationlisting.css">
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
                <div class="chTitle">Registration's Overview</div>
            </div>
            <div class="cBody">
                <div class="cbContent cbStatus">
                    <div class="cbsOption <%=allClass%>"><a href="/retrieveRegistrationListing?t=1&s=<%=crypto.CEncode("All")%>"><center>All</center></a></div>
                    <div class="cbsOption <%=pendingClass%>"><a href="/retrieveRegistrationListing?t=1&s=<%=crypto.CEncode("Pending")%>"><center>Pending</center></a></div>
                    <div class="cbsOption <%=approvedClass%>"><a href="/retrieveRegistrationListing?t=1&s=<%=crypto.CEncode("Approved")%>"><center>Approved</center></a></div>
                    <div class="cbsOption <%=rejectedClass%>"><a href="/retrieveRegistrationListing?t=1&s=<%=crypto.CEncode("Rejected")%>"><center>Rejected</center></a></div>
                </div>
                <div class="cbContent cbSearchCriteria">
                    <form action="/retrieveRegistrationListing?t=2" method="post">
                        <input class="cbscOption" type='text' name="requestNo"  maxlength="30" placeholder="Request No.">
                        <input class="cbscOption" type="text" name="requestSTime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Request Start Time">
                        <input class="cbscOption" type="text" name="requestETime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Request End Time">
                        <input class="cbscOption" type='text' name="tenantName" maxlength="30" placeholder="Tenant Name">
                        <input class="cbscOption" type="text" name="updateSTime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Update Start Time">
                        <input class="cbscOption" type="text" name="updateETime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Update End Time">
                        <div class="thridLine">
                            <input class="cbscOption cbscSearch" type='submit' value="Search" >
                        </div>
                    </form>
                </div>
                <div class="cbContent cbFunctionList">
                    <a onclick="return confirm('Confirm to approve all tenant registration request?')" href="/updateBatchRegistrationReq"><button class="cbAdd">Batch Approve</button></a>
                </div>
                <div class="cbContent cbList">
                    <table>
                        <thead>
                            <tr>
                                <th><a href="/retrieveRegistrationListing?t=3&o=<%=crypto.CEncode("r.requestNo")%>">Request No.</a></th>
                                <th><a href="/retrieveRegistrationListing?t=3&o=<%=crypto.CEncode("r.requestDate")%>">Request Date</a></th>
                                <th><a href="/retrieveRegistrationListing?t=3&o=<%=crypto.CEncode("r.hosteller.firstName, r.hosteller.middleName, r.hosteller.lastName")%>">Tenant Name</a></th>
                                <th><a href="/retrieveRegistrationListing?t=3&o=<%=crypto.CEncode("r.updateDate")%>">Updated Date</a></th>
                                <th><a href="/retrieveRegistrationListing?t=3&o=<%=crypto.CEncode("r.status")%>">Status</a></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int i = 0; i < registrationReqList.size(); i++) {
                                    Registrationreq registrationReq = registrationReqList.get(i);
                                    HostellerManager hostellerManager = new HostellerManager((EntityManager) session.getAttribute("mgr"));
                                    Hosteller hosteller = hostellerManager.findHostellerByRegreq(registrationReq.getRequestNo());
                            %>
                            <tr>
                                <td><a href="/retrieveRegistrationDetails?p=<%=crypto.RREncode(registrationReq.getRequestNo())%>" class="cbrlRequest"><%=registrationReq.getRequestNo()%></a></td>
                                <td><%=dateString.ToFormatDate(registrationReq.getRequestDate())%></td>
                                <td><a href="/retrieveTenantDetails?p=<%=crypto.HTEncode(hosteller.getHostellerID())%>" class="cbrlRequest"><%=hosteller.getFirstName()%> <%=hosteller.getMiddleName()%> <%=hosteller.getLastName()%></a></td>
                                <td><%=dateString.ToFormatDate(registrationReq.getUpdateDate())%></td>
                                <td><%=registrationReq.getStatus()%></td>
                            </tr>
                            <%
                                }
                            %>
                            <% if (registrationReqList.isEmpty()) {%>
                            <tr>
                                <td colspan="5"><center>Sorry, there are no any matching data currently.</center></td>
                            </tr>
                            <%}%>
                        </tbody>
                    </table>
                </div>
            </div>   
        </div>
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
