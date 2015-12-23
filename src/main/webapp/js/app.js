$.ajax({
    url: '/client',
    success: function (result) {
        document.getElementById("clients_table").innerHTML = result;
    }
});

function submitform() {
    var formData = JSON.stringify({"firstname": "Egor", "lastname": "Trololo"});
    console.log(formData);

    // send ajax
    $.ajax({
        url: '/client', // url where to submit the request
        type: 'POST', // type of action POST || GET
        contentType: "application/json",
        dataType: 'json', // data type
        data: formData, // post data || get data
        success: function (result) {
            // you can see the result from the console
            // tab of the developer tools
            console.log(result);
        },
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}
