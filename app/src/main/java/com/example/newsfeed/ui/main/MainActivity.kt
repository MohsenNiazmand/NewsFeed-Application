package com.example.newsfeed.ui.main

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.newsfeed.adapter.SliderAdapter
import com.example.newsfeed.databinding.ActivityMainBinding
import com.example.newsfeed.ui.favorites.FavoritesFragment
import com.example.newsfeed.ui.news.NewsFragment
import com.example.newsfeed.utils.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    lateinit var sliderAdapter: SliderAdapter
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        sliderAdapter= SliderAdapter(supportFragmentManager,lifecycle)
        sliderAdapter.addFragment(NewsFragment())
        sliderAdapter.addFragment(FavoritesFragment())
        binding.viewpager.adapter=sliderAdapter
        binding.viewpager.orientation=ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewpager.currentItem=0
        val zoomOutPageTransformer=ZoomOutPageTransformer()
        binding.viewpager.setPageTransformer(zoomOutPageTransformer)
        binding.viewpager.isUserInputEnabled = false;


        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0->{

                        val newsFragment=NewsFragment()
                          sliderAdapter.refreshFragment(0,newsFragment)
                    }
                    1-> {
                        val favoritesFragment=FavoritesFragment()
                           sliderAdapter.refreshFragment(1,favoritesFragment)
                    }
                }
            }


        })

        TabLayoutMediator(binding.tabLayout,binding.viewpager){ tab, position ->

           when(position){
               0->{
                   tab.text = "News"
               }
               1-> {
                   tab.text = "Favorites"
               }
           }
        }.attach()


    }



    override fun onBackPressed() {
        if (binding.viewpager.currentItem == 0) {
                    // If the user is currently looking at the first step, allow the system to handle the
                    // Back button. This calls finish() on this activity and pops the back stack.
                    finish()
                } else {
                    // Otherwise, select the previous step.
                    binding.viewpager.currentItem = binding.viewpager.currentItem - 1
                }
            }

}