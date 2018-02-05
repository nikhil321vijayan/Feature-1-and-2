package com.horstmann.violet.workspace.editorpart.behavior;

import java.util.Collection;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.horstmann.violet.product.diagram.abstracts.Id;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.IColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.sidebar.constrainttools.ConstraintChoice;
import com.horstmann.violet.workspace.sidebar.constrainttools.ConstraintToolsBarPanelUI.ConstraintTool;
import com.horstmann.violet.workspace.sidebar.constrainttools.IConstraintBar;
import com.horstmann.violet.workspace.sidebar.constrainttools.IConstraintChangeListener;

public class ConstraintBehavior extends AbstractEditorPartBehavior
{	    
    public ConstraintBehavior(final IWorkspace workspace, final IConstraintBar constraintBar)
    {	    
        this.behaviorManager = workspace.getEditorPart().getBehaviorManager();
        constraintBar.addConstraintChangeListener(new IConstraintChangeListener()
        {
			@Override
            public void onConstraintChoiceChange(ConstraintChoice newConstraintChoice)
            {

            	if(newConstraintChoice.getConstraintNo()==0)//first constraint
            	{
            		if(!newConstraintChoice.getTruthValue())//ȡ������
            		{
            			Collection<IEdge> allEdges = workspace.getEditorPart().getGraph().getAllEdges();//ȡ���еı�
            			//System.out.println(allEdges.size());
            			boolean canChange=true;
            			HashMap<Id,Integer> counter=new HashMap<Id,Integer>();
            			for(IEdge anEdge:allEdges)
            			{
            				//System.out.println(anEdge.getToolTip());
            				if((anEdge.getToolTip().equals("Is composed of")||anEdge.getToolTip().equals("Is an aggregate of"))&&(anEdge.getStartNode().getId().equals(anEdge.getEndNode().getId())))//����������Ǿۺϻ��������βΪͬһ���ڵ�
            				{
            					Integer currentCounter=null;
            					currentCounter=counter.get(anEdge.getStartNode().getId());
            					if(currentCounter==null)
            					{
            						counter.put(anEdge.getStartNode().getId(),1);
            					}
            					else 
            					{
            						currentCounter++;
            						counter.put(anEdge.getStartNode().getId(), currentCounter);
            					}
            				}
            			}
            			for(Integer count:counter.values())
            			{
            				//System.out.println("jishu="+count);
            				if(count>1)
            				{
            					canChange=false;
            					System.out.println("canChange=false");
            				}
            			}
            			if(canChange)
            			{
//            				Collection<INode> allNodes=workspace.getEditorPart().getGraph().getAllNodes();
//            				for(INode aNode:allNodes)
//            				{
//                        		if (aNode != null && IColorableNode.class.isInstance(aNode)) 
//                        		{
//                                	IColorableNode colorableElement = (IColorableNode) aNode;
//                                	updateConstraint(0,colorableElement, newConstraintChoice,workspace);
//                                	//System.out.println(workspace.getConstraint1());
//                        		}
//            				}
            				workspace.setConstraint1(false);
            			}
            			else
            			{
        					ConstraintTool cTool=constraintBar.getUI().getConstraintToolList().get(0);
        					cTool.setSelected(true);
        					constraintBar.getUI().getConstraintToolList().set(0, cTool);
        					canChange=false;
        					JOptionPane.showConfirmDialog(null, "Delete relevant edges before you cancel this constraint.","Warning", JOptionPane.WARNING_MESSAGE);
            			}
            		}
            		else
            		{
            			workspace.setConstraint1(true);
            		}
            	}
            }
        });
    }
    
//    private void updateConstraint(int constraintNo,IColorableNode colorableElement, ConstraintChoice newConstraintChoice,IWorkspace workspace) 
//    {
//    	if(constraintNo==0)
//    	{
//    		workspace.setConstraint1(newConstraintChoice.getTruthValue());
//    	}    	
//    }
    
    private IEditorPartBehaviorManager behaviorManager;

}
