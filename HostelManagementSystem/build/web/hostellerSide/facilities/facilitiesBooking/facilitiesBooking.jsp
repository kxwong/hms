<%@page import="Model.Facilitybooking"%>
<%@page import="Controller.Crypto"%>
<%@page import="java.util.Base64"%>
<%@page import="Model.Hosteller"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Facility"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/facilities/facilitiesBooking.css">
        <title>Hostel Management System</title>
    </head>
    <%
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEEEEEE");
        SimpleDateFormat formatTime = new SimpleDateFormat("HHmm");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat formatSelectDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatTimeSlot = new SimpleDateFormat("dd MMM yyyy HHmm");

        Facility selectedFacility = new Facility();
        Date currentDate = new Date();
        currentDate.setDate(currentDate.getDate() + 1);
        Date curDate = new Date();
        Date maxDate = new Date();
        Hosteller hosteller = new Hosteller();
        String facilityType = "";

        List<Facilitybooking> facilityBookingList = new ArrayList();

        String image = new String();
        String[] reminderList;
        List<Facility> facilityCourtList = new ArrayList();

        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller") == null || session.getAttribute("selectedFacility") == null || session.getAttribute("facilityBookingList") == null || session.getAttribute("facilityCourtList") == null) {
                throw new Exception("An error has occured, please re-login again.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                facilityBookingList = (List) session.getAttribute("facilityBookingList");
                if (session.getAttribute("curDate") != null) {
                    curDate = (Date) session.getAttribute("curDate");
                }

                selectedFacility = (Facility) session.getAttribute("selectedFacility");
                facilityCourtList = (List<Facility>) session.getAttribute("facilityCourtList");
                image = "data:image/jpeg;base64," + new String(Base64.getEncoder().encode(selectedFacility.getImage()));

                String reminder = selectedFacility.getReminder();
                reminderList = reminder.split("\\.");

                maxDate.setDate(maxDate.getDate() + 14);
            }
    %>

    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveAllFacilitiesWithinBranch'><div class="hBG"><div class="hOption hChoose"><p>Facilities Booking</p></div></div></a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/retrieveAllBookingRecord'><div class="hBG"><div class="hOption"><p>Booking History</p></div></div></a>
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
                <a href='/retrieveCurrentBillingList'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg"></div></center>Rental</div></div></a>
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveAllFacilitiesWithinBranch'><div class="spoFacilities spoBg"><div class="spOption spoChoose"><center><div class="spImg"  style="background-image:url(../../../hostellerSide/source/s_facilities.png)"></div></center>Facility</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveAllAnnouncement'><div class="spoNotice spoBg"><div class="spOption "><center><div class="spImg"></div></center>Notice</div></div></a>       
                <a href='/retrieveAllCase'><div class="spoReporting spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Report</div></div></a>            
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle"><a href="/retrieveAllFacilitiesWithinBranch">Facilities Overview</a> > Facility Selection</div>
            </div>
            <div class="cBody">                         
                <div class='cbContent facilityDetail' style="margin-bottom: 5%; height: auto; display: inline-block;">
                    <h2>Facility Details</h2>
                    <div class="facilityPicPanel" style="display: inline-block; background-image: url(<%=image%>);">
                    </div>

                    <div style="width:45%;float: right;box-shadow: 3px 3px 5px #aaa; padding: 12px;">
                        <label style="font-size: 20px;"><strong>Reminder</strong></label>                        
                        <% int j = 0;
                            for (int i = 0; i < reminderList.length; i++) {
                                j += 1;%>
                        <p><%=j%>. <%=reminderList[i]%></p>
                        <%  }%>                        
                    </div>                    
                    <div class="rightPanel" style="display:inline-block;">
                        <label style="font-size: 20px;"><h4>Timetable Selection (You can only book after today date) </h4></label>
                        <form action="/retrieveFacilityTimetable">
                            <label style="font-size: 20px"><strong>Date</strong></label>
                            <input style="margin-left: 1%;" type="date" name="curDate" value="<%=formatSelectDate.format(curDate)%>" min="<%=formatSelectDate.format(currentDate)%>" max="<%=formatSelectDate.format(maxDate)%>" title="You can only book facility after today date." required=""/>
                            <%
                                if (selectedFacility.getDescription().split("@").length > 1) { %>
                            <label style="font-size: 20px; margin-left: 20%;"><strong>Court</strong></label>
                            <select style="font-size: 20px; margin-left: 1%;" class="courtDropdwn" name="fid">
                                <%
                                    for (int k = 0; k < facilityCourtList.size(); k++) {
                                        String selected = "";

                                        if (facilityCourtList.get(k).getCategory().equals(selectedFacility.getCategory())) {
                                            if ((facilityCourtList.get(k).getDescription().split("@")[1]).equals(selectedFacility.getDescription().split("@")[1])) {
                                                selected = "selected";
                                            } else {
                                                selected = "";
                                            }
                                %>
                                <option <%=selected%> value="<%=encrypt.UNEncode(facilityCourtList.get(k).getFacilityID())%>"><%=facilityCourtList.get(k).getDescription().split("@")[1]%></option>
                                <%  }
                                    }
                                %>
                            </select>
                            <%  facilityType = "Court";
                            } else {%>                           
                            <%  facilityType = "Simple";                          
                                if(facilityCourtList.size()>0){ %>
                                <label style="font-size: 20px; margin-left: 20%;"><strong>Area</strong></label>
                                    <select style="font-size: 20px; margin-left: 1%;" class="courtDropdwn" name="fid">
                                <%
                                    for (int k = 0; k < facilityCourtList.size(); k++) {
                                        String selected = "";
                                        if (facilityCourtList.get(k).getCategory().equals(selectedFacility.getCategory())) {
                                            if ((facilityCourtList.get(k).getFacilityID()).equals(selectedFacility.getFacilityID())) {
                                                selected = "selected";
                                            } else {
                                                selected = "";
                                            }
                                %>
                                <option <%=selected%> value="<%=encrypt.UNEncode(facilityCourtList.get(k).getFacilityID())%>"><%=facilityCourtList.get(k).getHostelID().getBuilding()%> - <%=facilityCourtList.get(k).getFacilityID()%></option>
                                <%  }
                                    }
                                %> 
                                </select>
                              <%  }else{ %>
                                    <input type="hidden" name="fid" value="<%=encrypt.UNEncode(selectedFacility.getFacilityID())%>">
                              <%  }
                                
                            
                                }%>
                            <button type="submit">Search</button>                            
                        </form>
                    </div>
                    <%
                        String facilityname = "";
                        if (selectedFacility.getDescription().split("@").length > 1) {
                            facilityname = selectedFacility.getDescription().split("@")[1];
                        } else {
                            facilityname = selectedFacility.getDescription();
                        }
                    %>

                    <div style="height: 100px">
                        <div class="cbContent facilitiesList" style="overflow: auto; display: inline-block;">
                            <center><h2 style="margin-top: 20px; padding-top: 20px;">Timetable for <%=facilityname%>  on <%=formatDate.format(curDate)%></h2></center>
                            <table id="timetable">
                                <tbody>
                                    <tr>
                                        <th style="width:10%;">Day/Time</th>
                                            <%
                                                Date startDate = selectedFacility.getStartHour();
                                                Date endDate = selectedFacility.getEndHour();
                                                String startDateStr = formatTime.format(startDate);
                                                String endDateStr = formatTime.format(endDate);
                                                int startHrs = Integer.parseInt(startDateStr.substring(0, 2));
                                                int endHrs = Integer.parseInt(endDateStr.substring(0, 2));
                                                if (endHrs < startHrs) {
                                                    endHrs += 24;
                                                }

                                                Date tempoDate = startDate;
                                                int operatingHrs = (endHrs - startHrs);

                                                int k = 0;
                                                for (int i = 0; i < operatingHrs; i++) {
                                                    int hours = tempoDate.getHours() + k;
                                                    if (hours >= 24) {
                                                        hours -= 24;
                                                    }
                                            %>                           
                                        <th colspan="2"><%=String.format("%02d", hours)%><%=String.format("%02d", tempoDate.getMinutes())%></th>   
                                            <% k += 1;
                                                }%>                                                      
                                    </tr>                       
                                    <tr>
                                        <td style="cursor:default"><%=simpleDateFormat.format(curDate)%></td>
                                        <%
                                            int day = curDate.getDay();
                                            String schedule = new String();

                                            if (day == 6 || day == 0) {
                                                schedule = "Weekends";
                                            } else {
                                                schedule = "Weekdays";
                                            }

                                            if (schedule.equals(selectedFacility.getOperatingDay()) || selectedFacility.getOperatingDay().equals("All days")) {
                                                int h = 0;
                                                int occupiedCnt = 0;
                                                String slotStatus = "";
                                                String title = "";
                                                for (int i = 0; i < operatingHrs * 2; i++) {

                                                    Date tempoTimeslot = curDate;
                                                    int tempoHrs = tempoDate.getHours() + h;

                                                    if (tempoHrs >= 24) {
                                                        tempoHrs -= 24;
                                                    }

                                                    tempoTimeslot.setHours(tempoHrs);
                                                    tempoTimeslot.setMinutes(tempoDate.getMinutes());
                                                    String timeslotStr = formatTimeSlot.format(tempoTimeslot);
                                                    String time = formatTime.format(tempoTimeslot);

                                                    if (facilityType.equals("Court")) {
                                                        if (occupiedCnt == 0) {
                                                            title = "";
                                                        }

                                                        for (int z = 0; z < facilityBookingList.size(); z++) {
                                                            if (formatTimeSlot.format(facilityBookingList.get(z).getBookTime()).equals(timeslotStr)) {
                                                                slotStatus = "red";
                                                                title = "Already booked by others.";
                                                                occupiedCnt = 3;
                                                                break;
                                                            }
                                                        }
                                                    }

                                                    if (tempoDate.getMinutes() == 00) {%>
                                        <td <% if (occupiedCnt == 0) {%>title="<%=time%>" onclick="document.location = '/retrieveSelectedFacilityDetails?sid=<%=encrypt.UNEncode(selectedFacility.getFacilityID())%>&selectedDate=<%=formatSelectDate.format(curDate)%>&selectedTime=<%=String.format("%02d", tempoDate.getHours() + h)%>00';" <%} %>   <% if (occupiedCnt > 0) {%> style="background-color:red; cursor: default;" title="<%=title%>"<% occupiedCnt -= 1;
                                                slotStatus = "";
                                            }%> ><%=time.substring(2)%></td>                                        
                                        <%   tempoDate.setMinutes(30);
                                        } else {%>
                                                                         <td  <% if (occupiedCnt == 0) {%>title="<%=time%>" onclick="document.location = '/retrieveSelectedFacilityDetails?sid=<%=encrypt.UNEncode(selectedFacility.getFacilityID())%>&selectedDate=<%=formatSelectDate.format(curDate)%>&selectedTime=<%=String.format("%02d", tempoDate.getHours() + h)%>30';" <%}
                                                                             if (occupiedCnt > 0) {%>style="background-color:red; cursor: default;" title="<%=title%>"<% occupiedCnt -= 1;
                                                                                 slotStatus = "";
                                                                             }%> title="<%=title%>" ><%=time.substring(2)%></td>
                                        <%   h += 1;
                                                tempoDate.setMinutes(00);
                                            }%>                                                  
                                        <%

                                            }
                                        } else {%>
                                        <td colspan="<%=operatingHrs * 2%>" style="cursor:default"><center>The facility are not operate on that day.</center></td>
                                        <% }
                                        %>
                                </tr>
                                </tbody>
                            </table>
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
</html>
