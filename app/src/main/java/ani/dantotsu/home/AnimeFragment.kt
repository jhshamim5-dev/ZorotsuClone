package ani.dantotsu.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ani.dantotsu.R
import ani.dantotsu.databinding.FragmentAnimeBinding
import ani.dantotsu.media.Media
import ani.dantotsu.media.MediaAdaptor

class AnimeFragment : Fragment() {
    private var _binding: FragmentAnimeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val demo = mutableListOf(
            media("Demo Anime A", "https://bit.ly/31bsIHq"),
            media("Demo Anime B", "https://bit.ly/2ZGfcuG"),
            media("Demo Anime C", "https://bit.ly/2ZGfcuG"),
        )
        binding.animePageRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.animePageRecyclerView.adapter = MediaAdaptor(demo, showCountdown = true)
    }

    private fun media(name: String, cover: String): Media {
        return Media(title = name, coverUrl = cover, id = 0)
    }
}
