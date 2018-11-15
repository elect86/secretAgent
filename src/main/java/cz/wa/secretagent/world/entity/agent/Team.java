//package cz.wa.secretagent.world.entity.agent;
//
//import java.io.Serializable;
//
//import org.apache.commons.lang.Validate;
//
///**
// * Team of an agent.
// *
// * @author Ondrej Milenovsky
// */
//public class Team implements Serializable {
//    private static final long serialVersionUID = -4859957595199661132L;
//
//    private final String name;
//
//    public Team(String name) {
//        Validate.notNull(name, "name is null");
//        this.name = name;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((name == null) ? 0 : name.hashCode());
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        Team other = (Team) obj;
//        if (name == null) {
//            if (other.name != null) {
//                return false;
//            }
//        } else if (!name.equals(other.name)) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "Team " + name;
//    }
//}
