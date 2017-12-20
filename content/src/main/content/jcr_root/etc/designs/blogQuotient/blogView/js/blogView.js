(function ($) {
    $(document).ready(function () {
        var currentCount = $("#currentCount").val() ? parseInt($("#currentCount").val()) : 0;
        var pageUrl = "http://localhost:4502" + $("#pageUrl").val();
        console.log(currentCount, pageUrl);

        $.post(pageUrl, {
            "jcr:content/viewCount": (currentCount + 1)
        }).success(function (response) {
            console.log("view success");
        }).error(function () {
            console.log("view error");
        });

        $("#like").click(function () {
            var _this = $(this);
            var likes = _this.data("likes") ? parseInt(_this.data("likes")) : 0;
            console.log(likes," likes");
            $.post(pageUrl, {
                "jcr:content/likes": (likes + 1)
            }).success(function (response) {
                console.log("like success");
                _this.attr("data-likes",(likes + 1));
                _this.text((likes + 1) + " likes");
            }).error(function () {
                console.log("like error");
            });
        });
    });
})(jQuery);