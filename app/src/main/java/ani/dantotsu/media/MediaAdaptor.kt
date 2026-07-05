package ani.dantotsu.media

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ani.dantotsu.R
import ani.dantotsu.databinding.ItemMediaCompactBinding

class MediaAdaptor(
    private val mediaList: List<Media>,
    private val showCountdown: Boolean = false
) : RecyclerView.Adapter<MediaAdaptor.MediaViewHolder>() {

    inner class MediaViewHolder(val binding: ItemMediaCompactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return MediaViewHolder(ItemMediaCompactBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val media = mediaList[position]
        Glide.with(holder.itemView.context)
            .load(media.coverUrl)
            .apply(RequestOptions().placeholder(R.drawable.linear_gradient_bg))
            .into(holder.binding.itemCompactImage)
        holder.binding.itemCompactTitle.text = media.title
    }

    override fun getItemCount() = mediaList.size
}
