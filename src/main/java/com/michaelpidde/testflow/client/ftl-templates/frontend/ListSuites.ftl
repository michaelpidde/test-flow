<#include "Header.ftl">
	<form name="testSuite" id="testSuite" method="POST" action="?action=run">
		<input type="hidden" name="selectedTests" id="selectedTests" value="" />
		<input type="hidden" name="app" id="app" value="${app}" />
		
		<#if suites?size gt 0>
			<fieldset><legend>Suites</legend>
				<#list suites as suite>
					<button class="suiteButton" id="${suite}">${suite}</button>
				</#list>
			</fieldset>
		
		<br />
		
		<fieldset><legend>Roll Your Own</legend>
		</#if>
		
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
					<th>
						<p style="font-weight: bold;">Options</p>
					</th>
				</tr>
				<tr>
					<td>
						<table cellpadding="1" cellspacing="0" border="0">
							<tr>
								<td>
									Browser:
								</td>
								<td>
									<input type="radio" name="browser" value="ff" checked="checked" /> FF
								</td>
								<td>
									<input type="radio" name="browser" value="chrome" /> Chrome
								</td>
								<td>
									<input type="radio" name="browser" value="ie" /> IE
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<label for="logResults">Log Results: </label>
						<input type="checkbox" name="logResults" id="logResults" checked="checked" />
					</td>
				</tr>
			</table>
			<br />
			<input type="submit" id="submit" name="submit" value="Run Tests" />
		<#if suites?size gt 0>
		</fieldset>
		</#if>
	</form>
<#include "Footer.ftl">