function deleteRecord(id, name, type) {
    var btn = $('#delete-button');
    btn.attr("data-id", id);
    btn.attr("data-name", name);
    btn.attr("data-type", type);
    $('#deleteAlertModal').modal('show');
}

function deleteRequest(btn, url) {
    $('#deleteAlertModal').modal('hide');
    var id = $(btn).attr("data-id");
    var name = $(btn).attr("data-name");
    var type = $(btn).attr("data-type");
    $.ajax({
        method: "DELETE",
        url: url + id,
        success: function () {
            $('#successHead').text('Уведомление');
            $('#successText').text('Удаление ' + ' "' + type + '" : ' + ' "' + name + '" прошло успешно');
            $('#successModal').modal('show');
            window.location = url;
        },
        error: function (e) {
            $('#errorHead').text('Ошибка удаления');
            if (e.status === 403) {
                $('#errorText').text(e.responseText);
            } else {
                $('#errorText').text('Невозможно удалить ' + ' "' + type + '" : ' + ' "' + name + '"');
            }
            $('#errorModal').modal('show');
        },
    });
}