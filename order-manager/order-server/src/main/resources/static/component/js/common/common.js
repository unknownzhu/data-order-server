var Tree=Tree||{};
Tree.formatTreeJsonByRoot = function(listData,id,parentId,showName,rootNode,state){	
	var treeList = [];
	if (listData != null && listData.length > 0) {
		for (var i = 0; i < listData.length; i++) {
			var node = {};
			node.id = listData[i][id];
			node.text = listData[i][showName];
			if(listData[i].parent_id==null){
				listData[i].parent_id="";
			}
			node.tags = listData[i];
			
			node.children = [];
			if (node.tags.parent_id == rootNode.tags.id) {
				rootNode.children.push(node);
				this.formatTreeJsonByRoot(listData,id,parentId,showName, node,state);
			}
			if(node.children.length>0){
				if(null==state){
					node.state ='closed';
				}else{
					node.state =state;
				}
			}
		}
		treeList.push(rootNode);
	}
	return treeList;
}


Tree.formatTreeJson = function(listData,id,parentId,showName,state){
	var treeList = [];
	if (listData != null && listData.length > 0) {
		for (var i = 0; i < listData.length; i++) {
			var node = {};
			node.id = listData[i][id];
			node.text = listData[i][showName];
			node.tags = listData[i];
			node.children = [];
			if(0==listData[i][parentId]||""==listData[i][parentId]){
				this.formatTreeJsonByRoot(listData,id,parentId,showName,node,state);
				if(node.children.length>0){
					node.state=state;
				}
				treeList.push(node);
			}
		}
	}
	return treeList;
}





function formatTreeJson(arrayList, root){
		var treeList = [];
		
		if (arrayList != null && arrayList.length > 0) {
			
			for (var i = 0; i < arrayList.length; i++) {
				if(arrayList[i].parent_id==null){
					arrayList[i].parent_id="";
				}
				var node = {};
				node.id = arrayList[i]["id"];
				node.text = arrayList[i]["name"];
				node.tags = arrayList[i];
				node.children = [];
				if (node.tags.parent_id == root.tags.id) {
					root.children.push(node);
					formatTreeJson(arrayList, node);
				}
			}
			treeList.push(root);
		}
		return treeList;

	};
function formatTreeGrid(arrayList,rule) {
		var treeList = {};
		if (arrayList != null && arrayList.length > 0) {
			treeList.total=arrayList.length+"";
			treeList.rows=[];
			for (var i = 0; i < arrayList.length; i++) {
				var node = arrayList[i];
				if(arrayList[i]["parent_id"]!=rule){
					node._parentId=arrayList[i]["parent_id"];
				}
				
				treeList.rows.push(node);
			
			}
		}
		return treeList;

	};

var Request=Request||{};

Request.AjaxReq = function(type, url, data, callback, async) {
	if(url.indexOf("/")==0){
		url = rootContext.url.root+url;
	}
	return requestAjax(type, url, JSON.stringify(data), function(result) {
		var icon = 'error';
		if (result.code == "0000") {
			icon = 'info';
			if (callback != undefined)
				callback(result.data);
		}
		$.messager.alert({
			title : '提示消息',
			msg : result.msg,
			icon : icon,
			showType : 'show',
			style : {
				right : '',
				bottom : ''
			}
		});

	}, async);
};

Request.get = function(url,params,callback,async){
	if(async==undefined){
		async == false;
	}
	var data ;
	var urlParam = "";
	if(params != null ){
		urlParam = "?";
		for(var key in params){
			urlParam = urlParam+key+"="+params[key]+"&"; 
		}
	}
	$.ajax({
		type : "get",
		url : rootContext.url.root+url+urlParam,
		async : async,
		success : function(result) {
			data = result;
			if(callback!=undefined){
				callback(result);
			}
			
		}
	});
	return data;
}

Request.del = function(url,params,callback){
	
	var data = null;
	var urlParam = "";
	if(params != null ){
		urlParam = "?";
		for(var key in params){
			urlParam = urlParam+key+"="+params[key]; 
		}
	}
	$.ajax({
		type : "delete",
		url : rootContext.url.root+url+urlParam,
		async : false,
		success : function(result) {
			var icon = 'error';
			if (result.code == "0000") {
				icon = 'info';
				if(callback!=undefined)
					callback(result);	
			}
			$.messager.alert({
				title : '提示消息',
				msg : result.msg,
				icon:icon,
				showType : 'show',
				style : {
					right : '',
					bottom : ''
				}
			});
		}
	});
	return data;
}

function requestAjax(method,url, data, process, async){
	var data1;
	if(async==undefined){
		async = false;
	}

		$.ajax({
        type: method,
        url: url,
        dataType: 'json',
        async: async,//使用同步方式，目前data组件有同步依赖
        cache: false,
        contentType:"application/json",
        data: data,
        success: function(redata){
        	data1 = redata;
        	if(process != undefined) process(redata);
        }
    });
	if(async==false){
		return data1;
	}
};

var store={
	set:function(key,value,json,isStore){
		if(isStore){
			myCookie.set(key,value,json,7);
		}else{
			myCookie.set(key,value,json,0);
		}
	},
	get:function(key,json){
	//alert("hh")
		var result ="";		
		//if(key=="userInfo" && result ==null){
			//alert("url")
			$.ajax({
				type:"get",
				async: false,
				url:"http://"+window.location.host+"/user",
				//url:"http://172.16.14.212:9077/user",
				success:function(data){
					result=data;
					store.remove("userInfo");
					store.set("userInfo",result,true);
				}
			});
		//}
		return result;
	},
	remove:function(key){
		myCookie.remove(key);
	}
}
var myCookie = {
		
   set:function(name,value,json,time){
	   
	  if(json){
		  $.cookie.json=true;
		  $.cookie.raw = false;
	  }else{
		  $.cookie.json=false;
		  $.cookie.raw = true;
	  }
	  
	  if(time>0){
		  $.cookie(name,value,{expires:time,path:'/'});
	  }else{
		  $.cookie(name,value,{path:'/'});
	  }
	  
   },
   get:function(name,json){
	   if(json){
			$.cookie.json=true;
			$.cookie.raw = false;
	   }else{
		   $.cookie.json=false;
		   $.cookie.raw = true;
	   }
	   return $.cookie(name);
   },
   remove:function(name){
	   $.removeCookie(name,{path:'/'});
   }
}

//获取查询字符串参数
function getQsObject(){
	   // 取得查询字符串并去掉开头的问号
	   var qs = (location.search.length > 0 ? location.search.substring(1) : '');  
	  
	   // 保存数据的对象
	   args = {};  
	  
	   // 取得每一项
	   var items = qs.length ? qs.split('&') : [],  
	      item = null,  
	      name = null,  
	      // 在for循环中使用
	      i = 0, len = items.length;  
	  
	   // 逐个将每一项添加到args对象中
	   for(i = 0 ; i < len; i++){  
	      item = items[i].split('=');  
	      name = decodeURIComponent(item[0]);  
	      value = decodeURIComponent(item[1]);  
	  
	      if(name.length){  
	         args[name] = value;  
	      }  
	   }  
	   return args;
}
