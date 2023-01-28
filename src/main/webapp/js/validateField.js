function clearClass(fieldName) {
    console.log('onclick run')
    $('#' + fieldName).removeClass('is-valid');
    $('#' + fieldName).removeClass('is-invalid');
}


function validate(fieldName) { // When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
    var value = ('#' + fieldName);
    console.log(value);
    if ($(value)[0].checkValidity()) {
        $.post("validate", {type: fieldName, value: $(value).val()}, function (responseText) {   // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
                var element = '#result-' + fieldName;
                console.log(element);
                if (responseText === 'true') {
                    $(value).addClass('is-invalid');
                    $('#submit-button').prop("disabled", true);
                } else {
                    $(value).addClass('is-valid');
                    $('#submit-button').prop("disabled", false);
                }
            }
        )
    }
}
