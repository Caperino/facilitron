package at.fhj.ima.facilitron.service

import at.fhj.ima.facilitron.model.Gender
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class StringToDate : Converter<String, LocalDate> {
    override fun convert(source: String): LocalDate? {
        if (source.isBlank()) {
            return null
        }
        return LocalDate.parse(source, DateTimeFormatter.ISO_LOCAL_DATE)
    }

}

@Component
class StringToGender : Converter<String, Gender> {
    override fun convert(source: String): Gender? {
        if (source.isBlank()) {
            return null
        }
        return Gender.valueOf(source.toUpperCase());
    }
}