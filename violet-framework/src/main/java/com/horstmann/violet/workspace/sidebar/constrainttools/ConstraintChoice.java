package com.horstmann.violet.workspace.sidebar.constrainttools;

import java.awt.Color;

public class ConstraintChoice
{
	private int constraintNo;
	private String constraintName;
    private boolean truthValue;

    public ConstraintChoice(int constraintNum,String constraintName,boolean truthValue)
    {
    	this.constraintNo=constraintNum;
    	this.constraintName=constraintName;
        this.truthValue=truthValue;
    }

    public boolean getTruthValue()
    {
        return this.truthValue;
    }
    
    public String getConstraintName()
    {
        return this.constraintName;
    }
    
    public int getConstraintNo()
    {
        return this.constraintNo;
    }
}
