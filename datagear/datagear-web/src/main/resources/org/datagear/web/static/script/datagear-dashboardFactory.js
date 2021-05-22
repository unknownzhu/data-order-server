/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

/**
 * 看板工厂，用于初始化看板对象，为看板对象添加功能函数。
 * 全局变量名：window.dashboardFactory
 * 
 * 加载时依赖：
 *   无
 * 
 * 运行时依赖:
 *   jquery.js
 *   datagear-chartFactory.js
 *   datagear-chartSetting.js
 * 
 * 
 * 此看板工厂支持为<body>元素添加elementAttrConst.DASHBOARD_LISTENER属性，用于指定看板监听器JS对象名，
 * 看板监听器格式参考dashboardBase.listener()函数说明。
 * 
 * 此看板工厂支持为<body>元素添加elementAttrConst.MAP_URLS属性，用于扩展或替换内置地图，格式为：
 * {customMap:'map/custom.json', china: 'map/myChina.json'}
 * 
 * 此看板工厂支持为图表元素添加elementAttrConst.LINK属性，用于设置图表联动，具体格式参考chartBase.links函数说明。
 * 
 * 此看板工厂支持为<body>元素、图表元素添加elementAttrConst.AUTO_RESIZE属性，用于设置图表是否自动调整大小。
 * 
 * 此看板工厂支持为<body>元素、图表元素添加elementAttrConst.UPDATE_GROUP属性，用于设置图表更新ajax分组。
 * 
 * 此看板工厂支持将页面内添加了elementAttrConst.DASHBOARD_FORM属性的<form>元素构建为看板表单，具体参考dashboardBase._initForms函数说明。
 * 
 */
