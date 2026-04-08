package fit.cvut.cz.notificationservice.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaConsumerConfig(
    @Value("\${spring.kafka.bootstrap-servers}") private val bootstrapServers: String
) {

    private fun baseProps(): Map<String, Any> = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
        ConsumerConfig.GROUP_ID_CONFIG to "notification-group",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest"
    )

    @Bean
    fun reviewCreatedConsumerFactory(): ConsumerFactory<String, ReviewCreatedEvent> =
        DefaultKafkaConsumerFactory(
            baseProps(),
            StringDeserializer(),
            JsonDeserializer(ReviewCreatedEvent::class.java, false)
        )

    @Bean
    fun reviewCreatedListenerFactory(): ConcurrentKafkaListenerContainerFactory<String, ReviewCreatedEvent> =
        ConcurrentKafkaListenerContainerFactory<String, ReviewCreatedEvent>().also {
            it.consumerFactory = reviewCreatedConsumerFactory()
        }

    @Bean
    fun reviewDeletedConsumerFactory(): ConsumerFactory<String, ReviewDeletedEvent> =
        DefaultKafkaConsumerFactory(
            baseProps(),
            StringDeserializer(),
            JsonDeserializer(ReviewDeletedEvent::class.java, false)
        )

    @Bean
    fun reviewDeletedListenerFactory(): ConcurrentKafkaListenerContainerFactory<String, ReviewDeletedEvent> =
        ConcurrentKafkaListenerContainerFactory<String, ReviewDeletedEvent>().also {
            it.consumerFactory = reviewDeletedConsumerFactory()
        }

    @Bean
    fun userRegisteredConsumerFactory(): ConsumerFactory<String, UserRegisteredEvent> =
        DefaultKafkaConsumerFactory(
            baseProps(),
            StringDeserializer(),
            JsonDeserializer(UserRegisteredEvent::class.java, false)
        )

    @Bean
    fun userRegisteredListenerFactory(): ConcurrentKafkaListenerContainerFactory<String, UserRegisteredEvent> =
        ConcurrentKafkaListenerContainerFactory<String, UserRegisteredEvent>().also {
            it.consumerFactory = userRegisteredConsumerFactory()
        }
}
