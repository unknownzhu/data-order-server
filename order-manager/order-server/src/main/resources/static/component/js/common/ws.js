var BASE_URL = "http://" + window.location.host;
var UUM_URL = "http://" + window.location.hostname+":7002";
//var OAUTH_URL = "http://172.16.14.168:8013";
var OAUTH_URL = "http://" + window.location.hostname+":8013";
var BAOBIAO = "http://"+location.hostname+":8075/ReportServer?reportlet=tzps_xmpsrckh.cpt&op=write"
var BAOBIAO_LOOK = "http://"+location.hostname+":8075/ReportServer?reportlet=tzps_xmpsrckh.cpt"
var NIANDU_LOOK = "http://"+location.hostname+":8075/ReportServer?reportlet=tzps_xmpsndkh.cpt"
var BAOBIAO_BASE="http://"+location.hostname+":8075";
var WEITUOSHU="http://"+location.hostname+":8075";

//var FEI_BAOBIAO = "http://"+location.hostname+":8083/ReportServer?reportlet=tzps_fsys.cpt&op=write"
var FEI_Y_BAOBIAO_LOOK = "http://"+location.hostname+":8075/ReportServer?reportlet=tzps_fsys.cpt"
var FEI_J_BAOBIAO_LOOK = "http://"+location.hostname+":8075/ReportServer?reportlet=tzps_fsjs.cpt"

