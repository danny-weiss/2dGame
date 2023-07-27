package main

import entity.Player
import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.system.measureTimeMillis
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlin.system.*


class GamePanel : JPanel(), Runnable {
    //Screen settings
    var originalTileSize = 16
    var countDraw = 1
    var animCount = 0
    var scale = 3
    var FPS = 60
    var tileSize = originalTileSize * scale
    var tm = TileManager(this)
    var gameThread = Thread()
    var screenWidth = tm.maxScreenCol * tileSize // 768
    var screenHeight = tm.maxScreenRow * tileSize // 576 TO CHANGE????
    // Full screen
    var screenWidth2 = screenWidth
    var screenHeight2 = screenHeight
    var tempScreen: BufferedImage? = null
    lateinit var g2: Graphics2D
    lateinit var levelScreen: BufferedImage
    lateinit var LevelG2: Graphics2D
    var keyH = KeyHandler()
    var player = Player(this, keyH, tm)
    var fullScreenOffsetFactor = 0f
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
    fun setFullScreen(){
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
    fun setUpGame(){
        tempScreen = BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB)
        levelScreen = BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB)
        g2 = tempScreen!!.graphics as Graphics2D
        LevelG2 = levelScreen.graphics as Graphics2D
        drawLevelScreen()

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
                update()
                timer += (currentTime - lastTime)
                lastTime = System.nanoTime()
                drawToTempScreen()
            //    if (countDraw == 1) {
              //      countDraw = 0
                    drawToScreen()

          //      }
                g2.clearRect(0, 0, screenWidth2, screenHeight2)


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
                /*println("count draw: $countDraw")
                countDraw = 0*/
                animCount = 0
                drawCount = 0
                timer = 0
            }
        }
    }

    private fun update() {
            player.update()
        }
         fun drawToScreen(){
//            val executor = Executors.newSingleThreadExecutor()
          //  return CompletableFuture.supplyAsync {
                val g: Graphics = getGraphics()
                val g2: Graphics2D = g as Graphics2D
          /*      g.drawImage(levelScreen, 0, 0, screenWidth2, screenHeight2, null)
                player.draw(g2)*/
                g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null)
                g.dispose()
                countDraw = 1

          //  }
         }
//    : CompletableFuture<Unit>
        fun drawToTempScreen(){
            if (g2 != null) {
                g2.drawImage(levelScreen, 0, 0, screenWidth, screenHeight, null)
                player.draw(g2)
//                g2.drawImage(player.draw(g2), player.x, player.y, tileSize, tileSize, null)

            }

        }
        fun drawLevelScreen(){
            if (LevelG2 != null){
                tm.drawMatrix(LevelG2)
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


