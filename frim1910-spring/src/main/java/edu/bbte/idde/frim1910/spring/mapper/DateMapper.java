package edu.bbte.idde.frim1910.spring.mapper;

import lombok.extern.log4j.Log4j2;
import org.mapstruct.Mapper;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Mapper(componentModel = "spring")
@Log4j2
public class DateMapper {
    public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private SimpleDateFormat dateFormat;

    @PostConstruct
    public void postConstruct() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        dateFormat = new SimpleDateFormat(ISO8601_DATE_FORMAT, Locale.getDefault());
    }

    public String asString(Date date) {
        return date == null ? null : dateFormat.format(date);
    }

    public Date asDate(String date) throws ParseException {
        return date == null ? null : dateFormat.parse(date);
    }
}
