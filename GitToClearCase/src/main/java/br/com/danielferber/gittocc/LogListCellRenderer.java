/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc;

import ch.qos.logback.classic.spi.ILoggingEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import sun.swing.DefaultLookup;

/**
 *
 * @author X7WS
 */
public class LogListCellRenderer extends JLabel implements ListCellRenderer, Serializable {

    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;
    private static final Color vermelhin = new Color(0xCC, 0x33, 0x66);
    private static final Color verdin = new Color(0x33, 0x99, 0x66);

    /**
     * Constructs a default renderer object for an item in a list.
     */
    public LogListCellRenderer() {
        super();
        setOpaque(true);
        setBorder(getNoFocusBorder());
        setName("List.cellRenderer");
    }

    private Border getNoFocusBorder() {
        Border border = DefaultLookup.getBorder(this, ui, "List.cellNoFocusBorder");
        if (System.getSecurityManager() != null) {
            if (border != null) {
                return border;
            }
            return SAFE_NO_FOCUS_BORDER;
        } else {
            if (border != null
                    && (noFocusBorder == null
                    || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
                return border;
            }
            return noFocusBorder;
        }
    }

    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        setComponentOrientation(list.getComponentOrientation());
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        Border border = null;
        if (cellHasFocus) {
            if (isSelected) {
                border = DefaultLookup.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = DefaultLookup.getBorder(this, ui, "List.focusCellHighlightBorder");
            }
        } else {
            border = getNoFocusBorder();
        }
        setBorder(border);

        ILoggingEvent e = (ILoggingEvent) value;

        Color bg = null;
        Color fg = null;
        Font font = getFont();

        if (isSelected) {
            bg = list.getSelectionBackground();
            fg = list.getSelectionForeground();
        } else {
            bg = list.getBackground();
            fg = list.getForeground();
            
            if (e.getMarker() != null) {
                if (e.getMarker().getName().equals("git_cmd")) {
//                    font = font.deriveFont(font.getStyle() | Font.BOLD);
                } else if (e.getMarker().getName().equals("ct_cmd")) {
//                    font = font.deriveFont(font.getStyle() | Font.BOLD);
                } else if (e.getMarker().getName().equals("METER_MSG_OK")) {
                    fg = verdin;
                    font = font.deriveFont(font.getStyle() | Font.BOLD);
                } else if (e.getMarker().getName().equals("METER_MSG_FAIL")) {
                    fg = vermelhin;
                    font = font.deriveFont(font.getStyle() | Font.BOLD);
                }
            }
        }

        String msg = e.getFormattedMessage();

        setForeground(fg);
        setBackground(bg);
        setText(msg);
        setFont(font);

        return this;
    }

    @Override
    public boolean isOpaque() {
        Color back = getBackground();
        Component p = getParent();
        if (p != null) {
            p = p.getParent();
        }
        // p should now be the JList. 
        boolean colorMatch = (back != null) && (p != null)
                && back.equals(p.getBackground())
                && p.isOpaque();
        return !colorMatch && super.isOpaque();
    }

    @Override
    public void validate() {
    }

    @Override
    public void invalidate() {
    }

    @Override
    public void repaint() {
    }

    @Override
    public void revalidate() {
    }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
    }

    @Override
    public void repaint(Rectangle r) {
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        // Strings get interned...
        if (propertyName == "text"
                || ((propertyName == "font" || propertyName == "foreground")
                && oldValue != newValue
                && getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey) != null)) {

            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    @Override
    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, char oldValue, char newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, short oldValue, short newValue) {
    }

    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, long oldValue, long newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, float oldValue, float newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, double oldValue, double newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }

    public static class UIResource extends DefaultListCellRenderer
            implements javax.swing.plaf.UIResource {
    }
}
