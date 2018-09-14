package net.intelie.challenges;

import java.sql.Date;

public class DateUtils {

	public static boolean isDateBeetween(long currentDate, long startDate, long endDate) {
		Date currentDateObj = new Date(currentDate);
		Date startDateObj = new Date(startDate);
		Date endDateObj = new Date(endDate);
		
		return currentDateObj.after(startDateObj) && currentDateObj.before(endDateObj);

	}
}
