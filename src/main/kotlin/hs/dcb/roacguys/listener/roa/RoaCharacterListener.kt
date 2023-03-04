package hs.dcb.roacguys.listener.roa

import hs.dcb.roacguys.common.const.Consts
import hs.dcb.roacguys.common.embed.CommonEmbedBuilder
import hs.dcb.roacguys.listener.abstract.AbstractMessageListener
import hs.dcb.roacguys.webClient.roa.CharacterClient
import hs.dcb.roacguys.webClient.roa.model.CharacterInfo
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RoaCharacterListener(private var characterClient: CharacterClient) : AbstractMessageListener() {

    val log = LoggerFactory.getLogger(RoaCharacterListener::class.java)

    override fun execute(event: MessageReceivedEvent) {
        commandLogging(event)

        val contentRaw = event.message.contentRaw
        val eventTextChannel = event.channel

        if (contentRaw.startsWith(Consts.COMMAND_GET_ALL_CHARACTERS)) {
            sendAllCharacterInfomation(eventTextChannel, contentRaw)
        }
    }

    private fun sendAllCharacterInfomation(eventTextChannel: MessageChannelUnion, contentRaw: String) {

        val charNm = contentRaw.replace(Consts.COMMAND_GET_ALL_CHARACTERS, "")

        val client = characterClient.getClient()

        val response = client.get()
                .uri("/characters/$charNm/siblings")
                .retrieve()
                .bodyToFlux(CharacterInfo::class.java)

        val charInfos = response.collectList()
                .block()

        val eb = CommonEmbedBuilder.getEmbedBuilder("$charNm 의 캐릭터 모두 조회 결과")
        if (charInfos.isNullOrEmpty()) {
            eb.addField("조회 결과", "없음", false)
            return
        }
        eb.addField("캐릭터 개수", "총 ${charInfos.size} 개", false)
        eb.addBlankField(false)

        for (characterInfo in charInfos) {
            eb.addField("서버명", characterInfo.ServerName, false)
            eb.addField("캐릭터명", characterInfo.CharacterName, false)
            eb.addField("레벨", characterInfo.CharacterLevel.toString(), false)
            eb.addField("클래스", characterInfo.CharacterClassName, false)
            eb.addField("평균 아이템 레벨", characterInfo.ItemAvgLevel, false)
            eb.addField("Max 아이템 레벨", characterInfo.ItemMaxLevel, false)
            eb.addBlankField(false)
        }

        eventTextChannel.sendMessageEmbeds(eb.build()).queue()
    }

    private fun commandLogging(event: MessageReceivedEvent) {
        log.info("[{}][{}] {}: {}",
                event.guild.name,
                event.channel.name,
                event.member?.effectiveName ?: "Unknown",
                event.message.contentDisplay)
    }
}