package entity
import main.GamePanel
import main.KeyHandler
import main.TileManager

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class Player(private var gp: GamePanel, private var keyH: KeyHandler, private var tm: TileManager) : Entity() {

    override var animList: List<String> = listOf("playChar0", "playChar1", "playChar2", "playChar3")
    private var yTilePos = 0
    private var xTilePos = 0
    var tileX: Array<Int> = arrayOf(0 , 1)
    var tileY: Array<Int> = arrayOf(7, 8)
    override var source = "player"
    init {
        setDefaultValues()
        super.initImages()
    }
        /*private fun initPlayerImages(){
            for (i in 0..3){
                imageList[i] = ImageIO.read(javaClass.classLoader.getResourceAsStream("player/${this.animList[i]}.png"))
            }
        }*/
     private fun setDefaultValues(){
        x = 0
        y = 432-48
        speed = 4
    }

     fun update() {
        if (keyH.arrayDecoder[0]) {
            yTilePos += speed
            if (yTilePos >= 16 * 3){
                yTilePos -= speed
                if (tm.changeTile(-1, "y", tileY, tileX)){
                    tileY[1] -= 1
                    tileY[0] -= 1
                    y -= speed
                    yTilePos = 0
                }
            }
            else{
                y -= speed
            }
        } else if (keyH.arrayDecoder[2]) {
            yTilePos -= speed
            if (yTilePos <= 0){
                yTilePos += speed
                if (tm.changeTile(1, "y", tileY, tileX)){
                    tileY[1] += 1
                    tileY[0] += 1
                    y += speed
                    yTilePos = gp.tileSize
                }
            }
            else{
                y += speed
            }
        } else if (keyH.arrayDecoder[1]) {
            xTilePos -= speed
            if (xTilePos <= 0) {
                xTilePos += speed
                if (tm.changeTile(-1, "x", tileY, tileX)) {
                    tileX[1] -= 1
                    tileX[0] -= 1
                    x -= speed
                    xTilePos = 48
                }
            }
            else{
                x -= speed
            }
        } else if (keyH.arrayDecoder[3]) {
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
    fun draw(g2: Graphics2D){
        val image: BufferedImage = imageList[gp.animCount]!!
        g2.drawImage(image,x, y, gp.tileSize, gp.tileSize, null)
    }
}

