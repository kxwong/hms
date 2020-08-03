<%@page import="Controller.Crypto"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="java.util.Base64"%>
<%@page import="Model.Attachment"%>
<%@page import="Model.Conversation"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="Model.Issue"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Model.Hosteller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/reporting/selectedCaseDetails.css">
        <title>Hostel Management System</title>
    </head>
    <script>
        function closeSuccess() {
            var success = document.getElementById("successPopUp");
            success.style.display = "none";
        }
        function closeFail() {
            var fail = document.getElementById("failPopUp");
            fail.style.display = "none";
        }
    </script>    
    <%
        Hosteller hosteller = new Hosteller();
        Issue curIssue = new Issue();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMMMMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMMMMMM yyyy HH:mm aaa");
        Crypto encrypt = new Crypto();
        List<Conversation> conversationList = new ArrayList();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {

                hosteller = (Hosteller) session.getAttribute("curHosteller");

                if (session.getAttribute("curIssue") == null || session.getAttribute("conversationList") == null) {
                    throw new Exception("An error has occured, please re-login.");
                } else {
                    curIssue = (Issue) session.getAttribute("curIssue");
                    conversationList = (List) session.getAttribute("conversationList");
                }
            }
    %>     
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>           
                <a href='/retrieveAllCase'><div class="hBG"><div class="hOption "><p>Case Searching</p></div></div></a>                          
                <a href='/hosteller/reporting/makeReport'><div class="hBG"><div class="hOption"><p>Making Report</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div> 
                <a href='/retrieveAllSelfCase'><div class="hBG"><div class="hOption hChoose"><p>Case History</p></div></div></a>  
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
                <div class="chTitle"><a href="/retrieveAllSelfCase">Personal Case Records</a> > Case Details</div>
            </div>
            <div class="cBody">
                <%if (session.getAttribute("successMsg") != null) {%>
                <center>
                    <div onclick="closeSuccess()" class="popup" id="successPopUp">
                        <div class="cover">
                            <img class="popUpicon" src="../../../hostellerSide/source/success.png"/>
                            <div class="message"><%=String.valueOf(session.getAttribute("successMsg").toString())%></div>
                        </div>
                    </div>
                </center>
                <%  session.removeAttribute("successMsg");
                    }%>
                <%if (session.getAttribute("errMsg") != null) {%>
                <center>
                    <div onclick="closeFail()" class="popup" id="failPopUp">
                        <div class="cover">
                            <img class="popUpicon" src="../../../hostellerSide/source/failed.png"/>
                            <div class="message"><%=String.valueOf(session.getAttribute("errMsg").toString())%></div>
                        </div>
                    </div>
                </center>
                <%  session.removeAttribute("errMsg");
                    }%>                   
                <div class="cbContent">
                    <div class="reportCriteria">
                        <table class="selectedCaseTable">
                            <tbody>
                                <tr>
                                    <td style="font-size: 20px" width="50%"><strong>Case ID : <%=curIssue.getCaseNo()%></strong></td>
                                    <td style="font-size: 20px"><strong>Reported on: <%=dateFormat.format(curIssue.getIssueDate())%></strong></td>
                                </tr>
                                <tr>
                                    <td>
                                        Category
                                        <div class="customDropdown"><%=curIssue.getCategory()%></div>
                                    </td>
                                    <td>
                                        Type of issue
                                        <div class="customDropdown"><%=curIssue.getIssueType()%></div>
                                    </td>                                   
                                </tr>
                                <tr>                                   
                                    <td style="font-size:20px;">
                                        Title : <%=curIssue.getTitle()%>
                                    </td>
                                    <td>
                                        <%if (!(curIssue.getStatus().equals("Closed"))) {%>
                                        <form action="/closeCase" method="post">
                                            <input type="text" hidden="" value="<%=curIssue.getCaseNo()%>" name="iid">
                                            <button style="" type="submit">Close Case</button>
                                        </form>  
                                        <% }%>
                                    </td>
                                </tr> 
                                <tr><td><strong>Additional Attachment</strong></td></tr>
                                <tr>
                                    <td colspan="2">
                                        <%

                                            if (curIssue.getAttachmentList() != null) {
                                                List<Attachment> attachmentList = curIssue.getAttachmentList();
                                                if (attachmentList.size() > 0) {

                                                    for (int i = 0; i < attachmentList.size(); i++) {
                                                        String file_src = attachmentList.get(i).getHeader() + "," + new String(Base64.getEncoder().encode(attachmentList.get(i).getFile()));%>
                                        <a href="<%=file_src%>" download="attachment<%=i + 1%>">Attachment <%=i + 1%></a>                                         
                                        <% } %>  
                                    </td>
                                    <% } else {%>
                                <tr><td>No file is attached</td></tr>
                                <% } %>

                                </tr> 
                                <% } else { %>

                                <%  } %>
                            </tbody>
                        </table>
                    </div>

                    <center><h2 class="discussionTitle">Discussion</h2></center>
                    <hr style="width: 98%;"/>
                    <div class="discussionDiv">                                                                                               
                        <%
                            for (int i = 0; i < conversationList.size(); i++) {
                                if (conversationList.get(i).getReplyBy().equals(hosteller.getUsername())) {

                        %>
                        <div class="conversation leftSide">
                            <strong><%=encrypt.UNDecode(conversationList.get(i).getReplyBy().getUsername())%> on <%=timeFormat.format(conversationList.get(i).getTime())%></strong>
                            <p class="messageP"><%=conversationList.get(i).getContent()%></p>                                     
                        </div>
                        <%    } else {
                        %>
                        <div class="conversation rightSide">
                            <strong><%=encrypt.UNDecode(conversationList.get(i).getReplyBy().getUsername())%> on <%=timeFormat.format(conversationList.get(i).getTime())%></strong>
                            <p class="messageP"><%=conversationList.get(i).getContent()%></p>                                     
                        </div>

                        <%}
                            }%>
                        <hr style="margin-top: 20px;"/>
                        <center><h2 class="discussionTitle">End of conversation</h2></center>   

                        <%
                            if (!(curIssue.getStatus().equals("Closed"))) {%>
                        <div style="width:49%; display: inline-block;">
                            <form action="/addMessage" method="post">
                                <input class="newMessage" type="text" name="replyMessage" placeholder="Insert new message here">
                                <input type="text" hidden="" name="caseID" value="<%=curIssue.getCaseNo()%>">
                                <button  type="submit">Send Message</button>                                
                            </form>
                        </div>
                        <div style="width:49%; display: inline-block;">
                            <form action="/addAttachment" method="post">
                                <input type="file" name="newAttachment" id="files" onchange="removeInput();fileValidation(this, event)" accept="image/*" required="" multiple="true"/>                             
                                <div id="attachmentDiv">                               
                                </div>                                
                                <input type="text" hidden="" name="caseID" value="<%=curIssue.getCaseNo()%>">
                                <button style="width: 40%;" type="submit">Send Attachment</button>                                
                            </form>                              
                        </div>
                        <%   }%>                       
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
    </script>
</html>
