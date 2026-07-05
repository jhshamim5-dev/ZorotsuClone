package ani.dantotsu

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object AnilistService {

    private const val ENDPOINT = "https://graphql.anilist.co"

    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    @Serializable
    private data class GraphqlRequest(val query: String, val variables: Map<String, String> = emptyMap())

    @Serializable
    private data class AnilistResponse(val data: Data? = null)

    @Serializable
    private data class Data(
        val Page: Page? = null,
        val Media: MediaNode? = null
    )

    @Serializable
    private data class Page(
        val media: List<MediaNode> = emptyList(),
        val mediaListCollection: MediaListCollectionWrapper? = null,
        val recommendations: RecommendationPage? = null
    )

    @Serializable
    private data class MediaNode(
        val id: Int,
        val title: Title? = null,
        val coverImage: CoverImage? = null,
        val format: String? = null,
        val status: String? = null,
        val episodes: Int? = null,
        val nextAiringEpisode: NextAiring? = null,
        val mediaListEntry: MediaListEntry? = null,
        val meanScore: Int? = null,
        val popularity: Int? = null,
        val genres: List<String> = emptyList(),
        val bannerImage: String? = null,
        val isAdult: Boolean = false,
        val type: String? = null,
        val description: String? = null,
        val seasonYear: Int? = null
    )

    @Serializable
    private data class Title(val english: String? = null, val romaji: String? = null, val userPreferred: String? = null)

    @Serializable
    private data class CoverImage(val large: String? = null, val extraLarge: String? = null)

    @Serializable
    private data class MediaListCollectionWrapper(val lists: List<MediaList> = emptyList())

    @Serializable
    private data class MediaList(val entries: List<MediaEntry> = emptyList())

    @Serializable
    private data class MediaEntry(val media: MediaNode? = null, val progress: Int = 0, val status: String? = null, val score: Int? = null, val private: Boolean = false)

    @Serializable
    private data class RecommendationPage(val recommendations: List<RecommendationEdge> = emptyList())

    @Serializable
    private data class RecommendationEdge(val mediaRecommendation: MediaNode? = null)

    @Serializable
    private data class NextAiring(val episode: Int? = null, val airingAt: Int? = null, val timeUntilAiring: Int? = null)

    @Serializable
    private data class MediaListEntry(val progress: Int = 0, val score: Int? = null, val status: String? = null, val private: Boolean = false)

    suspend fun getTrending(): List<MediaNode> = withContext(Dispatchers.IO) {
        val query = """
            query {
              Page(page: 1, perPage: 20) {
                media(type: ANIME, sort: TRENDING_DESC) {
                  id
                  title { english romaji userPreferred }
                  coverImage { extraLarge large }
                  format
                  status
                  episodes
                  nextAiringEpisode { episode airingAt timeUntilAiring }
                  meanScore
                  popularity
                  genres
                  bannerImage
                  isAdult
                  type
                  description
                  mediaListEntry { progress score status private }
                  seasonYear
                }
              }
            }
        """.trimIndent()
        fetch(query)
            ?.Page
            ?.media
            ?.filter { !it.isAdult }
            ?: emptyList()
    }

    suspend fun getPopular(): List<MediaNode> = withContext(Dispatchers.IO) {
        val query = """
            query {
              Page(page: 1, perPage: 20) {
                media(type: ANIME, sort: POPULARITY_DESC) {
                  id
                  title { english romaji userPreferred }
                  coverImage { extraLarge large }
                  format
                  status
                  episodes
                  nextAiringEpisode { episode airingAt timeUntilAiring }
                  meanScore
                  popularity
                  genres
                  bannerImage
                  isAdult
                  type
                  description
                  mediaListEntry { progress score status private }
                  seasonYear
                }
              }
            }
        """.trimIndent()
        fetch(query)
            ?.Page
            ?.media
            ?.filter { !it.isAdult }
            ?: emptyList()
    }

    suspend fun getTopRated(): List<MediaNode> = withContext(Dispatchers.IO) {
        val query = """
            query {
              Page(page: 1, perPage: 20) {
                media(type: ANIME, sort: SCORE_DESC) {
                  id
                  title { english romaji userPreferred }
                  coverImage { extraLarge large }
                  format
                  status
                  episodes
                  nextAiringEpisode { episode airingAt timeUntilAiring }
                  meanScore
                  popularity
                  genres
                  bannerImage
                  isAdult
                  type
                  description
                  mediaListEntry { progress score status private }
                  seasonYear
                }
              }
            }
        """.trimIndent()
        fetch(query)
            ?.Page
            ?.media
            ?.filter { !it.isAdult }
            ?: emptyList()
    }

    suspend fun getUpcoming(): List<MediaNode> = withContext(Dispatchers.IO) {
        val query = """
            query {
              Page(page: 1, perPage: 20) {
                media(type: ANIME, sort: NEXT_AIRING, status: NOT_YET_RELEASED) {
                  id
                  title { english romaji userPreferred }
                  coverImage { extraLarge large }
                  format
                  status
                  episodes
                  nextAiringEpisode { episode airingAt timeUntilAiring }
                  meanScore
                  popularity
                  genres
                  bannerImage
                  isAdult
                  type
                  description
                  mediaListEntry { progress score status private }
                  seasonYear
                }
              }
            }
        """.trimIndent()
        fetch(query)
            ?.Page
            ?.media
            ?.filter { !it.isAdult }
            ?: emptyList()
    }

    suspend fun getRecommendations(): List<MediaNode> = withContext(Dispatchers.IO) {
        val query = """
            query {
              Page(page: 1, perPage: 20) {
                recommendations(sort: RATING_DESC) {
                  mediaRecommendation {
                    id
                    title { english romaji userPreferred }
                    coverImage { extraLarge large }
                    format
                    status
                    episodes
                    nextAiringEpisode { episode airingAt timeUntilAiring }
                    meanScore
                    popularity
                    genres
                    bannerImage
                    isAdult
                    type
                    description
                    mediaListEntry { progress score status private }
                    seasonYear
                  }
                }
              }
            }
        """.trimIndent()
        fetch(query)
            ?.Page
            ?.recommendations
            ?.mapNotNull { it.mediaRecommendation }
            ?.filter { !it.isAdult }
            ?: emptyList()
    }

    private suspend fun fetch(query: String): AnilistResponse? = withContext(Dispatchers.IO) {
        try {
            val body = GraphqlRequest(query = query).let { json.encodeToString(it) }
                .toRequestBody(jsonMediaType.toMediaType())

            val request = Request.Builder()
                .url(ENDPOINT)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext null
                val responseBody = response.body?.string() ?: return@withContext null
                json.decodeFromString(AnilistResponse.serializer(), responseBody).data
            }
        } catch (e: Exception) {
            null
        }
    }
}
