package fit.cvut.cz.reviewservice.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

const val TOPIC_REVIEW_CREATED = "review.created"
const val TOPIC_REVIEW_DELETED = "review.deleted"

@Component
class ReviewEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    private val log = LoggerFactory.getLogger(ReviewEventProducer::class.java)

    fun publishReviewCreated(event: ReviewCreatedEvent) {
        log.info("Publishing review.created event: reviewId=${event.reviewId}, movieId=${event.movieId}")
        kafkaTemplate.send(TOPIC_REVIEW_CREATED, event.reviewId.toString(), event)
    }

    fun publishReviewDeleted(event: ReviewDeletedEvent) {
        log.info("Publishing review.deleted event: reviewId=${event.reviewId}")
        kafkaTemplate.send(TOPIC_REVIEW_DELETED, event.reviewId.toString(), event)
    }
}
