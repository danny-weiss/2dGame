package entity

import main.GamePanel
import main.TileManager
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class BasicMonster(p: Player, tm: TileManager, gp: GamePanel): Monster(p, tm, gp) {
    override var source = "monsters/basic_monster"
    override var animList = listOf("basic_enemy0", "basic_enemy1", "basic_enemy2", "basic_enemy3")
    init{
        super.initImages()
    }
    fun draw(g2: Graphics2D){
        val image: BufferedImage = imageList[gp.animCount]!!
        g2.drawImage(image,x, y, gp.tileSize, gp.tileSize, null)
    }
}