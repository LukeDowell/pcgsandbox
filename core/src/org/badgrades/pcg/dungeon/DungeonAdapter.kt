package org.badgrades.pcg.dungeon

import com.badlogic.gdx.ApplicationAdapter
import org.badgrades.pcg.dungeon.generator.physics.PhysicsDungeonGenerator
import org.badgrades.pcg.dungeon.model.Dungeon

class DungeonAdapter : ApplicationAdapter() {
    
    val physicsDungeonGenerator = PhysicsDungeonGenerator()
    val dungeon: Dungeon? = null
    
    override fun create() {
        
    }
    
    override fun render() {
        
    }
}