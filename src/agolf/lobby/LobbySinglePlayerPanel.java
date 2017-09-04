package agolf.lobby;

import agolf.GameApplet;
import agolf.GameContainer;
import com.aapeli.client.StringDraw;
import com.aapeli.colorgui.*;
import org.moparforia.client.Launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.StringTokenizer;

class LobbySinglePlayerPanel extends Panel implements ItemListener, ActionListener, MultiColorListListener {

    private GameContainer gameContainer;
    private int width;
    private int height;
    private Choicer choicerTracksNum;
    private Choicer choicerTrackTypes;
    private Choicer choicerWaterEvent;
    private MultiColorList trackSetList;
    private ColorButton buttonStartTraining;
    private ColorButton buttonStartChampionship;
    private String selectedTrackData;
    private boolean requestTrackSetList;
    private Image image;
    private Graphics graphics;
    //private Choicer choicerTrackCategory;
    private boolean isUsingCustomServer;

    protected LobbySinglePlayerPanel(GameContainer var1, int var2, int var3) {
        isUsingCustomServer = Launcher.isUsingCustomServer();
        this.gameContainer = var1;
        this.width = var2;
        this.height = var3;
        this.setSize(var2, var3);
        this.create();
        this.requestTrackSetList = true;
    }

    public void addNotify() {
        super.addNotify();
        this.repaint();
    }

    public void paint(Graphics var1) {
        this.update(var1);
    }

    public void update(Graphics g) {
        if (this.image == null) {
            this.image = this.createImage(this.width, this.height);
            this.graphics = this.image.getGraphics();
        }

        this.graphics.setColor(GameApplet.colourGameBackground);
        this.graphics.fillRect(0, 0, this.width, this.height);
        this.graphics.drawImage(this.gameContainer.imageManager.getImage("bg-lobby-single"), 0, 0, this);
        Color trainingOutlineColour = new Color(14, 219, 14);
        Color championshipOutlineColour = new Color(0, 205, 0);
        this.graphics.setColor(GameApplet.colourTextBlack);
        this.graphics.setFont(GameApplet.fontSerif26b);
        StringDraw.drawString(this.graphics, this.gameContainer.textManager.getGame("LobbySelect_SinglePlayer"), this.width / 2, 37, 0);
        byte yPos = -45;
        this.graphics.setFont(GameApplet.fontSerif20);
        StringDraw.drawOutlinedString(this.graphics, trainingOutlineColour, this.gameContainer.textManager.getGame("LobbyReal_TrainingTitle"), this.width / 4, 170 + yPos, 0);
        StringDraw.drawOutlinedString(this.graphics, championshipOutlineColour, this.gameContainer.textManager.getGame("LobbyReal_ChampionshipTitle"), this.width * 3 / 4, 195 + yPos - 15 - 15, 0);
        this.graphics.setFont(GameApplet.fontDialog12);
        yPos = -45;
        /*if(isUsingCustomServer) {
            StringDraw.drawOutlinedString(this.graphics, trainingOutlineColour, "Track category:", this.width / 2 - 190, 235 + yPos, 1);
        }*/
        StringDraw.drawOutlinedString(this.graphics, trainingOutlineColour, this.gameContainer.textManager.getGame("LobbyReal_TrackCount"), this.width / 2 - 190, 205 + yPos, 1);
        StringDraw.drawOutlinedString(this.graphics, trainingOutlineColour, this.gameContainer.textManager.getGame("LobbyReal_TrackTypes"), this.width / 2 - 190, (/*isUsingCustomServer ? 265 :*/ 235) + yPos, 1);
        StringDraw.drawOutlinedString(this.graphics, trainingOutlineColour, this.gameContainer.textManager.getGame("LobbyReal_WaterEvent"), this.width / 2 - 190, (/*isUsingCustomServer ? 295 :*/ 265) + yPos, 1);
        if (this.choicerWaterEvent.getSelectedIndex() == 1) {
            this.graphics.setColor(GameApplet.colourTextRed);
            StringDraw.drawOutlinedString(this.graphics, trainingOutlineColour, this.gameContainer.textManager.getGame("LobbyReal_WaterEventWarning"), this.width / 2 - 190 + 20, (/*isUsingCustomServer ? 325 :*/ 295) + yPos, 0);
            this.graphics.setColor(GameApplet.colourTextBlack);
        }

        if (this.selectedTrackData != null) {
            this.graphics.setFont(GameApplet.fontDialog11);
            StringTokenizer trackData = new StringTokenizer(this.selectedTrackData, "\t");
            String[] trackInfoTitles = new String[]{"LobbyReal_TS_AllTimeBest", "LobbyReal_TS_MonthBest", "LobbyReal_TS_WeekBest", "LobbyReal_TS_DayBest"};
            int[] trackInfoYPos = new int[]{370, 385, 400, 415};

            for (int index = 0; index < 4; ++index) {
                String strokes = trackData.nextToken();
                String recordHolder = trackData.nextToken();
                StringDraw.drawOutlinedString(this.graphics, championshipOutlineColour, this.gameContainer.textManager.getGame(trackInfoTitles[index]), this.width - 240, trackInfoYPos[index] + yPos, 1);
                if (!recordHolder.equals("0")) {
                    StringDraw.drawOutlinedString(this.graphics, championshipOutlineColour, recordHolder + " (" + strokes + ")", this.width - 235, trackInfoYPos[index] + yPos, -1);
                } else {
                    StringDraw.drawOutlinedString(this.graphics, championshipOutlineColour, "-", this.width - 235, trackInfoYPos[index] + yPos, -1);
                }
            }
        }

        g.drawImage(this.image, 0, 0, this);
    }

