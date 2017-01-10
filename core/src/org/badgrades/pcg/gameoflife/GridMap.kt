package org.badgrades.pcg.gameoflife

class GridMap(val width: Int, val height: Int) {
    
    val grid: Array<IntArray> = Array(width, { IntArray(height, { DEAD }) })
    
    companion object {
        const val TILE_SIZE = 32f // Do display units belong here?
        const val DEAD = 0
        const val ALIVE = 1
    }
}