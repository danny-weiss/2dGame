package entity
import main.GamePanel
import main.KeyHandler
import main.TileManager
import main.MonsterHandler

import java.awt.Graphics2D
import java.awt.image.BufferedImage

class Player(private var gp: GamePanel, private var keyH: KeyHandler, private var tm: TileManager) : Entity() {
    override var wantedDir = 0
    override var animList: List<String> = listOf("playChar0", "playChar1", "playChar2", "playChar3")
    var tileX = mutableListOf(0 , 1)
    var tileY = mutableListOf(7, 8)
    private val dashSize = 3
    var dash = false
    private var dashedPixels = 0
    private var latestMovement = 0
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
      fun setDefaultValues(){
        x = 0
        y = 432-gp.tileSize
        yTilePos = 0
        xTilePos = 0
        tileX = mutableListOf(0 , 1)
        tileY = mutableListOf(7, 8)
        dash = false
        dashedPixels = 0
        latestMovement = 0
        speed = 4
    }
    private fun checkDash(dashBoost: Int){
        if(dash) {
            dashedPixels += speed * dashBoost
            if(dashedPixels >= dashSize * gp.tileSize){
                dash = false
                dashedPixels = 0
            }
        }
    }

     fun update() {
        val dashBoost = if(dash){gp.tileSize / 8} else { 1 }
        if (keyH.arrayDecoder[0]) {
            yTilePos += speed * dashBoost
            if (yTilePos > gp.tileSize){
                if (tm.changeTile(-1, "y", tileY, tileX)){
                    tileY[1] -= 1
                    tileY[0] -= 1
                    y -= speed * dashBoost
                    checkDash(dashBoost)
                    yTilePos %= gp.tileSize
                    latestMovement = 3
                }
                else{
                    yTilePos -= speed * dashBoost
                    dash = false
                }
            }
            else{
                y -= speed * dashBoost
                checkDash(dashBoost)
                latestMovement = 3
            }
        } else if (keyH.arrayDecoder[2]) {
            yTilePos -= speed * dashBoost
            if (yTilePos < 0){
                if (tm.changeTile(1, "y", tileY, tileX)){
                    tileY[1] += 1
                    tileY[0] += 1
                    y += speed * dashBoost
                    checkDash(dashBoost)
                    yTilePos += gp.tileSize
                    latestMovement = 2
                }
                else{
                    dash = false
                    yTilePos += speed * dashBoost
                }
            }
            else{
                y += speed * dashBoost
                checkDash(dashBoost)
                latestMovement = 2
            }
        } else if (keyH.arrayDecoder[1]) {
            xTilePos -= speed * dashBoost
            if (xTilePos < 0) {
                if (tm.changeTile(-1, "x", tileY, tileX)) {
                    tileX[1] -= 1
                    tileX[0] -= 1
                    x -= speed * dashBoost
                    checkDash(dashBoost)
                    xTilePos += gp.tileSize
                    latestMovement = 1
                }
                else{
                    dash = false
                    xTilePos += speed * dashBoost
                }
            }
            else{
                x -= speed * dashBoost
                checkDash(dashBoost)
                latestMovement = 1
            }
        } else if (keyH.arrayDecoder[3]) {
            xTilePos += speed * dashBoost
            if (xTilePos > gp.tileSize) {
                if (tm.changeTile(1, "x", tileY, tileX)) {
                    tileX[1] += 1
                    tileX[0] += 1
                    x += speed * dashBoost
                    checkDash(dashBoost)
                    xTilePos %= 48
                    latestMovement = 0
                }
                else{
                    dash = false
                    xTilePos -= speed * dashBoost
                }
            }
            else {
                x += speed * dashBoost
                latestMovement = 0
            }
        }
        else if(dash){
            checkDash(dashBoost*2)
        }
    }
    override fun draw(g2: Graphics2D){
        val image: BufferedImage = imageList[gp.animCount]!!
        g2.drawImage(image,x, y, gp.tileSize, gp.tileSize, null)
    }

    override fun move(index: Int, monsterHandler: MonsterHandler) {
        TODO("Not yet implemented")
    }

    override fun checkSpeed(monsterNumber: Int, initialMonsterNumber: Int) {
        TODO("Not yet implemented")
    }
}

