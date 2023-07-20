package main

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.lang.IndexOutOfBoundsException
import javax.imageio.ImageIO
import kotlin.math.abs

class TileManager(private var gp: GamePanel) {
    var maxScreenCol = 32
    var maxScreenRow = 18
    private var listImage: Array<BufferedImage?> = arrayOf(null, null, null)
    private var imageList = listOf<String>("rt", "gt", "st")
    var gameMatrix = Array(maxScreenCol) { IntArray(maxScreenRow -1) }
    init {
        initMatrixImages()
        initGameMatrix()
    }
    public fun changeTile(change: Int, tileDirection : String, tileY: Array<Int>, tileX: Array<Int>): Boolean{
        var tilesValue = 0
        val index = (change +1) / 2
        for (i in 0..1){
            try {
                tilesValue += if(tileDirection == "y"){
                    gameMatrix[tileX[i]][tileY[index] + change]

                } else{
                    gameMatrix[tileX[index] + change][tileY[i]]
                }

            }catch (e: IndexOutOfBoundsException){
                tilesValue = -1
            }
        }
        return tilesValue == 2
    }

    private fun initGameMatrix(){
        for (i in 0 until maxScreenCol) {
            for (j in 0 until maxScreenRow -1) {
                gameMatrix[i][j] = 0
            }
        }
        for (i in 0 until maxScreenCol) {
            for (j in maxScreenRow / 2 - 2 until maxScreenRow / 2 + 1) {
                gameMatrix[i][j] = 1
            }
            for (j in 0..1){
                gameMatrix[i][j] = 1
            }
            for (j in maxScreenRow -3 until maxScreenRow -1){
                gameMatrix[i][j] = 1
            }

        }
        for (i in 0 until maxScreenRow - 1){
            for(j in 0 until  2) {
                gameMatrix[j][i] = 1
            }
            for(j in maxScreenCol / 2 -2 until maxScreenCol/2){
                gameMatrix[j][i] = 1
            }
            for(j in maxScreenCol - 4 until maxScreenCol - 2){
                gameMatrix[j][i] = 1
            }
        }
    }
    fun drawMatrix(matrixG2: Graphics2D){
        for(i in gameMatrix.indices){
            for (j in gameMatrix[i].indices){
                val image =  listImage[gameMatrix[i][j]]
                matrixG2.drawImage(image, i  * gp.tileSize, j * gp.tileSize , gp.tileSize, gp.tileSize, null)
            }
        }
    }

    private fun initMatrixImages(){

        for(i in 0..2) {
            listImage[i] = ImageIO.read(javaClass.classLoader.getResourceAsStream(("tiles/${this.imageList[i]}.png")))
        }
    }

}
