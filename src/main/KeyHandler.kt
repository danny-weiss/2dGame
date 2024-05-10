package main

import java.awt.event.KeyEvent
import java.awt.event.KeyListener



class KeyHandler(private val gp: GamePanel) : KeyListener {
    var uiPos = 0
    var arrayDecoder = arrayOf(false, false, false, false)
     override fun keyTyped(e: KeyEvent) { /* useless but need */}

     override fun keyPressed(e: KeyEvent) {
         val keyID = e.keyCode
         val c = when(keyID){
            87 -> 0
            65 -> 1
            83 -> 2
            68 -> 3
            else -> null
        }
        if(c != null) {
            arrayDecoder[c] = true
        }
         if(keyID == 87 || keyID == 38){
             uiPos -= 1
             gp.updateUI = true
         }
         else if(keyID == 83 || keyID == 40){
             uiPos += 1
             gp.updateUI = true
         }
         else if(keyID == 32 || keyID == 10){
             if(gp.gameState == 0) {
                 gp.selectHighlight()
             }
             else if(gp.gameState == 1){
                 gp.playerDash()
             }
         }
     }


     override fun keyReleased(e: KeyEvent) {
         val c : Int? = when(e.keyCode){
            87 -> 0
            65 -> 1
            83 -> 2
            68 -> 3
            else -> null
        }
        if(c != null) {
            arrayDecoder[c] = false
        }
    }
}