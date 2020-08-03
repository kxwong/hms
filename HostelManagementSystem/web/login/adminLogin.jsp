<%
    if (session.getAttribute("account") != null) {
        response.sendRedirect("/retrieveFloorplanListing");
    }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../source/adminLogin.css">
        <title>Hostel Management System</title>
    </head>
    <body class="rgBody">

        <div class="loginContent">
            <div class="loginBody">

                <div class="formHeader">
                    <center><img src="../source/BaikinLogo.png" class="logo"/></center>
                    <center><h2>Hostel System Management Side Portal</h2></center>
                    <center><h3>Login Page</h3></center>
                </div>
                <div class="middleLoginPart">
                    <form action="/adminAuthentication" method="post">
                        <label style="font-size: 18px;">Username</label>
                        <input type="text" name="username" size="20" maxlength="20" required>
                        <label style="font-size: 18px;">Password</label>
                        <input type="password" name="password" size="20" maxlength="20" required> 
                        <center><input type="submit" value="Sign in"></center>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
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