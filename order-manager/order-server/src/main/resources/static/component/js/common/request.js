var tokenId=Math.random();
jQuery.support.cors = true;
var i = 0;
var request = {
	getRealUrl : function(url)
	{
		var realUrl = url;
		if(realUrl.indexOf("?")==-1)
		{
			realUrl+="?tokenId=" + tokenId;
		}
		else
		{
			var urlArr = realUrl.split("?");
			realUrl = urlArr[0] + "?tokenId=" + tokenId + "&" + urlArr[1];
		}
		return realUrl;
	},
	post : function(url,data, process, async)
	{
		var data1;
		if(async==undefined){
			async = false;
		}
		var realUrl = this.getRealUrl(url);
		jQuery.support.cors = true;
 		$.ajax({
            type: "post",
            url: realUrl,
            timeout : 5000,
            contentType:"application/json",
            async: async,//使用同步方式，目前data组件有同步依赖
            data: data,
            success: function(data){
				
            	data1 = data;
            	if(process != undefined) process(data);
              },
            error: function(XMLHttpRequest, textStatus, errorThrown){
            	if(XMLHttpRequest){
            		var status = XMLHttpRequest.status;
            		if(status&&status=='401'){
            			location.replace(location.href);
            		}
            	}
            	 
            	/* var errortext = XMLHttpRequest.responseText;
				 var obj = eval('(' + errortext + ')');
				if(errortext.code == '1111'){
					loginout();
				}*/
            }
            
        });
 		
 		if(async==false)
 			return data1;
	},
	put : function(url,data, process, async)
	{
		var data1;
		if(async==undefined){
			async = false;
		}
		var realUrl = this.getRealUrl(url);
		jQuery.support.cors = true;
 		$.ajax({
            type: "put",
            url: realUrl,
            timeout : 5000,
            contentType:"application/json",
            async: async,//使用同步方式，目前data组件有同步依赖
            data: data,
            success: function(data){
				
            	data1 = data;
            	if(process != undefined) process(data);
              },
            error: function(XMLHttpRequest, textStatus, errorThrown){
            	/* var errortext = XMLHttpRequest.responseText;
				 var obj = eval('(' + errortext + ')');
				if(errortext.code == '1111'){
					loginout();
				}*/
            	if(XMLHttpRequest){
            		var status = XMLHttpRequest.status;
            		if(status&&status=='401'){
            			location.replace(location.href);
            		}
            	}
            }
            
        });
 		
 		if(async==false)
 			return data1;
	},
	postUserOrgs : function(url,data, process, async)
	{
		var data1;
		if(async==undefined){
			async = false;
		}
		var realUrl = this.getRealUrl(url);
		jQuery.support.cors = true;
 		$.ajax({
            type: "post",
            url: realUrl,
            timeout : 5000,
            traditional: true,
            async: async,//使用同步方式，目前data组件有同步依赖
            data: data,
            success: function(data){
				
            	data1 = data;
            	if(process != undefined) process(data);
              },
            error: function(XMLHttpRequest, textStatus, errorThrown){
            	/* var errortext = XMLHttpRequest.responseText;
				 var obj = eval('(' + errortext + ')');
				if(errortext.code == '1111'){
					loginout();
				}*/
            	if(XMLHttpRequest){
            		var status = XMLHttpRequest.status;
            		if(status&&status=='401'){
            			location.replace(location.href);
            		}
            	}
            }
            
        });
 		
 		if(async==false)
 			return data1;
	},
	get : function(url, data, process, async)
	{
		store.remove("userInfo");
					store.remove("logType");
					store.remove("name");
		var data1;
		
		if(async==undefined){
			async = false;
		}

		var realUrl = this.getRealUrl(url);
		jQuery.support.cors = true;
 		$.ajax({
            type: "GET",
            url: realUrl,
            timeout : 5000,
			cache:false,
            async: async,//使用同步方式，目前data组件有同步依赖
            data: data,
            success: function(data){
				 
            	data1 = data;
            	if(process != undefined) process(data);
              },
            error: function(XMLHttpRequest, textStatus, errorThrown){
				/* var errortext = XMLHttpRequest.responseText;
				 var obj = eval('(' + errortext + ')');
				if(errortext.code == '1111'){
					loginout();
				}*/
            	//console.log("加载失败");
            	if(XMLHttpRequest){
            		var status = XMLHttpRequest.status;
            		if(status&&status=='401'){
            			location.replace(location.href);
            		}
            	}
            }
            
        });
 		
 		if(async==false)
 		return data1;
	},
	
	get_async : function(url, process, async)
	{
		var data1;
		
		if(async==undefined){
			async = false;
		}
		
		var realUrl = url;
		jQuery.support.cors = true;
 		$.ajax({
            type: "GET",
            url: realUrl,
            dataType:"json",
			timeout : 5000,
			cache:false,
            contentType:"application/json",
            async: true,//使用同步方式，目前data组件有同步依赖
            success: function(data){
				
            	data1 = data;
            	if(process != undefined) process(data);
              },
            error: function(XMLHttpRequest, textStatus, errorThrown){
            	 /*var errortext = XMLHttpRequest.responseText;
				 var obj = eval('(' + errortext + ')');
				if(errortext.code == '1111'){
					loginout();
				}*/
            	if(XMLHttpRequest){
            		var status = XMLHttpRequest.status;
            		if(status&&status=='401'){
            			location.replace(location.href);
            		}
            	}
            },
			complete:function(XMLHttpRequest,status){			
				
				//console.log(status);
			}
            
        });
 		
 		if(async==false)
 		return data1;
	},
	
	getToken : function(url,tokenid, condition, process, async)
	{
		var data1;
		
		if(async==undefined){
			async = false;
		}
		
		var realUrl = url;
		jQuery.support.cors = true;
 		$.ajax({
            type: "post",
            url: realUrl,
            dataType:"json",
			timeout : 5000,
			cache:false,
            contentType:"application/json",
            async: true,//使用同步方式，目前data组件有同步依赖
            beforeSend:function (request){
            	request.setRequestHeader("Authorization",tokenid);
            },
            success: function(data){
				
            	data1 = data;
            	if(process != undefined) process(data,condition);
              },
            error: function(XMLHttpRequest, textStatus, errorThrown){
            	var object = store.get("userInfo", true);
            	$("#count_"+condition.id+"_"+store.get(object.userId+"_org_id")).show();
            	$("#count_"+condition.id+"_"+store.get(object.userId+"_org_id")).attr("title","接口不通");
            	 $("#count_"+condition.id+"_"+store.get(object.userId+"_org_id")).css("background","url('./img/jingtanhao.png')");
            },
			complete:function(XMLHttpRequest,status){			
				
				//console.log(status);
			}
            
        });
 		
 		if(async==false)
 		return data1;
	},
	getOaTask : function(url,tokenid, condition, process, async)
	{
		var data1;
		
		if(async==undefined){
			async = false;
		}
		
		var realUrl = url;
		jQuery.support.cors = true;
 		$.ajax({
            type: "GET",
            url: realUrl,
            dataType:"json",
			timeout : 5000,
			cache:false,
			data:{Authorization:tokenid},
            async: true,//使用同步方式，目前data组件有同步依赖
            success: function(data){
				
            	data1 = data;
            	if(process != undefined) process(data,condition);
              },
            error: function(XMLHttpRequest, textStatus, errorThrown){
            	 $("#i_"+condition.id).attr("style","background: url(img/error.png)");
            },
			complete:function(XMLHttpRequest,status){			
				
				//console.log(status);
			}
            
        });
 		
 		if(async==false)
 		return data1;
	},
	deleted : function(url, data, process, async)
	{
		store.remove("userInfo");
		store.remove("logType");
		store.remove("name");
var data1;

if(async==undefined){
async = false;
}

var realUrl = this.getRealUrl(url);
jQuery.support.cors = true;
$.ajax({
type: "delete",
url: realUrl,
timeout : 5000,
cache:false,
async: async,//使用同步方式，目前data组件有同步依赖
data: data,
success: function(data){
	 
	data1 = data;
	if(process != undefined) process(data);
  },
error: function(XMLHttpRequest, textStatus, errorThrown){
	/* var errortext = XMLHttpRequest.responseText;
	 var obj = eval('(' + errortext + ')');
	if(errortext.code == '1111'){
		loginout();
	}*/
	//console.log("加载失败");
	if(XMLHttpRequest){
		var status = XMLHttpRequest.status;
		if(status&&status=='401'){
			location.replace(location.href);
		}
	}
}

});

if(async==false)
return data1;
	}
};

