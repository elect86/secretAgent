package cz.wa.secretagent.game.controller.menucreator;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import cz.wa.secretagent.game.GameSettings;
import cz.wa.secretagent.menu.builder.DialogMenuBuilder;
import cz.wa.secretagent.menu.builder.TextButtonDescriptor;
import cz.wa.secretagent.menu.window.GFrame;
import cz.wa.secretagent.menu.window.component.selectable.GButtonListener;
import cz.wa.secretagent.worldinfo.WorldHolder;

/**
 * Creates menu dialogs.
 *
 * @author Ondrej Milenovsky
 */
public abstract class GeneralMenuCreator implements Serializable {
    private static final long serialVersionUID = -8429325182641380571L;

    protected DialogMenuBuilder dialogBuilder;
    protected WorldHolder worldHolder;
    protected GameSettings gameSettings;
    protected Color frameColor;

    private transient GFrame settingsMenu;
    private transient GFrame exitDialog;

    private void init() {
        if (settingsMenu == null) {
            settingsMenu = createSettingsMenu();
            exitDialog = createExitDialog();
        }
    }

    public GFrame createConfirmDialog(String message, GButtonListener yesListener, Color color) {
        // buttons
        List<TextButtonDescriptor> buttons = new ArrayList<TextButtonDescriptor>();
        buttons.add(new TextButtonDescriptor("Yes", yesListener));
        buttons.add(new TextButtonDescriptor("No", new GButtonListener() {
            @Override
            public void actionPerformed() {
                worldHolder.getMenuHolder().removeTopFrame();
            }
        }));

        // frame
        GButtonListener closeListener = new GButtonListener() {
            @Override
            public void actionPerformed() {
                worldHolder.getMenuHolder().removeTopFrame();
            }
        };
        GFrame dialog = dialogBuilder.createDialog(message, buttons, color, closeListener, true);

        return dialog;
    }

    public GFrame getSettingsMenu() {
        init();
        return settingsMenu;
    }

    public GFrame getExitDialog() {
        init();
        return exitDialog;
    }

    private GFrame createSettingsMenu() {
        final GFrame saveSettingsDialog = createSaveSettingsDialog();

        // buttons
        List<TextButtonDescriptor> buttons = new ArrayList<TextButtonDescriptor>();
        buttons.add(new TextButtonDescriptor("Rebind keys", new GButtonListener() {
            @Override
            public void actionPerformed() {
                // TODO rebind keys menu
            }
        }));

        // frame
        GButtonListener closeListener = new GButtonListener() {
            @Override
            public void actionPerformed() {
                // TODO check if settings changed
                if (gameSettings.confirmDialogs) {
                    saveSettingsDialog.setSelectedIndex(0);
                    worldHolder.getMenuHolder().addFrame(saveSettingsDialog);
                } else {
                    saveSettings();
                }
            }
        };
        GFrame dialog = dialogBuilder.createDialog("Settings", buttons, frameColor, closeListener, false);

        return dialog;
    }

    private GFrame createExitDialog() {
        return createConfirmDialog("Exit the game?", new GButtonListener() {
            @Override
            public void actionPerformed() {
                exitGame();
            }
        }, frameColor);
    }

    protected void exitGame() {
        System.exit(0);
    }

    private GFrame createSaveSettingsDialog() {
        // buttons
        List<TextButtonDescriptor> buttons = new ArrayList<TextButtonDescriptor>();
        buttons.add(new TextButtonDescriptor("Yes", new GButtonListener() {
            @Override
            public void actionPerformed() {
                saveSettings();
                worldHolder.getMenuHolder().removeTopFrame();
            }
        }));
        buttons.add(new TextButtonDescriptor("No", new GButtonListener() {
            @Override
            public void actionPerformed() {
                worldHolder.getMenuHolder().removeTopFrame();
                worldHolder.getMenuHolder().removeTopFrame();
            }
        }));

        // frame
        GButtonListener closeListener = new GButtonListener() {
            @Override
            public void actionPerformed() {
                worldHolder.getMenuHolder().removeTopFrame();
            }
        };
        GFrame dialog = dialogBuilder.createDialog("Save settings?", buttons, frameColor, closeListener,
                true);

        return dialog;
    }

    private void saveSettings() {
        worldHolder.getMenuHolder().removeTopFrame();
        // TODO save settings
    }

    public DialogMenuBuilder getDialogBuilder() {
        return dialogBuilder;
    }

    @Required
    public void setDialogBuilder(DialogMenuBuilder dialogBuilder) {
        this.dialogBuilder = dialogBuilder;
    }

    public WorldHolder getWorldHolder() {
        return worldHolder;
    }

    @Required
    public void setWorldHolder(WorldHolder worldHolder) {
        this.worldHolder = worldHolder;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    @Required
    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public Color getFrameColor() {
        return frameColor;
    }

    @Required
    public void setFrameColor(Color frameColor) {
        this.frameColor = frameColor;
    }
}
