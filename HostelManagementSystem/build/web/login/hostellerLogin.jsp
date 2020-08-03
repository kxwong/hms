<%-- 
    Document   : hostellerRegistration
    Created on : Jul 19, 2019, 10:23:58 AM
    Author     : light
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../source/hostellerLogin.css">
        <title>Hostel Management System</title>
    </head>

    <script>
        function closeSuccess() {
            var fail = document.getElementById("successPopUp");
            fail.style.display = "none";
        }
        function closeFail() {
            var fail = document.getElementById("errorPopUp");
            fail.style.display = "none";
        }
    </script>

    <%
        String successStatus = "false";
        String errorStatus = "false";
        String message = "empty";

        if ((String) session.getAttribute("successStatus") == "true") {
            successStatus = (String) session.getAttribute("successStatus");
        } else {
            successStatus = "false";
        }

        if ((String) session.getAttribute("errorStatus") == "true") {
            errorStatus = (String) session.getAttribute("errorStatus");
            message = (String) session.getAttribute("errMsg");
        } else {
            errorStatus = "false";
        }

        if (successStatus.equals("true")) {
            message = (String) session.getAttribute("successMsg");
    %>              
    <div onclick="closeSuccess()" class="popup" id="successPopUp">
        <div class="cover">
            <img class="popUpicon" src="../hostellerSide/source/success.png"/>
            <div class="message"><%=message%></div>
        </div>
    </div>
    <% session.setAttribute("successStatus", "false");}%>
    
    <%if (errorStatus.equals("true")) {%> 
    <div onclick="closeFail()" class="popup" id="errorPopUp">
        <div class="cover">
            <img class="popUpicon" src="../hostellerSide/source/failed.png"/>
            <div class="message"><%=message%></div>
        </div>
    </div>
    <% session.setAttribute("errorStatus", "false"); session.invalidate();}%>

    <body class="rgBody">
        <div class="loginContent">
            <div class="loginBody">

                <div class="formHeader">
                    <center><img src="../source/BaikinLogo.png" class="logo"/></center>
                    <center><h2>Hostel System Hosteller Side Portal</h2></center>
                    <center><h3>Login Page</h3></center>
                </div>
                <div class="middleLoginPart">
                    <form action="/hostellerLoginValidation" method="post">
                        <label style="font-size: 18px;">Username</label>
                        <input type="text" size="70" name="username" id="username" required="">
                        <label style="font-size: 18px;">Password &nbsp;<a href="/hosteller/account/resetPassword">(Forgot password ?)</a></label>
                        <input type="password" size="70" name="password" id="password" required=""> 
                        <center><button type="submit">Sign in</button></a></center>
                    </form>

                </div>
                <div class="bottomLoginPart">
                    <hr width="25%" style="float: left;margin: 10px;"> <div style="float:left;"> New to our hostel ? </div> <hr width="25%" style="float: left;margin: 10px;"/>                  
                </div>
                <div class="createAccountBtn">
                    <center><a href="/hosteller/account/accountCredential"><button type="submit">Create Account</button></a></center>
                </div>
            </div>
        </div>
    </body>
</html>
