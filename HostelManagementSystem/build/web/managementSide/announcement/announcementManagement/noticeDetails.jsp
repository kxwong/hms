
<%@page import="java.util.Base64"%>
<%@page import="Model.Attachment"%>
<%@page import="Model.Announcement"%>
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
        Announcement notice = (Announcement) session.getAttribute("notice");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/makeAnnouncement.css">
        <title>Hostel Management System</title>
        <script src="https://cdn.ckeditor.com/ckeditor5/12.3.1/classic/ckeditor.js"></script>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveNoticeListing'>
                    <div class="hBG">
                        <div class="hOption hChoose">
                            <p>Notice Management</p>
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
                <a href='/retrieveFloorplanListing'><div class="spoRoom spoBg"><div class="spOption "><center><div class="spImg " ></div><p>Room</p></center></div></div></a>
                <a href='/retrieveRegistrationListing?t=0'><div class="spoTenant spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Tenant</div></div></a>
                <a href='/retrieveBillListing?t=0'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Rental</div></div></a>
                <a href='/retrieveFacilityListing'><div class="spoFacilities spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Facility</div></div></a>
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveNoticeListing'><div class="spoNotice spoBg"><div class="spOption spoChoose"><center><div class="spImg"  style="background-image:url(../../managementSide/source/s_notice.png)"></div></center>Notice</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveIssueListing?t=0'><div class="spoIssue spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Issue</div></div></a>
                <a href='/retrieveTenantEntryListing?t=0'><div class="spoEntry spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Entry</div></div></a>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Announcement's Overview &#10148; View Announcement</div>
            </div>
            <div class="cBody">
                <div class="cbPreviewNotice">
                    <div class="cbContent">
                        <div class="cbLabel">ID: <%=notice.getAnnounceID()%></div>
                        <div class="cbLabel">Title: <%=notice.getTitle()%></div>
                    </div>
                    <div class="cbContent">
                        <%=notice.getContent()%>
                    </div>
                    <%
                        if (notice.getAttachmentList().size() != 0) {
                    %>
                    <div class="cbContent">
                        <div class="cbLabel">Attachment List</div>
                        <%
                            for (int j = 0; j < notice.getAttachmentList().size(); j++) {
                                Attachment attachment = notice.getAttachmentList().get(j);
                        %>
                        <a href="<%=attachment.getHeader() + "," + new String(Base64.getEncoder().encode(attachment.getFile()))%>" download>
                            &#128206; File <%=j + 1%>
                        </a>
                        <%
                            }
                        %>
                    </div>
                    <%
                        }
                    %>
                </div>
                <div class="cbContent">
                    <a href="/retrieveNoticeListing"><button class="cbAdd">&#9166; Back</button></a>
                    <a href="/admin/announcement/editAnnouncement"><button class="cbAdd cBAddright">Edit</button></a>
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