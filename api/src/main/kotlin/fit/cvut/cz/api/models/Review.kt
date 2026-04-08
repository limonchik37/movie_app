package fit.cvut.cz.api.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "reviews")
class Review : BaseEntity() {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    var movie: Movie? = null

    @Column(nullable = false)
    var text: String? = null

    @Column(nullable = true)
    var photoPath: String? = null
}