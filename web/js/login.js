/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
onload = function() {
    
    //button handler toevoegen
    $("#login").click(function() {
        
    
        var credentials = {};
        credentials.username = $("#username").val();
        credentials.password = $("#password").val();
        
        var request = new XMLHttpRequest();
        request.open("POST", "https://localhost:8181/messages/api/credentials");
        request.onload = function(){
          if(request.status === 200) {
            sessionStorage.setItem("username", credentials.username);
            sessionStorage.setItem("password", credentials.password);
            location = "index.html";
            alert("amaai");
              $("#errorMessage").html(request.reponseText);
          }  else {
              $("#username").empty();
              $("#password").empty();
              alert("dat is toch niet serieus");
              $("#errorMessage").html(request.reponseText);
          }
        };
        request.setRequestHeader("Content-Type", "application/json");
        request.send(JSON.stringify(credentials));
    });
    
};

