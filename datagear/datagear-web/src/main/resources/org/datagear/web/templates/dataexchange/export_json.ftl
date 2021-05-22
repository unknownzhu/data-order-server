<#--
 *
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 *
-->
<#include "../include/import_global.ftl">
<#include "../include/html_doctype.ftl">
<#--
Schema schema 数据库，不允许为null
-->
<html>
<head>
<#include "../include/html_head.ftl">
<title>
	<#include "../include/html_title_app_name.ftl">
	<@spring.message code='dataExport.dataExport' />
	<@spring.message code='bracketLeft' />
	${schema.title}
	<@spring.message code='bracketRight' />
</title>
</head>
<body class="fill-parent">
<#if !isAjaxRequest>
<div class="fill-parent">
</#if>
<div id="${pageId}" class="page-dataexchange page-dataexport-text page-dataexport-json">
	<div class="head">
		<@spring.message code='dataExport.exportJsonData' />
	</div>
	<div class="content">
		<form id="${pageId}-form" action="${contextPath}/dataexchange/${schema.id}/export/json/doExport" method="POST">
			<input type="hidden" name="dataExchangeId" value="${dataExchangeId}" />
			<div class="form-content form-content-wizard">
				<h3><@spring.message code='dataExport.setDataFormat' /></h3>
				<div>
					<#include "include/dataExchange_form_dataFormat_html.ftl">
					<div class="form-item">
						<div class="form-item-label"><@spring.message code='dataExport.nullForIllegalColumnValue' /></div>
						<div class="form-item-value">
							<div id="${pageId}-nullForIllegalColumnValue">
								<label for="${pageId}-nullForIllegalColumnValue-0"><@spring.message code='yes' /></label>
								<input id="${pageId}-nullForIllegalColumnValue-0" type="radio" name="exportOption.nullForIllegalColumnValue" value="true" />
								<label for="${pageId}-nullForIllegalColumnValue-1"><@spring.message code='no' /></label>
								<input id="${pageId}-nullForIllegalColumnValue-1" type="radio" name="exportOption.nullForIllegalColumnValue" value="false" />
							</div>
						</div>
					</div>
					<div class="form-item">
						<div class="form-item-label"><@spring.message code='dataExport.exportFileFormat' /></div>
						<div class="form-item-value">
							<div id="${pageId}-exportFileFormat">
								<label for="${pageId}-exportFileFormat-0" title="<@spring.message code='dataExchange.JsonDataFormat.TABLE_OBJECT.desc' />"><@spring.message code='dataExchange.JsonDataFormat.TABLE_OBJECT' /></label>
								<input id="${pageId}-exportFileFormat-0" type="radio" name="exportOption.jsonDataFormat" value="TABLE_OBJECT" />
								<label for="${pageId}-exportFileFormat-1" title="<@spring.message code='dataExchange.JsonDataFormat.ROW_ARRAY.desc' />"><@spring.message code='dataExchange.JsonDataFormat.ROW_ARRAY' /></label>
								<input id="${pageId}-exportFileFormat-1" type="radio" name="exportOption.jsonDataFormat" value="ROW_ARRAY" />
							</div>
						</div>
					</div>
				</div>
				<h3><@spring.message code='dataExport.selectAndExportData' /></h3>
				<div>
					<div class="form-item form-item-table-head form-item-add page-status-aware-show edit-status-show">
						<div class="form-item-value">
							<div id="${pageId}-add-group">
								<button type="button" class="table-add-item-button edit-state-aware ui-corner-left"><@spring.message code='add' /></button>
								<select id="${pageId}-add-group-select">
									<option value="addAll"><@spring.message code='dataExport.addAllTable' /></option>
								</select>
							</div>
						</div>
					</div>
					<div class="form-item form-item-table-head form-item-progress page-status-aware-show exchange-status-show finish-status-show">
						<div class="form-item-value">
							<label><@spring.message code='dataExport.exportProgress' /></label>
							<div id="${pageId}-progress"></div>
							<div id="${pageId}-progress-percent" class="progress-percent"></div>
						</div>
					</div>
					<div class="form-item form-item-table">
						<div class="table-operation-wrapper">
							<button type="button" class="table-delete-item-button page-status-aware-show edit-status-show"><@spring.message code='delete' /></button>
							<button type="button" class="table-cancel-export-button page-status-aware-show exchange-status-show"><@spring.message code='cancel' /></button>
							<button type="button" class="table-download-all-button page-status-aware-show finish-status-show" file-name="export_json.zip"><@spring.message code='downloadAll' /></button>
						</div>
						<div class="file-encoding-wrapper">
							<span class="file-encoding-label page-status-aware-enable edit-status-enable">
								<@spring.message code='dataExport.exportFileEncoding' />
							</span>
							<select name="fileEncoding">
								<#list availableCharsetNames as item>
								<option value="${item}" <#if item == defaultCharsetName>selected="selected"</#if>>${item}</option>
								</#list>
							</select>
						</div>
						<div class="table-wrapper minor-dataTable">
							<table id="${pageId}-table" width="100%" class="hover stripe"></table>
						</div>
					</div>
				</div>
			</div>
		</form>
		<div class="return-wrapper page-status-aware-show edit-status-show finish-status-show">
			<#include "include/dataExchange_return_form_export.ftl">
		</div>
		<div class="restart-wrapper page-status-aware-show finish-status-show">
			<button type="button" class="restart-button"><@spring.message code='restart' /></button>
		</div>
		<div id="${pageId}-exchange-exception-tooltip" title="import tooltip" style="width:0; height:0;"></div>
	</div>
	<div class="foot">
	</div>
