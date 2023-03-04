package hs.dcb.roacguys.common.const

abstract class Consts {

    companion object {
        const val MY_REPO = "github.com/hesongg/roacguys-discordbot"

        const val VARIABLE_CHARACTER_NAME = "[캐릭터명]"

        const val COMMAND_HELP = "!help"
        const val COMMAND_CHECK = "!되나"
        const val COMMAND_GET_ALL_CHARACTERS = "!캐릭터들 "
        const val COMMAND_GET_PROFILE = "!프로필 "

        const val COMMAND_HELP_DESC = "로악 guys Help"
        const val COMMAND_CHECK_DESC = "봇 상태 체크"
        const val COMMAND_GET_ALL_CHARACTERS_DESC = "캐릭터 명으로 조회 시 해당 계정 캐릭터 3개까지 조회"
        const val COMMAND_GET_PROFILE_DESC = "!프로필 캐릭터 명으로 조회 시 해당 캐릭터 프로필 조회\n, (쉼표) 로 이어서 3개까지 조회 가능"
    }
}