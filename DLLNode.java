/**
 * Node class for DoubleLinkedList class 
 * @author Robbie Gill
 * 
 *
 * @param <T> generic type of elements stored in a node
 */
public class DLLNode<T>
{	
	private DLLNode<T> prev;		// Reference to the previous node.
	private DLLNode<T> next;		// reference to next node
	private T element;			// reference to object stored in node 
	
	/**
	 * Constructor - with given element 
	 * @param element - object of type T
	 */
	public DLLNode(T element)
	{
		this.setElement(element);
		this.setNext(null);
		this.setPrevious(null);
	}

	/**
	 * Returns reference to next Node
	 * @return - reference to DLLNode<T> object 
	 */
	public DLLNode<T> getNext()
	{
		return this.next;
	}
	
	/**
	 * Returns reference to previous Node
	 * @return - reference  to DLLNode<T> object 
	 */
	public DLLNode<T> getPrevious()
	{
		return this.prev;
	}

	/**
	 * Assign reference to next Node 
	 * @param next - reference to Node<T> object 
	 */
	public void setNext(DLLNode<T> next)
	{
		this.next = next;
	}
	
	
	/**
	 * Assign reference to previous Node 
	 * @param next - reference to Node<T> object 
	 */
	public void setPrevious(DLLNode<T> previous)
	{
		this.prev = previous;
	}

	/**
	 * Returns reference to node stored in node 
	 * @return - reference to object of type T 
	 */
	public T getElement()
	{
		return this.element;
	}

	/**
	 * Sets reference to element stored at node
	 * @param element - reference to object of type T
	 */
	public void setElement(T element)
	{
		this.element = element;
	}
	
	
}
