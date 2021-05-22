<#--
 *
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 *
-->
<#include "../../include/import_global.ftl">
<#include "../../include/html_doctype.ftl">
<#--
titleMessageKey 标题标签I18N关键字，不允许null
formAction 表单提交action，允许为null
readonly 是否只读操作，允许为null
-->
<#assign formAction=(formAction!'#')>
<#assign readonly=(readonly!false)>
<#assign isAdd=(formAction == 'saveAddForExcel')>
<html>
<head>
<#include "../../include/html_head.ftl">
<title><#include "../../include/html_title_app_name.ftl">
	<@spring.message code='${titleMessageKey}' /> - <@spring.message code='dataSet.dataSetType.Excel' />
</title>
</head>
<body>
<#include "../../include/page_js_obj.ftl" >
<div id="${pageId}" class="page-form page-form-dataSet page-form-dataSet-excel">
	<form id="${pageId}-form" action="#" method="POST">
		<div class="form-head"></div>
		<div class="form-content">
			<#include "include/dataSet_form_html_name.ftl">
			<div class="workspace">
				<div class="form-item">
					<div class="form-item-label">
						<label><@spring.message code='dataSet.excelFile' /></label>
					</div>
					<div class="form-item-value form-item-value-file-input">
						<#include "include/dataSet_form_file_input.ftl">
					</div>
				</div>
				<div class="form-item">
					<div class="form-item-label">
						<label title="<@spring.message code='dataSet.excel.sheetIndex.desc' />">
							<@spring.message code='dataSet.excel.sheetIndex' />
						</label>
					</div>
					<div class="form-item-value error-newline">
						<input type="text" name="sheetIndex" value="${(dataSet.sheetIndex)!''}" class="ui-widget ui-widget-content" style="width:41%;" />
					</div>
				</div>
				<div class="form-item">
					<div class="form-item-label">
						<label title="<@spring.message code='dataSet.excel.nameRow.desc' />">
							<@spring.message code='dataSet.excel.nameRow' />
						</label>
					</div>
					<div class="form-item-value error-newline">
						<input type="hidden" name="nameRow" value="${(dataSet.nameRow)!''}" class="ui-widget ui-widget-content" />
						<span class="nameRow-radios">
							<label for="${pageId}-nameRow_0">
								<@spring.message code='dataSet.excel.nameRow.none' />
							</label>
				   			<input type="radio" id="${pageId}-nameRow_0" name="nameRowRadio" value="0" />
							<label for="${pageId}-nameRow_1">
								<@spring.message code='dataSet.excel.nameRow.assign' />
							</label>
				   			<input type="radio" id="${pageId}-nameRow_1" name="nameRowRadio" value="1"  />
						</span>
						&nbsp;
						<input type="text" name="nameRowText" value="${(dataSet.nameRow)!''}" class="ui-widget ui-widget-content" style="width:4.1em;" />
					</div>
				</div>
				<div class="form-item">
					<div class="form-item-label">
						<label title="<@spring.message code='dataSet.excel.dataRowExp.desc' />">
							<@spring.message code='dataSet.excel.dataRowExp' />
						</label>
					</div>
					<div class="form-item-value error-newline">
						<input type="text" name="dataRowExp" value="${(dataSet.dataRowExp)!''}" class="ui-widget ui-widget-content" />
					</div>
				</div>
				<div class="form-item">
					<div class="form-item-label">
						<label title="<@spring.message code='dataSet.excel.dataColumnExp.desc' />">
							<@spring.message code='dataSet.excel.dataColumnExp' />
						</label>
					</div>
					<div class="form-item-value error-newline">
						<input type="text" name="dataColumnExp" value="${(dataSet.dataColumnExp)!''}" class="ui-widget ui-widget-content" />
					</div>
				</div>
				<div class="form-item">
					<div class="form-item-label">
						<label><@spring.message code='dataSet.excel.forceXls' /></label>
					</div>
					<div class="form-item-value no-padding-bottom">
						<div id="${pageId}-forceXls">
							<label for="${pageId}-forceXls-true"><@spring.message code='yes' /></label>
							<input id="${pageId}-forceXls-true" type="radio" name="forceXls" value="true" />
							<label for="${pageId}-forceXls-false"><@spring.message code='no' /></label>
							<input id="${pageId}-forceXls-false" type="radio" name="forceXls" value="false" />
						</div>
					</div>
				</div>
				<#include "include/dataSet_form_html_wow.ftl" >
			</div>
		</div>
		<div class="form-foot" style="text-align:center;">
			<#if !readonly>
			<input type="submit" value="<@spring.message code='save' />" class="recommended" />
			<#else>
			<div class="form-foot-placeholder">&nbsp;</div>
			</#if>
		</div>
	</form>
	<#include "include/dataSet_form_html_preview_pvp.ftl" >
