package edu.iastate.cs319.javapainter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sourceforge.napkinlaf.NapkinLookAndFeel;
import edu.iastate.cs319.javapainter.DrawingCanvas.Brush;

public class JavaPainter extends JFrame {

    public JavaPainter() {
        setupComponents();
        setExtraFeaturesEnabled(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800,600);
        setResizable(false);
    }
    
    @Override
    public void setVisible(boolean visible) {
        LoginDialog dialog = new LoginDialog(this, 3);
        dialog.setVisible(true);
    }
    
    public void open(String username) {
        super.setVisible(true);
        setTitle(username + " | Draw" + (mTabbedPane.getSelectedIndex() + 1));
    }
    
    public boolean isExtraFeaturesEnabled() {
        return mExtraFeaturesEnabled;
    }
    
    public void setExtraFeaturesEnabled(boolean enabled) {
        
        mExtraFeaturesEnabled = enabled;
        getJMenuBar().setVisible(!mExtraFeaturesEnabled);
        mToggleMenuButton.setSelected(false);

        final ActionListener shapeChangeListener = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
               
                Brush brush;
                if (e.getSource() == mRectangleButton) {
                    brush = DrawingCanvas.Brush.RECTANGLE;
                }
                else if (e.getSource() == mOvalButton) {
                    brush = DrawingCanvas.Brush.OVAL;
                } 
                else if(e.getSource() == mLineButton) {
                    brush = DrawingCanvas.Brush.LINE;
                } 
                else if (e.getSource() == mTextButton && mExtraFeaturesEnabled) {
                    brush = DrawingCanvas.Brush.TEXT;
                } 
                else {
                    brush = DrawingCanvas.Brush.DISABLED;
                }
                for(int i = 0; i < mTabbedPane.getComponentCount(); i ++) {
                    DrawingCanvas canvas = (DrawingCanvas)mTabbedPane.getComponentAt(i);
                    canvas.setBrush(brush);
                }
            }

        };
        
        ActionListener clearPanelListener = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                Component c = mTabbedPane.getSelectedComponent();
                DrawingCanvas panel = (DrawingCanvas) c;
                panel.clear();
            }
        };
    

        if(enabled) {
            mButtonPanel.setVisible(false);
            mButtonPanel.removeAll();
            mToolBar.setVisible(true);
            
            mLineButton  = new JToggleButton("Line", new ImageIcon(Boot.class.getResource("resources/icons/draw-freehand.png")));
            mOvalButton = new JToggleButton("Oval",new ImageIcon(Boot.class.getResource("resources/icons/draw-ellipse.png")));
            mRectangleButton = new JToggleButton("Rectangle", new ImageIcon(Boot.class.getResource(   "resources/icons/draw-rectangle.png")));
            mTextButton = new JToggleButton("Text", new ImageIcon(Boot.class.getResource(  "resources/icons/text-field.png")));
            mClearButton = new JButton("Clear", new ImageIcon(Boot.class.getResource( "resources/icons/draw-eraser.png")));
            
            if(mToolBar.getComponentCount() <= 5) {
                mToolBar.add(mToggleMenuButton);
                mToolBar.add(mPrintButton);
                mToolBar.add(mStrokeColorButton);
                mToolBar.add(mFillColorButton);
                mToolBar.add(mClearButton);
                mToolBar.addSeparator();
                mToolBar.add(mLineButton);
                mToolBar.add(mRectangleButton);
                mToolBar.add(mOvalButton);
                mToolBar.add(mTextButton);
            }
        } else {
            mToolBar.setVisible(false);
            mToolBar.removeAll();
            mButtonPanel.setVisible(true);
            
            mLineButton = new JToggleButton("Line");
            mOvalButton = new JToggleButton("Oval");
            mRectangleButton = new JToggleButton("Rectangle");
            mTextButton = new JToggleButton("Text");
            mClearButton = new JButton("Clear");
            
            if(mButtonPanel.getComponentCount() == 0) {
                mButtonPanel.add(mLineButton);
                mButtonPanel.add(mOvalButton);
                mButtonPanel.add(mRectangleButton);
                mButtonPanel.add(mTextButton);
                mButtonPanel.add(mClearButton);
            }
        }  
        
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(mLineButton);
        buttonGroup.add(mOvalButton);
        buttonGroup.add(mRectangleButton);
        buttonGroup.add(mTextButton);
        buttonGroup.setSelected(mLineButton.getModel(), true);
        
        mLineButton.addActionListener(shapeChangeListener);
        mOvalButton.addActionListener(shapeChangeListener);
        mRectangleButton.addActionListener(shapeChangeListener);
        mTextButton.addActionListener(shapeChangeListener);
        mClearButton.addActionListener(clearPanelListener);
        


    }
    
    public void setNapkinLookAndFeel(boolean enabled) {
        try {
            if(enabled) {
                mPreviousLookAndFeel = UIManager.getLookAndFeel().getClass().getName();
                UIManager.setLookAndFeel(new NapkinLookAndFeel());
            } else {
                if(mPreviousLookAndFeel != null) {
                    UIManager.setLookAndFeel(mPreviousLookAndFeel);
                } else {
                    System.out.println("null");
                }
            }
            SwingUtilities.updateComponentTreeUI(this);
            pack();
        } catch(Exception e) {
            System.err.println("Exception");
            System.err.println(e.getMessage());
        }

    } 
    
    //--------------------------------------------------------- Private Methods
    
    private void setupComponents() {

        ActionListener openPrintDialogListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPrintDialog();
            }
        };  
        
        ActionListener openSettingsListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final JDialog dialog = new JDialog(JavaPainter.this);
                dialog.setLocationRelativeTo(JavaPainter.this);
                dialog.setSize(200, 100);
                dialog.setTitle("Settings");
                dialog.setLayout(new GridLayout(3,1));
                
                final boolean enableExtraFeaturesBefore = mExtraFeaturesEnabled;
                final JCheckBox enableExtraCb = new JCheckBox("Enable Extra Features");
                enableExtraCb.setSelected(enableExtraFeaturesBefore);
                
                final JCheckBox enableNapkinCb = new JCheckBox("Enable Napkin Look & Feel");
                final boolean enableNapkinLafBefore = UIManager.getLookAndFeel().getName().compareTo("Napkin")==0;
                enableNapkinCb.setSelected(enableNapkinLafBefore);
                
                JButton saveButton = new JButton("Save") ;
                saveButton.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        if(enableExtraCb.isSelected() == !enableExtraFeaturesBefore) {
                            JavaPainter.this.setExtraFeaturesEnabled(enableExtraCb.isSelected());
                            SwingUtilities.updateComponentTreeUI(JavaPainter.this);
                        }
                        if(enableNapkinCb.isSelected() == !enableNapkinLafBefore) {
                            setNapkinLookAndFeel(enableNapkinCb.isSelected());
                        }
                        dialog.setVisible(false);
                    }
                });
                
                dialog.add(enableExtraCb);
                dialog.add(enableNapkinCb);
                dialog.add(saveButton);
                dialog.setVisible(true);
            }
        };
        
        ActionListener toggleMenuBarListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getJMenuBar().setVisible(!getJMenuBar().isVisible());
            }
        };
        
        ActionListener changeColorListener = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == mStrokeColorButton) {
                    Color currentColor = mCurrentCanvas.getBrush().getColor();
                    Color c = JColorChooser.showDialog(JavaPainter.this, "Change Brush Color", currentColor);
                    mCurrentCanvas.getBrush().setColor(c);
                } else {
                    Color currentColor = mCurrentCanvas.getBrush().getBackgroundColor();
                    Color c = JColorChooser.showDialog(JavaPainter.this, "Change Brush Color", currentColor);
                    mCurrentCanvas.getBrush().setBackgroundColor(c);
                }
            }
        };

        
        // Setup Menu...
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('v');
        JMenu printMenu = new JMenu("Print");
        printMenu.setMnemonic('p');
        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.setMnemonic('S');
        settingsItem.addActionListener(openSettingsListener);
        printMenu.add(settingsItem);
        JMenuItem previewItem = new JMenuItem("Preview");
        previewItem.setMnemonic('P');
        previewItem.addActionListener(openPrintDialogListener);
        printMenu.add(previewItem);

        JMenuBar menubar = new JMenuBar();
        menubar.add(viewMenu);
        menubar.add(printMenu);
        setJMenuBar(menubar);

        // Setup tabs and add menu entries for each one...
        mTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        mTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(e.getSource() == mTabbedPane) {
                    String title = getTitle();
                    if(title.length() > 0) {
                        Brush currentBrush = mCurrentCanvas.getBrush();
                        mCurrentCanvas = (DrawingCanvas)mTabbedPane.getSelectedComponent();
                        mCurrentCanvas.setBrush(currentBrush);
                        setTitle(title.substring(0, title.length() -1) + (mTabbedPane.getSelectedIndex() + 1));
                    }
                }
            }
        });
        
        int[] keyEvents = new int[] { KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4 };
        for(int i = 0; i < 4; i++) {
            
            final int drawIndex = i;
            int itemNameIndex = i+1;
            
            JMenuItem drawMenuItem = new JMenuItem("Draw" + itemNameIndex);
            drawMenuItem.setMnemonic(keyEvents[i]);
            drawMenuItem.addActionListener( new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mTabbedPane.setSelectedIndex(drawIndex);
                    mCurrentCanvas = (DrawingCanvas) mTabbedPane.getSelectedComponent();
                    
                }
            });

            viewMenu.add(drawMenuItem);
            DrawingCanvas drawingPanel = new DrawingCanvas();
            drawingPanel.setBrush(Brush.LINE);
            mTabbedPane.add("Draw" + itemNameIndex, drawingPanel);
            
        }
        mCurrentCanvas = (DrawingCanvas)mTabbedPane.getComponentAt(0);
        
        // Extra Features
        mToggleMenuButton = new JToggleButton("Menu", new ImageIcon(
                Boot.class.getResource(  "resources/icons/show-menu.png")));
        mToggleMenuButton.addActionListener(toggleMenuBarListener);
        mStrokeColorButton = new JButton("Stroke", new ImageIcon(Boot.class.getResource("resources/icons/format-stroke-color.png")));
        mStrokeColorButton.addActionListener(changeColorListener);
        mFillColorButton = new JButton("Fill", new ImageIcon(Boot.class.getResource("resources/icons/fill-color.png")));
        mFillColorButton.addActionListener(changeColorListener);
        mPrintButton = new JButton("Print", new ImageIcon(Boot.class.getResource("resources/icons/document-print.png")));
        mPrintButton.addActionListener(openPrintDialogListener);

        mToolBar = new JToolBar();    
        mButtonPanel = new JPanel();
        mButtonPanel.setLayout(new GridLayout(1,5, 0, 25));
        setExtraFeaturesEnabled(false); // Button's created & listeners added
        
        // Add components to the content pane
        getContentPane().add(mToolBar, BorderLayout.SOUTH);
        getContentPane().add(mButtonPanel, BorderLayout.NORTH);
        getContentPane().add(mTabbedPane, BorderLayout.CENTER);
        
    }
    
    private void openPrintDialog() {
        mPrintDialog = new JDialog();
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton printButton = new JButton("Print");
        if(mExtraFeaturesEnabled) {
        	printButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                mPrintDialog.setVisible(false);
            }
        });
        }
        buttonPanel.add(printButton);
        
        JPanel previewPanel = new JPanel();
        JLabel preview = new JLabel(new ImageIcon(mCurrentCanvas.getSnapshot()));
        preview.setSize(mCurrentCanvas.getWidth(), mCurrentCanvas.getHeight());
        previewPanel.add(preview);
        previewPanel.invalidate();
        
        Box layoutBox = Box.createVerticalBox();
        layoutBox.add(previewPanel);
        layoutBox.add(buttonPanel);

        mPrintDialog.getContentPane().add(layoutBox, BorderLayout.CENTER);
        mPrintDialog.setTitle("Draw" + mTabbedPane.getSelectedIndex()+ " | Print Preview");
        mPrintDialog.setResizable(false);
        mPrintDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        mPrintDialog.pack();
        mPrintDialog.setVisible(true);
    }
    
    //---------------------------------------------------------- Private Fields

    private JTabbedPane mTabbedPane;
    private JToggleButton mLineButton;
    private JToggleButton mOvalButton;
    private JToggleButton mRectangleButton;
    private JToggleButton mTextButton;
    private JToggleButton mToggleMenuButton;
    private JButton mClearButton;
    private JButton mStrokeColorButton;
    private JButton mFillColorButton;
    private JButton mPrintButton;
    private JToolBar mToolBar;
    private DrawingCanvas mCurrentCanvas;
    private JDialog mPrintDialog;
    private JPanel mButtonPanel;
    private boolean mExtraFeaturesEnabled;
    private String mPreviousLookAndFeel;
}
