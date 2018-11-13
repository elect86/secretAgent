package secretAgent.io

import java.io.Serializable

/**
 * Settings for files and paths.
 *
 * @author Ondrej Milenovsky
 */
class FileSettings : Serializable {

    lateinit var dataDir: String
    lateinit var saveDir: String
    lateinit var campaignsDir: String
    lateinit var campaignExt: String
    lateinit var tilesDefExt: String
    lateinit var mapOrigExt: String
    lateinit var mapWaExt: String

    lateinit var defaultGraphicsFile: String

    @JvmField
    var playerKeysFile: String? = null
    @JvmField
    var menuKeysFile: String? = null
    lateinit var settings2dFile: String
    lateinit var gameSettingsFile: String

//    companion object {
//        private const val serialVersionUID = -6445771138226370705L
//    }
}