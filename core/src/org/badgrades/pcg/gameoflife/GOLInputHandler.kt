package org.badgrades.pcg.gameoflife

import com.badlogic.gdx.InputProcessor

class GOLInputHandler(val gameOfLife: GameOfLife) : InputProcessor {
    
    override fun keyTyped(character: Char): Boolean {
        if (character == ' ') {
            gameOfLife.running = !gameOfLife.running
        }
        return true
    }
    
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val cellX = Math.floor((screenX / GridMap.TILE_SIZE).toDouble()).toInt()
        val cellY = Math.floor((screenY / GridMap.TILE_SIZE).toDouble()).toInt() // All this casting is gross
    
        val current = gameOfLife.gridMap.grid[cellX][cellY]
        gameOfLife.gridMap.grid[cellX][cellY] = Math.abs(current - 1)
        
        return true
    }
    
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }
    
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }
    
    override fun scrolled(amount: Int): Boolean {
        return false
    }
    
    override fun keyUp(keycode: Int): Boolean {
        return false
    }
    
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }
    
    override fun keyDown(keycode: Int): Boolean {
        return false
    }
}