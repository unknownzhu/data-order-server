//判断时间是否为个位数，如果时间为个位数就在时间之前补上一个“0”
	function check(val) {
		if (val < 10) {
			return ("0" + val);
		} 
		else {
			return (val);
		}
	}
	function displayTime() {
		var timeDiv=document.getElementById("time");
		var date = new Date();
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		var day = date.getDate();
		var hour = date.getHours();
		var minutes = date.getMinutes();
		var second = date.getSeconds();
		var dayn = date.getDay();
		var weekday = new Array(7)
		weekday[0] = "星期天"
		weekday[1] = "星期一"
		weekday[2] = "星期二"
		weekday[3] = "星期三"
		weekday[4] = "星期四"
		weekday[5] = "星期五"
		weekday[6] = "星期六"
		var timestr = year + "年" + month + "月" + day + "日  " + check(hour)
				+ ":" + check(minutes) + ":" + check(second)+ "   " + weekday[dayn]; 
	
		timeDiv.innerHTML = timestr;
	}
	function start(){
		window.setInterval("displayTime()",1000);
	}


// 设置为主页 
function SetHome(obj, vrl) {
	try {
		obj.style.behavior = 'url(#default#homepage)'; obj.setHomePage(vrl);
	}
	catch (e) {
		if (window.netscape) {
			try {
				netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
			}
			catch (e) {
				alert("此操作被浏览器拒绝！\n请在浏览器地址栏输入“about:config”并回车\n然后将 [signed.applets.codebase_principal_support]的值设置为'true',双击即可。");
			}
			var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);
			prefs.setCharPref('browser.startup.homepage', vrl);
		} else {
			alert("您的浏览器不支持，请按照下面步骤操作：1.打开浏览器设置。2.点击设置网页。3.输入：" + vrl + "点击确定。");
		}
	}
}
// 加入收藏 兼容360和IE6 
function shoucang(sTitle, sURL) {
	try {
		window.external.addFavorite(sURL, sTitle);
	}
	catch (e) {
		try {
			window.sidebar.addPanel(sTitle, sURL, "");
		}
		catch (e) {
			alert("加入收藏失败，请使用Ctrl+D进行添加");
		}
	}
}
function btnSearch(){
    $("#search").attr("href",$("#search").attr("href")+"?keyword="+$("#keyword").val());
}
