package nupa.aoc23.input

class CharacterMap(val rows: List<String>) {

    val rowLength = rows[0].length

    fun walkthrough(cb: (value: Char, x: Int, y: Int, rowEnd: Boolean) -> Unit) {
        for (j in rows.indices) {
            for (i in (0 ..< rowLength)) {
                cb(rows[j][i], i, j, i == rowLength - 1)
            }
        }
    }

    companion object {
        fun fromFile(file: String): CharacterMap {
            val lines = lines(file)
            val firstRowLength = lines[0].length
            if (lines.any { it.length != firstRowLength })
                error("Invalid map input: all rows must be the same")
            return CharacterMap(lines)
        }
    }
}