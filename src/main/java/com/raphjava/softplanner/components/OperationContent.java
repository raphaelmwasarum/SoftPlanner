package com.raphjava.softplanner.components;

public class OperationContent<Content1, Content2>
{
    private int operationID;

    public int getOperationID()
    {
        return operationID;
    }

    public Content1 getContent1()
    {
        return content1;
    }

    public Content2 getContent2()
    {
        return content2;
    }

    private Content1 content1;

    private Content2 content2;

    public OperationContent(int operationID, Content1 content1, Content2 content2)
    {
        this.operationID = operationID;
        this.content1 = content1;
        this.content2 = content2;
    }

    public OperationContent(int operationID, Content1 content1)
    {
        this.operationID = operationID;
        this.content1 = content1;
    }

    public OperationContent(int operationID)
    {
        this.operationID = operationID;
    }
}
