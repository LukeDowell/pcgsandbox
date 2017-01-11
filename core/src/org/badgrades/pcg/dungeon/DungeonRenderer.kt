package org.badgrades.pcg.dungeon

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class DungeonRenderer {
    
    val camera = OrthographicCamera()
    val shapeRenderer = ShapeRenderer()
    
    init {
        camera.setToOrtho(
                true,
                Gdx.graphics.width.toFloat(),
                Gdx.graphics.height.toFloat()
        )
        
        shapeRenderer.projectionMatrix = camera.combined
    }
}