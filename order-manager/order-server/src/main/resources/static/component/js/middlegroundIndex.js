layui.use(['element', 'layer', 'form', 'layedit', 'jquery'], function() {
	var $ = layui.jquery;
	var layer = layui.layer;
	var form = layui.form;
	var layedit = layui.layedit;
	var element = layui.element;
	$(document).ready(function() {
		if (window.location.search.indexOf("?") < 0) { //判断是否是第一次加载
			var index  = layer.load(1);
			setTimeout(function() {
				$('#middleConter').attr('src', 'middlegroundIndex/middleConter.html?ranparam=random()');
				layer.close(index);
			}, 500);
		} else {}
	});
	$('.demoA').hover(function() {
		var thisID = $(this).attr("id");
		var str = demoObj(thisID);
		$(this).siblings().html(str);
	});
});

function demoObj(id) {
	var URL = '';
	if (id == 1) {
		URL = 'middlegroundIndex/standardAM.json';
		ICON = 'layui-icon-read';
	} else if (id == 2) {
		URL = 'middlegroundIndex/middlegroundIndex.json';
		ICON = 'layui-icon-log';
	} else if (id == 3) {
		URL = 'middlegroundIndex/controlCenter.json';
		ICON = 'layui-icon-component';
	} else if (id == 4) {
		URL = 'middlegroundIndex/qualityCenter.json';
		ICON = 'layui-icon-set-sm';
	} else if (id == 5) {
		URL = 'middlegroundIndex/calculateCenter.json';
		ICON = 'layui-icon-list';
	} else if (id == 6) {
		URL = 'middlegroundIndex/monitoringCenter.json';
		ICON = 'layui-icon-chart';
	} else if (id == 7) {
		URL = 'middlegroundIndex/dataManagement.json';
		ICON = 'layui-icon-notice';
	}
	var strData = '';
	$.ajax({
		url: URL,
		dataType: 'json',
		type: 'get',
		async: false,
		success: function(data) {
			var divList = 'divList';
			var num = data.length;
			var str = '';
			str += '<div class="divList">';
			for (var k = 0; k < num; k++) {
				str += '<div id=' + divList + '>';
				if (data[k].href) {
					for (var j = 0; j < data[k].divList.length; j++) {
						if (j == 0) {
							str += '<a class="divListA" style="color:#2667DF !important" href="javascript:;">';
							str += '<i class="divListAImg layui-icon ' + ICON + '"></i>';
							str += data[k].divList[j] + '</a>';
						} else {
							str += '<a onclick="skip(this)" title=' + data[k].divList[j] + ' href="javascript:;" name=' + data[k].href[
								j] + '>' + data[k].divList[j] + '</a>';
						}
					}
				} else {
					for (var j = 0; j < data[k].divList.length; j++) {
						if (j == 0) {
							str += '<a class="divListA" style="color:#2667DF !important" href="javascript:;">';
							str += '<i class="divListAImg layui-icon ' + ICON + '"></i>';
							str += data[k].divList[j] + '</a>';
						} else {
							str += '<a title=' + data[k].divList[j] + ' href="javascript:;">' + data[k].divList[j] + '</a>';
						}
					}
				}

				str += '</div>';
			};
			str += '</div>';
			strData = str;
		}
	});
	return strData;
}
// 三级导航跳转
function skip($this) {
	var name = $($this).attr("name");
	var href = $($this).parent().parent().parent().parent().find('.demoA').attr('name');
	if (href == 'http://10.108.6.131:7180/cmf/') {
		window.open(href + name, '_blank');
	} else {
		window.open(href + '?href=' + name, '_blank');
	}
}
// 退出系统
function layuiBack() {
	layer.confirm('确定退出吗?', {
		icon: 3,
		title: '提示'
	}, function(index) {
		ws.util.startOut();
		//layer.msg('退出成功！');
	});
};
