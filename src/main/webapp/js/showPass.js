var input1 = document.getElementById("password");
show(input1);
var input2 = document.getElementById("new_password");
show(input2);
var input3 = document.getElementById("confirm_password");
show(input3);

function show(input) {
    if (input.getAttribute('type') === 'password') {
        input.removeAttribute('type');
        input.setAttribute('type', 'text');
    }
}