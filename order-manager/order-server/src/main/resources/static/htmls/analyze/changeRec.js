layui.config({
    base: 'module/'
}).extend({
    dtree: '{/}../../../lib/layui_ext/dtree/dtree',
    layuiTableColumnEdit: '/layuiTableColumnEdit',
}).use(['element', 'layer', 'form', 'layedit', 'jquery', 'table', 'dtree'], function () {
    var $ = layui.jquery,
        layer = layui.layer,
        form = layui.form,
        layedit = layui.layedit,
        element = layui.element,
        table = layui.table,
        dtree = layui.dtree,
        layuiTableColumnEdit = layui.layuiTableColumnEdit

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
                var tableName = obj.data.tableName;
                var chineseName = obj.data.chineseName;
                var url =  '/data-order-server/jxStdDatabaseDicionaryLog/detail/' + data.id;
                var json = {};
                timelineshow(url, 'get', json, "timeDiv", tableName, chineseName);//url为请求数据地址；json为参数json字符串；打三个参数为时间线显示位置标签id

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
                layer.msg('确定推送该数据？', {icon: 3, time: 2000});

                break;

        }

    });


    $(document).on('click', '#refreshDbTables', function () {
        var url = window.location.href;
        if (url.indexOf("&keyword") != -1) {
            url = url.substr(0, url.indexOf('&keyword'))
        }
        location.replace(url)
    });


    $(document).on('click', '#queryDbtables', function () {
        var url = window.location.href;
        if (url.indexOf("&keyword") != -1) {
            url = url.substr(0, url.indexOf('&keyword'))
        }
        url += '&keyword=' + $('#keyword').val();
        location.replace(url)
    });


    $(document).on('click', '#queryQueRec', function () {
        table.reload('errorRec', {
            page: {
                curr: 1 //重新从第 1 页开始
            }
            , where: {
                keyword: $('#queRecKeyword').val()
            }
        }, 'data');
    });

    $(document).on('click', '#refreshErrorRec', function () {
        table.reload('errorRec', {
            page: {
                curr: 1 //重新从第 1 页开始
            }
            , where: {
                key: {}
            }
        }, 'data');
    });


    $(document).on('click', '#save', function () {
        $.ajax({
            type: "post",
            url:  "/data-order-server/jxStdDatabaseDataDicionary/save",
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
        var loading = layer.load(0, {
            shade: false,
            time: 2 * 1000
        });

        $.ajax({
            type: 'get',
            url:  '/data-order-server/jxStdDatabaseDataDicionary/reScan/' + id,
            contentType: "application/json;charset=UTF-8",
            async: true,
            success: function (redata) {
                layer.close(loading);
                location.reload();
                layer.msg("操作成功!", {icon: 1, time: 2000});
            }
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
    var id = getUrlParams("dbId");
    var dataHierarchy = [];
    var updateData = [];

    $(document).ready(function () {
        if (id !== null && id !== undefined) {

            let dicionaryUrl =  '/data-order-server/jxStdDatabaseDataDicionary/detail';
            let queRecUrl =  '/data-order-server/jxStdDatabaseQuestionRecord/detail';
            let dicRecUrl =  '/data-order-server/jxStdDatabaseDataDicCRecord/detail';
            var tableSelects = [];
            var cols = table.render({
                elem: '#dbtables',
                url: dicionaryUrl,
                page: true,

                height: 'full',
                title: '列表',
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
                            // field: 'id',
                            type: 'numbers',
                            title: '序号',
                        },
                        {
                            field: 'tableName',
                            title: '表名',
                            width: '20%'
                        },
                        {
                            field: 'chineseName',
                            title: '中文名',
                        },
                        {
                            field: "entityName",
                            title: "实体名", templet: '#selectTpl'
                        },

                        {
                            field: 'logicalTable',
                            title: '逻辑库表	',
                            width: '15%'
                        },
                        {
                            field: 'businessClassfi',
                            title: '业务分类	',
                        },
                        {
                            field: 'detectionResult',
                            title: '检测结果	', templet: function (d) {
                                if (d.detectionResult == '1') {
                                    return '已变更'
                                } else if (d.detectionResult == '0') {
                                    return '未变更\n'
                                }

                            }
                        },

                        {
                            field: 'illustrate',
                            title: '说明	',
                            edit: 'text'
                        },
                        {
                            title: '操作',
                            toolbar: '#dbtablesBar',
                            align: "center",

                        }
                    ]
                ], done: function (res, curr, count) {
                    //  $(".layui-table-body, .layui-table-box, .layui-table-cell").css('overflow', 'visible');
                    materialdata = res.data;
                    getMaterislType();
                    page = curr;
                    //表格重载的时候 回显下拉框的数据


                    //根据已有的值回填修理项目下拉框
                    layui.each($("select[name='RepairItemName']", ""), function (index, item) {
                        var elem = $(item);
                        elem.val(elem.data('value'));
                    });
                    form.render('select');
                    $('tr').each(function (e) {
                        var $cr = $(this);
                        var dataindex = $cr.attr("data-index");
                        $.each(res.data, function (index, value) {
                            if (value.LAY_TABLE_INDEX == dataindex) {
                                $cr.find('input').val(value.entityName);
                            }
                        });
                    });

                },
                id: 'dbtables'
            }).config.cols;
            //下拉框输入值改变时触发 给表格赋值
            form.on('select(type)', function (data) {
                var elem = $(data.elem);
                var trElem = elem.parents('tr');
                var index = trElem.data('index') + (page - 1) * 10;
                materialdata[index]['entityName'] = elem.find('option:selected').text();
                materialdata[index]['logicalTable'] = selectData[data.value].tableName;
                materialdata[index]['businessClassfi'] = selectData[data.value].businessCode;
                trElem[0].cells[4].innerText = selectData[data.value].tableName;
                trElem[0].cells[5].innerText = selectData[data.value].businessCode;
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
                page: true,
                limit: 100,
                limits: [10, 20, 50, 100],
                even: true,
                request: {
                    pageName: 'page',
                    limitName: 'limit'
                },

                cols: [
                    [/*{
                        type: "checkbox"
                    },*/
                        {
                            type: 'numbers',
                            title: '序号',
                        },
                        {
                            field: 'createTime',
                            title: '时间',
                        },
                        {
                            field: 'tableName',
                            title: '表名',
                        }, {
                        field: 'columnName',
                        title: '字段名',
                    },
                        {
                            field: 'type',
                            title: '问题类型',
                        },
                        {
                            title: '操作',
                            toolbar: '#errorRecBar',
                            align: "center",

                        }
                    ]
                ], done: function (res, curr, count) {

                    $(".layui-table-body, .layui-table-box, .layui-table-cell").css('overflow', 'visible');

                    //   getDistinctTableName();
                    //   getDistinctColumnName();

                }
            }).config.cols;

        } else {
        }


    });

    function renderTableSelect(res) {

    }

    function getMaterislType() {
        var few;
        $.ajax({
            type: "get",
            contentType: "application/json;charset=utf-8",
            url:  '/sqlManager/modelTargetTableAndColumn/listModelTargetTableByNoPage?dataSourceId=' + id,
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
                    $('.type').html(html);
                    form.render();
                }
            }
        });

    }


    function getDistinctTableName() {
        $.ajax({
            type: "get",
            contentType: "application/json;charset=utf-8",
            url:  '/data-order-server/jxStdDatabaseQuestionRecord/listDistinctTableNameByDbId?databaseId=' + id,
            async: false,
            dataType: "json",
            success: function (data) {
                var list = data;
                var select = document.getElementById('tableName');
                if (list != null || list.size() > 0) {
                    for (var c in list) {
                        var option = document.createElement("option");
                        option.setAttribute("value", list[c].id);
                        option.innerText = list[c].tableName;
                        select.appendChild(option)
                    }
                }
                ;
                form.render('select');

                var few;
            }
        });

    }

    // tableName

    function getDistinctColumnName() {
        $.ajax({
            type: "get",
            contentType: "application/json;charset=utf-8",
            url:  '/data-order-server/jxStdDatabaseQuestionRecord/listDistinctColumnNameByDbIdAndTbaleName?databaseId='
                + id + '&tableName=' + tableName,
            async: false,
            dataType: "json",
            success: function (data) {

                var list = data;
                var select = document.getElementById('columnName');
                if (list != null || list.size() > 0) {
                    for (var c in list) {
                        var option = document.createElement("option");
                        option.setAttribute("value", list[c].id);
                        option.innerText = list[c].columnName;
                        select.appendChild(option)
                    }
                }
                ;
                form.render('select');

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
