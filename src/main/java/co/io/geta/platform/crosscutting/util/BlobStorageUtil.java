package co.io.geta.platform.crosscutting.util;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.springframework.web.multipart.MultipartFile;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.SharedAccessBlobPermissions;
import com.microsoft.azure.storage.blob.SharedAccessBlobPolicy;

import co.io.geta.platform.crosscutting.exception.EBusinessException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BlobStorageUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a connection to the blob storage.
	 * 
	 * @param connectionString: connection string blob storage
	 * @return CloudBlobClient: reference object blob storage
	 */
	private CloudBlobClient createCloudBlobClient(String connectionString)
			throws InvalidKeyException, URISyntaxException {
		CloudStorageAccount account = CloudStorageAccount.parse(connectionString);
		return account.createCloudBlobClient();
	}

	/**
	 * Obtain reference Blob Container blob storage.
	 * 
	 * @param connectionString: connection string blob storage
	 * @param containerName:    name container in blob storage
	 * @return CloudBlobContainer: reference object blob container
	 */
	private CloudBlobContainer getBlobConatiner(String connectionString, String containerName)
			throws InvalidKeyException, URISyntaxException, StorageException {
		CloudBlobClient cloudBlobClient = createCloudBlobClient(connectionString);
		return cloudBlobClient.getContainerReference(containerName);
	}

	/**
	 * upload file to blob storage in specific container.
	 * 
	 * @param connectionString: connection string blob storage
	 * @param containerName:    name container in blob storage
	 * @param filePath:         path blob in BlobStorage
	 * @Param contentType: content file to upload
	 * @param file: reference file to upload
	 * @return CloudBlobContainer: reference object blob container
	 */
	public CloudBlockBlob create(String connectionString, String containerName, String filePath, String contentType,
			MultipartFile file) throws EBusinessException {
		try {
			CloudBlobContainer cloudBlobContainer = getBlobConatiner(connectionString, containerName);
			CloudBlockBlob cloudBlockBlob = cloudBlobContainer.getBlockBlobReference(filePath);
			cloudBlockBlob.getProperties().setContentType(contentType);
			cloudBlockBlob.upload(file.getInputStream(), file.getSize());
			return cloudBlockBlob;
		} catch (Exception ex) {
			throw new EBusinessException(ex.getMessage());
		}
	}

	/**
	 * allow download file of Blob Storage.
	 * 
	 * @param connectionString: connection string blob storage
	 * @param containerName:    name container in blob storage
	 * @param filePath:         path blob in BlobStorage
	 * @return Byte[]: reference to content file
	 */
	public byte[] read(String connectionString, String containerName, String filePath) throws EBusinessException {
		try {
			CloudBlobContainer cloudBlobContainer = getBlobConatiner(connectionString, containerName);
			CloudBlockBlob cloudBlockBlob = cloudBlobContainer.getBlockBlobReference(filePath);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			cloudBlockBlob.download(outputStream);
			byte[] byteArray = outputStream.toByteArray();
			outputStream.close();
			return byteArray;
		} catch (Exception ex) {
			throw new EBusinessException(ex.getMessage());
		}
	}

	/**
	 * allow delete file of Blob Storage.
	 * 
	 * @param connectionString: connection string blob storage
	 * @param containerName:    name container in blob storage
	 * @param filePath:         path blob in BlobStorage
	 * @return Boolean: true or false if file was deleted
	 */
	public boolean delete(String connectionString, String containerName, String filePath) throws EBusinessException {
		try {
			CloudBlobContainer cloudBlobContainer = getBlobConatiner(connectionString, containerName);
			CloudBlockBlob cloudBlockBlob = cloudBlobContainer.getBlockBlobReference(filePath);
			return cloudBlockBlob.deleteIfExists();
		} catch (InvalidKeyException | URISyntaxException | StorageException ex) {
			throw new EBusinessException(ex.getMessage());
		}
	}

	/**
	 * Generate Sas token to access file in blob storage.
	 * 
	 * @param connectionString: connection string blob storage
	 * @param containerName:    name container in blob storage
	 * @param filePath:         path blob in BlobStorage
	 * @return String: url with access token to access file storage
	 */
	public String sasURL(String connectionString, String containerName, String filePath) throws EBusinessException {
		try {
			CloudBlobContainer cloudBlobContainer = getBlobConatiner(connectionString, containerName);
			CloudBlockBlob cloudBlockBlob = cloudBlobContainer.getBlockBlobReference(filePath);
			SharedAccessBlobPolicy sasPolicy = new SharedAccessBlobPolicy();
			GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
			calendar.add(Calendar.HOUR, 1);
			sasPolicy.setSharedAccessExpiryTime(calendar.getTime());
			sasPolicy.setPermissions(EnumSet.of(SharedAccessBlobPermissions.READ, SharedAccessBlobPermissions.WRITE,
					SharedAccessBlobPermissions.LIST));
			String sas = cloudBlockBlob.generateSharedAccessSignature(sasPolicy, null);
			return cloudBlockBlob.getUri() + "?" + sas;
		} catch (Exception ex) {
			throw new EBusinessException(ex.getMessage());
		}
	}

}
