/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.datagear.analysis.Chart;
import org.datagear.analysis.ChartDataSet;
import org.datagear.analysis.Dashboard;
import org.datagear.analysis.DashboardTheme;
import org.datagear.analysis.DashboardThemeSource;
import org.datagear.analysis.DataSetParam;
import org.datagear.analysis.DataSetResult;
import org.datagear.analysis.RenderContext;
import org.datagear.analysis.support.DataSetParamValueConverter;
import org.datagear.analysis.support.DefaultRenderContext;
import org.datagear.analysis.support.SimpleDashboardThemeSource;
import org.datagear.analysis.support.html.HtmlTplDashboard;
import org.datagear.analysis.support.html.HtmlTplDashboardRenderAttr;
import org.datagear.analysis.support.html.HtmlTplDashboardRenderAttr.WebContext;
import org.datagear.analysis.support.html.HtmlTplDashboardWidgetRenderer;
import org.datagear.util.StringUtil;
import org.datagear.web.util.WebUtils;

/**
 * 抽象数据分析控制器。
 * 
 * @author datagear@163.com
 *
 */
public abstract class AbstractDataAnalysisController extends AbstractController
{
	/** 更新看板数据请求的看板ID参数名 */
	public static final String UPDATE_DASHBOARD_PARAM_DASHBOARD_ID = "dashboardId";

	/** 更新看板数据请求的图表集参数名 */
	public static final String UPDATE_DASHBOARD_PARAM_CHART_IDS = "chartIds";

	/** 更新看板数据请求的图表集参数值的参数名 */
	public static final String UPDATE_DASHBOARD_PARAM_CHARTS_PARAM_VALUES = "chartsParamValues";

	public static final String DASHBOARD_THEME_NAME_PARAM = "theme";

	public static final String WEB_CONTEXT_CONTEXT_PATH_PARAM = "";

	/** 看板更新数据URL名 */
	public static final String DASHBOARD_UPDATE_URL_NAME = "updateDashboardURL";

	/** 看板加载图表URL名 */
	public static final String DASHBOARD_LOAD_CHART_URL_NAME = "loadChartURL";

	/** 看板心跳URL名 */
	public static final String DASHBOARD_HEARTBEAT_URL_NAME = "heartbeatURL";

	private DataSetParamValueConverter dataSetParamValueConverter = new DataSetParamValueConverter();

	private DashboardThemeSource dashboardThemeSource = new SimpleDashboardThemeSource();

	public AbstractDataAnalysisController()
	{
		super();
	}

	public DataSetParamValueConverter getDataSetParamValueConverter()
	{
		return dataSetParamValueConverter;
	}

	public void setDataSetParamValueConverter(DataSetParamValueConverter dataSetParamValueConverter)
	{
		this.dataSetParamValueConverter = dataSetParamValueConverter;
	}

	public DashboardThemeSource getDashboardThemeSource()
	{
		return dashboardThemeSource;
	}

	public void setDashboardThemeSource(DashboardThemeSource dashboardThemeSource)
	{
		this.dashboardThemeSource = dashboardThemeSource;
	}

	protected RenderContext createHtmlRenderContext(HttpServletRequest request, HttpServletResponse response,
			HtmlTplDashboardRenderAttr renderAttr, WebContext webContext,
			HtmlTplDashboardWidgetRenderer htmlTplDashboardWidgetRenderer) throws IOException
	{
		RenderContext renderContext = new DefaultRenderContext(resolveParamValues(request));

		Writer out = response.getWriter();
		DashboardTheme dashboardTheme = resolveDashboardTheme(request);
		renderAttr.inflate(renderContext, out, webContext, dashboardTheme);

		renderAttr.setIgnoreRenderAttrs(renderContext,
				Arrays.asList(renderAttr.getHtmlWriterName(), renderAttr.getHtmlTitleHandlerName(),
						renderAttr.getIgnoreRenderAttrsName(), HtmlTplDashboardRenderAttr.ATTR_NAME));

		return renderContext;
	}

	protected HtmlTplDashboardRenderAttr createHtmlTplDashboardRenderAttr()
	{
		HtmlTplDashboardRenderAttr renderAttr = new HtmlTplDashboardRenderAttr();
		return renderAttr;
	}

	/**
	 * 创建初始{@linkplain WebContext}。
	 * 
	 * @param request
	 * @return
	 */
	protected WebContext createInitWebContext(HttpServletRequest request)
	{
		WebContext webContext = new WebContext(WebUtils.getContextPath(request));

		return webContext;
	}

	protected Map<String, ?> resolveParamValues(HttpServletRequest request)
	{
		Map<String, Object> paramValues = new HashMap<>();

		Map<String, String[]> origin = request.getParameterMap();

		for (Map.Entry<String, String[]> entry : origin.entrySet())
		{
			String name = entry.getKey();
			String[] values = entry.getValue();

			if (values == null || values.length == 0)
				continue;

			if (values.length == 1)
				paramValues.put(name, values[0]);
			else
				paramValues.put(name, values);
		}

		return paramValues;
	}

	protected DashboardTheme resolveDashboardTheme(HttpServletRequest request)
	{
		String theme = request.getParameter(DASHBOARD_THEME_NAME_PARAM);

		if (StringUtil.isEmpty(theme))
			theme = WebUtils.getTheme(request);

		DashboardTheme dashboardTheme = this.dashboardThemeSource.getDashboardTheme(theme);
		if (dashboardTheme == null)
			dashboardTheme = this.dashboardThemeSource.getDashboardTheme();

		return dashboardTheme;
	}

