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
    <body class="rgBody">
        <div class="registrationContent">
            <div class="registrationBody ">
                <div class="formHeader">
                    <center><img src="../../../hostellerSide/source/daikinLogo.png" class="logo"/></center>
                    <center><p style="font-size: 18px;">Reset Account</p></center>
                    <center><p style="font-size: 18px;">Please insert the username for your account.</p></center>
                </div>
                <form method="post" action="/sendResetEmail">                   
                    <label style="margin-left: 5%; font-size: 18px;">Username</label>
                    <input type="text" name="username" style="width:90%; margin-left: 5%;margin-right: 5%;" required="">                                       
                    <button type="submit" id="resetBtn" hidden=""></button>
                </form> 
                <center><button type="submit" onclick="resetConfirmation()">Submit</button></center>
            </div>
        </div>
    </body>    
    <script>                
        function resetConfirmation(){           
            if(confirm("Are you sure you want to reset password ?")){
                document.getElementById("resetBtn").click();
            }           
        }        
    </script>
    
</html>


