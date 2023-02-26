package hs.dcb.roacguys.listener

import hs.dcb.roacguys.common.const.Consts
import hs.dcb.roacguys.common.exception.MessageException
import hs.dcb.roacguys.listener.common.AbstractMessageListener
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BasicMessageListener : AbstractMessageListener() {

    val log = LoggerFactory.getLogger(BasicMessageListener::class.java)

    override fun execute(event: MessageReceivedEvent) {

        if (isBotMessage(event)) return

        doReturnIsDirectMessage(event)

        val contentRaw = event.message.contentRaw
        if (isNotCommandMessage(contentRaw)) return

        commandLogging(event)

        val eventTextChannel = event.channel
        val user = event.author

        if (contentRaw == Consts.COMMAND_HELP) {
            sendHelpMessage(eventTextChannel)
        }

        if (contentRaw == Consts.COMMAND_CHECK) {
            sendCheckMessage(eventTextChannel, user)
        }
    }

    private fun sendHelpMessage(eventTextChannel: MessageChannelUnion) {
        val sb = StringBuilder()
        sb.append("명령어 설명 ! 업데이트 예정..\n\n")
        sb.append(Consts.COMMAND_HELP + Consts.COMMAND_SPACE + Consts.COMMAND_HELP_DESC + "\n")
        sb.append(Consts.COMMAND_CHECK + Consts.COMMAND_SPACE + Consts.COMMAND_CHECK_DESC + "\n")

        eventTextChannel.sendMessage(sb.toString()).queue()
    }

    private fun sendCheckMessage(eventTextChannel: MessageChannelUnion, user: User) {
        eventTextChannel.sendMessage(user.asMention + "님, 뭐요?").queue()
    }

    private fun isNotCommandMessage(contentRaw: String): Boolean {
        if (!contentRaw.startsWith("!")) return true
        return false
    }

    private fun isBotMessage(event: MessageReceivedEvent): Boolean {
        if (event.author.isBot) return true

        return false
    }

    private fun doReturnIsDirectMessage(event: MessageReceivedEvent) {
        if (event.isFromType(ChannelType.PRIVATE)) {
            log.info("[DM] {}: {}",
                    event.author.name,
                    event.message.contentDisplay)

            throw MessageException("개인 메세지 입니다.")
        }
    }

    private fun commandLogging(event: MessageReceivedEvent) {
        log.info("[{}][{}] {}: {}",
                event.guild.name,
                event.channel.name,
                event.member?.effectiveName ?: "Unknown",
                event.message.contentDisplay)
    }
}
