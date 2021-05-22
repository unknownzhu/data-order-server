/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.analysis.support;

import java.util.List;
import java.util.Map;

import org.datagear.analysis.AbstractIdentifiable;
import org.datagear.analysis.Category;
import org.datagear.analysis.ChartParam;
import org.datagear.analysis.ChartPlugin;
import org.datagear.analysis.DataSign;
import org.datagear.analysis.Icon;
import org.datagear.util.i18n.Label;

/**
 * 抽象{@linkplain ChartPlugin}。
 * 
 * @author datagear@163.com
 *
 */
public abstract class AbstractChartPlugin extends AbstractIdentifiable implements ChartPlugin
{
	private Label nameLabel;

	private Label descLabel;

	private Label manualLabel;

	private Map<String, Icon> icons;

	private List<ChartParam> chartParams;

	private List<DataSign> dataSigns;

	private String version;

	private int order = 0;

	private Category category;

	public AbstractChartPlugin()
	{
	}

	public AbstractChartPlugin(String id, Label nameLabel)
	{
		super(id);
		this.nameLabel = nameLabel;
	}

	@Override
	public Label getNameLabel()
	{
		return nameLabel;
	}

	@Override
	public void setNameLabel(Label nameLabel)
	{
		this.nameLabel = nameLabel;
	}

	@Override
	public Label getDescLabel()
	{
		return descLabel;
	}

	@Override
	public void setDescLabel(Label descLabel)
	{
		this.descLabel = descLabel;
	}

	@Override
	public Label getManualLabel()
	{
		return manualLabel;
	}

	public void setManualLabel(Label manualLabel)
	{
		this.manualLabel = manualLabel;
	}

	@Override
	public Map<String, Icon> getIcons()
	{
		return icons;
	}

	public void setIcons(Map<String, Icon> icons)
	{
		this.icons = icons;
	}

	@Override
	public Icon getIcon(String themeName)
	{
		Icon icon = (this.icons == null ? null : this.icons.get(themeName));

		if (icon == null && !DEFAULT_ICON_THEME_NAME.equals(themeName))
			icon = getIcon(DEFAULT_ICON_THEME_NAME);

		return icon;
	}

	@Override
	public List<ChartParam> getChartParams()
	{
		return chartParams;
	}

	public void setChartParams(List<ChartParam> chartParams)
	{
		this.chartParams = chartParams;
	}

	@Override
	public ChartParam getChartParam(String name)
	{
		if (this.chartParams == null)
			return null;

		for (ChartParam chartParam : this.chartParams)
		{
			if (chartParam.getName().equals(name))
				return chartParam;
		}

		return null;
	}

	@Override
	public List<DataSign> getDataSigns()
	{
		return dataSigns;
	}

	public void setDataSigns(List<DataSign> dataSigns)
	{
		this.dataSigns = dataSigns;
	}

	@Override
	public DataSign getDataSign(String name)
	{
		if (this.dataSigns == null)
			return null;

		for (DataSign dataSign : this.dataSigns)
		{
			if (dataSign.getName().equals(name))
				return dataSign;
		}

		return null;
	}

	@Override
	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	@Override
	public int getOrder()
	{
		return order;
	}

	public void setOrder(int order)
	{
		this.order = order;
	}

	@Override
	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}
}
