package com.example.ulkeuyqulamasi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.ulkeuyqulamasi.R
import com.example.ulkeuyqulamasi.databinding.FragmentCountryBinding
import com.example.ulkeuyqulamasi.util.downloadFromUrl
import com.example.ulkeuyqulamasi.util.placeholderProgressBar
import com.example.ulkeuyqulamasi.viewmodel.CountryViewModel


@Suppress("DEPRECATION")
class CountryFragment : Fragment() {
//    private lateinit var viewModel: CountryViewModel
    private val viewModel by activityViewModels<CountryViewModel>()
    private lateinit var dataBinding: FragmentCountryBinding


    private var countryUuid=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding= FragmentCountryBinding.inflate(inflater, container, false)
        dataBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_country,container,false)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel= androidx.lifecycle.ViewModelProviders.of(this)[CountryViewModel::class.java]
        arguments?.let {
            countryUuid= CountryFragmentArgs.fromBundle(it).countryUuid

        }
        viewModel.getDataFromRoom(countryUuid)





        observeLiveData()
    }
   private fun observeLiveData(){
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country->
            country?.let {
                dataBinding.selectedCountry=country
                /*
                binding.countryName.text=country.countryName
                binding.countryCapital.text=country.countryCapital
                binding.countryCurrency.text=country.countryCurrency
                binding.countryLanguage.text=country.countryLanguage
                binding.countryRegion.text=country.countryRegion
                context?.let {
                    binding.countryImage.downloadFromUrl(country.imageUrl, placeholderProgressBar(it))

                }

                 */
            }
        })
    }

}