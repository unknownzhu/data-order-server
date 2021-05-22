//  菜单显示异常修改tinymce/skins/ui/oxide/skin.min.css:96 .tox-silver-sink的z-index值
//  http://tinymce.ax-z.cn/   中文文档

var xhrOnProgress = function (fun) {
    xhrOnProgress.onprogress = fun;
    return function () {
        var xhr = $.ajaxSettings.xhr();
        if (typeof xhrOnProgress.onprogress !== 'function')
            return xhr;
        if (xhrOnProgress.onprogress && xhr.upload) {
            xhr.upload.onprogress = xhrOnProgress.onprogress;
        }
        return xhr;
    }
}
layui.define(['jquery'], function (exports) {
    var $ = layui.$

    var modFile = layui.cache.modules['tinymce'];

    var modPath = modFile.substr(0, modFile.lastIndexOf('.'))

    var setter = layui.setter || {}

    var response = setter.response || {}

    var settings = {
        base_url: modPath
        , images_upload_url: '/system/file/upload'//图片上传接口
        , language: 'zh_CN'
        , response: {
            statusName: response.statusName || 'code'//返回状态字段
            , msgName: response.msgName || 'msg'//返回消息字段
            , dataName: response.dataName || 'data'//返回的数据
            , statusCode: response.statusCode || {
                ok: 0//数据正常
            }
        }
        , success: function (res, succFun, failFun) {//上传完成回调
            if (res[this.response.statusName] == this.response.statusCode.ok) {
                console.log('/static/' + res[this.response.dataName]);
                succFun('/static/' + res[this.response.dataName]);
            } else {
                failFun(res[this.response.msgName]);
            }
        }
    };

    var t = {};

    t.render = function (option) {

        var admin = layui.admin || {}

        option.font_formats = "微软雅黑='微软雅黑';宋体='宋体';黑体='黑体';仿宋='仿宋';楷体='楷体';隶书='隶书';幼圆='幼圆';";
       //  +   "Andale Mono=andale mono,times;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;Comic Sans MS=comic sans ms,sans-serif;Courier New=courier new,courier;Georgia=georgia,palatino;Helvetica=helvetica;Impact=impact,chicago;Symbol=symbol;Tahoma=tahoma,arial,helvetica,sans-serif;Terminal=terminal,monaco;Times New Roman=times new roman,times;Trebuchet MS=trebuchet ms,geneva;Verdana=verdana,geneva;Webdings=webdings;Wingdings=wingdings",

        option.base_url = option.base_url ? option.base_url : settings.base_url

        option.language = option.language ? option.language : settings.language

        option.selector = option.selector ? option.selector : option.elem

        option.quickbars_selection_toolbar = option.quickbars_selection_toolbar ? option.quickbars_selection_toolbar : 'cut copy | bold italic underline strikethrough '

        option.plugins = option.plugins ? option.plugins : 'quickbars print preview searchreplace autolink fullscreen image upfile link media codesample table charmap hr advlist lists wordcount imagetools indent2em';

        option.toolbar = option.toolbar ? option.toolbar : 'undo redo | forecolor backcolor bold italic underline strikethrough | indent2em alignleft aligncenter alignright alignjustify outdent indent | upfile link bullist numlist image table codesample | formatselect fontselect fontsizeselect';
        option.toolbar_groups ={
            formatting: {
                text: '文字格式',
                tooltip: 'Formatting',
                items: 'bold italic underline | superscript subscript',
            },
            alignment: {
                icon: 'align-left',
                tooltip: 'alignment',
                items: 'alignleft aligncenter alignright alignjustify',
            }
        };
        // option.table_style_by_css=true;
        // option.OperationManualHtml='';
        // option.CommonProblemHtml='';
        // option.table_style_by_css=true;
        option.file_picker_types= 'file';

        // option.powerpaste_word_import=  "clean"; // 是否保留word粘贴样式  clean | merge
        // option.powerpaste_html_import=  'clean'; // propmt, merge, clean
        // option.powerpaste_allow_local_images=  true;//
        // option.powerpaste_keep_unsupported_src= true;
        // option.paste_data_images=  true;
        // option.toolbar_sticky=  false;
        // option.autosave_ask_before_unload=  false;


        option.resize = false;

        option.elementpath = false

        option.branding = false;

        option.contextmenu_never_use_native = true;

        option.menubar = false;

        option.images_upload_url = option.images_upload_url ? option.images_upload_url : settings.images_upload_url;

        option.images_upload_handler = function (blobInfo, succFun, failFun) {

            var formData = new FormData();

            //  formData.append('target', 'edit');


            formData.append('filePath', '');
            formData.append('file', blobInfo.blob());

            var ajaxOpt = {

                url: option.images_upload_url,

                dataType: 'json',

                type: 'POST',

                data: formData,

                processData: false,

                contentType: false,

                success: function (res) {
                    settings.success(res, succFun, failFun)

                },
                error: function (res) {

                    failFun("网络错误：" + res.status);

                }
            };

            if (typeof admin.req == 'function') {

                admin.req(ajaxOpt);

            } else {

                $.ajax(ajaxOpt);

            }
        }

        option.menu = option.menu ? option.menu : {
            file: {title: '文件', items: 'newdocument | print preview fullscreen | wordcount'},
            edit: {title: '编辑', items: 'undo redo | cut copy paste pastetext selectall | searchreplace'},
            format: {
                title: '格式',
                items: 'bold italic underline strikethrough superscript subscript | formats | forecolor backcolor | removeformat'
            },
            table: {title: '表格', items: 'inserttable tableprops deletetable | cell row column'},
        };


        option.file_picker_callback = function (succFun, value, meta) {
            Window.fileNames = ''
            var filetype = '.pdf, .txt, .zip, .rar, .7z, .doc, .docx, .xls, .xlsx, .ppt, .pptx, .mp3, .mp4';
            var input = document.createElement('input');
            input.setAttribute('type', 'file');
            input.setAttribute('accept', filetype);
            input.click();
            // input.onchange = function() {
           input.oninput = function() {
                var file = this.files[0];
                Window.fileNames = file.name
                var data = new FormData();
                data.append("file", file);
                $.ajax({
                    data: data,
                    type: 'post',
                    url: '/system/file/upload',
                    header:{'Content-Type':'multipart/form-data'},
                    cache: false,
                    contentType: false,
                    processData: false,
                    dataType: 'json',
                    xhr: xhrOnProgress(function (e) {
                        var percent = (e.loaded / e.total * 100 | 0) + '%';//计算百分比
                        // progressCallback(percent);
                    }),
                }).then(function (data) {
                    if ( data.code== 0) {
                        //succFun('test');
                        succFun('/static/' +  data.data,{ text: Window.fileNames  });
                    }
                }).fail(function (error) {
                    failFun('上传失败:' + error.message)
                });
            }
        }
        option.file_callback = function (file, succFun) {
            var data = new FormData();
            data.append("file", file);
            $.ajax({
                data: data,
                type: 'post',
                url: '/system/file/upload',
                cache: false,
                contentType: false,
                processData: false,
                header:{'Content-Type':'multipart/form-data'},
                dataType: 'json',
                xhr: xhrOnProgress(function (e) {
                    var percent = (e.loaded / e.total * 100 | 0) + '%';//计算百分比
                    // progressCallback(percent);
                }),
            }).then(function (data) {
                if ( data.code== 0) {
                    succFun('/static/' + data.data,{text: file.name});
                }
            }).fail(function (error) {
                // failFun('上传失败:' + error.message)
            });
        }

        $.ajax({//获取插件
            url: option.base_url + '/tinymce.js',

            dataType: 'script',

            cache: true,

            async: false,
        });

        tinymce.init(option);

        t.tinymce = tinymce;

        return tinymce.activeEditor;
    };
    exports('tinymce', t);
});