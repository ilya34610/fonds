$(document).ready(function () {
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