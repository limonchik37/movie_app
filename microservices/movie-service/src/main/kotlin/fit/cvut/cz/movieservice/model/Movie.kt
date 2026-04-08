package fit.cvut.cz.movieservice.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.Date

@Entity
@Table(name = "movies")
@EntityListeners(AuditingEntityListener::class)
class Movie(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var title: String = "",

    @Column(nullable = false)
    var year: Int = 0,

    // Stored as comma-separated string
    @Column(nullable = false, length = 500)
    var genre: String = "",

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
