$(document).ready(function () {

//        fetch("/currency_types/count_of_currency_types", {
//            method: "GET",
//            headers: {
//                "Content-Type": "application/json"
//            }
//        })
//        .then(response => response.json())
//        .then(data => {
//            console.log("Response from server:", data);
//            document.getElementById('countOfCurrencyType').value = Number(data);
//        })
//        .catch(error => {
//            console.error("Error:", error);
//        });

        $.ajax({
              type: "GET",
              url: "/currency_types/count_of_currency_types",
              contentType: "application/json",
              success: function(response) {
                  console.log("Success:", response);
                  document.getElementById('countOfCurrencyType').value = Number(response);
              },
              error: function(error) {
                  console.error("Error:", error);
              }
        });


    CurrencyTypeController.load();
});

const CurrencyTypeController = {

    _currencyTypeForm: $("#currencyTypeForm"),

    _submitButton: $("#submit"),

    _errorHead: $("#errorHead"),
    _errorText: $("#errorText"),
    _errorModal: $("#errorModal"),

    load: function () {
        let self = this;

        self._currencyTypeForm.on("submit", function (e) {
            e.preventDefault();
            self._submitButton.attr("disabled", true);
            let url = "/currency_types";
            let type = "post";
            if (currencyTypeId != null) {
                url = url + '/' + currencyTypeId;
                type = "put";
            }
            $.ajax({
                type: type,
                url: url,
                data: JSON.stringify({
                    id: $("#currencyTypeIdInput").val(),
                    name: $("#nameInput").val()
                }),
                contentType: "application/json",
                success: function(response) {
                    console.log("Success:", response);
                    window.location = "/currency_types";
                },
                error: function(error) {
                    console.error("Error:", error);
                }
            });
        });
    }
};