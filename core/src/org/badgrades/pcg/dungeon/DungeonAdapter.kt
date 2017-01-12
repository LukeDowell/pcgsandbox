package org.badgrades.pcg.dungeon

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL30
import org.badgrades.pcg.dungeon.generator.physics.PhysicsDungeonGenerator

class DungeonAdapter : ApplicationAdapter(), InputProcessor {
    
    lateinit var physicsDungeonGenerator: PhysicsDungeonGenerator
    
    override fun create() {
        physicsDungeonGenerator = PhysicsDungeonGenerator()
        Gdx.input.inputProcessor = this
    }
    
    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
        
        physicsDungeonGenerator.update(Gdx.graphics.deltaTime)
    }
    
    override fun keyTyped(character: Char): Boolean {
        if(character == ' ')
            physicsDungeonGenerator = PhysicsDungeonGenerator()
        
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
    
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }
}