    public void itemStateChanged(ItemEvent evt) {
        if (evt.getSource() == this.trackSetList) {
            this.selectedTrackData = this.getSelectedTrackSetData();
            if (this.selectedTrackData != null) {
                this.selectedTrackData = this.selectedTrackData.substring(this.selectedTrackData.indexOf(9) + 1);
            }
        }

        this.repaint();
    }

    public void actionPerformed(ActionEvent evt) {
        if (this.gameContainer.gameApplet.syncIsValidSite.get()) {
            Object var2 = evt.getSource();
            if (var2 == this.buttonStartTraining) {
                this.gameContainer.gameApplet.setGameState(0);
                this.gameContainer.lobbyPanel.writeData("cspt\t" + (this.choicerTracksNum.getSelectedIndex() + 1) + "\t" + this.choicerTrackTypes.getSelectedIndex() + "\t" + this.choicerWaterEvent.getSelectedIndex() /*+ (isUsingCustomServer ? ("\t" + this.choicerTrackCategory.getSelectedIndex()) : "")*/);
            } else {
                if (var2 == this.buttonStartChampionship) {
                    String var3 = this.getSelectedTrackSetData();
                    if (var3 != null) {
                        startChampionship(Integer.parseInt(var3.substring(0, var3.indexOf(9))));
                    }
                }

            }
        }
    }

    protected boolean handlePacket(String[] args) {
        if (args[1].equals("tracksetlist")) {
            this.trackSetList.removeAllItems();
            int numTrackSets = (args.length - 2) / 11;
            byte var3 = -1;

            for (int off = 0; off < numTrackSets; ++off) {
                String setName = args[2 + off * 11];
                int setDifficulty = Integer.parseInt(args[3 + off * 11]);
                String[] var7 = new String[]{isUsingCustomServer ? setName : this.gameContainer.textManager.getGame("LobbyReal_TS_" + setName), this.gameContainer.textManager.getGame("LobbyReal_TS_Level" + setDifficulty), args[4 + off * 11]};
                if (setDifficulty == 1) {
                    var3 = 2;
                } else if (setDifficulty == 2) {
                    var3 = 4;
                } else if (setDifficulty == 3) {
                    var3 = 1;
                }

                String var8 = args[5 + off * 11] + "\t" + args[6 + off * 11] + "\t" + args[7 + off * 11] + "\t" + args[8 + off * 11] + "\t" + args[9 + off * 11] + "\t" + args[10 + off * 11] + "\t" + args[11 + off * 11] + "\t" + args[12 + off * 11];
                boolean var9;
                if (var9 = off == numTrackSets - 1) {
                    this.selectedTrackData = var8;
                }

                MultiColorListItem var10 = new MultiColorListItem(var3, false, var7, off + "\t" + var8, var9);
                this.trackSetList.addItem(var10);
            }

            this.requestTrackSetList = false;
            this.repaint();
            return true;
        } else {
            return false;
        }
    }

