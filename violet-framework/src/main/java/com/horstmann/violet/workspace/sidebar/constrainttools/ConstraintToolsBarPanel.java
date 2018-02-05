package com.horstmann.violet.workspace.sidebar.constrainttools;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.sidebar.SideBar;

@ResourceBundleBean(resourceReference = SideBar.class)
public class ConstraintToolsBarPanel extends JPanel implements IConstraintBar
{

    public ConstraintToolsBarPanel()
    {
        ResourceBundleInjector.getInjector().inject(this);
        this.ui = new ConstraintToolsBarPanelUI(this);
        setUI(this.ui);
    }
	
    public ConstraintToolsBarPanelUI getUI()
    {
    	return this.ui;
    }
    
	@Override
	public void install(IWorkspace workspace)
	{
		// TODO Auto-generated method stub
		this.diagramPanel = workspace;
	}

	@Override
	public Component getAWTComponent()
	{
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void addConstraintChangeListener(IConstraintChangeListener listener)
	{
		// TODO Auto-generated method stub
		
		this.constraintChangeListenersList.add(listener);
	}

    public void fireConstraintChoiceChanged(ConstraintChoice newConstraintChoice)
    {
        for (IConstraintChangeListener aListener : this.constraintChangeListenersList)
        {
        	//System.out.println("click con");
            aListener.onConstraintChoiceChange(newConstraintChoice);
        }
    }
	
	public static ConstraintChoice DEFAULT_CONSTRAINT1=new ConstraintChoice(0,"allow multi/different recursions",true);
	public static ConstraintChoice DEFAULT_CONSTRAINT2=new ConstraintChoice(1,"allow class mutual consisting",true);
	public static ConstraintChoice DEFAULT_CONSTRAINT3=new ConstraintChoice(2,"show CBO",true);
	private List<IConstraintChangeListener> constraintChangeListenersList = new ArrayList<IConstraintChangeListener>();
	protected static List<ConstraintChoice> CHOICE_LIST = new ArrayList<ConstraintChoice>();
	private IWorkspace diagramPanel;
	private ConstraintToolsBarPanelUI ui;
	
    static
    {
        CHOICE_LIST.add(DEFAULT_CONSTRAINT1);
        CHOICE_LIST.add(DEFAULT_CONSTRAINT2);
        CHOICE_LIST.add(DEFAULT_CONSTRAINT3);
    }
}
