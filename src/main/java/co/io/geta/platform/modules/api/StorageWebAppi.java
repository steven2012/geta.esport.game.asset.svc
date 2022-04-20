package co.io.geta.platform.modules.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.domain.DeleteAssetDto;
import co.io.geta.platform.crosscutting.domain.UploadFileDto;
import co.io.geta.platform.crosscutting.payload.DeleteAssetPayload;
import co.io.geta.platform.crosscutting.payload.UploadFilePayload;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.usecases.BlobProcess;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping(value = GetaConstants.ROOT_BLOB_STORAGE, produces = { MediaType.APPLICATION_JSON_VALUE })
public class StorageWebAppi {

	@Autowired
	private BlobProcess blobProcess;

	@ApiOperation(value = "Upload file blob storage", notes = "allow Upload files to blob storage regarding Assets File")
	@PostMapping(GetaConstants.UPLOAD_ASSET)
	public ApiResponse<UploadFileDto> submit(@ModelAttribute MultipartFile file,
			@ModelAttribute UploadFilePayload payload) {
		return blobProcess.uploadFile(file, payload);
	}

	@ApiOperation(value = "Delete file blob storage", notes = "Allows you to delete an asset by its file name")
	@DeleteMapping(GetaConstants.GAME_ASSETS)
	public ApiResponse<DeleteAssetDto> assetDelete(DeleteAssetPayload payload) {
		return blobProcess.deleteAsset(payload);
	}
}