(function(global)
{
	/**图表工厂*/
	var chartFactory = (global.chartFactory || (global.chartFactory = {}));
	/**图表对象基类*/
	var chartBase = (chartFactory.chartBase || (chartFactory.chartBase = {}));
	/**图表状态常量*/
	var chartStatusConst = (chartFactory.chartStatusConst || (chartFactory.chartStatusConst = {}));
	/**DOM元素属性常量*/
	var elementAttrConst = (chartFactory.elementAttrConst || (chartFactory.elementAttrConst = {}));
	
	/**看板工厂*/
	var dashboardFactory = (global.dashboardFactory || (global.dashboardFactory = {}));
	/**看板对象基类*/
	var dashboardBase = (dashboardFactory.dashboardBase || (dashboardFactory.dashboardBase = {}));
	
	//----------------------------------------
	// chartStatusConst开始
	//----------------------------------------
	
	/**图表状态：需要参数值*/
	chartStatusConst.PARAM_VALUE_REQUIRED = "PARAM_VALUE_REQUIRED";
	
	/**图表状态：渲染出错*/
	chartStatusConst.RENDER_ERROR = "RENDER_ERROR";
	
	/**图表状态：更新出错*/
	chartStatusConst.UPDATE_ERROR = "UPDATE_ERROR";
	
	//----------------------------------------
	// chartStatusConst结束
	//----------------------------------------
	
	//----------------------------------------
	// elementAttrConst开始
	//----------------------------------------
	
	/**看板监听器*/
	elementAttrConst.DASHBOARD_LISTENER = "dg-dashboard-listener";
	
	/**看板表单*/
	elementAttrConst.DASHBOARD_FORM = "dg-dashboard-form";
	
	/**图表地图URL映射表*/
	elementAttrConst.MAP_URLS = "dg-chart-map-urls";
	
	/**图表联动*/
	elementAttrConst.LINK = "dg-chart-link";
	
	/**图表自动调整尺寸*/
	elementAttrConst.AUTO_RESIZE = "dg-chart-auto-resize";
	
	/**图表更新分组*/
	elementAttrConst.UPDATE_GROUP = "dg-chart-update-group";
	
	//----------------------------------------
	// elementAttrConst结束
	//----------------------------------------
	
	/**
	 * 更新看板数据配置，需与后台保持一致。
	 */
	dashboardFactory.updateDashboardConfig = (dashboardFactory.updateDashboardConfig ||
			{
				//org.datagear.web.controller.AbstractDataAnalysisController.UPDATE_DASHBOARD_PARAM_DASHBOARD_ID
				dashboardIdParamName: "dashboardId",
				//org.datagear.web.controller.AbstractDataAnalysisController.UPDATE_DASHBOARD_PARAM_CHART_IDS
				chartIdsParamName: "chartIds",
				//org.datagear.web.controller.AbstractDataAnalysisController.UPDATE_DASHBOARD_PARAM_CHARTS_PARAM_VALUES
				chartsParamValuesParamName: "chartsParamValues"
			});
	
	/**
	 * 异步加载图表配置，需与后台保持一致。
	 */
	dashboardFactory.loadChartConfig = (dashboardFactory.loadChartConfig ||
			{
				//org.datagear.web.controller.DashboardController.LOAD_CHART_PARAM_DASHBOARD_ID
				dashboardIdParamName: "dashboardId",
				//org.datagear.web.controller.DashboardController.LOAD_CHART_PARAM_CHART_WIDGET_ID
				chartWidgetIdParamName: "chartWidgetId",
				//org.datagear.web.controller.DashboardController.LOAD_CHART_PARAM_CHART_ELEMENT_ID
				chartElementIdParamName: "chartElementId"
			});

	/**
	 * 看板使用的渲染上下文属性名。
	 */
	dashboardFactory.renderContextAttrs =
	{
		//必须，看板主题，org.datagear.analysis.DashboardTheme
		dashboardTheme: "dashboardTheme",
		//必须，Web上下文，org.datagear.analysis.support.html.HtmlTplDashboardRenderAttr.WebContext
		webContext: "webContext"
	};
	
	/**
	 * 更新图表数据ajax请求的重试秒数，当更新图表数据ajax请求出错后，会在过这些秒后重试请求。
	 */
	dashboardFactory.UPDATE_AJAX_RETRY_SECONDS = 5;
	
	/**
	 * 循环监视处理图表状态间隔毫秒数。
	 */
	dashboardFactory.HANDLE_CHART_INTERVAL_MS = 1;
	
	/**
	 * 初始化指定看板对象。
	 * 
	 * @param dashboard 看板对象
	 */
	dashboardFactory.init = function(dashboard)
	{
		//如果未设置图表主题，则采用看板主题里定义的图表主题
		if(chartFactory.renderContextAttr(dashboard.renderContext,
				chartFactory.renderContextAttrs.chartTheme) == null)
		{
			var dashboardTheme = chartFactory.renderContextAttr(dashboard.renderContext,
					dashboardFactory.renderContextAttrs.dashboardTheme);
			chartFactory.renderContextAttr(dashboard.renderContext,
					chartFactory.renderContextAttrs.chartTheme, dashboardTheme.chartTheme);
		}
		
		chartFactory.initRenderContext(dashboard.renderContext);
		
		this._initOverwriteChartBase();
		this._initChartMapURLs();
		$.extend(dashboard, this.dashboardBase);
		dashboard.init();
		
		//开启心跳，避免会话超时
		var webContext = chartFactory.renderContextAttrWebContext(dashboard.renderContext);
		var heartbeatURL = chartFactory.toWebContextPathURL(webContext, webContext.attributes.heartbeatURL);
		this.startHeartBeat(heartbeatURL);
	};
	
	/**
	 * 重写chartFactory.chartBase的部分逻辑。
	 */
	dashboardFactory._initOverwriteChartBase = function()
	{
		//此方法不能重复执行，这里确保只执行一次
		if(chartBase._initSuper != null)
			return false;
		
		chartBase._initSuper = chartBase.init;
		chartBase._postProcessRenderedSuper = chartBase._postProcessRendered;
		
		chartBase.init = function()
		{
			this._initLinks();
			this._initAutoResize();
			this._initUpdateGroup();
			this._initSuper();
		};
		
		chartBase._postProcessRendered = function()
		{
			this.bindLinksEventHanders(this.links());
			this._postProcessRenderedSuper();
		};
	};
	
	/**
	 * 初始化chartFactory.chartMapURLs。
	 * 它将body元素的elementAttrConst.MAP_URLS属性值设置为自定义地图JSON地址映射表。
	 */
	dashboardFactory._initChartMapURLs = function()
	{
		for(var i=0; i<this.builtinChartMaps.length; i++)
		{
			var urlNames = this.builtinChartMaps[i];
			for(var j=0; j<urlNames.names.length; j++)
				chartFactory.chartMapURLs[urlNames.names[j]] = this.builtinChartMapBaseURL + urlNames.url;
		}
		
		var mapUrls = $(document.body).attr(elementAttrConst.MAP_URLS);
		
		if(mapUrls)
			mapUrls = chartFactory.evalSilently(mapUrls);
		
		$.extend(chartFactory.chartMapURLs, mapUrls);
	};
	
	/**
	 * 开始执行心跳请求。
	 * @param heartbeatURL 心跳URL，可选，初次调用时需设置
	 */
	dashboardFactory.startHeartBeat = function(heartbeatURL)
	{
		if(this._heartbeatStatus == "run")
			return false;
		
		this._heartbeatStatus = "run";
		
		this.heartbeatURL = (heartbeatURL == undefined ? this.heartbeatURL : heartbeatURL);
		this._heartBeatAjaxRequestTimeout();
		
		return true;
	};
	
	/**
	 * 停止执行心跳请求。
	 */
	dashboardFactory.stopHeartBeat = function()
	{
		this._heartbeatStatus = "stop";
		
		if(this._heartbeatTimeoutId != null)
			clearTimeout(this._heartbeatTimeoutId);
	};
	
	dashboardFactory._heartBeatAjaxRequestTimeout = function()
	{
		var interval = (this.heartbeatInterval || 1000*60*5);
		
		var _thisFactory = this;
		
		this._heartbeatTimeoutId = setTimeout(function()
		{
			if(_thisFactory._heartbeatStatus == "run")
			{
				var url = _thisFactory.heartbeatURL;
				
				if(url == null)
					throw new Error("[dashboardFactory.heartbeatURL] must be set");
				
				$.ajax({
					type : "GET",
					cache: false,
					url : url,
					complete : function()
					{
						if(_thisFactory._heartbeatStatus == "run")
							_thisFactory._heartBeatAjaxRequestTimeout();
					}
				});
			}
		},
		interval);
	};
	
	/**
	 * 获取对象的指定属性路径的值。
	 * 
	 * @param obj
	 * @param propertyPath 属性路径，示例：order、order.product、[0].name、order['product'].name
	 * @return 属性路径值，属性路径不存在则返回undefined
	 */
	dashboardFactory.getPropertyPathValue = function(obj, propertyPath)
	{
		if(obj == null)
			return undefined;
		
		var value = undefined;
		
		//简单属性值
		value = obj[propertyPath];
		
		if(value !== undefined)
			return value;
		
		//构建eval表达式
		if(propertyPath.charAt(0) == '[')
			propertyPath = "obj" + propertyPath;
		else
			propertyPath = "obj." + propertyPath;
		
		try
		{
			value = eval(propertyPath);
		}
		catch(e)
		{
			value = undefined;
		}
		
		return value;
	};
	
	//----------------------------------------
	// chartBase扩展开始
	//----------------------------------------
	
	/**
	 * 初始化图表联动设置。
	 * 此方法从图表元素的elementAttrConst.LINK属性获取联动设置。
	 */
	chartBase._initLinks = function()
	{
		var links = this.elementJquery().attr(elementAttrConst.LINK);
		
		if(!links)
			return;
		
		links = chartFactory.evalSilently(links);
		
		if(!links)
			return;
		
		this.links(links);
	};
	
	/**
	 * 初始化图表自动调整大小设置。
	 * 此方法从body元素、图表元素的elementAttrConst.AUTO_RESIZE属性获取联动设置。
	 */
	chartBase._initAutoResize = function()
	{
		var autoResize = this.elementJquery().attr(elementAttrConst.AUTO_RESIZE);
		
		if(autoResize == null)
			autoResize = $(document.body).attr(elementAttrConst.AUTO_RESIZE);
		
		this.autoResize(autoResize == "true");
	};

	/**
	 * 初始化图表更新分组。
	 * 此方法从body元素、图表元素的elementAttrConst.UPDATE_GROUP属性获取更新分组设置。
	 */
	chartBase._initUpdateGroup = function()
	{
		var updateGroup = this.elementJquery().attr(elementAttrConst.UPDATE_GROUP);
		
		if(updateGroup == null)
			updateGroup = $(document.body).attr(elementAttrConst.UPDATE_GROUP);
		
		this.updateGroup(updateGroup);
	};
	
	/**
	 * 获取/设置初始图表联动设置对象数组。
	 * 联动设置对象格式为：
	 * {
	 *   //可选，联动触发事件类型、事件类型数组，默认为"click"
	 *   trigger: "..."、["...", ...],
	 *   
	 *   //必选，联动目标图表元素ID、ID数组
	 *   target: "..."、["...", ...],
	 *   
	 *   //可选，联动数据参数映射表
	 *   data:
	 *   {
	 *     //ChartEvent对象的"data"、"orginalData"对象的属性名 : 目标数据集参数的映射索引、映射索引数组
	 *     "..." : 图表数据集参数索引对象、[ 图表数据集参数索引对象, ... ],
	 *     ...
	 *   }
	 * }
	 * 
	 * 图表数据集参数索引对象格式参考dashboardBase.batchSetDataSetParamValues函数相关说明，
	 * 其中value函数的sourceValueContext参数为图表事件对象（chartEvent）对象。
	 * 
	 * 图表渲染器实现相关：
	 * 图表渲染器应实现on函数，以支持“dg-chart-link”特性。
	 * 
	 * @param links 可选，要设置的图表联动设置对象、数组，没有则执行获取操作。
	 */
	chartBase.links = function(links)
	{
		if(links === undefined)
			return this._links;
		else
		{
			if(links && !$.isArray(links))
				links = [ links ];
			
			this._links = links;
		}
	};
	
	/**
	 * 获取/设置图表是否自动调整大小。
	 * 
	 * 图表渲染器实现相关：
	 * 图表渲染器应实现resize函数，以支持“dg-chart-auto-resize”特性。
	 * 
	 * @param autoResize 可选，设置为是否自动调整大小，没有则执行获取操作。
	 */
	chartBase.autoResize = function(autoResize)
	{
		if(autoResize === undefined)
			return (this._autoResize == true);
		else
			this._autoResize = autoResize;
	};
	
	/**
	 * 获取/设置图表更新分组。
	 * 如果图表从服务端加载数据比较耗时，可以为其指定一个分组标识，让其使用单独的ajax请求加载数据。
	 * 注意：相同分组的图表将使用同一个ajax请求。
	 * 
	 * @param group 可选，设置更新分组，没有则执行获取操作返回非null值。
	 */
	chartBase.updateGroup = function(group)
	{
		if(group === undefined)
			return (this._updateGroup == null ? "" : this._updateGroup);
		else
			this._updateGroup = group;
	};
	
	/**
	 * 为指定图表联动设置绑定事件处理函数。
	 * 
	 * 图表渲染器实现相关：
	 * 图表渲染器应实现on函数，以支持此特性。
	 * 
	 * @param links 图表联动设置对象、数组，格式参考chartBase.links函数说明
	 * @return 绑定的事件处理函数对象数组，格式为：[ { eventType: "...", eventHandler: function(chartEvent){ ... } }, ... ]
	 */
	chartBase.bindLinksEventHanders = function(links)
	{
		this._assertActive();
		
		if(!links)
			return [];
		
		if(!$.isArray(links))
			links = [ links ];
		
		var ehs = [];
		
		var triggers = this._resolveLinksTriggers(links);
		var _thisChart = this;
		
		for(var i=0; i<triggers.length; i++)
		{
			var eh =
			{
				eventType: triggers[i],
				eventHandler: function(chartEvent)
				{
					_thisChart.handleChartEventLink(chartEvent, links);
				}
			};
			
			this.on(eh.eventType, eh.eventHandler);
			
			ehs.push(eh);
		}
		
		return ehs;
	};
	
	/**
	 * 解析不重复的联动设置触发事件数组。
	 */
	chartBase._resolveLinksTriggers = function(links)
	{
		var triggers = [];
		
		for(var i=0; i<links.length; i++)
		{
			var myTriggers = (links[i].trigger || "click");
			if(!$.isArray(myTriggers))
				myTriggers = [ myTriggers ];
			
			for(var j=0; j<myTriggers.length; j++)
			{
				if($.inArray(myTriggers[j], triggers) < 0)
					triggers.push(myTriggers[j]);
			}
		}
		
		return triggers;
	};
	
	/**
	 * 处理指定图表事件的图表联动操作。
	 * 此方法根据图表联动设置对象，将图表事件数据传递至目标图表数据集参数值，然后请求刷新图表数据。
	 * 
	 * @param chartEvent 图表事件对象
	 * @param links 图表联动设置对象、数组，格式参考chartBase.links函数说明
	 */
	chartBase.handleChartEventLink = function(chartEvent, links)
	{
		this._assertActive();
		
		if(!links)
			return false;
		
		if(!$.isArray(links))
			links = [ links ];
		
		var dashboard = this.dashboard;
		var targetCharts = [];
		
		var batchSource =
		{
			data: this.eventData(chartEvent),
			originalData: this.eventOriginalData(chartEvent),
			getValue: function(name)
			{
				//需支持属性路径格式的name
				var val = dashboardFactory.getPropertyPathValue(this.data, name);
				if(val === undefined && this.originalData != null)
					val = dashboardFactory.getPropertyPathValue(this.originalData, name);
				
				return val;
			}
		};
		
		for(var i=0; i<links.length; i++)
		{
			var link = links[i];
			
			if(!this._isLinkTriggerableByEvent(link, chartEvent))
				continue;
			
			var myTargetCharts = dashboard.batchSetDataSetParamValues(batchSource, link, chartEvent);
			
			for(var j=0; j<myTargetCharts.length; j++)
			{
				if($.inArray(myTargetCharts[j], targetCharts) < 0)
					targetCharts.push(myTargetCharts[j]);
			}
		}
		
		for(var i=0; i<targetCharts.length; i++)
			targetCharts[i].refreshData();
	};
	
	chartBase._isLinkTriggerableByEvent = function(link, chartEvent)
	{
		var eventType = chartEvent.type;
		
		if(!eventType)
		{
			return false;
		}
		else if(!link.trigger)
		{
			//默认为点击事件
			return (eventType == "click");
		}
		else if($.isArray(link.trigger))
		{
			return ($.inArray(eventType, link.trigger) >= 0);
		}
		else
			return (link.trigger == eventType);
	};
	
	/**
	 * 从服务端获取并刷新图表数据。
	 */
	chartBase.refreshData = function()
	{
		this._assertActive();
		
		if(!this.isDataSetParamValueReady())
			chartFactory.logException("Chart '"+this.elementId+"' has required but unset data set param value");
		
		this.statusPreUpdate(true);
	};
	
	//----------------------------------------
	// chartBase扩展结束
	//----------------------------------------
	
	
	//----------------------------------------
	// dashboardBase start
	//----------------------------------------
	
	/**
	 * 初始化看板。
	 */
	dashboardBase.init = function()
	{
		if(this._inited == true)
			throw new Error("Dashboard has been initialized");
		this._inited = true;
		
		this._initListener();
		this._initForms();
		this._initChartResizeHandler();
		this._initCharts();
	};
	
	/**
	 * 初始化看板的监听器。
	 * 它将body元素的elementAttrConst.DASHBOARD_LISTENER属性值设置为看板的监听器。
	 */
	dashboardBase._initListener = function()
	{
		var listener = $(document.body).attr(elementAttrConst.DASHBOARD_LISTENER);
		
		if(listener)
			listener = chartFactory.evalSilently(listener);
		
		//@deprecated 用于兼容1.5.0版本的dashboardRenderer设计，未来版本会移除
		else if(typeof(dashboardRenderer) != "undefined")
			listener = dashboardRenderer.listener;
		//@deprecated 用于兼容1.5.0版本的dashboardRenderer设计，未来版本会移除
		
		if(listener)
			this.listener(listener);
	};
	
	/**
	 * 初始化看板表单。
	 * 它将看板页面内的所有<form dg-dashboard-form="...">元素构建为看板表单。
	 */
	dashboardBase._initForms = function()
	{
		var $forms = $("form[dg-dashboard-form]", document.body);
		
		var _this = this;
		
		$forms.each(function()
		{
			_this.renderForm(this);
		});
	};
	
	/**
	 * 初始化看板的所有图表。
	 */
	dashboardBase._initCharts = function()
	{
		if(!this.charts)
			return;
		
		for(var i=0; i<this.charts.length; i++)
			this._initChart(this.charts[i]);
	};
	
	/**
	 * 初始化看板的单个图表。
	 */
	dashboardBase._initChart = function(chart)
	{
		chart.renderContext = this.renderContext;
		chart.dashboard = this;
		
		chartFactory.init(chart);
		
		//如果图表没有定义监听器，则使用代理看板监听器
		if(!chart.listener())
			chart.listener(this._getDelegateChartListener());
	};
	
	/**
	 * 初始化自动调整图表大小处理器。
	 */
	dashboardBase._initChartResizeHandler = function()
	{
		var thisDashboard = this;
		
		$(window).resize(function()
		{
			setTimeout(function()
			{
				var charts = (thisDashboard.charts || []);
				
				for(var i =0; i<charts.length; i++)
				{
					var chart = charts[i];
					
					if(chart.autoResize() && chart.isActive())
						chart.resize();
				}
			},
			100);
		});
	};
	
	/**
	 * 获取/设置初始看板监听器。
	 * 看板监听器格式为：
	 * {
	 *   //可选，渲染看板完成回调函数
	 *   render: function(dashboard){ ... },
	 *   //可选，渲染图表完成回调函数
	 *   renderChart: function(dashboard, chart){ ... },
	 *   //可选，更新图表数据完成回调函数
	 *   updateChart: function(dashboard, chart, results){ ... },
	 *   //可选，渲染看板前置回调函数，返回false将阻止渲染看板
	 *   onRender: function(dashboard){ ... },
	 *   //可选，渲染图表前置回调函数，返回false将阻止渲染图表
	 *   onRenderChart: function(dashboard, chart){ ... },
	 *   //可选，更新图表数据前置回调函数，返回false将阻止更新图表数据
	 *   onUpdateChart: function(dashboard, chart, results){ ... }
	 * }
	 * 
	 * @param listener 可选，要设置的监听器对象，没有则执行获取操作
	 */
	dashboardBase.listener = function(listener)
	{
		if(listener === undefined)
			return this._listener;
		
		this._listener = listener;
		
		//需要同步设置全局图表监听器
		var chartListener = this._getDelegateChartListener();
		
		var dashboard = this;
		
		if(listener && listener.renderChart)
			chartListener.render = function(chart){ listener.renderChart(dashboard, chart); };
		else
			chartListener.render = undefined;
		
		if(listener && listener.updateChart)
			chartListener.update = function(chart, results){ listener.updateChart(dashboard, chart, results); };
		else
			chartListener.update = undefined;
		
		if(listener && listener.onRenderChart)
			chartListener.onRender = function(chart){ return listener.onRenderChart(dashboard, chart); };
		else
			chartListener.onRender = undefined;
		
		if(listener && listener.onUpdateChart)
			chartListener.onUpdate = function(chart, results){ return listener.onUpdateChart(dashboard, chart, results); };
		else
			chartListener.onUpdate = undefined;
	};
	
	/**
	 * 获取看板的代理图表监听器。
	 * 为了确保任意时刻设置看板监听器（dashboard.listener(...)）都能传递至图表，所以此方法应始终返回不为null且引用不变的对象。
	 */
	dashboardBase._getDelegateChartListener = function()
	{
		var chartListener = (this._delegateChartListener || (this._delegateChartListener = {}));
		return chartListener;
	};
	
	/**
	 * 获取图表，没有则返回undefined。
	 * 
	 * @param chartInfo 图表信息：图表HTML元素ID、图表ID、图表索引
	 */
	dashboardBase.getChart = function(chartInfo)
	{
		var index = this.getChartIndex(chartInfo);
		
		return (index < 0 ? undefined : this.charts[index]);
	};
	
	/**
	 * 获取所有图表数组。
	 */
	dashboardBase.getAllCharts = function()
	{
		return (this.charts || []);
	};
	
	/**
	 * 添加已经初始化的图表。
	 * 
	 * @param chart 图表对象
	 */
	dashboardBase.addChart = function(chart)
	{
		var charts = (this.charts || []);
		this.charts = charts.concat(chart);
	};
	
	/**
	 * 删除图表。
	 * 
	 * @param chartInfo 图表对象、图表HTML元素ID、图表ID、图表索引
	 * @param doDestory 选填参数，是否销毁图表，默认为true
	 * @return 移除的图表对象，或者图表未找到时为undefined
	 */
	dashboardBase.removeChart = function(chartInfo, doDestory)
	{
		var newCharts = (this.charts ? [].concat(this.charts) : []);
		var index = this.getChartIndex(chartInfo, newCharts);
		
		if(index < 0)
			return undefined;
		
		var removeds = newCharts.splice(index, 1);
		this.charts = newCharts;
		
		if(doDestory != false)
			this._destroyChart(removeds[0]);
		
		return removeds[0];
	};
	
	dashboardBase._destroyChart = function(chart)
	{
		chart.destroy();
	};
	
	/**
	 * 刷新图表数据。
	 * 
	 * @param chartInfo 图表对象、图表HTML元素ID、图表ID、图表索引
	 */
	dashboardBase.refreshData = function(chartInfo)
	{
		var chart = this.getChart(chartInfo);
		chart.refreshData();
	};
	
	/**
	 * 重新调整指定图表尺寸。
	 * 
	 * @param chartInfo 图表对象、图表HTML元素ID、图表ID、图表索引
	 */
	dashboardBase.resizeChart = function(chartInfo)
	{
		var chart = this.getChart(chartInfo);
		chart.resize();
	};
	
	/**
	 * 重新调整所有图表尺寸。
	 */
	dashboardBase.resizeAllCharts = function()
	{
		var charts = (this.charts || []);
		
		for(var i=0; i<charts.length; i++)
			charts[i].resize();
	};
	
	/**
	 * 获取图表索引号。
	 * 
	 * @param chartInfo 图表信息：图表对象、图表HTML元素ID、图表ID、图表索引
	 * @param charts 选填，查找的图表数组，如果不设置，则取this.charts
	 */
	dashboardBase.getChartIndex = function(chartInfo, charts)
	{
		if(charts === undefined)
			charts = this.charts;
		
		if(!charts)
			return -1;
		
		for(var i=0; i<charts.length; i++)
		{
			if(charts[i] === chartInfo
					|| charts[i].elementId === chartInfo
					|| charts[i].id === chartInfo
					|| i === chartInfo)
				return i;
		}
		
		return -1;
	};
	
	/**
	 * 获取/设置渲染上下文的属性值。
	 * 
	 * @param attrName
	 * @param attrValue 要设置的属性值，可选，不设置则执行获取操作
	 */
	dashboardBase.renderContextAttr = function(attrName, attrValue)
	{
		return chartFactory.renderContextAttr(this.renderContext, attrName, attrValue);
	};
	
	/**
	 * 渲染看板表单。
	 * 看板表单提交时会自动将表单输入值设置为目标图表的数据集参数值，并刷新图表。
	 * 
	 * 表单配置对象格式为：
	 * {
	 *   //必选，表单输入项对象、数组
	 *   items: 表单输输入项对象 或者 [ 表单输输入项对象, ... ],
	 *   //可选，表单提交操作时执行的联动图表设置
	 *   link: 图表联动设置对象,
	 *   //可选，表单提交按钮文本
	 *   submitText: "...",
	 *   //表单渲染完成回调函数
	 *   render: function(form){ ... }
	 * }
	 * 
	 * 表单输输入项对象格式为：
	 * {
	 *   //必选，输入项名称
	 *   name: "...",
	 *   //可选，默认值
	 *   value: ...,
	 *   //可选，输入项标签
	 *   label: "...",
	 *   //可选，输入项类型，参考chartSetting.DataSetParamDataType，默认值为：chartSetting.DataSetParamDataType.STRING
	 *   type: "...",
	 *   //可选，是否必须，默认为false
	 *   required: true || false,
	 *   //可选，输入框类型，参考chartSetting.DataSetParamInputType，默认值为：chartSetting.DataSetParamInputType.TEXT
	 *   inputType: "...",
	 *   //可选，输入框配置，参考chartSetting.renderDataSetParamValueForm函数说明
	 *   inputPayload: ...,
	 *   //可选，输入项的联动数据映射设置
	 *   link: 图表数据集参数索引对象、[ 图表数据集参数索引对象, ... ]
	 * }
	 * 或者，简写为其name属性值。
	 * 
	 * 图表联动设置对象格式为：
	 * {
	 *   //必选，联动目标图表元素ID、ID数组
	 *   target: "..."、["...", ...],
	 *   //可选，联动数据参数映射表
	 *   data:
	 *   {
	 *     表单输入项名称 : 图表数据集参数索引对象、[ 图表数据集参数索引对象, ... ],
	 *     ...
	 * }
	 * 或者，简写为图表联动设置对象的target属性值。
	 * 
	 * 图表数据集参数索引对象格式参考dashboardBase.batchSetDataSetParamValues函数相关说明，其中value函数的sourceValueContext参数为：表单数据对象、表单DOM对象。
	 * 
	 * @param form 要渲染的<form>表单元素、Jquery对象，表单结构允许灵活自定义，具体参考chartSetting.renderDataSetParamValueForm
	 * @param config 可选，表单配置对象，默认为表单元素的elementAttrConst.DASHBOARD_FORM属性值
	 */
	dashboardBase.renderForm = function(form, config)
	{
		form = $(form);
		
		form.addClass("dg-dashboard-form");
		
		if(!config)
			config = chartFactory.evalSilently(form.attr(elementAttrConst.DASHBOARD_FORM), {});
		
		var _thisDashboard = this;
		var bindBatchSetName = "dataGearBatchSet";
		
		config = $.extend(
		{
			submit: function(formData)
			{
				var thisForm = this;
				var batchSet = $(thisForm).data(bindBatchSetName);
				
				if(batchSet)
				{
					var charts = _thisDashboard.batchSetDataSetParamValues(formData, batchSet, [ formData, thisForm ]);
					
					for(var i=0; i<charts.length; i++)
						charts[i].refreshData();
				}
			}
		},
		config);
		
		//构建用于批量设置数据集参数值的对象
		var batchSet = undefined;
		if(config.link)
		{
			var link = config.link;
			
			//转换简写格式
			if(typeof(link) == "string" || $.isArray(link))
				link = { target: link };
			
			batchSet =
			{
				target: link.target,
				//新构建data对象，因为可能会在下面被修改
				data: (link.data ? $.extend({}, link.data) : {})
			};
		}
		
		var items = [];
		var defaultValues = {};
		
		var sourceItems = (config.items || []);
		if(!$.isArray(sourceItems))
			sourceItems = [ sourceItems ];
		
		for(var i=0; i<sourceItems.length; i++)
		{
			var item = sourceItems[i];
			
			if(typeof(item) == "string")
				item = { name: item };
			else
				//确保不影响初始对象
				item = $.extend({}, item);
			
			if(!item.type)
				item.type = chartFactory.chartSetting.DataSetParamDataType.STRING;
			
			items.push(item);
			
			if(item.value != null)
				defaultValues[item.name] = item.value;
			
			//合并输入项的link设置
			if(item.link != null && batchSet && batchSet.data)
				batchSet.data[item.name] = item.link;
		}
		
		if(batchSet)
			form.data(bindBatchSetName, batchSet);
		
		config.paramValues = defaultValues;
		
		var dashboardTheme = this.renderContextAttr(dashboardFactory.renderContextAttrs.dashboardTheme);
		config.chartTheme = dashboardTheme.chartTheme;
		
		chartFactory.chartSetting.renderDataSetParamValueForm(form, items, config);
	};
	
	/**
	 * 渲染看板。
	 */
	dashboardBase.render = function()
	{
		if(this._rendered == true)
			throw new Error("Dashboard has been rendered");
		this._rendered = true;
		
		var doRender = true;
		
		var listener = this.listener();
		if(listener && listener.onRender)
		  doRender = listener.onRender(this);
		
		if(doRender != false)
		{
			this.startHandleCharts();
			
			if(listener && listener.render)
				  listener.render(this);
		}
	};
	
	/**
	 * 是否正在监视处理看板图表。
	 */
	dashboardBase.isHandlingCharts = function()
	{
		return (this._doHandlingCharts == true);
	};
	
	/**
	 * 开始监视处理看板图表，循环查看它们的状态，执行相应操作：
	 * 如果isWaitForRender(chart)，则执行chart.render()；
	 * 如果isWaitForUpdate(chart)且图表的所有数据集参数值都齐备，则执行chart.update()。
	 */
	dashboardBase.startHandleCharts = function()
	{
		if(this._doHandlingCharts == true)
			return false;
		
		this._doHandlingCharts = true;
		this._doHandleCharts();
		
		return true;
	};
	
	/**
	 * 停止监视处理看板图表。
	 */
	dashboardBase.stopHandleCharts = function()
	{
		this._doHandlingCharts = false;
	};
	
	/**
	 * 给定图表是否在等待渲染。
	 * 等待渲染的判断条件：
	 * chart.statusPreRender()为true。
	 * 
	 * @param chart 图表对象
	 */
	dashboardBase.isWaitForRender = function(chart)
	{
		return chart.statusPreRender();
	};
	
	/**
	 * 给定图表是否在等待更新数据。
	 * 等待更新数据的判断条件：
	 * chart.statusRendered()为true
	 * 或者
	 * chart.statusPreUpdate()为true
	 * 或者
	 * chart.statusUpdated()为true且图表设置了定时刷新间隔。
	 * 
	 * @param chart 图表对象
	 */
	dashboardBase.isWaitForUpdate = function(chart)
	{
		return (chart.statusRendered() || chart.statusPreUpdate()
				|| (chart.statusUpdated() && chart.updateIntervalNonNull() > -1));
	};
	
	/**
	 * 开始循环处理看板所有图表，根据其状态执行render或者update。
	 */
	dashboardBase._doHandleCharts = function()
	{
		if(this._doHandlingCharts != true)
			return;
		
		var charts = (this.charts || []);
		
		for(var i=0; i<charts.length; i++)
		{
			var chart = charts[i];
			
			if(this.isWaitForRender(chart))
				this._renderChart(chart);
		}
		
		var preUpdateGroups = {};
		var time = new Date().getTime();
		
		for(var i=0; i<charts.length; i++)
		{
			var chart = charts[i];
			
			//图表正处于更新数据ajax中
			if(this._inUpdateDataAjax(chart))
				continue;
			
			//图表更新ajax请求出错后，应等待一段时间后再尝试，避免频繁发送ajax请求
			if(this._inUpdateDataAjaxErrorTime(chart, time))
				continue;
			
			if(this.isWaitForUpdate(chart))
			{
				if(!chart.isDataSetParamValueReady())
				{
					//标记为需要参数输入，避免参数准备好时会立即自动更新，实际应该由API控制是否更新
					chart.status(chartStatusConst.PARAM_VALUE_REQUIRED);
				}
				else
				{
					var updateInterval = chart.updateIntervalNonNull();
					var prevUpdateTime = this._chartUpdateTime(chart);
					
					if(prevUpdateTime == null || (prevUpdateTime + updateInterval) <= time)
					{
						var group = chart.updateGroup();
						var preUpdates = preUpdateGroups[group];
						
						if(preUpdates == null)
						{
							preUpdates = [];
							preUpdateGroups[group] = preUpdates;
						}
						
						preUpdates.push(chart);
					}
				}
			}
		}
		
		var webContext = chartFactory.renderContextAttrWebContext(this.renderContext);
		var url = chartFactory.toWebContextPathURL(webContext, webContext.attributes.updateDashboardURL);
		
		for(var group in preUpdateGroups)
		{
			this._doHandleChartsAjax(url, preUpdateGroups[group]);
		}
		
		var dashboard = this;
		setTimeout(function()
		{
			dashboard._doHandleCharts();
		},
		dashboardFactory.HANDLE_CHART_INTERVAL_MS);
	};
	
	dashboardBase._doHandleChartsAjax = function(url, preUpdateCharts)
	{
		if(!preUpdateCharts || preUpdateCharts.length == 0)
			return;
		
		var data = this._buildUpdateDashboardAjaxData(preUpdateCharts);
		var dashboard = this;
		
		dashboard._inUpdateDataAjax(preUpdateCharts, true);
		
		$.ajax({
			contentType : "application/json",
			type : "POST",
			url : url,
			data : JSON.stringify(data),
			success : function(resultsMap)
			{
				//@deprecated 用于兼容1.10.1版本的DataSetResult.datas结构，未来版本会移除
				if(resultsMap)
				{
					for(var chartId in resultsMap)
					{
						var results = (resultsMap[chartId] || []);
						for(var i=0; i<results.length; i++)
						{
							if(results[i] && results[i].data != null)
							{
								var resultDatas = results[i].data;
								if(resultDatas != null && !$.isArray(resultDatas))
									resultDatas = [ resultDatas ];
								
								results[i].datas = resultDatas;
							}
						}
					}
				}
				//@deprecated 用于兼容1.10.1版本的DataSetResult.datas结构，未来版本会移除
				
				try
				{
					dashboard._updateCharts(resultsMap);
				}
				catch(e)
				{
					chartFactory.logException(e);
				}
				
				dashboard._inUpdateDataAjax(preUpdateCharts, false);
			},
			error : function()
			{
				var updateTime = new Date().getTime();
				
				try
				{
					for(var i=0; i<preUpdateCharts.length; i++)
					{
						dashboard._chartUpdateTime(preUpdateCharts[i], updateTime);
						dashboard._updateDataAjaxErrorTime(preUpdateCharts[i], updateTime);
					}
				}
				catch(e)
				{
					chartFactory.logException(e);
				}
				
				dashboard._inUpdateDataAjax(preUpdateCharts, false);
			}
		});
	};
	
	/**
	 * 渲染指定图表。
	 * 
	 * @param chart 图表对象
	 */
	dashboardBase._renderChart = function(chart)
	{
		try
		{
			this._doRenderChart(chart);
		}
		catch(e)
		{
			//设置为渲染出错状态，避免渲染失败后会_doHandleCharts中会无限尝试渲染
			chart.status(chartStatusConst.RENDER_ERROR);
			
			chartFactory.logException(e);
		}
	};
	
	/**
	 * 执行渲染指定图表。
	 * 
	 * @param chart 图表对象
	 */
	dashboardBase._doRenderChart = function(chart)
	{
		return chart.render();
	};
	
	/**
	 * 更新看板的图表数据。
	 * 
	 * @param resultsMap 图表ID - 图表数据集结果数组
	 */
	dashboardBase._updateCharts = function(resultsMap)
	{
		var updateTime = new Date().getTime();
		
		for(var chartId in resultsMap)
		{
			var chart = this.getChart(chartId);
			
			if(!chart)
				continue;
			
			this._chartUpdateTime(chart, updateTime);
			
			var results = resultsMap[chartId];
			
			this._updateChart(chart, results);
		}
	};
	
	/**
	 * 更新指定图表。
	 * 
	 * @param chart 图表对象
	 * @param results 图表数据集结果数组
	 */
	dashboardBase._updateChart = function(chart, results)
	{
		try
		{
			this._doUpdateChart(chart, results);
		}
		catch(e)
		{
			//设置为更新出错状态，避免更新失败后会_doHandleCharts中会无限尝试更新
			chart.status(chartStatusConst.UPDATE_ERROR);
			
			chartFactory.logException(e);
		}
	};
	
	/**
	 * 更新指定图表。
	 * 
	 * @param chart 图表对象
	 * @param results 图表数据集结果数组
	 */
	dashboardBase._doUpdateChart = function(chart, results)
	{
		return chart.update(results);
	};
	
	dashboardBase._chartUpdateTime = function(chart, updateTime)
	{
		if(updateTime === undefined)
			return chart.extValue("_updateTime");
		else
			chart.extValue("_updateTime", updateTime);
	};
	
	dashboardBase._inUpdateDataAjax = function(chart, inAjax)
	{
		if(inAjax === undefined)
		{
			return (chart.extValue("_inUpdateDataAjax") == true);
		}
		else
		{
			if($.isArray(chart))
			{
				for(var i=0; i<chart.length; i++)
					chart[i].extValue("_inUpdateDataAjax", inAjax);
			}
			else
			{
				chart.extValue("_inUpdateDataAjax", inAjax);
			}
		}
	};
	
	dashboardBase._updateDataAjaxErrorTime = function(chart, errorTime)
	{
		if(errorTime === undefined)
			return chart.extValue("_updateDataAjaxErrorTime");
		else
			chart.extValue("_updateDataAjaxErrorTime", errorTime);
	};
	
	dashboardBase._inUpdateDataAjaxErrorTime = function(chart, currentTime)
	{
		var errorTime = dashboardBase._updateDataAjaxErrorTime(chart);
		
		if(errorTime == null)
			return false;
		
		return ((errorTime + dashboardFactory.UPDATE_AJAX_RETRY_SECONDS*1000) >= currentTime);
	};
	
	/**
	 * 构建更新看板的ajax请求数据。
	 */
	dashboardBase._buildUpdateDashboardAjaxData = function(charts)
	{
		var updateDashboardConfig = dashboardFactory.updateDashboardConfig;
		
		var data = {};
		data[updateDashboardConfig.dashboardIdParamName] = this.id;
		
		if(charts && charts.length)
		{
			var chartIds = [];
			var chartsParamValues = {};
			
			for(var i=0; i<charts.length; i++)
			{
				chartIds[i] = charts[i].id;
				var chartDataSets = (charts[i].chartDataSets || []);
				var myParamValuess = [];
				for(var j=0; j<chartDataSets.length; j++)
					myParamValuess.push(chartDataSets[j].paramValues || {});
				
				chartsParamValues[charts[i].id] = myParamValuess;
			}
			
			data[updateDashboardConfig.chartIdsParamName] = chartIds;
			data[updateDashboardConfig.chartsParamValuesParamName] = chartsParamValues;
		}
		
		return data;
	};
	
	/**
	 * 异步加载图表。
	 * 如果指定的HTML元素已经是图表组件，则不加载图表而直接返回false。
	 * 
	 * @param element 用于渲染图表的HTML元素、Jquery对象
	 * @param chartWidgetId 要加载的图表部件ID
	 * @param ajaxOptions 选填参数，参数格式可以是ajax配置项：{...}、也可以是图表加载成功回调函数：function(chart){ ... }。
	 * 					  如果ajax配置项的success函数、图表加载成功回调函数返回false，则后续不会自动调用dashboardBase.addChart函数。
	 */
	dashboardBase.loadChart = function(element, chartWidgetId, ajaxOptions)
	{
		if(chartFactory.isChartElement(element))
			return false;
		
		var chartElementId = $(element).attr("id");
		if(!chartElementId)
		{
			chartElementId = "chart" + new Date().getTime();
			$(element).attr("id", chartElementId);
		}
		
		var webContext = chartFactory.renderContextAttrWebContext(this.renderContext);
		var url = chartFactory.toWebContextPathURL(webContext, webContext.attributes.loadChartURL);
		var loadChartConfig = dashboardFactory.loadChartConfig;
		
		var _this = this;
		
		if(!ajaxOptions)
			ajaxOptions = {};
		else if(typeof(ajaxOptions) == "function")
		{
			var successHandler = ajaxOptions;
			ajaxOptions =
			{
				success: successHandler
			};
		}
		
		var data = {};
		data[loadChartConfig.dashboardIdParamName] = _this.id;
		data[loadChartConfig.chartWidgetIdParamName] = chartWidgetId;
		data[loadChartConfig.chartElementIdParamName] = chartElementId;
		
		var myAjaxOptions = $.extend(
		{
			url: url,
			data: data,
			error: function(jqXHR, textStatus, errorThrown)
			{
				var msg = (jqXHR.responseJSON ? jqXHR.responseJSON.message : undefined);
				if(!msg)
					msg = textStatus;
				
				$(element).html(msg);
			}
		},
		ajaxOptions);
		
		if(!myAjaxOptions.success)
		{
			myAjaxOptions.success = function(chart, textStatus, jqXHR)
			{
				_this._initLoadedChart(chart);
				_this.addChart(chart);
			};
		}
		else
		{
			var successHandler = myAjaxOptions.success;
			
			myAjaxOptions.success = function(chart, textStatus, jqXHR)
			{
				_this._initLoadedChart(chart);
				var re = successHandler.call(this, chart, textStatus, jqXHR);
				
				if(re != false)
					_this.addChart(chart);
			};
		}
		
		$.ajax(myAjaxOptions);
	};
	
	/**
	 * 初始化异步加载的图表。
	 * 
	 * @param chart 图表JSON对象
	 */
	dashboardBase._initLoadedChart = function(chart)
	{
		chart.plugin = chartFactory.chartPluginManager.get(chart.plugin.id);
		this._initChart(chart);
	};
	
	/**
	 * 批量设置图表数据集参数值。
	 * 
	 * 批量设置对象格式为：
	 * {
	 *   //必选，要设置的目标图表元素ID、图表ID、看板图表数组索引，或者它们的数组
	 *   target: "..."、["...", ...],
	 *   
	 *   //可选，要设置的参数值映射表，没有则不设置任何参数值
	 *   data:
	 *   {
	 *     源参数名 : 图表数据集参数索引对象、[ 图表数据集参数索引对象, ... ],
	 *     ...
	 *   }
	 * }
	 * 
	 * 上述【源参数名】可以是简单参数名，例如："name"、"value"，也可以是源参数对象的属性路径，例如："order.name"、"[0].name"、"['order'].product.name"
	 * 
	 * 图表数据集参数索引对象用于确定源参数值要设置到的目标图表数据集参数，格式为：
	 * {
	 *   //可选，目标图表在批量设置对象的target数组中的索引数值，默认为：0
	 *   chart: ...,
	 *   
	 *   //可选，目标图表数据集数组的索引数值，默认为：0
	 *   dataSet: ...,
	 *   
	 *   //可选，目标图表数据集的参数数组索引/参数名，默认为：0
	 *   param: ...,
	 *   
	 *   //可选，自定义源参数值处理函数，返回要设置的目标参数值
	 *   //sourceValue 源参数值
	 *   //sourceValueContext 源参数值上下文对象
	 *   value: function(sourceValue, sourceValueContext){ return ...; }
	 * }
	 * 或者，可简写为上述图表数据集参数索引对象的"param"属性值
	 * 
	 * @param sourceData 源参数值对象，格式为：{ 源参数名 : 源参数值, ...} 或者 { getValue: function(name){ return ...; } }（需支持属性路径）
	 * @param batchSet 批量设置对象
	 * @param sourceValueContext 可选，传递给图表数据集参数索引对象的value函数sourceValueContext参数的对象，如果为数组，则传递多个参数，默认为sourceData
	 * @return 批量设置的图表对象数组
	 */
	dashboardBase.batchSetDataSetParamValues = function(sourceData, batchSet, sourceValueContext)
	{
		sourceValueContext = (sourceValueContext === undefined ? sourceData : sourceValueContext);
		
		var targetCharts = [];
		
		var targets = ($.isArray(batchSet.target) ? batchSet.target : [ batchSet.target ]);
		for(var i=0; i<targets.length; i++)
			targetCharts[i] = this.getChart(targets[i]);
		
		var map = (batchSet.data || {});
		var hasGetValueFunc = (typeof(sourceData.getValue) == "function");
		
		var sourceValueContextArgs = [ "place-holder-for-source-value" ];
		sourceValueContextArgs = sourceValueContextArgs.concat($.isArray(sourceValueContext) ? sourceValueContext : [ sourceValueContext ]);
		
		for(var name in map)
		{
			var dataValue = (hasGetValueFunc ? sourceData.getValue(name)
					: dashboardFactory.getPropertyPathValue(sourceData, name));
			
			var indexes = map[name];
			if(!$.isArray(indexes))
				indexes = [ indexes ];
			
			for(var i=0; i<indexes.length; i++)
			{
				var indexObj = indexes[i];
				var indexObjType = typeof(indexObj);
				
				var chartIdx = 0;
				var dataSetIdx = 0;
				var param = 0;
				var paramValue = null;
				
				if(indexObjType == "number" || indexObjType == "string")
				{
					param = indexObj;
					paramValue = dataValue;
				}
				else
				{
					chartIdx = (indexObj.chart != null ? indexObj.chart : 0);
					dataSetIdx = (indexObj.dataSet != null ? indexObj.dataSet : 0);
					param = (indexObj.param != null ? indexObj.param : 0);
					
					if(indexObj.value)
					{
						sourceValueContextArgs[0] = dataValue;
						paramValue = indexObj.value.apply(indexObj, sourceValueContextArgs);
					}
					else
						paramValue = dataValue;
				}
				
				targetCharts[chartIdx].dataSetParamValue(dataSetIdx, param, paramValue);
			}
		}
		
		return targetCharts;
	};
	
	//----------------------------------------
	// dashboardBase end
	//----------------------------------------
	
	/**
	 * 内置地图JSON地址配置。
	 */
	dashboardFactory.builtinChartMapBaseURL = "/static/lib/echarts-map";
	dashboardFactory.builtinChartMaps =
	[
		{names: ["china", "中国"], url: "/china.json"},
		{names: ["china-contour", "中国轮廓"], url: "/china-contour.json"},
		{names: ["china-cities", "中国城市"], url: "/china-cities.json"},
		{names: ["world", "世界"], url: "/world.json"},
		{names: ["anhui", "安徽"], url: "/province/anhui.json"},
		{names: ["aomen", "澳门"], url: "/province/aomen.json"},
		{names: ["beijing", "北京"], url: "/province/beijing.json"},
		{names: ["chongqing", "重庆"], url: "/province/chongqing.json"},
		{names: ["fujian", "福建"], url: "/province/fujian.json"},
		{names: ["gansu", "甘肃"], url: "/province/gansu.json"},
		{names: ["guangdong", "广东"], url: "/province/guangdong.json"},
		{names: ["guangxi", "广西"], url: "/province/guangxi.json"},
		{names: ["guizhou", "贵州"], url: "/province/guizhou.json"},
		{names: ["hainan", "海南"], url: "/province/hainan.json"},
		{names: ["hebei", "河北"], url: "/province/hebei.json"},
		{names: ["heilongjiang", "黑龙江"], url: "/province/heilongjiang.json"},
		{names: ["henan", "河南"], url: "/province/henan.json"},
		{names: ["hubei", "湖北"], url: "/province/hubei.json"},
		{names: ["hunan", "湖南"], url: "/province/hunan.json"},
		{names: ["jiangsu", "江苏"], url: "/province/jiangsu.json"},
		{names: ["jiangxi", "江西"], url: "/province/jiangxi.json"},
		{names: ["jilin", "吉林"], url: "/province/jilin.json"},
		{names: ["liaoning", "辽宁"], url: "/province/liaoning.json"},
		{names: ["neimenggu", "内蒙古"], url: "/province/neimenggu.json"},
		{names: ["ningxia", "宁夏"], url: "/province/ningxia.json"},
		{names: ["qinghai", "青海"], url: "/province/qinghai.json"},
		{names: ["shandong", "山东"], url: "/province/shandong.json"},
		{names: ["shanghai", "上海"], url: "/province/shanghai.json"},
		{names: ["shanxi", "山西"], url: "/province/shanxi.json"},
		{names: ["shanxi1", "陕西"], url: "/province/shanxi1.json"},
		{names: ["sichuan", "四川"], url: "/province/sichuan.json"},
		{names: ["taiwan", "台湾"], url: "/province/taiwan.json"},
		{names: ["tianjin", "天津"], url: "/province/tianjin.json"},
		{names: ["xianggang", "香港"], url: "/province/xianggang.json"},
		{names: ["xinjiang", "新疆"], url: "/province/xinjiang.json"},
		{names: ["xizang", "西藏"], url: "/province/xizang.json"},
		{names: ["yunnan", "云南"], url: "/province/yunnan.json"},
		{names: ["zhejiang", "浙江"], url: "/province/zhejiang.json"}
	];
})
(this);