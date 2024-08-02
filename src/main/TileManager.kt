package main

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.lang.IndexOutOfBoundsException
import java.util.List.copyOf
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.random.Random

class TileManager(private var gp: GamePanel) {
    var maxScreenCol = 32
    var maxScreenRow = 18
    private lateinit var gameMatrix: List<List<Int>>
    private var listImage: Array<BufferedImage?> = arrayOf(null, null, null)
    private var imageList = listOf("rt", "gt", "st")
    var validSpawnPoints = mutableListOf<List<Int>>()
    var validMatrix = List(maxScreenCol) { MutableList(maxScreenRow - 1){0} }
//    private var nodeMatrix = Array(maxScreenCol) { Array(maxScreenRow - 1){1} } // This is used for new nodes to check if they're far away enough from old ones
    private val validXRange = 0..31
    private val validYRange = 0..16
    init {
        initMatrixImages()
        initGameMatrix()

    }
     fun changeTile(change: Int, tileDirection : String, tileY: List<Int>, tileX: List<Int>): Boolean{
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

     fun initGameMatrix(){
         val nodeMatrix = Array(maxScreenCol) { Array(maxScreenRow - 1){1} }
         validMatrix = List(maxScreenCol) { MutableList(maxScreenRow - 1){0} }
/*        for (i in 0 until maxScreenCol) {
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
        }*/
        for(i in 0 until 3){ // sets a cube and outline
            for(j in 7 until 10) {
                validMatrix[i][j] = 1
            }
        }
        for(i in 0 until maxScreenCol){
            for(j in maxScreenRow-3 until maxScreenRow-1){
                validMatrix[i][j] = 1
            }
        }
        for(i in 0 until maxScreenCol){
            for(j in 0 until 2){
                validMatrix[i][j] = 1
            }
        }
        for(i in 0 until maxScreenRow - 1){
            for(j in 0 until 2){
                validMatrix[i][j] = 1
            }
        }
        for(i in 0 until 2){
            for(j in 0 until maxScreenRow - 1){
                validMatrix[i][j] = 1
            }
        }
        var xCord: Int
        var lineLength: Int
        var yCord: Int
        var horizontal: Boolean
        var dice1:Int
        var twinLinePos: Int
        var dice2:Int
        var currentPos: Int
        var posCorr: Int
        var boundDistance: Int
        var currentDim: Int
        val currentNodes = mutableListOf(listOf(0, 0), listOf(31, 0), listOf(31, 16), listOf(3, 8), listOf(15, 16))
//        var currentNodes = mutableListOf(listOf(15, 8))
        val newNodes = mutableListOf<List<Int>>()
        var coinFlip: Int
        val vectorList = listOf(listOf(1, 0), listOf(-1, 0), listOf(0,1), listOf(0, -1))
        val boundList = listOf(31, 0, 17, 0)
        for(h in 0..3) {
            for (i in 0 until currentNodes.size) {
                posCorr = 0
                for (j in 0..3) {
                    xCord = currentNodes[i][0] + vectorList[j][0]
                    yCord = currentNodes[i][1] + vectorList[j][1]
                    currentDim = xCord
                    horizontal = true
                    if (j > 1) {
                        currentDim = yCord; horizontal = false
                        if (j == 3 && yCord >= 1) {
                            posCorr = -2
                        } else if (yCord <= 14) {
                            posCorr = 2
                        }
                    }
                    if (isValid(xCord, yCord)) {
                        coinFlip = (0..6).random()
                        if (validMatrix[xCord][yCord + posCorr] == 0 && coinFlip != 1) {
                            boundDistance = abs(currentDim - boundList[j])
                            dice1 = (5..7).random()
                            dice2 = (5..7).random()
                            lineLength = minOf(
                                boundDistance - 1,
                                dice1 + dice2 + 4
                            )  // generates a random value between 6 and 14, the 2 dices make center values more probable
//                        lineLength *= sign
                            currentPos = 0
                            if (horizontal) {
                                twinLinePos = if (currentNodes[i][1] == 0) {
                                    1
                                } else {
                                    -1
                                }
                                for (k in 0..lineLength) {
                                    if (validMatrix[currentNodes[i][0] + currentPos][currentNodes[i][1]] == 0 && validMatrix[currentNodes[i][0] + currentPos][currentNodes[i][1] + twinLinePos] == 0) {
                                        validMatrix[currentNodes[i][0] + currentPos][currentNodes[i][1]] = 1
                                        validMatrix[currentNodes[i][0] + currentPos][currentNodes[i][1] + twinLinePos] = 1
                                        currentPos += vectorList[j][0]
                                    } else {
                                        break
                                    }
                                }
                                newNodes.add(listOf(currentNodes[i][0] + currentPos, currentNodes[i][1]))
//                            println("${currentNodes[i][0] + lineLength * sign - 1}, ${currentNodes[i][1]}")
                            } else {
                                twinLinePos = if (currentNodes[i][0] == 0) {
                                    1
                                } else {
                                    -1
                                }
                                for (k in 0..lineLength) {
                                    currentPos += vectorList[j][1]
                                    if (validMatrix[currentNodes[i][0]][currentNodes[i][1] + currentPos] == 0) {
                                        validMatrix[currentNodes[i][0]][currentNodes[i][1] + currentPos] = 1
                                        validMatrix[currentNodes[i][0] + twinLinePos][currentNodes[i][1] + currentPos] = 1
                                    } else {
                                        break
                                    }
                                }
                                newNodes.add(listOf(currentNodes[i][0], currentNodes[i][1] + currentPos))
//                            println("${currentNodes[i][0] + lineLength * sign - 1}, ${currentNodes[i][1]}")
                            }
                        }
                    }
                }
            }
            var i = 0
            while (i < newNodes.size) {
                coinFlip = (1..5).random()
                if (nodeMatrix[newNodes[i][0]][newNodes[i][1]] >= 1 && coinFlip != 0) {
                    for (j in -2..2) {
                        for (k in -2..2) {
                            try {
                                nodeMatrix[j + newNodes[i][0]][k + newNodes[i][1]] = 0
                            } catch (_: ArrayIndexOutOfBoundsException) {
                            }
                        }
                    }
                    nodeMatrix[newNodes[i][0]][newNodes[i][1]] = 2
                } else {
                    newNodes.removeAt(i) // remove nodes to close to each other
                }
                i++
            }
            currentNodes.clear()
            currentNodes.addAll(newNodes)
            currentNodes.addAll(newNodes)
            newNodes.clear()
        }
        /*for (i in nodeMatrix.indices){
            for(j in nodeMatrix[i].indices){
                print("${nodeMatrix[i][j]}, ")
            }
            println("")
        }*/
        for(i in validMatrix.indices){
            for(j in validMatrix[i].indices){
                if(validMatrix[i][j] == 1){
                    validSpawnPoints.add(listOf(i, j))
                }
            }
        }
        gameMatrix = copyOf(validMatrix.toList())
    }
    private fun isValid(xCord:Int, yCord:Int): Boolean{
        return validXRange.contains(xCord) && validYRange.contains(yCord)
    }
    fun randomValidTile(): List<Int> {
        val index = Random.nextInt(validSpawnPoints.size)
        return listOf(validSpawnPoints[index][0], validSpawnPoints[index][1])
    }
    fun drawMatrix(matrixG2: Graphics2D){
        var image:BufferedImage?
        for(i in gameMatrix.indices){
            for (j in gameMatrix[i].indices){
                image = listImage[gameMatrix[i][j]]
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
