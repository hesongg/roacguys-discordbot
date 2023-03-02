package hs.dcb.roacguys.common.embed

import net.dv8tion.jda.api.EmbedBuilder
import java.awt.Color

class CommonEmbedBuilder {

    companion object {
        fun getEmbedBuilder(title: String): EmbedBuilder {
            val eb = EmbedBuilder()
            eb.setTitle(title)
            eb.addBlankField(true)
            eb.setColor(Color.GREEN)

            return eb
        }
    }
}