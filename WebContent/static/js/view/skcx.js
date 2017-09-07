//var colList = [];
var whereTj = "";
var table = null;
tj_load();
$(function () {


    $(".input-group.date").datepicker({
        todayBtn: "linked",
        keyboardNavigation: !1,
        forceParse: !1,
        calendarWeeks: !0,
        autoclose: !0
    });

    $(".datepicker").datepicker({
       language:"zh-CN",
       autoclose:true,
        startView:1,
        maxViewMode:1,
        minViewMode:1,
        format:"yyyy-mm"
    });

    var myDate=new Date();
    var year=myDate.getFullYear();
    var month=myDate.getMonth();
    if(month.toString().length=1)
    { month="-0"+month.toString();}
    $('#cxrqq').val(year+month);
    $('#cxrqz').val(year+month);
    //laydate({elem: "#cxrqq", event: "focus", format: "YYYY-MM"});
    //laydate({elem: "#cxrqz", event: "focus", format: "YYYY-MM"});

    table = $("#jg").DataTable({
        destroy: true,
        ordering: false,
        searching: false,
        bLengthChange: false,
        oLanguage: {
            sInfoEmpty: "当前显示第_START_至_END_项,共_TOTAL_项",
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
    });


});


//数据行全选
function qx(obj) {

    $('.where_chk').prop("checked", $(obj).prop("checked"));
    var ary = $('.where_chk:checked');
    $.each(ary, function (i, v) {
        addWhereVal(v.value);
    });

}
//展示列全选
function zxqx(obj) {

    var flag = ($(obj).prop("checked") ? "check" : "uncheck");
    $('.col_chk').iCheck(flag);
}
function tj_load() {
    $.ajax({
        type: 'POST',
        url: '/skcxtj',
        dataType: "json",
        async: false,
        cache: true,
        success: function (data) {

            var tjhtml = "";
            var colhtml = "";
            var xh = 0;
            var sfxz="";
            $.each(data, function (index, item) {
                if (item.LX == "1") {
                    if (index == 0) {
                        tjhtml += "<tr><td style='width:20%'><input class='tj_chk' type='radio' value='" + item.VALUE + "' mc='" + item.NAME +"' dmlx='" + item.DMLX + "'>&nbsp;&nbsp;<span id='tj_" + index + "'>" + item.NAME + "&nbsp;&nbsp;</span></td>";
                    } else if (index % 5 == 0) {
                        tjhtml += "</tr><tr><td style='width:20%'><input class='tj_chk'  type='radio' value='" + item.VALUE + "' mc='" + item.NAME + "' dmlx='" + item.DMLX + "'>&nbsp;&nbsp;<span id='tj_" + index + "'>" + item.NAME + "&nbsp;&nbsp;</span></td>";
                    } else {
                        tjhtml += "<td style='width:20%'><input class='tj_chk' type='radio' value='" + item.VALUE + "' mc='" + item.NAME +"' dmlx='" + item.DMLX + "'>&nbsp;&nbsp;<span id='tj_" + index + "'>" + item.NAME + "&nbsp;&nbsp;</span></td>";
                    }
                    xh += 1;
                } else {
                    if(item.MRZ==1)
                    {
                        sfxz="checked='checked'";
                    }else{
                        sfxz="";
                    }
                    if ((index - xh) == 0) {
                        colhtml += "<tr><td style='width:20%'><input class='col_chk' type='checkbox'"+sfxz+" value='" + item.VALUE + "' mc='" + item.NAME + "'>&nbsp;&nbsp;<span id='col_" + index + "'>" + item.NAME + "&nbsp;&nbsp;</span></td>";
                    } else if ((index - xh) % 5 == 0) {
                        colhtml += "</tr><tr><td style='width:20%'><input class='col_chk' type='checkbox'"+sfxz+" value='" + item.VALUE + "' mc='" + item.NAME + "'>&nbsp;&nbsp;<span id='col_" + index + "'>" + item.NAME + "&nbsp;&nbsp;</span></td>";
                    } else {
                        colhtml += "<td style='width:20%'><input class='col_chk' type='checkbox'"+sfxz+"  value='" + item.VALUE + "' mc='" + item.NAME + "'>&nbsp;&nbsp;<span id='col_" + index + "'>" + item.NAME + "&nbsp;&nbsp;</span></td>";
                    }
                }


            });
            var curCheckTj = null;
            if (tjhtml != "") {
                tjhtml += "</tr>";
                $("#tj").html(tjhtml);
                $('.tj_chk').iCheck({
                    checkboxClass: 'icheckbox_minimal-orange',
                    radioClass: 'iradio_minimal-orange',
                    increaseArea: '20%' // optional

                }).on('ifChecked', function (event) {
                    var colList = [];
                    $.each($(".col_chk:checked"), function (i, v) {
                        colList.push(v.value);
                    });
                    curCheckTj = $(this);
                    //alert(curCheckTj.val());
                    setTimeout(function () {
                        if (curCheckTj != null) {
                            var beginRq = $("#cxrqq").val();
                            var endRq = $("#cxrqz").val();

                            if (colList.length == 0) {
                                alert("请选择至少一个查询展示列！");
                                curCheckTj.iCheck('uncheck');
                                return;
                            } else if (beginRq == "") {
                                alert("请选择查询日期起!");
                                curCheckTj.iCheck('uncheck');
                                return;
                            } else {
                                var tj = curCheckTj.val();
                                var col = colList.toString();
                                var whereList = [];
                                var where = [];
                                var dmlx=curCheckTj.attr("dmlx");


                                if ($("#dh > li").length == 1 || $(".where_chk:checked").length > 0) {
                                    //添加导航标签
                                    $("#dh > li").removeClass("active");
                                    var newDh = $("<li class= 'active'><a style='cursor:pointer' href=\"javascript:;;\" onclick=\"navlink('" + tj + "','" + col + "','" + dmlx +"');\" hv='" + tj.split(',')[0] + "'>" + curCheckTj.attr("mc") + "</a></li>");
                                    $("#dh").append(newDh);
                                    if (tj != "") {
                                        var ary = tj.split(',');
                                        if (ary.length > 1) {

                                            var li;
                                            while ((li = newDh.prev()) != []) {
                                                var whereTj = "";
                                                var link = li.find("a");
                                                if (link.text() != "导航") {
                                                    var dhtj = link.attr("hv");
                                                    var dhz = link.attr("whereVal");
                                                    where.push(dhtj + ":" + dhz);
                                                    newDh = li;
                                                } else {
                                                    break;
                                                }

                                            }
                                            where = where.join(";");
                                        }

                                    }

                                    //加载表列
                                    addTableCols();

                                    //加载表数据
                                    loadGrid(tj, col, where, 0,dmlx);
                                    /*
                                     var lth=0;
                                     lth = $("#dh li").find('a').length;
                                     if(lth>1){
                                     where=$($("#dh > li").find('a')[lth-1]).attr("hv")+"|"+where;

                                     }
                                     **/

                                    //添加导航点击事件（功能：1、取消该导航层之后选中的展示列状态 2、删除后续导航标签）
                                    $("#dh li").click(function () {
                                        var i = $(this).index();
                                        $("#dh li").filter(function (index, v) {
                                            if (index > i) {
                                                $.each($('.tj_chk'), function (j, w) {
                                                    if ($(v).find('a').html() == $(w).attr("mc")) {
                                                        $(w).iCheck("uncheck");
                                                    }
                                                });
                                            }
                                            return index > i;
                                        }).remove();

                                        $(this).addClass("active");
                                    });


                                } else {
                                    alert("请选择查询项目!");
                                    curCheckTj.iCheck('uncheck');
                                    return;
                                }


                            }

                        }

                    }, 100);
                });
            }
            if (colhtml != "") {
                colhtml += "</tr>";
                $("#col").html(colhtml);
                $('.col_chk').iCheck({
                    checkboxClass: 'icheckbox_minimal-orange',
                    radioClass: 'iradio_minimal-orange',
                    increaseArea: '20%' // optional

                });
            }


        }
    });
}
//导航点击加载界面
function navlink(tj, col,dmlx) {

    var li;
    var where = [];
    var dh = $("#dh > li > a[hv='" + tj.split(',')[0] + "']").parent();
    while ((li = dh.prev()) != []) {
        var whereTj = "";
        var link = li.find("a");
        if (link.text() != "导航") {
            var dhtj = link.attr("hv");
            var dhz = link.attr("whereVal");
            where.push(dhtj + ":" + dhz);
            dh = li;
        } else {
            break;
        }

    }
    if (where.length > 0) {
        where = where.join(";");
    }
    addTableCols();
    //alert(where.toString());
    loadGrid(tj, col, where.toString(), 1,dmlx);//loadGrid(tj, col, where.toString());
}
//datatable加载前先加载列
function addTableCols() {
    if (table != null) {
        table.clear();
        table.destroy();
    }
    var theadAry = $(".col_chk:checked");
    if (theadAry.length > 0) {
        $("#jg > thead > tr > th").remove();
        var rsHtml = "<th style=\"width:60px;\"><input type=\"checkbox\" onclick=\"qx(this)\"/>&nbsp;&nbsp;选择</th> <th>代码</th><th>项目</th>";

        $.each(theadAry, function (i, v) {

            var mc = $(v).attr("mc");
            var colId = $(v).val();
            rsHtml += "<th colId = '" + colId + "'>" + mc + "</th>";
            rsHtml += "<th colId ='" + colId + "_TQ" + "'>" + ( "上期" + mc ) + "</th>";
            rsHtml += "<th colId ='" + colId + "_ZS" + "'>" + ( "本期" + mc + "增收") + "</th>";
            rsHtml += "<th colId ='" + colId + "_ZZ" + "'>" + ( "本期" + mc + "增长%") + "</th>";
        });
        $("#jg > thead > tr").append(rsHtml);


    }


}

function addWhereVal(v) {
    var activeDh = $("#dh > .active > a");
    if (activeDh.length > 0) {
        var hv = activeDh.attr("hv");
        if (hv != undefined) {

            var checkedAry = $(".where_chk:checked");
            var whereValAry = [];
            $.each(checkedAry, function (i, v) {
                whereValAry.push(v.value);
            });
            if (activeDh.attr("whereVal") != undefined) {
                var oldWhereVal = activeDh.attr("whereVal").split(',');
                if (oldWhereVal.length > 0) {
                    $.each(oldWhereVal, function (i, v) {
                        if ($.inArray(v, whereValAry) == -1) {
                            whereValAry.push(v);
                        }
                    })
                }
            }

            activeDh.attr("whereVal", whereValAry.toString());
        }
    }
}

//根据条件（group by）、展示列、where条件加载数据、lx类型1：导航 dmlx: 代码类型 0 数值，1
function loadGrid(tj, col, where, lx,dmlx) {
    var ary = col.split(',');
    var cols = [];
    var tjAry = tj.split(',');
    var sfhf="";
    if (tjAry.length > 0) {

        cols.push({"data": tjAry[0]});
        cols.push({"data": tjAry[0]});
        cols.push({"data": tjAry[1]});
    }
    for (var i = 0; i < ary.length; i++) {
        if (ary[i].indexOf('|') >= 0) {
            ary[i] = ary[i].split('|')[1];
        }

        var data = {"data": ary[i]};
        cols.push(data);
        data = {"data": ary[i] + "_TQ"};
        cols.push(data);
        data = {"data": ary[i] + "_ZS"};
        cols.push(data);
        data = {"data": ary[i] + "_ZZ"};
        cols.push(data);

    }
    if($('#sfhf').prop("checked") )
    {
        sfhf="";
    }else{
        sfhf="1";
    }
    if (table != null) {
        table = $("#jg").DataTable({
            paging:false,
            destroy: true,
            ordering: true,
            searching: false,
           // bLengthChange: false,
           // iDisplayLength:20,
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
            ajax: function (data, callback, settings) {

                var beginRq = $("#cxrqq").val();
                var endRq = $("#cxrqz").val();
                var beginYear, beginMonth, endYear, endMonth;
                if (endRq == "") {
                    endYear = new Date().getFullYear();
                    endMonth = new Date().getMonth() + 1;
                } else {
                    ary = endRq.split('-');
                    endYear = ary[0];
                    endMonth = parseInt(ary[1]);
                }
                ary = beginRq.split('-');
                beginYear = ary[0];
                beginMonth = parseInt(ary[1]);

                data.tj = tj;
                data.col = col;
                data.where = where;
                data.beginYear = beginYear;
                data.beginMonth = beginMonth;
                data.endYear = endYear;
                data.endMonth = endMonth;
                data.sfhf=sfhf;
                data.dmlx=dmlx;


                $.ajax({
                    type: "POST",
                    url: "/skcxSql",
                    data: data,
                    dataType: "json",
                    success: function (json) {
                        window.sql = json.sql;
                        callback(json);
                    },
                    error: function () {
                        alert("查询失败!");
                    }
                });

                /*
                 url: "/skcxSql",
                 type: "POST",
                 data: function (d) {
                 var beginRq =  $("#cxrqq").val();
                 var endRq =  $("#cxrqz").val();
                 var beginYear,beginMonth,endYear,endMonth;
                 if(endRq ==  ""){
                 endYear =  new Date().getFullYear();
                 endMonth =  new Date().getMonth()+1;
                 }else{
                 ary =   endRq.split('-');
                 endYear = ary[0];
                 endMonth =  parseInt(ary[1]);
                 }
                 ary =  beginRq.split('-');
                 beginYear =  ary[0];
                 beginMonth = parseInt(ary[1]);

                 d.tj = tj;
                 d.col = col;
                 d.where = where;
                 d.beginYear = beginYear;
                 d.beginMonth = beginMonth;
                 d.endYear = endYear;
                 d.endMonth = endMonth;

                 },
                 success:function(json){

                 console.log(json.sql);
                 callback(json)
                 }
                 */
            },
            columns: cols,
            columnDefs: [{
                render: function (data, type, row) {
                    var v = row[tj.split(',')[0]];
                    rtnVal = "<th><input onclick=\"addWhereVal('" + v + "')\" type=\'checkbox\' class=\'where_chk\' value=\'" + v + "\'></th>";
                    if (lx!=1) {
                        var activeDh = $("#dh > .active > a");
                        if (activeDh.attr("whereVal") != undefined) {
                            var arrayVal = activeDh.attr("whereVal").split(',');
                            if ($.inArray(v, arrayVal) > -1) {
                                rtnVal = "<th><input onclick=\"addWhereVal('" + v + "')\" type=\'checkbox\' class=\'where_chk\' value=\'" + v + "\' checked=\'checked\'></th>";
                            }
                        }
                    }
                    return rtnVal
                },
                targets: 0,
                sClass: "col_center"
            }
            ]
        });
    }
    /*
     $("#jg").on('page.dt', function () {

     var activeDh = $("#dh > .active > a");
     debugger;
     if (activeDh.attr("whereVal") != undefined) {
     var arrayVal = activeDh.attr("whereVal").split(',');
     var whereAry = $(".where_chk");
     $.each(arrayVal, function (i, v1) {
     $.each(whereAry, function (j, v2) {
     if (v1 == v2.value) {
     v2.iCheck('check');
     return false;
     }
     })
     })
     }
     }).dataTable();*/

    /*
     var table = $("#example").dataTable({
     language: lang,  //提示信息
     autoWidth: false,  //禁用自动调整列宽
     stripeClasses: ["odd", "even"],  //为奇偶行加上样式，兼容不支持CSS伪类的场合
     processing: true,  //隐藏加载提示,自行处理
     serverSide: true,  //启用服务器端分页
     searching: false,  //禁用原生搜索
     orderMulti: false,  //启用多列排序
     order: [],  //取消默认排序查询,否则复选框一列会出现小箭头
     renderer: "bootstrap",  //渲染样式：Bootstrap和jquery-ui
     pagingType: "simple_numbers",  //分页样式：simple,simple_numbers,full,full_numbers
     columnDefs: [{
     "targets": 'nosort',  //列的样式名
     "orderable": false    //包含上样式名‘nosort’的禁止排序
     }],
     ajax: function (data, callback, settings) {
     //封装请求参数
     var param = {};
     param.limit = data.length;//页面显示记录条数，在页面显示每页显示多少项的时候
     param.start = data.start;//开始的记录序号
     param.page = (data.start / data.length) + 1;//当前页码
     //console.log(param);
     //ajax请求数据
     $.ajax({
     type: "GET",
     url: urlstr,//"/hello/list",
     cache: false,  //禁用缓存
     data: param,  //传入组装的参数
     dataType: "json",
     success: function (result) {
     //console.log(result);
     //setTimeout仅为测试延迟效果
     setTimeout(function () {
     //封装返回数据
     var returnData = {};
     returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
     returnData.recordsTotal = result.total;//返回数据全部记录
     returnData.recordsFiltered = result.total;//后台不实现过滤功能，每次查询均视作全部结果
     returnData.data = result.data;//返回的数据列表
     //console.log(returnData);
     //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
     //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
     callback(returnData);
     }, 200);
     }
     });
     },
     //列表表头字段
     columns: [
     {"data": "Id"},
     {"data": "Name"},
     {"data": "Sex"}
     ]
     }).api();
     //此处需调用api()方法,否则返回的是JQuery对象而不是DataTables的API对象
     */
}
function showSql() {
    if (window.sql != null) {
        layer.open({
            type: 2,
            title: '当前执行SQL',
            maxmin: true,
            shadeClose: true,
            skin: 'layui-layer-lan',
            area: ['560px', '400px'],
            content: '/showSql'
        })
    }
}