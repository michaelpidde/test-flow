<#include "Header.ftl">
	<form method="POST" id="appSelect" action="?action=ListSuites">
		<label for="app">Select Application: </label>
		<select name="app" id="app">
			<option value="">Select...</option>
			<#list apps as app>
			<option value="${app}">${app}</option>
			</#list>
		</select>
	</form>
<#include "Footer.ftl">