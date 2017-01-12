package org.badgrades.pcg.dungeon.generator.physics

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import org.badgrades.pcg.dungeon.model.Dungeon

/**
 * Utility object containing conversion-based functions for turning a physics world into a
 * tile based dungeon
 */
object PhysicsWorldToDungeonConverter {
    
    /**
     * Attempts to convert a box2d world into a dungeon world
     */
    fun convert(world: World): Dungeon {
        val dungeon = Dungeon()
        /*
         * Potential steps:
         * 1. Find 'node' bodies, ie bodies whose sides are over a certain threshold
         */
        val bodies: Array<Body> = Array()
        world.getBodies(bodies)
        
        bodies[0].
        return dungeon
    }
    
    /**
     * Whether or not a given physics body is suitable for being a node of the dungeon
     */
    fun isBodyOverNodeThreshold(): Boolean {
        
    }
}