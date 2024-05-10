package main

import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D

class GameUserInterface(private val g2: Graphics2D, private val keyH: KeyHandler, private val gp: GamePanel, g: Graphics) {
    private val arial = Font("Arial", Font.PLAIN, 40)
    private var smallMetrics: FontMetrics
    private val bigArial = Font("Arial", Font.BOLD, 80)
    private var bigMetrics: FontMetrics
    var highlightedTextIndex = 0

    init{
        g2.font = arial
        smallMetrics = g.fontMetrics
        g2.font = bigArial
        bigMetrics = g.fontMetrics

    }
    fun drawTitle(title: String){
        val currentUiElements = 1
        g2.font = bigArial
        g2.color = Color.WHITE
        var text = title
        var textX = (gp.screenWidth2 - bigMetrics.stringWidth(text)) / 2
        g2.drawString(text, textX, 3 * gp.tileSize)

        g2.font = arial
        if(keyH.uiPos < 0){
            keyH.uiPos = currentUiElements
        }
        highlightedTextIndex = keyH.uiPos % (currentUiElements + 1)
        if(highlightedTextIndex == 0){
            g2.color = Color.RED
        }
        else{
            g2.color = Color.WHITE
        }
        text = "Play the game"
        textX = (gp.screenWidth2 - smallMetrics.stringWidth(text))  /2
        g2.drawString(text, textX, 6 * gp.tileSize)
        if(highlightedTextIndex == 1){
            g2.color = Color.RED
        }
        else{
            g2.color = Color.WHITE
        }
        text = "SoMETHING ELSE"
        textX = (gp.screenWidth2 - smallMetrics.stringWidth(text))  /2
        g2.drawString("SOMETHING ELSE", textX, 8*gp.tileSize)
    }
}