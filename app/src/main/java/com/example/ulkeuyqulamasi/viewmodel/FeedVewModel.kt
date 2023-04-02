package com.example.ulkeuyqulamasi.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.ulkeuyqulamasi.model.Country
import com.example.ulkeuyqulamasi.service.CountryAPIService
import com.example.ulkeuyqulamasi.service.CountryDatabase
import com.example.ulkeuyqulamasi.util.CustomSharedPreferences
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch


class FeedVewModel(application: Application):BaseViewModel(application) {
    private var customPreferences=CustomSharedPreferences(getApplication())
    private val countryAPIService=CountryAPIService()
    private val disposable=CompositeDisposable()
    private var refreshTime=10*60*1000*1000*1000L

    val countries=MutableLiveData<List<Country>>()
    val countryError=MutableLiveData<Boolean>()
    val countryLoading=MutableLiveData<Boolean>()

    fun refreshData(){

        val updateTime=customPreferences.getTime()
        if (updateTime!=null&&updateTime!=0L && System.nanoTime()-updateTime<refreshTime){
            getDataFromSQLite()
        }else {
            getDataFromAPI()
        }
      /*  val country=Country("Turkey","Asia","Ankara","lira","Turkish","www.ss.com")
        val country2=Country("France","Europe","Paris","EUR","French","www.ss.com")
        val country3=Country("Germany","Europe","Berlin","EUR","German","www.ss.com")
        val countryList= arrayListOf<Country>(country,country2,country3)
        countries.value=countryList
        countryError.value=false
        countryLoading.value=false
*/
    }
    fun refreshFromAPI(){
        getDataFromAPI()
    }
    private fun getDataFromSQLite(){
        countryLoading.value=true
        launch {
            val countries=CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(),"Countries From SQLite",Toast.LENGTH_SHORT).show()

        }
    }
    private fun getDataFromAPI(){
        countryLoading.value=true
        disposable.add(
            countryAPIService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Countries From SQLite",Toast.LENGTH_SHORT).show()


                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value=false
                        countryError.value=true
                        e.printStackTrace()
                    }

                })
        )

    }
    private fun showCountries(countryList: List<Country>) {
        countries.value=countryList
        countryError.value=false
        countryLoading.value=false
    }
    private fun storeInSQLite(list: List<Country>) {
        launch {
           val dao=CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
          val listLong=  dao.insertAll(*list.toTypedArray())//->list->individual
            var i=0
            while (i<list.size){
                list[i].uuid=listLong[i].toInt()
                i=i+1
            }
            showCountries(list)
        }
        customPreferences.saveTime(System.nanoTime())

    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }
}