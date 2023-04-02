package com.example.ulkeuyqulamasi.service

import com.example.ulkeuyqulamasi.model.Country
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface CountryAPI {
@GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")
fun getCountries():Single<List<Country>>

}