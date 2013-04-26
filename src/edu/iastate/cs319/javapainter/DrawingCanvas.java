package edu.iastate.cs319.javapainter;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class DrawingCanvas extends JPanel implements MouseListener, MouseMotionListener {

    public DrawingCanvas() {
        mMouseState = MouseState.IDLE;
        mBrush = Brush.LINE;
        mBackgroundColor = Color.WHITE;
        mIsEditable = true;
        setMinimumSize(new Dimension(400,300));
        setPreferredSize(new Dimension(600,500));
        setBorder(BorderFactory.createLoweredBevelBorder());
        addMouseListener(this);
        addMouseMotionListener(this);
        
        clear();
    }
    
    public void clear() {
        mImage = null;
        repaint();
    }
    
    public Image getSnapshot() {
        return mImage;
    }
    
    public Brush getBrush() {
        return mBrush;
    }
    
    public void setBrush(Brush brush) {
        mBrush = brush;
        setCursor(Cursor.getPredefinedCursor(mBrush.getCursor()));
    }

    public boolean isEditable() {
        return mIsEditable;
    }
    
    public void setEditable(boolean flag) {
        mIsEditable = flag;
    }
    //--------------------------------------------------------------- Container
    
    @Override
    public void paint(Graphics g) {
        if(!mIsEditable) {
            super.paint(g);
            return;
        }
        Graphics2D g2 = (Graphics2D)g;
        
        if(mImage == null) {
            mImage = (BufferedImage)this.createImage(getWidth(), getHeight());
            Graphics2D g2image = mImage.createGraphics();
            g2image.setColor(mBackgroundColor);
            g2image.fillRect(0, 0, getWidth(), getHeight());
        } 
        
        g2.drawImage(mImage, null, 0, 0);
        
        if(mMouseState == MouseState.DRAGGING) {
            mBrush.draw(g2, mStartPoint, mEndPoint);
        }

    }

    //----------------------------------------------------- MouseMotionListener
    
    @Override
    public void mouseDragged(MouseEvent e) {
        mMouseState = MouseState.DRAGGING;
        mEndPoint = e.getPoint();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) { }
    
    //----------------------------------------------------------- MouseListener
    
    @Override
    public void mousePressed(MouseEvent e) {
        mMouseState = MouseState.DRAGGING;
        mStartPoint = e.getPoint();
        mEndPoint = mStartPoint;
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        mEndPoint = e.getPoint();
        if(mMouseState == MouseState.DRAGGING) {
            mMouseState = MouseState.IDLE;
            mBrush.draw(mImage.createGraphics(), mStartPoint, mEndPoint);
            repaint();    

        }
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e) { 
        if(mBrush == Brush.TEXT) {
            mBrush.draw(mImage.createGraphics(), mStartPoint, mEndPoint);
        }
    }
    
    @Override
    public void mouseExited(MouseEvent e) { }

    //------------------------------------------------------------- Inner Types

    public enum MouseState {
        DRAGGING,
        IDLE
    }
    
    public enum Brush {

        DISABLED {

            @Override
            void draw(Graphics2D g2, Point p1, Point p2) {
                return;
            }

            @Override
            int getCursor() {
                return Cursor.TEXT_CURSOR;
            }
            
        },
         
        RECTANGLE {
            
            @Override
            int getCursor() {
                return Cursor.CROSSHAIR_CURSOR;
            }

            @Override
            void draw(Graphics2D g2, Point p1, Point p2) {
                g2.setColor(mColor);
                g2.drawRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
//                g2.setColor(mBgColor);
//                g2.fillRect(p1.x+1, p1.y+1, p2.x-p2.y+1, p2.x-p1.y+1);
            }

        },
        
        OVAL {

            @Override
            int getCursor() {
                return Cursor.CROSSHAIR_CURSOR;
            }

            @Override
            void draw(Graphics2D g2, Point p1, Point p2) {
                g2.setColor(mColor);
                g2.drawOval(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
//                g2.setColor(mBgColor);
//                g2.fillOval(p1.x+1, p1.y+1, p2.x-p1.x-1, p2.x-p1.x-1);
            }
            
        },
        
        LINE {
            
            @Override
            int getCursor () {
                return Cursor.CROSSHAIR_CURSOR;
            }

            @Override
            void draw(Graphics2D g2, Point p1, Point p2) {
                g2.setColor(mColor);
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            
        },
        
        TEXT {

            @Override
            void draw(Graphics2D g2, Point p1, Point p2) {
                String text = JOptionPane.showInputDialog("Text: ");
                if(text!= null) {
                    g2.setColor(mColor);
                    g2.drawString(text, p1.x, p1.y);
                }
            }

            @Override
            int getCursor() {
                return Cursor.TEXT_CURSOR;
            }
            
            
        };
       
        
        abstract void draw(Graphics2D g2, Point p1, Point p2);
        
        abstract int getCursor();
        
        public Color getColor() {
            return mColor;
        }
        
        public void setColor(Color c) {
            mColor = c;
        }
        
        public Color getBackgroundColor() {
            return mBgColor;
        }
        
        public void setBackgroundColor(Color c) {
            mBgColor = c;
        }
        
        private static Color mColor = Color.BLACK;
        private static Color mBgColor = Color.WHITE;
    }
    
    //---------------------------------------------------------- Private Fields

    private BufferedImage mImage;
    private MouseState mMouseState;
    private Point mStartPoint;
    private Point mEndPoint;
    private Brush mBrush;
    private Color mBackgroundColor;
    private boolean mIsEditable;   
}