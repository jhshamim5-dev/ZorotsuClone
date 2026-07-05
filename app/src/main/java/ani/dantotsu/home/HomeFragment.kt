package ani.dantotsu.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ani.dantotsu.R
import ani.dantotsu.databinding.FragmentHomeBinding
import ani.dantotsu.media.Media
import ani.dantotsu.media.MediaAdaptor

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.homeUserName.text = "Demo User"
        binding.homeUserEpisodesWatched.text = "128"
        binding.homeUserChaptersRead.text = "34"
        binding.homeUserAvatar.setImageResource(R.mipmap.ic_launcher)
        binding.homeNotificationCount.isVisible = false

        binding.homeUserAvatarContainer.visibility = View.VISIBLE
        binding.homeUserDataContainer.visibility = View.VISIBLE
        binding.homeAnimeList.visibility = View.VISIBLE
        binding.homeMangaList.visibility = View.VISIBLE

        val demo = mutableListOf(
            media("Demo Anime A", "https://bit.ly/31bsIHq"),
            media("Demo Anime B", "https://bit.ly/2ZGfcuG"),
            media("Demo Anime C", "https://bit.ly/31bsIHq"),
        )

        fun setupList(rv: RecyclerView) {
            rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rv.adapter = MediaAdaptor(demo, showCountdown = true)
            rv.visibility = View.VISIBLE
        }

        setupList(binding.homeWatchingRecyclerView)
        setupList(binding.homeFavAnimeRecyclerView)
        setupList(binding.homePlannedAnimeRecyclerView)
        setupList(binding.homeReadingRecyclerView)
        setupList(binding.homeFavMangaRecyclerView)
        setupList(binding.homePlannedMangaRecyclerView)
        setupList(binding.homeRecommendedRecyclerView)
    }

    private fun media(name: String, cover: String): Media {
        return Media(title = name, coverUrl = cover, id = 0)
    }
}
