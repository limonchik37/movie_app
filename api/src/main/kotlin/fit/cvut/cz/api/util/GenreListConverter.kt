package fit.cvut.cz.api.util

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class GenreListConverter : AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(attribute: List<String>?): String {
        return attribute?.joinToString(",") ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        return dbData?.split(",")?.map { it.trim() } ?: emptyList()
    }
}