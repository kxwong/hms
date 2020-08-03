
<%@page import="Model.Billitem"%>
<%@page import="java.util.List"%>
<%@page import="Model.BillitemManager"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="java.util.Base64"%>
<%@page import="Controller.DateToString"%>
<%@page import="Controller.Crypto"%>
<%@page import="Model.Bill"%>
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
        Bill bill = (Bill) session.getAttribute("bill");
        Crypto crypto = new Crypto();
        DateToString dateString = new DateToString();
        boolean nextStep = false;
        String status = bill.getStatus().toLowerCase();
        if (status.equals("pending") || status.equals("overdue")) {
            nextStep = true;
        }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/viewBill.css">
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
                <div class="chTitle">Billing's Overview &#10148; View Bill </div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <form action="/updateBill" methos="post">
                        <div class="cbFPdetails">
                            <table>
                                <tr>
                                    <td><div class="cbLabel">Bill No.</div><div></div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=bill.getBillNo()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Description</div><div></div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=bill.getDescription()%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Issue Date</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=dateString.ToFormatDate(bill.getIssueDate())%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Due Date</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><%=dateString.ToFormatDate(bill.getDueDate())%></div></td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Bill Item</div></td>
                                    <td> : </td>
                                    <td>
                                        <table>
                                            <%
                                                BillitemManager billItemManager = new BillitemManager((EntityManager) session.getAttribute("mgr"));
                                                List<Billitem> billItemList = billItemManager.findItemListByBillNo(bill.getBillNo());
                                                for (int i = 0; i < billItemList.size(); i++) {
                                            %>
                                            <tr>
                                                <td><div class="cbLabel"><%=billItemList.get(i).getDescription()%></div></td>
                                                <td><div class="cbLabel">RM <%=billItemList.get(i).getFee()%></div></td>
                                            </tr>
                                            <%
                                                }
                                            %>
                                            <tr>
                                                <td><div class="cbLabel">Total Amount</div></td>
                                                <td><div class="cbLabel">RM <%=bill.getTotalAmount()%></div></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Status</div></td>
                                    <td> : </td>
                                    <td>
                                        <%
                                            if (nextStep) {
                                                if (status.equals("pending")) {
                                        %>
                                        <select name="status" class="cbLabel">
                                            <option>Pending</option>
                                            <option>Cancelled</option>
                                        </select>
                                        <%
                                        } 
                                        } else {
                                        %>
                                        <div class="cbLabel"><%=bill.getStatus()%></div>
                                        <%
                                            }
                                        %>
                                    </td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Receipt No.</div></td>
                                    <td> : </td>
                                    <td>
                                        <div class="cbLabel">

                                            <%
                                                try {
                                                    if (bill.getReceiptNo() != null) {
                                            %>
                                            <a href="/retrieveReceiptDetails?p=<%=crypto.BEncode(bill.getReceiptNo().getReceiptNo())%>" class="cbrlRequest"><%=bill.getReceiptNo().getReceiptNo()%></a>
                                            <%
                                            } else {
                                            %>
                                            Not Assigned
                                            <%
                                                }
                                            } catch (Exception ex) {
                                            %>
                                            Not Assigned
                                            <%
                                                }
                                            %>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Remark </div></td>
                                    <td> : </td>
                                    <td>
                                        <%
                                            if (nextStep) {
                                        %>
                                        <input type="text" name="remark" class="cbLabel" 
                                               <%
                                                   if (bill.getRemark() != null) {
                                               %>
                                               value="<%=bill.getRemark()%>"
                                               <%
                                               } else {
                                               %>
                                               value="No Remark"
                                               <%
                                                   }
                                               %>
                                               >
                                        <%
                                        } else {
                                        %>
                                        <div class="cbLabel">
                                            <%
                                                if (bill.getRemark() != null) {
                                            %>
                                            <%=bill.getRemark()%>
                                            <%
                                            } else {
                                            %>
                                            No Remark
                                            <%
                                                }
                                            %>
                                        </div>
                                        <%
                                            }
                                        %>
                                    </td>
                                </tr>
                                <tr>
                                    <td><div class="cbLabel">Issue To</div></td>
                                    <td> : </td>
                                    <td><div class="cbLabel"><a href="/retrieveTenantDetails?p=<%=crypto.HTEncode(bill.getIssueTo().getHostellerID())%>" class="cbrlRequest"><%=bill.getIssueTo().getFirstName()%> <%=bill.getIssueTo().getMiddleName()%> <%=bill.getIssueTo().getLastName()%></a></div></td>
                                </tr>
                            </table>
                        </div>
                        <a href="/retrieveBillListing?t=0"><button type="button" class="cbAdd">&#9166; Back</button></a> 
                        <%
                            if (nextStep) {
                        %>
                        <input type="submit" value="Update" class="cbAdd cBAddright">
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
