$(document).ready(function () {
    FondController.load();
});

const FondController = {

    _fondForm: $("#fondForm"),

    _submitButton: $("#submit"),

    _errorHead: $("#errorHead"),
    _errorText: $("#errorText"),
    _errorModal: $("#errorModal"),

    load: function () {
        let self = this;

        self._fondForm.on("submit", function (e) {
            e.preventDefault();
            self._submitButton.attr("disabled", true);
            let url = "/fonds";
            let type = "post";
            if (fondId != null) {
                url = url + '/' + fondId;
                type = "put";
            }
            /*var selectedCountryId = $("#countryInput").val();
            var selectedCountryName = $("#countryInput option:selected").text();

            // Создаем объект страны
            var countryObj = {
                id: selectedCountryId,
                name: selectedCountryName
            };
            */
            $.ajax({
                type: type,
                url: url,
                data: JSON.stringify({
                    id: $("#fondIdInput").val(),
                    name: $("#nameInput").val(),
                    city: Number($("#cityInput").val()),
                    creationDate: $("#creationDateInput").val(),
                    user: Number($("#userInput").val()),
                    phone: $("#phoneInput").val(),
                    sum: $("#sumInput").val()
                }),
                contentType: "application/json",
                success: function(response) {
                    console.log("Success:", response);
                    window.location = "/fonds";
                },
                error: function(error) {
                    console.error("Error:", error);
                }
            });
        });
    }
};