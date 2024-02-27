let extraImageCounter = 0;

$(document).ready(function() {
	
	$("input[name='extraImage']").each(function(index){
		
		extraImageCounter++;
		
		$(this).change(function(){
			
			if (!checkFileSize(this)) {
				
				return;
			}
			
			showExtraImageThumbnail(this, index);
		});
	});
	
	$("a[name='linkRemoveExtraImage']").each(function(index){
		
		$(this).click(function(){
			
			removeExtraImage(index);
		});
	});

});

function showExtraImageThumbnail(fileInput, index) {

	let file = fileInput.files[0];
	
	let fileName = file.name;
	
	let imageNameHiddenField = $("#imageName" + index);
	
	if(imageNameHiddenField.length){
		
		imageNameHiddenField.val(fileName);
	}
	
	let reader = new FileReader();

	reader.onload = function(e) {

		$("#extraThumbnail" + index).attr("src", e.target.result);
	};

	reader.readAsDataURL(file);

    if(index >= extraImageCounter - 1 ){
		
		addNextExtraImageSection(index + 1);
	}
}

function addNextExtraImageSection(index) {

	let htmlExtraIamge = `
	    <div class="col border m-3 p-2" id="divExtraImage${index}">
			<div id="extraImageHeader${index}">
				<label>Extra Image #${index + 1}:</label>
			</div>
			<div class="mb-2">
				<img id="extraThumbnail${index}" alt="extra-image-#${index + 1}-preview" class="img-fluid" src="${defaulImageThumbnail}" />
			</div>
			<div>
				<input type="file" name="extraImage" accept="image/png, image/jpg, image/jpeg" onchange="showExtraImageThumbnail(this, ${index})" />
			</div>
		</div>
	`;
	
	let htmlLinkRemove = `
	      <a class="btn fas fa-times-circle fa-2x icon-red float-right" title="Remove this image" 
	      href="javascript:removeExtraImage(${index - 1})"></a>
	`;
	
	$("#divProductImages").append(htmlExtraIamge);
	
	$("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
	
	extraImageCounter++;
}

function removeExtraImage(index){
	
	$("#divExtraImage" + index).remove();
}
