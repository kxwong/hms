
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Model.RoombookingSearchingCriteria"%>
<%@page import="Controller.Crypto"%>
<%@page import="Controller.DateToString"%>
<%@page import="Model.Roombooking"%>
<%@page import="java.util.List"%>
<%
    try {
        AccountManager accountManager = new AccountManager((EntityManager)session.getAttribute("mgr"));
        Account account = (Account)session.getAttribute("account");
        if (!account.equals(accountManager.findAccount(account.getUsername(), account.getPassword()))) {
            throw new Exception("Unauthorized account");
        }
        if (account.getLevel() != 3) {
            throw new Exception("Unauthorized account");
        }
        List<Roombooking> roomBookingList = (List) session.getAttribute("roomBookingList");
        Crypto crypto = new Crypto();
        DateToString dateString = new DateToString();
        RoombookingSearchingCriteria searchingCriteria = (RoombookingSearchingCriteria) session.getAttribute("rbsearchingCriteria");
        String allClass = "";
        String pendingClass = "";
        String approvedClass = "";
        String paidClass = "";
        String completedClass = "";
        if (searchingCriteria.getStatus().equals("")) {
            allClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Pending")) {
            pendingClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Approved")) {
            approvedClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Paid")) {
            paidClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Assigned") || searchingCriteria.getStatus().equals("Rejected") || searchingCriteria.getStatus().equals("Cancelled") || searchingCriteria.getStatus().equals("Overdue")) {
            completedClass = "cbsChoice";
        }

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/roomrequestlisting.css">
        <title>Hostel Management System</title>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <a href='/retrieveFloorplanListing'>
                    <div class="hBG">
                        <div class="hOption">
                            <p>Hostel Setup</p>
                        </div>
                    </div>
                </a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveRoomRequestListing?t=0'>
                    <div class="hBG">
                        <div class="hOption hChoose">
                            <p>Room Allocation</p>
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
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveFloorplanListing'><div class="spoRoom spoBg"><div class="spOption spoChoose"><center><div class="spImg " style="background-image:url(../../managementSide/source/s_room.png)" ></div><p>Room</p></center></div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveRegistrationListing?t=0'><div class="spoTenant spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Tenant</div></div></a>
                <a href='/retrieveBillListing?t=0'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Rental</div></div></a>
                <a href='/retrieveFacilityListing'><div class="spoFacilities spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Facility</div></div></a>
                <a href='/retrieveNoticeListing'><div class="spoNotice spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Notice</div></div></a>
                <a href='/retrieveIssueListing?t=0'><div class="spoIssue spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Issue</div></div></a>
                <a href='/retrieveTenantEntryListing?t=0'><div class="spoEntry spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Entry</div></div></a>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Room's Request Overview</div>
            </div>
            <div class="cBody">
                <div class="cbContent cbStatus">
                    <div class="cbsOption <%=allClass%>"><a href="/retrieveRoomRequestListing?t=1&s=<%=crypto.CEncode("All")%>"><center>All</center></a></div>
                    <div class="cbsOption <%=pendingClass%>"><a href="/retrieveRoomRequestListing?t=1&s=<%=crypto.CEncode("Pending")%>"><center>Pending</center></a></div>
                    <div class="cbsOption <%=approvedClass%>"><a href="/retrieveRoomRequestListing?t=1&s=<%=crypto.CEncode("Approved")%>"><center>Approved</center></a></div>
                    <div class="cbsOption <%=paidClass%>"><a href="/retrieveRoomRequestListing?t=1&s=<%=crypto.CEncode("Paid")%>"><center>Paid</center></a></div>
                    <div class="cbsList <%=completedClass%>"><center>Completed:</center>
                        <div class="cbsCompletedList">
                            <div class="cbsCompletedOption"><a href="/retrieveRoomRequestListing?t=1&s=<%=crypto.CEncode("Assigned")%>"><center>Assigned</center></a></div>
                            <div class="cbsCompletedOption"><a href="/retrieveRoomRequestListing?t=1&s=<%=crypto.CEncode("Rejected")%>"><center>Rejected</center></a></div>
                            <div class="cbsCompletedOption"><a href="/retrieveRoomRequestListing?t=1&s=<%=crypto.CEncode("Cancelled")%>"><center>Cancelled</center></a></div>
                        </div>
                    </div>
                </div>
                <div class="cbContent cbSearchCriteria">
                    <form action="/retrieveRoomRequestListing?t=2" method="post">
                        <input class="cbscOption" type='text' name="requestNo" maxlength="30" placeholder="Request No.">
                        <input class="cbscOption" type='text' name="requestType" maxlength="30" placeholder="Request Type">
                        <input class="cbscOption" type="text" name="requestSTime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Request Start Time">
                        <input class="cbscOption" type="text" name="requestETime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Request End Time">
                        <input class="cbscOption" type='text' name="requestRoom" maxlength="30" placeholder="Request Room">
                        <input class="cbscOption" type='text' name="requestBillNo" maxlength="30" placeholder="Bill No.">
                        <input class="cbscOption" type="text" name="updateSTime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Update Start Time">
                        <input class="cbscOption" type="text" name="updateETime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Update End Time">
                        <div class="thridLine">
                            <input class="cbscOption cbscSearch" type='submit' value="Search" >
                        </div>
                    </form>
                </div>
                <div class="cbContent cbList">
                    <table>
                        <thead>
                            <tr>
                                <th><a href="/retrieveRoomRequestListing?t=3&o=<%=crypto.CEncode("r.requestNo")%>">Request No.</a></th>
                                <th><a href="/retrieveRoomRequestListing?t=3&o=<%=crypto.CEncode("r.requestType")%>">Request Type</a></th>
                                <th><a href="/retrieveRoomRequestListing?t=3&o=<%=crypto.CEncode("r.requestDate")%>">Request Date</a></th>
                                <th><a href="/retrieveRoomRequestListing?t=3&o=<%=crypto.CEncode("r.requestRoom.roomNo")%>">Request Room</a></th>
                                <th><a href="/retrieveRoomRequestListing?t=3&o=<%=crypto.CEncode("r.updateDate")%>">Update Date</a></th>
                                <th><a href="/retrieveRoomRequestListing?t=3&o=<%=crypto.CEncode("r.billNo.billNo")%>">Bill No.</a></th>
                                <th><a href="/retrieveRoomRequestListing?t=3&o=<%=crypto.CEncode("r.status")%>">Status</a></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int i = 0; i < roomBookingList.size(); i++) {
                                    Roombooking roomBooking = roomBookingList.get(i);
                                    String billNo = "Not Assigned";
                                    try {
                                        if (roomBooking.getBillNo() != null) {
                                            billNo = "<a href='' class='cbrlRequest'>" + roomBooking.getBillNo().getBillNo() + "</a>";
                                        }
                                    } catch (Exception ex) {
                                    }
                            %>
                            <tr>
                                <td><a href="/retrieveRoomBookingDetails?p=<%=crypto.RBEncode(roomBooking.getRequestNo())%>" class="cbrlRequest"><%=roomBooking.getRequestNo()%></a></td>
                                <td><%=roomBooking.getRequestType()%></td>
                                <td><%=dateString.ToFormatDate(roomBooking.getRequestDate())%></td>
                                <td><a href="/retrieveRoomDetail?p=<%=crypto.REncode(roomBooking.getRequestRoom().getRoomNo())%>" class="cbrlRequest"><%=roomBooking.getRequestRoom().getRoomNo()%></a></td>
                                <td><%=dateString.ToFormatDate(roomBooking.getUpdateDate())%></td>
                                <td><%=billNo%></td>
                                <td><%=roomBooking.getStatus()%></td>
                            </tr>
                            <%
                                }
                            %>
                            <% if (roomBookingList.isEmpty()) {%>
                            <tr>
                                <td colspan="7"><center>Sorry, there are no any matching data currently.</center></td>
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
                if (successmsg !=='null') {
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
