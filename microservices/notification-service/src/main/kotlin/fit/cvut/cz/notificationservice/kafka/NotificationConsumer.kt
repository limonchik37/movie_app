package fit.cvut.cz.notificationservice.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class NotificationConsumer(private val mailSender: JavaMailSender) {

    private val log = LoggerFactory.getLogger(NotificationConsumer::class.java)

    /**
     * Triggered when a new review is posted.
     * In production: look up the movie owner's email and notify them.
     */
    @KafkaListener(
        topics = ["review.created"],
        groupId = "notification-group",
        containerFactory = "reviewCreatedListenerFactory"
    )
    fun onReviewCreated(event: ReviewCreatedEvent) {
        log.info(
            "[NOTIFICATION] New review posted! reviewId=${event.reviewId}, " +
                "movieId=${event.movieId}, userId=${event.userId}"
        )
        // Example: send email to admin / movie owner
        sendEmail(
            to = "admin@movieapp.com",
            subject = "New review on movie #${event.movieId}",
            body = "User #${event.userId} left a review:\n\n\"${event.text}\""
        )
    }

    /**
     * Triggered when a review is deleted.
     */
    @KafkaListener(
        topics = ["review.deleted"],
        groupId = "notification-group",
        containerFactory = "reviewDeletedListenerFactory"
    )
    fun onReviewDeleted(event: ReviewDeletedEvent) {
        log.info(
            "[NOTIFICATION] Review deleted: reviewId=${event.reviewId}, " +
                "movieId=${event.movieId}, userId=${event.userId}"
        )
    }

    /**
     * Triggered when a new user registers.
     * Sends a welcome email to the user.
     */
    @KafkaListener(
        topics = ["user.registered"],
        groupId = "notification-group",
        containerFactory = "userRegisteredListenerFactory"
    )
    fun onUserRegistered(event: UserRegisteredEvent) {
        log.info("[NOTIFICATION] New user registered: userId=${event.userId}, username=${event.username}")
        sendEmail(
            to = "${event.username}@example.com",   // in production: store real email in user-service
            subject = "Welcome to MovieApp, ${event.username}!",
            body = "Hi ${event.username},\n\nThank you for registering on MovieApp. Enjoy!"
        )
    }

    private fun sendEmail(to: String, subject: String, body: String) {
        try {
            val msg = SimpleMailMessage().apply {
                setTo(to)
                setSubject(subject)
                setText(body)
                setFrom("noreply@movieapp.com")
            }
            mailSender.send(msg)
            log.info("Email sent to $to")
        } catch (e: Exception) {
            log.warn("Failed to send email to $to: ${e.message}")
        }
    }
}
