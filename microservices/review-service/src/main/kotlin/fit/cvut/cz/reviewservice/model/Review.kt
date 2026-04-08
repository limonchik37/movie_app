package fit.cvut.cz.reviewservice.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.Date

@Entity
@Table(name = "reviews")
@EntityListeners(AuditingEntityListener::class)
class Review(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    // userId and movieId are stored as plain foreign keys (no JPA relations — each service owns its own DB)
    @Column(name = "user_id", nullable = false)
    var userId: Long = 0,

    @Column(name = "movie_id", nullable = false)
    var movieId: Long = 0,

    @Column(nullable = false, length = 2000)
    var text: String = "",

    @Column(nullable = true, length = 1000)
    var photoPath: String? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    var createdAt: Date? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    @LastModifiedDate
    var updatedAt: Date? = null
)
