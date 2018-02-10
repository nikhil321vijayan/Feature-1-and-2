package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;//
import java.util.HashMap;//
import java.util.List;

import javax.swing.JOptionPane;//
import javax.swing.SwingUtilities;

import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.IGridSticker;
import com.horstmann.violet.product.diagram.abstracts.Id;
import com.horstmann.violet.product.diagram.abstracts.edge.AbstractEdge;//
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.common.node.PointNode;
import com.horstmann.violet.workspace.IWorkspace;//
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBarMouseListener;

public class AddEdgeBehavior extends AbstractEditorPartBehavior implements IGraphToolsBarMouseListener
{

    public AddEdgeBehavior(final IWorkspace workspace,IEditorPart editorPart, IGraphToolsBar graphToolsBar)
    {
    	//System.out.println("Add edge constructor");
    	BeanInjector.getInjector().inject(this);
        ResourceBundleInjector.getInjector().inject(this);
        this.workspace=workspace;
    	this.editorPart = editorPart;
        this.graph = editorPart.getGraph();
        this.grid = editorPart.getGraph().getGridSticker();
        this.selectionHandler = editorPart.getSelectionHandler();
        this.behaviorManager = editorPart.getBehaviorManager();
        this.graphToolsBar = graphToolsBar;
        this.dragging = false;
        graphToolsBar.addMouseListener(this);
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
    	//System.out.println("onMousePressed");
        if (!isConditionOK(event))
        {
            cancel();
            return;
        }
        if (!this.isLinkingInProgress)
        {
 
            startAction(event);
            return;
        }
        if (this.isLinkingInProgress && this.isLinkBySeparatedClicks)
        {
            if (isRecognizedAsTransitionAction())
            {
                transitionAction(event);
                return;
            }
            endAction(event);
            return;
        }
    }

    //feature1
    /*public boolean isConstrainted(Point2D startPoint,IEdge newEdge)
    {
    	if((!newEdge.getToolTip().equals("Is composed of")&&!newEdge.getToolTip().equals("Is an aggregate of"))||workspace.getConstraint1())
    	{
    		return false;
    	}
    	Collection<IEdge> allEdges = workspace.getEditorPart().getGraph().getAllEdges();//取所有的边
    	
		HashMap<Id,Integer> counter=new HashMap<Id,Integer>();
		for(IEdge anEdge:allEdges)
		{
			if((anEdge.getToolTip().equals("Is composed of")||anEdge.getToolTip().equals("Is an aggregate of"))&&(anEdge.getStartNode().getId().equals(anEdge.getEndNode().getId())))//如果有条边是聚合或组成且首尾为同一个节点
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
		
		INode startNode = graph.findNode(startPoint);
		boolean hasRecursiveEdge=false;
		for(Id id:counter.keySet())
		{
			if(startNode.getId().equals(id))
			{
				hasRecursiveEdge=true;
				//System.out.println(workspace.getConstraint1()+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}
		}
		
		if(hasRecursiveEdge&&!workspace.getConstraint1())
		{
			return true;
		}
		else
		{
			return false;
		}
    }*/
     
    @Override
    public void onMouseDragged(MouseEvent event)
    {
    	
        if (!this.isLinkingInProgress)
        {
            return;
        }
        //System.out.println("onMouseDragged");
        repaintOnMouseMoved(event);
    }

    @Override
    public void onMouseMoved(MouseEvent event)
    {
    	
        if (!this.isLinkingInProgress)
        {
        	//System.out.println("is Not LinkingInProgress");
            return;
        }
        //System.out.print("onMouseMoved");
        this.isLinkBySeparatedClicks = true;
        repaintOnMouseMoved(event);
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
    	//System.out.println("onMouseReleased");
        this.editorPart.getSwingComponent().invalidate();
        if (this.isLinkBySeparatedClicks)
        {
            return;
        }
        if (this.isLinkingInProgress)
        {
            endAction(event);
            return;
        }
    }

    private void repaintOnMouseMoved(MouseEvent event)
    {
    	//System.out.println("repaintOnMouseMoved");
        double zoom = this.editorPart.getZoomFactor();
        Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        Point2D snappedMousePoint = grid.snap(mousePoint);
        if (!snappedMousePoint.equals(lastMousePoint)) {
            this.editorPart.getSwingComponent().invalidate();
            this.editorPart.getSwingComponent().repaint();
        }
        this.lastMousePoint = snappedMousePoint;
    }

