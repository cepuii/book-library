function createNewElement() {
    // First create a DIV element.
    var txtNewInputBox = document.createElement('div');
    txtNewInputBox.className = "form-group mb-2";
    // Then add the content (a new input box) of the element.
    txtNewInputBox.innerHTML = "<input type='text' class='form-control' name='newAuthor' required>";

    // Finally put it where it is supposed to appear.
    document.getElementById("new-providers").appendChild(txtNewInputBox);
}