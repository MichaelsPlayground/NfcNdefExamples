package de.androidcrypto.nfcndefexamples;

import android.os.Build;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class Utils {

    public static String getTimestamp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return ZonedDateTime
                    .now(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("uuuu.MM.dd HH:mm:ss"));
        } else {
            return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
        }
    }

    public static String parseTextrecordPayload(byte[] ndefPayload) {
        int languageCodeLength = Array.getByte(ndefPayload, 0);
        int ndefPayloadLength = ndefPayload.length;
        byte[] languageCode = new byte[languageCodeLength];
        System.arraycopy(ndefPayload, 1, languageCode, 0, languageCodeLength);
        byte[] message = new byte[ndefPayloadLength - 1 - languageCodeLength];
        System.arraycopy(ndefPayload, 1 + languageCodeLength, message, 0, ndefPayloadLength - 1 - languageCodeLength);
        return new String(message, StandardCharsets.UTF_8);
    }

}
