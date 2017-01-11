package org.badgrades.pcg.dungeon.generator.physics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import org.badgrades.pcg.dungeon.model.Dungeon
import org.badgrades.pcg.dungeon.model.Hall
import org.badgrades.pcg.dungeon.model.Room

object PhysicsDungeonRenderer {
    
    const val TILE_SIZE = 16f
    
    val camera = OrthographicCamera(
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat()
    )
    val shapeRenderer = ShapeRenderer()
    val debugRenderer = Box2DDebugRenderer()
    
    init {
        shapeRenderer.projectionMatrix = camera.combined
    }
    
    fun render(dungeon: Dungeon, physicsWorld: World) {
        camera.update()
        shapeRenderer.projectionMatrix = camera.combined
        debugRenderer.render(physicsWorld, camera.combined)
        dungeon.halls.forEach {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
            drawHall(it)
            shapeRenderer.end()
        }
        dungeon.rooms.forEach {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            drawRoom(it)
            shapeRenderer.end()
        }
    }
    
    fun drawHall(hall: Hall) {
        shapeRenderer.color = Color.GRAY
    
        shapeRenderer.rect(
                hall.x1 * TILE_SIZE,
                hall.y1 * TILE_SIZE,
                (hall.x2 - hall.x1) * TILE_SIZE,
                (hall.y2 - hall.x1) * TILE_SIZE
        )
    }
    
    fun drawRoom(room: Room) {
        shapeRenderer.color = Color.RED
        
        shapeRenderer.rect(
                room.x1 * TILE_SIZE,
                room.y1 * TILE_SIZE,
                (room.x2 - room.x1) * TILE_SIZE,
                (room.y2 - room.x1) * TILE_SIZE
        )
    }
}