function log(data,url){
	
	
	layer.open({
		type: 2,
	  	title: "操作日志",
	   	scrollbar: false, //父页面是否有滚动条
		maxmin: true,//是否显示最大化按钮
	  	shadeClose: false,  //点击其他区域关闭弹窗
	  	shade: 0.6,  //笼罩层透明度
	  	area: ['50%', '70%'],  //大小
	  	content: url,
	  	btn: [],
	  	btn1: function(index, layero){

	  		
	  	  },
		btn2: function(index, layero){
  			layer.closeAll();  
  	  		}
	});
}
function applyDate(value){
	var date = new Date(value);//时间戳为10位需*1000，时间戳为13位的话不需乘1000  
    Y = date.getFullYear() + '-';  
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';  
    D = (date.getDate() < 10 ? '0'+(date.getDate()) : date.getDate()) + ' ';  
    h = (date.getHours() < 10 ? '0'+(date.getHours()) : date.getHours()) + ':';  
    m = (date.getMinutes() < 10 ? '0'+(date.getMinutes()) : date.getMinutes()) + ':';  
    s = (date.getSeconds() < 10 ? '0'+(date.getSeconds()) : date.getSeconds());  
    return Y+M+D+h+m+s;
}
function applyDateTime(value){
	var date = new Date(value);//时间戳为10位需*1000，时间戳为13位的话不需乘1000  
    Y = date.getFullYear() + '-';  
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';  
    D = (date.getDate() < 10 ? '0'+(date.getDate()) : date.getDate()) + ' ';  
    return Y+M+D;
}

