<!doctype html>
<html>
	<head>
		<title>${title}</title>
	</head>
<body>
	<#assign resultLinkIndex = 0>
	<#list suite as run>
		<table cellpadding="3" cellspacing="0" border="1">
			<tr>
				<th>Test</th>
				<th>Status</th>
				<th>Details</th>
				<th>Duration (s)</th>
			</tr>
		
			<#list run as result>
				<#if result.passed>
					<tr>
				<#else>
					<tr style="background-color:thistle;">
				</#if>
					<td>${result.testName}</td>
					<td>${result.passed?c}</td>
					<td><a href="${resultLinks[resultLinkIndex]}" target="right">View</a>
					<td>${result.timeElapsed()}</td>
				</tr>
				
				<#assign resultLinkIndex = resultLinkIndex + 1>
			</#list> <!-- End run loop -->
			
		</table>
		<br />
	</#list> <!-- End suite loop -->
</body>
</html>