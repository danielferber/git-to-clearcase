package org.gpl.validation;

import java.awt.Color;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.border.Border;
import org.gpl.border.IconBorder;

/**
 * This class handles all the details of validation and
 * decorating the component
 * @author Naveed Quadri
 */
public abstract class ErrorProvider extends InputVerifier {

    private Border originalBorder;
    private Color originalBackgroundColor;
    private String originalTooltipText;
    private Object parent;

    /**
     *
     * @param c The JComponent to be validated.
     */
    public ErrorProvider(JComponent c) {
        originalBorder = c.getBorder();
        originalBackgroundColor = c.getBackground();
        originalTooltipText = c.getToolTipText();
        
    }

    /**
     *
     * @param parent A JFrame that implements the ValidationStatus interface.
     * @param c The JComponent to be validated.
     */
    public ErrorProvider(JFrame parent, JComponent c) {
        this(c);
        this.parent = parent;
    }

    /**
     * 
     * @param parent A JDialog that implements the ValidationStatus interface.
     * @param c The JComponent to be validated.
     */
    public ErrorProvider(JDialog parent, JComponent c) {
        this(c);
        this.parent = parent;
    }

    /**
     * Define your custom Error in this method and return an Error Object.
     * @param c The JComponent to be validated.
     * @return Error
     * @see Error
     */
    protected abstract Error ErrorDefinition(JComponent c);

    /**
     * This method is called by Java when a component needs to be validated.
     * @param c The JCOmponent being validated
     * @return
     */
    @Override
    public boolean verify(JComponent c) {
        Error error = ErrorDefinition(c);
        if (error.getErrorType() == Error.NO_ERROR) {
            //revert back all changes made to the component
            c.setBackground(originalBackgroundColor);
            c.setBorder(originalBorder);
            c.setToolTipText(originalTooltipText);
        } else {
            c.setBorder(new IconBorder(error.getImage(), originalBorder));
            c.setBackground(error.getColor());
            c.setToolTipText(error.getMessage());
        }
        if (error.getErrorType() == Error.ERROR) {
            if (parent instanceof ValidationStatus) {
                ((ValidationStatus) parent).reportStatus(false);
            }
            return false;
        } else {
            if (parent instanceof ValidationStatus) {
                ((ValidationStatus) parent).reportStatus(true);
            }
            return true;
        }

    }
}
