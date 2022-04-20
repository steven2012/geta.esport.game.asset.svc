package co.io.geta.platform.modules.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import co.io.geta.platform.crosscutting.domain.BlobTokenDto;
import co.io.geta.platform.crosscutting.domain.DeleteAssetDto;
import co.io.geta.platform.crosscutting.domain.UploadFileDto;
import co.io.geta.platform.crosscutting.payload.BlobTokenPayload;
import co.io.geta.platform.crosscutting.payload.DeleteAssetPayload;
import co.io.geta.platform.crosscutting.payload.UploadFilePayload;
import co.io.geta.platform.modules.common.ApiResponse;
import co.io.geta.platform.modules.dataprovider.BlobDataProvider;

@Component
public class BlobProcess {

	@Autowired
	private BlobDataProvider blobDataProvider;

	public ApiResponse<BlobTokenDto> callblobRestAPIWithSas(BlobTokenPayload payload) {
		return blobDataProvider.callblobRestAPIWithSas(payload);
	}

	public ApiResponse<UploadFileDto> uploadFile(MultipartFile file, UploadFilePayload payload) {
		return blobDataProvider.uploadFileBlobStorage(file, payload);
	}

	public ApiResponse<DeleteAssetDto> deleteAsset(DeleteAssetPayload payload) {
		return blobDataProvider.deleteAsset(payload);
	}
}
