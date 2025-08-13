package com.example.vendorflow

import com.example.vendorflow.data.notion.NotionApi
import com.example.vendorflow.data.notion.NotionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val NOTION_TOKEN = NotionToken.NOTION_TOKEN
    @Singleton
    @Provides
    fun provideClient(): HttpClient {
        val client = HttpClient(engineFactory = Android) {
            install(plugin = Logging) {
                level = LogLevel.ALL
            }
            install(plugin = ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
            install(plugin = Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(
                            accessToken = NOTION_TOKEN,
                            refreshToken = null
                        )
                    }
                }
            }
            install(plugin = DefaultRequest) {
                header("Notion-Version", "2022-06-28")
            }
        }
        return client
    }
    @Singleton
    @Provides
    fun provideApi(
        client: HttpClient
    ) = NotionApi(client = client)

    @Singleton
    @Provides
    fun provideApiRepository(
        api: NotionApi
    ) = NotionRepository(notionApi = api)
}