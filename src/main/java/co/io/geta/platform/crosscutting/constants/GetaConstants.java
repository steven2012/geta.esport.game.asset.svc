package co.io.geta.platform.crosscutting.constants;

public class GetaConstants {

	private GetaConstants() {
	}

	// DB
	public static final String ENTITY_MANAGER_FACTORY = "entityManagerFactory";
	public static final String ENTITY_GAME_ASSET_MANAGER_FACTORY = "gameassetEntityManagerFactory";

	public static final String BASE_PACKAGES = "co.io.geta.platform.crosscutting.persistence.repository";
	public static final String BASE_PACKAGES_GAME_ASSET = "co.io.geta.platform.crosscutting.persistence.game.asset.repository";

	// Config Data Source Game-core
	public static final String NAME_BEAN_DATA_SOURCE = "db-games";
	public static final String NAME_BEAN_DATA_SOURCE_GAME_ASSET = "db-game-asset";

	public static final String DATASOURCE_GAME = "${game.core.connection.string}";
	public static final String DATASOURCE_GAME_ASSET = "${game.asset.connection.string}";

	public static final String ENTITY_PACKAGES = "co.io.geta.platform.crosscutting.persistence.entity";
	public static final String ENTITY_PACKAGES_GAME_ASSET = "co.io.geta.platform.crosscutting.persistence.game.asset.entity";
	public static final String PERSISTENCE_UNIT = "game";
	public static final String PERSISTENCE_UNIT_GAME_ASSET = "gameasset";

	public static final String TRANSACTION_MANAGER = "transactionManager";
	public static final String TRANSACTION_MANAGER_GAME_ASSET = "transactionManagerGameAsset";

	// App parameters Encryp-encodign
	public static final String AES_ALGORITHM = "AES/GCM/NoPadding";
	public static final String SHA1 = "SHA-1";
	public static final String AES = "AES";
	public static final String UTF_8 = "UTF-8";
	public static final String SHA_256 = "HmacSHA256";

	public static final String SEED_ENCRYPTOR = "${seed.encryptor}";

	// STORAGE ACCOUNT
	public static final String ACCOUNT_NAME_ENCRYPT = "${storage.account.encryptor}";
	public static final String ACCOUNT_KEY_STORAGE = "${storage.account.key}";
	public static final String CONECTION_ACCOUNT_STORAGE = "${storage.connection.string}";
	public static final int GCM_IV_LENGTH = 12;
	public static final int GCM_TAG_LENGTH = 16;

	// Limit Url Token Blob Storage
	public static final String LIMIT_TOKEN = "${limit.acces.blob.storage}";

	// FORMAT DATE
	public static final String PATTERN_DATE = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_DATE_BLOB_STORAGE = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	/// URL API
	public static final String ROOT_API_STORAGE = "/game";
	public static final String ROOT_API_TOKEN = "/token";
	public static final String ROOT_BLOB_STORAGE = "/storage";
	public static final String GAME_BY_ID = "/gameId";
	public static final String GAME_ASSETS = "/asset";
	public static final String UPLOAD_ASSET = "/upload-asset";
	public static final String USER_APLICATION = "/user-application";
	public static final String USER_APLICATION_BY_ID = "/user-application-by-userid";
	public static final String APLICATION_VERSION = "/application-version";
	public static final String APLICATION_VERSION_BY_NAME = "/application-version-by-name";
	public static final String AUTHENTICATE = "/authenticate";
	public static final String BLOB = "/blob";

	/// HEADER FILTER SECURITY
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_HEADER = "Bearer ";
	public static final String JWT_SECRET = "${jwt.secret}";

	// KEY DICTIONARY JSON
	public static final String DATA = "data";
	public static final String MESSAGE = "message";
	public static final String IS_DELETE_KEY = "isDelete";
	public static final String KEY_RULES = "KeyRules";
	public static final String DETAILS = "Details";

	// Message
	public static final String GAME_ID_NOT_FOUND = "game.id.not.found";
	public static final String UNAUTHORIZED_MESSAGE = "unauthorized.messages";
	public static final String EXPIRED_TOKEN_MESSAGE = "expired.token";
	public static final String ATRIBUTE_EXPIRED = "filter.atribute.expired";
	public static final String NOT_BEARER_CONTAIN_MESSAGE = "not.begin.with.bearer";
	public static final String UNABLE_GET_TOKEN = "unable.to.get.jwt.token";
	public static final String INVALID_CREDENTIALS = "invalid.credential";
	public static final String USER_DISABLED = "user.disable";
	public static final String USER_NOT_FOUND = "user.not.found";
	public static final String USER_GAME_NOT_FOUND = "user.game.not.found";
	public static final String NOT_RESOURCES_NAME = "not.resources.name";
	public static final String NOT_GET_TOKEN = "not.get.token";
	public static final String CATEGORY_NOT_EMPTY = "category.not.empty";
	public static final String GAME_ID_NOT_EMPTY = "gameid.not.empty";
	public static final String MULTIPART_NOT_EMPTY = "multipart.file.not.empty";
	public static final String GAME_ASSET_NOT_SAVED = "not.saved.game.asset";
	public static final String UPLOAD_FILED_SUCCES = "upload.succes";
	public static final String PAYLOAD_NOT_NULL = "payload.not.null";
	public static final String NAME_ASSET_NOT_EMPTY = "name.asset.not.empty";
	public static final String NOT_CAN_DELETE_ASSET = "not.can.delete.asset";
	public static final String DELETE_ASSET_SUCCES = "delete.asset.succes";
	public static final String RECORD_SAVED = "record.saved";
	public static final String RECORD_UPDATED = "record.updated";
	public static final String ASSET_HAS_BEEN_UPDATED = "asset.has.been.updated";
	public static final String NAME_VERSION_APP_NOT_EMPTY = "name.version.app.not.empty";
	public static final String OPERATION_NOT_SUCCES = "operation.not.succes";
	public static final String LIST_CANNOT_EMPTY = "list.asset.not.empty";
	public static final String APLICATION_VERSION_NOT_EXIST = "aplication.version.not.exist";
	public static final String APLICATION_VERSION_ID_NOT_EMPTY = "aplication.version.not.empty";
	public static final String USER_ID_NOT_EMPTY = "user.id.not.empty";
	public static final String THERE_IS_NOT_INFORMATION_CONSULT = "there.is.no.information.consult";
	public static final String APLICATION_VERSION_EXIST = "aplication.version.exist";
	public static final String APLICATION_VERSION_NOT_FOUND = "aplication.vertion.not.found";
	public static final String CANT_NOT_UPLOAD_FILE = "not.can.upload.file";

	// HOSTS
	public static final String HOST_KEY_STORAGE = "${host.uri.key.storage}";
	public static final String HOST_USER_IDENTITY = "${host.uri.user.identity}";
	public static final String URI_KEY_VAUL = "https://kv-getaclub-dev.vault.azure.net/";
	public static final String HOST_BLOB_STORAGE_SVCL = "${host.blob.stoage.svc}";

	// Name blob container
	public static final String NAME_BLOB_CONTAINER = "game-assets";
	public static final String NAME_FOLDER_ASSET = "/Assets/";

	// URI Proyect Blob Storage SVC
	public static final String URI_UPLOAD_BLOB_STORAGE = "/upload-storage";
	public static final String URI_SAS_TOKEN_BLOB_STORAGE = "/sas-token";

}