    private boolean isConditionOK(MouseEvent event)
    {
    	//System.out.println("isConditionOK");
        if (event.getClickCount() > 1)
        {
            return false;
        }
        if (event.getButton() != MouseEvent.BUTTON1)
        {
            return false;
        }
        if (GraphTool.SELECTION_TOOL.equals(this.graphToolsBar.getSelectedTool()))
        {
            return false;
        }
        GraphTool selectedTool = this.selectionHandler.getSelectedTool();
        if (!IEdge.class.isInstance(selectedTool.getNodeOrEdge()))
        {
            return false;
        }
        return true;
    }

    private boolean isRecognizedAsTransitionAction()
    {
    	//System.out.println("isRecognizedAsTransitionAction");
        if (this.newEdge == null)
        {
            return false;
        }
        if (!this.newEdge.isTransitionPointsSupported())
        {
            return false;
        }
        INode aNode = graph.findNode(this.lastMousePoint);
        if (aNode == null)
        {
            return true;
        }
        return false;
    }

    private void startAction(MouseEvent event)
    {
    	//System.out.println("startAction");
        double zoom = editorPart.getZoomFactor();
        final Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        INode targetNode = graph.findNode(mousePoint);
        this.isLinkingInProgress = (targetNode != null);
        this.firstMousePoint = grid.snap(mousePoint);       
        this.lastMousePoint = grid.snap(mousePoint);
        GraphTool selectedTool = this.selectionHandler.getSelectedTool();
        IEdge prototype = (IEdge) selectedTool.getNodeOrEdge();
        this.newEdge = (IEdge) prototype.clone();
        this.newEdge.setId(new Id());
        
        /*if(isConstrainted(this.firstMousePoint,this.newEdge))
        {
        	JOptionPane.showConfirmDialog(null, "You need to change the constraint first.","Warning", JOptionPane.ERROR_MESSAGE);
            cancel();
            return;
        } */  
    }

    private void transitionAction(MouseEvent event)
    {
    	//System.out.println("transitionAction");
        this.transitionPoints.add(this.lastMousePoint);
    }

    private void endAction(MouseEvent event)
    {
    	//System.out.println("endAction");
        boolean added = addEdgeAtPoints(this.newEdge, firstMousePoint, lastMousePoint);
        
        if (added)
        {
            this.selectionHandler.setSelectedElement(this.newEdge);
        }
        this.isLinkingInProgress = false;
        this.isLinkBySeparatedClicks = false;
        this.transitionPoints.clear();
        this.newEdge = null;
        
    }

    private void cancel()
    {
    	//System.out.println("cancel");
        this.isLinkingInProgress = false;
        this.isLinkBySeparatedClicks = false;
        this.transitionPoints.clear();
        this.newEdge = null;
    }

