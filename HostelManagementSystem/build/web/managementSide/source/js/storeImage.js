function previewImage(input) {
    var reader = new FileReader();
    reader.onload = function ()
    {
        var output = document.getElementById('preview');
        output.src = reader.result;
        document.getElementById('byte_content').value = reader.result;
    }
    reader.readAsDataURL(input.target.files[0]);
}
var _validFileExtensions = [".jpg", ".jpeg", ".bmp", ".gif", ".png"];
function fileValidation(oInput, event) {
    if (oInput.type == "file") {
        var sFileName = oInput.value;
        var inputfile = document.getElementById('files');
        var file = inputfile.files[0];
        var output = document.getElementById('preview');
        if (sFileName.length > 0) {
            if (file.size > 1070000) {
                alert("Sorry, only image file not exceed 1 mb is allowed");
                oInput.value = "";
                output.src = "";
                return false;
            }
            var blnValid = false;
            for (var j = 0; j < _validFileExtensions.length; j++) {
                var sCurExtension = _validFileExtensions[j];
                if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                    blnValid = true;
                    previewImage(event);
                    break;
                }
            }
            if (!blnValid) {
                alert("Sorry, only image file with extensions " + _validFileExtensions.join(", ") + " is allowed");
                oInput.value = "";
                output.src = "";
                return false;
            }
        }
    }
    return true;
}