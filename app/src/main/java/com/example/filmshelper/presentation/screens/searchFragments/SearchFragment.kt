package com.example.filmshelper.presentation.screens.searchFragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmshelper.R
import com.example.filmshelper.appComponent
import com.example.filmshelper.data.models.DisplayableItem
import com.example.filmshelper.databinding.BottomSheetFilterBinding
import com.example.filmshelper.databinding.FragmentSearchBinding
import com.example.filmshelper.presentation.adapters.AdapterDelegateSearch
import com.example.filmshelper.utils.DebouncingQueryTextListener
import com.example.filmshelper.utils.ViewStateWithList
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import javax.inject.Inject


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModels {
        factory.create()
    }

    @Inject
    lateinit var factory: SearchViewModelFactory.Factory


    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        viewModel.categories.observe(viewLifecycleOwner) {
            Log.d("riko", it.toString())
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        val filmsWithQueryDelegate = ListDelegationAdapter(
            AdapterDelegateSearch().filmsWithQueryAdapterDelegate(itemDetailFragmentContainer)
        )

        binding.imageViewFilter.setOnClickListener {
            showBottomSheetDialog(filmsWithQueryDelegate)
        }

        setupAdapters(filmsWithQueryDelegate)
        getDataForAdapter(filmsWithQueryDelegate)
        setDataForAdapter(filmsWithQueryDelegate)
        searchViewListeners(filmsWithQueryDelegate)
    }

    private val categoriesListener = View.OnClickListener { view ->
        val button = view as Button
        val value = button.tag.toString()
        if (viewModel.checkIfContainValue(
                value,
                viewModel.categories
            )
        ) {
            viewModel.removeCategory(value)
            button.setBackgroundResource(R.drawable.button_sort_filter_not_active)
            button.setTextColor(Color.rgb(226, 18, 33))
        } else {
            viewModel.addCategory(value)
            button.setBackgroundResource(R.drawable.button_sort_filter_active)
            button.setTextColor(Color.WHITE)

        }

    }

    private fun searchViewListeners(filmsWithQueryDelegate: ListDelegationAdapter<List<DisplayableItem>>) {
        binding.searchView.setOnQueryTextListener(
            DebouncingQueryTextListener(
                this.lifecycle
            ) { newText ->
                newText.let {
                    if (it?.isEmpty() == true) {
                        viewModel.getFilmsWithQuery("")
                        filmsWithQueryDelegate.notifyDataSetChanged()
                    } else {
                        viewModel.getFilmsWithQuery(it.toString())
                        filmsWithQueryDelegate.notifyDataSetChanged()
                    }
                }
            }
        )
        binding.searchView.setOnCloseListener {
            binding.searchView.clearFocus()
            false
        }


    }

    private fun setupAdapters(filmsWithQueryDelegate: ListDelegationAdapter<List<DisplayableItem>>) {
        binding.recyclerViewFilms.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = filmsWithQueryDelegate
        }
    }

    private fun getDataForAdapter(filmsWithQueryDelegate: ListDelegationAdapter<List<DisplayableItem>>) {
        if (viewModel.genres.value?.isEmpty() == true && viewModel.categories.value?.size == 1) {
            viewModel.getFilms()
        } else {
            viewModel.getFilmsWithQuery(binding.searchView.query.toString())
        }
    }

    private fun setDataForAdapter(filmsWithQueryDelegate: ListDelegationAdapter<List<DisplayableItem>>) {
        viewModel.films.observe(viewLifecycleOwner) {
            when (it) {
                is ViewStateWithList.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
                ViewStateWithList.Loading -> {
                    binding.recyclerViewFilms.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                ViewStateWithList.NoData -> {
                    Toast.makeText(requireContext(), "NoData", Toast.LENGTH_SHORT).show()
                }
                is ViewStateWithList.Success -> {
                    filmsWithQueryDelegate.items = it.data
                    filmsWithQueryDelegate.notifyDataSetChanged()
                    binding.searchView.clearFocus()
                    binding.recyclerViewFilms.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }


    private fun getArrayOfCategoriesButtons(binding: BottomSheetFilterBinding) = arrayListOf(
        binding.buttonFeatureFilms, binding.buttonTvMovie, binding.buttonTvSeries
    )

    private fun getArrayOfGenresButtons(binding: BottomSheetFilterBinding) = arrayListOf(
        binding.buttonAction,
        binding.buttonAdventure,
        binding.buttonComedy,
        binding.buttonDocumentary,
        binding.buttonDrama,
        binding.buttonFantasy,
        binding.buttonHistory,
        binding.buttonHorror,
        binding.buttonRomance,
        binding.buttonSport,
        binding.buttonThriller,
        binding.buttonWestern
    )

    private val genresListener = View.OnClickListener { view ->
        val button = view as Button
        val value = button.tag.toString()

        if (viewModel.checkIfContainValue(
                value, viewModel.genres
            )
        ) {
            viewModel.removeGenre(value)
            button.setBackgroundResource(R.drawable.button_sort_filter_not_active)
            button.setTextColor(Color.rgb(226, 18, 33))
        } else {
            viewModel.addGenre(value)
            button.setBackgroundResource(R.drawable.button_sort_filter_active)
            button.setTextColor(Color.WHITE)

        }

    }

    private fun showBottomSheetDialog(filmsWithQueryDelegate: ListDelegationAdapter<List<DisplayableItem>>) {
        val bottomSheet = BottomSheetDialog(requireContext())

        val bindingSheet = BottomSheetFilterBinding.inflate(
            layoutInflater,
            null, false
        )

        bottomSheet.setContentView(bindingSheet.root)

        setInitialState(
            getArrayOfCategoriesButtons(bindingSheet),
            getArrayOfGenresButtons(bindingSheet)
        )
        setOnClickListenersBottomSheet(bindingSheet, bottomSheet, filmsWithQueryDelegate)

        bottomSheet.show()
    }

    private fun setOnClickListenersBottomSheet(
        bindingSheet: BottomSheetFilterBinding,
        bottomSheet: BottomSheetDialog,
        filmsWithQueryDelegate: ListDelegationAdapter<List<DisplayableItem>>
    ) {
        bindingSheet.apply {

            buttonFeatureFilms.setOnClickListener(categoriesListener)
            buttonTvMovie.setOnClickListener(categoriesListener)
            buttonTvSeries.setOnClickListener(categoriesListener)
            buttonAction.setOnClickListener(genresListener)
            buttonAdventure.setOnClickListener(genresListener)
            buttonComedy.setOnClickListener(genresListener)
            buttonDocumentary.setOnClickListener(genresListener)
            buttonDrama.setOnClickListener(genresListener)
            buttonFantasy.setOnClickListener(genresListener)
            buttonHistory.setOnClickListener(genresListener)
            buttonHorror.setOnClickListener(genresListener)
            buttonRomance.setOnClickListener(genresListener)
            buttonSport.setOnClickListener(genresListener)
            buttonThriller.setOnClickListener(genresListener)
            buttonWestern.setOnClickListener(genresListener)

            buttonApply.setOnClickListener {
                if (viewModel.categories.value?.isEmpty() == true && viewModel.genres.value?.isEmpty() == true) {
                    Toast.makeText(
                        requireContext(),
                        "Choose at least one filter",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.getFilmsWithQuery(binding.searchView.query.toString())
                    filmsWithQueryDelegate.notifyDataSetChanged()
                    bottomSheet.dismiss()
                    binding.searchView.clearFocus()
                }

            }

            buttonReset.setOnClickListener {

                viewModel.categories.value?.clear()
                viewModel.genres.value?.clear()
                binding.searchView.setQuery("", false)
                binding.searchView.clearFocus()
                viewModel.categories.value?.add("feature")
                viewModel.getFilms()
                filmsWithQueryDelegate.notifyDataSetChanged()

                getArrayOfCategoriesButtons(bindingSheet).forEach { button ->
                    if (button.tag == "feature") {
                        button.setBackgroundResource(R.drawable.button_sort_filter_active)
                        button.setTextColor(Color.WHITE)
                    } else {
                        button.setBackgroundResource(R.drawable.button_sort_filter_not_active)
                        button.setTextColor(Color.rgb(226, 18, 33))
                    }

                }

                getArrayOfGenresButtons(bindingSheet).forEach { button ->
                    button.setBackgroundResource(R.drawable.button_sort_filter_not_active)
                    button.setTextColor(Color.rgb(226, 18, 33))
                }
            }
        }
    }

    private fun setInitialState(
        arrayOfCategoriesButtons: ArrayList<AppCompatButton>,
        arrayOfGenres: ArrayList<AppCompatButton>
    ) {
        viewModel.categories.value?.forEach { value ->
            arrayOfCategoriesButtons.forEach { button ->
                if (value == button.tag.toString()) {
                    button.setBackgroundResource(R.drawable.button_sort_filter_active)
                    button.setTextColor(Color.WHITE)
                }
            }
        }

        viewModel.genres.value?.forEach { value ->
            arrayOfGenres.forEach { button ->
                if (value == button.tag.toString()) {
                    button.setBackgroundResource(R.drawable.button_sort_filter_active)
                    button.setTextColor(Color.WHITE)
                }
            }
        }
    }

}