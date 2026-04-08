package cz.cvut.bekalim.app.data.review

data class ReviewCreateRequest(
    val userId: Long,
    val movieId: Long,
    val text:   String
)