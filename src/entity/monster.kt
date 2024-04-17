package entity
import main.GamePanel
import main.TileManager
import kotlin.math.abs

abstract class Monster(private val p: Player, private val tm: TileManager, val gp: GamePanel): Entity() {
    private val validRange = 0..31
    private var originalTile = listOf(0, 0)
    private var previousTile = listOf(0, 0)
    private var inertia = 0
    fun beginPathfind(){
        originalTile = mutableListOf(eTileX, eTileY)

    }
    private fun isValid(i: Int, xDirectionList: Array<Int>, yDirectionList: Array<Int>):Boolean{
        return if(validRange.contains(xDirectionList[i]) && validRange.contains(yDirectionList[i]) && (xDirectionList[i] != previousTile[0] || yDirectionList[i] != previousTile[1])){
            tm.gameMatrix[xDirectionList[i]][yDirectionList[i]] != 0
        }else{
            false
        }
    }
    fun pathfind() {
        val xDirectionList = arrayOf(eTileX + 1, eTileX - 1, eTileX, eTileX)
        val yDirectionList = arrayOf(eTileY, eTileY, eTileY + 1, eTileY - 1)
        val hList: ArrayList<Int> = arrayListOf(0, 0, 0, 0)
        val gList: ArrayList<Int> = arrayListOf(0, 0, 0, 0)
        val fList: ArrayList<Int> = arrayListOf(0, 0, 0, 0)
        var xh:Int
        var yh:Int
        var currentX: Int
        var currentY: Int
        for (i in 0..3) {
            if(isValid(i, xDirectionList, yDirectionList)) {
                currentX = xDirectionList[i]
                currentY = yDirectionList[i]
                xh = abs(currentX - p.tileX[0])
                yh = abs(currentY - p.tileY[0])
                hList[i] = xh + yh
                val xg = abs(currentX - originalTile[0])
                val yg = abs(currentY - originalTile[1])
                gList[i] = xg + yg
                fList[i] = hList[i] - gList[i]
            }
            else{
                hList[i] = 63
                gList[i] = 63
                fList[i] = 63
            }
        }
        //min
        var minVal = fList[0]
        var minIndex = 0
        for (i in 1..3) {
            if (fList[i] < minVal) {
                minIndex = i
                minVal = fList[i]
            } else if (fList[i] == minVal) {
                originalTile = listOf(eTileX, eTileY)
                if (gList[i] == gList[minIndex]) {
                    if(i == inertia){
                        minVal = fList[i]
                        minIndex = i
                    }
                    /*if (xh >= yh) {
                        if (xh < originalXH) {
                            minVal = fList[i]
                            minIndex = i
                        } //else nothing happens
                    } else {
                        if (yh < originalYH) {
                            minVal = fList[i]
                            minIndex = i
                        } //else nothing happens
                    }*/
                } else {
                    if (gList[i] > gList[minIndex]) {
                        minVal = fList[i]
                        minIndex = i
                    } //else nothing happens
                }
            }
        }
        println(minIndex)
        println(minVal)
        inertia = minIndex
        previousTile = listOf(xDirectionList[minIndex], yDirectionList[minIndex])
        move(xDirectionList[minIndex], yDirectionList[minIndex])
    }
    fun move(moveX: Int, moveY: Int){
        x = moveX * gp.tileSize
        y = moveY * gp.tileSize
        eTileX = moveX
        eTileY = moveY
    }
}
