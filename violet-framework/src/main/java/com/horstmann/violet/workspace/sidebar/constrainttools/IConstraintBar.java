package com.horstmann.violet.workspace.sidebar.constrainttools;

import com.horstmann.violet.workspace.sidebar.ISideBarElement;
import com.horstmann.violet.workspace.sidebar.colortools.IColorChoiceChangeListener;

public interface IConstraintBar extends ISideBarElement
{

    public void addConstraintChangeListener(IConstraintChangeListener listener);
    
    public ConstraintToolsBarPanelUI getUI();
    
}