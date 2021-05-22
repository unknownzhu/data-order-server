layui.use(['table', 'layer', 'form'], function () {
    var $ = layui.jquery, layer = layui.layer, form = layui.form;
//删除
    $(document).on('click', '#del', function () {
        var checkStatus = table.checkStatus('static.views.list')
            , data = checkStatus.data;
        if (data.length == 0) {
            layer.msg("当前未选中一行");
        } else {
            layer.alert(JSON.stringify(data));
        }
    });
    //新增
    $(document).on('click', '#addTradition', function () {
        openPage('新增担保业务', 'add.html', '65%', '65%', '')
    });
    /**
     * 通用弹窗
     * @param title 标题
     * @param url   链接
     * @param width 宽度
     * @param height高度
     * @param data  数据
     * @param shade 遮罩层
     */
    window.openPage = function (title, url, width, height, data, shade) {
        layer.open({
            type: 2
            , title: title
            , content: url
            , maxmin: true
            , shade: shade
            , area: [width, height]
            , data: data
            /*            , success: function (layero, index) {
                            if (data != null || data != "") {
                                // 获取子页面的iframe
                                var iframe = window['layui-layer-iframe' + index];
                                // 向子页面的全局函数child传参
                                iframe.child(data);
                            }
                        }*/
            /*,btn: ['确定', '取消']
            ,yes: function(index, layero){
                //点击确认触发 iframe 内容中的按钮提交
                layer.msg( '确定');
                var submit = layero.find('iframe').contents().find("#layuiadmin-app-form-submit");
                submit.click();
            }*/
        });

    }
    window.getUrlParam = function (key) {
        //获取url参数
        var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)", "i");
        var url = decodeURI(window.location.search)
        var r = url.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }

    /**
     * Json拼接
     * @param des
     * @param src
     * @param override
     * @returns {*}
     */
    window.extend = function (des, src, override) {
        if (src instanceof Array) {
            for (var i = 0, len = src.length; i < len; i++)
                extend(des, src[i], override);
        }
        for (var i in src) {
            if (override || !(i in des)) {
                des[i] = src[i];
            }
        }
        return des;
    }
    /**
     * select动态添加节点
     * @param url 数据接口
     * @param data 不调用接口，直接使用数据渲染
     * @param controlid 控件ID
     * @param levels 1为普通下拉框，2为联动下拉框，需将原数据清除
     * @param op_id 返回数据中的ID
     * @param op_name   返回数据中心的name
     */
    window.addOption = function (url, data, controlid, levels, op_id, op_name) {
        var str = "";
        var datas = data;
        if (url != null || url != undefined) {
            datas = request.get(url).data;
            if (levels == 2) {
                $(controlid).empty();
            }
        }
        for (var i = 0; i < datas.length; i++) {
            if (op_id == null || op_name == null) {
                str += "<option value='" + datas[i].id + "'>" + datas[i].name + "</option>"
            } else {
                str += "<option value='" + datas[i]["" + op_id + ""] + "'>" + datas[i]["" + op_name + ""] + "</option>"
            }
        }
        $(controlid).append(str);
        form.render("select");
    }

    /**
     * 数组转换成layui tree中需要的数据
     * @param arrayList
     * @param root
     * @param op_id 属性ID
     * @param op_name 属性名称
     * @returns {Array}
     */
    window.formatTreeJson = function (arrayList, root, op_id, op_name) {
        var treeList = [];
        if (arrayList != null && arrayList.length > 0) {

            for (var i = 0; i < arrayList.length; i++) {
                if (arrayList[i].parentId == null) {
                    arrayList[i].parentId = "";
                }
                var node = {};
                node.id = arrayList[i][op_id];
                node.name = arrayList[i][op_name];
                node.tags = arrayList[i];
                node.spread = true,
                    node.children = [];
                if (node.tags.parentId == root.tags.id) {
                    root.children.push(node);
                    formatTreeJson(arrayList, node, op_id, op_name);
                }
            }
            treeList.push(root);
        }
        return treeList;

    };
})
;