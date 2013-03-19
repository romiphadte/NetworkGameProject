public class LockDList extends DList {
	
	public void lockNode(DListNode node){
		locknode=(LockDListNode) node;
		locknode.locked=true;
	}
	
	public void remove(DListNode node) {
		if (node!=null && ((LockDListNode) node).locked==false)
		{
		node.prev.next=node.next;
		node.next.prev=node.prev;
		size--;
		}
	}


}
