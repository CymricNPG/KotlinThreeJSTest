import kotlin.js.Math

class EndlessField(val xsize:Int) : Field {
    val ysize = 1000
    val tileMap: MutableList<List<TileType>> = mutableListOf()

    init {
        for (y in 0..ysize) {
            val row = mutableListOf<TileType>()
            for (x in 0..xsize) {
                val tile = TileType.values()[(Math.random() * TileType.values().size).toInt()]
                row.add(tile)
            }
            tileMap.add(row)
        }
    }

    override fun get(x: Int, y: Int): TileType {
        val ypos = y.rem(ysize)
        return tileMap[ypos][x];
    }

}