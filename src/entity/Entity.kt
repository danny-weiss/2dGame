package entity

import main.TileManager
import main.MonsterHandler
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO


abstract class Entity {
    abstract var wantedDir: Int
    var x = 0

    var y = 0
    var eTileX = 0
    var eTileY = 0
    val moveList = listOf(listOf(1, 0), listOf(-1, 0), listOf(0, 1), listOf(0, -1))
    val xTileRange = (0..47)
    val yTileRange = (0..47)
    open val responsiveSpeed = false
    var speed = 0
    open var yTilePos = 0
    open var xTilePos = 0
    open lateinit var source: String
    open lateinit var animList: List<String>
    open var imageList: Array<BufferedImage?> = arrayOfNulls(4)
    open fun draw(g2: Graphics2D){}
    fun initImages(){
        for (i in 0..3){
            imageList[i] = ImageIO.read(javaClass.classLoader.getResourceAsStream("$source/${this.animList[i]}.png"))
        }
    }
    fun checkCollision(e1: Entity, e2: Entity): Boolean{
        if(minOf(e1.x, e2.x) + 24 > maxOf(e2.x, e1.x) - 24){
            if(minOf(e1.y, e2.y) + 24 > maxOf(e1.y, e2.y) - 24){
                return true
            }
        }
        return false
    }
    open fun playerContact(){
        //Nothing here
    }
    open fun pathfind(){}
    fun tileCheck(index: Int, tm: TileManager){
    }

    abstract fun move(index: Int, monsterHandler: MonsterHandler)
    abstract fun checkSpeed(monsterNumber: Int, initialMonsterNumber: Int)
}