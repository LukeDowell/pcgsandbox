package org.badgrades.pcg.gameoflife

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor

class GOLInputHandler(val gameOfLife: GameOfLife) : InputProcessor {
    
    override fun keyTyped(character: Char): Boolean {
        if (character == ' ') {
            Gdx.app.log("GameOfLife", "Toggling Pause State")
            gameOfLife.running = !gameOfLife.running
        }
        return true
    }
    
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val cellX = Math.floor((screenX / GridMap.TILE_SIZE).toDouble()).toInt()
        val cellY = Math.floor((screenY / GridMap.TILE_SIZE).toDouble()).toInt()
        Gdx.app.log("GameOfLife", "Cell X: $cellX - Cell Y: $cellY")
    
        val current = gameOfLife.gridMap.grid[cellX][cellY]
        gameOfLife.gridMap.grid[cellX][cellY] = Math.abs(current - 1)
        
        return true
    }
    
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }
    
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return true
    }
    
    override fun scrolled(amount: Int): Boolean {
        return true
    }
    
    override fun keyUp(keycode: Int): Boolean {
        return true
    }
    
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return true
    }
    
    override fun keyDown(keycode: Int): Boolean {
        return true
    }
}