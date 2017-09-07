/**
 *  方法：加载日期控件
 * */
 function loadDatepicker() {
    $(".input-group.date").datepicker({
        todayBtn: "linked",
        keyboardNavigation: !1,
        forceParse: !1,
        calendarWeeks: !0,
        autoclose: !0
    });

    $(".datepicker").datepicker({
        language: "zh-CN",
        autoclose: true,
        startView: 1,
        maxViewMode: 1,
        minViewMode: 1,
        format: "yyyymm"
    });
}