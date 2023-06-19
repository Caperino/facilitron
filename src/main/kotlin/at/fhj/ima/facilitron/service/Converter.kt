package at.fhj.ima.facilitron.service

import at.fhj.ima.facilitron.model.Gender
import at.fhj.ima.facilitron.model.Priority
import at.fhj.ima.facilitron.model.TicketStatus
import at.fhj.ima.facilitron.model.WorkingType
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

@Component
class StringToPriority : Converter<String, Priority> {
    override fun convert(source: String): Priority? {
        if (source.isBlank()) {
            return null
        }
        return Priority.valueOf(source.toUpperCase());
    }
}

@Component
class StringToTicketStatus : Converter<String, TicketStatus> {
    override fun convert(source: String): TicketStatus? {
        if (source.isBlank()) {
            return null
        }
        return TicketStatus.valueOf(source.toUpperCase());
    }
}

@Component
class StringToWorkingType : Converter<String, WorkingType> {
    override fun convert(source: String): WorkingType? {
        if (source.isBlank()) {
            return null
        }
        return WorkingType.valueOf(source.toUpperCase());
    }
}