</div>
<#if !isAjaxRequest>
</div>
</#if>

<#include "../include/page_js_obj.ftl">
<#include "../include/page_obj_grid.ftl">
<#include "../include/page_obj_format_time.ftl" >
<#include "include/dataExchange_js.ftl" >
<#include "include/dataExport_js.ftl" >
<script type="text/javascript">
(function(po)
{
	po.element(".binaryFormatSetButtonHex").attr("value", "0x"+"$"+"{Hex}");
	
	po.postBuildSubDataExchange = function(subDataExchange)
	{
		subDataExchange.tableName = po.resolveTableName(subDataExchange.query);
	};

	po.toExportFileNameSuper = po.toExportFileName;
	po.toExportFileName = function(query)
	{
		return po.toExportFileNameSuper(query, ".json");
	};
	
	po.onStepChangedSuper = po.onStepChanged;
	po.onStepChanged = function(event, currentIndex, priorIndex)
	{
		if(currentIndex == 1)
		{
			var visible = po.element("input[name='exportOption.jsonDataFormat']:checked").val() == "TABLE_OBJECT";
			po.getSubDataExchangeDataTable().column(2).visible(visible, false);
		}
		
		po.onStepChangedSuper(event, currentIndex, priorIndex);
	};
	
	po.dataExportTableColumns.splice(1, 0,
	{
		title : "<@spring.message code='dataExport.jsonExportTableName' />",
		data : "tableName",
		render : function(data, type, row, meta)
		{
			if(!data)
				data = "";
			
			return "<input type='text' name='tableNames[]' value='"+$.escapeHtml(data)+"' class='table-name-input input-in-table ui-widget ui-widget-content' style='width:90%' />";
		},
		defaultContent: "",
		width : "20%"
	});
	
	po.initDataExportSteps();
	po.initDataExchangeUIs();
	po.initDataExportUIs();
	po.element("#${pageId}-exportFileFormat").buttonset();
	po.element("#${pageId}-exportFileFormat-0").click();
	po.initDataExportDataTable();
	po.initDataExchangeActions();
	po.initDataExportActions();
	po.initDataExportDroppable();
	po.updateDataExchangePageStatus("edit");
})
(${pageId});
</script>
</body>
</html>
