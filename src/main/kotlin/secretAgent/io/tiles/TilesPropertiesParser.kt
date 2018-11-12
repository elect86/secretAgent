package secretAgent.io.tiles

import cz.wa.secretagent.Constants
import cz.wa.secretagent.world.map.TileType
import cz.wa.wautils.io.AbstractParser
import cz.wa.wautils.math.Vector2I
import cz.wa.wautils.string.StringUtilsWa
import secretAgent.view.renderer.TileId
import secretAgent.world.ModelType
import java.io.File
import java.io.IOException
import java.util.ArrayList
import java.util.HashMap

/**
 * Parses tiles info file. For each tile id there is tile info,
 * that contains model and map tile or entity definition.
 *
 * @author Ondrej Milenovsky
 */
class TilesPropertiesParser(private val file: File, private val tileSetId: Int) : AbstractParser(Constants.ENCODING) {
    private var tiles: MutableMap<TileId, TileProperties>? = null
    private var models: MutableMap<String, ModelProperties>? = null
    private var tileSize: Vector2I? = null

    private var currModel: ModelProperties? = null

    @Throws(IOException::class)
    fun parse(onlyModels: Boolean): TilesProperties {
        tiles = HashMap()
        models = HashMap()
        tileSize = null
        startParsing(file)

        if (currModel != null) {
            warn("Not closed model definition: " + currModel!!.name)
            closeCurrModel()
        }

        if (!onlyModels && tileSize == null) {
            warn("Missing tile size definition, used default 16*16")
            tileSize = DEFAULT_TILE_SIZE
        }
        return TilesProperties(tileSize, tiles!!, models!!)
    }

    override fun parseLine(line_: String): Boolean {
        var line = line_
        line = line.trim()
        // remove comment
        val cInd = line.indexOf(COMMENT_START)
        if (cInd >= 0)
            line = line.substring(0, cInd)
        if (line.isEmpty())
            return true

        // process rest
        when {
            line.startsWith(TILE_SIZE_TEXT) -> {
                if (tileSize != null)
                    warn("Duplicate tile size definition")
                tileSize = parseTileSize(line)
            }
            Character.isDigit(line[0]) -> parseTiles(line)
            else -> parseModel(line)
        }

        return true
    }

    private fun parseModel(line_: String) {
        var line = line_
        if (currModel == null) {
            if (line.endsWith(MODEL_DEF_START)) {
                line = line.substring(0, line.length - MODEL_DEF_START.length).trim()
                startCurrModel(line)
            } else
                warn("Model definition must end with $MODEL_DEF_START")
        } else {
            if (line == MODEL_DEF_END)
                closeCurrModel()
            else
                parseModelProperty(line)
        }
    }

    private fun startCurrModel(line: String) {
        val ind = line.indexOf(MODEL_TYPE_SEP)
        if (ind < 0) {
            warn("Model definition must consist of name and type separated by: $MODEL_TYPE_SEP")
            currModel = ModelProperties(line, null)
            return
        }
        val name = line.substring(0, ind).trim()
        val typeStr = line.substring(ind + 1).trim()
        var type: ModelType? = null
        try {
            type = ModelType.valueOf(typeStr)
        } catch (e: IllegalArgumentException) {
            error("Wrong model type: $typeStr", e)
        }

        currModel = ModelProperties(name, type)
    }

    private fun parseModelProperty(line: String) {
        val ind = line.indexOf(MODEL_PROPERTY_SEP)
        if (ind < 0) {
            warn("No property definition, missing $MODEL_PROPERTY_SEP")
            return
        }
        val name = line.substring(0, ind).trim()
        val value = line.substring(ind + 1).trim()
        if (currModel!!.properties.containsKey(name)) {
            warn("Duplicate model '${currModel!!.name}' property: $name")
        }
        val ids = parseIds(value)
        if (ids != null && ids.isNotEmpty())
            currModel!!.properties[name] = ids
    }

    private fun closeCurrModel() {
        models!![currModel!!.name] = currModel!!
        currModel = null
    }

    private fun parseTiles(line: String) {
        // tile type
        var tileType: TileType? = null
        var beginTypeI = -1
        var endTypeI = -1
        for (i in 0..line.length) {
            if (beginTypeI < 0) {
                if (i < line.length && isEnumChar(line[i]))
                    beginTypeI = i
            } else if (i == line.length || !isEnumChar(line[i])) {
                endTypeI = i
                val typeStr = line.substring(beginTypeI, i)
                try {
                    tileType = TileType.valueOf(typeStr)
                    break
                } catch (e: IllegalArgumentException) {
                    warn("Unknown tile type: '$typeStr'")
                    return
                }

            }
        }
        if (tileType == null) {
            warn("Tile type not specified")
            return
        }

        // model
        var modelRef: String? = line.substring(endTypeI).trim()

        // there could be entity, cut the definition
        val ind = modelRef!!.indexOf(ENTITY_DEF_END)
        if (ind > 0)
            modelRef = modelRef.substring(ind + 1).trim()

        if (modelRef.isEmpty())
            modelRef = null

        // tile ids
        val tileIds = line.substring(0, beginTypeI).trim()
        var tilePr: TileProperties? = null
        if (tileType == TileType.ENTITY) {
            val entityPr = parseEntity(line.substring(endTypeI))
            if (entityPr != null)
                tilePr = TileProperties(entityPr, modelRef)
        } else {
            tilePr = TileProperties(tileType, modelRef)
        }

        // store
        if (tilePr != null) {
            for (tileId in parseIds(tileIds)!!) {
                if (tileId.tileSetId == tileSetId)
                    putTileInfo(tileId, tilePr)
                else
                    warn("Definition tile id must be from this set: $tileId")
            }
        }
    }

