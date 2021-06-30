package com.sullivan.signear.domain

import com.sullivan.common.core.DataState
import com.sullivan.signear.data.model.RankingInfo
import kotlinx.coroutines.flow.Flow

interface SignearRepository {
    suspend fun fetchRankInfo(): Flow<DataState<RankingInfo>>


}