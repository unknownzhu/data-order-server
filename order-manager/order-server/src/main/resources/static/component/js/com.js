layui.use(['element', 'layer', 'form', 'layedit', 'jquery', 'table'], function() {
	var $ = layui.jquery;
	layer = layui.layer;
	form = layui.form;
	layedit = layui.layedit;
	element = layui.element;
	table = layui.table;

	// 表单验证
	form.verify({
		directoryUrl: function(value, item) { //value：表单的值、item：表单的DOM对象
			if (!/^([a-zA-Z]:|([a-zA-Z]:)?\\[^\/\:\*\?\""\<\>\|\,]*)$/.test(value)) {
				return '路径格式有误';
			}
		},
		allUrl: function(value, item) { //value：表单的值、item：表单的DOM对象
			var sRegex = '^((https|http|ftp|rtsp|mms)?://)' + '?(([0-9a-z_!~*\'().&=+$%-]+: )?[0-9a-z_!~*\'().&=+$%-]+@)?' //ftp的user@ 
				+
				'(([0-9]{1,3}.){3}[0-9]{1,3}' // IP形式的URL- 199.194.52.184 
				+
				'|' // 允许IP和DOMAIN（域名） 
				+
				'([0-9a-z_!~*\'()-]+.)*' // 域名- www. 
				+
				'([0-9a-z][0-9a-z-]{0,61})?[0-9a-z].' // 二级域名 
				+
				'[a-z]{2,6})' // first level domain- .com or .museum 
				+
				'(:[0-9]{1,4})?' // 端口- :80 
				+
				'((/?)|' // a slash isn't required if there is no file name 
				+
				'(/[0-9a-z_!~*\'().;?:@&=+$,%#-]+)+/?)$';
			var re = new RegExp(sRegex);
			if (!re.test(value)) {
				return '地址格式有误';
			}
		},
		emailUrl: function(value, item) { //value：表单的值、item：表单的DOM对象
			if (!/^([\w\.\-]+)\@(\w+)(\.([\w^\_]+)){1,2}$/.test(value)) {
				return '邮箱格式有误';
			}
		},
		ipAddress: function(value, item) { //value：表单的值、item：表单的DOM对象
			if (!/^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/.test(value)) {
				return 'IP不合法';
			}
		},
		port: function(value, item) { //value：表单的值、item：表单的DOM对象
			if (!(/^[1-9]\d*$/.test(value) && 1 <= 1 * value && 1 * value <= 65535)) {
				return '端口不合法';
			}
		},
		greaterNumber: function(value, item) { //value：表单的值、item：表单的DOM对象
			if (value.length > 1) {
				var flagRex = /^0/;
				if (flagRex.test(value)) {
					return '首个字符不能为0';
				}
			}
		},
		pass: [
			/^[\S]{6,12}$/, '密码必须6到12位，且不能出现空格'
		]
	});

	// 失去焦点时的表单验证
	$(".directoryUrl").blur(function() {
		var value = $(".directoryUrl").val();
		if (/^([a-zA-Z]:|([a-zA-Z]:)?\\[^\/\:\*\?\""\<\>\|\,]*)$/.test(value)) {
			layer.msg('路径格式有误', {
				icon: 5
			});
		}
	});
	$(".allUrl").blur(function() {
		var value = $(".allUrl").val();
		var sRegex = '^((https|http|ftp|rtsp|mms)?://)' + '?(([0-9a-z_!~*\'().&=+$%-]+: )?[0-9a-z_!~*\'().&=+$%-]+@)?' //ftp的user@
			+
			'(([0-9]{1,3}.){3}[0-9]{1,3}' // IP形式的URL- 199.194.52.184 
			+
			'|' // 允许IP和DOMAIN（域名） 
			+
			'([0-9a-z_!~*\'()-]+.)*' // 域名- www. 
			+
			'([0-9a-z][0-9a-z-]{0,61})?[0-9a-z].' // 二级域名 
			+
			'[a-z]{2,6})' // first level domain- .com or .museum 
			+
			'(:[0-9]{1,4})?' // 端口- :80 
			+
			'((/?)|' // a slash isn't required if there is no file name 
			+
			'(/[0-9a-z_!~*\'().;?:@&=+$,%#-]+)+/?)$';
		var re = new RegExp(sRegex);
		if (!re.test(value)) {
			layer.msg('地址格式有误', {
				icon: 5
			});
		}
	});
	$(".emailUrl").blur(function() {
		var value = $(".emailUrl").val();
		if (!/^([\w\.\-]+)\@(\w+)(\.([\w^\_]+)){1,2}$/.test(value)) {
			layer.msg('邮箱格式有误', {
				icon: 5
			});
		}
	});
	$(".port").blur(function() {
		var value = $(".port").val();
		if (!(/^[1-9]\d*$/.test(value) && 1 <= 1 * value && 1 * value <= 65535)) {
			layer.msg('端口不合法', {
				icon: 5
			});
		}
	});
	$(".ipAddress").blur(function() {
		var value = $(".ipAddress").val();
		if (!/^((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)$/.test(value)) {
			layer.msg('IP不合法', {
				icon: 5
			});
		}
	});
	$(".greaterNumber").blur(function() {
		var value = $(".greaterNumber").val();
		if (value < 0) {
			$(".greaterNumber").val(0);
		}
		if (!/^[+]{0,1}(\d+)$/.test(value)) {
			layer.msg('请输入正整数', {
				icon: 5
			});
		}
		if (value.length > 1) {
			var flagRex = /^0/;
			if (flagRex.test(value)) {
				layer.msg('首个字符不能为0', {
					icon: 5
				});
			}
		}
	});

	$(document).ready(function() {

	});
});
