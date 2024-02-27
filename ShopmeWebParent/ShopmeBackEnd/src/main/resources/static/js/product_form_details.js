$(document).ready(function() {
	
	$("a[name='linkRemoveDetail']").each(function(index){
		
		$(this).click(function(){
			
			removeDetailSectionByIndex(index);
		});
	});

});

function addNextDetailSection(){
	
	let allDivDetails = $("[id^='divDetail']");
	let divDetailsCount = allDivDetails.length;
	
	let htmlDetailSection=`
	    <div class="form-inline" id="divDetail${divDetailsCount}">
	        <input type="hidden" name="detailIDs" value="0" />
			<label class="m-3">Name:</label>
			<input type="text" class="form-control w-25 mr-3" name="detailNames" maxlength="255" />
			<label class="m-3">Value:</label>
			<input type="text" class="form-control w-25" name="detailValues" maxlength="255" />
		</div>
	`;
	
	$("#divProductDetails").append(htmlDetailSection);
	
	let previousDivDetailSection = allDivDetails.last();
	
	let previousDivDetailId = previousDivDetailSection.attr("id");
	
	let htmlLinkRemove = `
	      <a class="btn fas fa-times-circle fa-2x icon-red" title="Remove this detail" 
	      href="javascript:removeDetailSectionById('${previousDivDetailId}')"></a>
	`;
	
	previousDivDetailSection.append(htmlLinkRemove);
	
	$("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id){
	
	$("#" + id).remove();
}

function removeDetailSectionByIndex(index){
	
	$("#divDetail" + index).remove();
}
