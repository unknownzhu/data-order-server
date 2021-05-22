layui.config({
    base: 'module/'
}).extend({
  //  dtree: '{/}../../../lib/layui_ext/dtree/dtree',

    dtree: '{/}../../../component/lib/layui_ext/dtree/dtree',
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

    var changeRows = {};
    //创建一个编辑器
    var editIndex = layedit.build('LAY_demo_editor');


    $(document).on('click', '#history', function () {

        console.log("11");

        var url =  '/data-order-server/jxStdDatabaseDicionaryLog/detail/' + tableId;
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
    });


    $(document).on('click', '#save', function () {
        $.ajax({
            type: "post",
            url:  "/data-order-server/jxStdDatabaseDataDicColum/save",
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


    window.getUrlParams = function (key) {
        //获取url参数
        var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)", "i");
        var url = decodeURI(window.location.search)
        var r = url.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }


    //判断id是否存在，初始化表单
    var tableId = getUrlParams("tableId");
    var tableName = getUrlParams("tableName");
    var chineseName = getUrlParams("chineseName");
    var tmpData = JSON.parse(getUrlParams("objData"));
    var modelData = tmpData;
    var ttt = [];
    for (i in tmpData) {
        modelData[i] = tmpData['m' + i];
    }
    modelData.compareType = 'model';
    var objData = JSON.parse(getUrlParams("objData"));
    objData.compareType = 'obj';
    ttt.push(JSON.parse(getUrlParams("objData")));
    ttt.push(modelData);
    ttt[0].source = '实体表';
    ttt[1].source = '模型表';

    var dataHierarchy = [];
    var updateData = [];
    $(document).ready(function () {

        let url =  '/data-order-server/jxStdDatabaseDataDicColum/loadByTableId/' + tableId;

        var cols = table.render({
            elem: '#compare',
            id: 'compare',
            data: ttt,
            height: 'full',
            title: '列表',
            method: 'get',
            method: 'get',
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
                        title: '表结构来源', templet: function (d) {
                            return d.source;
                        }
                    },
                    {field: 'name', title: '字段名（不区分大小写）', width: '15%'},
                    {field: 'chineseName', title: '中文名（不参与对比）', width: '20%'},
                    {field: 'type', title: '类型'},
                    {field: 'length', title: '长度'},
                    {
                        field: 'isPrimary', title: '是否主键', templet: function (d) {
                            if (d.isPrimary == '0') {
                                return '是'
                            } else if (d.isPrimary == '1') {
                                return '否'
                            }else{
                                return '';
                            }
                        }
                    },
                    {
                        field: 'isEmpty', title: '是否可为空', templet: function (d) {
                            if (d.isEmpty == '0') {
                                return '是'
                            } else if (d.isEmpty == '1') {
                                return '否'
                            }else{
                                return '';
                            }

                        }
                    },
                    {field: 'defaultValue', title: '默认值'},
                ]
            ], done: function (res, curr, count) {
                console.log(412412);

                materialdata = res.data;
                $(".layui-table-body, .layui-table-box, .layui-table-cell").css('overflow', 'visible');


            }
        }).config.cols;
    });

/*    $(document).on('click', '#send', function () {
        table.reload('errorRec', {
            page: {
                curr: 1 //重新从第 1 页开始
            }
            , where: {}
        }, 'data');
    });*/

    $(document).on('click', '#refresh', function () {

        location.reload();
    });




    //加载数据来源
    function datasourceInit(datasource) {
        $.ajax({
            type: 'get',
            url:  '/manager/datasourceInfo/sourcelist',
            contentType: "application/json;charset=UTF-8",
            async: true,
            success: function (redata) {
                var res = redata.data;

                $.each(res, function (index, item) {
                    $('#sourceId').append(new Option(item.comeFrom, item.id));
                });
                form.render('select');
                if (id !== null && id !== undefined) {
                    form.val('datasourceConfig', {
                        "sourceId": datasource.data.sourceId,
                        "source": datasource.data.source
                    });
                }
            }

        });

    }


    //数据分层下拉树
    function dataHierachyTree(dataHierachy, datasource) {
        $.ajax({
            type: 'get',
            url:  '/manager/dataHierarchy/listLog',
            contentType: "application/json;charset=UTF-8",
            async: true,
            success: function (redata) {
                var temp = [];
                var res = redata.data;
                //初始化data，为data添加children
                var y = 0;
                for (var i = 0; i < res.length; i++) {
                    var data = getData(res[i], res);
                    if (data.parentId == null) {
                        temp.push(data);
                        y++;
                    }
                }
                dataHierarchy = removePid(temp);
                // 加载数据分层
                /* var dataHierarchyTree = xmSelect.render({
                     el: '#dataTiering',
                     name: 'dataTiering',
                     direction: 'down',
                     model: {
                         label: {
                             type: 'text'
                         }
                     },
                     radio: true,
                     clickClose: true,
                     tree: {
                         show: true,
                         strict: false,
                         expandedKeys: [-1],
                     },
                     height: 'auto',
                     data() {
                         return dataHierarchy
                     }
                 });*/
                if (id !== null && id !== undefined) {
                    dataHierarchyTree.setValue([datasource.data.dataTiering]);
                }
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
