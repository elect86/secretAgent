package cz.wa.secretagent;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.xerces.impl.dv.dtd.DTDDVFactoryImpl;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.parsers.XIncludeAwareParserConfiguration;
import org.springframework.beans.ExtendedBeanInfoFactory;
import org.springframework.beans.factory.xml.UtilNamespaceHandler;
import org.springframework.context.config.ContextNamespaceHandler;

import cz.wa.wautils.log4j.WindowAppender;

/**
 * This class only refers to other classes,
 * that are included in the executable.
 * It is easier than configuring Maven shade plugin. 
 * 
 * @author Ondrej Milenovsky
 */
@SuppressWarnings("unused")
public final class IncludeClasses {

    // logging
    private WindowAppender windowAppender;
    private ConsoleAppender consoleAppender;
    private RollingFileAppender rollingFileAppender;
    private PatternLayout patternLayout;

    // spring
    private DocumentBuilderFactoryImpl documentBuilderFactoryImpl;
    private XIncludeAwareParserConfiguration xIncludeAwareParserConfiguration;
    private DTDDVFactoryImpl dtddvFactoryImpl;
    private UtilNamespaceHandler utilNamespaceHandler;
    private ExtendedBeanInfoFactory extendedBeanInfoFactory;
    private ContextNamespaceHandler contextNamespaceHandler;

    private IncludeClasses() {
    }
}
