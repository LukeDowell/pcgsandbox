package org.badgrades.pcg.dungeon

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import org.badgrades.pcg.dungeon.generator.physics.PhysicsDungeonGenerator

class DungeonAdapter : ApplicationAdapter() {
    
    lateinit var physicsDungeonGenerator: PhysicsDungeonGenerator
    
    override fun create() {
        physicsDungeonGenerator = PhysicsDungeonGenerator()
    }
    
    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT)
        
        physicsDungeonGenerator.update(Gdx.graphics.deltaTime)
    }
}