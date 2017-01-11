package org.badgrades.pcg.dungeon.generator.physics

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
    val TIME_STEP = 1/45f
    var VELOCITY_ITERATIONS = 6
    var POSITION_ITERATIONS = 2
    
    var BODY_DENSITY = 0.2f
    var BODY_FRICTION =  0f
    var BODY_RESTITUTION = 0f
    
    /** The gravity vector for our box2d world. */
    var GRAVITY = Vector2(0f, 0f)
    
    /** The number of 'bodies' to initially create. Must be equal or greater than 1 */
    var NUM_BODIES = 15
    
    /** The radius that decides where our bodies will be placed in our physics world */
    var PLACEMENT_RADIUS = 100f
    
    /** Origin point for our spawn circle */
    var ORIGIN_X = 0f
    var ORIGIN_Y = 0f
    
    /** The min and max sizes for a given side of a room */
    var MIN_BOX_SIZE = 10f
    var MAX_BOX_SIZE = 55f
    
    val world: World = World(GRAVITY, true)
    val dungeon: Dungeon = Dungeon()
    val random: Random = Random()
    var accumulator = 0f
    
    init {
        (1..NUM_BODIES).forEach {
            // Create our body in the world using our body definition
            val body = world.createBody(createRectBodyDef())
    
            // Create a circle shape and set its radius to 6
            val square = randomPolygonShape()
    
            // Create a fixture definition to apply our shape to
            val fixtureDef = FixtureDef()
            fixtureDef.shape = square
            fixtureDef.density = BODY_DENSITY
            fixtureDef.friction = BODY_FRICTION
            fixtureDef.restitution = BODY_RESTITUTION
    
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
        accumulator += delta
        if(accumulator > TIME_STEP) {
            // Render
            PhysicsDungeonRenderer.render(dungeon, world)
            
            // Step
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS)
            accumulator -= TIME_STEP
        }
    }
    
    fun createRectBodyDef(): BodyDef {
        val bodyDef = BodyDef()
        val randomSpawnPoint = randomSpawnPoint()
        
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(randomSpawnPoint)
        bodyDef.fixedRotation = true
        
        return bodyDef
    }
    
    fun randomPolygonShape(): PolygonShape {
        val shape = PolygonShape()
        shape.setAsBox(
                randomPolygonSideLength(),
                randomPolygonSideLength()
        )
        return shape
    }
    
    fun randomPolygonSideLength() = random.nextInt((MAX_BOX_SIZE.toInt() - MIN_BOX_SIZE.toInt()) + 1) + MIN_BOX_SIZE // This is lol
    
    fun randomSpawnPoint(): Vector2 {
        val angle = random.nextDouble() * Math.PI * 2
        val radius = random.nextDouble() * PLACEMENT_RADIUS
        val x = ORIGIN_X + radius * Math.cos(angle)
        val y = ORIGIN_Y + radius * Math.sin(angle)
        return Vector2(x.toFloat(), y.toFloat())
    }
}