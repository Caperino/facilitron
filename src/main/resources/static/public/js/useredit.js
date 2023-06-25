"use strict";

let enabler = document.getElementById("enablePWChange");
let pwInput = document.getElementById("password");

function togglePWChange(){
    if (pwInput.disabled){
        pwInput.disabled = false;
        pwInput.value = "";
    } else {
        pwInput.disabled = true;
        pwInput.value = "_UNCHANGED_";
    }
}

enabler.addEventListener("click", togglePWChange);
pwInput.disabled = true;