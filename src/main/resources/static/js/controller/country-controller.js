$(document).ready(function () {
    CountryController.load();
});

const CountryController = {

    _countryForm: $("#countryForm"),

    _submitButton: $("#submit"),

    _errorHead: $("#errorHead"),
    _errorText: $("#errorText"),
    _errorModal: $("#errorModal"),

    load: function () {
        let self = this;

        self._countryForm.on("submit", function (e) {
            e.preventDefault();
            self._submitButton.attr("disabled", true);
            let url = "/countries";
            let type = "post";
            if (countryId != null) {
                url = url + '/' + countryId;
                type = "put";
            }
            $.ajax({
                type: type,
                url: url,
                data: JSON.stringify({
                    id: $("#countryIdInput").val(),
                    name: $("#nameInput").val()
                }),
                contentType: "application/json",
                success: function(response) {
                    console.log("Success:", response);
                    window.location = "/countries";
                },
                error: function(error) {
                    console.error("Error:", error);
                }
            });
        });
    }
};