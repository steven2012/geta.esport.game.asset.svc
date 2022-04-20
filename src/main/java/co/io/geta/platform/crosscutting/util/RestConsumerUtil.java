package co.io.geta.platform.crosscutting.util;

import java.util.Arrays;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestConsumerUtil {

	public Object restConsumer(Object trace, String restUri, HttpMethod method) {
		try {

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Object> entity = new HttpEntity<>(trace, headers);
			return restTemplate.exchange(restUri, method, entity, Object.class).getBody();
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return null;
		}

	}

	/**
	 * 
	 * @param file            to upload in blob storage.
	 * @param restUri         url of microservices blo.storage.svc
	 * @param nameContainer   name of container in blob storage
	 * @param structureFolder structure of folder to saved in blob storage
	 * @return response of microservice blob.storage.svc
	 */
	public Object multipartConsumerUploadFile(MultipartFile file, String restUri, String nameContainer,
			String structureFolder) {
		try {

			ByteArrayResource contentsAsResource = new ByteArrayResource(file.getBytes()) {
				@Override
				public String getFilename() {
					return file.getOriginalFilename();
				}
			};

			MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

			parts.add("FileToUpload", contentsAsResource);
			parts.add("NameContainer", nameContainer);
			parts.add("AddStructureFolder", structureFolder);

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(parts, headers);
			return restTemplate.exchange(restUri, HttpMethod.POST, entity, Object.class).getBody();
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return null;
		}

	}

}
