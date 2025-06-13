$(document).ready(function () {
    CityController.load();
});

const CityController = {

    _cityForm: $("#cityForm"),

    _submitButton: $("#submit"),

    _errorHead: $("#errorHead"),
    _errorText: $("#errorText"),
    _errorModal: $("#errorModal"),

    load: function () {
        let self = this;

        self._cityForm.on("submit", function (e) {
            e.preventDefault();
            self._submitButton.attr("disabled", true);
            let url = "/cities";
            let type = "post";
            if (cityId != null) {
                url = url + '/' + cityId;
                type = "put";
            }
            $.ajax({
                type: type,
                url: url,
                data: JSON.stringify({
                    id: $("#cityIdInput").val(),
                    name: $("#nameInput").val(),
                    country: Number($("#countryInput").val())
                }),
                contentType: "application/json",
                success: function(response) {
                    console.log("Success:", response);
                    window.location = "/cities";
                },
                error: function(error) {
                    console.error("Error:", error);
                }
            });
        });
    }
};