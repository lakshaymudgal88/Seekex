package com.example.seekexinternet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.example.seekexinternet.adapters.SpeedAdapter
import com.example.seekexinternet.services.InternetService
import com.example.seekexinternet.viewmodels.SeekexVIewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var seekexVIewModels: SeekexVIewModels
    private lateinit var speedAdapter: SpeedAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("Golu")
        seekexVIewModels = ViewModelProvider(this)[SeekexVIewModels::class.java]

        setUpRecyclerView()
        setUpClicks()

        InternetService.currInternetVal.observe(this) {
            tv_internet_speed.text = it.toString()
        }

        seekexVIewModels.getSpeedList().observe(this) { speedList ->
            speedAdapter.speedList.submitList(speedList)
        }

        speedAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (positionStart == 0 && positionStart == linearLayoutManager
                        .findFirstCompletelyVisibleItemPosition()
                ) {
                    linearLayoutManager.scrollToPosition(0)
                }
            }
        })
    }

    private fun setUpClicks() {
        start_service.setOnClickListener {
            startCommandToService()
        }

        stop_service.setOnClickListener {
            stopCommandToService()
        }
    }

    private fun setUpRecyclerView() {
        speedAdapter = SpeedAdapter()
        linearLayoutManager = LinearLayoutManager(this@MainActivity)
        recycler_view.apply {
            layoutManager = linearLayoutManager
            adapter = speedAdapter
        }
    }

    private fun startCommandToService() =
        Intent(this, InternetService::class.java).also {
            startService(it)
        }

    private fun stopCommandToService() =
        Intent(this, InternetService::class.java).also {
            stopService(it)
        }

}