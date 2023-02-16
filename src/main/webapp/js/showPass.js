function show(elementId) {
    let element = '#' + elementId;
    if ($(element)[0].getAttribute('type') === 'password') {
        $(element)[0].removeAttribute('type');
        $(element)[0].setAttribute('type', 'text');
    } else {
        $(element)[0].removeAttribute('type');
        $(element)[0].setAttribute('type', 'password');
    }
}


