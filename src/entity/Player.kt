package entity
import main.GamePanel
import main.KeyHandler
import main.TileManager

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.IOException
import java.lang.IndexOutOfBoundsException
import javax.imageio.ImageIO

class Player public constructor(var gp: GamePanel, var keyH: KeyHandler, var tm: TileManager) : Entity() {
    init {
        setDefaultValues()
    }
    var animList = arrayOf("playChar0", "playChar1", "playChar2", "playChar3")
    var yTilePos = 0
    var xTilePos = 0
    var gameMatrix = tm.gameMatrix
    var tileX: Array<Int> = arrayOf(0 , 1)
    var tileY: Array<Int> = arrayOf(7, 8)
      fun getPlayerImage() : BufferedImage?{
         return try {
//              println(this.animList[gp.animCount])
             ImageIO.read(javaClass.getClassLoader().getResourceAsStream("player/${this.animList[gp.animCount]}.png"))
         }catch (e: IOException){
             null
       //      e.printStackTrace()
         }
     }
     fun setDefaultValues(){
        x = 0
        y = 432-48
        speed = 4
    }

    public fun update() {
        if (keyH.arrayDecoder[0] == true) {
            yTilePos += speed
//            println(yTilePos)
            if (yTilePos >= 16 * 3){
                yTilePos -= speed

/*                var tileToMove = tileY[0]
                var tileNumber = 0
                for (i in tileX.indices){
                     try {
                         tileNumber += gameMatrix[tileX[i]][tileToMove -1]
                     }catch (e: IndexOutOfBoundsException){
                         tileNumber = -1
                     }

                }*/
                if (tm.changeTile(-1, "y", tileY, tileX)){
//                    println("tile ${tileY.contentToString()} is now")
                    tileY[1] -= 1
                    tileY[0] -= 1
//                    println("changed to tile ${tileY.contentToString()}")
                    y -= speed
                    yTilePos = 0
                }
                else{
//                    println("rejected: ${tileX.contentToString()}, ${tileY.contentToString()}")
                    return
                }
            }
            else{
                y -= speed
            }
        } else if (keyH.arrayDecoder[2] == true) {
            yTilePos -= speed
//            println(yTilePos)
            if (yTilePos <= 0){
                yTilePos += speed
/*                var tileToMove = tileY[1]
                var tileNumber = 0
                for (i in tileX.indices){
                    try {
                        tileNumber += gameMatrix[tileX[i]][tileToMove + 1]
//                        println(" y testing  ${tileX[i]} ${tileToMove +1}")
                    }catch (e: IndexOutOfBoundsException){
                        tileNumber = -1
                    }


                }*/
                if (tm.changeTile(1, "y", tileY, tileX)){
//                    println("tile ${tileY.contentToString()} is now")
                    tileY[1] += 1
                    tileY[0] += 1
//                    println("changed to tile ${tileY.contentToString()}")
                    y += speed
                    yTilePos = gp.tileSize
                }
                else{
                    //         println("rejected: ${tileX.contentToString()}, ${tileY.contentToString()}")
                    return
                }
            }
            else{
                y += speed
            }
        } else if (keyH.arrayDecoder[1] == true) {
            xTilePos -= speed
            if (xTilePos <= 0) {
                xTilePos += speed

/*                var tileToMove = tileX[0]
                var tileNumber = 0
                for (i in tileY.indices) {
//                    println("looping x")
                    try {

                        tileNumber += gameMatrix[tileToMove -1][tileY[i]]
//                        println("x testing ${tileToMove-1} ${tileY[i]}")
                    }catch (e: IndexOutOfBoundsException){
                        tileNumber = -1
                    }
                }*/
                if (tm.changeTile(-1, "x", tileY, tileX)) {
//                    println("tile ${tileX.contentToString()} is now")
                    tileX[1] -= 1
                    tileX[0] -= 1
//                    println("changed to tile ${tileX.contentToString()}")
                    x -= speed
                    xTilePos = 48
                } else {
                    return
                }
            }
            else{
                x -= speed
            }
        } else if (keyH.arrayDecoder[3] == true) {
            xTilePos += speed
            if (xTilePos >= 48) {
                xTilePos -= speed
                if (tm.changeTile(1, "x", tileY, tileX)) {
                    tileX[1] += 1
                    tileX[0] += 1
                    x += speed
                    xTilePos = 0
                }
            }
            else {
                x += speed
            }
        }

    }
    fun draw(g2: Graphics2D) {
        val image: BufferedImage = getPlayerImage()!!
   //     getPlayerImage()
        g2.color = Color.white
        g2.drawImage(image,x, y, gp.tileSize, gp.tileSize, null)
    }
}
