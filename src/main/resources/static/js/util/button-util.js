$(document).ready(function () {
    $(".form").submit(function () {
        $(":submit").attr("disabled", true);
    });
});

function toggleIcons(i) {
    console.log(i);
    $("#switchable" + i).toggleClass("fa-plus-square").toggleClass("fa-minus-square");
}

function refreshPage() {
    location.reload();
}