var object = store.get("userInfo", true);
var ws = {
	util:{
		getQueryString:function(name){
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
			var r = window.location.search.substr(1).match(reg);
			if (r != null)
				return decodeURI(r[2]);
			return null;
		},
		getUserInfo:function(){
			if(object!=null){
				return object;
			}
		},
		loginout : function() {
			var url = "http://" + window.location.host + "/logout"
			return request.post(url);
		},
		oauthLogout : function() {
			var num = Math.ceil(Math.random() * 1000);
			var script = document.createElement('script');// 创建一个script标签元素。
			script.setAttribute('type', 'text/javascript');// 设置script标签的type属性。
			script.setAttribute('src', OAUTH_URL + "/images/logout/0231.js?"
					+ num);
			if (navigator.userAgent.indexOf("IE") >= 0) {
				// IE下的事件
				script.onreadystatechange = function() {
					if (script
							&& (script.readyState == "loaded" || script.readyState == "complete")) {
						window.location.reload(true);

					}
				}
			} else {
				script.onload = function() {
					window.location.reload(true);

				}
			}
			document.body.appendChild(script);
		}
	},
	tzps:{
		findItem:function(Item_ID,Item_Period){
			var data = {
					Item_ID : Item_ID,
					Item_Period:Item_Period
	  			};
			var url = BASE_URL + "/tspzServer/apistore/findItem";
			return request.post(url,JSON.stringify(data));
		},
		wex5_list_byCQJG:function(data){
			var url = BASE_URL + "/tspzServer/apistore/wex5_list_byCQJG";
			return request.post(url,JSON.stringify(data));
		},
		lotterystart:function(Role_Type,flagOne){
			var data = {
					Role_Type : Role_Type,
					flagOne:flagOne
	  			};
			var url = BASE_URL + "/tspzServer/apistore/lotterystart";
			return request.post(url,JSON.stringify(data));
		},
		lotterystop:function(Role_Type,flagOne){
			var data = {
					Role_Type : Role_Type,
					flagOne:flagOne
	  			};
			var url = BASE_URL + "/tspzServer/apistore/lotterystop";
			return request.post(url,JSON.stringify(data));
		},
		wex5_list_byCQ:function(data,process){
			var url = BASE_URL + "/tspzServer/apistore/wex5_list_byCQ";
			return request.post(url,data,process);
		},
                getNextBachAgency_JG:function(data,process){
			var url = BASE_URL + "/tspzServer/apistore/getNextBachAgency_JG";
			return request.post(url,data,process);
		
		},
		lotterystop:function(Role_Type,flagOne){
			var data = {
					Role_Type : Role_Type,
					flagOne:flagOne
	  			};
			var url = BASE_URL + "/tspzServer/apistore/lotterystop";
			return request.post(url,JSON.stringify(data));
		},
		wex5_save_PC:function(Role_Type,Agency_ID){
			var data = {
					Role_Type : Role_Type,
					Agency_ID:Agency_ID
	  			};
			var url = BASE_URL + "/tspzServer/apistore/wex5_save_PC";
			return request.post(url,JSON.stringify(data));
		},
		wex5_save_ITEM:function(data,process){
			var url = BASE_URL + "/tspzServer/apistore/wex5_save_ITEM";
			return request.post(url,data,process);
		},
		wex5_save_ITEM:function(data,process){
			var url = BASE_URL + "/tspzServer/apistore/wex5_save_ITEM";
			return request.post(url,data,process);
		},
		wex5_update_ITEM:function(data){
			var url = BASE_URL + "/tspzServer/apistore/wex5_update_ITEM";
			return request.post(url,JSON.stringify(data));
		},
		wex5_list_byItemID:function(Item_ID,Item_Period,Role_Type,process){
			var data = {
					Item_ID : Item_ID,
					Item_Period:Item_Period,
					Role_Type:Role_Type
	  			};
			var url = BASE_URL + "/tspzServer/apistore/wex5_list_byItemID";
			return request.post(url,JSON.stringify(data),process);
		},
		wex5_update_log:function(data){
			var url = BASE_URL + "/tspzServer/apistore/wex5_update_log";
			return request.post(url,JSON.stringify(data));
		},
		nextBatch:function(data){
			var url = BASE_URL + "/tspzServer/apistore/nextBatch";
			return request.post(url,JSON.stringify(data));
		},
		updatedelayNumber:function(data){
			var url = BASE_URL + "/tspzServer/apistore/updatedelayNumber";
			return request.post(url,JSON.stringify(data));
		},
		insertAgencyRefuseItem:function(data){
			var url = BASE_URL + "/tspzServer/apistore/insertAgencyRefuseItem";
			return request.post(url,JSON.stringify(data));
		},
		wex5_save_CQINFO:function(data){
			var url = BASE_URL + "/tspzServer/apistore/wex5_save_CQINFO";
			return request.post(url,JSON.stringify(data));
		},
		list_agencyList:function(data){
			var url = BASE_URL + "/tspzServer/apistore/list_agencyList";
			return request.post(url,JSON.stringify(data));
		},
		deleteFileOld:function(server_id,id,process){
			var data = {
					SERVERFILEID : server_id,
					id:id
	  			};
			var url = BASE_URL + "/tspzServer/appClient/delete";
			return request.post(url,JSON.stringify(data),process);
		},
		deleteFile:function(data,process){ 
			console.log(data);
			var url = BASE_URL + "/tspzServer/appClient/delete";
			return request.post(url,JSON.stringify(data),process);
		},
		findFlowNodeListByUserId:function(userId,id,process){
			var data = {
					userId : userId,
					flowId:id
	  			};
			var url = BASE_URL + "/tspzServer/appProjectEntry/findFlowNodeListByUserId?userId="+userId+"&flowId="+id;
			return request.post(url,JSON.stringify(data),process);
		},
		lookChart:function(businessId,objName){
			var url = BASE_URL + "/tspzServer/appProjectEntry/lookChart?businessId="+businessId+"&objName="+objName;
			return request.get(url);
		},
		findItemAgency:function(item_id){
			var url = BASE_URL + "/tspzServer/apistore/findItemAgency?item_id="+item_id;
			return request.get(url);
		},
		deleteItemAgency:function(item_id){
			var url = BASE_URL + "/tspzServer/apistore/deleteItemAgency?item_id="+item_id;
			return request.get(url);
		},
		updateItemAgency:function(data){
			var url = BASE_URL + "/tspzServer/apistore/updateItemAgency";
			return request.post(url,data);
		},
		insertItemAgency:function(data){
			var url = BASE_URL + "/tspzServer/apistore/insertItemAgency";
			return request.post(url,data);
		},
		startAgencySet:function(item_id){
			var url = BASE_URL + "/tspzServer/apistore/startAgencySet?item_id="+item_id;
			return request.get(url);
		},
		getYearList:function(status,process){
			var url = UUM_URL + "/uum/busyear/getList?status="+status;
			var data={};
			return request.get(url,data,process);
		},
                isBackSure:function(id){
			var url = BASE_URL + "/tspzServer/apistore/isBackSure?id="+id;
			return request.get(url);
		},
		reportFindById:function(id){
			var url = BASE_URL + "/tspzServer/reportDefend/findById?id="+id;
			return request.get(url);
		},
		startUpdateKaohe:function(id,region_code,user_id){
			var url = BASE_URL + "/tspzServer/kaohe/startUpdateKaohe?id="+id+"&region_code="+region_code+"&user_id="+user_id;
			return request.get(url);
		}
	}
}
