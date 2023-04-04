package hs.dcb.roacguys.common.const

abstract class Consts {

    companion object {
        const val MY_REPO = "github.com/hesongg/roacguys-discordbot"

        const val VARIABLE_CHARACTER_NAME = "[캐릭터명]"
        const val VARIABLE_MEMBER_MENTION = "[@멤버]"
        const val VARIABLE_RAID_NAME = "[레이드명]"

        const val CHEAT_SHEET_BAL = "발탄"
        const val CHEAT_SHEET_BIA = "비아"
        const val CHEAT_SHEET_KUK = "쿠크"
        const val CHEAT_SHEET_AB12 = "아브12"
        const val CHEAT_SHEET_AB34 = "아브34"
        const val CHEAT_SHEET_AB5 = "아브5"
        const val CHEAT_SHEET_AB6 = "아브6"

        const val COMMAND_HELP = "!help"
        const val COMMAND_CHECK = "!되나"
        const val COMMAND_GET_MEMBER_PROFILE_PICTURE = "!프사 "
        const val COMMAND_GET_ALL_CHARACTERS = "!캐릭터들 "
        const val COMMAND_GET_PROFILE = "!프로필 "
        const val COMMAND_GET_CHEAT_SHEET = "!커닝페이퍼 "

        const val COMMAND_HELP_DESC = "로악 guys Help"
        const val COMMAND_CHECK_DESC = "봇 상태 체크"
        const val COMMAND_GET_MEMBER_PROFILE_PICTURE_DESC = "디스코드 멤버 프로필 사진 조회"
        const val COMMAND_GET_ALL_CHARACTERS_DESC = "$VARIABLE_CHARACTER_NAME 으로 조회 시 해당 계정 캐릭터 3개까지 조회"
        const val COMMAND_GET_PROFILE_DESC = "!프로필 $VARIABLE_CHARACTER_NAME 으로 조회 시 해당 캐릭터 프로필 조회\n" +
                ", (쉼표) 로 이어서 3개까지 조회 가능"
        const val COMMAND_GET_CHEAT_SHEET_DESC = "!커닝페이퍼 $VARIABLE_RAID_NAME 으로 조회 시 커닝페이퍼 조회\n" +
                "$CHEAT_SHEET_BAL, $CHEAT_SHEET_BIA, $CHEAT_SHEET_KUK, $CHEAT_SHEET_AB12, " +
                "$CHEAT_SHEET_AB34, $CHEAT_SHEET_AB5, $CHEAT_SHEET_AB6 커닝페이퍼 조회 가능"


    }
}