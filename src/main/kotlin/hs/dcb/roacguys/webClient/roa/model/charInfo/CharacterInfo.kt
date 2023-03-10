package hs.dcb.roacguys.webClient.roa.model.charInfo

data class CharacterInfo(
        val ServerName: String,
        val CharacterName: String,
        val CharacterLevel: Int,
        val CharacterClassName: String,
        val ItemAvgLevel: String,
        val ItemMaxLevel: String
)
