package com.example.newsfeed.ui.news

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsfeed.R
import com.example.newsfeed.adapter.NewsAdapter
import com.example.newsfeed.data.models.NewsResult
import com.example.newsfeed.databinding.FragmentNewsBinding
import com.google.android.material.snackbar.Snackbar
import io.reactivex.processors.PublishProcessor
import org.koin.android.ext.android.inject


class NewsFragment : Fragment(),NewsAdapter.NewsEventListener {

    val newsViewModel:NewsViewModel by inject()
    lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentNewsBinding
    private val paginator = PublishProcessor.create<Int>()
    private var loading = false
    private var pageNumber = 1
    private val VISIBLE_THRESHOLD = 1
    private var lastVisibleItem = 0
    private  var totalItemCount:Int = 0
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentNewsBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!checkInternet()){
            showConnectionLost()
        }

        setupRecyclerView()

        newsViewModel.progressBarLiveData.observe(viewLifecycleOwner){
            when(it){
                true-> {
                    binding.ProgressBar.visibility=View.VISIBLE

                }
                false->{
                    binding.ProgressBar.visibility=View.INVISIBLE

                }
            }
        }




    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        newsViewModel.newsLiveData.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                val news = it.body()?.response?.results
                binding.loadingStateTv.visibility=View.INVISIBLE
                newsAdapter.addNews(news as MutableList<NewsResult>)
                newsAdapter.notifyDataSetChanged()
                loading = false
                binding.ProgressBar.visibility = View.GONE
            }else{
                showNetworkProblem()
            }

        }
    }

    private fun showConnectionLost(){
        binding.loadingStateTv.visibility=View.INVISIBLE
        val snackBar = Snackbar
            .make(
                binding.root,
                resources.getString(R.string.please_connect_the_network),
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(resources.getString(R.string.try_again)) {
                requireActivity().finish()
                requireActivity().startActivity(activity?.intent); }
        snackBar.show()
    }

    private fun showNetworkProblem(){
        val snackBar = Snackbar
            .make(
                binding.root,
                resources.getString(R.string.server_problem),
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(resources.getString(R.string.try_again)) {
                newsViewModel.getNews(pageNumber)
            }
        snackBar.show()
    }



    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        binding.rvNews.adapter=newsAdapter
        layoutManager= LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
        binding.rvNews.layoutManager=layoutManager
        setUpLoadMoreListener();
        newsAdapter.newsEventListener=this
    }

    /**
     * setting listener to get callback for load more
     */
    private fun setUpLoadMoreListener() {
        binding.rvNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int, dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = layoutManager.itemCount
                lastVisibleItem = layoutManager
                    .findLastVisibleItemPosition()
                if (!loading
                    && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD
                ) {
                    pageNumber++
                    paginator.onNext(pageNumber)
                    newsViewModel.getNews(pageNumber)
                    loading = true
                }
            }
        })
    }





    override fun addToFavorites(result: NewsResult) {
        newsViewModel.addNewsFavorites(result)
    }



    override fun onNewsClicked(result: NewsResult) {
        openUrlInCustomTab(requireContext(),result.webUrl)
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
            //            customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            if (context != null) {
                customTabsIntent.launchUrl(context, uri)
            }
        } catch (e: Exception) {
        }
    }

    private fun checkInternet(): Boolean {
        val wifiConnected: Boolean
        val mobileConnected: Boolean
        var returns = false
        val connMgr = requireContext().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeInfo = connMgr.activeNetworkInfo
        if (activeInfo != null && activeInfo.isConnected) {
            wifiConnected = activeInfo.type == ConnectivityManager.TYPE_WIFI
            mobileConnected = activeInfo.type == ConnectivityManager.TYPE_MOBILE
            if (wifiConnected) {
                //Connected with Wifi
            } else if (mobileConnected) {
                //Connected with Mobile data
            }
            returns = true
        } else {

            returns = false
        }
        return returns
    }
}