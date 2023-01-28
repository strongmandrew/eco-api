package com.example.data

import com.example.data.database.DatabaseFactory.dbQuery
import com.example.domain.dao.NewsDao
import com.example.entity.News
import com.example.utils.Errors
import com.example.utils.ServiceResult
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class NewsDaoImpl : NewsDao {

    override suspend fun getNews(): ServiceResult<List<News>> {
        return try {
            val news = dbQuery {
                NewsTable.selectAll().toList().map { rowToNews(it) }
            }

            if (news.isNotEmpty()) ServiceResult.Success(news)
            else ServiceResult.Error(Errors.EMPTY_DATA)
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    private fun rowToNews(row: ResultRow): News {

        return News(
            title = row[NewsTable.title],
            imageUrl = row[NewsTable.imageUrl],
            dateOf = row[NewsTable.dateCreated].toString(),
            url = row[NewsTable.url]
        )
    }
}