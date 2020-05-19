package com.jon.cotbeacon.cot;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* For "easier" translating between milliseconds and strings without littering annoying little
 * utility functions all over the place. */
public class UtcTimestamp {
    private String isoTimestamp;
    private long milliseconds;

    public long toLong() {
        return milliseconds;
    }

    @NonNull
    @Override
    public String toString() {
        return isoTimestamp;
    }

    public static UtcTimestamp now() {
        return new UtcTimestamp(System.currentTimeMillis());
    }

    public UtcTimestamp(String str) throws IllegalArgumentException {
        if (isValidString(str)) {
            milliseconds = timestampStringToLong(str);
            isoTimestamp = str;
        } else {
            String newStr = fullString(str);
            if (isValidString(newStr)) {
                milliseconds = timestampStringToLong(newStr);
                isoTimestamp = newStr;
            } else {
                throw new IllegalArgumentException("Invalid string passed to UtcTimestamp: '" + str + "'");
            }
        }
    }

    public UtcTimestamp(long ms) {
        milliseconds = ms;
        isoTimestamp = timestampLongToString(ms);
    }

    public UtcTimestamp add(long ms) {
        return new UtcTimestamp(milliseconds + ms);
    }

    public UtcTimestamp add(String str) throws IllegalArgumentException {
        if (isValidString(str)) {
            return new UtcTimestamp(milliseconds + timestampStringToLong(str));
        } else {
            String newStr = fullString(str);
            if (isValidString(newStr)) {
                return new UtcTimestamp(milliseconds + timestampStringToLong(newStr));
            } else {
                throw new IllegalArgumentException("Invalid string passed to UtcTimestamp: '" + str + "'");
            }
        }
    }

    /* Accepts strings in the format:
     *       "yyyy-mm-dd hh:mm:ss.sss"
     *       "yyyy-mm-ddThh:mm:ss.sssZ" */
    private boolean isValidString(String timestamp) {
        final String pattern = "(\\d{4}-\\d{2}-\\d{2}.\\d{2}:\\d{2}:\\d{2}(\\.\\d{1,3})?Z*$)";
        return timestamp.matches(pattern);
    }

    /* I don't quite remember why this was needed, but I'd better not delete it or I'll soon need
     * it again */
    private String cleanedString(String str) {
        return str.replace("T", " ").replace("Z", "");
    }

    /* CoT requires the T and Z in the timestamp string */
    private String fullString(String str) {
        /* 0 to 10 is "yyyy-mm-dd", 11 to lastDigit+1 is "hh:mm:ss.sss". I've put the lastDigit
         * finding in here to account for the optional decimal places after seconds on the string.
         * So "2019-07-29T01:23:45.678Z" and "2019-07-29T01:23:45Z" are both handled equally */
        int lastDigit = -1;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                lastDigit = i;
            }
        }
        return str.substring(0, 10) + "T" + str.substring(11, lastDigit + 1) + "Z";
    }

    private long timestampStringToLong(String timestamp) {
        try {
            Timestamp time = Timestamp.valueOf(fullString(timestamp));
            return time.getTime();
        } catch (IllegalArgumentException e) {
            Timestamp time = Timestamp.valueOf(cleanedString(timestamp));
            return time.getTime();
        }
    }

    private String timestampLongToString(long ms) {
        Date date = new Date(ms);
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).format(date);
    }
}
