package hs.dcb.roacguys.client

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@SpringBootTest
class Rest {
    companion object {
        const val TOKEN = "secret"
    }

    @Test
    fun clientTest() {

        val webClient = WebClient.builder()
                .baseUrl("https://developer-lostark.game.onstove.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, TOKEN)
                .build()

        val response = webClient.get() // http get 요청 수행
                .uri("/characters/루스피린/siblings") // 요청 Uri
                .retrieve() // 응답 받아옴
                .bodyToMono<String>() // 응답 본문은 mono 로 변환
                .block() // mono 에서 결과 값

        println(response)
    }

    @Test
    fun stringToDoubleTest() {
        val s1 = "1,100.00"

        val s1r = s1.replace(",", "")
        val d1: Double = s1r.toDouble()

        println(d1)
    }
}