</div>
<#include "../../include/page_obj_form.ftl">
<#include "include/dataSet_form_js.ftl">
<#include "include/dataSet_form_js_nameRow.ftl">
<script type="text/javascript">
(function(po)
{
	po.dataSetProperties = <@writeJson var=dataSetProperties />;
	po.dataSetParams = <@writeJson var=dataSetParams />;
	
	$.initButtons(po.element());
	po.initAnalysisProject("${((dataSet.analysisProject.id)!'')?js_string?no_esc}", "${((dataSet.analysisProject.name)!'')?js_string?no_esc}");
	po.element(".nameRow-radios").controlgroup();
	po.element("#${pageId}-forceXls").buttonset();
	po.element("#${pageId}-forceXls-${((dataSet.forceXls)!true)?string('true', 'false')}").click();
	po.initWorkspaceHeight();
	po.initWorkspaceTabs(true);
	po.initDataSetPropertiesTable(po.dataSetProperties);
	po.initDataSetParamsTable(po.dataSetParams);
	po.initPreviewParamValuePanel();
	po.initNameRowOperation(${(dataSet.nameRow)!"1"});

	po.initDataSetFileInput(po.url("uploadFile"), "${((dataSet.fileSourceType)!'')?js_string?no_esc}", ${isAdd?string("true", "false")});
	
	po.updatePreviewOptionsData = function()
	{
		var dataSet = po.previewOptions.data.dataSet;

		dataSet.fileSourceType = po.fileSourceTypeValue();
		dataSet.fileName = po.element("input[name='fileName']").val();
		dataSet.dataSetResDirectory = {};
		dataSet.dataSetResDirectory.id = po.element("input[name='dataSetResDirectory.id']").val();
		dataSet.dataSetResDirectory.directory = po.element("input[name='dataSetResDirectory.directory']").val();
		dataSet.dataSetResFileName = po.element("input[name='dataSetResFileName']").val();
		dataSet.sheetIndex = po.element("input[name='sheetIndex']").val();
		dataSet.nameRow = po.nameRowValue();
		dataSet.dataRowExp = po.element("input[name='dataRowExp']").val();
		dataSet.dataColumnExp = po.element("input[name='dataColumnExp']").val();
		dataSet.forceXls = po.element("input[name='forceXls']:checked").val();
		
		po.previewOptions.data.originalFileName = po.element("#${pageId}-originalFileName").val();
	};
	
	<#if !isAdd>
	//编辑、查看操作应初始化为已完成预览的状态
	po.updatePreviewOptionsData();
	po.previewSuccess(true);
	</#if>
	
	po.isPreviewValueModified = function()
	{
		var fileSourceType = po.fileSourceTypeValue();
		var dataSetResDirectoryId = po.element("input[name='dataSetResDirectory.id']").val();
		var dataSetResFileName = po.element("input[name='dataSetResFileName']").val();
		var fileName = po.element("input[name='fileName']").val();
		var sheetIndex = po.element("input[name='sheetIndex']").val();
		var nameRow = po.nameRowValue();
		var dataRowExp = po.element("input[name='dataRowExp']").val();
		var dataColumnExp = po.element("input[name='dataColumnExp']").val();
		var forceXls = po.element("input[name='forceXls']:checked").val();
		
		var pd = po.previewOptions.data.dataSet;
		var dataSetResDirectory = (pd.dataSetResDirectory || {});
		
		return (pd.fileSourceType != fileSourceType)
			|| (po.isFileSourceTypeUpload(fileSourceType) && pd.fileName != fileName)
			|| (po.isFileSourceTypeServer(fileSourceType) && (dataSetResDirectory.id != dataSetResDirectoryId || pd.dataSetResFileName != dataSetResFileName))
			|| (pd.sheetIndex != sheetIndex)
			|| (pd.nameRow != nameRow) || (pd.dataRowExp != dataRowExp)
			|| (pd.dataColumnExp != dataColumnExp) || (pd.forceXls != forceXls);
	};
	
	po.previewOptions.url = po.url("previewExcel");
	po.previewOptions.beforePreview = function()
	{
		po.updatePreviewOptionsData();
		return po.isPreviewDataFileValid(this.data);
	};
	po.previewOptions.beforeRefresh = function()
	{
		return po.isPreviewDataFileValid(this.data);
	};
	
	po.initPreviewOperations();
	
	$.validator.addMethod("dataSetExcelPreviewRequired", function(value, element)
	{
		return !po.isPreviewValueModified() && po.previewSuccess();
	});
	
	$.validator.addMethod("dataSetExcelDataRowExpRegex", function(value, element)
	{
		if(!value)
			return true;
		
		var regex = /[\d\-\,\s]$/g;
		
		return regex.test(value);
	});

	$.validator.addMethod("dataSetExcelDataColumnExpRegex", function(value, element)
	{
		if(!value)
			return true;
		
		var regex = /([A-Z]|[\-\,\s])$/g;
		
		return regex.test(value);
	});
	
	po.form().validate(
	{
		ignore : "",
		rules :
		{
			"name" : "required",
			"displayName" :
			{
				"dataSetUploadFileNameRequired": true,
				"dataSetUploadFilePreviewRequired": true,
				"dataSetUploadFilePropertiesRequired": true
			},
			"dataSetResDirectory.directory":
			{
				"dataSetServerDirectoryRequired": true,
			},
			"dataSetResFileName":
			{
				"dataSetServerFileNameRequired": true,
				"dataSetServerFilePreviewRequired": true,
				"dataSetServerFilePropertiesRequired": true
			},
			"sheetIndex": {"required": true, "integer": true, "min": 1},
			"nameRowText": {"integer": true, "min": 1},
			"dataRowExp": {"dataSetExcelDataRowExpRegex": true},
			"dataColumnExp": {"dataSetExcelDataColumnExpRegex": true}
		},
		messages :
		{
			"name" : "<@spring.message code='validation.required' />",
			"displayName" :
			{
				"dataSetUploadFileNameRequired": "<@spring.message code='validation.required' />",
				"dataSetUploadFilePreviewRequired": "<@spring.message code='dataSet.validation.previewRequired' />",
				"dataSetUploadFilePropertiesRequired": "<@spring.message code='dataSet.validation.propertiesRequired' />"
			},
			"dataSetResDirectory.directory":
			{
				"dataSetServerDirectoryRequired": "<@spring.message code='validation.required' />",
			},
			"dataSetResFileName":
			{
				"dataSetServerFileNameRequired": "<@spring.message code='validation.required' />",
				"dataSetServerFilePreviewRequired": "<@spring.message code='dataSet.validation.previewRequired' />",
				"dataSetServerFilePropertiesRequired": "<@spring.message code='dataSet.validation.propertiesRequired' />"
			},
			"sheetIndex":
			{
				"required": "<@spring.message code='validation.required' />",
				"integer": "<@spring.message code='validation.integer' />",
				"min": "<@spring.message code='validation.min' />"
			},
			"nameRowText":
			{
				"integer": "<@spring.message code='validation.integer' />",
				"min": "<@spring.message code='validation.min' />"
			},
			"dataRowExp": {"dataSetExcelDataRowExpRegex": "<@spring.message code='dataSet.validation.excel.dataRowExp.regex' />"},
			"dataColumnExp": {"dataSetExcelDataColumnExpRegex": "<@spring.message code='dataSet.validation.excel.dataColumnExp.regex' />"}
		},
		submitHandler : function(form)
		{
			var formData = $.formToJson(form);
			formData["properties"] = po.getFormDataSetProperties();
			formData["params"] = po.getFormDataSetParams();
			formData["nameRow"] = po.nameRowValue();
			formData["nameRowRadio"] = undefined;
			formData["nameRowText"] = undefined;
			
			var originalFileName = po.element("#${pageId}-originalFileName").val();
			
			$.postJson("${contextPath}/analysis/dataSet/${formAction}?originalFileName="+originalFileName, formData,
			function(response)
			{
				po.pageParamCallAfterSave(true, response.data);
			});
		},
		errorPlacement : function(error, element)
		{
			error.appendTo(element.closest(".form-item-value"));
		}
	});
})
(${pageId});
</script>
</body>
</html>