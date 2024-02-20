package hs.dcb.roacguys.listener

import hs.dcb.roacguys.common.const.Consts
import hs.dcb.roacguys.common.embed.CommonEmbedBuilder
import hs.dcb.roacguys.listener.abstract.AbstractMessageListener
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.utils.FileUpload
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Paths

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

        if (isCommand(contentRaw, Consts.COMMAND_HELP)) {
            sendHelpMessage(eventTextChannel)
            return
        }

        if (isCommand(contentRaw, Consts.COMMAND_CHECK)) {
            sendCheckMessage(eventTextChannel, user)
            return
        }

        if (isCommandStartsWith(contentRaw, Consts.COMMAND_GET_MEMBER_PROFILE_PICTURE)) {
            sendMemberPictureUrl(event)
            return
        }

        if (isCommandStartsWith(contentRaw, Consts.COMMAND_GET_CHEAT_SHEET)) {
            sendCheatSheetImage(contentRaw, eventTextChannel)
            return
        }
    }

    private fun sendHelpMessage(eventTextChannel: MessageChannelUnion) {

        val eb = CommonEmbedBuilder.getEmbedBuilder("명령어 설명! 업데이트 예정")
        eb.addField(Consts.COMMAND_HELP, Consts.COMMAND_HELP_DESC, false)
        eb.addField(Consts.COMMAND_CHECK, Consts.COMMAND_CHECK_DESC, false)
        eb.addField(Consts.COMMAND_GET_MEMBER_PROFILE_PICTURE +
                Consts.VARIABLE_MEMBER_MENTION, Consts.COMMAND_GET_MEMBER_PROFILE_PICTURE_DESC, false)
        eb.addField(Consts.COMMAND_GET_ALL_CHARACTERS + Consts.VARIABLE_CHARACTER_NAME,
                Consts.COMMAND_GET_ALL_CHARACTERS_DESC, false)
        eb.addField(Consts.COMMAND_GET_PROFILE +
                Consts.VARIABLE_CHARACTER_NAME + "," + Consts.VARIABLE_CHARACTER_NAME,
                Consts.COMMAND_GET_PROFILE_DESC, false)
        eb.addField(Consts.COMMAND_GET_CHEAT_SHEET, Consts.COMMAND_GET_CHEAT_SHEET_DESC, false)

        eb.addBlankField(false)
        eb.setFooter(Consts.MY_REPO)

        sendEmbedMessage(eventTextChannel, eb)
    }

    private fun sendCheckMessage(eventTextChannel: MessageChannelUnion, user: User) {
        sendMessage(eventTextChannel, user.asMention + "님, 뭐요?")
    }

    private fun sendMemberPictureUrl(event: MessageReceivedEvent) {
        val users = event.message.mentions.users
        if (users.isEmpty()) {
            sendMessage(event, "멘션된 멤버 정보가 없습니다.")
            return
        }
        if (users[0].avatarUrl == null) {
            sendMessage(event, "해당 멤버의 프로필 사진 정보가 없습니다.")
            return
        }

        sendMessage(event, users[0].avatarUrl.toString() + "?size=4096")
    }

    private fun sendCheatSheetImage(contentRaw: String, eventTextChannel: MessageChannelUnion) {
        val raid = contentRaw.replace(Consts.COMMAND_GET_CHEAT_SHEET, "")

        val resource = Thread.currentThread().contextClassLoader.getResourceAsStream("static/roaCheatSheet/bal.jpeg")

        eventTextChannel.sendMessage("이미지 테스트").addFiles(FileUpload.fromData(resource, "bal.jpeg")).queue()

        val file = File("static/roaCheatSheet/bal.jpeg")
        val eb = CommonEmbedBuilder.getEmbedBuilder("이미지 전송 테스트")

        sendEmbedMessage(eventTextChannel, eb)
    }

    private fun commandLogging(event: MessageReceivedEvent) {
        log.info("[{}][{}] {}: {}",
                event.guild.name,
                event.channel.name,
                event.member?.effectiveName ?: "Unknown",
                event.message.contentDisplay)
    }
}
