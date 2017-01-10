package org.badgrades.pcg.gameoflife

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL30

class GOLAdapter : ApplicationAdapter() {
    
    lateinit var gameOfLife: GameOfLife
        private set
    
    lateinit var gameOfLifeInputProcessor: InputProcessor
        private set
    
    override fun create() {
        gameOfLife = GameOfLife()
        gameOfLifeInputProcessor = GOLInputHandler(gameOfLife)
        Gdx.input.inputProcessor = gameOfLifeInputProcessor
    }
    
    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
        
        gameOfLife.update(Gdx.graphics.deltaTime)
        GameOfLifeRenderer.render(gameOfLife.gridMap.grid)
    }
}