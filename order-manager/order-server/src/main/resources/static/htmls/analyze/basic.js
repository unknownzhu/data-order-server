layui.config({
    base: 'module/'
}).extend({
    dtree: '{/}../../../component/lib/layui_ext/dtree/dtree',
    layuiTableColumnEdit: '/layuiTableColumnEdit',
}).use(['element', 'layer', 'form', 'layedit', 'jquery', 'table', 'dtree'], function () {

    var dbId = GetQueryString('dbId');
    var url = GetQueryString('url');
    var id = GetQueryString("dbId");


    var queryUrl = '/sqlpad/' + dbId;
    var queryIframe = document.getElementById("queryIframe");
    var iframeHeight = window.screen.availHeight;
    var iframe = document.createElement("iframe");
    //   document.body.appendChild(iframe);
    iframe.src = queryUrl;
    iframe.width = '100%';
    iframe.height = iframeHeight - 300;
    iframe.border = 'none';
    iframe.id = 'iframe';
    iframe.name = 'iframe';
    iframe.style.border = 'none';
    iframe.style.overflow = 'hidden';

    var $ = layui.jquery,
        layer = layui.layer,
        form = layui.form,
        layedit = layui.layedit,
        element = layui.element,
        table = layui.table,
        dtree = layui.dtree,
        layuiTableColumnEdit = layui.layuiTableColumnEdit


    table.on('tool(commonSql-table)', function (obj) {
        if (obj.event === 'copy') {
            window.copy(obj);
        }
    });
    table.on('tool(historySql-table)', function (obj) {
        if (obj.event === 'copy') {
            window.copy(obj);
        }
        if (obj.event === 'addCommon') {
            window.addCommon(obj);
        }
    });


    //创建一个编辑器
    var editIndex = layedit.build('LAY_demo_editor');
    var tabledata = '';
    var selectData = [];
    table.on('tool(dbtables)', function (obj) {//我给表格设置的lay-filter叫table
        var checkStatus = table.checkStatus('dbtables')//表格id，获取选中行
        var data = obj.data;
        switch (obj.event)//对lay-event的值，进行不同的判断
        {
            case 'look':
                console.log("111");
                var cnName = (data.chineseName != null) ? data.chineseName : '无';
                var title = '表名：' + data.tableName + '&nbsp;&nbsp;&nbsp;&nbsp;中文名：'
                    + cnName;
                var index = layer.open({
                    title: title,
                    id: 'detail',
                    type: 2,
                    shade: 0.2,
                    resize: false,
                    shadeClose: true,
                    area: ['100%', '100%'],
                    content: './detail.html?tableId=' + data.id + '&chineseName=' + data.chineseName + '&tableName=' + data.tableName,
                    success: function (layero) {
                        iframe_add = document.getElementById('layui-layer-iframe');
                    }
                });
                $(window).on("resize", function () {
                    layer.full(index);
                });

                break;
            case 'history':
                console.log("qwret")
                var tableName = obj.data.tableName;
                var chineseName = obj.data.chineseName;
                var url = '/data-order-server/jxStdDatabaseDicionaryLog/detail/' + data.id;
                var json = {};
                timelineshow(url, 'get', json, "timeDiv", tableName, chineseName);  //url为请求数据地址；json为参数json字符串；打三个参数为时间线显示位置标签id

                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: 0,
                    area: ['420px', '90%'],
                    top: 10,
                    shadeClose: true,
                    skin: 'yourclass',
                    content: $('#timeDiv'),
                    offset: "r"
                });


                break;

        }

    });

    table.on('tool(errorRec)', function (obj) {//我给表格设置的lay-filter叫table
        var checkStatus = table.checkStatus('errorRec')//表格id，获取选中行
        var data = obj.data;
        switch (obj.event)//对lay-event的值，进行不同的判断
        {
            case 'send':
                layer.confirm('确定推送该数据？', {
                    btn: ['是', '否'] //按钮
                }, function () {
                    layer.msg('的确很重要', {icon: 1});
                }, function () {
                });

                break;
            case 'difference':
                var index = layer.open({
                    title: '表差异',
                    id: 'detail',
                    type: 2,
                    shade: 0.2,
                    resize: false,
                    shadeClose: true,
                    area: ['100%', '100%'],
                    content: './difference.html?objData=' + encodeURI(JSON.stringify(obj.data)),
                    success: function (layero) {
                        iframe_add = document.getElementById('layui-layer-iframe');
                    }
                });
                break;
        }
    });


    $(document).on('click', '#refreshDbTables', function () {
        $("#columnName").val('');
        $("#tableName").val('');
        table.reload('dbtables', {
            page: {
                curr: 1 //重新从第 1 页开始
            }
            , where: {}
        }, 'data');
    });


    $(document).on('click', '#export', function () {

        layer.confirm('确定导出该库的数据字典？', {icon: 3, title: '提示'}, function (index) {
            layer.close(index);

            window.location.href = '/data-order-server/jxStdDatabaseDataDicionary/export/' + id;

        });
    });

    function RemoveChinese(strValue) {
        if (strValue != null && strValue != "") {
            var reg = /[\u4e00-\u9fa5]/g;
            return strValue.replace(reg, "");
        } else
            return "";
    }


    function getTime(nS) {
        var date = new Date(parseInt(nS) * 1000);
        var year = date.getFullYear();
        var mon = date.getMonth() + 1;
        var day = date.getDate();
        var hours = date.getHours();
        var minu = date.getMinutes();
        var sec = date.getSeconds();

        return year + '/' + mon + '/' + day + ' ' + hours + ':' + minu + ':' + sec;
    }

    $(document).on('click', '#queryQueRec', function () {
        var columnNameSelect = RemoveChinese($("#columnName option:selected").text());
        var tableNameSelect = RemoveChinese($("#tableName option:selected").text());
        table.reload('errorRec', {
            page: {
                curr: 1 //重新从第 1 页开始
            }
            , where: {
                databaseId: id,
                columnName: columnNameSelect,
                tableName: tableNameSelect,
                keyword: $('#queRecKeyword').val()
            }
        }, 'data');
    });


    $(document).on('click', '#queryDbtables', function () {

        table.reload('dbtables', {
            page: {
                curr: 1 //重新从第 1 页开始
            }
            , where: {
                databaseId: id,
                keyword: $('#keyword').val()
            }
        }, 'data');
    });


    $(document).on('click', '#refreshErrorRec', function () {
        console.log("111")
        $("#columnName").val('');
        $("#tableName").val('');

        table.reload('errorRec', {
            page: {
                curr: 1 //重新从第 1 页开始
            }
            , where: {}
        }, 'data');
    });

    $(document).on('click', '#refreshScanInfo', function () {

        table.reload('scanLog', {
            page: {
                curr: 1 //重新从第 1 页开始
            }
            , where: {}
        }, 'data');
    });


    $(document).on('click', '#save', function () {
        $.ajax({
            type: "post",
            url: "/data-order-server/jxStdDatabaseDataDicionary/save",
            dataType: 'text',
            cache: false,
            contentType: "application/json",
            data: JSON.stringify(updateData),
            success: function (redata) {
                layer.msg("操作成功!", {icon: 1, time: 2000});
                location.reload();
            },
            error: function (redata) {
                window.parent.postMessage('update_error-' + redata, '*');
            }
        });

    });

    $(document).on('click', '#reScan', function () {


        layer.confirm('确定重新扫描该数据库？', {icon: 3, title: '提示'}, function (index) {
            layer.close(index);

            $.ajax({
                url: '/data-order-server/jxStdDatabaseDataDicionary/reScan/' + dbId,
                contentType: "application/json;charset=UTF-8",
                async: true,
                dataType: 'json',
                type: 'get',
                success: function (result) {
                }
            })


            layer.alert('启动扫描成功', {
                btn: ['确定']
            }, function (index) {
                layer.close(index);
                location.reload();
            });


        });


    });

    window.getUrlParams = function (key) {
        //获取url参数
        var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)", "i");
        var url = decodeURI(window.location.search)
        var r = url.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }


    //判断id是否存在，初始化表单

    var queryParam = getUrlParams('keyword');

    $("#keyword").val(queryParam);
    var dataHierarchy = [];
    var updateData = [];


    $(document).ready(function () {
        if (id !== null && id !== undefined) {
            let dicionaryUrl = '/data-order-server/jxStdDatabaseDataDicionary/detail';
            // let queRecUrl =  '/jxStdDatabaseQuestionRecord/detail';
            let queRecUrl = '/data-order-server/jxStdDatabaseQuestionRecord/loadJxCompareVo';
            let scanInfoUrl = '/data-order-server/jxStdScanInfo/list';
            let dicRecUrl = '/data-order-server/jxStdDatabaseDataDicCRecord/detail';
            var tableSelects = [];


            var cols = table.render({
                elem: '#dbtables',
                url: dicionaryUrl,
                page: true,
                height: 'full',
                title: '列表',
                //height: '600px',
                method: 'get',
                where: {
                    databaseId: id,
                    keyword: queryParam
                },
                method: 'get',
                page: true,
                limit: 100,
                limits: [10, 20, 50, 100],
                even: true,
                request: {
                    pageName: 'page',
                    limitName: 'limit'
                },

                cols: [
                    [
                        {
                            field: 'id1',
                            type: 'numbers',
                            title: '序号',
                            width: '5%'
                        },
                        {
                            field: 'tableName',
                            title: '表名',
                            width: '28%'
                        },
                        {
                            field: 'chineseName',
                            title: '中文名（备注）',
                            width: '28%'
                        },
                        {
                            field: 'tableRowSize',
                            title: '数据行数',
                            width: '10%', templet: function (d) {
                                if (d.tableRowSize == undefined || d.tableRowSize == '') {
                                    return '未获取'
                                } else {
                                    return d.tableRowSize
                                }
                            }
                        },
                        {
                            field: 'tableRowChangeTime',
                            title: '最新变更时间',
                            width: '10%', templet: function (d) {
                                console.log(d.tableRowChangeTime);
                                if (d.tableRowChangeTime == undefined || d.tableRowChangeTime == '') {
                                    return '未获取'
                                } else {
                                    return d.tableRowChangeTime
                                }
                            }
                        },
                        {
                            field: 'detectionResult',
                            title: '检测结果	', templet: function (d) {
                                if (d.detectionResult == '1') {
                                    return '已变更'
                                } else if (d.detectionResult == '0') {
                                    return '未变更'
                                }
                            },
                            width: '10%'
                        },
                        {
                            title: '操作',
                            toolbar: '#dbtablesBar',
                            align: "center",
                            width: '10%'
                        }
                    ]
                ], done: function (res, curr, count) {

                },
                id: 'dbtables'
            }).config.cols;


            //下拉框输入值改变时触发 给表格赋值
            form.on('select(entityName)', function (data) {
                var elem = $(data.elem);
                var trElem = elem.parents('tr');
                var index = trElem.data('index') + (page - 1) * 10;
                materialdata[index]['entityName'] = elem.find('option:selected').text();
                var selectIndex = elem.find('option:selected').index() - 1;
                materialdata[index]['logicalTable'] = selectData[selectIndex].tableName;
                materialdata[index]['businessClassfi'] = selectData[selectIndex].tableType;
                trElem[0].cells[4].innerText = selectData[selectIndex].tableName;
                var tableType = '模型未定义';
                if (selectData[selectIndex].tableType == '1') {
                    tableType = '维表';
                } else if (selectData[selectIndex].tableType == '0') {
                    tableType = '事实表';
                }
                trElem[0].cells[5].innerText = tableType;
                var flag = false;
                for (let i = 0; i < updateData.length; i++) {
                    if (updateData[i].id == materialdata[index].id) {
                        flag = true;
                        updateData[i] = materialdata[index]
                    }
                }
                if (flag) {
                } else {
                    updateData.push(materialdata[index]);
                }

            });

            table.on('edit(dbtables)', function (obj) {
                var value = obj.value //得到修改后的值
                    , data = obj.data //得到所在行所有键值
                    , field = obj.field; //得到字段


                var index = obj.tr[0].rowIndex;
                var flag = false;
                for (let i = 0; i < updateData.length; i++) {
                    if (updateData[i].id == materialdata[index].id) {
                        flag = true;
                        updateData[i]['illustrate'] = obj.value
                    }
                }
                if (flag) {
                } else {
                    updateData.push(materialdata[index]);
                }

            });

            var errorRecTable = table.render({
                elem: '#errorRec',
                id: 'errorRec',
                url: queRecUrl,
                page: true,
                height: 'full',
                title: '列表',
                method: 'get',
                where: {
                    databaseId: id,
                },
                method: 'get',
                //  page: true,
                limit: 100,
                limits: [10, 20, 50, 100],
                // even: true,
                request: {
                    pageName: 'page',
                    limitName: 'limit'
                },
                cols: [
                    [
                        {
                            field: 'id233',
                            type: 'numbers',
                            title: '序号',
                        },
                        {
                            field: 'tableName',
                            title: '表名', width: '22%'
                        },
                        {
                            field: 'columnName',
                            title: '字段名', width: '21%'
                        },
                        {
                            field: 'columnType',
                            title: '变更类型', width: '6%'
                        },
                        {field: 'type', title: '类型', width: '6%'},
                        {field: 'length', title: '长度', width: '6%'},
                        {
                            field: 'isPrimary', title: '是否主键', width: '6%', templet: function (d) {
                                if (d.isPrimary == '0') {
                                    return '是'
                                } else if (d.isPrimary == '1') {
                                    return '否'
                                } else {
                                    return '';
                                }
                            }
                        },

                        {
                            field: 'isEmpty', title: '是否可为空', width: '6%', templet: function (d) {
                                if (d.isEmpty == '0') {
                                    return '是'
                                } else if (d.isEmpty == '1') {
                                    return '否'
                                } else {
                                    return '';
                                }

                            }
                        },
                        {
                            field: 'defaultValue',
                            title: '默认值', width: '6%', templet: function (d) {
                                if (d.defaultValue == 'null') {
                                    return ''
                                } else {
                                    return d.defaultValue;
                                }

                            }
                        },


                        {
                            field: 'createTime',
                            title: '时间', width: '7%',
                            templet: function (d) {

                                return getTime(d.createTime / 1000);

                            }


                        },
                        {
                            title: '是否最新记录',
                            align: "center", width: '6%',
                            templet: function (d) {
                                if (d.isNewest == '1') {
                                    return "是";
                                } else {
                                    return "否";
                                }

                            }
                        },
                        {
                            title: '是否关联工单',
                            align: "center", width: '6%',
                            templet: function (d) {
                                if (d.orderId != null && d.orderId != '') {
                                    return " <a   style='color: blue'  onclick=orderDetail('" + d.orderId + "')>工单详情</a>   ";
                                } else {
                                    return "否";
                                }
                            }
                        },
                    ]
                ], done: function (res, curr, count) {
                    console.log(res);
                    console.log(curr);
                    console.log(count);

                    //   $(".layui-table-body, .layui-table-box, .layui-table-cell").css('overflow', 'visible');
                }
            }).config.cols;


            table.on('tool(scanLog)', function (obj) {
                var info = obj.data.status;
                console.log();

                    layer.alert(info , {
                        btn: ['确定']
                    }, function (index) {
                        layer.close(index);

                    });


            });


            var scanLog = table.render({
                elem: '#scanLog',
                id: 'scanLog',
                url: scanInfoUrl,
                page: true,
                height: 'full',
                title: '列表',
                method: 'get',
                where: {
                    databaseId: id,
                },
                method: 'get',
                //  page: true,
                limit: 100,
                limits: [10, 20, 50, 100],
                even: true,
                request: {
                    pageName: 'page',
                    limitName: 'limit'
                },
                cols: [
                    [
                        {
                            field: 'id2',
                            type: 'numbers',
                            title: '序号',
                        },
                        {
                            field: 'createtime',
                            title: '扫描时间', width: '10%'
                        },
                        {
                            field: 'info',
                            title: '状态', width: '8%',
                            templet: function (d) {
                                if (d.info == '1') {
                                    return "成功";
                                } else {
                                    return "失败";
                                }

                            }
                        },
                        /* {
                             field: 'status',
                             title: '状态', width: '8%'
                         },*/

                        {
                            field: 'insertdicrecords',
                            title: '表增加数', width: '14%'
                        },
                        /*  {
                              field: 'updatedicrecords',
                              title: '表修改数', width: '10%'
                          },*/
                        {
                            field: 'deletedicrecords',
                            title: '表删除数', width: '14%'
                        },
                        {
                            field: 'insertcolumnsrecord',
                            title: '字段增加数', width: '14%'
                        },
                        {
                            field: 'updatecolumnsrecord',
                            title: '字段修改数', width: '14%'
                        },
                        {
                            field: 'deletecolumnsrecord',
                            title: '字段删除数', width: '14%'
                        },

                        {

                            title: '操作' , templet: function (d) {
                                var str = '';
                                if (d.info != '1') {
                                    str = ' <button sec:authorize="hasPermission(\'/data-order-server/system/historySql/edit\',\'system:historySql:edit\')"\n' +
                                        '                  class="pear-btn pear-btn-primary pear-btn-sm" lay-event="info"> 查看错误日志 \n' +
                                        '          </button>\n' ;
                                }

                                return str;
                            }
                        }
                    ]
                ], done: function (res, curr, count) {
                    $(".layui-table-body, .layui-table-box, .layui-table-cell").css('overflow', 'visible');
                }
            }).config.cols;


        } else {
        }

    });

    var tabChangeTime = 0;
    element.on('tab(docDemoTabBrief)', function (data) {
        if (tabChangeTime < 1) {
            if (data.index == '0') {

                //  getDistinctTableName();
                //  var tableName = '';
                //  getDistinctColumnName(tableName);
            } else if (data.index == '1') {
                getDistinctTableName();
                var tableName = '';
                getDistinctColumnName(tableName);
            }
        }
        tabChangeTime++;
    });


    window.orderDetail = function (orderId) {
        console.log(orderId)
        layer.open({
            type: 2,
            title: '工单详情',
            shade: 0.1,
            area: ['100%', '100%'],
            content: '../../orders/detail.html?id=' + orderId
        });
    }


    function getMaterislType() {
        $.ajax({
            type: "get",
            contentType: "application/json;charset=utf-8",
            url: '/sqlManager/modelTargetTableAndColumn/listModelTargetTableByNoPage?dataSourceId=' + id,
            async: false,
            dataType: "json",
            success: function (data) {
                //  data = data.data;
                selectData = data;
                if (data != null) {
                    var html = "<option value=''>" + "</option>";
                    $.each(data, function (index, item) {
                        html += "<option value='" + item.tableId + "'>" + item.tableName + ' ' + item.tableRenark + "</option>";
                    });
                    $('.entityName').html(html);
                    form.render('select');

                }
            }
        });
    }


    var distinctTableNameList;
    var distinctColumnName;

    function getDistinctTableName() {
        $.ajax({
            type: "get",
            contentType: "application/json;charset=utf-8",
            url: '/data-order-server/jxStdDatabaseQuestionRecord/listDistinctTableNameByDbId?databaseId=' + id,
            async: false,
            dataType: "json",
            success: function (data) {
                distinctTableNameList = data;
                //  var list = data;
                var select = document.getElementById('tableName');
                $("#tableName").empty();

                var option = document.createElement("option");
                option.setAttribute("value", null);
                option.innerText = '请选择表名';
                select.appendChild(option);

                if (distinctTableNameList != null || distinctTableNameList.size() > 0) {
                    for (var c in distinctTableNameList) {
                        var option = document.createElement("option");
                        option.setAttribute("value", distinctTableNameList[c].id);
                        option.innerText = distinctTableNameList[c].tableName;
                        select.appendChild(option)
                    }
                }
                ;
                form.render('select', 'tableName');
            }
        });

    }

    function getDistinctColumnName(tableName) {
        $.ajax({
            type: "get",
            contentType: "application/json;charset=utf-8",
            url: '/data-order-server/jxStdDatabaseQuestionRecord/listDistinctColumnNameByDbIdAndTbaleName?databaseId='
                + id + '&tableName=' + tableName,
            async: false,
            dataType: "json",
            success: function (data) {
                distinctColumnName = data;

                // var list = data;
                var select = document.getElementById('columnName');
                $("#columnName").empty();

                var option = document.createElement("option");
                option.setAttribute("value", null);
                option.innerText = '请选择字段名';
                select.appendChild(option);

                if (distinctColumnName != null || distinctColumnName.size() > 0) {
                    for (var c in distinctColumnName) {
                        var option = document.createElement("option");
                        option.setAttribute("value", distinctColumnName[c].id);
                        option.innerText = distinctColumnName[c].columnName;
                        select.appendChild(option)
                    }
                }
                ;
                form.render('select', 'columnName');

            }
        });

    }


    //接收来自父页面的点击信息，触发提交按钮点击事件
    window.addEventListener('message', function (event) {
        if (event.data === 'click') {
            $("#yes").trigger("click");
        }
        if (event.data === 'test') {
            $("#test").trigger("click");
        }
    });
    //接收来自父页面的点击信息，触发测试连接按钮点击事件
    // window.addEventListener('message', function(event) {
    //
    // });

    //监听测试
    form.on('submit(test)', function (data) {
        $.ajax({
            type: "post",
            url: FLOW_URL5 + "/strategy/checkConnection",
            dataType: 'json',
            cache: false,
            contentType: "application/json",
            data: JSON.stringify(data.field),
            success: function (redata) {
                if (redata.msg === '数据源测试连接成功') {
                    window.parent.postMessage('test_success-' + redata.msg, '*');
                } else window.parent.postMessage('test_error-' + redata.msg, '*');
            },
            error: function (redata) {
                window.parent.postMessage('test_error-' + redata.msg, '*');
            }
        });
        return false;

    });
    //监听提交
    form.on('submit(submit)', function (data) {
        var sourceObj = document.getElementById('sourceId');
        var sourceIndex = sourceObj.selectedIndex;
        var sourceName = sourceObj[sourceIndex].text;
        data.field['source'] = sourceName;

        return false;
    })
});

function getData(data, array) {
    var childrenArray = getChildrenData(data.id, array);
    data.children = childrenArray;
    for (var j in childrenArray) {
        getData(childrenArray[j].id, array);
    }
    return data;
}

function getChildrenData(id, array) {
    var newArray = [];
    for (var i in array) {
        if (array[i].parentId === id) {
            newArray.push(array[i]);
        }
    }
    return newArray;
}

function removePid(data) {
    var temp = data.map(item => {
        var children = item.children;
        if (children.length === 0) {
            return {
                name: item.myName,
                // value:item.myName,
                value: item.id,
                children: item.children
            }
        } else {
            return {
                name: item.myName,
                // value:item.myName,
                value: item.id,
                children: removePid(item.children)
            }
        }

    });
    return temp;
}
