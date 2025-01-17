package com.example.android.politicalpreparedness.data.source.remote

import com.example.android.politicalpreparedness.data.source.remote.jsonadapter.DateAdapter
import com.example.android.politicalpreparedness.data.source.remote.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.data.source.remote.models.ElectionResponse
import com.example.android.politicalpreparedness.data.source.remote.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.source.remote.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

private val moshi = Moshi.Builder()
    .add(ElectionAdapter())
    .add(DateAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(CivicsHttpClient.getClient())
    .baseUrl(BASE_URL)
    .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {

    @GET("elections")
    suspend fun getUpcomingElections(): ElectionResponse

    @GET("voterinfo")
    suspend fun getVoterInfo(
        @Query("address") address: String,
        @Query("electionId") electionId: Long
    ): VoterInfoResponse

    @GET("representatives")
    suspend fun getRepresentatives(@Query("address") address: String): RepresentativeResponse
}

object CivicsApi {
    fun createRetrofitService(): CivicsApiService = retrofit.create(CivicsApiService::class.java)
}