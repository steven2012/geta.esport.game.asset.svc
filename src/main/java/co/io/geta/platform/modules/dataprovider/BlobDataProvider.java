package co.io.geta.platform.modules.dataprovider;

import org.springframework.web.multipart.MultipartFile;

import co.io.geta.platform.crosscutting.domain.BlobTokenDto;
import co.io.geta.platform.crosscutting.domain.DeleteAssetDto;
import co.io.geta.platform.crosscutting.domain.UploadFileDto;
import co.io.geta.platform.crosscutting.payload.BlobTokenPayload;
import co.io.geta.platform.crosscutting.payload.DeleteAssetPayload;
import co.io.geta.platform.crosscutting.payload.UploadFilePayload;
import co.io.geta.platform.modules.common.ApiResponse;

public interface BlobDataProvider {

	public ApiResponse<BlobTokenDto> callblobRestAPIWithSas(BlobTokenPayload payload);

	public ApiResponse<UploadFileDto> uploadFileBlobStorage(MultipartFile file, UploadFilePayload payload);

	public ApiResponse<DeleteAssetDto> deleteAsset(DeleteAssetPayload payload);
}