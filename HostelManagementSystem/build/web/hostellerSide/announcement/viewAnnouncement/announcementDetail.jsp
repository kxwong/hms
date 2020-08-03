<%@page import="java.util.Base64"%>
<%@page import="Model.Attachment"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Model.Announcement"%>
<%@page import="Model.Hosteller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../../hostellerSide/source/frame.css">
        <link rel="stylesheet" href="../../../hostellerSide/source/css/announcement/annoucementDetail.css">
        <title>Hostel Management System</title>
    </head>
    <%

        Hosteller hosteller = new Hosteller();
        Announcement selectedAnnouncement = new Announcement();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMMMMMM yyyy");
        List<Attachment> attachmentList = new ArrayList();

        try {
            if (session.getAttribute("curHosteller") == null || session.getAttribute("selectedAnnouncement") == null) {
                throw new Exception("An error has occured, please re-login.");
            } else {
                hosteller = (Hosteller) session.getAttribute("curHosteller");
                selectedAnnouncement = (Announcement) session.getAttribute("selectedAnnouncement");
                attachmentList = selectedAnnouncement.getAttachmentList();
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
                <div class="chTitle"><a href="/retrieveAllAnnouncement">Notice and News</a> > Selected Notice</div>
            </div>
            <div class="cBody">                         
                <div class='cbContent noticeContent'>
                    <h3>Title : <%=selectedAnnouncement.getTitle()%> <br/> Issued on <%=formatDate.format(selectedAnnouncement.getDate())%></h3>    
                    <hr/>
                    <%=selectedAnnouncement.getContent()%>
                    <hr/>
                    <h2>Attachment</h2>
                    <%for (int i = 0; i < attachmentList.size(); i++) {
                            String file_src = attachmentList.get(i).getHeader() + "," + new String(Base64.getEncoder().encode(attachmentList.get(i).getFile()));%>  
                    <a href="<%=file_src%>" download="attachment<%=i + 1%>">
                        <p>Attachment <%=i + 1%></p>
                    </a>
                    <% }%>
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
