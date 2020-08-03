
<%@page import="Model.VisitorManager"%>
<%@page import="Model.EntryRecordSearchingCriteria"%>
<%@page import="Model.Entrecord"%>
<%@page import="Controller.DateToString"%>
<%@page import="java.util.List"%>
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
        List<Entrecord> visitorEntryRecordList = (List) session.getAttribute("visitorEntryRecordList");
        DateToString dateString = new DateToString();
        EntryRecordSearchingCriteria searchingCriteria = (EntryRecordSearchingCriteria) session.getAttribute("vsearchingCriteria");
        String allClass = "";
        String entryClass = "";
        String leaveClass = "";
        if (searchingCriteria.getGate().equals("")) {
            allClass = "cbsChoice";
        } else if (searchingCriteria.getGate().equals("Entry")) {
            entryClass = "cbsChoice";
        } else if (searchingCriteria.getGate().equals("Leave")) {
            leaveClass = "cbsChoice";
        }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/entryListing.css">
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
                <div class="chTitle">Visitor Entrance Record's Overview</div>
            </div>
            <div class="cBody">
                <div class="cbContent cbStatus">
                    <div class="cbsOption  <%=allClass%>"><a href="/retrieveVisitorEntryListing?t=1&s=<%=crypto.CEncode("All")%>"><center>All</center></a></div>
                    <div class="cbsOption  <%=entryClass%>"><a href="/retrieveVisitorEntryListing?t=1&s=<%=crypto.CEncode("Entry")%>"><center>Entry</center></a></div>
                    <div class="cbsOption  <%=leaveClass%>"><a href="/retrieveVisitorEntryListing?t=1&s=<%=crypto.CEncode("Leave")%>"><center>Leave</center></a></div>
                </div>
                <div class="cbContent cbSearchCriteria">
                    <form action="/retrieveVisitorEntryListing?t=2" method="post">
                        <input class="cbscOption" type='text' name="entID" maxlength="30" placeholder="Entrance ID">
                        <input class="cbscOption" type='text' name="tenantName" maxlength="30" placeholder="Access by">
                        <input class="cbscOption" type='text' name="location" maxlength="30" placeholder="Location">
                        <input class="cbscOption" type='text' name="building" maxlength="30" placeholder="Building">
                        <div style="text-align: left;" class="thridLine">
                            <input style="width:400px;" class="cbscOption" type="text" name="startDate" onfocus="(this.type = 'datetime-local')" onblur="(this.type = 'text')" placeholder="Start Date">
                            <input style="width:400px;" class="cbscOption" type="text" name="endDate" onfocus="(this.type = 'datetime-local')" onblur="(this.type = 'text')" placeholder="End Date">
                        </div>
                        <div class="thridLine">
                            <input class="cbscOption cbscSearch" type='submit' value="Search" >
                        </div>
                    </form>
                </div>
                <div class="cbContent cbFunctionList">
                    <a href="" target="_blank" onclick="var ans = ''; ans = window.prompt('Enter report month and year (MM-YYYY):');if (ans === null) {
                                return false;
                            } else if (ans !== '') {
                                this.href = '/initializeReport?t=5&d=' + ans;
                            } else {
                                return false;
                            }"><div class="cbfOption"><center>Transaction Report</center></div></a>
                </div>
                <div class="cbContent cbList">
                    <table>
                        <thead>
                            <tr>
                                <th><a href="/retrieveVisitorEntryListing?t=3&o=<%=crypto.CEncode("e.entRecordID")%>">Entrance ID</a></th>
                                <th><a href="/retrieveVisitorEntryListing?t=3&o=<%=crypto.CEncode("e.entCardNo.visitor.name")%>">Access By</a></th>
                                <th><a href="/retrieveVisitorEntryListing?t=3&o=<%=crypto.CEncode("e.accessTime")%>">Time</a></th>
                                <th><a href="/retrieveVisitorEntryListing?t=3&o=<%=crypto.CEncode("e.gate")%>">Gate</a></th>
                                <th><a href="/retrieveVisitorEntryListing?t=3&o=<%=crypto.CEncode("e.hostel.location")%>">Location</a></th>
                                <th><a href="/retrieveVisitorEntryListing?t=3&o=<%=crypto.CEncode("e.hostel.building")%>">Building</a></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int i = 0; i < visitorEntryRecordList.size(); i++) {
                                    Entrecord entryRecord = visitorEntryRecordList.get(i);
                                    VisitorManager visitorManager = new VisitorManager((EntityManager) session.getAttribute("mgr"));
                            %>
                            <tr>
                                <td><a href="/retrieveVisitorEntryDetails?p=<%=crypto.ENEncode(entryRecord.getEntRecordID())%>" class="cbrlRequest"><%=entryRecord.getEntRecordID()%></a></td>
                                <td><%=(visitorManager.findByCard(entryRecord.getEntCardNo().getEntCardNo())).getName()%></td>
                                <td><%=dateString.ToFormatDate(entryRecord.getAccessTime())%> <%=dateString.DateTimeToTime(entryRecord.getAccessTime())%></td>
                                <td><%=entryRecord.getGate()%></td>
                                <td><%=entryRecord.getHostel().getLocation()%></td>
                                <td><%=entryRecord.getHostel().getBuilding()%></td>
                            </tr>
                            <%
                                }
                            %>
                            
                            <% if (visitorEntryRecordList.isEmpty()) {%>
                            <tr>
                                <td colspan="6"><center>Sorry, there are no any matching data currently.</center></td>
                            </tr>
                            <%}%>
                        </tbody>
                    </table>
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
