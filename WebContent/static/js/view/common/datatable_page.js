/**
 *  方法：加载分页控件
 * */
function loadDataTable(controlId, where, cols, colsefs, url, submitMethod, submitData) {
    var table = controlId.DataTable({
        destroy: true,
        ordering: false,
        searching: false,
        bLengthChange: false,
        oLanguage: {
            sZeroRecords: "查询不到任何相关数据",
            sProcessing: "数据正在加载中....请稍等",
            sInfo: "当前显示第_START_至_END_项,共_TOTAL_项",
            oPaginate: {
                sFirst: "首页",
                sPrevious: "上页",
                sNext: "下页",
                sLast: "末页",
                sJump: "跳转"
            }
        },
        processing: true,
        serverSide: true,
        "ajax": {
            url: url,
            type: submitMethod,
            data: submitData,
        },
        aLengthMenu: [[10, 25, 50], ["10条", "25条", "50条"]],
        columns: cols,
        columnDefs: colsefs
    });
}