package GUI;

import DataStructures.SoccerPlayer;
import StaticClasses.InfoTableModel;
import HelpMenu.JHelpFrame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/*
 * MainFrame.java
 *
 * Created on Mar 12, 2009, 8:09:15 AM
 */
/**
 *
 * @author Joel_Ward
 */
public class MainFrame extends javax.swing.JFrame {

    private ArrayList<SoccerPlayer> players;
    private int numGames;
    public String filePath;

    /** Creates new form MainFrame */
    public MainFrame() {

        guiOptions();

        players = new ArrayList<SoccerPlayer>();
        numGames = 0;
        initComponents();
        
        saveMenuItem.setEnabled(false); 
        refreshPanels();
    }

    // Methods which will manipulate the data in the Database
    public void addPlayer(String firstName, String lastName, int number, String position) {
        players.add(new SoccerPlayer(firstName, lastName, number, position, numGames));
    }

    public void deletePlayer(int index) {
        players.remove(index);
    }

    private void editPlayer(int athlete, int type) {
        switch (type) {
            case 0:
                String firstName = JOptionPane.showInputDialog(this, "Edit first name for \n"
                        + players.get(athlete).getFirstName() + " "
                        + players.get(athlete).getLastName(), players.get(athlete).getFirstName());
                players.get(athlete).setFirstName(firstName);
                break;
            case 1:
                String lastName = JOptionPane.showInputDialog(this, "Edit last name for \n"
                        + players.get(athlete).getFirstName() + " "
                        + players.get(athlete).getLastName(), players.get(athlete).getLastName());
                players.get(athlete).setLastName(lastName);
                break;
            case 2:
                try {
                    int number = Integer.parseInt(JOptionPane.showInputDialog(this, "Edit number for \n"
                            + players.get(athlete).getFirstName() + " "
                            + players.get(athlete).getLastName(), players.get(athlete).getNumber()));
                    
                    players.get(athlete).setNumber(number);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Please enter an number.",
                            "Invalid Data",
                            JOptionPane.ERROR_MESSAGE);
                }   break;
            case 3:
                String position = JOptionPane.showInputDialog(this, "Edit position for \n"
                        + players.get(athlete).getFirstName() + " "
                        + players.get(athlete).getLastName(), players.get(athlete).getPosition());
                players.get(athlete).setPosition(position);
                break;
            default:
                break;
        }

