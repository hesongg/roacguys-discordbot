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

    companion object {
        const val CHARACTERS_SHOW_LIMIT = 3
    }

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
        eventTextChannel.sendMessage("$charNm 님 캐릭터들 모두 조회 중...\n").queue()

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
            eventTextChannel.sendMessageEmbeds(eb.build()).queue()
            return
        }
        eb.addField("캐릭터 개수", "총 ${charInfos.size} 개", true)
        eb.addBlankField(false)

        if (charInfos.size > CHARACTERS_SHOW_LIMIT) {
            eventTextChannel.sendMessage("아이템 레벨 상위 3개까지만 조회 가능합니다.").queue()
        }

        val itemLevelSortedCharInfos = charInfos.sortedByDescending { charInfo ->
            charInfo.ItemAvgLevel.replace(",", "").toDoubleOrNull() ?: 0.0
        }
        for (i in itemLevelSortedCharInfos.indices) {
            if (i == CHARACTERS_SHOW_LIMIT) break

            eb.addField("서버명", charInfos[i].ServerName, true)
            eb.addField("닉네임", itemLevelSortedCharInfos[i].CharacterName, true)
            eb.addField("레벨", itemLevelSortedCharInfos[i].CharacterLevel.toString(), true)
            eb.addField("클래스", itemLevelSortedCharInfos[i].CharacterClassName, true)
            eb.addField("평균 아이템 레벨", itemLevelSortedCharInfos[i].ItemAvgLevel, true)
//            eb.addField("Max 아이템 레벨", characterInfo.ItemMaxLevel, false)
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