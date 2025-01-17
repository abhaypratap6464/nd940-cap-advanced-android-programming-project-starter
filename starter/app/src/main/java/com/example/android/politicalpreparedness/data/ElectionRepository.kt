package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.source.local.ElectionDao
import com.example.android.politicalpreparedness.data.source.remote.CivicsApiService
import com.example.android.politicalpreparedness.data.source.remote.models.ElectionResponse
import com.example.android.politicalpreparedness.data.source.remote.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.source.remote.models.asVoterInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ElectionRepository(
    private val service: CivicsApiService,
    private val dao: ElectionDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionDataSource {

    override suspend fun getUpcomingElections(): Result<ElectionResponse> =
        withContext(dispatcher) {
            return@withContext try {
                Result.Success(service.getUpcomingElections())
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }

    override fun getSavedElections(): LiveData<List<Election>> = dao.getSavedElections()

    override suspend fun getElection(id: Long): Election? = withContext(dispatcher) {
        dao.get(id)
    }

    override suspend fun insert(election: Election) {
        withContext(dispatcher) {
            dao.insert(election)
        }
    }

    override suspend fun delete(election: Election) {
        withContext(dispatcher) {
            dao.delete(election)
        }
    }

    override suspend fun getVoterInfo(address: String, electionId: Long): Result<VoterInfoDTO> {
        return try {
            Result.Success(service.getVoterInfo(address, electionId).asVoterInfo())
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun getRepresentatives(address: String): Result<RepresentativeResponse> =
        withContext(dispatcher) {
            return@withContext try {
                Result.Success(service.getRepresentatives(address))
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }


}