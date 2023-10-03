package no.hiof.friluftslivcompanionapp.models.api

data class WikipediaResponse(
    val query: Query
) {
    data class Query(
        val pages: Map<String, Page>
    ) {
        data class Page(
            val extract: String,
            val thumbnail: Thumbnail?
        ) {
            data class Thumbnail(
                val source: String
            )
        }
    }
}

