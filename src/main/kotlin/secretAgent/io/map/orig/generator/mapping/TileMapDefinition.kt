package secretAgent.io.map.orig.generator.mapping

/**
 * Tile maps taken from Camoto studio (fmt-map-sagent-mapping.hpp, fmt-map-sagent.cpp).
 *
 * @author Adam Nielsen
 * @author Ondrej Milenovsky
 */
object TileMapDefinition {

    @get:SuppressWarnings("serial")
    val bgMap: Map<Int, Int>
        get() = mapOf(
                -1 to st(6, 16),
                209 to st(11, 8),
                213 to st(11, 12),
                217 to st(11, 16),
                233 to st(11, 32),
                237 to st(11, 36),
                241 to st(11, 40),
                245 to st(11, 44),
                325 to st(9, 24),
                329 to st(9, 28),
                333 to st(9, 32),
                337 to st(9, 36),
                341 to st(9, 40),
                501 to st(1, 0),
                667 to st(6, 16),
                695 to st(6, 44),
                767 to st(8, 16),
                771 to st(8, 20))

    val islandMap: Array<Pair<Int, IntArray>>
        get() = arrayOf(
                48 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 24)),
                66 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 1)),
                67 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 2)),
                68 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 3)),
                69 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 4)),
                70 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 5)),
                71 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 6)),
                77 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 12)),
                78 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 13)),
                79 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 14)),
                80 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 15)),
                85 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 20)),
                86 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 21)),
                87 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 22)),
                88 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 23)),
                89 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(13, 0)),
                90 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 25)),
                97 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 26)),
                98 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 27)),
                99 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 28)),
                100 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 29)),
                101 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 30)),
                102 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 31)),
                103 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 32)),
                104 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 33)),
                108 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(1, 7)),
                119 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, st(10, 28), -1, -1, -1, st(10, 32)))

    val tileMap: Array<Pair<Int, IntArray>>
        get() = arrayOf(
                1 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 18)),
                2 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 10)),
                3 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 11)),
                4 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 4)),
                5 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 5), st(10, 6)),
                6 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 7)),
                7 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 23)),
                8 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 30), st(10, 31)),
                11 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 27)),
                12 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 34), st(10, 35)),
                14 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 32)),
                15 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 33)),
                16 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 34)),
                17 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 36)),
                18 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 40)),
                19 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 42)),
                20 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, st(7, 44), -1, -1, -1, st(7, 48)),
                21 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 45)),
                22 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 49)),
                23 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 26)),
                24 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 27)),
                25 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, st(15, 0), -1, -1, -1, st(15, 4)),
                28 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, st(15, 3), -1, -1, -1, st(15, 7)),
                31 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 15)),
                33 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 0)),
                35 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 0)),
                36 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, st(2, 40), -1, -1, -1, st(2, 44)),
                37 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 1), st(4, 2)),
                27 to intArrayOf(-1, -1, -1, -1, -1, -1, st(15, 1), st(15, 2), -1, -1, st(15, 5), st(15, 6)),
                38 to intArrayOf(-1, -1, -1, -1, -1, -1, st(4, 4), st(4, 5), -1, -1, st(4, 8), st(4, 9)),
                40 to intArrayOf(-1, -1, -1, -1, -1, -1, st(4, 6), st(4, 7), -1, -1, st(4, 10), st(4, 11)),
                41 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 12)),
                // 42 only applies when x > 0
                42 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 16)),
                43 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 20)),
                44 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 21)),
                45 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 22)),
                46 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 23)),
                47 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 24)),
                48 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(14, 16)),
                52 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 25)),
                56 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(14, 0)),
                57 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(14, 8)),
                58 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 15)),
                59 to intArrayOf(-1, -1, -1, st(3, 27), -1, -1, -1, st(3, 28), -1, -1, -1, st(3, 26)),
                60 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 36)),
                61 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 40)),
                62 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 32), st(3, 36), st(3, 33)),
                63 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 24)),
                64 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, st(9, 4), -1, -1, -1, st(9, 1)),
                65 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 32)),
                66 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 2)),
                67 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 4)),
                68 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 12)),
                69 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 13)),
                70 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 5)),
                71 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(14, 32)),
                72 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 17)),
                74 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 6)),
                75 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 5)),
                76 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 38)),
                77 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 41)),
                78 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 12)),
                79 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 14)),
                80 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 3)),
                81 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 14)),
                82 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 13)),
                83 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 39)),
                84 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 15)),
                85 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 15)),
                86 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, st(12, 0), -1, -1, -1, st(12, 4)),
                87 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 17)),
                88 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, st(12, 31), -1, -1, -1, st(12, 35)),
                89 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(13, 0)),
                90 to intArrayOf(-1, -1, -1, -1, -1, -1, st(8, 44), st(8, 45), -1, -1, st(8, 48), st(8, 49)),
                91 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 18)),
                92 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 19)),
                93 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 28)),
                94 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 13)),
                95 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 46)),
                96 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 48)),
                97 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(4, 49)),
                98 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 25)),
                99 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(12, 40)),
                100 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 29)),
                101 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(2, 16)),
                102 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 30)),
                103 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(14, 24)),
                104 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 31)),
                105 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 40)),
                106 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 41)),
                107 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 42)),
                108 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 43)),
                109 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 44)),
                110 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(2, 32)),
                111 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 16)),
                112 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 8), st(10, 9)),
                113 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, st(5, 32), -1, -1, -1, st(5, 36)),
                114 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 6)),
                115 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 0)),
                116 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 34)),
                117 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(2, 8)),
                118 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(2, 12)),
                119 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, st(10, 28), -1, -1, -1, st(10, 32)),
                120 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(15, 8)),
                121 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 17), st(9, 18)),
                122 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 16)),
                124 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 18)),
                126 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 19)),
                127 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 8)),
                128 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 0)),
                129 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 1)),
                130 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 2)),
                131 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 3)),
                132 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 4)),
                133 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, st(11, 20), st(11, 21), st(11, 22), st(11, 23)),
                134 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, st(11, 28), st(11, 29), st(11, 30)),
                135 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 22), st(9, 23)),
                136 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 44)),
                137 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 45)),
                138 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 46)),
                139 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 12), st(10, 13)),
                140 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 20)),
                141 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 21)),
                142 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 22)),
                143 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 0)),
                144 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 1)),
                145 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 2)),
                146 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 3)),
                147 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 4)),
                148 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 5)),
                149 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(3, 48)),
                150 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 7)),
                151 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 8)),
                152 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 9)),
                153 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 10)),
                154 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 15), st(10, 14)),
                155 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 12)),
                156 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 13)),
                157 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 14)),
                158 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 15)),
                159 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 40)),
                160 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 41)),
                161 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 42)),
                162 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 47)),
                163 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 20)),
                164 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 21)),
                165 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 22)),
                166 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 23)),
                167 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 24)),
                168 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 24)),
                169 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 25)),
                170 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 26)),
                171 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, st(10, 43), -1, -1, -1, st(10, 47)),
                172 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 46)),
                173 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 42)),
                174 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(0, 0), st(0, 4)),
                175 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 48)),
                176 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 33)),
                177 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 34)),
                178 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 35)),
                179 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 36)),
                180 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 37)),
                181 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 38)),
                182 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 30)),
                183 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 40)),
                184 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 41)),
                185 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 42)),
                186 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 39), st(6, 43)),
                187 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 44)),
                188 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 47)),
                189 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 45)),
                190 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 46)),
                191 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 28), st(6, 29)),
                192 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(6, 48), st(6, 49)),
                193 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 0)),
                194 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 1)),
                195 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 2)),
                196 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 3)),
                197 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 4)),
                198 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 5)),
                199 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 6)),
                200 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 7)),
                201 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 8)),
                202 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 9)),
                203 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 10)),
                204 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 11)),
                205 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 12)),
                206 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 13)),
                207 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 14)),
                208 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 15)),
                209 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 16)),
                210 to intArrayOf(-1, -1, -1, -1, -1, -1, st(10, 40), st(10, 41), -1, -1, st(10, 44), st(10, 45)),
                211 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(5, 7)),
                212 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(9, 8)),
                213 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 20)),
                214 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 21)),
                215 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 22)),
                216 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(15, 17), st(15, 18)),
                217 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 24)),
                218 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 25)),
                219 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 26)),
                220 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 27)),
                221 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 28)),
                222 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(7, 29)),
                223 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(11, 0)),
                224 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(11, 1)),
                225 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(11, 2)),
                226 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(11, 4)),
                227 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(11, 5)),
                228 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(11, 6)),
                229 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 7)),
                230 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 8)),
                231 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 9)),
                232 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 10)),
                233 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 11)),
                234 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 12)),
                235 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 13)),
                236 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 14)),
                237 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(10, 48), st(10, 49)),
                238 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, st(15, 13), st(15, 14), st(15, 15)),
                239 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, st(15, 12), -1, -1, -1, st(15, 16)),
                241 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 24)),
                242 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 25)),
                243 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 26)),
                244 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 27)),
                245 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 28)),
                246 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 29)),
                247 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 30)),
                248 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 31)),
                249 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 32)),
                250 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 33)),
                251 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 34)),
                252 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 35)),
                253 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 36)),
                254 to intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, st(8, 37)))

    fun st(a: Int, b: Int) = a * 50 + b
}