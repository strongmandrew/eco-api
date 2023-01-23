package com.example.domain.usecase.news

import com.example.domain.Response
import com.example.domain.dao.NewsDao
import com.example.entity.News
import com.example.utils.ServiceResult

class GetNews(
    private val newsDao: NewsDao
) {

    suspend operator fun invoke(): Response<List<News>> {

        return when (val result = newsDao.getNews()) {
            is ServiceResult.Success -> {
                Response(
                    data = result.data
                )
            }
            is ServiceResult.Error -> {
                Response(
                    errors = result.error
                )
            }
        }
    }
}