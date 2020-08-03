<%@page import="java.util.Date"%>
<%@page import="Controller.DateToString"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Bill"%>
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
        List<Bill> billList = new ArrayList();
        DateToString dateToString = new DateToString();
        Date curDate = new Date();
        int totalNumberBill = 0;
        int totalPaidBill = 0;
        BigDecimal totalAmt = new BigDecimal(0);

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                try {
                    if (session.getAttribute("billList") == null) {
                        throw new Exception("Please a year before proceed.");
                    } else {
                        billList = (List<Bill>) session.getAttribute("billList");
                    }
                } catch (Exception ex) {
                    String errMgs = ex.getMessage();
                    response.sendRedirect("/hosteller/rental/viewPaymentHistory");
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
                    <h2>Yearly Bill Statement for <%=billList.get(0).getIssueDate().getYear() + 1900%></h2>
                    <br/>
                </div> 
                <div>
                    <table>
                        <thead>
                            <tr>
                                <th style="text-align: right" width="13%">Month</th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="18%">Number of Bill</th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="11%">Bill Paid</th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="12%">Bill Miss</th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="12%">Total Amount (RM)</th>
                            </tr>
                        </thead>

                        <tbody>
                            <%
                                for (int i = 0; i < 12; i++) {
                                    BigDecimal totalBillAmt = new BigDecimal(0);
                                    int totalBillQty = 0;
                                    int totalPaidQty = 0;
                                    for (int j = 0; j < billList.size(); j++) {
                                        int month = billList.get(j).getIssueDate().getMonth();
                                        if (month == i) {
                                            totalBillAmt = totalBillAmt.add(billList.get(j).getTotalAmount());
                                            totalBillQty++;
                                            if (billList.get(j).getPaidDate() != null) {
                                                totalPaidQty++;
                                            }

                                        }
                                    }
                                    String monthStr = dateToString.ToStringMonth(i);
                            %> 
                            <tr>
                                <td style="text-align: right"><%=monthStr%></td>
                                <td></td>
                                <td style="text-align: right"><%=totalBillQty%></td>
                                <td></td>
                                <td style="text-align: right"><%=totalPaidQty%></td>
                                <td></td>
                                <td style="text-align: right"><%=totalBillQty - totalPaidQty%></td>
                                <td></td>
                                <td style="text-align: right"><%=totalBillAmt%></td>
                            </tr>
                            <%      totalNumberBill += totalBillQty;
                                    totalPaidBill += totalPaidQty;
                                    totalAmt = totalAmt.add(totalBillAmt);
                                }
                            %>
                        </tbody>
                        <thead>
                            <tr>
                                <th width="5%">Total: </th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="13%"><%=totalNumberBill%></th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="18%"><%=totalPaidBill%></th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="11%"><%=totalNumberBill-totalPaidBill%></th>
                                <th width="1%"></th>
                                <th style="text-align: right" width="18%"><%=totalAmt%></th>
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
