$(document).ready(function () {
    CapitalSourceController.load();

//    // Получаем CSRF токен и заголовок из мета-тегов
//    var token = $("meta[name='_csrf']").attr("content");
//    var header = $("meta[name='_csrf_header']").attr("content");
//
//    $.ajaxSetup({
//        beforeSend: function(xhr) {
//            xhr.setRequestHeader(header, token);
//        }
//    });
});

const CapitalSourceController = {

    _capitalSourceForm: $("#capitalSourceForm"),

    _submitButton: $("#submit"),

    _errorHead: $("#errorHead"),
    _errorText: $("#errorText"),
    _errorModal: $("#errorModal"),

    load: function () {
        let self = this;

        self._capitalSourceForm.on("submit", function (e) {
            e.preventDefault();
            self._submitButton.attr("disabled", true);
            let url = "/capital_sources";
            let type = "post";
            if (capitalSourceId != null) {
                url = url + '/' + capitalSourceId;
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
            var idIn = $("#capitalSourceIdInput").val();
            var fondIn = Number($("#fondInput").val());
            var currencyTypeIn = Number($("#currencyTypeInput").val());
            var donationDateIn = $("#donationDateInput").val();
            var userIn = Number($("#userInputId").val());
            $.ajax({
                type: type,
                url: url,
                data: JSON.stringify({
                    id: idIn,
                    sum: Number($("#sumInput").val()),
                    currencyType: currencyTypeIn,
                    donationDate: donationDateIn,
                    user: userIn,
                    fonds: [Number(fondIn)]
                }),
                contentType: "application/json",
                success: function(response) {
                    console.log("Success:", response);
                    window.location = "/capital_sources";
                },
                error: function(error) {
                    console.error("Error:", error);
                }
            });
        });
    }
};