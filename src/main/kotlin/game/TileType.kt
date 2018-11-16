enum class TileType(val rgb: Int) {

    NOTIME(0x777777),
    NORMAL1(0xa48662),
    NORMAL2(0xFFA500),
    SLOW(0xFF0000),
    EMPTY(0x000000),
    JUMP(0x0000ff),
    STOP(0x800080),
    SWAP(0x00FFFF);

    val light: Int = rgb
    val dark: Int = rgb and 0xfefefe shr 1


}
