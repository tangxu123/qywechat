///**
// * 方法：打印
// * */
//function preview(oper) {
//    if (oper < 10) {
//        bdhtml = window.document.body.innerHTML;//获取当前页的html代码
//        sprnstr = "<!--startprint" + oper + "-->";//设置打印开始区域
//        eprnstr = "<!--endprint" + oper + "-->";//设置打印结束区域
//
//
//
//        prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 18); //从开始代码向后取html
//
//
//
//        prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));//从结束代码向前取html
//
//
//        window.document.body.innerHTML = prnhtml;
//        window.print();
//        window.document.body.innerHTML = bdhtml;
//    } else {
//        window.print();
//    }
//}

/**
 * 方法：打印
 * */
function preview(oper, oper2) {
    if (oper < 10) {
        bdhtml = window.document.body.innerHTML;//获取当前页的html代码
        sprnstr = "<!--startprint" + oper + "-->";//设置打印开始区域
        eprnstr = "<!--endprint" + oper + "-->";//设置打印结束区域

        sprnstr2 = "<!--startprint" + oper2 + "-->";//设置打印开始区域
        eprnstr2 = "<!--endprint" + oper2 + "-->";//设置打印结束区域

        prnhtml = bdhtml.substring(bdhtml.indexOf(sprnstr) + 18); //从开始代码向后取html

        prnhtml2 = bdhtml.substring(bdhtml.indexOf(sprnstr2) + 18); //从开始代码向后取html

        prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));//从结束代码向前取html

        prnhtml2 = prnhtml2.substring(0, prnhtml2.indexOf(eprnstr2));//从结束代码向前取html

        prnhtml3 = prnhtml + prnhtml2;

        window.document.body.innerHTML = prnhtml3;
        window.print();
        window.document.body.innerHTML = bdhtml;
    } else {
        window.print();
    }
}