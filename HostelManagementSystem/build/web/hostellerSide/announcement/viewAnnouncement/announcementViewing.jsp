<%@page import="Controller.Crypto"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Model.Announcement"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="Model.Hosteller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/announcement/annoucementViewing.css">
        <title>Hostel Management System</title>
    </head>

    <%
        Hosteller hosteller = new Hosteller();
        List<Announcement> announcementList = new ArrayList();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");

                if (session.getAttribute("announcementList") == null) {
                    throw new Exception("An error has occured, please re-login.");
                } else {
                    announcementList = (List) session.getAttribute("announcementList");
                }
            }
    %>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveAllAnnouncement'><div class="hBG"><div class="hOption hChoose"><p>Announcement</p></div></div></a>
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
                <a href='/retrieveCurrentBillingList'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg"></div></center>Rental</div></div></a>
                <a href='/retrieveAllFacilitiesWithinBranch'><div class="spoFacilities spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Facility</div></div></a>
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveAllAnnouncement'><div class="spoNotice spoBg"><div class="spOption spoChoose"><center><div class="spImg" style="background-image:url(../../../hostellerSide/source/s_notice.png)"></div></center>Notice</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveAllCase'><div class="spoReporting spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Report</div></div></a>            
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Notice and News</div>
            </div>
            <div class="cBody">                         
                <div class='cbContent criteriaSearching'>
                    <table>
                        <tr><td width="10%;">Notice ID</td><td><input type='text' id="idSearchBox" onkeyup="idFilter()"/></td><td>Date</td><td><input id="dateSelection" onchange="filterTable()" type='date'/></td></tr>
                        <tr><td>Title</td><td colspan="3"><input id="titleSearchBox" onkeyup="titleFilter()" type='text' width="100%;"/></td></tr>
                    </table>
                </div>

                <div class="cbContent caseHistorytb">
                    <table id="noticeTbl">
                        <tbody>
                            <%  String seq = "desc";
                                if (session.getAttribute("seq") == null) {
                                    seq = "asc";
                                } else if (session.getAttribute("seq").equals("desc")) {
                                    seq = "asc";
                                } else {
                                    seq = "desc";
                                }
                            %> 
                        <thead class="theadA">
                        <th width="10%"><a href="/retrieveAllAnnouncement?ob=<%=encrypt.UNEncode("a.announceID")%>&seq=<%=encrypt.UNEncode(seq)%>">Notice ID</a></th>
                        <th width="75%"><a href="/retrieveAllAnnouncement?ob=<%=encrypt.UNEncode("a.title")%>&seq=<%=encrypt.UNEncode(seq)%>">Title</a></th>
                        <th width="15%"><a href="/retrieveAllAnnouncement?ob=<%=encrypt.UNEncode("a.date")%>&seq=<%=encrypt.UNEncode(seq)%>">Date</a></th>
                        </thead>
                        <%
                            if (announcementList.size() < 1) { %>
                        <tr><td colspan="3"><center>No announcement available.</center></td></tr>
                            <% } else {
                                for (int i = 0; i < announcementList.size(); i++) {%>
                        <tr>
                            <td><center><%=announcementList.get(i).getAnnounceID()%></center></td><td><center><a href="/retrieveSelectedAnnouncement?aid=<%=encrypt.UNEncode(announcementList.get(i).getAnnounceID())%>"><%=announcementList.get(i).getTitle()%></a></center></td><td><center><%=dateFormat.format(announcementList.get(i).getDate())%></center></td>
                        </tr>
                        <%  }
                            }%>
                        </tbody>
                    </table>                            
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
        function titleFilter() {
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("titleSearchBox");
            filter = input.value;
            table = document.getElementById("noticeTbl");
            tr = table.getElementsByTagName("tr");
            filter = filter.toUpperCase();
            for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByTagName("td")[1];
                if (td) {
                    txtValue = td.textContent || td.innerText;
                    txtValue = txtValue.toUpperCase();
                    if (txtValue.indexOf(filter) > -1) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
        }

        function idFilter() {
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("idSearchBox");
            filter = input.value;
            table = document.getElementById("noticeTbl");
            tr = table.getElementsByTagName("tr");
            filter = filter.toUpperCase();
            for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByTagName("td")[0];
                if (td) {
                    txtValue = td.textContent || td.innerText;
                    txtValue = txtValue.toUpperCase();
                    if (txtValue.indexOf(filter) > -1) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
        }

        function filterTable() {
            var table, rows, cells, tableBookDateValue;
            var postDate = document.getElementById("dateSelection");

            table = document.getElementById("noticeTbl");
            rows = table.getElementsByTagName("tr");
            var postDateValue = postDate.value;

            if (!(postDateValue === "")) {
                postDateValue = postDateValue.substring(8, 10) + "/" + postDateValue.substring(5, 7) + "/" + postDateValue.substring(0, 4);
            } else {
                postDateValue = "";
            }

            for (var i = 0; i < rows.length; i++) {
                cells = rows[i].getElementsByTagName("td");
                tableBookDateValue = cells[2] || null;
                
                if (!tableBookDateValue) {
                    rows[i].style.display = "";
                } else if (postDateValue === "") {
                    rows[i].style.display = "";
                } else if(tableBookDateValue.innerText === postDateValue || tableBookDateValue.textContent === postDateValue){
                    rows[i].style.display = "";
                }else{
                    rows[i].style.display = "none";
                }
            }
        }
    </script>


</script> 

</html>