function toDecimal(x) {  
	   var f = parseFloat(x);  
	   if (isNaN(f)) {  
	    return;  
	   }  
	   f = Math.round(x*100)/100;  
	   return f;  
	  } 

function onKeyPrice(t) 
{ 
    var stmp = ""; 
    if(t.value==stmp)
    {
        return; 
    }
    var ms = t.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0"); 
    var txt = ms.split("."); 
    while(/\d{4}(,|$)/.test(txt[0])) 
    {
       txt[0] = txt[0].replace(/(\d)(\d{3}(,|$))/,"$1,$2"); 
    }
    t.value = stmp = txt[0]+(txt.length>1?"."+txt[1]:""); 
}
function format_number(n){
		var arr=n+"";
		arr=arr.split(".");
		n=arr[0];
		var sy=arr[1];
	   var b=parseInt(n).toString();
	   var len=b.length;
	   if(len<=3){return b;}
	   var r=len%3;
	   if(sy!=undefined){
		   return r>0?b.slice(0,r)+","+b.slice(r,len).match(/\d{3}/g).join(",")+"."+sy:b.slice(r,len).match(/\d{3}/g).join(",")+"."+sy;
	   }else{
		   return r>0?b.slice(0,r)+","+b.slice(r,len).match(/\d{3}/g).join(","):b.slice(r,len).match(/\d{3}/g).join(",");
	   }
	 }