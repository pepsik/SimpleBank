$.ajax({
    url: '/client',
    success: function (response) {
        create_table(response);
    }
});

function create_table(json) {
    var tr;
    tr = $('<tr/>');
    tr.append("<td>" + "ID" + "</td>");
    tr.append("<td>" + "Firstname" + "</td>");
    tr.append("<td>" + "Lastname" + "</td>");
    tr.append("<td>" + "AccID | Balance" + "</td>");
    $('#clients_table').append(tr);
    for (var i = 0; i < json.length; i++) {
        tr = $('<tr/>');
        tr.append("<td>" + json[i].id + "</td>");
        tr.append("<td>" + json[i].firstname + "</td>");
        tr.append("<td>" + json[i].lastname + "</td>");
        for (var j = 0; j < json[i].accounts.length; j++) {
            tr.append("<td>" + json[i].accounts[j].id + " | " + json[i].accounts[j].balance + "</td>");
        }
        $('#clients_table').append(tr);
    }
}

function create_client() {
    var result_table = document.getElementById("result_table");
    var form = document.getElementById("create_client");
    var formData = JSON.stringify({
        "firstname": form.elements[0].value,
        "lastname": form.elements[1].value
    });
    $.ajax({
        url: '/client',
        type: 'POST',
        contentType: "application/json",
        dataType: 'json',
        data: formData,
        success: function (result) {
            console.log(result);
            location.reload();
        },
        error: function (xhr, resp, text) {
            obj = JSON.parse(xhr.responseText);
            result_table.innerHTML = "<strong> <p style='color: red'> ERROR: " + obj.message;
            console.log(xhr, resp, text);
        }
    });
}

function create_account() {
    var result_table = document.getElementById("result_table");
    var form = document.getElementById("create_account");
    var formData = JSON.stringify({
        "firstname": form.elements[0].value,
        "lastname": form.elements[1].value
    });
    $.ajax({
        url: '/client/account',
        type: 'POST',
        contentType: "application/json",
        dataType: 'json',
        data: formData,
        success: function (result) {
            console.log(result);
            location.reload();
        },
        error: function (xhr, resp, text) {
            obj = JSON.parse(xhr.responseText);
            result_table.innerHTML = "<strong> <p style='color: red'> ERROR: " + obj.message;
            console.log(xhr, resp, text);
        }
    });
}

function show_client_accounts() {
    var result_table = document.getElementById("result_table");
    var form = document.getElementById("show_client_accounts");
    var firstname = form.elements[0].value;
    var lastname = form.elements[1].value;
    $.ajax({
        url: '/client/accounts',
        type: 'GET',
        data: {firstname: firstname, lastname: lastname},
        success: function (result) {
            var str = "Client " + firstname + " " + lastname + " have accounts:<br>";
            for (var i = 0; i < result.length; i++) {
                var ob = result[i];
                str += "id " + ob["id"] + " balance " + ob["balance"] + "<br>";
            }
            result_table.innerHTML = str;
            console.log(result.length);
        },
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}

function find_clients_with_min_balance() {
    var result_table = document.getElementById("result_table");
    $.ajax({
        url: '/client/min',
        type: 'GET',
        success: function (result) {
            var str = "<p style=\"color:green\">";
            str += "Client ";
            str += result[0].firstname;
            str += "  ";
            str += result[0].lastname;
            str += " has the lowest balance!";
            $('#result_table').append(str);
            console.log(result);
        },
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}

function find_clients_with_max_balance() {
    var result_table = document.getElementById("result_table");
    $.ajax({
        url: '/client/max',
        type: 'GET',
        success: function (result) {
            var str = "<p style=\"color:green\">";
            str += "Client ";
            str += result[0].firstname;
            str += "  ";
            str += result[0].lastname;
            str += " has the largest balance!";
            $('#result_table').append(str);
            console.log(result);
        },
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}

function show_all_accounts() {
    var result_table = document.getElementById("result_table");
    $.ajax({
        url: '/client/accounts',
        type: 'GET',
        success: function (result) {
            result_table.innerHTML = JSON.stringify(result);
            console.log(result);
        },
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}

function deposit() {
    var result_table = document.getElementById("result_table");
    var form = document.getElementById("deposit");
    var formData = {
        "accountId": form.elements[0].value,
        "amount": form.elements[1].value
    };

    $.ajax({
        url: '/client/deposit',
        contentType: "application/json",
        dataType: 'json',
        data: formData,
        type: 'GET',
        success: function (result) {
            alert("Success! You put some money on your account.");
            location.reload();
            console.log(result);
        },
        error: function (xhr, resp, text) {
            obj = JSON.parse(xhr.responseText);
            result_table.innerHTML = "<strong> <p style='color: red'> ERROR: " + obj.message;
            console.log(xhr, resp, text);
        }
    });
}
function withdraw() {
    var result_table = document.getElementById("result_table");
    var form = document.getElementById("withdraw");
    var formData = {
        "accountId": form.elements[0].value,
        "amount": form.elements[1].value
    };

    $.ajax({
        url: '/client/withdraw',
        contentType: "application/json",
        dataType: 'json',
        data: formData,
        type: 'GET',
        success: function (result) {
            alert("Success! You lose some money from your account.");
            location.reload();
            console.log(result);
        },
        error: function (xhr, resp, text) {
            obj = JSON.parse(xhr.responseText);
            result_table.innerHTML = "<strong> <p style='color: red'> ERROR: " + obj.message;
            console.log(xhr, resp, text);
        }
    });
}

function payment() {
    var result_table = document.getElementById("result_table");
    var form = document.getElementById("payment");
    var formData = JSON.stringify({
        "senderAccountId": form.elements[0].value,
        "amount": form.elements[1].value,
        "recipientAccountId": form.elements[2].value
    });

    $.ajax({
        url: '/client/payment',
        contentType: "application/json",
        dataType: 'json',
        data: formData,
        type: 'POST',
        success: function (result) {
            alert("Success! You send some money.");
            location.reload();
            console.log(result);
        },
        error: function (xhr, resp, text) {
            obj = JSON.parse(xhr.responseText);
            result_table.innerHTML = "<strong> <p style='color: red'> ERROR: " + obj.message;
            console.log(xhr, resp, text);
        }
    });
}