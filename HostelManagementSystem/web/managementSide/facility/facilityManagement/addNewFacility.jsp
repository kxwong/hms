
<%@page import="Model.Hostel"%>
<%@page import="java.util.List"%>
<%@page import="Controller.Crypto"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%
    try {
        AccountManager accountManager = new AccountManager((EntityManager) session.getAttribute("mgr"));
        Account account = (Account) session.getAttribute("account");
        if (!account.equals(accountManager.findAccount(account.getUsername(), account.getPassword()))) {
            throw new Exception("Unauthorized account");
        }
        if (account.getLevel() != 3) {
            throw new Exception("Unauthorized account");
        }
        Crypto crypto = new Crypto();
        List<Hostel> hostelList = (List) session.getAttribute("hostelList");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/addNewFacility.css">
        <script type="text/javascript" src="../../managementSide/source/js/storeImage.js"></script>
        <title>Hostel Management System</title>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveFacilityListing'><div class="hBG"><div class="hOption hChoose"><p>Facility Management</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/retrieveBookingListing?t=0'><div class="hBG"><div class="hOption"><p>Booking Management</p></div></div></a>
            </div>
            <div class="accountCorner"><%=crypto.UNDecode(account.getUsername())%> &#11163;
                <div class="accountSettingList">
                    <a href="/admin/account/changePassword"><div class="accountSettingOption">Change Password</div></a>
                    <a href="/admin/account/authorizeAccount"><div class="accountSettingOption">Authorize Account</div></a>
                    <a href="/accountLogout"><div class="accountSettingOption">Log Out</div></a>
                </div>
            </div>
        </div>
        <div class="sidePanel">
            <div class="spCover">
                <a href='/retrieveFloorplanListing'><div class="spoRoom spoBg"><div class="spOption"><center><div class="spImg " ></div><p>Room</p></center></div></div></a>
                <a href='/retrieveRegistrationListing?t=0'><div class="spoTenant spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Tenant</div></div></a>
                <a href='/retrieveBillListing?t=0'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Rental</div></div></a>
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveFacilityListing'><div class="spoFacilities spoBg"><div class="spOption spoChoose"><center><div class="spImg" style="background-image:url(../../managementSide/source/s_facilities.png)"  ></div></center>Facility</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveNoticeListing'><div class="spoNotice spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Notice</div></div></a>
                <a href='/retrieveIssueListing?t=0'><div class="spoIssue spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Issue</div></div></a>
                <a href='/retrieveTenantEntryListing?t=0'><div class="spoEntry spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Entry</div></div></a>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Facility's Overview &#10148; Add New Facility</div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <form id="sendform" action="/storeFacility" method="post">
                        <table>
                            <tr>
                                <td><div class="cbLabel">Hostel</div><div></div></td>
                                <td> : </td>
                                <td>
                                    <input class="cbInput" type="text" list="hostelList" name="hostel" id="hostel" onclick="clearHostel()" onchange="hostelSelected()" />
                                    <datalist id="hostelList">
                                        <% try {
                                                for (Hostel hostel : hostelList) {%>
                                        <option value="<%=hostel.getHostelID()%>"><span id="<%=hostel.getHostelID()%>1"><%=hostel.getLocation()%></span> <span id="<%=hostel.getHostelID()%>2"><%=hostel.getBuilding()%></span></option>
                                        <%}
                                            } catch (Exception ex) {
                                            }
                                        %>
                                    </datalist>
                                <td rowspan="6" >
                                    <img class="cbPreview" id="preview" width="350px"/>
                                </td>
                            </tr>

                            <tr>
                                <td><div class="cbLabel">Building</div></td>
                                <td> : </td>
                                <td><input class="cbInput" type="text" name="building" id="building" onclick="clearPK()" maxlength="15" required=""></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Location</div></td>
                                <td> : </td>
                                <td><input class="cbInput" type="text" name="location" id="location" onclick="clearPK()" maxlength="30" required=""></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Type</div></td>
                                <td> : </td>
                                <td>
                                    <select id="catchoose" class="cbInput"  name="catdesc" onchange="allowDesc()">
                                        <option>Gym</option>
                                        <option>Swimming Pool</option>
                                        <option>Badminton Court</option>
                                        <option>Tennis Court</option>
                                        <option>Computer Room</option>
                                    </select>
                                </td>
                            </tr>
                            <tr id="courtDesc">
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Category</div></td>
                                <td> : </td>
                                <td>
                                    <select name="category" class="cbInput">
                                        <option>Indoor</option>
                                        <option>Outdoor</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Operating Day</div></td>
                                <td> : </td>
                                <td>
                                    <select name="operatingDay" class="cbInput">
                                        <option>All days</option>
                                        <option>Weekdays</option>
                                        <option>Weekends</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Operating Hours</div></td>
                                <td> : </td>
                                <td>
                                    <input class="cbInput" type="text" onfocus="(this.type = 'time')" onblur="(this.type = 'text')" name="operatingStartHour" step="1800"> 
                                    <i class="cbDash">-</i>
                                    <input class="cbInput" type="text" onfocus="(this.type = 'time')" onblur="(this.type = 'text')" name="operatingEndHour" step="1800">
                                </td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Reminder</div></td>
                                <td> : </td>
                                <td><textarea style="resize:none;" rows="5" name="reminder" class="cbInputArea" form="sendform"></textarea></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Capacity</div></td>
                                <td> : </td>
                                <td><input name="capacity" class="cbInput" type="number" min="1"></td>
                            </tr>
                            <tr>
                                <td><div class="cbLabel">Image</div></td>
                                <td> : </td>
                                <td>
                                    <input type="file" id="files" name="floorplanMap" onchange="fileValidation(this, event)" accept="image/*" required="">
                                    <input type="text" name="imageByte" id="byte_content" hidden>
                                </td>
                            </tr>
                        </table>
                        <a href="/retrieveFacilityListing"><button type="button" class="cbAdd">&#9166; Back</button></a>
                        <input class="cbAdd cBAddright" type="submit" value="Add">
                    </form>
                </div>
            </div>
        </div>
    </body>
    <script type="text/javascript">
        window.onload = allowDesc;
        function allowDesc(){
            var list = document.getElementById("catchoose");
            var opt = list.options[list.selectedIndex];
            var desc = document.getElementById("courtDesc");
            if(opt.text === "Badminton Court" || opt.text === "Tennis Court"){
                desc.innerHTML="";
                desc.innerHTML="<td><div class='cbLabel'>Court Desc</div></td><td> : </td><td><input name='desc' class='cbInput' type='text' ></td>";
            }else{
                desc.innerHTML="";
            }
        }
        var hostelInput = document.getElementById("hostel");
        var buildingInput = document.getElementById("building");
        var locationInput = document.getElementById("location");
        function hostelSelected() {
            var building = document.getElementById(hostelInput.value + "2");
            var location = document.getElementById(hostelInput.value + "1");
            buildingInput.value = building.innerHTML;
            locationInput.value = location.innerHTML;
        }
        function clearHostel() {
            hostelInput.value = '';
            buildingInput.value = '';
            locationInput.value = '';
        }
        function clearPK() {
            hostelInput.value = '';
        }
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
</html>
<%
    } catch (Exception ex) {
        session.invalidate();
        session = request.getSession();
        session.setAttribute("error", ex.getLocalizedMessage());
        response.sendRedirect("/login/admin");
    }
%>
