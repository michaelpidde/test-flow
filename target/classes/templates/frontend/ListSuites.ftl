<#include "Header.ftl">
	<form name="testSuite" id="testSuite" method="POST" action="?action=run">
		<input type="hidden" name="selectedTests" id="selectedTests" value="" />
		<input type="hidden" name="selectedMarkets" id="selectedMarkets" value="" />
		
		<fieldset><legend>Suites</legend>
			<#list suites as suite>
				<button class="suiteButton" id="${suite}">${suite}</button>
			</#list>
		</fieldset>
		
		<br />
		
		<fieldset><legend>Roll Your Own</legend>
			<label for="market">Select Market: </label>
			<select name="markets" id="markets" multiple="multiple">
				<#list markets as market>
				<option value="${market}">${market}</option>
				</#list>
			</select>
			<br />
			<table cellpadding="1" cellspacing="0" border="1">
				<tr>
					<th>Test</th>
					<th>Run</th>
				</tr>
				<#list tests as test>
				<tr>
					<td>${test}</td>
					<td><input type="checkbox" class="test" name="${test}" value="${test}" /></td>
				</tr>
				</#list>
			</table>
			<br />
			<table cellpadding="1" cellspacing="0" border="1">
				<tr>
					<th colspan="2">
						<p style="font-weight: bold;">Options</p>
					</th>
				</tr>
				<tr>
					<td>
						<label for="logResults">Log Results</label>
					</td>
					<td>
						<input type="checkbox" name="logResults" id="logResults" checked="checked" />
					</td>
				</tr>
			</table>
			<br />
			<input type="submit" name="submit" value="Run Tests" />
		</fieldset>
	</form>
<#include "Footer.ftl">