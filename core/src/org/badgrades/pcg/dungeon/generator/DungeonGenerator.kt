package org.badgrades.pcg.dungeon.generator

import org.badgrades.pcg.dungeon.model.Dungeon

interface DungeonGenerator {
    
    fun generate(): Dungeon
}