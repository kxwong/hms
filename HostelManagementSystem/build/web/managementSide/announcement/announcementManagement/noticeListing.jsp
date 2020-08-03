
<%@page import="java.util.Base64"%>
<%@page import="Model.Attachment"%>
<%@page import="Controller.DateToString"%>
<%@page import="Model.Announcement"%>
<%@page import="java.util.List"%>
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
        DateToString dateStirng = new DateToString();
        List<Announcement> noticeList = (List) session.getAttribute("noticeList");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/noticeManagement.css">
        <title>Hostel Management System</title>
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
                <div class="chTitle">Announcement's Overview</div>
            </div>
            <div class="cBody">
                <div class="cbContent cbFunction">
                    <a href="/admin/announcement/makeAnnouncement"><div class="cbAdd"><center>Make Announcement</center></div></a>
                    <input class="cbSearch" type='text' maxlength="30" placeholder="Search" onkeyup="searchNotice(this)">
                </div>
                <div class="cbContent cbList">
                    <table>
                        <thead>
                            <tr>
                                <th style="width:10%" >ID</th>
                                <th>Title</th>
                                <th style="width:25%" >Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int i = 0; i < noticeList.size(); i++) {
                                    Announcement notice = noticeList.get(i);
                            %>
                            <tr class="announcement">
                                <td><a href="/retrieveNoticeDetails?p=<%=crypto.ANEncode(notice.getAnnounceID())%>" class="cbrlRequest"><%=notice.getAnnounceID()%></a></td>
                                <td><%=notice.getTitle()%></td>
                                <td><%=dateStirng.ToFormatDate(notice.getDate())%></td>
                            </tr>
                            <%
                                }
                            %>
                            <% if (noticeList.isEmpty()) {%>
                            <tr class="announcement">
                                <td colspan="3"><center>Sorry, there are no any matching data currently.</center></td>
                            </tr>
                            <%}%>
                        </tbody>
                    </table>
                </div>
            </div>   
        </div>
    </body>
    <script type="text/javascript">
        function searchNotice(input) {
            var x = document.getElementsByClassName("announcement");
            for (i = 0; i < x.length; i++) {
                var y = x[i].children;
                if ((y[0].children[0].innerHTML.toLowerCase().concat(" ", y[1].innerHTML.toLowerCase()).indexOf(input.value.toLowerCase()) !== -1)) {
                    x[i].className = "announcement";
                } else {
                    x[i].className = "announcement hidden";
                }
            }
        }
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
<%    } catch (Exception ex) {
        session.invalidate();
        session = request.getSession();
        session.setAttribute("error", ex.getLocalizedMessage());
        response.sendRedirect("/login/admin");
    }
%>
