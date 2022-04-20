package co.io.geta.platform.crosscutting.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import co.io.geta.platform.crosscutting.constants.GetaConstants;
import co.io.geta.platform.crosscutting.exception.EBusinessException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DateTimeZoneUtil {

	public Date getUTCdatetimeAsDate() throws EBusinessException {
		// note: doesn't check for null
		return stringDateToDate(getUTCdatetimeAsString());
	}

	private String getUTCdatetimeAsString() {
		final SimpleDateFormat sdf = new SimpleDateFormat(GetaConstants.PATTERN_DATE);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(new Date());
	}

	private Date stringDateToDate(String strDate) throws EBusinessException {
		Date dateToReturn = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(GetaConstants.PATTERN_DATE);

		try {
			dateToReturn = dateFormat.parse(strDate);
		} catch (Exception e) {

			throw new EBusinessException(e.getMessage());
		}

		return dateToReturn;
	}

}