    /**
     * @param startNode
     * @param endNode
     * @param newEdge
     */
    public boolean applyBidirectionalConstraint(INode startNode, INode endNode,IEdge newEdge){
    	boolean constraintApplied = false;
    	if (newEdge instanceof AbstractEdge) {
            AbstractEdge abstractEdge = (AbstractEdge) newEdge;
            if (abstractEdge.getType().equals("Aggregation") || abstractEdge.getType().equals("Composition")) {
            	//if(startNode == endNode) && startNode.getGraph().getAllEdges().size() >= 1)
                if (startNode.getGraph() != null && startNode.getGraph().getAllEdges() != null && startNode.getGraph().getAllEdges().size() >= 1) {
                    for (IEdge edg : startNode.getGraph().getAllEdges()) {
                        if (abstractEdge.getType().equals("Aggregation") || abstractEdge.getType().equals("Composition"))
                            if (startNode.getId().equals(edg.getEndNode().getId()) &&
                                    endNode.getId().equals(edg.getStartNode().getId())) {
								if (startNode.getId().equals(endNode.getId())) {
									JOptionPane.showMessageDialog(null," Recursive relationship is not allowed for aggregation and composition ", "",JOptionPane.ERROR_MESSAGE);
								}
                            	else
                            	{
                            		JOptionPane.showMessageDialog(null, " Bidirectional relationship is not allowed for aggregation and composition ", "", JOptionPane.ERROR_MESSAGE);
                            	}
                                return constraintApplied = true;
                            }

                    }
                }
            }
        }
		return constraintApplied;
    }
    /**
     * Adds an edge at a specific location
     * 
     * @param newEdge
     * @param startPoint
     * @param endPoint
     * @return true id the edge has been added
     */
    public boolean addEdgeAtPoints(IEdge newEdge, Point2D startPoint, Point2D endPoint) {
        boolean isAdded = false;

        if (startPoint.distance(endPoint) > CONNECT_THRESHOLD) {
            this.behaviorManager.fireBeforeAddingEdgeAtPoints(newEdge, startPoint, endPoint);
            INode startNode = graph.findNode(startPoint);
            INode endNode = graph.findNode(endPoint);
            //feature2
            //if(feature1 is checked, do not call the applyBidirectional or applyRecursive constraint method)
            
            if(applyBidirectionalConstraint(startNode, endNode, newEdge))
                return isAdded;
            
            try {
                Point2D relativeStartPoint = null;
                Point2D relativeEndPoint = null;
                Point2D[] transitionPointsAsArray = this.transitionPoints.toArray(new Point2D[this.transitionPoints.size()]);
                if (startNode != null) {
                    Point2D startNodeLocationOnGraph = startNode.getLocationOnGraph();
                    double relativeStartX = startPoint.getX() - startNodeLocationOnGraph.getX();
                    double relativeStartY = startPoint.getY() - startNodeLocationOnGraph.getY();
                    relativeStartPoint = new Point2D.Double(relativeStartX, relativeStartY);
                }
                if (endNode != null) {
                    Point2D endNodeLocationOnGraph = endNode.getLocationOnGraph();
                    double relativeEndX = endPoint.getX() - endNodeLocationOnGraph.getX();
                    double relativeEndY = endPoint.getY() - endNodeLocationOnGraph.getY();
                    relativeEndPoint = new Point2D.Double(relativeEndX, relativeEndY);
                }
                if (graph.connect(newEdge, startNode, relativeStartPoint, endNode, relativeEndPoint, transitionPointsAsArray)) {
                    newEdge.incrementRevision();
                    isAdded = true;
                }
            } finally {
                this.behaviorManager.fireAfterAddingEdgeAtPoints(newEdge, startPoint, endPoint);
            }
        }
        return isAdded;
    }

    @Override
    public void onPaint(Graphics2D g2)
    {
    	
        if (!isLinkingInProgress)
        {
        	//System.out.println("is Not LinkingInProgress");
            return;
        }
        //System.out.print("onPaint");
        Color oldColor = g2.getColor();
        g2.setColor(PURPLE);
        GeneralPath path = new GeneralPath();
        path.moveTo(this.firstMousePoint.getX(), this.firstMousePoint.getY());
        for (Point2D aTransitionPoint : this.transitionPoints)
        {
            path.lineTo(aTransitionPoint.getX(), aTransitionPoint.getY());
        }
        path.lineTo(this.lastMousePoint.getX(), this.lastMousePoint.getY());
        g2.draw(path);
        g2.setColor(oldColor);
    }
    
	@Override
	public void onMouseToolClicked(GraphTool selectedTool)
	{
		//System.out.println("onMouseToolClicked");
		Object obj = selectedTool.getNodeOrEdge();
		if (obj instanceof IEdge)
		{
			//System.out.println("addEdgeBehavior");
			this.dragging = true;
		}
	}

	@Override
	public void onMouseToolDragged(MouseEvent event)
	{
		//System.out.println("onMouseToolDragged");
		if (this.dragging) 
		{
			this.dialogFactory.showWarningDialog(noDragMessage);
			this.dragging = false;
		}
	}

	@Override
	public void onMouseToolReleased(MouseEvent event)
	{
		//System.out.println("onMouseToolReleased");
		this.dragging = false;
	}
	
	

    private static final Color PURPLE = new Color(0.7f, 0.4f, 0.7f);

    private static final int CONNECT_THRESHOLD = 8;

    private Point2D firstMousePoint = null;

    private Point2D lastMousePoint = null;
    
    private IEditorPart editorPart;

    private IGraph graph;
    
    private IGridSticker grid;

    private IEditorPartSelectionHandler selectionHandler;

    private IEditorPartBehaviorManager behaviorManager;

    private IGraphToolsBar graphToolsBar;

    private boolean isLinkingInProgress = false;

    private boolean isLinkBySeparatedClicks = false;

    private List<Point2D> transitionPoints = new ArrayList<Point2D>();

    private IEdge newEdge = null;
    
    private boolean dragging;
    
    @InjectedBean
    private DialogFactory dialogFactory;
    
    @ResourceBundleBean(key = "addedge.properties.no_drag_message")
    private String noDragMessage;

    private IWorkspace workspace;
}
