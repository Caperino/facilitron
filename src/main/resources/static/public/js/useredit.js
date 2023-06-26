"use strict";

let enabler = document.getElementById("enablePWChange");
let pwInput = document.getElementById("password");
let submitBtn = document.getElementById("submit-btn");
let forbiddenRegex = new RegExp("^(?=.*[\"\`\´\'<>=()|{}^° ])");
let strongRegex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?!.*[\"\`\´\'<>=()|{}]).{6,20}$");

function togglePWChange(){
    if (pwInput.disabled){
        pwInput.disabled = false;
        pwInput.value = "";
        enabler.textContent = "keep password";
    } else {
        pwInput.disabled = true;
        pwInput.value = "_UNCHANGED_";
        enabler.textContent = "use new password";
    }
    checkPWComplexity(pwInput);
}

function checkInput(sender){
    if (forbiddenRegex.test(sender.value)){
        sender.value = sender.value.substring(0, sender.value.length-1);
    }
}

function checkPWComplexity(sender){
    if (forbiddenRegex.test(sender.value) || (!strongRegex.test(sender.value) && sender.value !== "_UNCHANGED_")) {
        sender.style.border = "3px dashed red";
        console.log("not secure enough");
        submitBtn.disabled = true;
    }
    else {
        sender.style.border = "3px solid green";
        submitBtn.disabled = false;
    }
}

enabler.addEventListener("click", togglePWChange);
checkPWComplexity(pwInput);