	/**
	 * 获取看板数据。
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param webContext
	 * @param dashboardParams
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, DataSetResult[]> getDashboardData(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model model, WebContext webContext, Map<String, ?> dashboardParams) throws Exception
	{
		String dashboardId = (String) dashboardParams.get(UPDATE_DASHBOARD_PARAM_DASHBOARD_ID);
		Collection<String> chartIds = (Collection<String>) dashboardParams.get(UPDATE_DASHBOARD_PARAM_CHART_IDS);
		Map<String, ? extends List<? extends Map<String, ?>>> chartsParamValues = (Map<String, ? extends List<? extends Map<String, ?>>>) dashboardParams
				.get(UPDATE_DASHBOARD_PARAM_CHARTS_PARAM_VALUES);

		if (StringUtil.isEmpty(dashboardId))
			throw new IllegalInputException();

		SessionHtmlTplDashboardManager dashboardManager = getSessionHtmlTplDashboardManagerNotNull(request);

		HtmlTplDashboard dashboard = dashboardManager.get(dashboardId);

		if (dashboard == null)
			throw new RecordNotFoundException();

		if (chartIds == null || chartIds.isEmpty())
			return dashboard.getDataSetResults();
		else
		{
			if (chartsParamValues == null)
				chartsParamValues = Collections.EMPTY_MAP;

			Set<String> chartIdSet = new HashSet<>(chartIds.size());
			chartIdSet.addAll(chartIds);

			return dashboard.getDataSetResults(chartIdSet, convertChartsParamValues(dashboard, chartsParamValues));
		}
	}

	protected SessionHtmlTplDashboardManager getSessionHtmlTplDashboardManagerNotNull(HttpServletRequest request)
	{
		HttpSession session = request.getSession();

		SessionHtmlTplDashboardManager dashboardManager = (SessionHtmlTplDashboardManager) session
				.getAttribute(SessionHtmlTplDashboardManager.class.getName());

		synchronized (session)
		{
			if (dashboardManager == null)
			{
				dashboardManager = new SessionHtmlTplDashboardManager();
				session.setAttribute(SessionHtmlTplDashboardManager.class.getName(), dashboardManager);
			}
		}

		return dashboardManager;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, List<? extends Map<String, ?>>> convertChartsParamValues(Dashboard dashboard,
			Map<String, ? extends List<? extends Map<String, ?>>> chartsParamValues)
	{
		if (chartsParamValues == null)
			return Collections.EMPTY_MAP;

		Map<String, List<? extends Map<String, ?>>> re = new HashMap<>();

		for (Map.Entry<String, ? extends List<? extends Map<String, ?>>> entry : chartsParamValues.entrySet())
		{
			Chart chart = dashboard.getChart(entry.getKey());
			ChartDataSet[] chartDataSets = chart.getChartDataSets();

			if (chartDataSets == null || chartDataSets.length == 0)
				re.put(entry.getKey(), Collections.EMPTY_LIST);
			else
			{
				List<? extends Map<String, ?>> paramValuess = entry.getValue();
				List<Map<String, ?>> converteds = new ArrayList<>();

				for (int i = 0; i < chartDataSets.length; i++)
				{
					List<DataSetParam> dataSetParams = chartDataSets[i].getDataSet().getParams();
					converteds.add(getDataSetParamValueConverter().convert(paramValuess.get(i), dataSetParams));
				}

				re.put(entry.getKey(), converteds);
			}
		}

		return re;
	}

	protected void addHeartBeatValue(HttpServletRequest request, WebContext webContext)
	{
		String heartbeatURL = "/analysis/dashboard/heartbeat";
		heartbeatURL = addJsessionidParam(heartbeatURL, request.getSession().getId());

		webContext.addAttribute(DASHBOARD_HEARTBEAT_URL_NAME, heartbeatURL);
	}

	/**
	 * 为指定URL添加会话ID参数。
	 * <p>
	 * 图表、看板展示页可能会以&lt;iframe&gt;的方式嵌入外部网页中，当在跨域场景时，某些浏览器会禁用&lt;iframe&gt;内的cookie，导致会话无法保持，
	 * 从而引起图表、看板内的ajax请求失效，此方法可以解决上述问题。
	 * </p>
	 * 
	 * @param url
	 * @param sessionId
	 * @return
	 */
	protected String addJsessionidParam(String url, String sessionId)
	{
		return WebUtils.addJsessionidParam(url, sessionId);
	}

	protected static class SessionHtmlTplDashboardManager implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private transient Map<String, HtmlTplDashboard> htmlTplDashboards;

		public SessionHtmlTplDashboardManager()
		{
			super();
		}

		public synchronized HtmlTplDashboard get(String htmlTplDashboardId)
		{
			if (this.htmlTplDashboards == null)
				return null;

			return this.htmlTplDashboards.get(htmlTplDashboardId);
		}

		public synchronized void put(HtmlTplDashboard dashboard)
		{
			if (this.htmlTplDashboards == null)
				this.htmlTplDashboards = new HashMap<>();

			this.htmlTplDashboards.put(dashboard.getId(), dashboard);
		}
	}
}