    private fun parseEntity(line_: String): EntityProperties? {
        var line = line_
        // separate arguments
        val ind1 = line.indexOf(ENTITY_DEF_START)
        val ind2 = line.indexOf(ENTITY_DEF_END)
        if (ind1 < 0) {
            warn("Missing entity arguments opening  character: $ENTITY_DEF_START")
            return null
        }
        if (ind2 < 0) {
            warn("Missing entity arguments closing character: $ENTITY_DEF_END")
            return null
        }
        line = line.substring(ind1 + 1, ind2).trim()

        // parse arguments
        val args = StringUtilsWa.split(line, ENTITY_ARG_SEP)
        for (i in args.indices)
            args[i] = args[i].trim()
        return EntityProperties(args)
    }

    private fun isEnumChar(c: Char): Boolean = c == '_' || !Character.isWhitespace(c) && Character.isUpperCase(c)

    private fun parseIds(str: String): List<TileId>? {
        // find second -
        val rangeInd = str.indexOf(ID_RANGE_SEP, 1)
        // parse ids
        return if (str.indexOf(ID_LIST_SEP) < 0 && rangeInd > 0)
            parseRangeIds(str, rangeInd)
        else
            parseListIds(str)
    }

    private fun parseListIds(str: String): List<TileId> {
        val ret = ArrayList<TileId>()
        for (idStr in str.split(ID_LIST_SEP_REGEX))
            if (idStr.isNotEmpty())
                ret += parseTileId(idStr)!!
        return ret
    }

    private fun parseRangeIds(str: String, rangeSepInd: Int): List<TileId> {
        val num1 = str.substring(0, rangeSepInd).trim()
        val num2 = str.substring(rangeSepInd + 1).trim()
        val ret = ArrayList<TileId>()
        // parse range
        val id1 = parseTileId(num1)
        val id2 = parseTileId(num2)
        if (id1!!.tileSetId != id2!!.tileSetId)
            warn("Both tile ids from range must have same tile set id: $id1, $id2")
        else // process
            for (i in id1.tileId..id2.tileId)
                ret += TileId(id1.tileSetId, i)
        return ret
    }

    private fun parseTileId(str: String): TileId? {
        try {
            var ind = str.indexOf(ID_SEP)
            if (ind < 0)
                ind = str.indexOf(ID_SEP_ALT)
            if (ind >= 0)
                return TileId.from(str)
            else {
                val id = Integer.decode(str)!!
                if (id >= 0)
                    return TileId(tileSetId, id)
                else
                    warn("Tile id must be >= 0: $id")
            }
        } catch (e: IllegalArgumentException) {
            error("Wrong id format: $str", e)
        }
        return null
    }

    private fun putTileInfo(tileId: TileId, tileInfo: TileProperties) {
        if (tiles!!.put(tileId, tileInfo) != null)
            warn("Duplicate tile id, rewriting: $tileId")
    }

    private fun parseTileSize(line: String): Vector2I? {
        val s = line.substring(TILE_SIZE_TEXT.length)
        val ind = s.indexOf(TILE_SIZE_SEP)
        if (ind < 0) {
            warn("Wrong tile size definition, missing '$TILE_SIZE_SEP': $s")
            return null
        }
        val num1 = s.substring(0, ind).trim()
        val num2 = s.substring(ind + 1).trim()
        try {
            val x = Integer.parseInt(num1)
            val y = Integer.parseInt(num2)
            if (x <= 0 || y <= 0) {
                warn("Tile size must be > 0: $s")
                return null
            }
            return Vector2I(x, y)
        } catch (e: NumberFormatException) {
            error("Wrong number format: $s", e)
            return null
        }

    }

    companion object {
        private const val COMMENT_START = "//"

        private const val TILE_SIZE_TEXT = "tileSize"
        private const val TILE_SIZE_SEP = '*'

        private val ID_LIST_SEP_REGEX = Regex("[\\s,]+")
        private const val ID_LIST_SEP = ','
        private const val ID_RANGE_SEP = '-'
        private const val ID_SEP = '*'
        private const val ID_SEP_ALT = '.'

        private const val MODEL_DEF_START = "{"
        private const val MODEL_DEF_END = "}"
        private const val MODEL_TYPE_SEP = '.'
        private const val MODEL_PROPERTY_SEP = '='

        private const val ENTITY_DEF_START = '('
        private const val ENTITY_DEF_END = ')'
        private const val ENTITY_ARG_SEP = ","

        val DEFAULT_TILE_SIZE = Vector2I(16, 16)
    }
}