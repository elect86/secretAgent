//package cz.wa.secretagent.utils;
//
//import java.util.Map;
//
//import org.springframework.context.ApplicationContext;
//
//import cz.wa.wautils.collection.TypedKey;
//
///**
// * Utils to easily load app context
// *
// * @author Ondrej Milenovsky
// */
//public class ContextWrapper {
//    private final ApplicationContext context;
//
//    public ContextWrapper(ApplicationContext context) {
//        this.context = context;
//    }
//
//    public <C> C getBean(TypedKey<C> key) {
//        return getBean(context, key);
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <C> C getBean(ApplicationContext context, TypedKey<C> key) {
//        return (C) context.getBean(key.key);
//    }
//
//    public boolean containsBean(TypedKey<?> key) {
//        return context.containsBean(key.key);
//    }
//
//    public <C> Map<String, C> getBeans(Class<C> clazz) {
//        return context.getBeansOfType(clazz);
//    }
//
//}
