package org.badgrades.pcg.gameoflife

/**
 * The map array is wrapped so that later if we want our arrays to be
 * toroidal we can have it be so
 */
class GridMap(val width: Int, val height: Int) {
    
    val grid: Array<IntArray> = Array(width, { IntArray(height) })
    
    companion object {
        const val TILE_SIZE = 32f // Do display units belong here?
        const val DEAD = 0
        const val ALIVE = 1
    }
}