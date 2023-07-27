package main

import java.awt.event.KeyEvent
import java.awt.event.KeyListener



class KeyHandler : KeyListener {
    //    var codeMap = mutableMapOf<Int, Int>()

    private val codeMap: Map<Int, Int> = mapOf(87 to 0, 65 to 1, 83 to 2, 68 to 3)
    var arrayDecoder = arrayOf(false, false, false, false)
     override fun keyTyped(e: KeyEvent?) { /*useless but needed */}

     override fun keyPressed(e: KeyEvent) {
        val code = e.keyCode
        val c = codeMap[code]!!
        arrayDecoder[c] = true
    }

     override fun keyReleased(e: KeyEvent) {
        val code = e.keyCode
        val c : Int = codeMap[code]!!
        arrayDecoder[c] = false
    }
}