<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../hostellerSide/source/css/accountManagement/hostellerRegistration.css">
        <title>Hostel Management System</title>
    </head>
    <%
        String errorStatus = "false";
        String errMsg = "false";       
        
        if ((String) session.getAttribute("errorStatus") == "true") {
            errorStatus = (String) session.getAttribute("errorStatus");
        } else {
            errorStatus = "false";
        }

        if (errorStatus.equals("true")) {
            errMsg = (String) session.getAttribute("errMsg");
    %>  
    <div onclick="closeFail()" class="popup" id="errorPopUp">
        <div class="cover">
            <img class="popUpicon" src="../../../hostellerSide/source/failed.png"/>
            <div class="message"><%=errMsg%></div>
        </div>
    </div>            
    <%session.removeAttribute("errorStatus");
        }%>
    <body class="rgBody">
        <div class="registrationContent">
            <div class="registrationBody ">

                <div class="formHeader">
                    <center><img src="../../../hostellerSide/source/daikinLogo.png" class="logo"/></center>
                    <center>Already have an account ? <a href="/login/hosteller">Click here</a> for sign in</center>
                </div>

                <form method="post" action="/hostellerAccountSetting">
                    <div class="personalInfoDiv">
                        <label><h3><center>Hosteller Account Login Credential</center></h3></label>
                        <label>Username</label>
                        <input type="text" size="70" name="username" required="">
                        <label>Password</label>
                        <input type="password" size="70" name="password" onfocus="displayRequirementDiv()" onblur="displayRequirementDivNone()" onkeyup="validatePassword()" id="passwordInput" required="" title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters">    
                        <div id="passwordRequirementDiv" class="passwordRequirementDiv" style="display: none;"><div id="lowerLbl" class="passwordlbl">At least 1 lowercase</div><div id="numberLbl" class="passwordlbl">At least 1 number</div><div id="lengthLbl" class="passwordlbl">Must be between 8 to 16 characters</div></div>
                    </div>
                    <button type="submit" hidden="" id="submit"></button>
                </form>
                <center><button onclick="confirmPassword()">Proceed</button></center>
            </div>
        </div>
    </body>
    <script>
        var lowerValidation = 0;
        var numberValidation = 0;
        var lengthValidation = 0;

        function closeFail() {
            var fail = document.getElementById("errorPopUp");
            fail.style.display = "none";
        }

        function displayRequirementDiv() {
            var div = document.getElementById("passwordRequirementDiv");
            div.style.display = 'block';
        }

        function displayRequirementDivNone() {
            var div = document.getElementById("passwordRequirementDiv");
            div.style.display = 'none';
        }

        function confirmPassword() {
            if (lowerValidation === 1 && numberValidation === 1 && lengthValidation === 1) {
                document.getElementById("submit").click();
            } else {
                alert("Password does not meet requirement, please re-enter.");
            }
        }

        function validatePassword() {

            var passwordInput, filter, lowerLbl, numberLbl, lengthLbl;
            passwordInput = document.getElementById("passwordInput").value;
            filter = passwordInput;

            lowerLbl = document.getElementById("lowerLbl");
            numberLbl = document.getElementById('numberLbl');
            lengthLbl = document.getElementById('lengthLbl');

            var lowerCaseLetters = /[a-z]/g;
            if (filter.match(lowerCaseLetters)) {
                lowerLbl.style.color = 'green';
                lowerValidation = 1;
            } else {
                lowerLbl.style.color = 'red';
                lowerValidation = 0;
            }

            var numbers = /[0-9]/g;
            if (filter.match(numbers)) {
                numberLbl.style.color = 'green';
                numberValidation = 1;
            } else {
                numberLbl.style.color = 'red';
                numberValidation = 0;
            }

            if (filter.length >= 8 && filter.length <= 16) {
                lengthLbl.style.color = 'green';
                lengthValidation = 1;
            } else {
                lengthLbl.style.color = 'red';
                lengthValidation = 0;
            }

        }
    </script>
</html>
