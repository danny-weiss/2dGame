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
        var gamePanel = GamePanel()
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        frame.setLocationRelativeTo(null)
        frame.setVisible(true)
        frame.setTitle("2d")
        frame.setResizable(false)
        frame.add(gamePanel)
        frame.pack()

        gamePanel.startGameThread()
    }

}