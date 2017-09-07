  $(function () {
            // 调用方法：加载日期控件
      loadDatepicker();
      var myDate=new Date();
      var year=myDate.getFullYear();
      var month=myDate.getMonth()+1;
      if(month.toString().length=1)
      { month="0"+month.toString();}
      $('#cxrqq').val(year+month);

  });

        /**
         *  方法：查询方法
         * */
        function toSearch() {

            /*--------------------参数值获取--start--------------------*/
            var lx = $("#div_LX").find("input[name='inlineRadio']:checked").val();
            var cxrqq = $("#cxrqq").val();
            if (cxrqq == "") {
                alert("所属日期起不能为空！");
                return;
            }

            var mon=parseInt(cxrqq.substr(4,2))
            /*--------------------参数值获取--end--------------------*/
            /*--------------------分页参数赋值--start--------------------*/
            var controlId = $("#jg");
            var where = "";
            var colName="";
            var cols = [];
            if (lx=="5")
            {
              colName = "<tr> <th rowspan='2' style='vertical-align: middle;style=' text-align:center;'' data-field='id'>序号</th> <th rowspan='2' style='vertical-align: middle;style=' text-align:center;'' data-field='name'>纳税人代码</th>"
                +"<th rowspan='2' style='vertical-align: middle;style=' text-align:center;'' data-field='name'>纳税人识别码</th><th rowspan='2' style='vertical-align: middle;style=' text-align:center;'' data-field='name'>纳税人名称</th><th colspan='4' style=' text-align:center;' data-field='name'>"+mon+"月</th>"
                +"<th colspan='4' style=' text-align:center;' data-field='name'>1-"+mon+"月</th> </tr><tr><th data-field='price'style=' text-align:center;'>本期税收</th> <th data-field='price'style=' text-align:center;'>同期税收</th>"
                +"<th data-field='price'style=' text-align:center;'>增减额</th> <th data-field='price'style=' text-align:center;'>±%</th> <th data-field='price'style=' text-align:center;'>本期税收</th> <th data-field='price'style=' text-align:center;'>同期税收</th>"
                +"<th data-field='price'style=' text-align:center;'>增减额</th> <th data-field='price'style=' text-align:center;'>±%</th></tr>";

                cols.push({data: "RN"});
                cols.push({data: "NSRBM"});
                cols.push({data: "NSRSBM"});
                cols.push({data: "NSR_MC"});
                cols.push({data: "ZSS_BQ"});
                cols.push({data: "ZSS_QNTQ"});
                cols.push({data: "ZSS_ZJE"});
                cols.push({data: "ZSS_FD2"});
                cols.push({data: "ZSS_BQ1"});
                cols.push({data: "ZSS_QNTQ1"});
                cols.push({data: "ZSS_ZJE1"});
                cols.push({data: "ZSS_FD1"});
            }else if(lx =="6"||lx =="7"||lx =="8"||lx =="11"||lx =="10")
            {
                colName = "<tr> <th rowspan='2' style='vertical-align: middle;' data-field='id'>序号</th> <th rowspan='2' style='vertical-align: middle; data-field='name'>类型</th>"
                    +"<th rowspan='2' style='vertical-align: middle;text-align:center;' data-field='name'>税收规模代码</th><th rowspan='2' style='vertical-align: middle;text-align:center;' data-field='name'>税收规模</th>"
                    +"<th rowspan='2' style='vertical-align: middle;text-align:center;' data-field='name'>户数</th><th colspan='4' style=' text-align:center;' data-field='name'>"+mon+"月</th>"
                    +"<th colspan='4' style=' text-align:center;' data-field='name'>1-"+mon+"月</th> </tr><tr><th data-field='price'style=' text-align:center;'>本期税收</th> <th data-field='price' style=' text-align:center;'>同期税收</th>"
                    +"<th data-field='price' style='text-align:center;'>增减额</th> <th data-field='price' style=' text-align:center;'>±%</th> <th data-field='price' style=' text-align:center;'>本期税收</th> <th data-field='price'style='text-align:center;'>同期税收</th>"
                    +"<th data-field='price'style='text-align:center;'>增减额</th> <th data-field='price' style=' text-align:center;'>±%</th></tr>";

                cols.push({data: "RN"});
                cols.push({data: "LX"});
                cols.push({data: "XLDM"});
                cols.push({data: "XLDMMC"});
                cols.push({data: "ZSS_HS"});
                cols.push({data: "ZSS_BQ"});
                cols.push({data: "ZSS_QNTQ"});
                cols.push({data: "ZSS_ZJE"});
                cols.push({data: "ZSS_FD2"});
                cols.push({data: "ZSS_BQ1"});
                cols.push({data: "ZSS_QNTQ1"});
                cols.push({data: "ZSS_ZJE1"});
                cols.push({data: "ZSS_FD1"});

            }else{


            }
          var colsefs=[];
            if(lx!="5")
            {
                colsefs = [{
                    targets:1,
                    visible:false
                },{
                    targets:2,
                    visible:false
                },{
                    render: function (data, type, row) {
                        var lx = row.LX;
                        var lxmc = row.XLDMMC.replace(/[\r\n]/g,"");
                        var xldm = row.XLDM;
                        return "<a style='cursor:pointer' href=\"javascript:;;\" onclick=\"gomx('" + lx + "','" + lxmc + "','" + xldm + "','"+ cxrqq + "');\" data-toggle=\"modal\">" + data + "</a>";
                    },
                    targets: 4

                }
                ];
            }
            var url = "/fzfxptSql";
            var submitMethod = "POST";

            var submitData = function (d) {
                d.lx = lx;
                d.lxdm = "";
                d.cxrqq = cxrqq;
            }
            /*--------------------分页参数赋值--end--------------------*/
            // 调用方法：加载分页控件
            addTableCols(colName);
            loadDataTable(controlId, where, cols, colsefs, url, submitMethod, submitData);
        }

     //跳转至明细页面
        function gomx(lx, lxmc,xldm,cxrqq) {
            //添加导航标签

            $("#dh > li").removeClass("active");
            var newDh = $("<li class= 'active'><a style='cursor:pointer' href=\"javascript:;;\" onclick=\"navlink('" + lx + "','" + xldm + "','" + cxrqq + "');\">" + lxmc + "</a></li>");
            $("#dh").append(newDh);
            //添加导航点击事件（功能：1、取消该导航层之后选中的展示列状态 2、删除后续导航标签）
            $("#dh li").click(function () {
                var i = $(this).index();
                $("#dh li").filter(function (index, v) {
                    return index > i;
                }).remove();

                $(this).addClass("active");
            });

            /*--------------------分页参数赋值--start--------------------*/
            var mon=parseInt(cxrqq.substr(4,2))
            var controlId = $("#jg");
            var where = "";
            var colName = "<tr> <th rowspan='2' style='vertical-align: middle;' data-field='id'>序号</th> <th rowspan='2' style='vertical-align: middle;' data-field='name'>纳税人代码</th>"
                    +"<th rowspan='2' style='vertical-align: middle;' data-field='name'>纳税人识别码</th><th rowspan='2' style='vertical-align: middle;' data-field='name'>纳税人名称</th>"
                +"<th rowspan='2' style='vertical-align: middle;' data-field='name'>管理所</th><th colspan='4' style=' text-align:center;' data-field='name'>"+mon+"月</th>"
                +"<th colspan='4' style=' text-align:center;' data-field='name'>1-"+mon+"月</th> </tr><tr><th data-field='price'>本期税收</th> <th data-field='price'>同期税收</th>"
                +"<th data-field='price'>增减额</th> <th data-field='price'>±%</th> <th data-field='price'>本期税收</th> <th data-field='price'>同期税收</th>"
                +"<th data-field='price'>增减额</th> <th data-field='price'>±%</th></tr>";
            var cols = [ ];
            var colsefs = "";
            var url = "/fzfxptSql";
            var submitMethod = "POST";
            //var submitData = "lx=" + lx + "&cxrqq=" + cxrqq + "&cxrqz=" + cxrqz;
            var submitData = function (d) {
                d.lx = lx;
                d.xldm = xldm;
                d.cxrqq = cxrqq;
            }
                cols.push({data: "RN"});
                cols.push({data: "NSRBM"});
                cols.push({data: "NSRSBM"});
                cols.push({data: "NSR_MC"});
                cols.push({data: "GLJG_MC"});
                cols.push({data: "ZSS_BQ"});
                cols.push({data: "ZSS_QNTQ"});
                cols.push({data: "ZSS_ZJE"});
                cols.push({data: "ZSS_FD2"});
                cols.push({data: "ZSS_BQ1"});
                cols.push({data: "ZSS_QNTQ1"});
                cols.push({data: "ZSS_ZJE1"});
                cols.push({data: "ZSS_FD1"});

            addTableCols(colName);//加载列
            /*--------------------分页参数赋值--end--------------------*/
            // 调用方法：加载分页控件
            loadDataTable($("#jg"), where, cols, colsefs, url, submitMethod, submitData);

        }
          //datatable加载前先加载列
        function addTableCols(colName) {

            $("#jg").DataTable().clear();
            $("#jg").DataTable().destroy();
            $("#jg > thead > tr > th").remove();
            var rsHtml = colName;
            $("#jg > thead").append(rsHtml);


        }
