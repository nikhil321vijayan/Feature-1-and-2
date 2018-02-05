/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.workspace.sidebar;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;
import com.horstmann.violet.workspace.sidebar.colortools.IColorChoiceBar;
import com.horstmann.violet.workspace.sidebar.constrainttools.ConstraintToolsBarPanel;
import com.horstmann.violet.workspace.sidebar.constrainttools.IConstraintBar;
import com.horstmann.violet.workspace.sidebar.editortools.EditorToolsPanel;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphToolsBar;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;
import com.horstmann.violet.workspace.sidebar.optionaltools.OptionalToolsPanel;

public class SideBar extends JPanel implements ISideBar
{

    public SideBar(IWorkspace diagramPanel)
    {
    	//System.out.println("SideBar1");
        this.diagramPanel = diagramPanel;
        setupUI();
    }

    private void setupUI()
    {
    	//System.out.println("SideBar2");
        setUI(new SideBarUI(this));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.horstmann.violet.framework.display.clipboard.sidebar.ISideBar#addElement(com.horstmann.violet.framework.display.clipboard
     * .sidebar.ISideBarElement, java.lang.String)
     */
    public void addElement(ISideBarElement element, String title)
    {
    	//System.out.println("SideBar3");
        element.install(this.diagramPanel);
        this.externalContributionElements.put(element, title);
    }

    public IGraphToolsBar getGraphToolsBar()
    {
    	//System.out.println("SideBar4");
        if (this.graphToolsBar == null)
        {
            this.graphToolsBar = new GraphToolsBar();
            this.graphToolsBar.install(this.diagramPanel);
        }
        return this.graphToolsBar;
    }

    protected ISideBarElement getEditorToolsBar()
    {
    	//System.out.println("SideBar5");
        if (this.editorToolsBar == null)
        {
            this.editorToolsBar = new EditorToolsPanel();
            this.editorToolsBar.install(this.diagramPanel);
        }
        return this.editorToolsBar;
    }

    protected ISideBarElement getOptionalToolsBar()
    {
    	//System.out.println("SideBar6");
        if (this.optionalToolsBar == null)
        {
            this.optionalToolsBar = new OptionalToolsPanel();
            this.optionalToolsBar.install(this.diagramPanel);
        }
        return this.optionalToolsBar;
    }

    public IColorChoiceBar getColorChoiceBar()
    {
    	//System.out.println("SideBar7");
        if (this.colorChoiceBar == null)
        {
            this.colorChoiceBar = new ColorToolsBarPanel();
            this.colorChoiceBar.install(this.diagramPanel);
        }
        //System.out.println("SideBar7.1");
        return this.colorChoiceBar;

    }
    
    public IConstraintBar getConstraintChoiceBar()
    {
    	//System.out.println("SideBar8");
        if (this.constraintBar == null)
        {
            this.constraintBar = new ConstraintToolsBarPanel();
            this.constraintBar.install(this.diagramPanel);
        }
        return this.constraintBar;
    }

    protected Map<ISideBarElement, String> getExternalContributionElements()
    {
    	//System.out.println("SideBar9");
        return this.externalContributionElements;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.sidebar.ISideBar#getAWTComponent()
     */
    public Component getAWTComponent()
    {
    	//System.out.println("SideBar10");
        return this;
    }

    public IWorkspace getDiagramPanel()
    {
    	return this.diagramPanel;
    }
    
    private IWorkspace diagramPanel;
    private IGraphToolsBar graphToolsBar;
    private ISideBarElement editorToolsBar;
    private ISideBarElement optionalToolsBar;
    private IColorChoiceBar colorChoiceBar;
    private IConstraintBar constraintBar;
    private Map<ISideBarElement, String> externalContributionElements = new HashMap<ISideBarElement, String>();

}
