package org.badgrades.pcg.dungeon.generator.physics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Array
import org.badgrades.pcg.dungeon.generator.DungeonGenerator
import org.badgrades.pcg.dungeon.model.Dungeon
import java.util.*

/**
 * http://www.gamasutra.com/blogs/AAdonaac/20150903/252889/Procedural_Dungeon_Generation_Algorithm.php
 */
class PhysicsDungeonGenerator : DungeonGenerator {
    
    /** Box2d shit */
    var TIME_STEP = 1/45f
    var SLEEP_CHECK_PERIOD = 2f
    var VELOCITY_ITERATIONS = 15
    var POSITION_ITERATIONS = 8
    
    var accumulator = 0f
    var sleepAccumulator = 0f
    
    
    /** Box2d body properties */
    var BODY_DENSITY = 0.2f
    var BODY_FRICTION =  0f
    var BODY_RESTITUTION = 0f // How much it bounces ( ͡° ͜ʖ ͡°)
    
    /** The gravity vector for our box2d world. */
    var GRAVITY = Vector2(0f, 0f)
    
    /** The number of 'bodies' to initially create. Must be equal or greater than 1 */
    var NUM_BODIES = 20
    
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
    
    
    init {
        (1..NUM_BODIES).forEach {
            // Create a body definition with a random position
            val body = world.createBody(createRectBodyDef())
    
            // Create a polygon shape with pseudo random dimensions
            val square = randomPolygonShape()
    
            // Create a fixture definition to apply our shape to
            val fixtureDef = FixtureDef()
            fixtureDef.shape = square
            fixtureDef.density = BODY_DENSITY
            fixtureDef.friction = BODY_FRICTION
            fixtureDef.restitution = BODY_RESTITUTION
    
            // Attach our fixture to the body
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
        PhysicsDungeonRenderer.render(dungeon, world)
        
        // Update world
        accumulator += delta
        if(accumulator > TIME_STEP) {
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS)
            accumulator -= TIME_STEP
        }
        
        // Check to see if all bodies are sleeping
        sleepAccumulator += delta
        if(sleepAccumulator > SLEEP_CHECK_PERIOD) {
            val bodies: Array<Body> = Array()
            world.getBodies(bodies)
            val numBodiesAwake = bodies.filter { it.isAwake }.size
            if(numBodiesAwake == 0) {
                Gdx.app.log("GameOfLife", "All bodies are sleeping!")
                // Convert to dungeon
                
                
            } else {
                Gdx.app.log("GameOfLife", "$numBodiesAwake bodies are still awake!")
            }
            sleepAccumulator -= SLEEP_CHECK_PERIOD
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
    
    fun randomPolygonSideLength() = random.nextInt((MAX_BOX_SIZE.toInt() - MIN_BOX_SIZE.toInt()) + 1) + MIN_BOX_SIZE
    
    fun randomSpawnPoint(): Vector2 {
        val angle = random.nextDouble() * Math.PI * 2
        val radius = random.nextDouble() * PLACEMENT_RADIUS
        val x = ORIGIN_X + radius * Math.cos(angle)
        val y = ORIGIN_Y + radius * Math.sin(angle)
        return Vector2(x.toFloat(), y.toFloat())
    }
}