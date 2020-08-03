<%@page import="Controller.Crypto"%>
<%@page import="Model.Account"%>
<%@page import="Model.Hosteller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/accountManagement/accountSettings.css">
        <title>Hostel Management System</title>
    </head>
    <%
        Hosteller hosteller = new Hosteller();
        Account account = new Account();
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller").equals(null)) {
                throw new Exception("An error has occured, please re-login again.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                account = hosteller.getUsername();
            }
    %>    
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>            
                <a href='/retrieveHosteller'><div class="hBG"><div class="hOption "><p>Profile Details</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/hosteller/account/accountSetting'><div class="hBG"><div class="hOption hChoose"><p>Account Settings</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <div style="float:right;">
                    <p style="position:fixed;right:60px; font-size: 20px; color: white;"><%=hosteller.getFirstName()%> <%=hosteller.getMiddleName()%> <%=hosteller.getLastName()%></p>
                    <p style="position:fixed; right:15px;"><a href="/hostellerLogout"><img class="logoutIcon" title="Logout" style="width: 35px; height: 35px;" onclick="logout()" src="../../../hostellerSide/source/logout_icon.png"></a></p>                   
                </div>
            </div>
        </div>
        <div class="sidePanel">
            <div class="spCover">
                <a href='/retrieveCurrentRoomStatus'><div class="spoRoom spoBg"><div class="spOption"><center><div class="spImg" ></div><p>Room</p></center></div></div></a>   
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveHosteller'><div class="spoTenant spoBg"><div class="spOption spoChoose"><center><div class="spImg" style="background-image:url(../../../hostellerSide/source/s_tenant.png)"></div></center>Profile</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveCurrentBillingList'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg"></div></center>Rental</div></div></a>
                <a href='/retrieveAllFacilitiesWithinBranch'><div class="spoFacilities spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Facility</div></div></a>
                <a href='/retrieveAllAnnouncement'><div class="spoNotice spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Notice</div></div></a>
                <a href='/retrieveAllCase'><div class="spoReporting spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Report</div></div></a>            
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Account Setting</div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <form action="/accountSettingUpdate" method="post">
                        <div class="accountSettingTbl">
                            <table>
                                <tbody>
                                    <tr><td style="width: 30%;">Username</td><td colspan="2"><%=encrypt.UNDecode(account.getUsername())%></td></tr>
                                    <tr><td>Old Password<div style="color:red; display: inline;"> *</div></td><td colspan="2"><input required="" type="password" name="currentPassword"></td></tr>
                                    <tr id="passwordRequirementDiv" style="display: none;"><td id="lowerLbl" style="color: red;"><strong>At least 1 Digit</strong></td><td id="numberLbl" style="color: red;"><strong>At least 1 letter</strong></td><td id="lengthLbl" style="color: red;"><strong>Must be between 8 to 16 characters</strong></td></tr>
                                    <tr><td style="width: 30%;">New Password<div style="color:red; display: inline;"> *</div></td><td colspan="2"><input onfocus="displayRequirementDiv()" onblur="displayRequirementDivNone()" onkeyup="validatePassword()" id="passwordInput" required="" type="password" name="newPassword"></td></tr>
                                    <tr><td style="width: 30%;">New Password Confirmation<div style="color:red; display: inline;"> *</div></td><td colspan="2"><input required="" type="password" name="newPasswordConfirmation"></td></tr>
                                <input hidden type="text" value="<%=encrypt.UNDecode(account.getUsername())%>" name="username">
                                </tbody>
                            </table>                      
                        </div>
                        <button type="submit" class="saveBtn">Save</button>
                        <button type="reset" class="cancelBtn">Cancel</button>
                    </form>
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
        function displayRequirementDiv() {
            var div = document.getElementById("passwordRequirementDiv");
            div.style.display = '';
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
            } else {
                lowerLbl.style.color = 'red';
            }

            var numbers = /[0-9]/g;
            if (filter.match(numbers)) {
                numberLbl.style.color = 'green';
            } else {
                numberLbl.style.color = 'red';
            }

            if (filter.length >= 8 && filter.length <= 16) {
                lengthLbl.style.color = 'green';
            } else {
                lengthLbl.style.color = 'red';
            }

        }
    </script>

</html>
