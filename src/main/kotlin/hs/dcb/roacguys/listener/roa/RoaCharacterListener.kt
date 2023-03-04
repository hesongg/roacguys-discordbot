package hs.dcb.roacguys.listener.roa

import hs.dcb.roacguys.common.const.Consts
import hs.dcb.roacguys.common.embed.CommonEmbedBuilder
import hs.dcb.roacguys.listener.abstract.AbstractMessageListener
import hs.dcb.roacguys.webClient.roa.CharacterClient
import hs.dcb.roacguys.webClient.roa.model.charInfo.CharacterInfo
import hs.dcb.roacguys.webClient.roa.model.profile.CharacterProfile
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RoaCharacterListener(private var characterClient: CharacterClient) : AbstractMessageListener() {

    companion object {
        private val log = LoggerFactory.getLogger(RoaCharacterListener::class.java)
        const val CHARACTERS_SHOW_LIMIT = 3
        const val NO_DATA = ""
    }

    private val client = characterClient.getClient()

    override fun execute(event: MessageReceivedEvent) {
        commandLogging(event)

        val contentRaw = event.message.contentRaw
        val eventTextChannel = event.channel

        if (contentRaw.startsWith(Consts.COMMAND_GET_ALL_CHARACTERS)) {
            sendAllCharacterInfomation(eventTextChannel, contentRaw)
            return
        }
        if (contentRaw.startsWith(Consts.COMMAND_GET_PROFILE)) {
            sendCharacterProfile(eventTextChannel, contentRaw)
            return
        }
    }

    private fun sendAllCharacterInfomation(eventTextChannel: MessageChannelUnion, contentRaw: String) {

        val charNm = contentRaw.replace(Consts.COMMAND_GET_ALL_CHARACTERS, NO_DATA)
        eventTextChannel.sendMessage("$charNm 님 캐릭터들 모두 조회 중...\n").queue()

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

        val itemLevelSortedCharInfos = charInfos.sortedByDescending {
            charInfo -> charInfo.ItemAvgLevel.replace(",", "").toDoubleOrNull() ?: 0.0
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

    private fun sendCharacterProfile(eventTextChannel: MessageChannelUnion, contentRaw: String) {

        val charNm = contentRaw.replace(Consts.COMMAND_GET_PROFILE, "")
        eventTextChannel.sendMessage("$charNm 캐릭터 조회 중...\n").queue()

        val profile = client.get()
                .uri("armories/characters/$charNm/profiles")
                .retrieve()
                .bodyToMono(CharacterProfile::class.java)
                .block()

        val eb = CommonEmbedBuilder.getEmbedBuilder("$charNm 캐릭터 프로필 조회 결과")
        if (profile == null) {
            eb.addField("조회 결과", "없음", false)
            eventTextChannel.sendMessageEmbeds(eb.build()).queue()
            return
        }
        if (profile.CharacterImage == null) {
            eventTextChannel.sendMessage("$charNm 캐릭터 정보를 조회할 수 없습니다. - 캐릭터 이미지 없음!").queue()
            return
        }
        eb.setThumbnail(profile.CharacterImage)
        eb.addField("서버", profile.ServerName, true)
        eb.addField("길드", profile.GuildName ?: NO_DATA, true)
        eb.addField("닉네임", (profile.Title ?: "") + " $charNm", true)
        eb.addField("클래스", profile.CharacterClassName, true)
        eb.addField("레벨", profile.CharacterLevel, true)
        eb.addField("아이템 평균 레벨", profile.ItemAvgLevel, true)
        eb.addBlankField(false)
        eb.addField("탐험 레벨", profile.ExpeditionLevel, true)
        eb.addField("PVP 등급", profile.PvpGradeName ?: NO_DATA, true)
        eb.addBlankField(false)
        eb.addField("영지", profile.TownName ?: NO_DATA, true)
        eb.addField("영지 레벨", profile.TownLevel ?: NO_DATA, true)
        eb.addBlankField(false)

        for (stat in profile.Stats!!) {
            eb.addField(stat.Type, stat.Value, true)
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