
<%@page import="Model.Visitor"%>
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
        Visitor visitor = (Visitor) session.getAttribute("visitor");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/viewVisitor.css">
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
                <div class="chTitle">Visitor's Overview &#10148; View Visitor </div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <div class="cbFPdetails">
                        <table class="cbPIList">
                            <tr>
                                <td><div class="cbLabel">Visitor ID</div><div></div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitor.getVisitorID()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Name</div><div></div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitor.getName()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Identification NO</div><div></div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitor.getIdentificationNo()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Contact No</div><div></div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitor.getMobilePhone()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Entry Reason</div><div></div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitor.getEntryReason()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Visit Room</div><div></div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitor.getVisitRoom().getRoomNo()%> <%=visitor.getVisitRoom().getFloorplanID().getHostelID().getLocation()%> <%=visitor.getVisitRoom().getFloorplanID().getHostelID().getBuilding()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Assigned Entrance Card</div><div></div></td>
                                <td> : </td>
                                <td>
                                    <% try {%>
                                    <div class="cbLabel"><%=visitor.getEntCardNo().getEntCardNo().split("_")[1]%></div>
                                    <% } catch (Exception ex) {%>
                                    <div class="cbLabel"><%=visitor.getEntCardNo().getEntCardNo()%></div>
                                    <% } %>
                                </td>
                            </tr>
                            <%try {
                                    if (visitor.getEntCardNo().getEntrecordList().get(0).getGate().toLowerCase().equals("entry")) {%>
                            <tr>
                                <td><div class="cbLabel">Entry Time</div><div></div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=visitor.getEntCardNo().getEntrecordList().get(0).getAccessTime().toLocaleString()%></div></td>
                            </tr>
                            <%}
                                } catch (Exception ex) {
                                }%>
                        </table>
                    </div>
                    <a href="/retrieveVisitorListing?t=0"><button type="button" class="cbAdd">&#9166; Back</button></a> 
                </div>   
            </div>   
        </div>
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
