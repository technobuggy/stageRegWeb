/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
onload = function() {
    //username en paswoord opvragen
    var username  = sessionStorage.getItem("username");
    var password = sessionStorage.getItem("password");
    
    if (username == null || password == null){
        location = "login.html";
    }
    //logout knop instellen
    $("#logout").click(function(){
      sessionStorage.removeItem("username");
      sessionStorage.removeItem("password");
      location = "login.html";
    });
    
    var request= new XMLHttpRequest();
    request.open("POST", "https://localhost:8181/messages/api/messages/latest", true,username, password)
    request.onload = function() {
        if (request.status === 200) {
            var message = JSON.parse(request.responseText);
            $("#latestMessage").text(message.text);
        } else {
            console.log("Fout: " + request.status + " - " + request.responseText);
        }
    };
  //latestmessage invullen met /messages/latest  
  request.send(null);
};


