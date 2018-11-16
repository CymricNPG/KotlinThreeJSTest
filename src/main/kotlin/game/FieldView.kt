class FieldView(val xsize: Int, val ysize: Int, val origin: Field) {
    val tileMap: MutableList<MutableList<TileType>> = mutableListOf()

    fun makeView(yPos: Int) {
        tileMap.clear()
        for (y in 0..ysize) {
            val row = mutableListOf<TileType>()
            for (x in 0..xsize) {

                val worldY = y + yPos
                val tile = origin.get(x, worldY)
                row.add(tile)
            }
            tileMap.add(row)
        }
    }


    fun get(x: Int, y: Int): TileType {
        return tileMap[y][x];
    }
}
