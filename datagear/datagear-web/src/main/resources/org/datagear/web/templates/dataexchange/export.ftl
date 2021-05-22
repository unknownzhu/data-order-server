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
<div id="${pageId}" class="page-dataexchange-type page-dataexport-type">
	<div class="head">
	</div>
	<div class="content">
		<form id="${pageId}-form" action="" method="POST">
			<div class="init-sqls" style="display:none;">
				<#if initSqls??>
				<#list initSqls as initSql>
					<textarea name="initSqls">${initSql}</textarea>
				</#list>
				</#if>
			</div>
			<div class="form-head">
				<@spring.message code='dataExport.selectDataType' />
			</div>
			<div class="form-content">
				<div class="form-item">
					<div class="form-item-label">&nbsp;</div>
					<div class="form-item-value">
						<label for="${pageId}-dataType-0"><@spring.message code='dataExport.dataType.csv' /></label>
						<input id="${pageId}-dataType-0" type="radio" name="dataType" value="csv" />
						<div class="input-desc minor">
							<@spring.message code='dataExport.dataType.csv.desc' />
						</div>
					</div>
				</div>
				<div class="form-item">
					<div class="form-item-label">&nbsp;</div>
					<div class="form-item-value">
						<label for="${pageId}-dataType-1"><@spring.message code='dataExport.dataType.sql' /></label>
						<input id="${pageId}-dataType-1" type="radio" name="dataType" value="sql" />
						<div class="input-desc minor">
							<@spring.message code='dataExport.dataType.sql.desc' />
						</div>
					</div>
				</div>
				<div class="form-item">
					<div class="form-item-label">&nbsp;</div>
					<div class="form-item-value">
						<label for="${pageId}-dataType-2"><@spring.message code='dataExport.dataType.json' /></label>
						<input id="${pageId}-dataType-2" type="radio" name="dataType" value="json" />
						<div class="input-desc minor">
							<@spring.message code='dataExport.dataType.json.desc' />
						</div>
					</div>
				</div>
				<div class="form-item">
					<div class="form-item-label">&nbsp;</div>
					<div class="form-item-value">
						<label for="${pageId}-dataType-3"><@spring.message code='dataExport.dataType.excel' /></label>
						<input id="${pageId}-dataType-3" type="radio" name="dataType" value="excel" />
						<div class="input-desc minor">
							<@spring.message code='dataExport.dataType.excel.desc' />
						</div>
					</div>
				</div>
			</div>
			<div class="form-foot">
				<button type="submit" class="recommended">
					<@spring.message code='confirm' />
				</button>
			</div>
		</form>
	</div>
	<div class="foot">
	</div>
</div>
<#if !isAjaxRequest>
</div>
</#if>

<#include "../include/page_js_obj.ftl">
<script type="text/javascript">
(function(po)
{
	po.schemaId = "${schema.id}";
	
	$.initButtons(po.element());
	
	po.element("input[name='dataType']").change(function()
	{
		var dataType = $(this).val();
		po.element("#${pageId}-form").attr("action", "${contextPath}/dataexchange/" + po.schemaId +"/export/" + dataType);
	});
	
	po.element("input[type=radio]").checkboxradio({icon:true});
	po.element("#${pageId}-dataType-0").click();
	
	<#if isAjaxRequest>
	po.element("#${pageId}-form").ajaxForm(
	{
		success: function(data)
		{
			po.element().parent().html(data);
		}
	});
	</#if>
})
(${pageId});
</script>
</body>
</html>
