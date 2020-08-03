
<%@page import="Model.FacilitybookingSearchingCriteria"%>
<%@page import="Controller.DateToString"%>
<%@page import="Model.Facilitybooking"%>
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
        List<Facilitybooking> facilityBookingList = (List) session.getAttribute("facilityBookingList");
        Crypto crypto = new Crypto();
        DateToString dateString = new DateToString();
        FacilitybookingSearchingCriteria searchingCriteria = (FacilitybookingSearchingCriteria) session.getAttribute("fbsearchingCriteria");
        String allClass = "";
        String pendingClass = "";
        String approvedClass = "";
        String rejectedClass = "";
        String cancelledClass = "";
        if (searchingCriteria.getStatus().equals("")) {
            allClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Pending")) {
            pendingClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Approved")) {
            approvedClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Rejected")) {
            rejectedClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Cancelled")) {
            cancelledClass = "cbsChoice";
        }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/bookingListing.css">
        <title>Hostel Management System</title>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <a href='/retrieveFacilityListing'><div class="hBG"><div class="hOption "><p>Facility Management</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveBookingListing?t=0'><div class="hBG"><div class="hOption hChoose"><p>Booking Management</p></div></div></a>
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
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveFacilityListing'><div class="spoFacilities spoBg"><div class="spOption spoChoose"><center><div class="spImg" style="background-image:url(../../managementSide/source/s_facilities.png)"  ></div></center>Facility</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveNoticeListing'><div class="spoNotice spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Notice</div></div></a>
                <a href='/retrieveIssueListing?t=0'><div class="spoIssue spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Issue</div></div></a>
                <a href='/retrieveTenantEntryListing?t=0'><div class="spoEntry spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Entry</div></div></a>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Booking's Overview</div>
            </div>
            <div class="cBody">
                <div class="cbContent cbStatus">
                    <div class="cbsOption <%=allClass%>"><a href="/retrieveBookingListing?t=1&s=<%=crypto.CEncode("All")%>"><center>All</center></a></div>
                    <div class="cbsOption <%=pendingClass%>"><a href="/retrieveBookingListing?t=1&s=<%=crypto.CEncode("Pending")%>"><center>Pending</center></a></div>
                    <div class="cbsOption <%=approvedClass%>"><a href="/retrieveBookingListing?t=1&s=<%=crypto.CEncode("Approved")%>"><center>Approved</center></a></div>
                    <div class="cbsOption <%=rejectedClass%>"><a href="/retrieveBookingListing?t=1&s=<%=crypto.CEncode("Rejected")%>"><center>Rejected</center></a></div>
                    <div class="cbsOption <%=cancelledClass%>"><a href="/retrieveBookingListing?t=1&s=<%=crypto.CEncode("Cancelled")%>"><center>Cancelled</center></a></div>
                </div>
                <div class="cbContent cbSearchCriteria">
                    <form action="/retrieveBookingListing?t=2" method="post">
                        <input class="cbscOption" name="bookingID" type='text' maxlength="30" placeholder="Booking ID">
                        <input class="cbscOption" name="facilityID" type='text' maxlength="30" placeholder="Facility ID">
                        <input class="cbscOption" name="bookStartTime" type="text" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Book Start Time">
                        <input class="cbscOption" name="bookEndTime" type="text" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Book End Time">
                        <input class="cbscOption" name="bookedBy" type='text' maxlength="30" placeholder="Booked By">
                        <input class="cbscOption" name="updateStartTime" type="text" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Update Start Time">
                        <input class="cbscOption" name="updateEndTime" type="text" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Update End Time">
                        <div class="thridLine">
                            <input class="cbscOption cbscSearch" type='submit' value="Search" >
                        </div>
                    </form>
                </div>
                <div class="cbContent cbFunctionList">
                    <a href="" target="_blank" onclick="var ans = ''; ans = window.prompt('Enter report month and year (MM-YYYY):');if (ans === null) {
                                return false;
                            } else if (ans !== '') {
                                this.href = '/initializeReport?t=1&d=' + ans;
                            } else {
                                return false;
                            }"><div class="cbfOption"><center>Transaction Report</center></div></a>
                    <a href="" target="_blank" onclick="var ans = '';
                            ans = window.prompt('Enter report year (YYYY):');
                            if (ans === null) {
                                return false;} else if (ans !== '') {
                                this.href = '/initializeReport?t=2&d=' + ans;} else {
                                return false;
                            }"><div class="cbfOption"><center>Summary Report</center></div></a>
                </div>
                <div class="cbContent cbList">
                    <table>
                        <thead>
                            <tr>
                                <th><a href="/retrieveBookingListing?t=3&o=<%=crypto.CEncode("f.bookingID")%>">Booking ID</a></th>
                                <th><a href="/retrieveBookingListing?t=3&o=<%=crypto.CEncode("f.bookFacility")%>">Facility ID</a></th>
                                <th><a href="/retrieveBookingListing?t=3&o=<%=crypto.CEncode("f.bookBy.firstName, f.bookBy.middleName, f.bookBy.lastName")%>">Booked By</a></th>
                                <th><a href="/retrieveBookingListing?t=3&o=<%=crypto.CEncode("f.bookTime")%>">Book Time</a></th>
                                <th><a href="/retrieveBookingListing?t=3&o=<%=crypto.CEncode("f.updateTime")%>">Update Time</a></th>
                                <th><a href="/retrieveBookingListing?t=3&o=<%=crypto.CEncode("f.status")%>">Status</a></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int i = 0; i < facilityBookingList.size(); i++) {
                                    Facilitybooking facilityBooking = facilityBookingList.get(i);
                            %>
                            <tr>
                                <td><a href="/retrieveFacilityBookingDetails?p=<%=crypto.FBEncode(facilityBooking.getBookingID())%>" class="cbrlRequest"><%=facilityBooking.getBookingID()%></a></td>
                                <td><a href="/retrieveFacilityDetails?p=<%=crypto.FAEncode(facilityBooking.getBookFacility().getFacilityID())%>" class="cbrlRequest"><%=facilityBooking.getBookFacility().getFacilityID()%></a></td>
                                <td><a href="/retrieveTenantDetails?p=<%=crypto.HTEncode(facilityBooking.getBookBy().getHostellerID())%>" class="cbrlRequest"><%=facilityBooking.getBookBy().getFirstName()%> <%=facilityBooking.getBookBy().getMiddleName()%> <%=facilityBooking.getBookBy().getLastName()%></a></td>
                                <td><%=dateString.ToFormatDate(facilityBooking.getBookTime())%></td>
                                <td><%=dateString.ToFormatDate(facilityBooking.getUpdateTime())%></td>
                                <td><%=facilityBooking.getStatus()%></td>
                            </tr>
                            <%
                                }
                            %>
                            <% if (facilityBookingList.isEmpty()) {%>
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
<%    } catch (Exception ex) {
        session.invalidate();
        session = request.getSession();
        session.setAttribute("error", ex.getLocalizedMessage());
        response.sendRedirect("/login/admin");
    }
%>
