
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Model.BillSearchingCriteria"%>
<%@page import="Controller.DateToString"%>
<%@page import="Controller.Crypto"%>
<%@page import="Model.Bill"%>
<%@page import="java.util.List"%>
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
        List<Bill> billList = (List) session.getAttribute("billList");
        Crypto crypto = new Crypto();
        DateToString dateString = new DateToString();
        BillSearchingCriteria searchingCriteria = (BillSearchingCriteria) session.getAttribute("bsearchingCriteria");
        String allClass = "";
        String pendingClass = "";
        String overdueClass = "";
        String paidClass = "";
        String cancelledClass = "";
        if (searchingCriteria.getStatus().equals("")) {
            allClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Pending")) {
            pendingClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Overdue")) {
            overdueClass = "cbsChoice";
        } else if (searchingCriteria.getStatus().equals("Paid")) {
            paidClass = "cbsChoice";
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
        <link rel="stylesheet" href="../../managementSide/source/css/billListing.css">
        <title>Hostel Management System</title>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveBillListing?t=0'>
                    <div class="hBG">
                        <div class="hOption hChoose">
                            <p>Bill Management</p>
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
                <a href='/retrieveFloorplanListing'><div class="spoRoom spoBg"><div class="spOption"><center><div class="spImg"></div><p>Room</p></center></div></div></a>
                <a href='/retrieveRegistrationListing?t=0'><div class="spoTenant spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Tenant</div></div></a>
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveBillListing?t=0'><div class="spoRental spoBg"><div class="spOption spoChoose"><center><div class="spImg"  style="background-image:url(../../managementSide/source/s_rental.png)" ></div></center>Rental</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveFacilityListing'><div class="spoFacilities spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Facility</div></div></a>
                <a href='/retrieveNoticeListing'><div class="spoNotice spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Notice</div></div></a>
                <a href='/retrieveIssueListing?t=0'><div class="spoIssue spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Issue</div></div></a>
                <a href='/retrieveTenantEntryListing?t=0'><div class="spoEntry spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Entry</div></div></a>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Billing's Overview</div>
            </div>
            <div class="cBody">
                <div class="cbContent cbStatus">
                    <div class="cbsOption <%=allClass%> "><a href="/retrieveBillListing?t=1&s=<%=crypto.CEncode("All")%>"><center>All</center></a></div>
                    <div class="cbsOption <%=pendingClass%> "><a href="/retrieveBillListing?t=1&s=<%=crypto.CEncode("Pending")%>"><center>Pending</center></a></div>
                    <div class="cbsOption <%=overdueClass%> "><a href="/retrieveBillListing?t=1&s=<%=crypto.CEncode("Overdue")%>"><center>Overdue</center></a></div>
                    <div class="cbsOption <%=paidClass%> "><a href="/retrieveBillListing?t=1&s=<%=crypto.CEncode("Paid")%>"><center>Paid</center></a></div>
                    <div class="cbsOption <%=cancelledClass%> "><a href="/retrieveBillListing?t=1&s=<%=crypto.CEncode("Cancelled")%>"><center>Cancelled</center></a></div>
                </div>
                <div class="cbContent cbSearchCriteria">
                    <form action="/retrieveBillListing?t=2" method="post">
                        <input class="cbscOption" type='text' name="billno" maxlength="30" placeholder="Bill No.">
                        <input class="cbscOption" type='text' name="desc" maxlength="30" placeholder="Description">
                        <input class="cbscOption" type="text" name="issueSTime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Issue Start Time">
                        <input class="cbscOption" type="text" name="issueETime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Issue End Time">
                        <input class="cbscOption" type='text' name="issueTo" maxlength="30" placeholder="Issue To">
                        <input class="cbscOption" type="text" name="dueSTime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Due Start Time">
                        <input class="cbscOption" type="text" name="dueETime" onfocus="(this.type = 'date')" onblur="(this.type = 'text')" placeholder="Due End Time">
                        <div class="thridLine">
                            <input class="cbscOption cbscSearch" type='submit' value="Search" >
                        </div>
                    </form>
                </div>
                <div class="cbContent cbFunctionList">
                    <a href="/initializeIssueBill"><div class="cbfOption"><center>Issue Bill</center></div></a>
                    <a href="" target="_blank" onclick="var ans = ''; ans = window.prompt('Enter report year (YYYY):');if (ans === null) {
                                return false;
                            } else if (ans !== '') {
                                this.href = '/initializeReport?t=4&d=' + ans;
                            } else {
                                return false;
                            }"><div class="cbfOption"><center>Summary Report</center></div></a>
                </div>
                <div class="cbContent cbList">
                    <table>
                        <thead>
                            <tr>
                                <th><a href="/retrieveBillListing?t=3&o=<%=crypto.CEncode("b.billNo")%>">Bill No.</a></th>
                                <th><a href="/retrieveBillListing?t=3&o=<%=crypto.CEncode("b.description")%>">Description</a></th>
                                <th><a href="/retrieveBillListing?t=3&o=<%=crypto.CEncode("b.totalAmount")%>">Amount</a></th>
                                <th><a href="/retrieveBillListing?t=3&o=<%=crypto.CEncode("b.issueDate")%>">Issue Date</a></th>
                                <th><a href="/retrieveBillListing?t=3&o=<%=crypto.CEncode("b.dueDate")%>">Due Date</a></th>
                                <th><a href="/retrieveBillListing?t=3&o=<%=crypto.CEncode("b.issueTo.firstName, b.issueTo.middleName, b.issueTo.lastName")%>">Issue To</a></th>
                                <th><a href="/retrieveBillListing?t=3&o=<%=crypto.CEncode("b.status")%>">Status</a></th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int i = 0; i < billList.size(); i++) {
                                    Bill bill = billList.get(i);
                            %>
                            <tr>
                                <td><a href="/retrieveBillDetails?p=<%=crypto.BEncode(bill.getBillNo())%>" class="cbrlRequest"><%=bill.getBillNo()%></a></td>
                                <td><%=bill.getDescription()%></td>
                                <td><%=bill.getTotalAmount()%></td>
                                <td><%=dateString.ToFormatDate(bill.getIssueDate())%></td>
                                <td><%=dateString.ToFormatDate(bill.getDueDate())%></td>
                                <td><a href="/retrieveTenantDetails?p=<%=crypto.HTEncode(bill.getIssueTo().getHostellerID())%>" class="cbrlRequest"><%=bill.getIssueTo().getFirstName()%> <%=bill.getIssueTo().getMiddleName()%> <%=bill.getIssueTo().getLastName()%></a></td>
                                <td><%=bill.getStatus()%></td>
                            </tr>
                            <%
                                }
                            %>
                            <% if (billList.isEmpty()) {%>
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
