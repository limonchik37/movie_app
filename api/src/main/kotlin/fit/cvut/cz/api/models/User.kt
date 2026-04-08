package fit.cvut.cz.api.models

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "user_account")
class User : BaseEntity() {
    @Column(nullable = false)
    var username: String? = null

    @Column(nullable = false)
    var password: String? = null

    @Column(nullable = true, length = 1000)
    var profilePhoto: String? = null

    @ManyToMany
    @JoinTable(
        name = "user_movie",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "movie_id")]
    )
    var movies: MutableList<Movie> = mutableListOf()

    @Column(name = "role")
    var role: Role? = null
}