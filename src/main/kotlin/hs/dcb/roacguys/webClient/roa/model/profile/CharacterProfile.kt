package hs.dcb.roacguys.webClient.roa.model.profile

data class CharacterProfile(
        val CharacterImage: String?,
        val ExpeditionLevel: String,
        val PvpGradeName: String?,
        val TownLevel: String?,
        val TownName: String?,
        val Title: String?,
        val GuildName: String?,
        val ServerName: String,
        val CharacterName: String,
        val CharacterLevel: String,
        val CharacterClassName: String,
        val ItemAvgLevel: String,
        val Stats: List<Stat>?
)