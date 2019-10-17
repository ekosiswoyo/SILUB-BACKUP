package Tool;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
 
/**
 * Created with IntelliJ IDEA.
 * Author: Lee
 * Date: 5/20/2014
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class NumberTextField extends JTextField {
    private char separator = '.';
 
    public NumberTextField(){
        super();
        setHorizontalAlignment(SwingConstants.RIGHT);
        setText("0");
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                NumberTextField.super.setText(getText());
                NumberTextField.super.selectAll();
            }
 
            @Override
            public void focusLost(FocusEvent e){
                setText(NumberTextField.super.getText());
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_ENTER) ||
                        (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });
        // */
    }
 
    public void setSeparator(char s){
        separator = s;
    }
 
    @Override
    public void setText(String s){
        super.setText(formatNumber(s));
    }
 
    @Override
    public String getText() {
        String s = super.getText();
        return s.replaceAll("[^0-9]", "");
    }
 
    public String getNumberFormattedText(){
        return super.getText();
    }
 
    public String formatNumber(String s){
        try {
            long v = Long.parseLong(s);
            return String.format("%,d", v).replace(",", separator + "");
        } catch (Exception e) {
            return "0";
        }
    }
    // */
}
