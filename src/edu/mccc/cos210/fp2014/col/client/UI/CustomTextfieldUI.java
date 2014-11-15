package edu.mccc.cos210.fp2014.col.client.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.BorderFactory;
import javax.swing.plaf.ComponentUI;
import javax.swing.JComponent;

public class CustomTextfieldUI 
  extends BasicTextFieldUI implements FocusListener {

  private boolean hideOnFocus;
  private Color color;

  public CustomTextfieldUI(){}

  public static ComponentUI createUI(JComponent c) {
    return new CustomTextfieldUI();
  }
  public Color getColor() {
   return color;
  }
  public void setColor(Color color) {
    this.color = color;
    repaint();
  }
  private void repaint() {
    if(getComponent() != null) {
      getComponent().repaint();           
    }
  }
  public boolean isHideOnFocus() {
    return hideOnFocus;
  }
  public void setHideOnFocus(boolean hideOnFocus) {
    this.hideOnFocus = hideOnFocus;
    repaint();
  }
  @Override
  protected void paintSafely(Graphics g) {
    super.paintSafely(g);
    JTextComponent comp = getComponent();
    if(
      comp.getText().length() == 0 && 
      (!(hideOnFocus && comp.hasFocus()))
      ){
        if(color != null) {
          g.setColor(color);
        } else {
          g.setColor(color);              
        }
        int padding = (
          (comp.getHeight() - comp.getFont().getSize()) 
          /2
        );         
    }
    if(comp.isFocusOwner()){
      comp.setBorder(
        new CompoundBorder(
          new LineBorder(
            new Color(33, 118, 199, 90),
            2,
            true
            ),
          BorderFactory.createMatteBorder(
            5, 10, 10, 15, Color.WHITE)
        )
      );
    }else{
      comp.setBorder(
        new CompoundBorder(
          new LineBorder(
            new Color(33, 118, 199, 50),
            2,
            true
          ),
          BorderFactory.createMatteBorder(
            5, 10, 10, 15, Color.WHITE)
          )
        );
    }
    g.dispose();
  }
  @Override
  protected void installListeners() {
    super.installListeners();
    getComponent().addFocusListener(this);
  }
  @Override
  protected void uninstallListeners() {
    super.uninstallListeners();
    getComponent().removeFocusListener(this);
  }
  public void focusGained(FocusEvent e) {}
  public void focusLost(FocusEvent e) {}
}