        refreshPanels();

    }

    public void importPlayers(String file) {
        String input;

        try {
            // Load file and read info to RAM from file
            BufferedReader loadFile = new BufferedReader(new FileReader(file));

            // Continue to read in from text file 2 lines for each athlete
            // while there are still line to be read in
            // First line is their personal info
            // Second line is their goal data
            while ((input = loadFile.readLine()) != null) {
                String[] values = input.split(",");
                String firstName = values[0];
                String lastName = values[1];
                int num = Integer.parseInt(values[2]);
                String pos = values[3];

                players.add(new SoccerPlayer(firstName, lastName, num, pos, numGames));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error trying to "
                    + "load file: " + ex,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public void sortPlayers(int type) {
        // Selection Sort
        for (int end = players.size() - 1; end > 0; end--) {
            int max = 0;
            // Find index value of Max
            for (int x = 1; x <= end; x++) {
                if (players.get(x).get(type).toLowerCase().compareTo(
                        players.get(max).get(type).toLowerCase()) > 0) {
                    max = x;
                }
            }
            // Swap Max with End
            SoccerPlayer temp = players.get(max);
            players.set(max, players.get(end));
            players.set(end, temp);
        }
    }

    private void addGame() {
        for (int p = 0; p < players.size(); p++) {
            players.get(p).setGoals(numGames, 0);
        }
        numGames++;
        refreshPanels();
    }

    private void editGoalData(int athlete, int game) {
        if (game >= 0) {
            int newGoals = Integer.parseInt(
                    JOptionPane.showInputDialog(this, "Enter goals scored\n"
                    + "for " + players.get(athlete).getFirstName() + " "
                    + players.get(athlete).getLastName(), players.get(athlete).getGoals(game)));

            players.get(athlete).setGoals(game, newGoals);
            refreshPanels();
        }
    }

    public void load() {
        
        // Reset data to allow data from file to populate data types
        players = new ArrayList<SoccerPlayer>();
        numGames = 0;
        
        try {
            // Load file and read info to RAM from file
            BufferedReader loadFile = new BufferedReader(new FileReader(
                    filePath));

            // Read in first line of text file which tells how many games of
            // data we have saved
            String input;
            input = loadFile.readLine();
            numGames = Integer.parseInt(input);

            // Continue to read in from text file 2 lines for each athlete
            // while there are still line to be read in
            // First line is their personal info
            // Second line is their goal data
            while ((input = loadFile.readLine()) != null) {
                String[] values = input.split(",");
                String firstName = values[0];
                String lastName = values[1];
                int num = Integer.parseInt(values[2]);
                String pos = values[3];

                input = loadFile.readLine();
                values = input.split(",");
                int[] goals = new int[SoccerPlayer.MAX_GAMES];
                for (int g = 0; g < values.length; g++) {
                    goals[g] = Integer.parseInt(values[g]);
                }

                players.add(new SoccerPlayer(firstName, lastName, num, pos, goals));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error trying to "
                    + "load file: " + ex,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void save() {
        // Output the numbers to a text file
        try {
            PrintWriter writer = new PrintWriter(new File(filePath));

            // Write the number of games to the file
            writer.println(numGames);
            
            // Loop through players and write their information to the file
            // Names on the first line, goals on the second line
            for (int n = 0; n < players.size(); n++) {
                writer.println(players.get(n).getFirstName() + ","
                        + players.get(n).getLastName() + ","
                        + players.get(n).getNumber() + ","
                        + players.get(n).getPosition());
                for (int g = 0; g < SoccerPlayer.MAX_GAMES; g++) {
                    writer.print(players.get(n).getGoals(g) + ",");
                }
                writer.println();
            }

            writer.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error trying to load file: " + ex,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshPanels() {
        drawAthletePanel();
        drawGoalPanel();
    }

    private void drawAthletePanel() {
        // Get Roster Header
        String[] rosterHeader = {
            "First Name", "Last Name", "Number", "Position", "Avg Goals"
        };

        // Get data for table
        String[][] rosterData = new String[players.size()][rosterHeader.length];
        // Populate table with data
        for (int x = 0; x < rosterData.length; x++) {
            // Athlete Info
            rosterData[x][0] = players.get(x).getFirstName();
            rosterData[x][1] = players.get(x).getLastName();
            rosterData[x][2] = String.valueOf(players.get(x).getNumber());
            rosterData[x][3] = players.get(x).getPosition();
            rosterData[x][4] = String.format("%.2f", players.get(x).getAverage());
        }

        // Create JTable with all our data
        rosterTable.setModel(new InfoTableModel(rosterData, rosterHeader));

        rosterTable.getColumnModel().getColumn(0).setMinWidth(100);
        rosterTable.getColumnModel().getColumn(1).setMinWidth(25);
        rosterTable.getColumnModel().getColumn(2).setMinWidth(100);
    }

    private void drawGoalPanel() {
        // Get Roster Header
        String[] goalsHeader = new String[numGames + 2];
        goalsHeader[0] = "First Name";
        goalsHeader[1] = "Last Name";

        for (int n = 1; n <= numGames; n++) {
            goalsHeader[n + 1] = "Game " + n;
        }

        // Get data for table
        String[][] goalsData = new String[players.size()][goalsHeader.length];
        // Populate table with data
        for (int x = 0; x < goalsData.length; x++) {
            // Athlete Info
            goalsData[x][0] = players.get(x).getFirstName();
            goalsData[x][1] = players.get(x).getLastName();
            for (int n = 2; n <= numGames + 1; n++) {
                goalsData[x][n] = String.valueOf(players.get(x).getGoals(n - 2));
            }
        }

        // Create JTable with all our data
        goalsTable.setModel(new InfoTableModel(goalsData, goalsHeader));

        goalsTable.getColumnModel().getColumn(0).setMinWidth(100);
    }
    
    // Set options, look, and feel of GUI
    private void guiOptions()
    {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null,
                    ex);
        }        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        infoPanel = new javax.swing.JPanel();
        infoScrollPane = new javax.swing.JScrollPane();
        rosterTable = new javax.swing.JTable();
        addPlayerButton = new javax.swing.JButton();
        deletePlayerButton = new javax.swing.JButton();
        goalsPanel = new javax.swing.JPanel();
        goalsScrollPane = new javax.swing.JScrollPane();
        goalsTable = new javax.swing.JTable();
        addGameButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        importNamesMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        quitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        addPlayerMenuItem = new javax.swing.JMenuItem();
        deletePlayerMenuItem = new javax.swing.JMenuItem();
        sortMenu = new javax.swing.JMenu();
        sortFirstNameMenuItem = new javax.swing.JMenuItem();
        sortLastNameMenuItem = new javax.swing.JMenuItem();
        sortNumberMenuItem = new javax.swing.JMenuItem();
        sortPositionMenuItem = new javax.swing.JMenuItem();
        sortAverageGoalsMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Soccer Database");

        infoScrollPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                infoScrollPaneMouseClicked(evt);
            }
        });

        rosterTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        rosterTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rosterTableMouseClicked(evt);
            }
        });
        infoScrollPane.setViewportView(rosterTable);

        addPlayerButton.setText("Add Player");
        addPlayerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPlayerButtonActionPerformed(evt);
            }
        });

        deletePlayerButton.setText("Delete Player");
        deletePlayerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePlayerButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout infoPanelLayout = new org.jdesktop.layout.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(infoScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
                .addContainerGap())
            .add(infoPanelLayout.createSequentialGroup()
                .add(addPlayerButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deletePlayerButton)
                .add(0, 0, Short.MAX_VALUE))
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(infoScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(infoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(addPlayerButton)
                    .add(deletePlayerButton))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Roster", infoPanel);

        goalsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        goalsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goalsTableMouseClicked(evt);
            }
        });
        goalsScrollPane.setViewportView(goalsTable);

        addGameButton.setText("Add Game");
        addGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addGameButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout goalsPanelLayout = new org.jdesktop.layout.GroupLayout(goalsPanel);
        goalsPanel.setLayout(goalsPanelLayout);
        goalsPanelLayout.setHorizontalGroup(
            goalsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(goalsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(goalsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(goalsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                    .add(addGameButton))
                .addContainerGap())
        );
        goalsPanelLayout.setVerticalGroup(
            goalsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, goalsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(goalsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(addGameButton)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Goals", goalsPanel);

        fileMenu.setText("File");

        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        importNamesMenuItem.setText("Import Names");
        importNamesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importNamesMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(importNamesMenuItem);

        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText("Save As...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);

        quitMenuItem.setText("Quit");
        quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(quitMenuItem);

        jMenuBar1.add(fileMenu);

        editMenu.setText("Edit");

        addPlayerMenuItem.setText("Add Player");
        addPlayerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPlayerMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(addPlayerMenuItem);

        deletePlayerMenuItem.setText("Delete Player");
        deletePlayerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePlayerMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(deletePlayerMenuItem);

        sortMenu.setText("Sort");

        sortFirstNameMenuItem.setText("Sort by First Name");
        sortFirstNameMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortFirstNameMenuItemActionPerformed(evt);
            }
        });
        sortMenu.add(sortFirstNameMenuItem);

        sortLastNameMenuItem.setText("Sort by Last Name");
        sortLastNameMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortLastNameMenuItemActionPerformed(evt);
            }
        });
        sortMenu.add(sortLastNameMenuItem);

        sortNumberMenuItem.setText("Sort by Number");
        sortNumberMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortNumberMenuItemActionPerformed(evt);
            }
        });
        sortMenu.add(sortNumberMenuItem);

        sortPositionMenuItem.setText("Sort by Position");
        sortPositionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortPositionMenuItemActionPerformed(evt);
            }
        });
        sortMenu.add(sortPositionMenuItem);

        sortAverageGoalsMenuItem.setText("Sort by Average Goals");
        sortAverageGoalsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortAverageGoalsMenuItemActionPerformed(evt);
            }
        });
        sortMenu.add(sortAverageGoalsMenuItem);

        editMenu.add(sortMenu);

        jMenuBar1.add(editMenu);

        helpMenu.setText("Help");

        helpMenuItem.setText("Help Documents");
        helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(helpMenuItem);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 407, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveMenuItemActionPerformed
    {//GEN-HEADEREND:event_saveMenuItemActionPerformed
        save();
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_quitMenuItemActionPerformed
    {//GEN-HEADEREND:event_quitMenuItemActionPerformed
        save();
        System.exit(0);
    }//GEN-LAST:event_quitMenuItemActionPerformed

    private void addPlayerMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addPlayerMenuItemActionPerformed
    {//GEN-HEADEREND:event_addPlayerMenuItemActionPerformed
        JDialog dialog = new JDialog(this, true);
        dialog.add(new AddPanel(this));
        dialog.pack();
        dialog.setVisible(true);

        refreshPanels();
    }//GEN-LAST:event_addPlayerMenuItemActionPerformed

    private void sortFirstNameMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sortFirstNameMenuItemActionPerformed
    {//GEN-HEADEREND:event_sortFirstNameMenuItemActionPerformed
        sortPlayers(SoccerPlayer.FIRSTNAME);
        refreshPanels();
    }//GEN-LAST:event_sortFirstNameMenuItemActionPerformed

    private void sortNumberMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sortNumberMenuItemActionPerformed
    {//GEN-HEADEREND:event_sortNumberMenuItemActionPerformed
        sortPlayers(SoccerPlayer.NUMBER);
        refreshPanels();
    }//GEN-LAST:event_sortNumberMenuItemActionPerformed

    private void sortPositionMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_sortPositionMenuItemActionPerformed
    {//GEN-HEADEREND:event_sortPositionMenuItemActionPerformed
        sortPlayers(SoccerPlayer.POSITION);
        refreshPanels();
    }//GEN-LAST:event_sortPositionMenuItemActionPerformed

    private void deletePlayerMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deletePlayerMenuItemActionPerformed
    {//GEN-HEADEREND:event_deletePlayerMenuItemActionPerformed
        int index = rosterTable.getSelectedRow();
        deletePlayer(index);
        refreshPanels();
    }//GEN-LAST:event_deletePlayerMenuItemActionPerformed

    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_helpMenuItemActionPerformed
    {//GEN-HEADEREND:event_helpMenuItemActionPerformed
        new JHelpFrame("HelpMenu/HelpMain.html").setVisible(true);
    }//GEN-LAST:event_helpMenuItemActionPerformed

    private void addGameButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addGameButtonActionPerformed
    {//GEN-HEADEREND:event_addGameButtonActionPerformed
        addGame();
    }//GEN-LAST:event_addGameButtonActionPerformed

    private void goalsTableMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_goalsTableMouseClicked
    {//GEN-HEADEREND:event_goalsTableMouseClicked
        if (evt.getClickCount() > 1) {
            int athlete = goalsTable.getSelectedRow();
            int game = goalsTable.getSelectedColumn() - 2;

            editGoalData(athlete, game);
        }
    }//GEN-LAST:event_goalsTableMouseClicked

    private void rosterTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rosterTableMouseClicked
        if (evt.getClickCount() > 1) {
            int athlete = rosterTable.getSelectedRow();
            int type = rosterTable.getSelectedColumn();

            editPlayer(athlete, type);
        }
    }//GEN-LAST:event_rosterTableMouseClicked

    private void sortLastNameMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortLastNameMenuItemActionPerformed
        sortPlayers(SoccerPlayer.LASTNAME);
        refreshPanels();
    }//GEN-LAST:event_sortLastNameMenuItemActionPerformed

    private void sortAverageGoalsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortAverageGoalsMenuItemActionPerformed
        sortPlayers(SoccerPlayer.AVERAGE);
        refreshPanels();
    }//GEN-LAST:event_sortAverageGoalsMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        JDialog dialog = new JDialog(this, true);
        dialog.add(new OpenPanel(this, OpenPanel.OPEN_FILE));
        dialog.pack();
        dialog.setVisible(true);

        saveMenuItem.setEnabled(true); 
        refreshPanels();
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void importNamesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importNamesMenuItemActionPerformed
        JDialog dialog = new JDialog(this, true);
        dialog.add(new OpenPanel(this, OpenPanel.IMPORT));
        dialog.pack();
        dialog.setVisible(true);

        refreshPanels();
    }//GEN-LAST:event_importNamesMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        JDialog dialog = new JDialog(this, true);
        dialog.add(new SavePanel(this));
        dialog.pack();
        dialog.setVisible(true);
        save();

        saveMenuItem.setEnabled(true); 
        refreshPanels();    
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void addPlayerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPlayerButtonActionPerformed
        JDialog dialog = new JDialog(this, true);
        dialog.add(new AddPanel(this));
        dialog.pack();
        dialog.setVisible(true);

        refreshPanels();
    }//GEN-LAST:event_addPlayerButtonActionPerformed

    private void deletePlayerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePlayerButtonActionPerformed
        int index = rosterTable.getSelectedRow();
        deletePlayer(index);
        refreshPanels();
    }//GEN-LAST:event_deletePlayerButtonActionPerformed

    private void infoScrollPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_infoScrollPaneMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_infoScrollPaneMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addGameButton;
    private javax.swing.JButton addPlayerButton;
    private javax.swing.JMenuItem addPlayerMenuItem;
    private javax.swing.JButton deletePlayerButton;
    private javax.swing.JMenuItem deletePlayerMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPanel goalsPanel;
    private javax.swing.JScrollPane goalsScrollPane;
    private javax.swing.JTable goalsTable;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JMenuItem importNamesMenuItem;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JScrollPane infoScrollPane;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JTable rosterTable;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem sortAverageGoalsMenuItem;
    private javax.swing.JMenuItem sortFirstNameMenuItem;
    private javax.swing.JMenuItem sortLastNameMenuItem;
    private javax.swing.JMenu sortMenu;
    private javax.swing.JMenuItem sortNumberMenuItem;
    private javax.swing.JMenuItem sortPositionMenuItem;
    // End of variables declaration//GEN-END:variables
}
