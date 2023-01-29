package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.recycle_adapter.AsteroidsListAdapter
import com.udacity.asteroidradar.repository.Repo

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by lazy {
        val application = requireNotNull(this.activity).application

        val dataSource = AsteroidDatabase.initDatabase(application).asteroidDao
        val repo = Repo(dataSource)
        val viewModelFactory = MainViewModelFactory(repo)

        ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        binding.asteroidRecycler.adapter =
            AsteroidsListAdapter(AsteroidsListAdapter.OnClickListener {
                viewModel.navigateToAsteroidDetails(it)
            })
        binding.asteroidRecycler.invalidate()
        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.navigateToAsteroidDetailsComplete()
            }
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val application = requireNotNull(this.activity).application

        val dataSource = AsteroidDatabase.initDatabase(application).asteroidDao
        val repo = Repo(dataSource)
        when(item.itemId){
            R.id.show_all_menu -> viewModel.getAllPlanetary()
            R.id.show_rent_menu -> viewModel.getTodayAsteroids()
            R.id.show_buy_menu -> viewModel.getSavedAsteroids()
        }
        return true
    }
}
