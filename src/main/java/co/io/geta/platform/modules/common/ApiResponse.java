package co.io.geta.platform.modules.common;

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
public class ApiResponse<T> {

	private T data;
	private boolean success;
	private String message;
	private int statusCode;

}
