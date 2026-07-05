package ani.dantotsu

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType

object JikanService {

    private const val BASE = "https://api.jikan.moe/v4"

    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    @Serializable
    private data class JikanResponse(val data: List<JikanMedia>? = null)

    @Serializable
    private data class JikanMedia(
        val malId: Int? = null,
        val title: String? = null,
        val images: Images? = null,
        val score: Double? = null,
        val episodes: Int? = null,
        val status: String? = null,
        val genres: List<Genre>? = null,
        val synopsis: String? = null,
        val aired: Aired? = null
    )

    @Serializable
    private data class Images(val jpg: Jpg? = null)

    @Serializable
    private data class Jpg(val imageUrl: String? = null)

    @Serializable
    private data class Genre(val name: String? = null)

    @Serializable
    private data class Aired(val string: String? = null)

    suspend fun getTopManga(limit: Int = 20): List<SimpleMedia> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$BASE/top/manga?limit=$limit")
                .get()
                .addHeader("Accept", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val body = response.body?.string() ?: return@withContext emptyList()
                val result = json.decodeFromString(JikanResponse.serializer(), body)
                result.data?.map { jm ->
                    SimpleMedia(
                        id = jm.malId ?: 0,
                        title = jm.title ?: "",
                        coverUrl = jm.images?.jpg?.imageUrl,
                        meanScore = jm.score?.toInt(),
                        format = jm.status,
                        genres = jm.genres?.mapNotNull { it.name } ?: emptyList()
                    )
                } ?: emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getTopCharacters(limit: Int = 20): List<SimpleMedia> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$BASE/top/characters?limit=$limit")
                .get()
                .addHeader("Accept", "application/json")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val body = response.body?.string() ?: return@withContext emptyList()
                val result = json.decodeFromString(JikanResponse.serializer(), body)
                result.data?.map { jm ->
                    SimpleMedia(
                        id = jm.malId ?: 0,
                        title = jm.title ?: "",
                        coverUrl = jm.images?.jpg?.imageUrl
                    )
                } ?: emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Shared minimal model between AniList and Jikan responses
    @Serializable
    data class SimpleMedia(
        val id: Int,
        val title: String,
        val coverUrl: String? = null,
        val meanScore: Int? = null,
        val format: String? = null,
        val genres: List<String> = emptyList(),
        val status: String? = null
    )
}