    protected void setRequestTrackSetList() {
        this.requestTrackSetList = true;
    }

    protected void requestTrackSetList() {
        if (this.requestTrackSetList) {
            this.gameContainer.lobbyPanel.writeData("tracksetlist");
        }

    }

    private void create() {
        this.setLayout((LayoutManager) null);
        /*if(isUsingCustomServer) {
            this.choicerTrackCategory = this.gameContainer.lobbyPanel.addChoicerTrackCategory(this, this.width / 2 - 170, 175, 150, 20);
        }*/
        this.choicerTracksNum = this.gameContainer.lobbyPanel.addChoicerNumTracks(this, this.width / 2 - 170, 145, 50, 20);
        this.choicerTrackTypes = this.gameContainer.lobbyPanel.addChoicerTrackTypes(this, this.width / 2 - 170, /*isUsingCustomServer ? 205 :*/ 175, 150, 20);
        this.choicerWaterEvent = this.gameContainer.lobbyPanel.addChoicerWaterEvent(this, this.width / 2 - 170, /*isUsingCustomServer ? 235 :*/ 205, 150, 20);
        this.choicerWaterEvent.addItemListener(this);
        this.buttonStartTraining = new ColorButton(this.gameContainer.textManager.getGame("LobbyReal_Start"));
        this.buttonStartTraining.setBackground(GameApplet.colourButtonGreen);
        this.buttonStartTraining.setBounds(this.width / 2 - 170, /*isUsingCustomServer ? 300 :*/ 270, 100, 25);
        this.buttonStartTraining.addActionListener(this);
        this.add(this.buttonStartTraining);
        String[] trackSetListTitles = new String[]{this.gameContainer.textManager.getGame("LobbyReal_TS_TitleName"), this.gameContainer.textManager.getGame("LobbyReal_TS_TitleDifficulty"), this.gameContainer.textManager.getGame("LobbyReal_TS_TitleTracks")};
        int[] var2 = new int[]{0, 0, 4};
        this.trackSetList = new MultiColorList(trackSetListTitles, var2, 1, 250, 130);
        this.trackSetList.setLocation(this.width - 290, 130);
        this.trackSetList.setBackgroundImage(this.gameContainer.imageManager.getImage("bg-lobby-single-fade"), this.width - 290, 130);
        this.trackSetList.setSelectable(1);
        this.trackSetList.addItemListener(this);
        trackSetList.setListListener(this);
        this.add(this.trackSetList);
        this.buttonStartChampionship = new ColorButton(this.gameContainer.textManager.getGame("LobbyReal_Start"));
        this.buttonStartChampionship.setBackground(GameApplet.colourButtonGreen);
        this.buttonStartChampionship.setBounds(this.width - 290 + 75 - 20, 270, 100, 25);
        this.buttonStartChampionship.addActionListener(this);
        this.add(this.buttonStartChampionship);
    }

    private String getSelectedTrackSetData() {
        MultiColorListItem var1 = this.trackSetList.getSelectedItem();
        return var1 == null ? null : (String) var1.getData();
    }

    @Override
    public void mouseDoubleClicked(MultiColorListItem clickedItem) {
        String itemData = (String) clickedItem.getData();
        if (itemData != null) {
            startChampionship(Integer.parseInt(itemData.substring(0, itemData.indexOf(9))));
        }
    }

    private void startChampionship(int index) {
        this.gameContainer.gameApplet.setGameState(0);
        this.gameContainer.lobbyPanel.writeData("cspc\t" + index);
    }
}
