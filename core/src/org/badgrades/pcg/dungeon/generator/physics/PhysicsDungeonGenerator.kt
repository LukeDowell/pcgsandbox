package org.badgrades.pcg.dungeon.generator.physics

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import org.badgrades.pcg.dungeon.generator.DungeonGenerator
import org.badgrades.pcg.dungeon.model.Dungeon

/**
 * http://www.gamasutra.com/blogs/AAdonaac/20150903/252889/Procedural_Dungeon_Generation_Algorithm.php
 */
class PhysicsDungeonGenerator : DungeonGenerator {
    
    val world: World = World(Vector2(0f, -10f), true)
    
    override fun generate(): Dungeon {
        return Dungeon()
    }
}