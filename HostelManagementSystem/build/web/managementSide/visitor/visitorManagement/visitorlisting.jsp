
<%@page import="Model.Visitor"%>
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
        if (account.getLevel() != 2) {
            throw new Exception("Unauthorized account");
        }
        Crypto crypto = new Crypto();
        List<Visitor> visitorList = (List) session.getAttribute("visitorList");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/visitorlisting.css">
        <title>Hostel Management System</title>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <a href='/guard/visitor/registerVisitor'><div class="hBG"><div class="hOption "><p>Register Visitor</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveVisitorListing?t=0'><div class="hBG"><div class="hChoose hOption"><p>Visitor Management</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
            </div>
            <div class="accountCorner"><%=crypto.UNDecode(account.getUsername())%> &#11163;
                <div class="accountSettingList">
                    <a href="/accountLogout"><div class="accountSettingOption">Log Out</div></a>
                </div>
            </div>
        </div>
        <div class="sidePanel">
            <div class="spCover">
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/guard/visitor/registerVisitor'><div class="spoTenant spoBg"><div class="spOption spoChoose"><center><div class="spImg" style="background-image:url(../../managementSide/source/s_visitor.png)"></div></center>Visitor</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Visitor's Overview</div>
            </div>
            <div class="cBody">
                <form action="/retrieveVisitorListing?t=2" method="post">
                    <div class="cbContent cbSearchCriteria">
                        <input class="cbscOption" type='text' name="visitorID" maxlength="30" placeholder="Visitor ID">
                        <input class="cbscOption" type='text' name="visitorName" maxlength="30" placeholder="Visitor Name">
                        <input class="cbscOption" type='text' name="visitorIC" maxlength="30" placeholder="Identification No.">
                        <div class="thridLine">
                            <input class="cbscOption cbscSearch" type='submit' value="Search" >
                        </div>
                    </div>
                </form>
                <div class="cbContent cbList">
                    <table>
                        <thead>
                            <tr>
                                <th><a href="/retrieveVisitorListing?t=3&o=<%=crypto.CEncode("v.visitorID")%>">Visitor ID</a></th>
                                <th><a href="/retrieveVisitorListing?t=3&o=<%=crypto.CEncode("v.name")%>">Visitor Name</a></th>
                                <th><a href="/retrieveVisitorListing?t=3&o=<%=crypto.CEncode("v.identificationNo")%>">Identification No</a></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int i = 0; i < visitorList.size(); i++) {
                                    Visitor visitor = visitorList.get(i);
                            %>
                            <tr>
                                <td><a href="/retrieveVisitorDetails?p=<%=crypto.VIEncode(visitor.getVisitorID())%>" class="cbrlRequest"><%=visitor.getVisitorID()%></a></td>
                                <td><%=visitor.getName()%></td>
                                <td><%=visitor.getIdentificationNo()%></td>
                            </tr>
                            <%
                                }
                            %>
                            <% if (visitorList.isEmpty()) {%>
                            <tr>
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
