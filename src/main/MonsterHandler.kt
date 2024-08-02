package main

import entity.BasicMonster
import entity.Entity
import entity.Monster
import entity.Player
import java.awt.Graphics2D
import kotlin.random.Random

class MonsterHandler {
    private val basicMax = 10
    private var monsterId = 0
    private var initialMonsterNumber= 0
    private var monsterMap = mutableMapOf<Int, Monster>()
    private var wanderingList = mutableListOf<Int>()
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
    fun wanderTick(){
        for (i in 0 until wanderingList.size) {
            monsterMap[wanderingList[i]]?.stopWander()
        }
        if(monsterIndexList.size > 2) {
            var coinFlip = 0
            for (i in 0 until monsterIndexList.size) {
                coinFlip = (0..3).random()
                if (coinFlip == 0) {
                    wanderingList.add(monsterIndexList[i])
                    monsterMap[monsterIndexList[i]]?.beginWander()
                }
            }
        }
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
    private fun spawnEntity(monster: Monster, tm: TileManager, gp: GamePanel){
        val randomTile = tm.randomValidTile()
        monster.eTileX = randomTile[0]
        monster.eTileY = randomTile[1]
        println(tm.validMatrix[randomTile[0]][randomTile[1]])
        monster.x = monster.eTileX * gp.tileSize
        monster.y = monster.eTileY * gp.tileSize
    }
    fun draw(g2: Graphics2D){
        for(i in 0 until monsterIndexList.size){
            monsterMap[monsterIndexList[i]]?.draw(g2)
        }
    }

}