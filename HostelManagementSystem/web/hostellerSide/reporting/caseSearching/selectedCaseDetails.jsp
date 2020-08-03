<%@page import="Model.Conversation"%>
<%@page import="java.util.Base64"%>
<%@page import="Model.Attachment"%>
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
        <link rel="stylesheet" href="../../../hostellerSide/source/css/reporting/selectedCaseDetails.css">
        <title>Hostel Management System</title>
    </head>
    <%
        Hosteller hosteller = new Hosteller();
        Issue curIssue = new Issue();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMMMMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMMMMMM yyyy HH:mm aaa");
        Crypto encrypt = new Crypto();

        try {
            if (session.getAttribute("curHosteller") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {

                hosteller = (Hosteller) session.getAttribute("curHosteller");
                if (session.getAttribute("selectedIssue") == null) {
                    throw new Exception("An error has occured, please re-login.");
                } else {
                    curIssue = (Issue) session.getAttribute("selectedIssue");
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
                <a href='/hosteller/reporting/makeReport'><div class="hBG"><div class="hOption "><p>Making Report</p></div></div></a>
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
                <div class="chTitle">Case Details</div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <div class="reportCriteria">
                        <table>
                            <tbody>
                                <tr>
                                    <td width="50%"><p>Case ID : <%=curIssue.getCaseNo()%></p></td>
                                    <td><p>Closed on: <%=dateFormat.format(curIssue.getUpdateDate())%></p></td>
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
                                    <td colspan="2">
                                        <p>Title : <%=curIssue.getTitle()%></p>
                                    </td>
                                </tr> 
                                <tr><td><strong>Additional Attachment</strong></td></tr>
                                <tr>
                                    <td colspan="2">
                                        <%
                                            if (curIssue.getAttachmentList() != null) {
                                                List<Attachment> attachmentList = curIssue.getAttachmentList();
                                                for (int i = 0; i < attachmentList.size(); i++) {
                                                    String file_src = attachmentList.get(i).getHeader() + "," + new String(Base64.getEncoder().encode(attachmentList.get(i).getFile()));%>
                                        <a href="<%=file_src%>" download="attachment<%=i + 1%>">Attachment <%=i + 1%></a>
                                        <% }  %>
                                    </td>
                                </tr> 
                                <% } else { %>
                                <tr><td colspan="2">No file is attached</td></tr>
                                <%} %>
                                </td> 
                            </tbody>
                        </table>
                    </div>

                    <center><h2 class="discussionTitle">Discussion</h2></center>
                    <hr style="width: 98%;"/>
                    <div class="discussionDiv">
                        <table>
                            <th></th>
                                <%
                                    List<Conversation> conversationList = new ArrayList();
                                    conversationList = curIssue.getConversationList();

                                    for (int i = 0; i < conversationList.size(); i++) {
                                        if (conversationList.get(i).getReplyBy().equals(hosteller.getUsername())) {
                                %>
                            <tr><td><strong style="font-size: 18px;"><%=encrypt.UNDecode(conversationList.get(i).getReplyBy().getUsername())%> on <%=timeFormat.format(conversationList.get(i).getTime())%></strong></td></tr> 
                            <%  } else {%>
                            <tr><td><strong style="font-size: 18px;"><%=encrypt.UNDecode(conversationList.get(i).getReplyBy().getUsername())%> on <%=timeFormat.format(conversationList.get(i).getTime())%></strong></td></tr>
                            <%  }%>                            
                            <tr>
                                <td>
                                    <p><%=conversationList.get(i).getContent()%></p>
                                </td>
                            </tr>                            
                            <% }%>                             
                        </table>           
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
