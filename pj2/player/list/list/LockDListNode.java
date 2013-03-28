
public class LockDListNode extends DListNode {
	
	protected bool locked=false;
	
	LockDListNode(Object i, DListNode p, DListNode n)
	{
		super(i,p,n);
	}

}
