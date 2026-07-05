package ani.dantotsu.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ani.dantotsu.R
import ani.dantotsu.anilistService
import ani.dantotsu.databinding.FragmentAnimeBinding
import ani.dantotsu.media.Media

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
        binding.animeRefresh.setOnRefreshListener {
            loadSections()
        }

        // Genre card click
        binding.animeGenre.setOnClickListener {
            Toast.makeText(requireContext(), "Genres — coming soon", Toast.LENGTH_SHORT).show()
        }

        // Calendar card click
        binding.animeCalendar.setOnClickListener {
            Toast.makeText(requireContext(), "Release Calendar — coming soon", Toast.LENGTH_SHORT).show()
        }

        // Scroll to top button
        binding.animePageScrollTop.setOnClickListener {
            binding.animePageScrollTop.visibility = View.GONE
            binding.animePageRecyclerView.smoothScrollToPosition(0)
        }

        binding.animePageRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                binding.animePageScrollTop.visibility = if (rv.computeVerticalScrollOffset() > 500) View.VISIBLE else View.GONE
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            loadSections()
        }
    }

    private fun setupHorizontal(rv: RecyclerView, items: List<Media>) {
        rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = MediaAdaptor(items)
        rv.visibility = View.VISIBLE
    }

    private fun loadSections() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val trending = anilistService.getTrending()
                val popular = anilistService.getPopular()
                val topRated = anilistService.getTopRated()
                val upcoming = anilistService.getUpcoming()

                // Show section headers
                binding.animeRecently.visibility = View.VISIBLE
                binding.animeTopRated.visibility = View.VISIBLE
                binding.animeMovies.visibility = View.VISIBLE
                binding.animeMostFav.visibility = View.VISIBLE

                setupHorizontal(binding.animeUpdatedRecyclerView, trending)
                setupHorizontal(binding.animeTopRatedRecyclerView, topRated)
                setupHorizontal(binding.animeMoviesRecyclerView, upcoming)
                setupHorizontal(binding.animeMostFavRecyclerView, popular)

                binding.animeUpdatedProgressBar.visibility = View.GONE
                binding.animeTopRatedProgressBar.visibility = View.GONE
                binding.animeMoviesProgressBar.visibility = View.GONE
                binding.animeMostFavProgressBar.visibility = View.GONE
            } catch (e: Exception) {
                binding.animeRefresh.isRefreshing = false
            } finally {
                binding.animeRefresh.isRefreshing = false
            }
        }
    }
}
