
<%@page import="java.util.Base64"%>
<%@page import="Model.Attachment"%>
<%@page import="Model.Conversation"%>
<%@page import="java.util.List"%>
<%@page import="Model.ConversationManager"%>
<%@page import="Model.Issue"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Controller.Crypto"%>
<%
    try {
        EntityManager mgr = (EntityManager) session.getAttribute("mgr");
        AccountManager accountManager = new AccountManager(mgr);
        Account account = (Account) session.getAttribute("account");
        if (!account.equals(accountManager.findAccount(account.getUsername(), account.getPassword()))) {
            throw new Exception("Unauthorized account");
        }
        if (account.getLevel() != 3) {
            throw new Exception("Unauthorized account");
        }
        ConversationManager conversationManager = new ConversationManager(mgr);
        Crypto crypto = new Crypto();
        Issue issue = (Issue) session.getAttribute("issue");
        Account issueAccount = conversationManager.getIssueAccount(issue.getCaseNo());
        Account lastReplyAccount = conversationManager.getLastReply(issue.getCaseNo());
        List<Conversation> conversationList = conversationManager.getConversationList(issue.getCaseNo());
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/viewIssue.css">
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
                <div class="chTitle">Issue's Overview &#10148; View Issue Details</div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <table class="cbIssueDetails">
                        <tr>
                            <th>Case Number</th>
                            <th>Issue Date</th>
                            <th>Update Date</th>
                            <th>Status</th>
                        </tr>
                        <tr>
                            <td><%=issue.getCaseNo()%></td>
                            <td><%=issue.getIssueDate().toLocaleString()%></td>
                            <td><%=issue.getUpdateDate().toLocaleString()%></td>
                            <td><%=issue.getStatus()%></td>
                        </tr>
                        <tr>
                            <th>Category</th>
                            <th>IssueType</th>
                            <th>Issue By</th>
                            <th>Last Reply</th>
                        </tr>
                        <tr>
                            <td><%=issue.getCategory()%></td>
                            <td><%=issue.getIssueType()%></td>
                            <td><a class="cbrlRequest" href="/retrieveTenantDetails?p=<%=crypto.HTEncode(issueAccount.getHosteller().getHostellerID())%>"><%=issueAccount.getHosteller().getHostellerID()%></a></td>
                            <td><%=lastReplyAccount.getLevel() == 1 ? "Tenant" : "Management"%></td>
                        </tr>
                    </table>
                    <div class="cbLabel">Title : <%=issue.getTitle()%></div>
                    <div class="cbContent">
                        <div class="cbLabel">Attachment List</div>
                        <%
                            for (int j = 0; j < issue.getAttachmentList().size(); j++) {
                                Attachment attachment = issue.getAttachmentList().get(j);
                        %>
                        <a href="<%=attachment.getHeader() + "," + new String(Base64.getEncoder().encode(attachment.getFile()))%>" download>
                            &#128206; File <%=j + 1%>
                        </a>
                        <%
                            }
                        %>
                    </div>
                    <hr>
                    <%
                        for (int i = 0; i < conversationList.size(); i++) {
                            Conversation msg = conversationList.get(i);
                    %>
                    <div class="cbReply">
                        <div class="cbrTitle"><%=msg.getReplyBy().getLevel() == 1 ? crypto.UNDecode(msg.getReplyBy().getUsername()) + " (Hosteller)" : crypto.UNDecode(msg.getReplyBy().getUsername()) + " (Admin)"%></div>
                        <div class="cbrTitle"><%=msg.getTime().toLocaleString()%></div>
                        <div class="cbrContent"><%=msg.getContent()%></div>
                    </div>
                    <hr>
                    <%
                        }
                    %>
                    <% if (!issue.getStatus().equals("Closed")) {%>
                    <div class="cbLabel"><br>Reply</div>
                    <form action="/storeIssueConversation" method="post" id="replyArea">
                        <textarea class="cbReplyArea" name="replyContent" form="replyArea"  maxlength="200" ></textarea>
                        <a href="/retrieveIssueListing?t=0"><button type="button" class="cbAdd">&#9166; Back</button></a> 
                        <button class="cbAdd cBAddright">Reply</button>
                        <a href="/updateIssueStatus?p=<%=crypto.CAEncode(issue.getCaseNo())%>"><button class="cbAdd cBAddright" type="button">Case Close</button></a>
                    </form>
                    <%} else {%>
                    <a href="/retrieveIssueListing?t=0"><button style="margin-left: 0px;" type="button" class="cbAdd">&#9166; Back</button></a> 
                    <%}%>
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
