<%@page import="Model.Hosteller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/reporting/makeReport.css">
        <title>Hostel Management System</title>
    </head>
    <%
        Hosteller hosteller = new Hosteller();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
            }
    %>    

    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>           
                <a href='/retrieveAllCase'><div class="hBG"><div class="hOption "><p>Case Searching</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>            
                <a href='/hosteller/reporting/makeReport'><div class="hBG"><div class="hOption hChoose"><p>Making Report</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/retrieveAllSelfCase'><div class="hBG"><div class="hOption"><p>Case History</p></div></div></a>   
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
                <a href='/retrieveCurrentBillingList'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg " ></div></center>Rental</div></div></a>           
                <a href='/retrieveAllFacilitiesWithinBranch'><div class="spoFacilities spoBg"><div class="spOption "><center><div class="spImg"  ></div></center>Facility</div></div></a>           
                <a href='/retrieveAllAnnouncement'><div class="spoNotice spoBg"><div class="spOption "><center><div class="spImg"></div></center>Notice</div></div></a>   
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveAllCase'><div class="spoReporting spoBg"><div class="spOption spoChoose"><center><div class="spImg " style="background-image:url(../../../hostellerSide/source/s_issue.png)"></div></center>Report</div></div></a> 
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Reporting issue</div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <div class="reportCriteria">
                        <p>Tell us more about your issue.</p>
                        <form action="/createReport" method="post">
                            <table>
                                <tbody>
                                    <tr>
                                        <td>
                                            Select category
                                            <select class="customDropdown" id="categoryDropDown" onchange="changeIssueType()" name="category" required="1">
                                                <option value="Suspicious Activity">Suspicious Activity</option>
                                                <option value="Facilities Problem">Facilities Problem</option>
                                                <option value="System Issue">System Issue</option>
                                                <option value="General Issue">General Issue</option>
                                            </select>
                                        </td>
                                        <td width="50%">
                                            Select type of issue
                                            <select class="customDropdown" id="issueDropDown" name="type" required="1">
                                                <option value="Complaint" selected="">Vandalisme</option>
                                                <option value="Complaint">Complaint</option>
                                                <option value="Complaint">Complaint</option>
                                                <option value="Complaint">Complaint</option>
                                            </select>
                                        </td>                                   
                                    </tr>
                                    <tr>                                   
                                        <td>
                                            Issue Title
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan='2'><input name="title" type="text" required="1"/></td>
                                    </tr>
                                    <tr>                                   
                                        <td>Issue Details</td>
                                    </tr>
                                    <tr>
                                        <td colspan='2'><textarea name="issueDetails" rows="10" cols="100%" placeholder="Tell us about the issue in details"></textarea></td>
                                    </tr>
                                </tbody>
                            </table>

                            <div class="btmText">
                                Additional Attachment                        
                            </div>

                            <button id="submitBtn" type='submit' style="display: none;"></button>

                            <div>
                                <input type="file" name="profilePicPreview" id="files" onchange="removeInput(); fileValidation(this, event)" accept="image/*" multiple="true"/>                             
                            </div>

                            <div id="attachmentDiv">                               
                            </div>
                        </form>

                        <div class="btmText">
                            <button class="submitBtn" style="margin-left: 87%;" onclick="addConfirmation()" type='submit'>Submit</button>                            
                        </div>
                    </div>
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
        var byte = [];
        function removeInput() {
            var inputs = document.getElementsByName("imageByte"), index;
            if (inputs) {
                for (index = inputs.length - 1; index >= 0; index--) {
                    inputs[index].parentNode.removeChild(inputs[index]);
                }
            }
        }

        var _validFileExtensions = [".jpg", ".jpeg", ".bmp", ".gif", ".png", ".pdf"];
        function fileValidation(oInput, event) {
            if (oInput.type == "file") {
                var sFileName = oInput.value;
                var inputfile = document.getElementById('files').files;
                for (var i = 0; i < inputfile.length; i++) {
                    var file = inputfile[i];
                    console.log(file);
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
                                previewImage(event, i);
                                break;
                            }
                        }
                        if (!blnValid) {
                            alert("Sorry, only image file with extensions " + _validFileExtensions.join(", ") + " is allowed");
                            oInput.value = "";
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        function previewImage(input, counter) {
            var reader = new FileReader();
            reader.onload = function () {
                var div = document.getElementById("attachmentDiv");
                var input = document.createElement("input");
                input.type = "text";
                input.name = "imageByte";
                input.id = "byte_content" + counter;
                input.hidden = "hidden";
                input.value = reader.result;
                div.appendChild(input);
                console.log("haha " + counter + " " + reader.result);
            }
            reader.readAsDataURL(input.target.files[counter]);
        }

        function addConfirmation() {
            if (confirm("Are you sure you want to add this report?")) {
                document.getElementById("submitBtn").click();
            }
        }
        
        window.onload(changeIssueType())

        function changeIssueType() {

            var category = document.getElementById("categoryDropDown");
            var categoryValue = category.options[category.selectedIndex].value;
            var dropdown = document.getElementById("issueDropDown");

            var option0 = dropdown.options[0];
            var option1 = dropdown.options[1];
            var option2 = dropdown.options[2];
            var option3 = dropdown.options[3];
            
            console.log()

            var j;

            switch (categoryValue) {
                case "Suspicious Activity":
                    console.log()
                    option0.innerHTML = "Vandalisme";
                    option0.value = "Vandalisme";
                    option0.attribute = "Selected";
                    option1.innerHTML = "Breaking of rule and regulation";
                    option2.value = "Breaking of rule and regulation";
                    option2.innerHTML = "Entry of unknown personnel";
                    option2.value = "Entry of unknown personnel";
                    option3.innerHTML = "Others";
                    option3.value = "Others";
                    break;
                case "Facilities Problem":
                    option0.innerHTML = "Facilities Broken";
                    option0.value = "Facilities Broken";
                    option0.attribute = "Selected";
                    option1.innerHTML = "Internet problem";
                    option1.value = "Internet problem";
                    option2.innerHTML = "Slow internet speed";
                    option2.value = "Slow internet speed";
                    option3.innerHTML = "Others";
                    option3.value = "Others";
                    break;
                case "System Issue":
                    option0.innerHTML = "Lack of functionality";
                    option0.value = "Lack of functionality";
                    option0.attribute = "Selected";
                    option1.innerHTML = "Usability Problem";
                    option1.value = "Usability Problem";
                    option2.innerHTML = "Lack of usable info";
                    option2.value = "Slow internet speed";
                    option3.innerHTML = "Others";
                    option3.value = "Others";
                    break;
                case "General Issue":
                    option0.innerHTML = "Payment Problem";
                    option0.value = "Payment Problem";
                    option0.attribute = "Selected";
                    option1.innerHTML = "Room Booking Problem";
                    option1.value = "Room Booking Problem";
                    option2.innerHTML = "Facility Booking Problem";
                    option2.value = "Facility Booking Problem";
                    option3.innerHTML = "Others";
                    option3.value = "Others";
                    break;
            }

        }

    </script>
</html>
