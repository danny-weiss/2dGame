package entity
import main.GamePanel
import main.MonsterHandler
import main.TileManager
import kotlin.math.abs
import kotlin.math.roundToInt

abstract class Monster(val p: Player, private val tm: TileManager, val gp: GamePanel, val mh:MonsterHandler): Entity() {
    private val validXRange = 0..31
    private val validYRange = 0..16
    private var originalTile = mutableListOf(0, 0)
    private var previousTile = listOf(0, 0)
    private var inertia = 0
    var walkState = 0 //Default player chasing state
    lateinit var aimedTile: List<Int>
    override var wantedDir = 0
    private fun isValid(tileX: Int, tileY: Int, previousTile:List<Int>):Boolean{
        return if(validXRange.contains(tileX) && validYRange.contains(tileY) && (tileX != previousTile[0] || tileY != previousTile[1])){
            tm.validMatrix[tileX][tileY] != 0
        }else{
            false
        }
    }
    private fun  closePathfind(): Int{
        var xh: Int
        var yh: Int
        var minH = 100000
        var minIndex = 0
        for(i in 0..3){
            xh = abs(x + (moveList[i][0] * speed)- p.x)
            yh = abs(y + (moveList[i][1] * speed) - p.y)
            if(xh + yh < minH){
                if(isValid(((x + moveList[i][0] * speed).toFloat() / gp.tileSize).roundToInt(), ((y + moveList[i][1] * speed).toFloat() / gp.tileSize).roundToInt(), listOf(-1, -1))) {
                    minH = xh + yh
                    minIndex = i
                }
            }
        }
        return minIndex
    }
    override fun pathfind() {
        val aimedTilesList = listOf(p.tileX[0], p.tileY[0])
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
            if(isValid(xDirectionList[i], yDirectionList[i], previousTile)) {
                currentX = xDirectionList[i]
                currentY = yDirectionList[i]
                xh = abs(currentX - aimedTilesList[0])
                yh = abs(currentY - aimedTilesList[1])
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
        for (i in 1..3) { // loops starts at one because min started before
            if (fList[i] < minVal) {
                minIndex = i
                minVal = fList[i]
            } else if (fList[i] == minVal) {
                originalTile = mutableListOf(eTileX, eTileY)
                if (gList[i] == gList[minIndex]) {
                    if(i == inertia){
                        minVal = fList[i]
                        minIndex = i
                    }
                } else {
                    if (gList[i] > gList[minIndex]) {
                        minVal = fList[i]
                        minIndex = i
                    } //else nothing happens
                }
            }
        }
        if(minVal == 63){
            previousTile = listOf(eTileX, eTileY)
            println("No positions")
            return
        }
        inertia = minIndex
        previousTile = listOf(xDirectionList[minIndex], yDirectionList[minIndex])
        wantedDir = minIndex
    }
    fun changeSpeed(){

    }
    override fun move(index: Int, monsterHandler: MonsterHandler){
        var moveIndex = index
        var tileChanged = false
        var closeToPlayer = false
        if(moveIndex != wantedDir){
            println("$moveIndex, $wantedDir")
        }
        if(abs(x - p.x) + abs(y - p.y) <= gp.tileSize * 2){
            moveIndex = this.closePathfind()
            closeToPlayer = true
        }
        x += moveList[moveIndex][0] * speed
        y += moveList[moveIndex][1] * speed
        if (eTileX != x / gp.tileSize) {
//            tm.validMatrix[eTileX][eTileY] = 1
            eTileX = x / gp.tileSize
            this.pathfind()
            tileChanged = true
        }
        if (eTileY != y / gp.tileSize) {
//            tm.validMatrix[eTileX][eTileY] = 1
            eTileY = y / gp.tileSize
            this.pathfind()
            yTilePos = abs(yTilePos - gp.tileSize)
            tileChanged = true
        }
        if(tileChanged){
//            tm.validMatrix[eTileX][eTileY] = 0
        }
        if(closeToPlayer){
            if(x < p.x){
                if(checkCollision(this, p)){
                    playerContact()
                }
            }
            else {
                if(checkCollision(p, this)){
                    playerContact()
                }
            }
        }
    }
}
