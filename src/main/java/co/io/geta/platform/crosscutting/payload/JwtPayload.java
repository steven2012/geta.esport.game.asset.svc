package co.io.geta.platform.crosscutting.payload;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Data
public class JwtPayload implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	/**
	 * Refers to username as part of authentication credentials
	 */
	private String username;

	/**
	 * Refers to Password as part of authentication credentials
	 */
	private String password;

}
