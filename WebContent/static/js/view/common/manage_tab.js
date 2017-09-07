/**
 * Created by JXL on 2017/8/28.
 */
// 加页签
function addTab(href, nsrsbm, text) {

    $("#top-search").val(nsrsbm);
    $(".page-tabs-content>a").removeClass("active");
    $(".page-tabs-content").append($("<a href= 'javascript:;' class='active J_menuTab' data-id='" + href + nsrsbm + "'>" + text + " <i class='fa fa-times-circle'></i></a>"));
    $("#content-main>.J_iframe").css("display", "none");
    $("#content-main").append($("<iframe class='J_iframe' name='iframe3' width='100%' height='100%' src='" + href + nsrsbm + "' frameborder='0' data-id='" + href + nsrsbm + "' seamless></iframe>"));
}