<!doctype html>
<html>
	<head>
		<title>${title}</title>
	</head>
<body>
	<#if steps?has_content>
		<table cellpadding="3" cellspacing="0" border="1">
			<#list steps as step>
				<tr>
					<td><img src="${step.imagePath}" /></td>
					<td>${step.description}</td>
				</tr>
			</#list>
		</table>
	</#if>
	<#if error?length &gt; 0>
		<p>${error}</p>
	</#if>
</body>
</html>