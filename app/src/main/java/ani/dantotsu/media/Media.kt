package ani.dantotsu.media

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val id: Int,
    val title: String,
    val coverUrl: String? = null,
    val bannerImage: String? = null,
    val format: String? = null,
    val status: String? = null,
    val episodes: Int? = null,
    val nextEpisode: Int? = null,
    val meanScore: Int? = null,
    val popularity: Int? = null,
    val genres: List<String> = emptyList(),
    val description: String? = null,
    val seasonYear: Int? = null,
    val type: String? = null
) : Parcelable
