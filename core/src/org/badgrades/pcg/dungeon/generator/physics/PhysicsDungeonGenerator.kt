package org.badgrades.pcg.dungeon.generator.physics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import org.badgrades.pcg.dungeon.generator.DungeonGenerator
import org.badgrades.pcg.dungeon.model.Dungeon
import java.util.*

/**
 * http://www.gamasutra.com/blogs/AAdonaac/20150903/252889/Procedural_Dungeon_Generation_Algorithm.php
 */
class PhysicsDungeonGenerator : DungeonGenerator {
    
    /** Box2d shit */
    val TIME_STEP = 1/30f
    var VELOCITY_ITERATIONS = 6
    var POSITION_ITERATIONS = 2
    
    /** The gravity vector for our box2d world. */
    var GRAVITY = Vector2(0f, 0f)
    
    /** The number of 'bodies' to initially create. Must be equal or greater than 1 */
    var NUM_BODIES = 10
    
    /** The radius that decides where our bodies will be placed in our physics world */
    var PLACEMENT_RADIUS = 200f
    
    /** Origin point for our spawn circle */
    var ORIGIN_X = 0f
    var ORIGIN_Y = 0f
    
    val world: World = World(GRAVITY, true)
    val dungeon: Dungeon = Dungeon()
    val random: Random = Random()
    var accumulator = 0f
    
    init {
        (1..NUM_BODIES).forEach {
            // Create our body in the world using our body definition
            val body = world.createBody(createRectBodyDef())
    
            // Create a circle shape and set its radius to 6
            val square = PolygonShape()
            square.setAsBox(50f, 50f)
    
            // Create a fixture definition to apply our shape to
            val fixtureDef = FixtureDef()
            fixtureDef.shape = square
            fixtureDef.density = 0.8f
            fixtureDef.friction = 0.4f
            fixtureDef.restitution = 0.6f // Make it bounce a little bit
    
            // Create our fixture and attach it to the body
            body.createFixture(fixtureDef)
    
            // Remember to dispose of any shapes after you're done with them!
            // BodyDef and FixtureDef don't need disposing, but shapes do.
            square.dispose()
        }
    }
    
    override fun generate(): Dungeon {
        return dungeon
    }
    
    fun update(delta: Float) {
        Gdx.app.log("DungeonGenerator", "$delta")
        PhysicsDungeonRenderer.render(dungeon, world)
        
        // Update
        accumulator += delta
        if(accumulator > TIME_STEP) {
            Gdx.app.log("DungeonGenerator", "Physics update")
            // Render
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS)
            accumulator -= TIME_STEP
        }
    }
    
    fun createRectBodyDef(): BodyDef {
        
        // First we create a body definition
        val bodyDef = BodyDef()
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody
        // Set our body's starting position in the world
        val randomSpawnPoint = randomSpawnPoint()
        bodyDef.position.set(randomSpawnPoint)
        bodyDef.fixedRotation = true
        
        return bodyDef
    }
    
    fun randomSpawnPoint(): Vector2 {
        val angle = random.nextDouble() * Math.PI * 2
        val radius = random.nextDouble() * PLACEMENT_RADIUS
        val x = ORIGIN_X + radius * Math.cos(angle)
        val y = ORIGIN_Y + radius * Math.sin(angle)
        return Vector2(x.toFloat(), y.toFloat())
    }
}