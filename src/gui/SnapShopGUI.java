/*
 * TCSS 305 - Spring 2015
 * 
 * Assignment 4 - SnapShop.
 * Alex Terikov
 */

package gui;

import filters.EdgeDetectFilter;
import filters.EdgeHighlightFilter;
import filters.Filter;
import filters.FlipHorizontalFilter;
import filters.FlipVerticalFilter;
import filters.GrayscaleFilter;
import filters.SharpenFilter;
import filters.SoftenFilter;
import image.PixelImage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This class creates a GUI to manipulate image files.
 * 
 * @author Alex Terikov (teraliv@uw.edu)
 * @version April 30, 2015
 */
public class SnapShopGUI {
    
    /** The number of filters & buttons. */
    private static final int NUMBER_OF_FILTERS = 7;
    
    /** A size dimensions for the JFrame. */
    private static final Dimension DEFAULT_FRAME_SIZE = new Dimension(736, 110);
    
    
    // constants to capture screen dimensions
    /** A ToolKit. */
    private static final Toolkit KIT = Toolkit.getDefaultToolkit();
    
    /** The Dimension of the screen. */
    private static final Dimension SCREEN_SIZE = KIT.getScreenSize();
    
    /** The width of the screen. */
    private static final int SCREEN_WIDTH = SCREEN_SIZE.width;
    
    /** The height of the screen. */
    private static final int SCREEN_HEIGHT = SCREEN_SIZE.height;
    
    
    // components variables
    /** JFrame component to display on the screen. */
    private final JFrame myFrame;
    
    /** JPanel to be used on JFrame and contain filter buttons. */
    private JPanel myNorthPanel;
    
    /** JLabel to be used on JFrame and contain image. */
    private JLabel myCenterLabel;
    
    /** JPanel to be used on JFrame and contain option buttons. */
    private JPanel mySouthPanel;
    
    /** Button to open selection file dialog. */
    private JButton myOpenButton;
    
    /** Button to open save file dialog.  */
    private JButton mySaveButton;
    
    /** Button to close current image. */
    private JButton myCloseButton;
    
    /** JFileChooser to open open/save dialog. */
    private final JFileChooser myFileChooser;
    
    /** A JFileChooser integer representation. */
    private int myResult;
    
    /** An image file to display on the screen. */
    private PixelImage myImage;
    
    /** A file  that contains the name of selected image.*/
    private File myFile;
    
    /** A list with all used filters. */
    private final List<Filter> myFilterList;
    
    /** A list with all used buttons. */
    private final List<JButton> myButtonList;
    
    
    
    /**
     * Constructs GUI interface with frame and creates buttons.
     */
    public SnapShopGUI() {
        
        myFrame = new JFrame("TCSS 305 SnapShop");
        myFileChooser = new JFileChooser(".");
        
        myFilterList = new ArrayList<Filter>();
        myButtonList = new ArrayList<JButton>();
        
        myFrame.setMinimumSize(DEFAULT_FRAME_SIZE);
        
        myFrame.setLocation(SCREEN_WIDTH / 2 - myFrame.getWidth() / 2, 
                    SCREEN_HEIGHT / 2 - myFrame.getHeight() / 2);
        
        setupComponents();
        createFilterButtons();
        createOptionButtons();
    }
    
    /**
     * 
     */
    public void start() {
        
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
        myFrame.pack();
    }
    
    /**
     * This method sets up main components of the frame.
     */
    private void setupComponents() {
        
        final JPanel mainPanel = new JPanel(new BorderLayout());
        myNorthPanel = new JPanel(new FlowLayout());
        mySouthPanel = new JPanel(new FlowLayout());
        
        myCenterLabel = new JLabel();
        myCenterLabel.setHorizontalAlignment(JLabel.CENTER);
        myCenterLabel.setVerticalAlignment(JLabel.CENTER);
        
        mainPanel.add(myNorthPanel, BorderLayout.NORTH);
        mainPanel.add(mySouthPanel, BorderLayout.SOUTH);
        mainPanel.add(myCenterLabel, BorderLayout.CENTER);
        
        myFrame.add(mainPanel, BorderLayout.CENTER);
        
    }
    
    
    /**
     * This method creates filter button.
     */
    private void createFilterButtons() {
        
        // add filter objects to ArrayList
        myFilterList.add(new EdgeDetectFilter());
        myFilterList.add(new EdgeHighlightFilter());
        myFilterList.add(new FlipHorizontalFilter());
        myFilterList.add(new FlipVerticalFilter());
        myFilterList.add(new GrayscaleFilter());
        myFilterList.add(new SharpenFilter());
        myFilterList.add(new SoftenFilter());
        
        // make an ArrayList of buttons 
        for (int i = 0; i < NUMBER_OF_FILTERS; i++) {
            
            myButtonList.add(i, createFilterButton(myFilterList.get(i)));
            myNorthPanel.add(myButtonList.get(i));
        }
    }
    
