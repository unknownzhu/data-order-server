layui.use(['form', 'layedit'], function() {
	var $ = layui.jquery,
		form = layui.form,
		layedit = layui.layedit;

	//创建一个编辑器
	var editIndex = layedit.build('LAY_demo_editor');

	window.getUrlParams = function(key) {
		//获取url参数
		var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)", "i");
		var url = decodeURI(window.location.search)
		var r = url.substr(1).match(reg);
		if (r != null) return unescape(r[2]);
		return null;
	}


	form.verify({
		name: function(value) {
			if (value.length > 100) {
				return '数据源名长度不能超过100个字符';
			} else if (value === '') {
				return '当前项不可为空';
			}
		},
		url: function(value) {
			if (value.length > 100) {
				return '数据源URL长度不能超过100个字符';
			} else if (value === '') {
				return '当前项不可为空';
			}
		},
		dbName: function(value) {
			if (value.length > 100) {
				return '数据库名长度不能超过100个字符';
			} else if (value === '') {
				return '当前项不可为空';
			}
		},
		driverClassName: function(value) {
			if (value.length > 100) {
				return '数据库驱动名长度不能超过100个字符';
			} else if (value === '') {
				return '当前项不可为空';
			}
		},
		userName: function(value) {
			if (value.length > 100) {
				return '用户名长度不能超过100个字符';
			} else if (value === '') {
				return '当前项不可为空';
			}
		},
		dataTiering: function(value) {
			if (value.length > 40) {
				return '数据库分级长度不能超过40个字符';
			} else if (value === null) {
				return '当前项不可为空';
			}
		},
		maxActive: function(value) {
			if (value > 9999) {
				return '最大连接数不超过4位';
			} else if (value == null) {
				return '当前项不可为空';
			}
		},
		maxWait: function(value) {
			if (value > 9999) {
				return '最大等待时间不超过4位';
			} else if (value == null) {
				return '当前项不可为空';
			}
		},
		remark: function(value) {
			if (value.length > 500) {
				return '备注长度不能超过100个字符';
			}
			// } else if (value === '') {
			//     return '当前项不可为空';
			// }
		},
		password: [
			/^[\S]{6,10}$/, '密码必须6到12位，且不能出现空格'
		],
		content: function(value) {
			layedit.sync(editIndex);
		}
	});

	//判断id是否存在，初始化表单
	var id = getUrlParams("dbId");
	var dataHierarchy = [];

	$(document).ready(function () {
		if (id !== null && id !== undefined) {
			$.ajax({
				type: 'get',
				url: FLOW_URL5 + '/strategy/queryDataSourceById?dbId=' + id,
				contentType: "application/json;charset=UTF-8",
				async: true,
				success: function(redata) {
					var datasource =redata;
					if (datasource !== undefined && datasource != null) {
						datasourceInit(datasource);
						dataHierachyTree(dataHierarchy,datasource);
						//表单初始赋值
						form.val('datasourceConfig', {
							"id": datasource.data.id,
							"name": datasource.data.name,
							"type": datasource.data.type
							// , "dataTiering": datasource.data.dataTiering
							,
							"url": datasource.data.url,
							"dbName": datasource.data.dbName,
							"driverClassName": datasource.data.driverClassName,
							"userName": datasource.data.userName,
							"password": datasource.data.password,
							"maxActive": datasource.data.maxActive,
							"maxWait": datasource.data.maxWait,
							"remark": datasource.data.remark
							// , "sourceId":datasource.data.sourceId
							// , "source":datasource.data.source
						});
					}
				}

			});
			// request.get(FLOW_URL5 + "/strategy/queryDataSourceById?dbId=" + id);
		}else{
			datasourceInit();
			dataHierachyTree(dataHierarchy);
		}

	});
	//加载数据来源
	function datasourceInit(datasource){
		$.ajax({
			type: 'get',
			url:  '/manager/datasourceInfo/sourcelist',
			contentType: "application/json;charset=UTF-8",
			async: true,
			success: function(redata) {
				var res = redata.data;

				$.each(res, function(index, item) {
					$('#sourceId').append(new Option(item.comeFrom, item.id));
				});
				form.render('select');
				if (id !== null && id !== undefined) {
					form.val('datasourceConfig', {
						"sourceId": datasource.data.sourceId,
						"source": datasource.data.source
					});
				}
			}

		});

	}


	//数据分层下拉树
	function dataHierachyTree(dataHierachy,datasource){
		$.ajax({
			type: 'get',
			url:  '/manager/dataHierarchy/listLog',
			contentType: "application/json;charset=UTF-8",
			async: true,
			success: function(redata) {
				var temp = [];
				var res = redata.data;
				//初始化data，为data添加children
				var y = 0;
				for (var i = 0; i < res.length; i++) {
					var data = getData(res[i], res);
					if (data.parentId == null) {
						temp.push(data);
						y++;
					}
				}
				dataHierarchy = removePid(temp);
				// 加载数据分层
				var dataHierarchyTree = xmSelect.render({
					el: '#dataTiering',
					name: 'dataTiering',
					direction: 'down',
					model: {
						label: {
							type: 'text'
						}
					},
					radio: true,
					clickClose: true,
					tree: {
						show: true,
						strict: false,
						expandedKeys: [-1],
					},
					height: 'auto',
					data() {
						return dataHierarchy
					}
				});
				if (id !== null && id !== undefined) {
					dataHierarchyTree.setValue([datasource.data.dataTiering]);
				}
			}
		});

	}





	//接收来自父页面的点击信息，触发提交按钮点击事件
	window.addEventListener('message', function(event) {
		if (event.data === 'click') {
			$("#yes").trigger("click");
		}
		if (event.data === 'test') {
			$("#test").trigger("click");
		}
	});
	//接收来自父页面的点击信息，触发测试连接按钮点击事件
	// window.addEventListener('message', function(event) {
	//
	// });

	//监听测试
	form.on('submit(test)', function(data) {
		$.ajax({
			type: "post",
			url: FLOW_URL5 + "/strategy/checkConnection",
			dataType: 'json',
			cache: false,
			contentType: "application/json",
			data: JSON.stringify(data.field),
			success: function(redata) {
				if (redata.msg === '数据源测试连接成功') {
					window.parent.postMessage('test_success-' + redata.msg, '*');
				} else window.parent.postMessage('test_error-' + redata.msg, '*');
			},
			error: function(redata) {
				window.parent.postMessage('test_error-' + redata.msg, '*');
			}
		});
		return false;

	});
	//监听提交
	form.on('submit(submit)', function(data) {
		var sourceObj = document.getElementById('sourceId');
		var sourceIndex = sourceObj.selectedIndex;
		var sourceName = sourceObj[sourceIndex].text;
		data.field['source'] = sourceName;
		// layer.alert(JSON.stringify(data.field), {
		// 	title: '最终的提交信息'
		// });
		if (data.field['id'] != null && data.field['id'] !== '') {
			$.ajax({
				type: "post",
				url: FLOW_URL5 + "/strategy/editDataSource",
				dataType: 'text',
				cache: false,
				contentType: "application/json",
				data: JSON.stringify(data.field),
				success: function(redata) {
					window.parent.postMessage('update_success-' + redata, '*');
				},
				error: function(redata) {
					window.parent.postMessage('update_error-' + redata, '*');
				}
			});

		} else {
			$.ajax({
				type: "post",
				url: FLOW_URL5 + "/strategy/saveDataSource",
				dataType: 'text',
				cache: false,
				contentType: "application/json",
				data: JSON.stringify(data.field),
				success: function(redata) {
					window.parent.postMessage('success-' + redata, '*');
				},
				error: function(redata) {
					window.parent.postMessage('error-' + redata, '*');
				}
			});

		}
		return false;
	})
});

function getData(data, array) {
	var childrenArray = getChildrenData(data.id, array);
	data.children = childrenArray;
	for (var j in childrenArray) {
		getData(childrenArray[j].id, array);
	}
	return data;
}

function getChildrenData(id, array) {
	var newArray = [];
	for (var i in array) {
		if (array[i].parentId === id) {
			newArray.push(array[i]);
		}
	}
	return newArray;
}

function removePid(data) {
	var temp = data.map(item => {
		var children = item.children;
		if (children.length === 0) {
			return {
				name: item.myName,
				// value:item.myName,
				value: item.id,
				children: item.children
			}
		} else {
			return {
				name: item.myName,
				// value:item.myName,
				value: item.id,
				children: removePid(item.children)
			}
		}

	});
	return temp;
}
