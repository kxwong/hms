<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
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
        String loginStatus = "false";
        Hosteller hosteller = new Hosteller();
        hosteller = (Hosteller) session.getAttribute("curHosteller");
        String image = new String();
        String name = new String();
        String gender = new String();
        List<String> locationList = new ArrayList();

        try {
            if (session.getAttribute("curHosteller").equals(null) || session.getAttribute("locationList") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                locationList = (List) session.getAttribute("locationList");
            }

            image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(hosteller.getImage()));
            name = hosteller.getFirstName() + hosteller.getMiddleName() + " " + hosteller.getLastName();
            gender = hosteller.getGender();

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
                    <form method="post" action="/editHostellerDetails">
                        <div class="profilePicPanel">                           
                            <img class="profilePic" id="preview" style="width:230px;" src="<%=image%>"/>                           
                            <center class="name">
                                <% if (gender.equals("M")) {%>
                                <strong><label>Mr. <%=name%></label></strong>
                                <% } else {%>
                                <strong><label>Ms. <%=name%></label></strong>
                                <%}%>
                            </center>                                 
                            <a href="/hosteller/account/updateProfilePic" class="editProfilePicBtn">Edit Profile Picture</a>
                        </div>

                        <div class="profileDetails">
                            <table>
                                <tbody>
                                <thead style="background-color: #aaa; color: white; font-size: 22px; font-weight: bold;">
                                    <tr><td colspan="2">Personal Info</td></tr>
                                </thead>
                                <tr><td width="40%;">NRIC/Passport</td><td><input type="text" class="inputText"  name="nric" value="<%=hosteller.getIdentificationNo()%>" required=""></td></tr>
                                <tr><td width="40%;">Nationality : </td>
                                    <td>                                
                                        <select class="inputText" id="countrySelection" name="nationality" required="" onchange="retrievePhoneCountryCode()">                                                                              
                                            <option value="Malaysia" <%if (hosteller.getNationality().equals("Malaysia")) {%>selected=""<%}%>>Malaysia</option>
                                            <option value="Singapore" <%if (hosteller.getNationality().equals("Singapore")) {%>selected=""<%}%>>Singapore</option>
                                            <option value="Japan" <%if (hosteller.getNationality().equals("Japan")) {%>selected=""<%}%>>Japan</option>
                                            <option value="China" <%if (hosteller.getNationality().equals("China")) {%>selected=""<%}%>>China</option>
                                            <option value="Australia" <%if (hosteller.getNationality().equals("Australia")) {%>selected=""<%}%>>Australia</option>
                                        </select>
                                    </td>  
                                </tr>
                                </tbody>
                            </table>
                            <input type="text" hidden="" name="countryCode" id="mobileCountryCode" value="">
                        </div>

                        <div class="profileDetails">
                            <table>
                                <tbody>
                                <thead style="background-color: #aaa; color: white; font-size: 22px; font-weight: bold;">
                                    <tr><td colspan="2">Contact Details</td></tr>
                                </thead>
                                <tr><td width="40%;">Address</td><td><input type="text" class="inputText" value="<%=hosteller.getContact().getAddress()%>" name="address" required=""></td></tr>
                                <tr><td width="40%;">Post Code</td><td><input type="text" class="inputText" value="<%=hosteller.getContact().getPostcode()%>" name="postCode" required=""></td></tr>
                                <tr><td width="40%;">City</td><td><input type="text" class="inputText" value="<%=hosteller.getContact().getCity()%>" name="city" required=""></td></tr>
                                <tr><td width="40%;">State/Province</td><td><input type="text" class="inputText" value="<%=hosteller.getContact().getState()%>" name="state" required=""></td></tr>
                                <tr><td width="40%;">Country</td><td><input type="text" class="inputText" value="<%=hosteller.getContact().getCountry()%>" name="country" required=""></td></tr>
                                <tr><td width="40%;"><div id="countryCodeDiv1">Mobile Contact</div></td><td><input type="text" class="inputText" name="mobileContact" value="<%=String.valueOf(hosteller.getContact().getMobilePhone()).substring(2)%>" required="" placeholder="Without country code" title="Without country code"></td></tr>
                                <tr><td width="40%;"><div id="countryCodeDiv2">Emergency Contact</div></td><td><input type="text" class="inputText" name="homeContact" value="<%=String.valueOf(hosteller.getContact().getHomePhone()).substring(2)%>" required="" placeholder="Without country code" title="Without country code"></td></tr>                                
                                <tr><td width="40%;">Email Address</td><td><input type="text" class="inputText" value="<%=hosteller.getContact().getEmail()%>" name="email" required=""></td></tr>
                                </tbody>
                            </table>
                        </div>

                        <div class="profileDetails">
                            <table>
                                <tbody>
                                <thead style="background-color: #aaa; color: white; font-size: 22px; font-weight: bold;">
                                    <tr><td colspan="2">Employee Details</td></tr>
                                </thead>
                                <tr><td width="40%;">Worker ID</td><td><input type="text" class="inputText" value="<%=hosteller.getEmpdetails().getWorkerID()%>" name="workerID" required=""></td></tr>
                                <tr><td width="40%;">Current Branch</td>
                                    <td>                                
                                        <select class="inputText" name="branch" required="">
                                            <%for (int i = 0; i < locationList.size(); i++) {
                                                    if (hosteller.getEmpdetails().getBranch().equals(locationList.get(i))) {%>
                                            <option selected="" value="<%=locationList.get(i)%>"><%=locationList.get(i)%></option>                                   
                                            <% } else {%>
                                            <option value="<%=locationList.get(i)%>"><%=locationList.get(i)%></option>  
                                            <%  }
                                                }%>
                                        </select>
                                    </td>
                                </tr>
                                <tr><td width="40%;">Department</td>
                                    <td>
                                        <select class="inputText" name="department" required="">                                                                              
                                            <option value="Admin" <%if (hosteller.getEmpdetails().getDepartment().equals("Admin")) {%>selected=""<%}%>>Admin</option>
                                            <option value="R&D" <%if (hosteller.getEmpdetails().getDepartment().equals("R&D")) {%>selected=""<%}%>>R&D</option>
                                            <option value="Sales" <%if (hosteller.getEmpdetails().getDepartment().equals("Sales")) {%>selected=""<%}%>>Sales</option>
                                            <option value="Production" <%if (hosteller.getEmpdetails().getDepartment().equals("Production")) {%>selected=""<%}%>>Production</option>
                                            <option value="Management" <%if (hosteller.getEmpdetails().getDepartment().equals("Management")) {%>selected=""<%}%>>Management</option>
                                        </select>                                
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <button type="submit" id="editBtn" hidden=""></button>
                    </form>
                    <button onclick="updateConfirmation()" class="editBtn">Confirm</button>
                    <a href="/retrieveHosteller" class="cancelBtn">Back</a>
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
        function updateConfirmation() {
            if (confirm("Are you sure you want to update your info ?")) {
                document.getElementById("editBtn").click();
            }
        }

        window.onload = retrievePhoneCountryCode();

        function retrievePhoneCountryCode() {

            var country = document.getElementById("countrySelection").value;
            var div1 = document.getElementById("countryCodeDiv1");
            var div2 = document.getElementById("countryCodeDiv2");
            var mobileCode;

            if (country === 'Malaysia') {
                mobileCode = '60';
                div1.innerHTML = " Mobile Contact +(60)";
                div2.innerHTML = " Home Contact +(60)";
            } else if (country === 'Singapore') {
                mobileCode = '65';
                div1.innerHTML = " Mobile Contact +(65)";
                div2.innerHTML = " Home Contact +(65)";
            } else if (country === 'Japan') {
                mobileCode = '86';
                div1.innerHTML = " Mobile Contact +(86)";
                div2.innerHTML = " Home Contact +(86)";
            } else if (country === 'China') {
                mobileCode = '81';
                div1.innerHTML = " Mobile Contact +(81)";
                div2.innerHTML = " Home Contact +(81)";
            } else if (country === 'Australia') {
                mobileCode = '61';
                div1.innerHTML = " Mobile Contact +(61)";
                div2.innerHTML = " Home Contact +(61)";
            }
            console.log(mobileCode);
            document.getElementById("mobileCountryCode").value = mobileCode;

        }

    </script>
</html>

