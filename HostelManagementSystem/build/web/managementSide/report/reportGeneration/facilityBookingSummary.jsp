
<%@page import="Model.FacilitybookingManager"%>
<%@page import="Model.Facility"%>
<%@page import="Model.FacilityManager"%>
<%@page import="java.util.Date"%>
<%@page import="Model.Facilitybooking"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Controller.DateToString"%>
<%@page import="Controller.Crypto"%>
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
        DateToString dateString = new DateToString();
        List<Facility> facilityList = (List) session.getAttribute("facilityList");
        List<String> locationList = (List) session.getAttribute("locationList");
        String time = (String) session.getAttribute("time");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">        
        <link rel="stylesheet" href="../../../managementSide/source/css/report.css">
        <title>Hostel Management System</title>
    </head>
    <%
    %>
    <body style="background-color:#525659;">
    <center>
        <div id="content" style="box-shadow: 0px 0px 10px 3px  #46494c;" class="reportContent">
            <div class="reportInside">
                <img src="../../../source/BaikinLogo.png" alt=""/>
                <h1>BAIKIN HOSTEL MANAGEMENT SYSTEM</h1><br/><br/>
                <u><h2>Facility Booking Summary Report <%=time%></h2></u>
                    <%
                        for (int i = 0; i < locationList.size(); i++) {
                            FacilityManager facilityManager = new FacilityManager((EntityManager) session.getAttribute("mgr"));
                            List<String> buildingList = facilityManager.findAllBuilding(locationList.get(i));
                    %>
                <br/><h3 style="text-align: left">Hostel: <%=locationList.get(i)%></h3>
                <table>
                    <thead>
                        <tr>
                            <th width="5%">Block</th>
                            <th width="1%"></th>
                            <th width="20%">Facility</th>
                            <th width="1%"></th>
                            <th width="5%">Total</th>
                            <th width="1%"></th>
                            <th width="5%">Pending</th>
                            <th width="1%"></th>
                            <th width="5%">Approved</th>
                            <th width="1%"></th>
                            <th width="5%">Rejected</th>
                            <th width="1%"></th>
                            <th width="5%">Cancelled</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            int totalBooking = 0;
                            int totalPending = 0;
                            int totalApproved = 0;
                            int totalRejected = 0;
                            int totalCancelled = 0;
                            for (int j = 0; j < buildingList.size(); j++) {
                                int l = 0;
                                for (int k = 0; k < facilityList.size(); k++) {
                                    Facility facility = facilityList.get(k);
                                    if (facility.getHostelID().getLocation().equals(locationList.get(i)) && facility.getHostelID().getBuilding().equals(buildingList.get(j))) {
                                        l++;
                                        FacilitybookingManager fbManager = new FacilitybookingManager((EntityManager) session.getAttribute("mgr"));
                                        List<Facilitybooking> bookingList = fbManager.findAllByDateRangeFacility(dateString.ToStartYear(time), dateString.ToEndYear(time), facility.getFacilityID());
                                        List<Facilitybooking> penbookingList = fbManager.findAllByDateRangeFacilityStatus(dateString.ToStartYear(time), dateString.ToEndYear(time), facility.getFacilityID(), "Pending");
                                        List<Facilitybooking> appbookingList = fbManager.findAllByDateRangeFacilityStatus(dateString.ToStartYear(time), dateString.ToEndYear(time), facility.getFacilityID(), "Approved");
                                        List<Facilitybooking> rejbookingList = fbManager.findAllByDateRangeFacilityStatus(dateString.ToStartYear(time), dateString.ToEndYear(time), facility.getFacilityID(), "Rejected");
                                        List<Facilitybooking> canbookingList = fbManager.findAllByDateRangeFacilityStatus(dateString.ToStartYear(time), dateString.ToEndYear(time), facility.getFacilityID(), "Cancelled");
                                        totalBooking += bookingList.size();
                                        totalPending += penbookingList.size();
                                        totalApproved += appbookingList.size();
                                        totalRejected += rejbookingList.size();
                                        totalCancelled += canbookingList.size();
                        %>
                        <tr>
                            <td><%=l == 1 ? buildingList.get(j) : ""%></td>
                            <td></td>
                            <td><%=facility.getDescription()%></td>
                            <td></td>
                            <td><%=bookingList.size()%></td>
                            <td></td>
                            <td><%=penbookingList.size()%></td>
                            <td></td>
                            <td><%=appbookingList.size()%></td>
                            <td></td>
                            <td><%=rejbookingList.size()%></td>
                            <td></td>
                            <td><%=canbookingList.size()%></td>
                        </tr>
                        <%
                                    }
                                }
                            }
                        %>
                    </tbody>
                    <thead>
                        <tr>
                            <th colspan="3">Total Quantity</th>
                            <th width="1%"></th>
                            <th width="5%"><%=totalBooking%></th>
                            <th width="1%"></th>
                            <th width="5%"><%=totalPending%></th>
                            <th width="1%"></th>
                            <th width="5%"><%=totalApproved%></th>
                            <th width="1%"></th>
                            <th width="5%"><%=totalRejected%></th>
                            <th width="1%"></th>
                            <th width="5%"><%=totalCancelled%></th>
                        </tr>
                    </thead>
                </table>
                <%
                    }
                %>
                <h4 style="text-align: right">Generated on <%=new Date().toLocaleString()%></h4>
            </div>
        </div>
    </center>
</body>
<script>
    window.onload = PrintElem();

    function PrintElem()
    {
        var mywindow = window.open('', 'PRINT', 'height=768,width=1366');
        mywindow.document.write('<html><head><title>' + '</title>');
        mywindow.document.write('<link rel="stylesheet" href="../../../managementSide/source/css/report.css">');
        mywindow.document.write('</head><body><center><div class="reportContent">');
        mywindow.document.write(document.getElementById("content").innerHTML);
        mywindow.document.write('</div></center></body></html>');
        mywindow.document.close();
        mywindow.focus();
        mywindow.print();
        return true;
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