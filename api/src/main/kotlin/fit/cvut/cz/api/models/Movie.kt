package fit.cvut.cz.api.models

import fit.cvut.cz.api.util.GenreListConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "movies")
class Movie : BaseEntity() {
    @Column(nullable = false)
    var title: String = ""

    @Column(nullable = false)
    var year: Int? = null

    @Convert(converter = GenreListConverter::class)
    @Column(nullable = false)
    var genre: List<String> = listOf()

    @Column(nullable = true, length = 1000)
    var photoPath: String? = null

    @ManyToMany(mappedBy = "movies")
    var users: MutableList<User> = mutableListOf()
}