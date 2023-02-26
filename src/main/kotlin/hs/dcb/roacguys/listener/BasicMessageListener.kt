package hs.dcb.roacguys.listener

import hs.dcb.roacguys.listener.common.AbstractMessageListener
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BasicMessageListener : AbstractMessageListener() {

    val log = LoggerFactory.getLogger(BasicMessageListener::class.java)

    override fun execute(event: MessageReceivedEvent) {
        if (event.author.isBot) return

        if (event.isFromType(ChannelType.PRIVATE)) {
            log.info("[DM] {}: {}\n",
                    event.author.name,
                    event.message.contentDisplay)

            return
        }

        val contentRaw = event.message.contentRaw

        if (!contentRaw.startsWith("!")) {
            return
        }

        log.info("[{}][{}] {}: {}\n",
                event.guild.name,
                event.channel.name,
                event.member?.effectiveName ?: "Unknown",
                event.message.contentDisplay)

        val eventTextChannel = event.channel
        val user = event.author

        if (contentRaw == "!되나") {
            eventTextChannel.sendMessage(user.asMention + "님, 뭐요?").queue()
        }
    }
}