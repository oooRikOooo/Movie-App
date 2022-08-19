package com.example.filmshelper.presentation.screens.mainFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmshelper.appComponent
import com.example.filmshelper.databinding.FragmentNowShowingFilmsBinding
import com.example.filmshelper.presentation.adapters.AdapterDelegatesHome
import com.example.filmshelper.utils.ViewStateWithList
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import javax.inject.Inject

class NowShowingFilmsFragment : Fragment() {

    private lateinit var binding: FragmentNowShowingFilmsBinding

    private val viewModel: MainFragmentViewModel by viewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: MainFragmentViewModelFactory.Factory

    private val nowShowingAdapter = ListDelegationAdapter(
        AdapterDelegatesHome().nowShowingShowingFullAdapterDelegate()
    )

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNowShowingFilmsBinding.inflate(layoutInflater, container, false)

        viewModel.getNowShowingFilmsFromServer()

        setupAdapters()

        gedDataForAdapter()

        return binding.root
    }

    private fun setupAdapters() {
        binding.recyclerViewNowShowingFull.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = nowShowingAdapter
        }
    }

    private fun gedDataForAdapter() {
        viewModel.listNowShowingFilmsFull.observe(viewLifecycleOwner) {
            when (it) {
                is ViewStateWithList.Error -> {
                    Log.d("riko", "error")
                }
                ViewStateWithList.Loading -> {

                }
                ViewStateWithList.NoData -> {
                    Log.d("riko", "no data")
                }
                is ViewStateWithList.Success -> {
                    nowShowingAdapter.items = it.data
                    nowShowingAdapter.notifyDataSetChanged()
                    Log.d("riko", it.data.size.toString())
                }
            }
        }
    }

}