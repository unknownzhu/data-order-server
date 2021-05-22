var BASE_URL = "http://"+window.location.host;

// 管控平台
var FLOW_URL = "http://172.16.7.58:9077";
//数据查询工具
var DATA_URL="http://10.108.6.135:8080";
// var FLOW_URL = "http://127.0.0.1:9077";
//数据链接管理
// var FLOW_URL5 = BASE_URL;//数据源
var FLOW_URL5 = BASE_URL;//数据源
// var FLOW_URL5 = BASE_URL;//数据源
// var FLOW_URL5 = "http://10.108.6.118:9077";//数据源
// var QUALITY_URL = "http://10.108.6.118:7073";//质量
var QUALITY_URL = BASE_URL;//质量
//数据分类管理
var data_hierarchy_ip = BASE_URL+'/data-manager-server';
// var data_hierarchy_ip = "http://10.108.6.118:7071/data-manager-server/";


//管控平台-维表转换
var url4 = BASE_URL;


//管控平台-事实表转换
var FLOW_URL2 = BASE_URL+'/data-manager-server';
//管控平台-事实表转换-事实表步骤条
var FLOW_URL3 = "http://10.108.6.118:7071";

//数据模型管理/维表字段变更
var FLOW_URL6 = BASE_URL;
//徐光辉的接口调数据分层
var FLOW_URL6_FUWU = BASE_URL+'/data-manager-server';

// 标准管理
var STANDARD_URL833 = "http://10.108.8.33:8081";
var STANDARD_URL833833 = "http://10.108.8.33";
//数据标准分类/新增标准文档/标准反馈
var STANDARD_URL = "http://10.108.6.118:7071";
// var STANDARD_URL = "http://10.108.71.157:8002";
//数仓分层管理
var STANDARD_UR_IP = BASE_URL;

var STANDARD_URL118 = "http://10.108.71.157:8002";
//首页采集平台
var INDEX_URL = "http://10.108.8.33";
var INDEX_URL1 = "http://10.108.8.30";

//质量平台
var FLOW_URL9 = BASE_URL;

//管理平台
var managementPlatform_URL = 'http://10.108.6.118:7073';

function GetRequest() {
	var url = location.search; //获取url中"?"符后的字串
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for (var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest;
}

//自动加逗号¥
function formatNum(str) {
	if (str) {
		var newStr = "";
		var count = 0;
		if (str.indexOf(".") == -1) {
			for (var i = str.length - 1; i >= 0; i--) {
				if (count % 3 == 0 && count != 0) {
					newStr = str.charAt(i) + "," + newStr;
				} else {
					newStr = str.charAt(i) + newStr;
				};
				count++;
			};
			str = newStr; //自动补小数点后两位
			return str;
		} else {
			for (var i = str.indexOf(".") - 1; i >= 0; i--) {
				if (count % 3 == 0 && count != 0) {
					newStr = str.charAt(i) + "," + newStr;
				} else {
					newStr = str.charAt(i) + newStr; //逐个字符相接起来
				}
				count++;
			}
			str = newStr + (str + "00").substr((str + "00").indexOf("."), 3);
			return str;
		}
	}
};
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
