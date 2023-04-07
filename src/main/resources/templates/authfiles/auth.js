"use strict";

class LoginRequest {
    constructor(mail, pw) {
        this.mail = mail;
        this.password = pw;
    }

    sendRequest(){
        let xhttp = new XMLHttpRequest();
        xhttp.onload = () => {
            if (xhttp.status === 200){
                //window.location.href = "/content/hello"
            }
        }
        xhttp.open("POST", `/auth/authenticate`, true);
        xhttp.setRequestHeader("Content-type", "application/json");
        let content = JSON.stringify(this);
        xhttp.send(content);
    }
}

const mailInput = document.getElementById("username");
const passwordInput = document.getElementById("pw");

function login(){
    let request = new LoginRequest(mailInput.value, passwordInput.value)
    request.sendRequest()
}

function logout(){
    let xhttp = new XMLHttpRequest()
    xhttp.onload = () => {
        if (xhttp.status === 200){
            window.location.href = "/"
        }
    }

    xhttp.open("POST", '/auth/logout', true);
    xhttp.send()
}