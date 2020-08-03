
<%@page import="Model.Room"%>
<%@page import="java.util.List"%>
<%@page import="Model.RoomManager"%>
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
        
        RoomManager roomManager = new RoomManager((EntityManager) session.getAttribute("mgr"));
        List<Room> roomList = roomManager.findAll();
        
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/registerVisitor.css">
        <title>Hostel Management System</title>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/guard/visitor/registerVisitor'><div class="hBG"><div class="hOption hChoose"><p>Register Visitor</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/retrieveVisitorListing?t=0'><div class="hBG"><div class=" hOption"><p>Visitor Management</p></div></div></a>
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
                <div class="chTitle">Register Visitor</div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <form action="/storeVisitor" method="post">
                        <table>
                            <tr>
                                <td><div class="cbLabel">Name</div></td>
                                <td> : </td>
                                <td><input  class="cbInput" name="name" type="text" required="" maxlength="50" ></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Identification No</div></td>
                                <td> : </td>
                                <td><input  class="cbInput" name="icNo" type="text" required="" maxlength="20" ></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Mobile Phone</div></td>
                                <td> : </td>
                                <td>
                                    <select style="width:80px" class="cbInput" name="countryCode">
                                        <option>+60</option>
                                        <option>+65</option>
                                        <option>+86</option>
                                        <option>+81</option>
                                        <option>+61</option>
                                    </select>
                                    <input  class="cbInput" name="contactNo" type="text" required="" maxlength="20" >
                                </td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Entry Reason</div></td>
                                <td> : </td>
                                <td><input  class="cbInput" name="entryReason" type="text" required="" maxlength="100" ></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Visit Room</div></td>
                                <td> : </td>
                                <td>
                                    <input class="cbInput" type="text" list="roomList" name="room" required="" />
                                    <datalist id="roomList">
                                        <% try {
                                                for (Room room : roomList) {%>
                                                <option value="<%=room.getRoomNo()%>"><span><%=room.getFloorplanID().getHostelID().getLocation()%></span> <span><%=room.getFloorplanID().getHostelID().getBuilding()%></span></option>
                                        <%}
                                            } catch (Exception ex) {
                                            }
                                        %>
                                    </datalist>
                                </td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Entrance Card</div></td>
                                <td> : </td>
                                <td><input  class="cbInput" name="entCard" type="text" maxlength="30" ></td>
                            </tr>
                        </table>
                        <input class="cbAdd" type="submit" value="Done">
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
