//package cz.wa.secretagent.view.renderer.model;
//
//import java.util.List;
//
//import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
//import org.apache.commons.math3.util.FastMath;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.view.texture.DrawBounds;
//import cz.wa.secretagent.view.texture.TextureToDraw;
//import cz.wa.secretagent.world.entity.Entity;
//import cz.wa.secretagent.world.entity.agent.AgentAction;
//import cz.wa.secretagent.world.entity.agent.HumanAgent;
//import cz.wa.secretagent.world.weapon.Weapon;
//import cz.wa.secretagent.worldinfo.WorldHolder;
//import cz.wa.wautils.math.Rectangle2D;
//import secretAgent.view.model.AgentModel;
//import secretAgent.view.model.AgentTextures;
//import secretAgent.view.model.WeaponModel;
//import secretAgent.world.ObjectModel;
//import secretAgent.world.entity.EntityXDirection;
//
///**
// * Draws agent and his weapon.
// *
// * @author Ondrej Milenovsky
// */
//public class AgentModelDrawer extends AbstractModelDrawer<AgentModel> {
//    private static final long serialVersionUID = -3663846828569231873L;
//
//    private static final Logger logger = LoggerFactory.getLogger(AgentModelDrawer.class);
//
//    private WorldHolder worldHolder;
//    /** speed of animation (relative to agent's speed) */
//    private double animSpeed;
//
//    @Override
//    public void draw(AgentModel model, Entity entity, Vector2D pos, double scale) {
//        if (!(entity instanceof HumanAgent)) {
//            logger.warn("The entity must be instance of Agent, but is " + entity.getClass().getSimpleName());
//            return;
//        }
//        HumanAgent agent = (HumanAgent) entity;
//
//        // bounds and texture list
//        DrawBounds bounds;
//        List<TextureToDraw> txs;
//        EntityXDirection dir = agent.getDirection();
//        AgentAction action = agent.getCurrentAction();
//
//        if (dir == EntityXDirection.LEFT) {
//            AgentTextures lTex = model.getLeftTextures();
//            if (lTex != null) {
//                bounds = new DrawBounds(model.getBounds());
//            } else {
//                bounds = new DrawBounds(model.getBounds(), true);
//                lTex = model.getRightTextures();
//            }
//            txs = lTex.getTextures(action);
//        } else {
//            AgentTextures rTex = model.getRightTextures();
//            if (rTex != null) {
//                bounds = new DrawBounds(model.getBounds());
//            } else {
//                bounds = new DrawBounds(model.getBounds(), true);
//                rTex = model.getLeftTextures();
//            }
//            txs = rTex.getTextures(action);
//        }
//
//        // texture from list
//        TextureToDraw tex;
//        long timeMs = agent.getActionTime();
//        if (txs.size() > 1) {
//            long durationMs;
//            if (action == AgentAction.STAY) {
//                durationMs = model.getStayDurationMs();
//            } else {
//                durationMs = FastMath
//                        .round(1000.0 / (animSpeed * agent.getSpeed().add(agent.getMoveSpeed()).getNorm()));
//            }
//            tex = getFrame(timeMs, durationMs, txs);
//        } else {
//            tex = txs.get(0);
//        }
//        // draw
//        primitivesDrawer.drawTexture(tex, pos, bounds, scale);
//        if (agent.getWeapon() != null) {
//            Vector2D weaponPos = model.getWeaponCenter().scalarMultiply(scale);
//            if (agent.getDirection() == EntityXDirection.LEFT) {
//                weaponPos = new Vector2D(-weaponPos.getX(), weaponPos.getY());
//            }
//            weaponPos = weaponPos.add(pos);
//            drawWeapon(agent, agent.getWeapon(), weaponPos, scale);
//        }
//    }
//
//    private void drawWeapon(HumanAgent agent, Weapon weapon, Vector2D pos, double scale) {
//        ObjectModel model1 = weapon.getModel();
//        if (model1 == null) {
//            logger.warn("Weapon '" + weapon.getName() + "' has not linked model");
//            return;
//        }
//        if (!(model1 instanceof WeaponModel)) {
//            logger.warn("Weapon '" + weapon.getName() + "' has unknown model: "
//                    + model1.getClass().getSimpleName());
//            return;
//        }
//        // get all current values from agent
//        WeaponModel model = (WeaponModel) model1;
//        double angle = agent.getAimAngle();
//        boolean aimLeft = agent.getDirection() == EntityXDirection.LEFT;
//        long reloadingS = FastMath.round(agent.getReloadTimeRemainingS() * 1000);
//        if (aimLeft) {
//            angle = -angle;
//        }
//        long reloadTimeS = FastMath.round(weapon.getReloadTimeS() * 1000);
//        boolean firing = reloadingS > 0;
//        // get parameters from model
//        double modelScale = model.getScale();
//        Vector2D wCenter = model.getCenter(firing).scalarMultiply(-modelScale);
//        if (aimLeft) {
//            wCenter = new Vector2D(-wCenter.getX(), wCenter.getY());
//        }
//        List<TextureToDraw> textures = model.getModel(firing).getTextures();
//        TextureToDraw tex;
//        if (reloadingS > 0) {
//            tex = getFrame(reloadTimeS - reloadingS, reloadTimeS, textures);
//        } else {
//            tex = getFrame(worldHolder.getWorld().getLevelMap().getTimeMs(),
//                    model.getModel(false).getDurationMs(), textures);
//        }
//        if (tex == null) {
//            logger.warn("Weapon model '" + weapon.getModelName() + "' has not linked textures");
//            return;
//        }
//        // compute canvas coords
//        double texWidth = tex.getTexture().getWidth() * modelScale;
//        double texHeight = tex.getTexture().getHeight() * modelScale;
//        Rectangle2D rect = new Rectangle2D(wCenter.getX() - texWidth / 2.0, wCenter.getY() - texHeight / 2.0,
//                texWidth, texHeight);
//        DrawBounds bounds = new DrawBounds(rect);
//        // draw the weapon
//        primitivesDrawer.drawTexture(tex, pos, bounds, scale, angle, aimLeft);
//    }
//
//    public double getAnimSpeed() {
//        return animSpeed;
//    }
//
//    @Required
//    public void setAnimSpeed(double animSpeed) {
//        this.animSpeed = animSpeed;
//    }
//
//    public WorldHolder getWorldHolder() {
//        return worldHolder;
//    }
//
//    @Required
//    public void setWorldHolder(WorldHolder worldHolder) {
//        this.worldHolder = worldHolder;
//    }
//
//}
