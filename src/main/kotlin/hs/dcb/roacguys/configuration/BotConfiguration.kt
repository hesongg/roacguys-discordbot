package hs.dcb.roacguys.configuration

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class BotConfiguration(private var listeners: Array<ListenerAdapter>) {

    @Value("\${bot.token}")
    private lateinit var token: String

    @PostConstruct
    fun initBot() {
        val jda = JDABuilder.createDefault(token)
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT)
                .build()
        jda.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)
        jda.addEventListener(*listeners)
        jda.awaitReady()
    }
}

