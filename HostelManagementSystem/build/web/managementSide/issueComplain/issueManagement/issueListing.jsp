
<%@page import="Model.IssueSearchingCriteria"%>
<%@page import="Controller.DateToString"%>
<%@page import="Model.Issue"%>
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
        List<Issue> issueList = (List) session.getAttribute("issueList");
        Crypto crypto = new Crypto();
        DateToString dateString = new DateToString();
        IssueSearchingCriteria searchingCriteria = (IssueSearchingCriteria) session.getAttribute("isearchingCriteria");
        String allClass = "";
        String processingClass = "";
        String closedClass = "";
        if (searchingCriteria.getStatus().equals("")) {
            allClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Processing")) {
            processingClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Closed")) {
            closedClass = "cbsChoice";
        }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/issueListing.css">
        <title>Hostel Management System</title>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveIssueListing?t=0'><div class="hBG"><div class="hOption hChoose"><p>Issue Management</p></div></div></a>
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
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveIssueListing?t=0'><div class="spoIssue spoBg"><div class="spOption spoChoose"><center><div class="spImg"  style="background-image:url(../../managementSide/source/s_issue.png)"  ></div></center>Issue</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveTenantEntryListing?t=0'><div class="spoEntry spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Entry</div></div></a>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Issue's Overview</div>
            </div>
            <div class="cBody">
                <div class="cbContent cbStatus">
                    <div class="cbsOption <%=allClass%>"><a href="/retrieveIssueListing?t=1&s=<%=crypto.CEncode("All")%>"><center>All</center></a></div>
                    <div class="cbsOption <%=processingClass%>"><a href="/retrieveIssueListing?t=1&s=<%=crypto.CEncode("Processing")%>"><center>Processing</center></a></div>
                    <div class="cbsOption <%=closedClass%>"><a href="/retrieveIssueListing?t=1&s=<%=crypto.CEncode("Closed")%>"><center>Closed</center></a></div>
                </div>
                <div class="cbContent cbSearchCriteria">
                    <form action="/retrieveIssueListing?t=2" method="post">
                        <input class="cbscOption" type='text' name="caseno" maxlength="30" placeholder="Case Number">
                        <input class="cbscOption" type='text' name="title" maxlength="30" placeholder="Title">
                        <input class="cbscOption" type="text" name="issueStartTime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Issue Start Time">
                        <input class="cbscOption" type="text" name="issueEndTime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Issue End Time">
                        <input class="cbscOption" type='text' name="category" maxlength="30" placeholder="Category">
                        <input class="cbscOption" type='text' name="issue" maxlength="30" placeholder="Issue">
                        <input class="cbscOption" type="text" name="updateStartTime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Update Start Time">
                        <input class="cbscOption" type="text" name="updateEndTime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Update End Time">
                        <div class="thridLine">
                            <input class="cbscOption cbscSearch" type='submit' value="Search" >
                        </div>
                    </form>
                </div>
                <div class="cbContent cbFunctionList">
                    <a href="" target="_blank" onclick="var ans = ''; ans = window.prompt('Enter report month and year (MM-YYYY):');if (ans === null) {
                                return false;
                            } else if (ans !== '') {
                                this.href = '/initializeReport?t=3&d=' + ans;
                            } else {
                                return false;
                            }"><div class="cbfOption"><center>Transaction Report</center></div></a>
                </div>
                <div class="cbContent cbList">
                    <table>
                        <thead>
                            <tr>
                                <th style="width:9%"><a href="/retrieveIssueListing?t=3&o=<%=crypto.CEncode("i.caseNo")%>">Case Number</a></th>
                                <th style="width:40%"><a href="/retrieveIssueListing?t=3&o=<%=crypto.CEncode("i.title")%>">Title</a></th>
                                <th style="width:11%"><a href="/retrieveIssueListing?t=3&o=<%=crypto.CEncode("i.issueDate")%>">Issue Date</a></th>
                                <th style="width:11%"><a href="/retrieveIssueListing?t=3&o=<%=crypto.CEncode("i.updateDate")%>">Last Update</a></th>
                                <th style="width:9%"><a href="/retrieveIssueListing?t=3&o=<%=crypto.CEncode("i.category")%>">Category</a></th>
                                <th style="width:10%"><a href="/retrieveIssueListing?t=3&o=<%=crypto.CEncode("i.issueType")%>">Issue</a></th>
                                <th style="width:10%"><a href="/retrieveIssueListing?t=3&o=<%=crypto.CEncode("i.requestNo")%>">Status</a></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int i = 0; i < issueList.size(); i++) {
                                    Issue issue = issueList.get(i);
                            %>
                            <tr>
                                <td><a href="/retrieveIssueDetails?p=<%=crypto.CAEncode(issue.getCaseNo())%>" class="cbrlRequest"><%=issue.getCaseNo()%></a></td>
                                <td><%=issue.getTitle()%></td>
                                <td><%=dateString.ToFormatDate(issue.getIssueDate())%></td>
                                <td><%=dateString.ToFormatDate(issue.getUpdateDate())%></td>
                                <td><%=issue.getCategory()%></td>
                                <td><%=issue.getIssueType()%></td>
                                <td><%=issue.getStatus()%></td>
                            </tr>
                            <%
                                }
                            %>
                            <% if (issueList.isEmpty()) {%>
                            <tr>
                                <td colspan="7"><center>Sorry, there are no any matching data currently.</center></td>
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
