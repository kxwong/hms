<%@page import="Controller.Crypto"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Model.Issue"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="Model.Hosteller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/reporting/caseSearching.css">
        <title>Hostel Management System</title>
    </head>
    <%
        Hosteller hosteller = new Hosteller();
        List<Issue> issueList = new ArrayList();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {

                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("issueList") == null) {
                    throw new Exception("An error has occured, please re-login.");
                } else {
                    issueList = (List) session.getAttribute("issueList");
                }
            }
    %>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveAllCase'><div class="hBG"><div class="hOption hChoose"><p>Case Searching</p></div></div></a> 
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
                <a href='/hosteller/reporting/makeReport'><div class="hBG"><div class="hOption"><p>Making Report</p></div></div></a>            
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
                <div class="chTitle">Case Searching</div>
            </div>
            <div class="cBody">                         
                <div class='cbContent criteriaSearching'>

                    <table>
                        <tr><td>Title</td><td><input type='text' id="titleSearchBox" onkeyup="titleFilter()"/></td></tr>
                        <tr><td>Issue Date</td><td><input type='date' id="issueDate" onchange="filterTable()"/></td><td>Last Update Date</td><td><input type='date' id="updateDate" onchange="filterTable()"/></td></tr>
                        <tr>
                            <td>Category</td>
                            <td width="35%">                           
                                <select class="customDropdown" onchange="changeIssueType();filterTable()" required="1" id="categoryFilter">
                                    <option value="Suspicious Activity">Suspicious Activity</option>
                                    <option value="Facilities Problem">Facilities Problem</option>
                                    <option value="System Issue">System Issue</option>
                                    <option value="General Issue">General Issue</option>
                                    <option value="All" selected="">All</option>
                                </select>
                            </td>
                            <td>Type of Issue</td>
                            <td width="35%">                            
                                <select class="customDropdown" onchange="filterTable()" required="1" id="typeFilter">
                                    <option value="Complaint" selected="">Complaint</option>
                                    <option value="Report">Report</option>
                                    <option value="Questions">Questions</option>
                                    <option value="Other">Other</option>
                                    <option value="All">All</option>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="cbContent caseHistorytb">
                    <p style="font-size: 20px; font-weight: bold;">All previous closed case are shown here.</p>
                    <table id="caseTable">
                        <tbody>
                            <%  String seq = "asc";
                                if (session.getAttribute("seq") == null) {
                                    seq = "asc";
                                } else if (session.getAttribute("seq").equals("desc")) {
                                    seq = "asc";
                                } else {
                                    seq = "desc";
                                }
                            %>
                        <thead class="theadA">
                        <th width="10%"><a href="/retrieveAllCase?ob=<%=encrypt.UNEncode("i.caseNo")%>&seq=<%=encrypt.UNEncode(seq)%>">Case Number</a></th>
                        <th width="30%"><a href="/retrieveAllCase?ob=<%=encrypt.UNEncode("i.title")%>&seq=<%=encrypt.UNEncode(seq)%>">Title</a></th>
                        <th><a href="/retrieveAllCase?ob=<%=encrypt.UNEncode("i.issueDate")%>&seq=<%=encrypt.UNEncode(seq)%>">Issue Date</a></th>
                        <th><a href="/retrieveAllCase?ob=<%=encrypt.UNEncode("i.updateDate")%>&seq=<%=encrypt.UNEncode(seq)%>">Last Update Date</a></th>
                        <th><a href="/retrieveAllCase?ob=<%=encrypt.UNEncode("i.category")%>&seq=<%=encrypt.UNEncode(seq)%>">Category</a></th>
                        <th><a href="/retrieveAllCase?ob=<%=encrypt.UNEncode("i.issueType")%>&seq=<%=encrypt.UNEncode(seq)%>">Type</a></th>
                        </thead>
                        <%
                            if (issueList.size() < 1) { %>
                        <tr><td colspan="6"><center>No issue available at the moment.</center></td></tr>
                            <% } else {
                                for (int i = 0; i < issueList.size(); i++) {%>
                        <tr>
                            <td><center><a href="/retrieveSelectedCase?iid=<%=encrypt.UNEncode(issueList.get(i).getCaseNo())%>"><%=issueList.get(i).getCaseNo()%></a></center></td>
                        <td><center><%=issueList.get(i).getTitle()%></center></td>
                        <td><center><%=dateFormat.format(issueList.get(i).getIssueDate())%></center></td>
                        <td><center><%=dateFormat.format(issueList.get(i).getUpdateDate())%></center></td>
                        <td><center><%=issueList.get(i).getCategory()%></center></td>
                        <td><center><%=issueList.get(i).getIssueType()%></center></td>
                        </tr>
                        <% }
                            }%>
                        </tbody>
                    </table>   
                    <center class="labelTxt"><strong>Finding solution ? Why not <a href="/hosteller/reporting/makeReport">create a new one </a>?</strong></center>
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
            filter = filter.toUpperCase();
            table = document.getElementById("caseTable");
            tr = table.getElementsByTagName("tr");
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

        function filterTable() {
            var table, rows, cells, tableTypeValue, tableCategoryValue, tableIssueDateValue, tableUpdateDateValue;
            var typeDropdown = document.getElementById("typeFilter");
            var categoryDropdown = document.getElementById("categoryFilter");
            var issueDate = document.getElementById("issueDate");
            var updateDate = document.getElementById("updateDate");

            table = document.getElementById("caseTable");
            rows = table.getElementsByTagName("tr");
            var typeValue = typeDropdown.value;
            var categoryValue = categoryDropdown.value;
            var issueDateValue = issueDate.value;
            var updateDateValue = updateDate.value;

            if (!(issueDateValue === "")) {
                issueDateValue = issueDateValue.substring(8, 10) + "/" + issueDateValue.substring(5, 7) + "/" + issueDateValue.substring(0, 4);
            }

            if (!(updateDateValue === "")) {
                updateDateValue = updateDateValue.substring(8, 10) + "/" + updateDateValue.substring(5, 7) + "/" + updateDateValue.substring(0, 4);
                ;
            }

            for (var i = 0; i < rows.length; i++) {
                cells = rows[i].getElementsByTagName("td");
                tableTypeValue = cells[5] || null;
                tableCategoryValue = cells[4] || null;
                tableIssueDateValue = cells[2] || null;
                tableUpdateDateValue = cells[3] || null;

                if (!tableTypeValue || !tableCategoryValue || !tableIssueDateValue || !tableUpdateDateValue) {
                    rows[i].style.display = "";
                } else if (issueDateValue === "" && updateDateValue === "") {
                    if (typeValue === "All" && categoryValue === tableCategoryValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && typeValue === tableTypeValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && typeValue === "All") {
                        rows[i].style.display = "";
                    } else if (categoryValue === tableCategoryValue.textContent && typeValue === tableTypeValue.textContent) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                } else if (issueDateValue === "" && !(updateDateValue === "")) {
                    if (typeValue === "All" && categoryValue === tableCategoryValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && typeValue === tableTypeValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && typeValue === "All" && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === tableCategoryValue.textContent && typeValue === tableTypeValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                } else if (updateDateValue === "" && !(issueDateValue === "")) {
                    if (typeValue === "All" && categoryValue === tableCategoryValue.textContent && issueDateValue === tableIssueDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && typeValue === tableTypeValue.textContent && issueDateValue === tableIssueDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && typeValue === "All" && issueDateValue === tableIssueDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === tableCategoryValue.textContent && typeValue === tableTypeValue.textContent && issueDateValue === tableIssueDateValue.textContent) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                } else if (!(issueDateValue === "") && !(updateDateValue === "")) {
                    if (typeValue === "All" && categoryValue === tableCategoryValue.textContent && issueDateValue === tableIssueDateValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && typeValue === tableTypeValue.textContent && issueDateValue === tableIssueDateValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && typeValue === "All" && issueDateValue === tableIssueDateValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === tableCategoryValue.textContent && typeValue === tableTypeValue.textContent && issueDateValue === tableIssueDateValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                } else {
                    rows[i].style.display = "none";
                }
            }
        }

        window.onload(changeIssueType())

        function changeIssueType() {

            var category = document.getElementById("categoryFilter");
            var categoryValue = category.options[category.selectedIndex].value;
            var dropdown = document.getElementById("typeFilter");

            var option0 = dropdown.options[0];
            var option1 = dropdown.options[1];
            var option2 = dropdown.options[2];
            var option3 = dropdown.options[3];
            var option4 = dropdown.options[4];
            
            option0.style.display = "";
            option1.style.display = "";
            option2.style.display = "";
            option3.style.display = "";
            option4.style.display = "";        

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
                    option4.innerHTML = "All";
                    option4.value = "All";
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
                    option4.innerHTML = "All";
                    option4.value = "All";
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
                    option4.innerHTML = "All";
                    option4.value = "All";
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
                    option4.innerHTML = "All";
                    option4.value = "All";
                    break;
                case "All":
                    option0.innerHTML = "All";
                    option0.value = "All";
                    option0.attribute = "Selected";
                    option1.innerHTML = "";
                    option1.value = "";
                    option1.style.display = "none";
                    option2.innerHTML = "";
                    option2.value = "";
                    option2.style.display = "none";
                    option3.innerHTML = "";
                    option3.value = "";
                    option3.style.display = "none";
                    option4.innerHTML = "";
                    option4.value = "";
                    option4.style.display = "none";
            }

        }
    </script>
</html>
