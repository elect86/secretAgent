package secretAgent.io.map.orig

import cz.wa.wautils.math.Vector2I
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.util.ArrayList

/**
 * Parses map in format .sam produced by Camoto Studio.
 * By default the map size is 40x24 but other size can be defined (it is not determined automatically).
 * If the first line contains string 'size', then the next two separate numbers
 * are taken as size x and y (excluding the last character on line).
 *
 * No tile mapping is performed, the output must be mapped and processed.
 *
 * @author Ondrej Milenovsky
 */
class MapParser(private val input: File) {
    private var tiles: Array<IntArray>? = null
    private var items: Array<IntArray>? = null
    private var currLine: Int = 0

    @Throws(IOException::class)
    fun parse(): MapLevel {
        val lines = splitToLines(FileUtils.readFileToByteArray(input))

        // level size
        var sizeX = SIZE_X
        var sizeY = SIZE_Y
        val newSize = parseMapSize(lines[0])
        if (newSize != null) {
            sizeX = newSize.x
            sizeY = newSize.y
        }

        // background
        val bgTile = parseBgTile(lines[0])
        val bgTileOver = parseBgTileOver(lines[1])

        // offsets
        val offsets = parseOffsets(lines[1])

        //map init
        tiles = Array(sizeX) { IntArray(sizeY) }
        items = Array(sizeX) { IntArray(sizeY) }
        for (y in 0 until sizeY) {
            for (x in 0 until sizeX) {
                tiles!![x][y] = 0x20
                items!![x][y] = 0x20
            }
        }
        // map parsing
        currLine = -1
        for (i in 2 until lines.size)
            processLine(lines[i])

        return MapLevel(Vector2I(sizeX, sizeY), bgTile, bgTileOver, tiles!!, items!!, offsets)
    }

    private fun parseMapSize(bytes: Array<Byte?>): Vector2I? {
        val chars = CharArray(bytes.size)
        for (i in bytes.indices) {
            chars[i] = bytes[i]?.toChar() ?: 0.toChar()
        }
        var line = String(chars)
        val ind = line.indexOf(SIZE_START)
        if (ind >= 0) {
            var sizeX = -1
            val sizeY: Int
            line = line.substring(ind, line.length - 2)
            for (str in line.split(Regex("[^0-9]+"))) {
                if (str.isNotEmpty()) {
                    val a = Integer.parseInt(str)
                    if (a <= 4) {
                        logger.warn("Size must be at least 4x4")
                        return null
                    }
                    if (sizeX < 0)
                        sizeX = a
                    else {
                        sizeY = a
                        return Vector2I(sizeX, sizeY)
                    }
                }
            }
        }
        return null
    }

    private fun parseOffsets(bytes: Array<Byte?>): IntArray {
        val offsets = IntArray(37)
        for (i in 0..36)
            offsets[i] = bytes[i + 3]!!.toInt()
        return offsets
    }

    private fun parseBgTileOver(bytes: Array<Byte?>): Int = bytes[1]!!.toInt()

    private fun processLine(bytes: Array<Byte?>) {
        if (bytes[0] == null)
            return

        val array: Array<IntArray>?
        if (bytes[0]!! == 0x2a.toByte())
            // line stars with *, overlay line
            array = items
        else {
            // normal line
            array = tiles
            currLine++
            if (currLine >= SIZE_Y)
                return
        }

        // process the line
        for (i in bytes.indices) {
            var v = bytes[i]!!.toInt()
            if (v < 0)
                v += 256
            array!![i][currLine] = v
        }
    }

    private fun parseBgTile(bytes: Array<Byte?>): Int {
        var bgTile = 0
        for (i in 0..7) {
            if (Character.isDigit(bytes[i]!!.toInt())) {
                bgTile *= 10
                bgTile += bytes[i]!! - 48
            } else
                break
        }
        return bgTile
    }

    private fun splitToLines(bytes: ByteArray): List<Array<Byte?>> {
        val lines = ArrayList<Array<Byte?>>(SIZE_Y)
        lines.add(arrayOfNulls(SIZE_X))
        var lastLine = lines[0]
        var off = 0
        var i = 0
        while (i < bytes.size) {
            if (bytes[i].toInt() == 0x0d && bytes[i + 1].toInt() == 0x0a || off >= lastLine.size) {
                lastLine = arrayOfNulls(SIZE_X)
                lines += lastLine
                i++
                off = 0
            } else {
                lastLine[off] = bytes[i]
                off++
            }
            i++
        }
        return lines
    }

    companion object {

        private val logger = LoggerFactory.getLogger(MapParser::class.java)

        const val SIZE_X = 40
        const val SIZE_Y = 24

        private const val SIZE_START = "size"
    }
}