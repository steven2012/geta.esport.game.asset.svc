package co.io.geta.platform.crosscutting.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EncryptUtil {

	private EncryptUtil() {
		super();
	}

	public static String encrypt(String text, String secretKey)
			throws EBusinessException, InvalidAlgorithmParameterException {
		try {
			byte[] iv = new byte[GetaConstants.GCM_IV_LENGTH];
			(new SecureRandom()).nextBytes(iv);
			Cipher cipher = Cipher.getInstance(GetaConstants.AES_ALGORITHM);

			cipher.init(Cipher.ENCRYPT_MODE, generateKey(secretKey),
					new GCMParameterSpec(GetaConstants.GCM_TAG_LENGTH * Byte.SIZE, iv));
			byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8.name()));

			ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encrypted.length);
			byteBuffer.put(iv);
			byteBuffer.put(encrypted);

			byte[] base64Bytes = Base64.encodeBase64(byteBuffer.array());
			return new String(base64Bytes);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException
				| IllegalBlockSizeException | BadPaddingException ex) {
			log.error(ex.getMessage());
			throw new EBusinessException(ex.getMessage(), ex.getCause());

		}

	}

	private static SecretKeySpec generateKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {

		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8.name());

		MessageDigest sha = MessageDigest.getInstance(GetaConstants.SHA1);

		keyBytes = sha.digest(keyBytes);
		keyBytes = Arrays.copyOf(keyBytes, 16);

		return new SecretKeySpec(keyBytes, GetaConstants.AES);

	}

	public static String decrypt(String encryptedText, String secretKey) throws EBusinessException {
		try {
			byte[] message = Base64.decodeBase64(encryptedText.getBytes(StandardCharsets.UTF_8.name()));
			ByteBuffer byteBuffer = ByteBuffer.wrap(message);
			byte[] iv = new byte[GetaConstants.GCM_IV_LENGTH];
			byteBuffer.get(iv);
			byte[] encrypted = new byte[byteBuffer.remaining()];
			byteBuffer.get(encrypted);

			Cipher decipher = Cipher.getInstance(GetaConstants.AES_ALGORITHM);
			GCMParameterSpec ivSpec = new GCMParameterSpec(GetaConstants.GCM_TAG_LENGTH * Byte.SIZE, iv);

			decipher.init(Cipher.DECRYPT_MODE, generateKey(secretKey), ivSpec);

			byte[] plainText = decipher.doFinal(encrypted);

			return new String(plainText, StandardCharsets.UTF_8.name());

		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | UnsupportedEncodingException
				| NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
			log.error(ex.getMessage());
			throw new EBusinessException(ex.getMessage(), ex.getCause());
		}
	}

}
