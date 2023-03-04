package hs.dcb.roacguys.webClient.roa

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class RoaClient {

    @Value("\${roa-api.token}")
    private lateinit var token: String

    fun getClient(): WebClient {

        return WebClient.builder()
                .baseUrl("https://developer-lostark.game.onstove.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, token)
                .build()
    }
}