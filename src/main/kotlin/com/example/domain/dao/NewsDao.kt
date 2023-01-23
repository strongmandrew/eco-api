package com.example.domain.dao

import com.example.entity.News
import com.example.utils.ServiceResult

interface NewsDao {
    suspend fun getNews(): ServiceResult<List<News>>
}