<%@page import="java.util.Base64"%>
<%@page import="Model.Hosteller"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="Model.Bill"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Billitem"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/rental/viewSelectedBillingDetails.css">
        <title>Hostel Management System</title>
    </head>
    <%
        List<Billitem> billItemList = new ArrayList<Billitem>();
        Bill selectedBill = new Bill();
        Hosteller hosteller = new Hosteller();
        String image = "";

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("selectedBill") == null || session.getAttribute("billItemList") == null) {
                    throw new Exception("An error has occured, please try again.");
                } else {
                    billItemList = (List<Billitem>) session.getAttribute("billItemList");
                    selectedBill = (Bill) session.getAttribute("selectedBill");

                    if (selectedBill.getReceiptRefered() != null) {
                        image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(selectedBill.getReceiptRefered()));
                    }

                }
            }
    %>


    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>   
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/hosteller/rental/viewCurrentBilling'><div class="hBG"><div class="hOption hChoose"><p>Current Billing</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/retrieveMonthlySummary'><div class="hBG"><div class="hOption "><p>Payment History</p></div></div></a> 
                <div style="float:right;">
                    <p style="position:fixed;right:60px; font-size: 20px; color: white;"><%=hosteller.getFirstName()%> <%=hosteller.getMiddleName()%> <%=hosteller.getLastName()%></p>
                    <p style="position:fixed; right:15px;"><a href="/hostellerLogout"><img class="logoutIcon" title="Logout" style="width: 35px; height: 35px;" onclick="logout()" src="../../../hostellerSide/source/logout_icon.png"></a></p>                   
                </div>
            </div>
        </div>
        <div class="sidePanel">
            <div class="spCover">
                <a href='/retrieveCurrentRoomStatus'><div class="spoRoom spoBg"><div class="spOption"><center><div class="spImg" ></div><p>Room</p></center></div></div></a>               
                <a href='/retrieveHosteller'><div class="spoTenant spoBg"><div class="spOption "><center><div class="spImg"></div></center>Profile</div></div></a>
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveCurrentBillingList'><div class="spoRental spoBg"><div class="spOption spoChoose"><center><div class="spImg" style="background-image:url(../../../hostellerSide/source/s_rental.png)"></div></center>Rental</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveAllFacilitiesWithinBranch'><div class="spoFacilities spoBg"><div class="spOption"><center><div class="spImg"  ></div></center>Facility</div></div></a>            
                <a href='/retrieveAllAnnouncement'><div class="spoNotice spoBg"><div class="spOption "><center><div class="spImg"></div></center>Notice</div></div></a>       
                <a href='/retrieveAllCase'><div class="spoReporting spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Report</div></div></a>            
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle"><a href="/hosteller/rental/viewCurrentBilling">Current Billing</a> > <a href="/hosteller/rental/viewSelectedBillingDetails">Selected Bill</a> > Receipt Attachment</div>
            </div>
            <div class="cBody">              
                <div class="cbContent">

                    <p><h2>Please upload your payment prove</h2></p>
                    <form action="/billingPayment" method="post">

                        <%
                            if (image.equals("")) { %>
                        <center>
                            <h3>New prove of payment</h3>
                            <div class="receiptPicPanel">                      
                                <img class="receiptPic" id="preview" src="../../../hostellerSide/source/notAvailable.jpg"/>
                            </div>                               
                        </center>
                        <%   } else {%>
                        <div class="receiptPicPanel">
                            <h3>Current prove of payment</h3>
                            <img class="receiptPic" src=<%=image%> />
                        </div>

                        <div class="receiptPicPanel">   
                            <h3>New prove of payment</h3>
                            <img class="receiptPic" id="preview" src="../../../hostellerSide/source/notAvailable.jpg"/>
                        </div>
                        <%}%>

                        <div class="imageInputDiv">
                            <center>
                                <input type="file" id="files" name="profilePicPreview" onchange="fileValidation(this, event)" accept="image/*" required="">  
                            </center>                                                      
                        </div>                                                                                    
                        <div class="receiptAttachment">                      
                            <input type="text" name="imageByte" id="byte_content" hidden="" required="">
                            <input type="hidden" name="bid" value="<%=selectedBill.getBillNo()%>"/>
                        </div>
                        <button type="submit" id="payBtn" style="display: none;"></button>
                    </form>
                    <center>
                        <button class="payBtn">Cancel</button>
                        <button class="payBtn" onclick="addConfirmation()" type="submit">Upload</button>                            
                    </center>



                </div>
            </div>
        </div>
    </body>
    <%
        } catch (Exception ex) {
            session.invalidate();
            response.sendRedirect("/login/hosteller");
        }
    %>
    <script>              
        function addConfirmation(){           
            if(confirm("Are you sure you want to pay with the new prove ?")){
                document.getElementById("payBtn").click();
            }           
        }               
        
        function previewImage(input) {
            var reader = new FileReader();
            reader.onload = function ()
            {
                var output = document.getElementById('preview');
                output.src = reader.result;
                document.getElementById('byte_content').value = reader.result;
            }
            reader.readAsDataURL(input.target.files[0]);
        }
        var _validFileExtensions = [".jpg", ".jpeg", ".bmp", ".gif", ".png", ".pdf"];
        function fileValidation(oInput, event) {
            if (oInput.type == "file") {
                var sFileName = oInput.value;
                var inputfile = document.getElementById('files');
                var file = inputfile.files[0];
                var output = document.getElementById('previewFloorplan');
                if (sFileName.length > 0) {
                    if (file.size > 1070000) {
                        alert("Sorry, only image file not exceed 1 mb is allowed");
                        oInput.value = "";
                        output.src = "";
                        return false;
                    }
                    var blnValid = false;
                    for (var j = 0; j < _validFileExtensions.length; j++) {
                        var sCurExtension = _validFileExtensions[j];
                        if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                            blnValid = true;
                            previewImage(event);
                            break;
                        }
                    }
                    if (!blnValid) {
                        alert("Sorry, only image file with extensions " + _validFileExtensions.join(", ") + " is allowed");
                        oInput.value = "";
                        output.src = "";
                        return false;
                    }
                }
            }
            return true;
        }
    </script>
</html>


