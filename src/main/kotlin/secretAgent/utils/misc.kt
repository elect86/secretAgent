package secretAgent.utils

import com.martiansoftware.jsap.ParseException
import com.martiansoftware.jsap.stringparsers.ColorStringParser
import cz.wa.wautils.collection.TypedKey
import cz.wa.wautils.io.FileInputStreamNamed
import cz.wa.wautils.io.PropertiesUtils
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import java.awt.Color
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URISyntaxException

/**
 * Utils for color.
* Parses color in format #RRGGBB or #RRGGBBAA
*/
fun String.parseColor(): Color =
        try {
            ColorStringParser.getParser().parse(this) as Color
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }

/**
 * Utils to easily load app context
 *
 * @author Ondrej Milenovsky
 */
class ContextWrapper(private val context: ApplicationContext) {

    fun <C> getBean(key: TypedKey<C>): C = getBean(context, key)

    fun containsBean(key: TypedKey<*>): Boolean = context.containsBean(key.key)

    fun <C> getBeans(clazz: Class<C>): Map<String, C> = context.getBeansOfType(clazz)

    companion object {
        fun <C> getBean(context: ApplicationContext, key: TypedKey<C>): C = context.getBean(key.key) as C
    }
}


/**
 * Util methods loading properties from resource to object.
 *
 * @author Ondrej Milenovsky
 */
class PropertiesLoader {

    fun <C> loadProperties(file: String, clazz: Class<C>): C? =
        try {
            val ret = clazz.newInstance()
            loadPropertiesToObject(file, ret as Any)
            ret
        } catch (e: Throwable) {
            logger.error("", e)
            null
        }

    companion object {
        private val logger = LoggerFactory.getLogger(PropertiesLoader::class.java)

        private const val CLASSPATH_PREFIX = "classpath:"

        fun loadPropertiesToObject(file: String, obj: Any) = loadPropertiesToObject(getInputStream(file), obj)

        /**
         * Loads the file to properties, then injects its properties to public fields of the object.
         * Logs all missing and extra properties.
         * @throws RuntimeException wrapping IOException
         * @throws IllegalArgumentException for null input
         * @throws URISyntaxException
         * @param input input file
         * @param obj object to be injected
         */
        fun loadPropertiesToObject(input: InputStream, obj: Any) {

            try {
                val pr = PropertiesUtils.loadProperties(input)
                val remaining = PropertiesUtils.injectToObject(pr, obj, true)
                if (!pr.isEmpty || !remaining.isEmpty()) {
                    logger.warn("FileSettings: " + input.toString())
                    for ((key, value) in pr)
                        logger.warn("Unknown property: $key = $value")
                    for (key in remaining)
                        logger.warn("Missing property: $key")
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

        }

        /**
         * Returns input stream for system file or classpath resource, if the path starts with classpath:, then it is resource.
         * If file or resource does not exist, then throws exceptions.
         * @param resourcePath_
         * @return
         */
        fun getInputStream(resourcePath_: String): InputStream {
            var resourcePath = resourcePath_
            if (resourcePath.toLowerCase().startsWith(CLASSPATH_PREFIX)) {
                resourcePath = resourcePath.substring(CLASSPATH_PREFIX.length)
                return ClassLoader.getSystemResourceAsStream(resourcePath) ?: throw RuntimeException("Missing resource: $resourcePath")
            }
            return when {
                File(resourcePath).isFile -> try {
                    FileInputStreamNamed(resourcePath)
                } catch (e: IOException) {
                    throw RuntimeException("Wrong file name: $resourcePath", e)
                }
                else -> throw RuntimeException("File not found: $resourcePath")
            }
        }
    }
}