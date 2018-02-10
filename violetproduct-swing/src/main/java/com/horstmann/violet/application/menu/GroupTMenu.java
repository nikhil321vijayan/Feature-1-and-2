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

package com.horstmann.violet.application.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.behavior.CutCopyPasteBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.EditSelectedBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.SelectAllBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.SelectByDistanceBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.UndoRedoCompoundBehavior;

/**
 * Edit menu
 * 
 * @author Alexandre de Pellegrin
 * 
 */
@ResourceBundleBean(resourceReference = MenuFactory.class)
public class GroupTMenu extends JMenu
{

    JCheckBoxMenuItem feature1Menu = new JCheckBoxMenuItem("Enable recursive aggregation/composition",false);
    JCheckBoxMenuItem feature2Menu = new JCheckBoxMenuItem("Enable bidirectional aggregation/composition",false);
    JCheckBoxMenuItem feature3Menu = new JCheckBoxMenuItem("Display CBO",false);

    /**
     * Default constructor
     * 
     * @param mainFrame where is attached this menu
     * @param factory for accessing to external resources
     */
    @ResourceBundleBean(key = "groupt")
    public GroupTMenu(final MainFrame mainFrame)
    {
        ResourceBundleInjector.getInjector().inject(this);
        this.mainFrame = mainFrame;
        this.createMenu();
    }


    /**
     * Initializes menu
     */
    private void createMenu()
    {
    	
    	feature1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
            	
                System.out.println("adding feature1");
            }
        });
        this.add(feature1);
        
        feature1Menu.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.out.println("adding feature2");
                	
            }
        });
        
        this.add(feature1Menu);
        
        feature3Menu.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.out.println("adding feature3");
                if(feature3Menu.isSelected()){
                	
                }
            }
        });
        this.add(feature3Menu);

    }


    /**
     * @return current editor
     */
    private IEditorPart getActiveEditorPart()
    {
        return this.mainFrame.getActiveWorkspace().getEditorPart();
    }

    /**
     * @return true id at least one workspace is reachable
     */
    private boolean isThereAnyWorkspaceDisplayed()
    {
        return mainFrame.getWorkspaceList().size() > 0;
    }
    
    /** Application frame */
    private MainFrame mainFrame;

    @ResourceBundleBean(key = "groupt.feature1")
    private JMenuItem feature1;

    @ResourceBundleBean(key = "groupt.feature2")
    private JMenuItem feature2;

    @ResourceBundleBean(key = "groupt.feature3")
    private JMenuItem feature3;
}
