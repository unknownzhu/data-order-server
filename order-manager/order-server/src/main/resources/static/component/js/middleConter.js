layui.use(['element', 'layer', 'form', 'layedit', 'jquery', 'table'], function() {
	var $ = layui.jquery;
	layer = layui.layer;
	form = layui.form;
	layedit = layui.layedit;
	element = layui.element;
	table = layui.table

	$(document).ready(function() {
		//数据应用平台
		tableUseFun();
		//采集平台
		// $.ajax({
		// 	url: INDEX_URL + '/sqlManager/sqlExec/query?charCterName=kettle&sqlName=sum-categorydata',
		// 	dataType: 'json',
		// 	type: 'get',
		// 	contentType: 'application/json',
		// 	success: function(data) {
		// 		console.log(data);
		// 	},
		// 	error: function(data) {
		// 		layer.msg(data.msg);
		// 	}
		// });
	});
});
//采集平台
$(function() {
	var mainGather = echarts.init(document.getElementById('mainGather'), 'infographic');
	let bgColor = '#fff';
	let color = ['#0E7CE2', '#FF8352', '#E271DE', '#F8456B', '#00FFFF', '#4AEAB0'];
	let echartData = [{
			name: "采集平台",
			value: "3720"
		},
		{
			name: "管控平台",
			value: "2920"
		},
		{
			name: "质量平台",
			value: "2200"
		},
		{
			name: "计算平台",
			value: "1420"
		},
		{
			name: "监控分析平台",
			value: "920"
		},
		{
			name: "API",
			value: "1020"
		},
		{
			name: "数据应用平台",
			value: "2020"
		}
	];

	let formatNumber = function(num) {
		let reg = /(?=(\B)(\d{3})+$)/g;
		return num.toString().replace(reg, ',');
	}
	let total = echartData.reduce((a, b) => {
		return a + b.value * 1
	}, 0);

	option = {
		backgroundColor: '',
		color: color,
		series: [{
			type: 'pie',
			radius: ['45%', '60%'],
			center: ['50%', '50%'],
			data: echartData,
			hoverAnimation: false,
			itemStyle: {
				normal: {
					borderColor: bgColor,
					borderWidth: 2
				}
			},
			labelLine: {
				normal: {
					length: 20,
					length2: 120,
					lineStyle: {
						color: '#e6e6e6'
					}
				}
			},
			label: {
				normal: {
					formatter: params => {
						return (
							'{icon|●}{name|' + params.name + '}{value|' +
							formatNumber(params.value) + '}'
						);
					},
					padding: [0, -100, 25, -100],
					rich: {
						icon: {
							fontSize: 16
						},
						name: {
							fontSize: 12,
							padding: [0, 10, 0, 4],
							color: '#666666'
						},
						value: {
							fontSize: 14,
							fontWeight: 'bold',
							color: '#333333'
						}
					}
				}
			},
		}]
	};
	mainGather.setOption(option);
	window.addEventListener('resize', function() {
		mainGather.resize();
	});
});
//管控平台
$(function() {
	var mainControl = echarts.init(document.getElementById('mainControl'), 'infographic');
	const targetCoord = [1000, 250]
	const curveness = 0.2
	const linesData = []
	const categories = [{
		itemStyle: {
			normal: {
				color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
					offset: 0,
					color: '#01acca'
				}, {
					offset: 1,
					color: '#5adbe7'
				}]),
			}
		},
		label: {
			normal: {
				fontSize: '14'
			}
		},
	}, {
		itemStyle: {
			normal: {
				color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
					offset: 0,
					color: '#ffb402'
				}, {
					offset: 1,
					color: '#ffdc84'
				}]),
			}
		},
		label: {
			normal: {
				fontSize: '14'
			}
		},
	}]

	const item = {
		name: "数据中心",
		value: targetCoord,
		symbolSize: 60,
		itemStyle: {
			normal: {
				color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
					offset: 0,
					color: '#157eff'
				}, {
					offset: 1,
					color: '#35c2ff'
				}]),
			}
		},
		label: {
			normal: {
				fontSize: '14'
			}
		},
	}

	const items = [{
		name: "子系统1",
		category: 0,
		active: true,
		value: [0, 0]
	}, {
		name: "子系统2",
		category: 0,
		active: true,
		value: [0, 200]
	}, {
		name: "子系统3",
		category: 0,
		active: true,
		value: [0, 400]
	}, {
		name: "子系统4",
		category: 0,
		active: true,
		value: [0, 600]
	}]

	const data = items.concat([item])

	items.forEach(function(el) {
		if (el.active) {
			linesData.push([{
				coord: el.value
			}, {
				coord: targetCoord
			}])
		}
	})

	const links = items.map((el) => {
		return {
			source: el.name,
			target: item.name,
			speed: el.speed,
			lineStyle: {
				normal: {
					color: el.speed ? '#12b5d0' : '#888BF7',
					curveness: curveness,
				}
			},
		}
	})

	option = {
		legend: [{
			formatter: function(name) {
				return echarts.format.truncateText(name, 100, '14px Microsoft Yahei', '…');
			},
			tooltip: {
				show: true
			},
			textStyle: {
				color: '#999'
			},
			selectedMode: false,
			right: 0,
			data: categories.map(function(el) {
				return el.name
			})
		}],
		xAxis: {
			show: false,
			type: 'value'
		},
		yAxis: {
			show: false,
			type: 'value'
		},
		series: [{
			type: 'graph',
			layout: 'none',
			coordinateSystem: 'cartesian2d',
			symbolSize: 30,
			z: 3,
			edgeLabel: {
				normal: {
					show: true,
					textStyle: {
						fontSize: 14
					},
					formatter: function(params) {
						let txt = ''
						if (params.data.speed !== undefined) {
							txt = params.data.speed
						}
						return txt
					},
				}
			},
			label: {
				normal: {
					show: true,
					position: 'bottom',
					color: '#5e5e5e'
				}
			},
			itemStyle: {
				normal: {
					shadowColor: 'none'
				},
				emphasis: {

				}
			},
			lineStyle: {
				normal: {
					width: 2,
					shadowColor: 'none'
				},
			},
			edgeSymbol: ['none', 'arrow'],
			edgeSymbolSize: 8,
			data: data,
			links: links,
			categories: categories
		}, {
			name: 'A',
			type: 'lines',
			coordinateSystem: 'cartesian2d',
			z: 1,
			effect: {
				show: true,
				smooth: false,
				trailLength: 0,
				symbol: "arrow",
				color: 'rgba(55,155,255,0.5)',
				symbolSize: 12
			},
			lineStyle: {
				normal: {
					curveness: curveness
				}
			},
			data: linesData
		}]
	};
	mainControl.setOption(option);
	window.addEventListener('resize', function() {
		mainControl.resize();
	});
});
// 质量平台
$(function() {
	var mainQuality = echarts.init(document.getElementById('mainQuality'), 'infographic');
	option = {
		backgroundColor: '',
		tooltip: {
			trigger: 'axis',
			axisPointer: {
				type: 'shadow'
			}
		},
		grid: {
			top: '12%',
			right: '3%',
			left: '10%',
			bottom: '15%'
		},
		xAxis: [{
			type: 'category',
			data: ['规则1', '规则2', '规则3', '规则4', '规则5', '规则6'],
			axisLine: {
				lineStyle: {
					color: 'rgba(0,0,0,1)'
				}
			},
			axisLabel: {
				color: '#000000',
				textStyle: {
					fontSize: 12
				},
				interval: -1,
				rotate: 20
			},
		}],
		yAxis: [{
			name: '单位：万元',
			axisLabel: {
				formatter: '{value}',
				color: '#000000',
			},
			axisLine: {
				show: false,
				lineStyle: {
					color: 'rgba(0,0,0,1)'
				}
			},
			splitLine: {
				lineStyle: {
					color: 'rgba(0,0,0,0.12)'
				}
			}
		}],
		series: [{
			name: '异常数量',
			type: 'bar',
			data: [5000, 2600, 1300, 1300, 1250, 1500],
			barWidth: '20px',
			itemStyle: {
				normal: {
					color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
						offset: 0,
						color: 'rgba(0,244,255,1)' // 0% 处的颜色
					}, {
						offset: 1,
						color: 'rgba(0,77,167,1)' // 100% 处的颜色
					}], false),
					barBorderRadius: [30, 30, 30, 30],
					shadowColor: 'rgba(0,160,221,1)',
					shadowBlur: 4,
				}
			}
		}]
	};
	mainQuality.setOption(option);
	window.addEventListener('resize', function() {
		mainQuality.resize();
	});
});
//监控分析平台
$(function() {
	var mainMonitoring = echarts.init(document.getElementById('mainMonitoring'), 'infographic');
	option = {
		backgroundColor: '',
		tooltip: {
			formatter: '{c}'
		},
		animationDurationUpdate: function(idx) {
			// 越往后的数据延迟越大
			return idx * 100;
		},
		animationEasingUpdate: 'bounceIn',
		color: ['#fff', '#fff', '#fff'],
		series: [{
			type: 'graph',
			layout: 'force',
			force: {
				repulsion: 300,
				gravity: 0.3,
				edgeLength: 90,
				layoutAnimation: true
			},
			roam: true,
			label: {
				normal: {
					show: true
				}
			},
			data: [{
				"name": "HBase",
				"value": '运行状况良好',
				"symbolSize": symbolSize,
				"draggable": true,
				"itemStyle": {
					"normal": {
						"borderColor": "rgb(105,172,19)",
						"borderWidth": 4,
						"shadowBlur": 100,
						"shadowColor": "rgb(105,172,19)",
						"color": "rgb(105,172,19)"
					}
				}
			}, {
				"name": "HDFS",
				"value": '运行状况良好',
				"symbolSize": symbolSize,
				"draggable": true,
				"itemStyle": {
					"normal": {
						"borderColor": "rgb(105,172,19)",
						"borderWidth": 4,
						"shadowBlur": 100,
						"shadowColor": "rgb(105,172,19)",
						"color": "rgb(105,172,19)"
					}
				}
			}, {
				"name": "Hive",
				"value": '运行状况不良',
				"symbolSize": symbolSize,
				"draggable": true,
				"itemStyle": {
					"normal": {
						"borderColor": "rgb(208,2,27)",
						"borderWidth": 4,
						"shadowBlur": 100,
						"shadowColor": "rgb(208,2,27)",
						"color": "rgb(208,2,27)"
					}
				}
			}, {
				"name": "Hue",
				"value": '运行状况存在隐患',
				"symbolSize": symbolSize,
				"draggable": true,
				"itemStyle": {
					"normal": {
						"borderColor": "rgb(237,178,52)",
						"borderWidth": 4,
						"shadowBlur": 100,
						"shadowColor": "rgb(237,178,52)",
						"color": "rgb(237,178,52)"
					}
				}
			}, {
				"name": "Impala",
				"value": '运行状况存在隐患',
				"symbolSize": symbolSize,
				"draggable": true,
				"itemStyle": {
					"normal": {
						"borderColor": "rgb(237,178,52)",
						"borderWidth": 4,
						"shadowBlur": 100,
						"shadowColor": "rgb(237,178,52)",
						"color": "rgb(237,178,52)"
					}
				}
			}, {
				"name": "Oozie",
				"value": '运行状况存在隐患',
				"symbolSize": symbolSize,
				"draggable": true,
				"itemStyle": {
					"normal": {
						"borderColor": "rgb(237,178,52)",
						"borderWidth": 4,
						"shadowBlur": 100,
						"shadowColor": "rgb(237,178,52)",
						"color": "rgb(237,178,52)"
					}
				}
			}, {
				"name": "Solr",
				"value": '运行状况不良',
				"symbolSize": symbolSize,
				"draggable": true,
				"itemStyle": {
					"normal": {
						"borderColor": "rgb(208,2,27)",
						"borderWidth": 4,
						"shadowBlur": 100,
						"shadowColor": "rgb(208,2,27)",
						"color": "rgb(208,2,27)"
					}
				}
			}, {
				"name": "Spark",
				"value": '运行状况不良',
				"symbolSize": symbolSize,
				"draggable": true,
				"itemStyle": {
					"normal": {
						"borderColor": "rgb(208,2,27)",
						"borderWidth": 4,
						"shadowBlur": 100,
						"shadowColor": "rgb(208,2,27)",
						"color": "rgb(208,2,27)"
					}
				}
			}, {
				"name": "Sqoop",
				"value": '运行状况不良',
				"symbolSize": symbolSize,
				"draggable": true,
				"itemStyle": {
					"normal": {
						"borderColor": "rgb(208,2,27)",
						"borderWidth": 4,
						"shadowBlur": 100,
						"shadowColor": "rgb(208,2,27)",
						"color": "rgb(208,2,27)"
					}
				}
			}, {
				"name": "YARN",
				"value": '运行状况良好',
				"symbolSize": symbolSize,
				"draggable": true,
				"itemStyle": {
					"normal": {
						"borderColor": "rgb(105,172,19)",
						"borderWidth": 4,
						"shadowBlur": 100,
						"shadowColor": "rgb(105,172,19)",
						"color": "rgb(105,172,19)"
					}
				}
			}, {
				"name": "ZooKeeper",
				"value": '运行状况良好',
				"symbolSize": symbolSize,
				"draggable": true,
				"itemStyle": {
					"normal": {
						"borderColor": "rgb(105,172,19)",
						"borderWidth": 4,
						"shadowBlur": 100,
						"shadowColor": "rgb(105,172,19)",
						"color": "rgb(105,172,19)"
					}
				}
			}]
		}]
	};
	mainMonitoring.setOption(option);
	window.addEventListener('resize', function() {
		mainMonitoring.resize();
	});
});
// API
$(function() {
	var mainApi = echarts.init(document.getElementById('mainApi'), 'infographic');
	option = {
		backgroundColor: '',
		tooltip: {
			trigger: 'axis'
		},
		grid: {
			top: '5%',
			left: '10%',
			right: '7%',
			bottom: '10%',
			// containLabel: true
		},
		xAxis: [{
			type: 'category',
			axisLine: {
				show: true
			},
			splitArea: {
				// show: true,
				color: '#f00',
				lineStyle: {
					color: '#f00'
				},
			},
			axisLabel: {
				color: '#000000'
			},
			splitLine: {
				show: false
			},
			boundaryGap: false,
			data: ['周一', '周二', '周三', '周四', '周五', '周六'],

		}],
		yAxis: [{
			type: 'value',
			// min: 0,
			// max: 140,
			splitNumber: 4,
			axisLabel: {
				show: true,
				textStyle: {
					color: '#000000',

				},
			},
			axisLine: {
				show: false,
				lineStyle: {
					color: 'rgba(0,0,0,1)'
				}
			},
			splitLine: {
				lineStyle: {
					color: 'rgba(0,0,0,0.12)'
				}
			},
			axisTick: {
				show: true,
			}
		}],
		series: [{
				name: '数据量1',
				type: 'line',
				smooth: true, //是否平滑
				showAllSymbol: true,
				// symbol: 'image://./static/images/guang-circle.png',
				symbol: 'circle',
				symbolSize: 5,
				lineStyle: {
					normal: {
						color: "#6c50f3",
						shadowColor: 'rgba(0, 0, 0, .3)',
						shadowBlur: 0,
						shadowOffsetY: 0,
						shadowOffsetX: 0,
					},
				},
				itemStyle: {
					color: "#6c50f3",
					borderColor: "#fff",
					borderWidth: 3,
					shadowColor: 'rgba(0, 0, 0, .3)',
					shadowBlur: 0,
					shadowOffsetY: 0,
					shadowOffsetX: 0,
				},
				tooltip: {
					show: true
				},
				areaStyle: {
					normal: {
						color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
								offset: 0,
								color: 'rgba(15, 57, 243, 0.9)'
							},
							{
								offset: 1,
								color: 'rgba(97, 78, 243, 0.1)'
							}
						], false),
						shadowColor: 'rgba(108,80,243, 0.9)',
						shadowBlur: 20
					}
				},
				data: [502.84, 205.97, 332.79, 281.55, 398.35, 1214.02]
			},
			{
				name: '数据量2',
				type: 'line',
				smooth: true, //是否平滑
				showAllSymbol: true,
				// symbol: 'image://./static/images/guang-circle.png',
				symbol: 'circle',
				symbolSize: 5,
				lineStyle: {
					normal: {
						color: "#00ca95",
						shadowColor: 'rgba(0, 0, 0, .3)',
						shadowBlur: 0,
						shadowOffsetY: 0,
						shadowOffsetX: 0,
					},
				},
				itemStyle: {
					color: "#00ca95",
					borderColor: "#fff",
					borderWidth: 3,
					shadowColor: 'rgba(0, 0, 0, .3)',
					shadowBlur: 0,
					shadowOffsetY: 0,
					shadowOffsetX: 0,
				},
				tooltip: {
					show: true
				},
				areaStyle: {
					normal: {
						color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
								offset: 0,
								color: 'rgba(0,202,149,0.7)'
							},
							{
								offset: 1,
								color: 'rgba(0,202,149,0)'
							}
						], false),
						shadowColor: 'rgba(0,202,149, 0.9)',
						shadowBlur: 0
					}
				},
				data: [281.55, 398.35, 214.02, 179.55, 289.57, 356.14],
			},
		]
	};
	mainApi.setOption(option);
	window.addEventListener('resize', function() {
		mainApi.resize();
	});
});
//数据应用平台
function tableUseFun() {
	var tableIns = table.render({
		elem: '#useTable',
		height: 'full',
		title: '列表',
		// text: {
		// 	none: '无数据'
		// },
		id: 'idUseTable',
		method: 'get',
		url: 'useTable.json',
		// where: {
		// 	sourceId: sourceId,
		// 	keywords: keywords
		// },
		method: 'get',
		page: false,
		limit: 10,
		limits: [10, 20, 50, 100],
		skin: 'nob',
		even: true,
		request: {
			pageName: 'page',
			limitName: 'rows'
		},
		parseData: function(res) { //res 即为原始返回的数据
			return {
				"code": 0, //解析接口状态
				"msg": res.msg, //解析提示文本
				"count": res.count, //解析数据长度
				"data": res.data //解析数据列表
			};
		},
		cols: [
			[ //表头
				{
					field: 'dataClassify',
					title: '数据分类',
					width: 150
				}, {
					field: 'dataAmount',
					title: '数据量',
					width: 150
				}, {
					field: 'updateTime',
					title: '更新日期',
					width: 250
				}
			]
		]
	});
}
