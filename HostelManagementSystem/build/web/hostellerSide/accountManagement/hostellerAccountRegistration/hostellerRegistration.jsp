<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/accountManagement/hostellerRegistration.css">
        <title>Hostel Management System</title>
    </head>
    <%
        String username = new String();
        String password = new String();
        List<String> locationList = new ArrayList();
        String errorStatus = "false";
        String message = "empty";
        Date maximumDob = new Date();
        Date minimumDob = new Date();
        SimpleDateFormat dobFormat = new SimpleDateFormat("yyyy-MM-dd");

        if ((String) session.getAttribute("errorStatus") == "true") {
            errorStatus = (String) session.getAttribute("errorStatus");
            message = (String) session.getAttribute("errMsg");
        } else {
            errorStatus = "false";
        }

        try {
            if (session.getAttribute("username") == null || session.getAttribute("password") == null || session.getAttribute("locationList") == null) {
                throw new Exception("An error has occured.");
            } else {
                username = (String) session.getAttribute("username");
                password = (String) session.getAttribute("password");
                locationList = (List) session.getAttribute("locationList");
                maximumDob.setYear(maximumDob.getYear()-18);
                minimumDob.setYear(minimumDob.getYear()-100);
            }

            if (errorStatus.equals("true")) {%> 
    <div onclick="closeFail()" class="popup" id="errorPopUp">
        <div class="cover">
            <img class="popUpicon" src="../hostellerSide/source/failed.png"/>
            <div class="message"><%=message%></div>
        </div>
    </div>
    <% session.setAttribute("errorStatus", "false");
        }%>

    <body class="rgBody">

        <div class="registrationContent">
            <div class="registrationBody ">
                <div class="formHeader">
                    <center><img src="../../../hostellerSide/source/daikinLogo.png" class="logo"/></center>
                    <center><p style="font-size: 18px;">Account Registration</p></center>
                    <center><p style="font-size: 18px;">Already have an account ? <a href="/login/hosteller">Click here</a> for sign in</p></center>
                </div>
                <form method="post" action="/hostellerDetailsValidation">
                    <input type="hidden" name="username" value="<%=username%>">
                    <input type="hidden" name="password" value="<%=password%>">
                    <div class="personalInfoDiv">
                        <label><h3><center>Personal Info</center></h3></label>
                        <label>First Name<div style="color:red; display: inline;"> *</div></label>
                        <input pattern="[a-zA-Z\s]+"  type="text" size="70" name="fname" required="" class="requiredField">
                        <label>Middle Name</label>
                        <input pattern="[a-zA-Z\s]+" type="text" size="70" name="mname">
                        <label>Last Name<div style="color:red; display: inline;"> *</div></label>
                        <input pattern="[a-zA-Z\s]+" type="text" size="70" name="lname" required="">
                        <label>Gender<div style="color:red; display: inline;"> *</div>   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="gender" value="m">Male <input type="radio" name="gender" value="f">Female</label>               
                        <label>Date of Birth (Age of 18 above)<div style="color:red; display: inline;"> *</div>  &nbsp;&nbsp;&nbsp;<input type="date" name="dob" style="margin-left: 0px; display: inline-block;" min="<%=dobFormat.format(minimumDob)%>" max="<%=dobFormat.format(maximumDob)%>"></label>                    
                    </div>
                    <hr width="85%"/>
                    <div class="workingStatusDiv">
                        <label><h3>Working Status</h3></label>
                        <label>Branch Selection<div style="color:red; display: inline;"> *</div> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                     
                            <select class="customDropdown" name="branch" required="">
                                <%for (int i = 0; i < locationList.size(); i++) {%>
                                <option value="<%=locationList.get(i)%>" selected=""><%=locationList.get(i)%></option>
                                <%   }%>                              
                            </select>
                        </label>
                        <label>Department<div style="color:red; display: inline;"> *</div> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <select class="customDropdown" name="department" required="">
                                <option value="Admin" selected="">Admin</option>
                                <option value="R&D">R&D</option>
                                <option value="Sales">Sales</option>
                                <option value="Production">Production</option>
                                <option value="Management">Management</option>
                            </select>
                        </label>
                        <label>Worker ID<div style="color:red; display: inline;"> *</div></label>
                        <input pattern="[A-Za-z0-9]+" type="text" size="70" name="workerID" required="">
                    </div>                
                    <hr width="85%"/>
                    <div class="nationailityDiv">
                        <label><h3>Contacts</h3></label>
                        <label>Please select your nationality<div style="color:red; display: inline;"> *</div>
                            <select class="customDropdown" onchange="retrievePhoneCountryCode()" id="countryDropdown" name="nationality" required="">
                                <option value="Malaysia" selected="">Malaysia</option>
                                <option value="Singapore">Singapore</option>
                                <option value="Japan">Japan</option>
                                <option value="China">China</option>
                                <option value="Australia">Australia</option>
                            </select>
                        </label>
                        <label>NRIC / Passport<div style="color:red; display: inline;"> *</div></label>
                        <input pattern="[0-9]+" type="text" size="70" name="nric" required=""> 
                        <label>Address<div style="color:red; display: inline;"> *</div></label>
                        <input pattern="[A-Za-z0-9,-\s]+" type="text" size="70" name="address1" placeholder="Address Line 1" required="">
                        <input pattern="[A-Za-z0-9,-\s]+" type="text" size="70" name="address2" placeholder="Address Line 2">
                        
                        <label>PostCode</label>
                        <input pattern="[0-9]+" type="text" size="10" name="postcode">
                        <label>City</label>
                        <input pattern="[a-zA-Z\s]+" type="text" size="30" name="city">
                        <label>State</label>
                        <input pattern="[a-zA-Z\s]+" type="text" size="30" name="state">
                        <label>Country<div style="color:red; display: inline;"> *</div></label>
                        <input pattern="[a-zA-Z\s]+" type="text" size="30" name="country" required="">

                        <label id="contactNumberLbl" style="display: inline; margin-top:10px;">Contact Number</label><div style="color:red; display: inline;"> *</div>
                        <input type="text" size="70" title="Only numbers" pattern="[0-9]+" name="contactNum" required="">
                        <label id="emergencyNumberLbl">Emergency Number</label>
                        <input type="text" size="70" title="Only numbers" pattern="[0-9]+" name="homeNum">
                        <input type="text" hidden="" name="countryCode" id="mobileCountryCode" value="">
                        <label>Email<div style="color:red; display: inline;"> *</div></label>
                        <input type="text" size="70" pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-z]{2,}$" name="email" placeholder="xxx@gmail.com" required="">                 
                        <label>Please upload a profile picture for your account<div style="color:red; display: inline;"> *</div></label>
                        <center><img class="picPreview" id="preview" src="../../../hostellerSide/source/notAvailable.jpg"/></center>
                        <center><input type="file" id="files" name="profilePicPreview" onchange="fileValidation(this, event)" accept="image/*" required=""></center>
                        <input type="text" name="imageByte" id="byte_content" hidden="">
                    </div>
                    <center><button type="submit">Submit</button></center>
                </form>                   
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

        window.onload = retrievePhoneCountryCode();

        function closeFail() {
            var fail = document.getElementById("errorPopUp");
            fail.style.display = "none";
        }

        function retrievePhoneCountryCode() {

            var country = document.getElementById("countryDropdown").value;
            var contactNumberLbl = document.getElementById("contactNumberLbl");
            var emergencyNumberLbl = document.getElementById("emergencyNumberLbl");
            var mobileCode;

            if (country === 'Malaysia') {
                mobileCode = '60';
            } else if (country === 'Singapore') {
                mobileCode = '65';
            } else if (country === 'Japan') {
                mobileCode = '86';
            } else if (country === 'China') {
                mobileCode = '81';
            } else if (country === 'Australia') {
                mobileCode = '61';
            }
            contactNumberLbl.innerText = "Contact Number " + "(" + mobileCode + ")";
            emergencyNumberLbl.innerText = "Emerygency Number " + "(" + mobileCode + ")";
            document.getElementById("mobileCountryCode").value = mobileCode;

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
        var _validFileExtensions = [".jpg", ".jpeg", ".bmp", ".gif", ".png"];
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


