package com.zestedesavoir.android.internal.ioc;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import timber.log.Timber;

public class DateTypeDeserializer implements JsonDeserializer<Date>, JsonSerializer<Date> {
    private static final String DATE_UTC_MEDIUM = "yyyy-MM-dd'T'HH:mm:ss";
    private final DateFormat dateFormat;

    public DateTypeDeserializer() {
        dateFormat = new SimpleDateFormat(DATE_UTC_MEDIUM, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(dateFormat.format(date));
    }

    @Override
    public Date deserialize(JsonElement jsonElement, Type typeOF, JsonDeserializationContext context) throws JsonParseException {
        Date date = convertStringToDate(DATE_UTC_MEDIUM, jsonElement.getAsString(), true);
        if (date != null) {
            return date;
        }
        return null;
    }

    private Date convertStringToDate(String format, String dateFormatted, boolean isTimeZoneUtc) {
        SimpleDateFormat sourceFormat = new SimpleDateFormat(format, Locale.getDefault());
        if (isTimeZoneUtc) {
            sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        try {
            return sourceFormat.parse(dateFormatted);
        } catch (ParseException e) {
            Timber.e(e);
        }
        return null;
    }
}
