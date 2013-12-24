$(function(){ 
	$(".test").change(
		function(){
			var tests = $(".test:checked").map(function() {
				return this.value;
			}).get();
			$("#selectedTests").val(tests.join());
		}
	);
	
	$("#markets").change(
		function(){
			$("#selectedMarkets").val(
				$(this).val().join()
			);
		}
	);
	
	$("#app").change(function() {
		$("#appSelect").submit();
	});
	
	$(".suiteButton").each(function() {
		$(this).click(function() {
			$("#testSuite").attr("action", $("#testSuite").attr("action") + "&suite=" + $(this).text());
			$("#testSuite").submit();
		});
	});
});