/**
 * Created by wiki on 2017/8/8.
 */
$(function () {
    col_load();
    row_load();
});
//加载列条件1、2、3
function col_load() {
    $.ajax({
        type: 'POST',
        url: '/skcxgjcol',
        dataType: "json",
        async: false,
        cache: true,
        success: function (data) {
            var col1html = "";
            var col2html = "";
            var col3html = "<tr>";
            var col1selhtml = "";
            var col2selhtml = "";

            $.each(data, function (index, item) {
                switch (item.TP) {
                    case "1":
                        //col1html += "<tr><td style='width:20%'><input class='tj_chk' type='radio' value='" + item.VALUE + "' mc='" + item.NAME + "'>&nbsp;&nbsp;<span id='col1_" + index + "'>" + item.NAME + "&nbsp;&nbsp;</span></td>";
                        col1html += "<div class='radio'><input onclick type='radio' name='radio1' id='col1_rd_" + index + "' ft='" + item.FT + "' bg='" + item.BG + "' tb='" + item.TB + "'><label>" + item.NM + "</label></div>";
                        col1selhtml += "<select id='col1_sel_" + index + "' width='50%' class='selectpicker show-tick form-control' multiple data-live-search='false'></select>";
                        break;
                    case "2":
                        col2html += "<div class='radio'><input type='radio' name='radio2' id='col2_rd_" + index + "' ft='" + item.FT + "' bg='" + item.BG + "' tb='" + item.TB + "'><label>" + item.NM + "</label></div>";
                        col2selhtml += "<select id='col2_sel_" + index + "' width='50%' class='selectpicker show-tick form-control' multiple data-live-search='false'></select>";
                        break;
                    case "3":
                        col3html += "<td style='width:20%'><input class='col_chk' type='checkbox' id='col3_chk_" + index + "' ft='" + item.FT + "' bg='" + item.BG + "' tb='" + item.TB + "'>&nbsp;&nbsp;<span>" + item.NM + "&nbsp;&nbsp;</span></td>";
                        break;
                    default:
                        break;
                }
            });
            col3html += "</tr>"
            $("#col1").html(col1html);
            $("#col1_sel").html(col1selhtml);
            $("#col2").html(col2html);
            $("#col2_sel").html(col2selhtml);
            $("#col3").html(col3html);
            $(".selectpicker").selectpicker({
                actionsBox: true
            });
            if (col1html != "" || col2html != "") {
                $("input[name^='radio']").click(function () {
                    var selId = "#" + $(this).prop("id").replace('rd', 'sel');
                    var cols = $(this).attr("FT").split(',');
                    var postdata = {};
                    postdata.dm = cols[0];
                    postdata.mc = cols[1];
                    postdata.tb = $(this).attr("TB");
                    $.ajax({
                        type: "POST",
                        url: "/getsel",
                        catche: false,
                        dataType: "json",
                        data: postdata,
                        success: function (data) {
                            $.each(data, function (i) {
                                debugger;
                                $(selId).append("<option value='" + data[i][postdata.dm] + "'>" + data[i][postdata.mc] + "</option>");
                                //$("<option></option>").val(data[i][postdata.dm]).text(data[i][postdata.mc]).appendTo($(selId));
                            });
                            $(selId).selectpicker('refresh');
                        }
                    });
                });
            }
        }
    });

}

function row_load() {
    $("#tp").hide();
    $("input[name='r1']").click(function () {
        if ($(this).val() == "1") {
            $("#tp").show();
        } else {
            $("#tp").hide();
        }
    });

    $.ajax({
        type: 'POST',
        url: '/skcxgjrow',
        dataType: "json",
        async: false,
        cache: true,
        success: function (data) {
            var row1html = "";
            var row2html = "";
            var row3html = "";
            var row1selhtml = "";
            var row2selhtml = "";

            $.each(data, function (index, item) {
                var innerhtml = "";
                innerhtml += "<div class='radio'><input onclick type='radio' name='radio1' id='col1_rd_" + index + "' col='" + item.COL + "' tb='" + item.TB + "'><label>" + item.NM + "</label></div>";
                $("#col1").html(col1html);
                $("#col2").html(col2html);
                $("#col3").html(col3html);
            });
        }
    });
}