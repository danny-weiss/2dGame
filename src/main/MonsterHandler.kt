package main

import entity.BasicMonster
import entity.Entity
import entity.Player
import java.awt.Graphics2D
import kotlin.random.Random

class MonsterHandler {
    private val basicMax = 10
    private var monsterId = 0
    private var initialMonsterNumber= 0
    private var monsterMap = mutableMapOf<Int, Entity>()
    private var monsterIndexList = mutableListOf<Int>()
    fun spawnMonsters(number: Int, p:Player, tm: TileManager, gp: GamePanel){
        initialMonsterNumber = number
        for(i in 0 until minOf(basicMax, number)){
            val basicMonster = BasicMonster(p, tm, gp, this, monsterId)
            spawnEntity(basicMonster, tm, gp)
            monsterMap[monsterId] = basicMonster
            monsterIndexList.add(monsterId)
            monsterId += 1
        }
    }
    fun beginWander(){

    }
    fun checkWin(): Boolean{
        return monsterIndexList.size == 0
    }
    fun reset(){
        monsterMap = mutableMapOf()
        monsterIndexList = mutableListOf()
    }
    private fun checkSpeed(){
        for(i in 0 until monsterIndexList.size){
            monsterMap[monsterIndexList[i]]?.checkSpeed(monsterIndexList.size, initialMonsterNumber)
        }
    }
    fun killMe(id: Int){
        monsterMap.remove(id)
        monsterIndexList.remove(id)
        checkSpeed()
    }
    fun update(){
        var i = 0
        while(i < monsterIndexList.size){
            if(monsterMap[monsterIndexList[i]] != null) {
                monsterMap[monsterIndexList[i]]!!.move(monsterMap[monsterIndexList[i]]!!.wantedDir, this)
            }
            i += 1
        }
    }
    fun pathfind(){
        for(i in 0 until monsterIndexList.size){
            monsterMap[monsterIndexList[i]]?.pathfind()
        }
    }
    private fun spawnEntity(entity: Entity, tm: TileManager, gp: GamePanel){
        val spawnIndex = Random.nextInt(tm.validSpawnPoints.size)
        entity.eTileX = tm.validSpawnPoints[spawnIndex][0]
        entity.eTileY = tm.validSpawnPoints[spawnIndex][1]
        entity.x = entity.eTileX * gp.tileSize
        entity.y = entity.eTileY * gp.tileSize
    }
    fun draw(g2: Graphics2D){
        for(i in 0 until monsterIndexList.size){
            monsterMap[monsterIndexList[i]]?.draw(g2)
        }
    }

}