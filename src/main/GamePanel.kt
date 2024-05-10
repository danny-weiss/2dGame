package main

import entity.Player
import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel


class GamePanel : JPanel(), Runnable {
    //Screen settings
    private lateinit var g: Graphics
    private lateinit var g2: Graphics2D
    private lateinit var gui: GameUserInterface
    private var lastDashTime = 0
    var winClock = 0
    var timeSeconds = 0
    var currentTitle = "PixelDash"
    var monsterNumber = 1
    private var originalTileSize = 16
    var animCount = 0
    var gameState = 0 //Show UI
    var scale = 3
    private var FPS = 60
    var tileSize = originalTileSize * scale
    private var tm = TileManager(this)
    private var gameThread = Thread()
    private val screenWidth = tm.maxScreenCol * tileSize // 768
    private val screenHeight = tm.maxScreenRow * tileSize // 576 TO CHANGE????
    // Full screen
    var screenWidth2 = screenWidth
    private var screenHeight2 = screenHeight
    private lateinit var tempScreen: BufferedImage
    private lateinit var levelScreen: BufferedImage
    private lateinit var levelG2: Graphics2D
    private lateinit var tempG2: Graphics2D
    private var keyH = KeyHandler(this)
    var updateUI = true
    private var player = Player(this, keyH, tm)
    private var monsterHandler = MonsterHandler()
    private var fullScreenOffsetFactor = 0f
    //     default player pos

    init {
        this.background = Color.BLACK
        this.isDoubleBuffered = true
        this.addKeyListener(keyH)
        this.isFocusable = true
        setFullScreen()
        this.preferredSize = Dimension(screenWidth2, screenHeight2)
    }


    // Game Thread code
    private fun setFullScreen(){
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val width = screenSize.getWidth()
        val height = screenSize.getHeight()
        Main.frameObject.frame.extendedState = JFrame.MAXIMIZED_BOTH
        screenWidth2 = width.toInt()
        screenHeight2 = height.toInt()
        //offset factor to be used by mouse listener or mouse motion listener if you are using cursor in your game. Multiply your e.getX()e.getY() by this.
        fullScreenOffsetFactor = screenWidth.toFloat() / screenWidth2.toFloat()
    }
    fun startGameThread() {
        gameThread = Thread(this)
        gameThread.start()
    }
    private fun setUpGame(){
        g = graphics
        g2 = g as Graphics2D
        gui = GameUserInterface(g2, keyH, this, g)
        tempScreen = BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB)
        levelScreen = BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB)
        tempG2 = tempScreen.graphics as Graphics2D
        levelG2 = levelScreen.graphics as Graphics2D
        drawLevelScreen()
        monsterHandler.spawnMonsters(monsterNumber, player, tm, this)
        monsterHandler.pathfind()

    }
        // run the game loop
    override fun run() {
        setUpGame()
        var currentTime: Long
        val drawInterval = 1000000000 / FPS
        var lastTime: Long = System.nanoTime()
        var drawCount = 0
        var timer: Long = 0
        while (gameThread != null) {
            currentTime = System.nanoTime()
            if (currentTime - lastTime >= drawInterval) {
                timer += (currentTime - lastTime)
                lastTime = System.nanoTime()
                update()
                if(gameState == 1) {
                    drawToScreen()
                    drawToTempScreen()
                }
                else if(gameState == 0 && updateUI){
                    gui.drawTitle(currentTitle)
                    updateUI = false
                }
                if (drawCount % 10 == 0) {
                    if (animCount + 1 == 4) {
                        animCount = 0
                    } else {
                        animCount += 1
                    }
                }
                drawCount++

            }

            if (timer >= 1000000000) {
                println("FPS: $drawCount")
                animCount = 0
                drawCount = 0
                winClockHandler()
                timeSeconds +=1
                timer = 0
            }
        }
    }
    private fun winClockHandler(){
        if(winClock  > 0){
            winClock -= 1
            if(winClock == 0){
                win()
            }
        }
    }
    private fun update() {
        if(gameState == 1) {
            player.update()
            monsterHandler.update()
        }
    }
     private fun drawToScreen(){
//            val executor = Executors.newSingleThreadExecutor()
      //  return CompletableFuture.supplyAsync {
      /*      g.drawImage(levelScreen, 0, 0, screenWidth2, screenHeight2, null)
            player.draw(g2)*/
            val drawG = graphics
            drawG.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null)
            drawG.dispose()
      //  }
     }
    private fun drawToTempScreen(){
        if (g2 != null) {
            tempG2.drawImage(levelScreen, 0, 0, screenWidth, screenHeight, null)
            monsterHandler.draw(tempG2)
            player.draw(tempG2)
//                g2.drawImage(player.draw(g2), player.x, player.y, tileSize, tileSize, null)
        }

    }
    fun playerDash(){
        if(lastDashTime < timeSeconds - 2) {
            player.dash = true
        }
    }
    private fun newGame(){
        player.setDefaultValues()
        monsterHandler.reset()
        tm.initGameMatrix()
        monsterHandler.spawnMonsters(monsterNumber, player, tm, this)
        drawLevelScreen()
        gameState = 1
    }
    fun selectHighlight(){
        if(gui.highlightedTextIndex == 0){
            newGame()
        }
        else{
            println("HighLighter")
        }
    }
    fun win(){
        currentTitle = "Victory"
        gameState = 0
        monsterNumber += 1
    }
    fun lose(){
        currentTitle = "Defeat"
        gameState = 0
    }
    private fun drawLevelScreen(){
        if (levelG2 != null){
            tm.drawMatrix(levelG2)
        }
    }
/*        public override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            val g2 = g as? Graphics2D
            if (g2 != null) {
                player.draw(g2)
                g2.dispose()
            }
        }*/
    }


