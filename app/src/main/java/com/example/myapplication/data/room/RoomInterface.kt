package com.example.myapplication.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.response.BiasedArticles
import com.example.myapplication.response.NewsArticle

@Dao
interface NewsArticleDao {

    @Query("SELECT * FROM news_articles")
    suspend fun getAll(): List<NewsArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<NewsArticle>)

    @Delete
    suspend fun delete(article: NewsArticle)

    @Query("DELETE FROM news_articles")
    suspend fun clearAll()

    @Query("""
        SELECT * FROM news_articles n 
        WHERE LOWER(n.headline) LIKE '%' || :query || '%'
           OR LOWER(n.description) LIKE '%' || :query || '%'
           OR LOWER(n.article) LIKE '%' || :query || '%'
        ORDER BY 
            CASE 
                WHEN LOWER(n.headline) LIKE '%' || :query || '%' THEN 0 
                ELSE 1 
            END ASC,
            n.date DESC
    """)
    suspend fun searchDatabase(query: String): List<NewsArticle>

    @Query("SELECT * FROM news_articles n WHERE LOWER(n.category) LIKE '%' || :query || '%'")
    suspend fun searchCategory(query: String): List<NewsArticle>

    @Query("SELECT * FROM news_articles WHERE DATE(date) = :targetDate")
    suspend fun searchToday(targetDate: String): List<NewsArticle>

    @Query("SELECT * FROM news_articles WHERE id= :id")
    suspend fun fetchArticle(id: Int): NewsArticle?



}
@Dao
interface BiasedArticleDao {

    @Query("SELECT * FROM biased_articles")
    suspend fun getAll(): List<BiasedArticles>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<BiasedArticles>)

    @Delete
    suspend fun delete(article: BiasedArticles)

    @Query("DELETE FROM biased_articles")
    suspend fun clearAll()

    @Query("SELECT * FROM biased_articles n WHERE n.articleId=:id ")
    suspend fun getBiasedArticles(id: Int): List<BiasedArticles>
}
@Dao
interface SavedArticleDao {

    @Query("SELECT * FROM saved_articles")
    suspend fun getAll(): List<NewsArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: NewsArticle)

    @Delete
    suspend fun delete(article: NewsArticle)

    @Query("DELETE FROM saved_articles")
    suspend fun clearAll()
}

