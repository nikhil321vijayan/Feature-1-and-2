package com.horstmann.violet.workspace.sidebar.constrainttools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.PanelUI;

import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.workspace.sidebar.colortools.ColorChoice;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

public class ConstraintToolsBarPanelUI extends PanelUI
{
    public ConstraintToolsBarPanelUI(ConstraintToolsBarPanel constraintToolsPanel)
    {
        this.constraintToolsPanel = constraintToolsPanel;
    }
    
    @Override
    public void installUI(JComponent c)
    {
        c.removeAll();
        this.constraintToolsPanel.setBackground(ThemeManager.getInstance().getTheme().getSidebarElementBackgroundColor());
        this.constraintToolsPanel.add(getPanel());
    }
    
    private JPanel getPanel()
    {
        if (this.panel == null)
        {
            this.panel = new JPanel();
            this.panel.setOpaque(false);
            this.panel.setBorder(new EmptyBorder(0, 0, 0, 0));
            this.panel.setPreferredSize(new Dimension(215, 100));
            FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 10, 5);
            this.panel.setLayout(layout);
            for (ConstraintTool aConstraintButton : getConstraintToolList())
            {
            	//System.out.println("zhixingyici");
                this.panel.add(aConstraintButton);
            }
        }
        return this.panel;
    }
    
    public List<ConstraintTool> getConstraintToolList()
    {
        if (this.constraintToolList == null)
        {
            this.constraintToolList = new ArrayList<ConstraintTool>();
            for (ConstraintChoice aChoice : ConstraintToolsBarPanel.CHOICE_LIST)
            {
            	ConstraintTool aConstraintTool = getConstraintTool(aChoice);
                this.constraintToolList.add(aConstraintTool);
            }
        }
        return this.constraintToolList;
    }
    
    private ConstraintTool getConstraintTool(final ConstraintChoice constraintChoice)
    {
        final ConstraintTool aConstraintTool = new ConstraintTool(constraintChoice);
        //System.out.println("This constraint tool NO = "+aConstraintTool.constraintChoice.getConstraintNo());
        aConstraintTool.setText(constraintChoice.getConstraintName());
        aConstraintTool.setSelected(constraintChoice.getTruthValue());
        aConstraintTool.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {            	
            	constraintToolsPanel.fireConstraintChoiceChanged(new ConstraintChoice(aConstraintTool.constraintChoice.getConstraintNo(), aConstraintTool.constraintChoice.getConstraintName(), aConstraintTool.isSelected()));
            }
        });
        return aConstraintTool;
    }
    
    public class ConstraintTool extends JCheckBox//每个限制工具是一个复选按钮
    {
        public ConstraintTool(ConstraintChoice constraintChoice)
        {
            this.constraintChoice = constraintChoice;
            setPreferredSize(new Dimension(200, 20));
        }
        public void setBorderPaintable(boolean isBorderPaintable)
        {
            this.isBorderPaintable = isBorderPaintable;
        }
        
        private boolean isBorderPaintable = false;
        private ConstraintChoice constraintChoice;
        
    }
    
    private JPanel panel;
    private ConstraintToolsBarPanel constraintToolsPanel;
    private List<ConstraintTool> constraintToolList;//限制工具列表，里面是两个选项,每个选项是一个选择框
}
