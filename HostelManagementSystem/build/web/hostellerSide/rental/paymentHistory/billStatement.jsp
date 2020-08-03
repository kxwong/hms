<%@page import="java.util.List"%>
<%@page import="Model.Billitem"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Bill"%>
<%@page import="Model.Hosteller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">        
        <link rel="stylesheet" href="../../../hostellerSide/source/css/rental/billStatement.css">
    </head>
    <%
        Hosteller hosteller = new Hosteller();
        Bill curBill = new Bill();
        List<Billitem> billitemList = new ArrayList();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                try {
                    if (session.getAttribute("curBill") == null || session.getAttribute("curBillItems") == null) {
                        throw new Exception("Please a receipt before proceed.");
                    } else {
                        curBill = (Bill) session.getAttribute("curBill");
                        billitemList = (List) session.getAttribute("curBillItems");
                    }
                } catch (Exception ex) {
                    String errMgs = ex.getMessage();
                    response.sendRedirect("/hosteller/rental/viewPaymentHistory");
                }
            }
    %>
    <body style="background-color: rgba(82, 86, 89)">
        <div class="statementForeground" id="statement" style="height: 100%;">
            <center>
                <div class="Header">
                    <center>
                        <div>
                            <img style="margin-top:2%;" class="logo" src="../../../hostellerSide/source/daikinLogo.png" class="logo"/>
                        </div>                
                    </center>
                    <h2>RECEIPT</h2>
                </div> 
                <div class="content">
                    <table class="contentTable">
                        <tbody>
                            <tr><td>Receipt Number</td><td><%=curBill.getReceiptNo().getReceiptNo()%></td></tr>
                            <tr><td>Name</td><td><%=hosteller.getFirstName() + " " + hosteller.getMiddleName() + " " + hosteller.getLastName()%></td></tr>
                            <tr><td>Bill Number Ref.</td><td><%=curBill.getBillNo()%></td></tr>
                            <tr><td>Description</td><td><%=curBill.getDescription()%></td></tr>
                            <tr><td>Bill Amount (RM)</td><td><%=curBill.getTotalAmount()%></td></tr>
                            <tr></tr>                            
                            <%
                                for (int i = 0; i < billitemList.size(); i++) {

                                    if (i == 0) {%>
                            <tr>
                                <td rowspan="<%=billitemList.size()%>" style="vertical-align: top;">Item</td>
                                <td><%=i + 1%>. <%=billitemList.get(i).getDescription()%> (RM <%=billitemList.get(i).getFee()%>)</td>
                            </tr> 
                            <%  } else {%>
                            <tr><td><%=i + 1%>. <%=billitemList.get(i).getDescription()%> (RM <%=billitemList.get(i).getFee()%>)</td></tr>
                            <%}
                            %>

                            <%   }%>                                                                                  
                            <tr><td>Amount Paid (RM)</td><td><%=curBill.getTotalAmount()%></td></tr>
                            <tr><td>Receipt Date</td><td><%=curBill.getReceiptNo().getPaidDate().toLocaleString()%></td></tr>
                        </tbody>
                    </table>                
                </div>

                <div class="footer">
                    <p>1. This is a computer generated receipt and does not require a signature.</p>
                    <p>2. This receipt is generated on <%=curBill.getReceiptNo().getGenerateDate().toLocaleString()%></p>
                </div>
            </center>
        </div>
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
            mywindow.document.write('<link rel="stylesheet" href="../../../hostellerSide/source/css/rental/billStatement.css">');
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
