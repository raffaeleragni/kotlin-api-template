package app.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.io.IOException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME

@Configuration
class JacksonConfig {
  @Bean
  @Primary
  fun objectMapper(): ObjectMapper {
    val mapper = JsonMapper.builder()
      .enable(SerializationFeature.INDENT_OUTPUT)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .serializationInclusion(JsonInclude.Include.NON_NULL)
      .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
      .build()

    mapper.registerModule(JavaTimeModule())

    val module = SimpleModule()
    val formatterWrite: DateTimeFormatter = ISO_OFFSET_DATE_TIME
    val formatterRead: DateTimeFormatter = ISO_OFFSET_DATE_TIME
    module.addSerializer(ZonedDateTime::class.java, object : JsonSerializer<ZonedDateTime?>() {
      @Throws(IOException::class)
      override fun serialize(
        zonedDateTime: ZonedDateTime?,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider?
      ) {
        jsonGenerator.writeString(formatterWrite.format(zonedDateTime))
      }
    })
    module.addDeserializer(ZonedDateTime::class.java, object : JsonDeserializer<ZonedDateTime?>() {
      @Throws(IOException::class)
      override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): ZonedDateTime? {
        return ZonedDateTime.from(formatterRead.parse(p.text))
      }
    })
    mapper.registerModule(module)

    mapper.registerModule(KotlinModule.Builder().build())

    return mapper
  }
}
