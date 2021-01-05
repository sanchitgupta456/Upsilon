
var submit = document.getElementById("submitButton");
var title = document.getElementById("title");

submit.addEventListener("click", changePassword);

function changePassword(){
    console.log("Submit pressed!");
    //resetPwd();
    //window.location = "Upsilon://";

    const params = new URLSearchParams(window.location.search);
    const token = params.get('token');
    const tokenId = params.get('tokenId');

    console.log(token);
    console.log(tokenId);

    return false;
}
