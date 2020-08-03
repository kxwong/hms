
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="java.util.Base64"%>
<%@page import="Controller.DateToString"%>
<%@page import="Model.Hosteller"%>
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
        Hosteller hosteller = (Hosteller) session.getAttribute("hosteller");
        String status = "Disable";
        if (hosteller.getStatus().toLowerCase().equals("inactive")) {
            status = "Enable";
        }
        if (hosteller.getRegReqNo().getStatus().toLowerCase().equals("pending")) {
            status = "Approve";
        }
        DateToString dateString = new DateToString();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/viewTenant.css">
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
                <div class="chTitle">Tenant's Overview &#10148; View Tenant </div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <div class="cbFPdetails">
                        <div class="cbfImage"><img width="100%" height="100%" src="<%=hosteller.getImage() == null ? "" : "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(hosteller.getImage()))%>" alt="Profile Picture"></div>
                        <table class="cbPIList">
                            <tr>
                                <th colspan="3">Personal Information</th>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Hosteller ID</div><div></div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=hosteller.getHostellerID()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Full Name</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=hosteller.getFirstName()%> <%=hosteller.getMiddleName()%> <%=hosteller.getLastName()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Birth Date</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=dateString.ToFormatDate(hosteller.getDoB())%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Gender</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=hosteller.getGender().equals("M") ? "Male" : "Female"%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Nationality</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=hosteller.getNationality()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Status</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=hosteller.getStatus()%></div></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Remark</div></td>
                                <td> : </td>
                                <td><div class="cbLabel"><%=hosteller.getRemark() == null ? "No Remark" : hosteller.getRemark()%></div></td>
                            </tr>
                        </table>
                    </div>
                    <div class="cbFPdetails">
                        <div class="cbOther">
                            <table class="cbPIList otherList">
                                <tr>
                                    <th colspan="3">Contact</th>
                                </tr>
                                <%
                                    String address = hosteller.getContact().getAddress();
                                    String addressLine1 = "";
                                    String addressLine2 = "";
                                    int count = 0;
                                    for(String part:address.split(" ")){
                                        count += part.length();
                                        if(count <= 20){
                                            addressLine1 += part + " ";
                                        }else if(count<=40){
                                            addressLine2 += part + " ";
                                        }else{
                                            addressLine2 +="...";
                                        }
                                    }
                                %>
                                <tr>
                                    <td><div class="cbLabel">Address</div><div></div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=addressLine1%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel"></div><div></div></td>
                                    <td> </td>
                                    <td><div class="cbLabel"><%=addressLine2%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Postcode</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=hosteller.getContact().getPostcode()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">City</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=hosteller.getContact().getCity()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">County</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=hosteller.getContact().getCountry()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Mobile Phone</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel">+<%=hosteller.getContact().getMobilePhone()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Home Phone</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel">+<%=hosteller.getContact().getHomePhone()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Email</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=hosteller.getContact().getEmail()%></div></td>
                                </tr>
                                <tr style="height:17px"></tr>
                            </table>
                        </div>
                        <div class="cbOther">
                            <table class="cbPIList otherList">
                                <tr>
                                    <th colspan="3">Other Information</th>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Registration No.</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><a href="/retrieveRegistrationDetails?p=<%=crypto.RREncode(hosteller.getRegReqNo().getRequestNo())%>" class="cbrlRequest"><%=hosteller.getRegReqNo().getRequestNo()%></a></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Entrance Card No.</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=hosteller.getEntCardNo() == null ? "No Ent Card" : hosteller.getEntCardNo().getEntCardNo()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Username</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=crypto.UNDecode(hosteller.getUsername().getUsername())%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Stayed Room</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=hosteller.getStayRoom() == null ? "No Room Assigned" : "<a class='cbrlRequest' href='/retrieveRoomDetail?p=" + crypto.REncode(hosteller.getStayRoom().getRoomNo()) + "'>" + hosteller.getStayRoom().getRoomNo() + "</a>"%></div></td>
                                </tr>
                            </table>
                            <table class="cbPIList otherList">
                                <tr>
                                    <th colspan="3">Employee Information</th>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Branch</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=hosteller.getEmpdetails().getBranch()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Department</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=hosteller.getEmpdetails().getDepartment()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Worker ID</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=hosteller.getEmpdetails().getWorkerID()%></div></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <form action="/updateTenantStatus" method="post">
                        <a href="/retrieveTenantListing?t=0"><button type="button" class="cbAdd">&#9166; Back</button></a> 
                        <%
                            if (status.toLowerCase().equals("approve")) {
                        %>
                        <input type="submit" value="Reject" class="cbAdd cBAddright" name="update">
                        <input type="submit" value="<%=status%>" style="margin-right:20px;" class="cbAdd cBAddright" name="update">
                        <%
                        } else {
                        %>
                        <input type="submit" value="<%=status%>" class="cbAdd cBAddright" name="update">
                        <%
                            }
                        %>
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
