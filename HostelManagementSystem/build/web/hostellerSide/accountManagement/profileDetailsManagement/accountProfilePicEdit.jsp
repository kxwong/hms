<%@page import="java.util.Base64"%>
<%@page import="Model.Hosteller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/accountManagement/accountDetailEdit.css">
        <title>Hostel Management System</title>
    </head>
    <%
        Hosteller hosteller = new Hosteller();

        try {
            if (session.getAttribute("curHosteller").equals(null)) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
            }

            String image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(hosteller.getImage()));
    %>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveHosteller'><div class="hBG"><div class="hOption hChoose"><p>Profile Details</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/hosteller/account/accountSetting'><div class="hBG"><div class="hOption"><p>Account Settings</p></div></div></a>
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
                <div class="chTitle"><a href="/retrieveHosteller">Account details</a> > Edit Profile</div>
            </div>
            <div class="cBody">
                <div class="cbContent">       
                    <form action="/editHostellerProfilePic" method="post">
                        <div class="oldProfilePicPanel" style="width: 25%;">
                            <label>Current Profile</label>
                            <img class="oldProfilePic" src="<%=image%>"/>
                        </div>
                        <div class="newProfilePicPanel" style="width: 25%;">
                            <label>New Profile</label>
                            <img class="newProfilePic" id="preview" src="../../../hostellerSide/source/notAvailable.jpg"/>
                        </div>
                        <center>
                            <div class="profilePicUpdatePanel">
                                <input type="file" id="files" name="profilePicPreview" onchange="fileValidation(this, event)" accept="image/*" required="">
                                <input type="text" name="imageByte" id="byte_content" hidden="">
                            </div>
                        </center>

                        <center>
                            <div class="profilePicUpdatePanel">
                                <button type="submit" class="profilePicUpdateBtn">Confirm Changes</button>
                                <a href="/retrieveHosteller" class="profilePicCancelBtn">Back</a>                            
                            </div>                            
                        </center>
                        <input type="text" hidden="" value="<%=hosteller.getHostellerID()%>" name="hostellerID"/>
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

