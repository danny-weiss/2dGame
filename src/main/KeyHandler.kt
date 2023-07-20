package main

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.lang.NullPointerException


public class KeyHandler(): KeyListener {
    //    var codeMap = mutableMapOf<Int, Int>()

    val codeMap: Map<Int, Int> = mapOf(87 to 0, 65 to 1, 83 to 2, 68 to 3)
    var arrayDecoder = arrayOf(false, false, false, false)
    override fun keyTyped(e: KeyEvent?) { /*useless but needed */}

    public override fun keyPressed(e: KeyEvent) {
        val code = e.getKeyCode()
        val c = codeMap[code]!!
        arrayDecoder[c] = true
    }

    public override fun keyReleased(e: KeyEvent) {
        var code = e.getKeyCode()
        var c : Int = codeMap[code]!!
        arrayDecoder[c] = false
    }
}