    /**
     * A method to create a button and assign specific filter to it.
     * 
     * @param theFilter A specific filter for a button.
     * @return button with a assign filter.
     */
    private JButton createFilterButton(final Filter theFilter) {
        
        // A button that receive a name from description of the file.
        final JButton button = new JButton(theFilter.getDescription());
        
        /**
         * An inner class that creates an ActionListener for a filter button.
         * 
         * @author Alex Terikov (teraliv@uw.edu)
         * @version April 30, 2015
         */
        class FilterActionListener implements ActionListener {
            
            /**
             * This method applies a filter to an image and display it.
             * 
             * @param theEvent The current filter for an image.
             */
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                theFilter.filter(myImage);
                myCenterLabel.setIcon(new ImageIcon(myImage));
                myFrame.pack();
            }
        }
        
        button.addActionListener(new FilterActionListener());
        button.setEnabled(false);
        
        return button;
    }
 
    /**
     * This method creates option buttons:
     * Open/Save/Close/Undo.
     */
    private void createOptionButtons() {
        
        myOpenButton = new JButton("Open...");
        mySaveButton = new JButton("Save As...");
        mySaveButton.setEnabled(false);
        myCloseButton = new JButton("Close Image");
        myCloseButton.setEnabled(false);
        
        mySouthPanel.add(myOpenButton);
        mySouthPanel.add(mySaveButton);
        mySouthPanel.add(myCloseButton);
        
        myOpenButton.addActionListener(new OptionActionListener());
        mySaveButton.addActionListener(new OptionActionListener());
        myCloseButton.addActionListener(new OptionActionListener());
    }
    
    
    /**
     * Inner class that creates ActionListener for option buttons.
     * 
     * @author Alex Terikov (teraliv@uw.edu)
     * @version April 30, 2015
     */
    class OptionActionListener implements ActionListener {
        
        /**
         * This method handles event for option buttons.
         * 
         * @param theEvent A button that handles this event.
         */
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            
            myFile = myFileChooser.getSelectedFile();
            
            // Handle OPEN button action.
            if (theEvent.getSource() == myOpenButton) {
                
                openButtonAction();
            }
            
            // Handle SAVE button action.
            if (theEvent.getSource() == mySaveButton) {
                
                myResult  = myFileChooser.showSaveDialog(null);
                
                if (myResult == JFileChooser.APPROVE_OPTION) {
                    try {
                        myImage.save(myFileChooser.getSelectedFile());
                    
                    } catch (final IOException e) {
                        JOptionPane.showMessageDialog(null, "The file can't be written!");
                    }
                }
            }
            
            // Handle CLOSE button action.
            if (theEvent.getSource() == myCloseButton) {
                myCenterLabel.setIcon(null);
                
                // disable option buttons
                mySaveButton.setEnabled(false);
                myCloseButton.setEnabled(false);
                
                // disable filter buttons
                for (int i = 0; i < NUMBER_OF_FILTERS; i++) {
                    myButtonList.get(i).setEnabled(false);
                }
                
                myFrame.setMinimumSize(DEFAULT_FRAME_SIZE);
                myFrame.pack();
                myFrame.setLocation(SCREEN_WIDTH / 2 - myFrame.getWidth() / 2, 
                                    SCREEN_HEIGHT / 2 - myFrame.getHeight() / 2);
            }
        }
        
        
        // An openButtonAction method was taken out to reduce 
        // the number of cyclomatic complexity
        /**
         * This method opens supported image file and display it.
         */
        private void openButtonAction() {
            myResult = myFileChooser.showOpenDialog(myFrame);
            
            if (myResult == JFileChooser.APPROVE_OPTION) {
                myFile = myFileChooser.getSelectedFile();
                
                try {
                    myImage = PixelImage.load(myFile);
                    myCenterLabel.setIcon(new ImageIcon(myImage));
                    
                    // an image file was opened successfully so enable option buttons
                    mySaveButton.setEnabled(true);
                    myCloseButton.setEnabled(true);
                    
                    for (int i = 0; i < NUMBER_OF_FILTERS; i++) {
                        myButtonList.get(i).setEnabled(true);
                    }
                
                } catch (final IOException e) {
                    JOptionPane.showMessageDialog(null, "The selected " + myFile.getName() 
                                                  + " file did not contain an image!");
                }
                
                // Call setMininumSize method to properly resize window if
                // several images was opened without closing previous one.
                myFrame.setMinimumSize(DEFAULT_FRAME_SIZE);
                myFrame.pack();
                
                myFrame.setMinimumSize(new Dimension(myFrame.getWidth(), myFrame.getHeight()));
                
                myFrame.setLocation(SCREEN_WIDTH / 2 - myFrame.getWidth() / 2, 
                                    SCREEN_HEIGHT / 2 - myFrame.getHeight() / 2);
            }
        }
    }
    
} 