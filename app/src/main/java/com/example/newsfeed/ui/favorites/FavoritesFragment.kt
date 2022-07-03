package com.example.newsfeed.ui.favorites

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsfeed.adapter.FavoritesAdapter
import com.example.newsfeed.data.models.NewsResult
import com.example.newsfeed.databinding.FragmentFavoritesBinding
import org.koin.android.ext.android.inject

class FavoritesFragment : Fragment(),FavoritesAdapter.OnFavoritesClickEvent {
    val favoritesViewModel:FavoritesViewModel by inject()
    private lateinit var binding:FragmentFavoritesBinding
    val newsList=ArrayList<NewsResult>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentFavoritesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFavorites.layoutManager= LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)

        favoritesViewModel.progressBarLiveData.observe(viewLifecycleOwner){
            when(it){
                true-> binding.ProgressBarFavorites.visibility=View.VISIBLE
                false-> binding.ProgressBarFavorites.visibility=View.GONE
            }
        }

        favoritesViewModel.favoritesLiveData.observe(viewLifecycleOwner){
          if (it.size>0){
              newsList += it
              binding.emptyStateTv.visibility=View.INVISIBLE
              binding.rvFavorites.adapter=FavoritesAdapter(it as MutableList<NewsResult>).also {
                  favoritesAdapter -> favoritesAdapter.onFavoritesClickEvent=this
              }

          }else{
              binding.emptyStateTv.visibility=View.VISIBLE
          }
        }

    }




    override fun removeFromFavorites(result: NewsResult) {
        favoritesViewModel.removeFromFavorites(result)
        if (binding.rvFavorites.adapter?.itemCount==1)
            binding.emptyStateTv.visibility=View.VISIBLE
    }

    override fun onFavoritesClicked(result: NewsResult) {
        openUrlInCustomTab(requireContext(), result.webUrl)

    }

    private fun openUrlInCustomTab(context: Context?, url: String?) {

        try {
            val uri = Uri.parse(url)
            val intentBuilder = CustomTabsIntent.Builder()
            if (context != null) {
                intentBuilder.setStartAnimations(
                    context,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
            }
            if (context != null) {
                intentBuilder.setExitAnimations(
                    context,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
            }
            val customTabsIntent = intentBuilder.build()
            if (context != null) {
                customTabsIntent.launchUrl(context, uri)
            }
        } catch (e: Exception) {
        }
    }
}