package entity

import java.awt.image.BufferedImage
import javax.imageio.ImageIO


 abstract class Entity {
    var x = 0
    var y = 0
    var eTileX = 0
    var eTileY = 0
    var speed = 0
    open lateinit var source: String
    open lateinit var animList: List<String>
    open var imageList: Array<BufferedImage?> = arrayOfNulls(4)
    fun initImages(){
        for (i in 0..3){
            imageList[i] = ImageIO.read(javaClass.classLoader.getResourceAsStream("$source/${this.animList[i]}.png"))
        }
    }
}