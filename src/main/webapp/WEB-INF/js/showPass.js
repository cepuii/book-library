var input1 = document.getElementById("password");
var input3 = document.getElementById("confirm_password");
var input2 = document.getElementById("new_password");


function show() {
    if (input1.getAttribute('type') === 'password') {
        input1.removeAttribute('type');
        input1.setAttribute('type', 'text');
    } else {
        input1.removeAttribute('type');
        input1.setAttribute('type', 'password');
    }
    if (input3.getAttribute('type') === 'password') {
        input3.removeAttribute('type');
        input3.setAttribute('type', 'text');
    } else {
        input3.removeAttribute('type');
        input3.setAttribute('type', 'password');
    }

    if (input2.getAttribute('type') === 'password') {
        input2.removeAttribute('type');
        input2.setAttribute('type', 'text');
    } else {
        input2.removeAttribute('type');
        input2.setAttribute('type', 'password');
    }
}