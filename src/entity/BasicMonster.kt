package entity

import main.GamePanel
import main.MonsterHandler
import main.TileManager
import java.awt.Graphics2D
import java.awt.image.BufferedImage
class BasicMonster(p: Player, tm: TileManager, gp: GamePanel, mh: MonsterHandler, private val id: Int): Monster(p, tm, gp, mh) {
    override var source = "monsters/basic_monster"
    override var animList = listOf("basic_enemy0", "basic_enemy1", "basic_enemy2", "basic_enemy3")
    override val responsiveSpeed = true

    init{
        super.initImages()
        speed =2
    }
    override fun draw(g2: Graphics2D){
        val image: BufferedImage = imageList[gp.animCount]!!
        g2.drawImage(image,x, y, gp.tileSize, gp.tileSize, null)
    }
     override fun checkSpeed(monsterNumber: Int, initialMonsterNumber: Int) {
        println("speed check")
        if(monsterNumber < initialMonsterNumber / 2){
            if(monsterNumber == 1){
                speed = 5
            }
            else if(monsterNumber < initialMonsterNumber / 4) {
                speed = 4
            }
            else{
                speed = 3
            }
        }
    }
    override fun playerContact() {
        if(p.dash) {
            mh.killMe(id)
            if(mh.checkWin()){
                gp.winClock = 1
            }

        }
        else{
            gp.lose()
        }
    }
    /*fun beginPathfind(){
        originalTile = mutableListOf(eTileX, eTileY)
    }*/
}