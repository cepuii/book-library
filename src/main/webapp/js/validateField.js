function clearClass(fieldName) {
    $('#' + fieldName).removeClass('is-valid').removeClass('is-invalid');
    // $('#' + fieldName).removeClass('is-invalid');
}


function validate(fieldName) {
    let value = ('#' + fieldName);
    console.log(value);
    if ($(value)[0].checkValidity()) {
        $.post("validate", {type: fieldName, value: $(value).val()}, function (responseText) {

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
