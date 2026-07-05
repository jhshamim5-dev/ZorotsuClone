package ani.dantotsu.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ani.dantotsu.R
import ani.dantotsu.anilistService
import ani.dantotsu.databinding.FragmentHomeBinding
import ani.dantotsu.media.Media
import kotlinx.coroutines.launch
import android.view.animation.AnimationUtils
import android.widget.Toast

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
        // Restore SwipeRefresh
        binding.homeRefresh.setOnRefreshListener {
            loadAllSections()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            loadAllSections()
        }
    }

    private fun setupHorizontal(rv: RecyclerView, items: List<Media>) {
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = MediaAdaptor(items)
        rv.visibility = View.VISIBLE
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        rv.startAnimation(anim)
    }

    private fun loadAllSections() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val trending = anilistService.getTrending()
                val popular = anilistService.getPopular()
                val topRated = anilistService.getTopRated()
                val upcoming = anilistService.getUpcoming()
                val recommended = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    anilistService.getRecommendations()
                } else emptyList()

                setupHorizontal(binding.homeWatchingRecyclerView, trending)
                setupHorizontal(binding.homeFavAnimeRecyclerView, popular)
                setupHorizontal(binding.homePlannedAnimeRecyclerView, topRated)
                setupHorizontal(binding.homeReadingRecyclerView, upcoming)
                setupHorizontal(binding.homeRecommendedRecyclerView, recommended)

                // Show anime list section
                binding.homeAnimeList.visibility = View.VISIBLE
                binding.homeMangaList.visibility = View.GONE

                // Hide progress bars
                binding.homeUserDataProgressBar.visibility = View.GONE
                binding.homeFavAnimeProgressBar.visibility = View.GONE
                binding.homePlannedAnimeProgressBar.visibility = View.GONE
                binding.homeReadingProgressBar.visibility = View.GONE
                binding.homeRecommendedProgressBar.visibility = View.GONE
            } catch (e: Exception) {
                binding.homeRefresh.isRefreshing = false
                Toast.makeText(requireContext(), "Failed to load anime list", Toast.LENGTH_SHORT).show()
            } finally {
                binding.homeRefresh.isRefreshing = false
            }
        }
    }
}
