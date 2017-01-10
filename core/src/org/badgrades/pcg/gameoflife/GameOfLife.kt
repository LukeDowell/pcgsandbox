package org.badgrades.pcg.gameoflife

import com.badlogic.gdx.Gdx

/**
 * Manager for GameOfLife
 *
 * The first generation is created by applying the above rules simultaneously to every cell in the
 * seed—births and deaths occur simultaneously, and the discrete moment at which this happens is
 * sometimes called a tick (in other words, each generation is a pure function of the preceding one).
 * The rules continue to be applied repeatedly to create further generations.
 */
class GameOfLife {
    
    val UPDATE_PERIOD = 0.5f
    
    var timeSinceLastUpdate = 0f
    var running = false
    
    /* The live map that is rendered */
    val gridMap: GridMap = GridMap(
            Math.round(Gdx.graphics.width / GridMap.TILE_SIZE),
            Math.round(Gdx.graphics.height / GridMap.TILE_SIZE)
    )
    
    /* The map we use to temporarily store cell values for the next tick */
    val tempMap: GridMap = GridMap(
            Math.round(Gdx.graphics.width / GridMap.TILE_SIZE),
            Math.round(Gdx.graphics.height / GridMap.TILE_SIZE)
    )
    
    fun update(delta: Float) {
        timeSinceLastUpdate += delta
        if (timeSinceLastUpdate > UPDATE_PERIOD && running) {
           timeSinceLastUpdate = 0f
            
            // Calculate new values
            for(x in 0 until gridMap.grid.size)
                for(y in 0 until gridMap.grid[0].size)
                    tempMap.grid[x][y] = calculateStateAtPoint(x, y)
    
            // Copy new values over to main grid map
            for(x in 0 until gridMap.grid.size)
                for(y in 0 until gridMap.grid[0].size)
                    gridMap.grid[x][y] = tempMap.grid[x][y]
        }
    }
    
    /**
     * Every cell interacts with its eight neighbours, which are the cells that are horizontally,
     * vertically, or diagonally adjacent. At each step in time, the following transitions occur:
     *
     * Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.
     * Any live cell with two or three live neighbours lives on to the next generation.
     * Any live cell with more than three live neighbours dies, as if by overpopulation.
     * Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
     *
     */
    fun calculateStateAtPoint(x: Int, y: Int) : Int {
        val origin = gridMap.grid[x][y]
        val numNeighbors = getNumLiveNeighbors(x, y)
        
        if (origin == GridMap.ALIVE) {
            
            if (numNeighbors < 2)
                return GridMap.DEAD
            
            if (numNeighbors == 2 || numNeighbors == 3)
                return GridMap.ALIVE
            
            if (numNeighbors > 3)
                return GridMap.DEAD
            
        } else if (numNeighbors == 3) {
            return GridMap.ALIVE
        }
        
        return GridMap.DEAD
    }
    
    /**
     * Returns the number of live neighbors a given cell has
     */
    fun getNumLiveNeighbors(x: Int, y: Int) : Int {
        var numNeighbors = 0
        
        for (xOffset in -1..1) {
            for (yOffset in -1..1) {
                // These variable names are trash
                val xDelta = x + xOffset
                val yDelta = y + yOffset
                
                // If we are in the center of our square, skip and move on
                if(xDelta == x && yDelta == y) {
                    continue
                }
                
                // Make sure we are in the bounds of the map, and that the cell we are checking is alive
                if (xDelta > 0 &&
                        yDelta > 0 &&
                        xDelta < gridMap.grid.size &&
                        yDelta < gridMap.grid[0].size &&
                        gridMap.grid[xDelta][yDelta] == GridMap.ALIVE) {
                    numNeighbors++
                }
            }
        }
        
        return numNeighbors
    }
}