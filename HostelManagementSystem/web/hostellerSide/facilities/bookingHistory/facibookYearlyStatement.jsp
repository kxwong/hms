<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="Controller.DateToString"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Facilitybooking"%>
<%@page import="Model.Hosteller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">        
        <link rel="stylesheet" href="../../../managementSide/source/css/report.css">
    </head>
    <%
        Hosteller hosteller = new Hosteller();
        List<Facilitybooking> facibookList = new ArrayList();
        DateToString dateToString = new DateToString();
        Date curDate = new Date();
        List<String> facilityList = new ArrayList();
        SimpleDateFormat formatDate = new SimpleDateFormat("MMMMMMMM yyyy");
        int totalFacibookQty = 0;
        int totalApproveQty = 0;
        int totalRejectedQty = 0;
        int totalPendingQty = 0;
        int totalCancelledQty = 0;

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured.");
            } else {

                hosteller = (Hosteller) session.getAttribute("curHosteller");
                try {
                    if (session.getAttribute("faciBookingList") == null) {
                        throw new Exception("Please a year before proceed.");
                    } else {
                        facibookList = (List<Facilitybooking>) session.getAttribute("faciBookingList");
                        for (int i = 0; i < facibookList.size(); i++) {
                            String facilityName = "";

                            if (facibookList.get(i).getBookFacility().getDescription().split("@").length > 0) {
                                facilityName = facibookList.get(i).getBookFacility().getDescription().split("@")[0];
                            } else {
                                facilityName = facibookList.get(i).getBookFacility().getDescription();
                            }

                            if (!facilityList.contains(facilityName)) {
                                facilityList.add(facilityName);
                            }
                        }

                    }

                } catch (Exception ex) {
                    String errMsg = ex.getMessage();
                    session.setAttribute("errMsg", errMsg);
                    response.sendRedirect("/hosteller/facilities/facilityRecord");
                }
            }
    %>
    <body style="background-color:#525659;">
    <center>
        <div style="box-shadow: 0px 0px 10px 3px  #46494c;" class="reportContent" id="statement">
            <center>
                <div class="Header">
                    <center>
                        <div>
                            <img style="margin-top:2%;" class="logo" src="../../../source/BaikinLogo.png" class="logo"/>
                        </div>                
                    </center>
                    <br/>
                    <h2>Monthly Facility Booking Statistic for <%=formatDate.format(facibookList.get(0).getRequestTime())%></h2>
                    <br/>
                </div> 
                <div>
                    <table>
                        <thead>
                            <tr>
                                <th style="text-align: right" width="13%">Facility Name</th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="18%">Total Booking</th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="11%">Pending Booking</th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="12%">Approved Booking</th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="12%">Rejected Booking</th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="12%">Cancelled Booking</th>
                            </tr>
                        </thead>

                        <tbody>
                            <%
                                for (int i = 0; i < facilityList.size(); i++) {
                                    String facilityName = facilityList.get(i);
                                    int facibookQty = 0;
                                    int approveQty = 0;
                                    int rejectedQty = 0;
                                    int pendingQty = 0;
                                    int cancelledQty = 0;
                                    for (int j = 0; j < facibookList.size(); j++) {
                                        String tempoFacilityName = "";
                                        if (facibookList.get(j).getBookFacility().getDescription().split("@").length > 0) {
                                            tempoFacilityName = facibookList.get(j).getBookFacility().getDescription().split("@")[0];
                                        } else {
                                            tempoFacilityName = facibookList.get(j).getBookFacility().getDescription();
                                        }

                                        if (facilityName.equals(tempoFacilityName)) {
                                            facibookQty++;

                                            if (facibookList.get(j).getStatus().equals("Pending")) {
                                                pendingQty++;
                                            }

                                            if (facibookList.get(j).getStatus().equals("Approved")) {
                                                approveQty++;
                                            }

                                            if (facibookList.get(j).getStatus().equals("Rejected")) {
                                                rejectedQty++;
                                            }

                                            if (facibookList.get(j).getStatus().equals("Cancelled")) {
                                                cancelledQty++;
                                            }

                                        }

                                    }
                            %> 
                            <tr>
                                <td style="text-align: right"><%=facilityName%></td>
                                <td></td>
                                <td style="text-align: right"><%=facibookQty%></td>
                                <td></td>
                                <td style="text-align: right"><%=pendingQty%></td>
                                <td></td>
                                <td style="text-align: right"><%=approveQty%></td>
                                <td></td>
                                <td style="text-align: right"><%=rejectedQty%></td>
                                <td></td>
                                <td style="text-align: right"><%=cancelledQty%></td>
                            </tr>
                            <% 
                                    totalFacibookQty += facibookQty;
                                    totalApproveQty += approveQty;
                                    totalRejectedQty += rejectedQty;
                                    totalPendingQty += pendingQty;
                                    totalCancelledQty += cancelledQty;
                                }
                            %>
                        </tbody>
                        <thead>
                            <tr>
                                <th width="5%">Total: </th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="13%"><%=totalFacibookQty%></th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="18%"><%=totalPendingQty%></th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="11%"><%=totalApproveQty%></th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="18%"><%=totalRejectedQty%></th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="18%"><%=totalCancelledQty%></th>
                            </tr>
                        </thead>
                    </table>  
                    <div style="margin:5%;">
                        <h4>Generated on <%=new Date().toLocaleString()%></h4>
                    </div>                             
                </div>
            </center>
        </div>
    </center>
</body>
<%
    } catch (Exception ex) {
        session.invalidate();
        response.sendRedirect("/login/hosteller");
    }
%>
<script>
    window.onload(PrintElem("statement"));

    function PrintElem(elem)
    {
        var mywindow = window.open('', 'PRINT', 'height=400,width=600');
        mywindow.document.write('<html><head><title>' + '</title>');
        mywindow.document.write('<link rel="stylesheet" href="../../../managementSide/source/css/report.css">');
        mywindow.document.write('</head><body >');
        mywindow.document.write('<h1>' + '</h1>');
        mywindow.document.write(document.getElementById(elem).innerHTML);
        mywindow.document.write('</body></html>');

        mywindow.document.close();
        mywindow.focus();

        mywindow.print();

        return true;
    }


</script>
</html>
