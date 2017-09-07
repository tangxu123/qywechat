/**
 * Created by SJJ on 2017/8/11.
 */
/*
$(function () {
    var keyword = "房产税";
    var firstId = "";
    $.ajax({
        type: "POST",
        url: "/zsk",
        data: {"searchKey": ""},
        dataType: "json",
        success: function (json) {
            var html = "";
            $.each(json, function (i, v) {

                var jm = v.jm;
                var title = v.title
                var ly = v.ly;
                var rq = v.rq;
                var key = v.key;
                var mx = v.mx;
                var ary = mx.split(",");
                var id = "";
                if (ary.length > 0) {
                    id = ary[0].replace(/\'/g, "");
                    firstId = id;
                }

                indexKeyword = title.indexOf(keyword);
                var titleHtml = "";
                if (indexKeyword == 0) {
                    titleHtml = "<mark>" + keyword + "</mark>" + title.substring(2);
                } else {
                    titleHtml = "<a href=\"javascript:void();\" onclick =\"showMx('" + id + "','" + keyword + "')\">" + title.substring(0, indexKeyword) + "<mark>" + keyword + "</mark>" + title.substring(indexKeyword + 3) + "</a>";
                }
                var trHtml = "<tr><td>" + jm + "</td><td>" + titleHtml + "</td>" + "<td>" + ly + "</td>" + "<td>" + rq + "</td></tr>";
                html += trHtml;
            });
            if (html != "") {

                $("#zsk_table>tbody").html(html);
                showMx(firstId, keyword);
            }
        },
        error: function () {

        }
    });
});
*/
function showMx(id, keyword) {

    $("#zskframe").attr('src', '/getZskContent?id=' + id + "&keyword=" + keyword);
    document.documentElement.scrollTop =  document.body.scrollTop = 0;

}

function search() {
    var keyword = $("#search_txt").val();
    if (keyword == "") {
        alert("请输入关键词搜索!");
    }
    var firstId = "";
    $.ajax({
        type: "POST",
        url: "/zsk",
        data: {"searchKey": keyword},
        dataType: "json",
        success: function (json) {
            var html = "";
            $.each(json, function (i, v) {

                var jm = v.jm;
                var title = v.title
                var ly = v.ly;
                var rq = v.rq;
                var key = v.key;
                var mx = v.mx;
                var ary = mx.split(",");
                var id = "";
                if (ary.length > 0) {
                    id = ary[0].replace(/\'/g, "");
                    if(i == 0){
                          firstId = id;
                    }
                }

                indexKeyword = title.indexOf(keyword);
                var titleHtml = "";
                if (indexKeyword == 0) {
                    titleHtml = "<mark>" + keyword + "</mark>" + title.substring(2);
                } else {
                    titleHtml = "<a href=\"javascript:void();\" onclick =\"showMx('" + id + "','" + keyword + "')\">" + title.substring(0, indexKeyword) + "<mark>" + keyword + "</mark>" + title.substring(indexKeyword + 3) + "</a>";
                }
                var trHtml = "<tr><td>" + jm + "</td><td>" + titleHtml + "</td>" + "<td>" + ly + "</td>" + "<td>" + rq + "</td></tr>";
                html += trHtml;
            });
            if (html != "") {

                $("#zsk_table>tbody").html(html);
                showMx(firstId, keyword);
            }
        },
        error: function () {

        }
    });
}
