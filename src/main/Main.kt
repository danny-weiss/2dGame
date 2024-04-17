package main

import main.Main.frameObject.frame
import javax.swing.JFrame

object Main {
    object frameObject{
        lateinit var frame: JFrame
    }
    @JvmStatic
      fun main(args: Array<String>) {
        frame = JFrame("2d game")
        val gamePanel = GamePanel()
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
        frame.title = "2d"
        frame.isResizable = false
        frame.add(gamePanel)
        frame.pack()

        gamePanel.startGameThread()
    }

}