<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Model.Contact"%>
<%@page import="Model.Empdetails"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Base64"%>
<%@page import="Model.Hosteller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/accountManagement/accountManagement.css">
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
        String updateSuccess = new String();
        String message = new String();
        Hosteller hosteller = new Hosteller();
        String image = new String();
        String name = new String();
        String gender = new String();

        try {

            if (session.getAttribute("curHosteller").equals(null)) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
            }

            if (session.getAttribute("updateSuccess") == null) {

            } else if (session.getAttribute("updateSuccess").equals("true")) {
                updateSuccess = "true";
                message = (String) session.getAttribute("message");
            } else if (session.getAttribute("updateSuccess").equals("false")) {
                updateSuccess = "false";
                message = (String) session.getAttribute("message");
            } else {
                updateSuccess = "empty";
            }

            image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(hosteller.getImage()));
            name = hosteller.getFirstName() + hosteller.getMiddleName() + " " + hosteller.getLastName();
            gender = hosteller.getGender();

            if (updateSuccess.equals("true")) {%>
    <div onclick="closeSuccess()" class="popup" id="successPopUp">
        <div class="cover">
            <img class="popUpicon" src="../../../hostellerSide/source/success.png"/>
            <div class="message"><%=message%></div>
        </div>
    </div>            
    <%session.setAttribute("updateSuccess", "empty");
            session.setAttribute("message", "empty");
        }%>  

    <% if (updateSuccess.equals("false")) {%>
    <div onclick="closeFail()" class="popup" id="failPopUp">
        <div class="cover">
            <img class="popUpicon" src="../../../hostellerSide/source/failed.png"/>
            <div class="message"><%=message%></div>
        </div>
    </div>            
    <%session.setAttribute("updateSuccess", "empty");
            session.setAttribute("message", "empty");
        }%>  

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
                <div class="chTitle">Account details</div>
            </div>
            <div class="cBody">
                <div class="cbContent">

                    <div class="profilePicPanel">
                        <img src="<%=image%>" class="profilePic"/>
                        <center class="name">
                            <% if (gender.equals("M")) {%>
                            <strong><label>Mr. <%=name%></label></strong>

                            <% } else {%>
                            <strong><label>Ms. <%=name%></label></strong>
                            <%}%>
                        </center>  
                    </div>
                    <div class="profileDetails">
                        <table>
                            <tbody>
                                <tr><td class="lblTxt" >NRIC/Passport</td><td><%=hosteller.getIdentificationNo()%></td></tr>
                                <tr><td class="lblTxt">Nationality : </td><td><%=hosteller.getNationality()%></td></tr>
                                <tr><td class="lblTxt">Current Branch :</td><td><%=hosteller.getEmpdetails().getBranch()%></td></tr>
                                <tr><td class="lblTxt">Department :</td><td><%=hosteller.getEmpdetails().getDepartment()%></td></tr>
                                <tr><td class="lblTxt">Worker ID : </td><td><%=hosteller.getEmpdetails().getWorkerID()%></td></tr>
                                <tr><td class="lblTxt">Mobile Contact Num :</td><td><%=hosteller.getContact().getMobilePhone()%></td></tr>
                                <tr><td class="lblTxt">Email</td><td><%=hosteller.getContact().getEmail()%></td></tr>
                                <%
                                    if (hosteller.getEntCardNo() != null) {%>
                                <tr><td class="lblTxt">Entrance Card ID :</td><td><%=hosteller.getEntCardNo().getEntCardNo()%></td></tr> 
                                <%  }else {%>
                                <tr><td class="lblTxt">Entrance Card ID :</td><td>N/A</td></tr> 
                                <%  }%>
                            </tbody>
                        </table>                                                 
                    </div>
                    <a href="/retrieveHostellerDetails" class="editBtn">Edit Profile</a>
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
</html>
