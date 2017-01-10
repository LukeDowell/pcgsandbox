package org.badgrades.pcg.gameoflife.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.badgrades.pcg.gameoflife.GridMap

object GameOfLifeRenderer {
    
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
    
    fun render(grid: Array<IntArray>) {
        camera.update()
        
        for(x in 0 until grid.size) {
            for(y in 0 until grid[0].size) {
                
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
                
                when(grid[x][y]) {
                    0 -> shapeRenderer.color = Color.BLACK
                    else -> shapeRenderer.color = Color.WHITE
                }
                
                
                shapeRenderer.rect(
                        x * GridMap.TILE_SIZE,
                        y * GridMap.TILE_SIZE,
                        GridMap.TILE_SIZE,
                        GridMap.TILE_SIZE
                )
                shapeRenderer.end()
                
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                shapeRenderer.color = Color.WHITE
                shapeRenderer.rect(
                        x * GridMap.TILE_SIZE,
                        y * GridMap.TILE_SIZE,
                        GridMap.TILE_SIZE,
                        GridMap.TILE_SIZE
                )
                shapeRenderer.end()
            }
        }
    }
}