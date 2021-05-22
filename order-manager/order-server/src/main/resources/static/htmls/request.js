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
            timeout : 500000,
            contentType:"application/json",
            async: async,//使用同步方式，目前data组件有同步依赖
            dataType : 'json',
            data: data,
            success: function(data){
            	data1 = data;
            	if(process != undefined) process(data);
              },
            error: function(XMLHttpRequest, textStatus, errorThrown){
            	if(XMLHttpRequest){
            		var status = XMLHttpRequest.status;
            		if(status&&status=='401'){
            			//location.replace(location.href);
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
            			//location.replace(location.href);
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
            			//location.replace(location.href);
            		}
            	}
            }
            
        });
 		
 		if(async==false)
 			return data1;
	},
	get : function(url, data, process, async)
	{
		/*store.remove("userInfo");
					store.remove("logType");
					store.remove("name");*/
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


            	if(XMLHttpRequest){
            		var status = XMLHttpRequest.status;
            		if(status&&status=='401'){
            			//location.replace(location.href);
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
            			//location.replace(location.href);
            		}
            	}
            },
			complete:function(XMLHttpRequest,status){

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
            	/*var object = store.get("userInfo", true);
            	$("#count_"+condition.id+"_"+store.get(object.userId+"_org_id")).show();
            	$("#count_"+condition.id+"_"+store.get(object.userId+"_org_id")).attr("title","接口不通");
            	 $("#count_"+condition.id+"_"+store.get(object.userId+"_org_id")).css("background","url('./img/jingtanhao.png')");*/
            },
			complete:function(XMLHttpRequest,status){

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

			}
            
        });
 		
 		if(async==false)
 		return data1;
	},
	deleted : function(url, data, process, async)
	{
		/*store.remove("userInfo");
		store.remove("logType");
		store.remove("name");*/
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


	if(XMLHttpRequest){
		var status = XMLHttpRequest.status;
		if(status&&status=='401'){
			//location.replace(location.href);
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
function initMoney(account,dividend,decimal){
	if(!dividend){
		dividend = 10000;
	}
	if(decimal==0){
		decimal=0;
	}else{
		if(!decimal){
			decimal = 2;
		}
	}
	
	if(account){
		account = account/dividend;
		account = account.toFixed(decimal);
		return account;
	}else{
		return "";
	}
}
function getColor(account,level){
	var color = new Array("#AFDDFF","#8DC8FF","#62B3FF","#468FFF","#1A7BF4");
	var currentColor = color[0];
	if(account>level[0]){
		currentColor = color[4];
	}else if(account<level[0] && account>=level[1]){
		currentColor = color[3];
	}else if(account<level[1] && account>=level[2]){
		currentColor = color[2];
	}else if(account<level[2] && account>=level[3]){
		currentColor = color[1];
	}
	return currentColor;
}


function getMoney(data){
	var array = [];
	var max = Math.max.apply(null,data);
	var min = Math.min.apply(null,data);
	var zhongjian = (max-min)/3;
	max = max+"";
	zhongjian = zhongjian+"";
	
	var maxLength = max.split(".")[0].length;
	var zhongjianLength =zhongjian.split(".")[0].length;
	zhongjian  =  zhongjian.substring(0,1);
	max = max.substring(0,1);
	max = Number(max);
	if(max<4){
		max=4;
		zhongjian=1;
		zhongjianLength = maxLength;
	}
	
	for(var i=0;i<maxLength-1;i++){
		max+="0";
	}
	 
	for(var i=0;i<zhongjianLength-1;i++){
		zhongjian+="0";
	}
	max = Number(max);
	zhongjian = Number(zhongjian);
	array[0]=max;
	array[1]=max-zhongjian;
	array[2]=max-zhongjian*2;
	array[3]=max-zhongjian*3;
	
	return array;
}

function getBuggetMoney(data){
	var array = [];
	var max = Math.max.apply(null,data);
	var min = Math.min.apply(null,data);
	var batData = [];
	var j = 0;
	 
	for(var i =0;i<data.length;i++){
		 if(Number(data[i])!=max && Number(data[i])!=min){
			 batData[j]=data[i];
			 j++;
		 }
	 }
	data=batData;
	max = Math.max.apply(null,data);
	min = Math.min.apply(null,data);
	var zhongjian = (max-min)/3;
	max = max+"";
	zhongjian = zhongjian+"";
	var maxLength = max.split(".")[0].length;
	var zhongjianLength =zhongjian.split(".")[0].length;
	zhongjian  =  zhongjian.substring(0,1);
	max = max.substring(0,1);
	max = Number(max);
		if(maxLength<7){
		max=4;
		zhongjian=1;
		zhongjianLength = maxLength;
		}
	
	for(var i=0;i<maxLength-1;i++){
		max+="0";
	}
	 
	for(var i=0;i<zhongjianLength-1;i++){
		zhongjian+="0";
	}
	max = Number(max);
	zhongjian = Number(zhongjian);
	array[0]=max;
	array[1]=max-zhongjian;
	array[2]=max-zhongjian*2;
	array[3]=max-zhongjian*3;
	return array;
}


function initlevels(level,id){
	/*var str = "<div><a class=\"a1\"></a><p><span>"+initMoney(level[0],10000,0)+"</span><span>亿元以上</span></p></div>";
	str += "<div><a class=\"a2\"></a><p><span>"+initMoney(level[0],10000,0)+"-"+initMoney(level[1],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a3\"></a><p><span>"+initMoney(level[1],10000,0)+"-"+initMoney(level[2],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a4\"></a><p><span>"+initMoney(level[2],10000,0)+"-"+initMoney(level[3],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a5\"></a><p><span>"+initMoney(level[3],10000,0)+"</span><span>亿元以下</span></p></div>";
	str += "<div><p><span>数据来源:</span><span>支付系统</span></p></div>";
	$("#"+id).html(str);*/
	var str = "<div><a class=\"a1\"></a><p><span>"+initMoney(level[0],10000,0)+"</span><span>亿元以上</span></p></div>";
	str += "<div><a class=\"a2\"></a><p><span>"+initMoney(level[1],10000,0)+"-"+initMoney(level[0],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a3\"></a><p><span>"+initMoney(level[2],10000,0)+"-"+initMoney(level[1],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a4\"></a><p><span>"+initMoney(level[3],10000,0)+"-"+initMoney(level[2],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a5\"></a><p><span>"+initMoney(level[3],10000,0)+"</span><span>亿元以下</span></p></div>";
	str += "<div><span>数据来源:</span><span>支付系统</span></div>";
	$("#"+id).html(str);
}


function initlevelsfs(level,id){
	/*var str = "<div><a class=\"a1\"></a><p><span>"+initMoney(level[0],10000,0)+"</span><span>亿元以上</span></p></div>";
	str += "<div><a class=\"a2\"></a><p><span>"+initMoney(level[0],10000,0)+"-"+initMoney(level[1],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a3\"></a><p><span>"+initMoney(level[1],10000,0)+"-"+initMoney(level[2],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a4\"></a><p><span>"+initMoney(level[2],10000,0)+"-"+initMoney(level[3],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a5\"></a><p><span>"+initMoney(level[3],10000,0)+"</span><span>亿元以下</span></p></div>";
	str += "<div><p><span>数据来源:</span><span>非税系统、横联系统</span></p></div>";
	$("#"+id).html(str);*/
	var str = "<div><a class=\"a1\"></a><p><span>"+initMoney(level[0],10000,0)+"</span><span>亿元以上</span></p></div>";
	str += "<div><a class=\"a2\"></a><p><span>"+initMoney(level[1],10000,0)+"-"+initMoney(level[0],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a3\"></a><p><span>"+initMoney(level[2],10000,0)+"-"+initMoney(level[1],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a4\"></a><p><span>"+initMoney(level[3],10000,0)+"-"+initMoney(level[2],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a5\"></a><p><span>"+initMoney(level[3],10000,0)+"</span><span>亿元以下</span></p></div>";
	str += "<div><span></span><span></span></div>";
	$("#"+id).html(str);
}


function initlevelsys(level,id){
	/*var str = "<div><a class=\"a1\"></a><p><span>"+initMoney(level[0],10000,0)+"</span><span>亿元以上</span></p></div>";
	str += "<div><a class=\"a2\"></a><p><span>"+initMoney(level[0],10000,0)+"-"+initMoney(level[1],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a3\"></a><p><span>"+initMoney(level[1],10000,0)+"-"+initMoney(level[2],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a4\"></a><p><span>"+initMoney(level[2],10000,0)+"-"+initMoney(level[3],10000,0)+"</span><span>亿元</span></p></div>";
	str += "<div><a class=\"a5\"></a><p><span>"+initMoney(level[3],10000,0)+"</span><span>亿元以下</span></p></div>";
	str += "<div><p><span>数据来源:</span><span>非税系统、横联系统</span></p></div>";
	$("#"+id).html(str);*/
	var str = "<div><a class=\"a1\"></a><p><span>"+level[0]+"</span><span>人数以上</span></p></div>";
	str += "<div><a class=\"a2\"></a><p><span>"+level[1]+"-"+level[0]+"</span><span>人数</span></p></div>";
	str += "<div><a class=\"a3\"></a><p><span>"+level[2]+"-"+level[1]+"</span><span>人数</span></p></div>";
	str += "<div><a class=\"a4\"></a><p><span>"+level[3]+"-"+level[2]+"</span><span>人数</span></p></div>";
	str += "<div><a class=\"a5\"></a><p><span>"+level[3]+"</span><span>人数以下</span></p></div>";
	str += "<div><p><span></span></p></div>";
	$("#"+id).html(str);
}

function getColorfs(account,level){
	var color = new Array("#AFDDFF","#8DC8FF","#62B3FF","#468FFF","#1A7BF4");
	var currentColor = color[0];
	if(account>level[0]){
		currentColor = color[4];
	}else if(account<level[0] && account>=level[1]){
		currentColor = color[3];
	}else if(account<level[1] && account>=level[2]){
		currentColor = color[2];
	}else if(account<level[2] && account>=level[3]){
		currentColor = color[1];
	}
	return currentColor;
}

function getColordp(account,level){
	var color = new Array("#AFDDFF","#8DC8FF","#62B3FF","#468FFF","#1A7BF4");
	var currentColor = color[0];
	if(account>level[0]){
		currentColor = color[4];
	}else if(account<level[0] && account>=level[1]){
		currentColor = color[3];
	}else if(account<level[1] && account>=level[2]){
		currentColor = color[2];
	}else if(account<level[2] && account>=level[3]){
		currentColor = color[1];
	}
	return currentColor;
}

var codeName = {};
codeName["11"]='一般公共预算收入';
codeName["12"]='一般公共预算支出';
codeName["21"]='政府性基金收入';
codeName["22"]='政府性基金支出';
codeName["31"]='国有资本收入';
codeName["32"]='国有资本支出';
codeName["41"]='社保收入';
codeName["42"]='社保支出';
codeName["99"]="其他";
function getCodeName(code){
	return codeName[code];
}


function change(regionCode){
	if(regionCode){
		return regionCode.substring(4,8)+"00";
	}else{
		return "";
	}
}

function getRegionLevel(regionId){
	if(regionId.substring(2,6)=='0000'){
		return "1";
	}else if(regionId.substring(4,6)=='00'){
		return "2";
	}else{
		return "3";
	}
}


function getRegionName(name,tags){
	var regionName = {};
	regionName["江西省"]="江西省本级";
	regionName["南昌市"]="南昌市本级";
	regionName["景德镇市"]="景德镇市本级";
	regionName["萍乡市"]="萍乡市本级";
	regionName["九江市"]="九江市本级";
	regionName["新余市"]="新余市本级";
	regionName["鹰潭市"]="鹰潭市本级";
	regionName["赣州市"]="赣州市本级";
	regionName["吉安市"]="吉安市本级";
	regionName["宜春市"]="宜春市本级";
	regionName["抚州市"]="抚州市本级";
	regionName["上饶市"]="上饶市本级";
	regionName["赣江新区"]="赣江新区本级";
	
	
	
	
	if(tags){
		if(name=='江西省'){
			return regionName[name];
		}else{
			return name;
		}
	}else{
		var x = regionName[name];
		
		if(x){
			return x;
		}else{
			return name;
		}
		
	}
}
var neeYear=true;