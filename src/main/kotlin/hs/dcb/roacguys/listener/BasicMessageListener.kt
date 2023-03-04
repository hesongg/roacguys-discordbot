package hs.dcb.roacguys.listener

import hs.dcb.roacguys.common.const.Consts
import hs.dcb.roacguys.common.embed.CommonEmbedBuilder
import hs.dcb.roacguys.listener.abstract.AbstractMessageListener
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BasicMessageListener : AbstractMessageListener() {

    companion object {
        private val log = LoggerFactory.getLogger(BasicMessageListener::class.java)
    }

    override fun execute(event: MessageReceivedEvent) {
        commandLogging(event)

        val contentRaw = event.message.contentRaw
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

        val eb = CommonEmbedBuilder.getEmbedBuilder("명령어 설명! 업데이트 예정")
        eb.addField(Consts.COMMAND_HELP, Consts.COMMAND_HELP_DESC, false)
        eb.addField(Consts.COMMAND_CHECK, Consts.COMMAND_CHECK_DESC, false)
        eb.addField(Consts.COMMAND_GET_ALL_CHARACTERS + Consts.VARIABLE_CHARACTER_NAME,
                Consts.COMMAND_GET_ALL_CHARACTERS_DESC, false)
        eb.addField(Consts.COMMAND_GET_PROFILE +
                Consts.VARIABLE_CHARACTER_NAME + "," + Consts.VARIABLE_CHARACTER_NAME,
                Consts.COMMAND_GET_PROFILE_DESC, false)

        eb.addBlankField(false)
        eb.setFooter(Consts.MY_REPO)

        eventTextChannel.sendMessageEmbeds(eb.build()).queue()
    }

    private fun sendCheckMessage(eventTextChannel: MessageChannelUnion, user: User) {
        eventTextChannel.sendMessage(user.asMention + "님, 뭐요?").queue()
    }


    private fun commandLogging(event: MessageReceivedEvent) {
        log.info("[{}][{}] {}: {}",
                event.guild.name,
                event.channel.name,
                event.member?.effectiveName ?: "Unknown",
                event.message.contentDisplay)
    }
}
