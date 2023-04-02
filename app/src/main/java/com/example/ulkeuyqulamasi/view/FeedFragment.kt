package com.example.ulkeuyqulamasi.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ulkeuyqulamasi.R
import com.example.ulkeuyqulamasi.adapter.CountryAdapter
import com.example.ulkeuyqulamasi.databinding.FragmentCountryBinding
import com.example.ulkeuyqulamasi.databinding.FragmentFeedBinding
import com.example.ulkeuyqulamasi.viewmodel.FeedVewModel

@Suppress("DEPRECATION")
class FeedFragment : Fragment() {
//    private lateinit var viewModel:FeedVewModel
private val viewModel by activityViewModels<FeedVewModel>()

    private val countryAdapter=CountryAdapter(arrayListOf())

    private lateinit var binding:FragmentFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding= FragmentFeedBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel= androidx.lifecycle.ViewModelProviders.of(this).get(FeedVewModel::class.java)
        viewModel.refreshData()

        binding.countryList.layoutManager=LinearLayoutManager(context)
        binding.countryList.adapter=countryAdapter

        binding.swiperefreshlayout.setOnRefreshListener{
            binding.countryList.visibility=View.GONE
            binding.countryERROR.visibility=View.GONE
            binding.countryloading.visibility=View.VISIBLE
            viewModel.refreshData()
            viewModel.refreshFromAPI()
            binding.swiperefreshlayout.isRefreshing=false
        }

        observerLiveData()

    }
  private  fun observerLiveData(){
        viewModel.countries.observe(viewLifecycleOwner, Observer { counties->
            counties?.let {
                binding.countryList.visibility=View.VISIBLE
                countryAdapter.updateCountryList(counties)
            }

        })
        viewModel.countryError.observe(viewLifecycleOwner, Observer { error->
            error?.let {
                if (it){
                    binding.countryERROR.visibility=View.VISIBLE
                } else{
                    binding.countryERROR.visibility=View.GONE
                }
            }
        })
        viewModel.countryLoading.observe(viewLifecycleOwner, Observer { loadin->
            loadin?.let {
                if (it){
                    binding.countryloading.visibility=View.VISIBLE
                    binding.countryList.visibility=View.GONE
                    binding.countryERROR.visibility=View.GONE
                }
                else{
                    binding.countryloading.visibility=View.GONE
                }
            }

        })
    }

}