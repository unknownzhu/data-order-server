function timelineshow(url,type,json,div,tableName,chineseName){
    $.ajax({
        url: url,
        type: type,
        data: json,
        dataType: "json",
        success: function (res) {
            console.log(res);
            if(res.success==true){

                var list = res.data;
                console.log(list);


                var uls = "<ul class=\"layui-timeline\">";
                var uls1 = "<ul>";
                var uls2 = "</ul>";
                var lis = "<li class=\"layui-timeline-item\">";
                var lis1 = "<li>";
                var lis2 = "</li>";
                var is = "<i class=\"layui-icon layui-timeline-axis\"></i>";
                var divs = "<div class=\"layui-timeline-content layui-text\">";
                var divs2 = "</div>";
                var h3s = "<h3 class=\"layui-timeline-title\">";
                var h3s2 = "</h3>";
                var ps = "<p>";
                var ps2 = "</p>";
                var br = "</br>";

                if(list.length>0){
                    var content1 = "";
                    content1 = content1+uls;
                    for(var i=0; i<list.length; i++){
                        var content2 = "";
                        content2 = content2+lis+is+divs;
                        if(list[i].createTime!=null&&list[i].createTime!=''){
                            content2 = content2+h3s+createTime(list[i].createTime)+h3s2
                        }
                        if(list[i].type!=null){
                            var str ='变更类型：';
                            if (list[i].type == '0') {
                                str += '修改';
                            }
                            content2 = content2+ps+str+ps2;
                        }

                        if(list[i].remark!=null&&list[i].remark!=''){

                            content2 = content2+uls1;
                            content2 = content2+lis1+list[i].remark+lis2;
                            content2 = content2+uls2;
                        }
                        //可扩展
                        content2 = content2 + divs2+lis2;
                        content1 =content1+content2;
                    }
                    content1 = content1 +uls2;

                    //再跟你想追加的代码加到一起插入div中

                    //title
                    if (chineseName == '' || chineseName == null || chineseName == 'null') {
                        chineseName = '(中文名注释未填写)'
                    }
                    content1 = "<div style='margin: 10px'>" +"<span style='font-size: 20px'>"+tableName+"</br>"+ chineseName+" </span></br></br> "+ content1;
                    content1 += "</div>";
                    document.getElementById(div).innerHTML = content1;
                }
            }else {
                layer.msg(res.msg);
            }
        }

    });
}

function createTime(v){
    var date = new Date(v);
    var y = date.getFullYear();
    var m = date.getMonth()+1;
    m = m<10?'0'+m:m;
    var d = date.getDate();
    d = d<10?("0"+d):d;
    var h = date.getHours();
    h = h<10?("0"+h):h;
    var ms = date.getMinutes();
    ms = ms<10?("0"+ms):ms;
    var s = date.getSeconds();
    s = s<10?("0"+s):s;
    var str = y+"-"+m+"-"+d+" "+h+":"+ms+":"+s;
    return str;
}