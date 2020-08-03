
<%@page import="java.util.Base64"%>
<%@page import="Model.Attachment"%>
<%@page import="Model.Announcement"%>
<%@page import="Model.Account"%>
<%@page import="Model.AccountManager"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="Controller.Crypto"%>
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
        Announcement notice = (Announcement) session.getAttribute("notice");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../../managementSide/source/css/frame.css">
        <link rel="stylesheet" href="../../managementSide/source/css/makeAnnouncement.css">
        <title>Hostel Management System</title>
        <script src="https://cdn.ckeditor.com/ckeditor5/12.3.1/classic/ckeditor.js"></script>
    </head>
    <body>
        <div class="header">
            <div class="hCover">
                <div class="hSpaceF"></div>
                <div class="hSubcover"><div class="hSpaceLeft"></div></div>
                <a href='/retrieveNoticeListing'>
                    <div class="hBG">
                        <div class="hOption hChoose">
                            <p>Notice Management</p>
                        </div>
                    </div>
                </a>
                <div class="hSubcover"><div class="hSpaceRight"></div></div>
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
                <a href='/retrieveFloorplanListing'><div class="spoRoom spoBg"><div class="spOption "><center><div class="spImg " ></div><p>Room</p></center></div></div></a>
                <a href='/retrieveRegistrationListing?t=0'><div class="spoTenant spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Tenant</div></div></a>
                <a href='/retrieveBillListing?t=0'><div class="spoRental spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Rental</div></div></a>
                <a href='/retrieveFacilityListing'><div class="spoFacilities spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Facility</div></div></a>
                <div class="spSubcover"><div class="spSpaceUp"></div></div>
                <a href='/retrieveNoticeListing'><div class="spoNotice spoBg"><div class="spOption spoChoose"><center><div class="spImg"  style="background-image:url(../../managementSide/source/s_notice.png)"></div></center>Notice</div></div></a>
                <div class="spSubcover"><div class="spSpaceDn"></div></div>
                <a href='/retrieveIssueListing?t=0'><div class="spoIssue spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Issue</div></div></a>
                <a href='/retrieveTenantEntryListing?t=0'><div class="spoEntry spoBg"><div class="spOption"><center><div class="spImg" ></div></center>Entry</div></div></a>
            </div>
        </div>
        <div class="content">
            <div class="cHeader">
                <div class="chTitle">Announcement's Overview &#10148; Edit Announcement</div>
            </div>
            <div class="cBody">
                <div class="cbContent">
                    <form action="/updateNotice" method="post" id="noticeForm">
                        <div class="cbLabel">Title : <input class="cbInput" name="title" type="text" form="noticeForm" maxlength="100" value="<%=notice.getTitle()%>"></div>
                        <br />
                        <div class="cbLabel">Content</div>
                        <div class="cbInsertContent">
                            <textarea name="content" id="editor" form="noticeForm"><%=notice.getContent()%></textarea>
                        </div>
                        <div class="cbLabel">Attachment <button type="button" class="cbAddAtt" onclick="addAttachment()">Add Attachment</button></div>
                        <%
                            for (int j = 0; j < notice.getAttachmentList().size(); j++) {
                                Attachment attachment = notice.getAttachmentList().get(j);
                                String attString = attachment.getHeader() + "," + new String(Base64.getEncoder().encode(attachment.getFile()));
                        %>
                        <div class="addFileArea" id="addFileArea<%=j + 1%>">
                            <a href="<%=attString%>" download>
                                &#128206; File <%=j + 1%>
                            </a>
                                <input type='text' name='fileByte' id='byte_content<%=j + 1%>' value="<%=attString%>" hidden>
                            <br>
                        </div>
                        <%
                            }
                            for (int j = notice.getAttachmentList().size(); j <= 5; j++) {
                        %>
                        <div class="addFileArea" id="addFileArea<%=j + 1%>"></div>
                        <%
                            }
                        %>
                        <a id="removeAtt" href='javascript:removeFileAdd()'>&#10060;<b>Remove</b></a>
                        <div class="cbContent">
                            <a href="/retrieveNoticeDetails?p=<%=crypto.ANEncode(notice.getAnnounceID())%>"><button type="button" class="cbAdd">&#9166; Back</button></a>
                            <button class="cbAdd cBAddright">Edit</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
    <script type="text/javascript">
        window.onload = showMsg;
        var fileID = <%=notice.getAttachmentList().size()%>;
        function addAttachment() {
            if (fileID > 4) {
                alert("Fail to Add. Only up to 5 attachment is available for each announcement");
            } else {
                fileID++;
                var x = document.getElementById('addFileArea' + fileID);
                x.innerHTML = "<input type='file' id='files' onchange='fileValidation(this, event," + fileID + ")' required><input type='text' name='fileByte' id='byte_content" + fileID + "' hidden><br>";
            }
        }
        function removeFileAdd() {
            var x = document.getElementById('addFileArea' + fileID);
            x.innerHTML = "";
            fileID--;
        }
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
        function fileValidation(oInput, event, count) {
            if (oInput.type == "file") {
                var sFileName = oInput.value;
                var file = oInput.files[0];
                if (sFileName.length > 0) {
                    if (file.size > 1070000) {
                        alert("Sorry, only image file not exceed 1 mb is allowed");
                        oInput.value = "";
                        return false;
                    }
                }
                storeFile(event, count);
            }
            return true;
        }
        function storeFile(input, count) {
            var reader = new FileReader();
            reader.onload = function ()
            {
                document.getElementById('byte_content'.concat(count)).value = reader.result;
            }
            reader.readAsDataURL(input.target.files[0]);
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
<script>
    ClassicEditor
            .create(document.querySelector('#editor'))
            .catch(error => {
                console.error(error);
            });
</script>