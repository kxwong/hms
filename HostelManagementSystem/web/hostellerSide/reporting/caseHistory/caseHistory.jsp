<%@page import="Controller.Crypto"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Model.Issue"%>
<%@page import="java.util.List"%>
<%@page import="Model.Hosteller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/reporting/caseHistory.css">
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
        List<Issue> issueList = new ArrayList();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String issueSuccess = "false";
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {

                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("selfIssueList") == null) {
                    throw new Exception("An error has occured, please re-login.");
                } else {
                    issueList = (List) session.getAttribute("selfIssueList");
                }
            }

            if (session.getAttribute("issueSuccess") != null) {
                issueSuccess = (String) session.getAttribute("issueSuccess");
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
                <div class="chTitle">Personal Case Records</div>
            </div>
            <div class="cBody"> 
                <%if (session.getAttribute("successMsg")!=null) { %>
                <center>
                    <div onclick="closeSuccess()" class="popup" id="successPopUp">
                        <div class="cover">
                            <img class="popUpicon" src="../../../hostellerSide/source/success.png"/>
                            <div class="message"><%=String.valueOf(session.getAttribute("successMsg")).toString()%></div>
                        </div>
                    </div>
                </center>
                <%  session.removeAttribute("successMsg");
                        }%>
                <%if (session.getAttribute("errMsg")!=null) { %>
                <center>
                    <div onclick="closeSuccess()" class="popup" id="failPopUp">
                        <div class="cover">
                            <img class="popUpicon" src="../../../hostellerSide/source/failed.png"/>
                            <div class="message"><%=String.valueOf(session.getAttribute("errMsg")).toString()%></div>
                        </div>
                    </div>
                </center>
                <%  session.removeAttribute("errMsg");
                        }%>                        
                        
                <div class='cbContent criteriaSearching'>
                    <p>All your cases is shown below.</p>                      
                </div>
                <div class='cbContent criteriaSearching'>
                    <table>
                        <tr><td>Title</td><td><input type='text' id="titleSearchBox" onkeyup="titleFilter()"/></td></tr>
                        <tr><td>Issue Date</td><td><input type='date' id="issueDate" onchange="filterTable()"/></td><td>Last Update Date</td><td><input type='date' id="updateDate" onchange="filterTable()"/></td></tr>
                        <tr>
                            <td width="15%">Category</td>
                            <td width="35%">                           
                                <select class="customDropdown" onchange="filterTable()" required="1" id="categoryFilter">
                                    <option value="Suspicious Activity">Suspicious Activity</option>
                                    <option value="Facilities Problem">Facilities Problem</option>
                                    <option value="System Issue">System Issue</option>
                                    <option value="General Issue">General Issue</option>
                                    <option value="All" selected="">All</option>
                                </select>
                            </td>
                            <td width="15%">Status</td>
                            <td width="35%">                            
                                <select class="customDropdown" onchange="filterTable()" required="1" id="statusFilter">
                                    <option value="Processing" >Processing</option>
                                    <option value="Closed">Closed</option>
                                    <option value="All" selected="">All</option>
                                </select>
                            </td>

                        </tr>
                    </table>
                </div>
                <div class="cbContent caseHistorytb">
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
                        <th width="10%"><a href="/retrieveAllSelfCase?ob=<%=encrypt.UNEncode("i.caseNo")%>&seq=<%=encrypt.UNEncode(seq)%>">Case Number</a></th>
                        <th><a href="/retrieveAllSelfCase?ob=<%=encrypt.UNEncode("i.title")%>&seq=<%=encrypt.UNEncode(seq)%>">Title</a></th>
                        <th><a href="/retrieveAllSelfCase?ob=<%=encrypt.UNEncode("i.issueDate")%>&seq=<%=encrypt.UNEncode(seq)%>">Issue Date</a></th>
                        <th><a href="/retrieveAllSelfCase?ob=<%=encrypt.UNEncode("i.updateDate")%>&seq=<%=encrypt.UNEncode(seq)%>">Last Update Date</a></th>
                        <th><a href="/retrieveAllSelfCase?ob=<%=encrypt.UNEncode("i.category")%>&seq=<%=encrypt.UNEncode(seq)%>">Category</a></th>
                        <th><a href="/retrieveAllSelfCase?ob=<%=encrypt.UNEncode("i.issueType")%>&seq=<%=encrypt.UNEncode(seq)%>">Type</a></th>
                        <th><a href="/retrieveAllSelfCase?ob=<%=encrypt.UNEncode("i.status")%>&seq=<%=encrypt.UNEncode(seq)%>">Status</a></th>
                        </thead>
                        <%if (issueList.size() < 1) { %>
                        <tr><td colspan="6">You have not made any case  report before.</td></tr>
                        <% } else {%>
                        <%for (int i = 0; i < issueList.size(); i++) {%>
                        <tr><td><a href="/retrieveCaseDetails?cid=<%=encrypt.UNEncode(issueList.get(i).getCaseNo())%>"><%=issueList.get(i).getCaseNo()%></a></td><td><%=issueList.get(i).getTitle()%></td><td><%=dateFormat.format(issueList.get(i).getIssueDate())%></td><td><%=dateFormat.format(issueList.get(i).getUpdateDate())%></td><td><%=issueList.get(i).getCategory()%></td><td><%=issueList.get(i).getIssueType()%></td><td><%=issueList.get(i).getStatus()%></td></tr>
                                <% }
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
            var table, rows, cells, tableStatusValue, tableCategoryValue, tableIssueDateValue, tableUpdateDateValue;
            var statusDropdown = document.getElementById("statusFilter");
            var categoryDropdown = document.getElementById("categoryFilter");
            var issueDate = document.getElementById("issueDate");
            var updateDate = document.getElementById("updateDate");

            table = document.getElementById("caseTable");
            rows = table.getElementsByTagName("tr");
            var statusValue = statusDropdown.value;
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
                tableStatusValue = cells[6] || null;
                tableCategoryValue = cells[4] || null;
                tableIssueDateValue = cells[2] || null;
                tableUpdateDateValue = cells[3] || null;

                if (!tableStatusValue || !tableCategoryValue || !tableIssueDateValue || !tableUpdateDateValue) {
                    rows[i].style.display = "";
                } else if (issueDateValue === "" && updateDateValue === "") {
                    if (statusValue === "All" && categoryValue === tableCategoryValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && statusValue === tableStatusValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && statusValue === "All") {
                        rows[i].style.display = "";
                    } else if (categoryValue === tableCategoryValue.textContent && statusValue === tableStatusValue.textContent) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                } else if (issueDateValue === "" && !(updateDateValue === "")) {
                    if (statusValue === "All" && categoryValue === tableCategoryValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && statusValue === tableStatusValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && statusValue === "All" && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === tableCategoryValue.textContent && statusValue === tableStatusValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                } else if (updateDateValue === "" && !(issueDateValue === "")) {
                    if (statusValue === "All" && categoryValue === tableCategoryValue.textContent && issueDateValue === tableIssueDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && statusValue === tableStatusValue.textContent && issueDateValue === tableIssueDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && statusValue === "All" && issueDateValue === tableIssueDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === tableCategoryValue.textContent && statusValue === tableStatusValue.textContent && issueDateValue === tableIssueDateValue.textContent) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                } else if (!(issueDateValue === "") && !(updateDateValue === "")) {
                    if (statusValue === "All" && categoryValue === tableCategoryValue.textContent && issueDateValue === tableIssueDateValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && statusValue === tableStatusValue.textContent && issueDateValue === tableIssueDateValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === "All" && statusValue === "All" && issueDateValue === tableIssueDateValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else if (categoryValue === tableCategoryValue.textContent && statusValue === tableStatusValue.textContent && issueDateValue === tableIssueDateValue.textContent && updateDateValue === tableUpdateDateValue.textContent) {
                        rows[i].style.display = "";
                    } else {
                        rows[i].style.display = "none";
                    }
                } else {
                    rows[i].style.display = "none";
                }
            }
        }
    </script>
</html>
