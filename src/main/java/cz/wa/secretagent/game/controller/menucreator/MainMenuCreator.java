//package cz.wa.secretagent.game.controller.menucreator;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//
//import org.apache.commons.io.FileUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Required;
//
//import cz.wa.secretagent.game.starter.CampaignStarter;
//import cz.wa.secretagent.io.FileSettings;
//import cz.wa.secretagent.io.campaign.CampaignProperties;
//import cz.wa.secretagent.io.campaign.CampaignPropertiesParser;
//import cz.wa.secretagent.launcher.SecretAgentMain;
//import cz.wa.secretagent.menu.builder.TextButtonDescriptor;
//import cz.wa.secretagent.menu.window.GFrame;
//import cz.wa.secretagent.menu.window.component.selectable.GButtonListener;
//
///**
// * Creates main menu. Caches all frames.
// *
// * @author Ondrej Milenovsky
// */
//public class MainMenuCreator extends GeneralMenuCreator {
//
//    private static final long serialVersionUID = 3451448102156807094L;
//    private static final Logger logger = LoggerFactory.getLogger(MainMenuCreator.class);
//
//    private static final String ABOUT_MESSAGE = "About Secret Agent Remake\n\nVersion: "
//            + SecretAgentMain.VERSION + "\nAuthor: Ondrej Milenovsky"
//            + "\nLevel parsing inspired\n    by Camoto Studio\nAdditional images from games:\n    Duke Nukem 3D\n    Commando";
//
//    private FileSettings fileSettings;
//    private CampaignStarter campaignStarter;
//
//    private transient GFrame mainMenu;
//    private transient GFrame aboutDialog;
//
//    public GFrame getMainMenu() {
//        if (mainMenu == null) {
//            mainMenu = createMainMenu();
//            aboutDialog = createAboutDialog();
//        }
//        return mainMenu;
//    }
//
//    private GFrame createMainMenu() {
//        // buttons
//        List<TextButtonDescriptor> buttons = new ArrayList<TextButtonDescriptor>();
//        buttons.add(new TextButtonDescriptor("Load game", new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                // TODO load all saved games
//            }
//        }));
//        buttons.add(new TextButtonDescriptor("New game", new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                worldHolder.getMenuHolder().addFrame(createNewGameMenu());
//            }
//        }));
//        buttons.add(createSettingsButton());
//        buttons.add(new TextButtonDescriptor("About", new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                worldHolder.getMenuHolder().addFrame(aboutDialog);
//            }
//        }));
//        buttons.add(createExitButton());
//
//        // frame
//        GFrame dialog = dialogBuilder.createDialog("Secret Agent Remake", buttons, frameColor, null, false);
//
//        return dialog;
//    }
//
//    private TextButtonDescriptor createSettingsButton() {
//        return new TextButtonDescriptor("Settings", new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                GFrame settingsMenu = getSettingsMenu();
//                settingsMenu.setSelectedIndex(0);
//                worldHolder.getMenuHolder().addFrame(settingsMenu);
//            }
//        });
//    }
//
//    private TextButtonDescriptor createExitButton() {
//        return new TextButtonDescriptor("Exit the game", new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                if (gameSettings.confirmDialogs) {
//                    GFrame exitDialog = getExitDialog();
//                    exitDialog.setSelectedIndex(0);
//                    worldHolder.getMenuHolder().addFrame(exitDialog);
//                } else {
//                    exitGame();
//                }
//            }
//        });
//    }
//
//    private GFrame createNewGameMenu() {
//        // files
//
//        Collection<File> files = FileUtils.listFiles(new File(fileSettings.campaignsDir),
//                new String[] { fileSettings.campaignExt }, false);
//
//        // buttons
//        List<TextButtonDescriptor> buttons = new ArrayList<TextButtonDescriptor>(files.size());
//        for (File file : files) {
//            TextButtonDescriptor button = createCampaignButton(file);
//            if (button != null) {
//                buttons.add(button);
//            }
//        }
//
//        // frame
//        GButtonListener closeListener = new GButtonListener() {
//            @Override
//            public void actionPerformed() {
//                worldHolder.getMenuHolder().removeTopFrame();
//            }
//        };
//        GFrame dialog = dialogBuilder.createDialog("Select campaign to start", buttons, frameColor,
//                closeListener, false);
//
//        return dialog;
//    }
//
//    /**
//     * Loads the file and reads campaign name. On exception logs the exception and returns null.
//     * @param file file containing campaign description
//     * @return new button descriptor or null
//     */
//    private TextButtonDescriptor createCampaignButton(final File file) {
//        try {
//            CampaignProperties cp = new CampaignPropertiesParser(file, fileSettings).parse();
//            return new TextButtonDescriptor(cp.getTitle(), new GButtonListener() {
//                @Override
//                public void actionPerformed() {
//                    if (campaignStarter.startCampaign(file)) {
//                        worldHolder.getMenuHolder().removeAllFrames();
//                    }
//                }
//            });
//        } catch (IOException e) {
//            logger.error("Failed to create campaign button", e);
//            return null;
//        }
//    }
//
//    private GFrame createAboutDialog() {
//        return dialogBuilder.createDialog(ABOUT_MESSAGE, Collections.<TextButtonDescriptor> emptyList(),
//                frameColor, new GButtonListener() {
//                    @Override
//                    public void actionPerformed() {
//                        worldHolder.getMenuHolder().removeTopFrame();
//                    }
//                }, false);
//    }
//
//    public FileSettings getFileSettings() {
//        return fileSettings;
//    }
//
//    @Required
//    public void setFileSettings(FileSettings fileSettings) {
//        this.fileSettings = fileSettings;
//    }
//
//    public CampaignStarter getCampaignStarter() {
//        return campaignStarter;
//    }
//
//    @Required
//    public void setCampaignStarter(CampaignStarter campaignStarter) {
//        this.campaignStarter = campaignStarter;
//    }
//
//}
