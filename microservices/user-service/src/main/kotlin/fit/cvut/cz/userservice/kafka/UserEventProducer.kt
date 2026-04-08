package fit.cvut.cz.userservice.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

const val TOPIC_USER_REGISTERED = "user.registered"

@Component
class UserEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val log = LoggerFactory.getLogger(UserEventProducer::class.java)

    fun publishUserRegistered(event: UserRegisteredEvent) {
        log.info("Publishing user.registered event for userId=${event.userId}")
        kafkaTemplate.send(TOPIC_USER_REGISTERED, event.userId.toString(), event)
    }
}
