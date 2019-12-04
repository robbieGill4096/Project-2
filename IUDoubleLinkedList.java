import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * @author Robbie Gill
 *
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {

	private DLLNode<T> head;
	private DLLNode<T> tail;
	private DLLNode<T> end;
	private DLLNode<T> returnedNode = head; // used for DLL iterator
	private int count;
	private int modCount; // keeps track of the number of modifications

	/**
	 * Default constructor, empty list
	 */
	public IUDoubleLinkedList() {
		head = null;
		tail = null;
		end = new DLLNode<T>(null);
		count = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		DLLNode<T> newNode = new DLLNode<T>(element);

		// case 1: empty case
		if (isEmpty()) {
			head = newNode;
			tail = newNode;
		}

		// case 2: general case
		newNode.setNext(head); // right link to head
		head.setprev(newNode); // left link back to newNode
		head = newNode;

		count++;

	}

	@Override
	public void addToRear(T element) {
		add(element);
	}

	@Override
	public void add(T element) {
		DLLNode<T> newNode = new DLLNode<T>(element);

		if (isEmpty()) {
			head = newNode;
			tail = newNode;
		}

		else {
			tail.setNext(newNode); // doesn't work if empty
		}

		end.setprev(newNode);
		newNode.setNext(end);
		newNode.setprev(tail);
		tail = newNode;

		count++;
		modCount++;

	}

	@Override
	public void addAfter(T element, T target) {
		int targetIndex = indexOf(target);

		if (targetIndex == -1) {
			throw new NoSuchElementException();
		}

		else {
			add(targetIndex + 1, element);
		}

	}

	@Override
	public void add(int index, T element) {
		// case 1: index out of bounds
		if (count < 0 || index < 0 || index > count) {
			throw new IndexOutOfBoundsException();
		}

		DLLNode<T> newNode = new DLLNode<T>(element);
		DLLNode<T> oneBefore = head; // saves the reference for the previous node
		DLLNode<T> oneAfter = null; // saves reference of one node after where we want to add

		// case 2: when adding at index 0
		if (index == 0) {
			addToFront(element);
		}

		// case 3: when index == count
		else if (index == count) {
			addToRear(element);
		}

		// case 4: when index == count - 1 (index where we want to add is at tail)
		else if (index == count - 1) {

			oneBefore = tail.getPrev();

			oneBefore.setNext(newNode); // connects newNode to node before it
			newNode.setprev(oneBefore); // connects node before back to newNode
			newNode.setNext(tail); // connects newNode to node after it (which is tail)
			tail.setprev(newNode); // connects node after (which is tail) back to newNode

			count++;
		}

		// case 5: general case
		else {

			for (int i = 0; i < index - 1; i++) {
				oneBefore = oneBefore.getNext();
			}

			oneAfter = oneBefore.getNext();
			oneBefore.setNext(newNode); // connects newNode to node before it
			newNode.setprev(oneBefore); // connects node before back to newNode
			newNode.setNext(oneAfter); // connects newNode to node after it
			oneAfter.setprev(newNode); // connects node after back to newNode

			count++;

		} // end else
	}

	@Override
	public T removeFirst() {
		// case 1: empty case
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		DLLNode<T> secondNode = head.getNext(); // saves reference of node we're removing
		T temp = head.getElement();

		// case 2: when count = 1
		if (count == 1) {
			head = null;
			tail = null;
			count--;
			return temp;
		}

		// case 3: general case
		head.setNext(null);
		secondNode.setprev(null);
		head = secondNode;

		count--;
		return temp;
	}

	@Override
	public T removeLast() {
		// case 1: empty case
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		// case 2: when count = 1
		else if (count == 1) {
			return removeFirst(); // uses same logic for the same case in removeFirst()
		}

		DLLNode<T> secondLast = tail.getPrev(); // stores reference to previous node so the list doesn't break
		T temp = tail.getElement();

		// case 3: when count (node count) = 2
		if (count == 2) {
			head.setNext(null);
			tail = head;
			count--;
			return temp;
		}

		// case 4: general case (if count > 2)
		else {
			tail.setprev(null);
			secondLast.setNext(end);
			tail = secondLast;
			count--;
		}

		return temp;
	}

	@Override
	public T remove(T element) {
		int targetIndex = indexOf(element);

		if (targetIndex == -1) {
			throw new NoSuchElementException();
		}

		return remove(targetIndex);
	}

	@Override
	public T remove(int index) {
		DLLNode<T> before = head;
		DLLNode<T> toRemove = null;
		DLLNode<T> after = toRemove;
		T temp = null;

		// case 1: empty case
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException();
		}

		// case 2: if index is at 0
		else if (index == 0) {
			return removeFirst();
		}

		// case 3: if index is at tail
		else if (index == count - 1) {
			return removeLast();
		}

		// case 4: general case
		else {

			for (int i = 0; i < index - 1; i++) {
				before = before.getNext();
			}

			toRemove = before.getNext();
			temp = toRemove.getElement();
			after = before.getNext().getNext();
			before.setNext(after);
			after.setprev(before);
			toRemove.setNext(null);
			toRemove.setprev(null);

			count--;

		} // end else

		return temp;
	}

	@Override
	public void set(int index, T element) {
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException();
		}

		DLLNode<T> current = head;

		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}

		current.setElement(element);

	}

	@Override
	public T get(int index) {
		if (index < 0 || index >= count) {
			throw new IndexOutOfBoundsException();
		}

		DLLNode<T> current = head;

		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}

		return current.getElement();
	}

	@Override
	public int indexOf(T element) {
		DLLNode<T> current = head;
		int index;

		for (int i = 0; i < count; i++) {
			if (element.equals(current.getElement())) {
				index = i;
				return index;
			}

			current = current.getNext();
		}

		return -1;
	}

	@Override
	public T first() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		return head.getElement();
	}

	@Override
	public T last() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		int exists = indexOf(target);

		if (exists == -1) {
			return false;
		}

		return true;
	}

	@Override
	public boolean isEmpty() {
		if (count == 0) {
			return true;
		}

		return false;
	}

	@Override
	public int size() {
		return count;
	}

	@Override

	public Iterator<T> iterator() {

		return new DLLIterator();
		//throw new UnsupportedOperationException();

	}

	@Override

	public ListIterator<T> listIterator() {
		 //throw new UnsupportedOperationException();
		return new DLLIterator();

	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		//throw new UnsupportedOperationException();
		 return new DLLIterator(startingIndex);
	}

	/**
	 * 
	 * /**
	 * 
	 * Iterator for the IUDoubleLinkedList class, implements optional remove method
	 *
	 * 
	 * 
	 * @author Robbie Gill
	 *
	 * 
	 * 
	 */

	private class DLLIterator implements ListIterator<T> {

		private DLLNode<T> nextNode = IUDoubleLinkedList.this.head;
		private DLLNode<T> dummyNode;
		private DLLNode<T> lastReturnedNode;
		private int count = IUDoubleLinkedList.this.count;
		private int size = 0;
		private DLLNode<T> current = null;
		DLLNode<T> newNode;

		private int nextIndex;
		public int itrModCount;

		private boolean canSet = false;

		private boolean canRemove = false;

		private DLLIterator() {

			this(0);

		}

		public DLLIterator(int nextIndex) {
			dummyNode = new DLLNode<T>(null);
			// Bounds Check

			if (nextIndex < 0 || nextIndex > count) {

				throw new IndexOutOfBoundsException();

			}

			// Sync index and modCount.

			this.nextIndex = nextIndex;

			itrModCount = modCount;

		}

		@Override

		public void add(T e) {
			newNode = new DLLNode<T>(e);
			modCheck();
			if (head == null || isEmpty() == true) {
				nextNode = new DLLNode(null);
				newNode.setNext(nextNode);
				nextNode.setprev(newNode);
				head = newNode;
				modCount++;
				itrModCount++;
				itrModCount = modCount;
				count++;
				nextIndex++;
				canRemove = false;
				size++;
			}
			else if (isEmpty() == false) {
				if (nextNode.getPrev() != null) {
					newNode.setprev(nextNode.getPrev());
					nextNode.getPrev().setNext(newNode);
				}
				
				
				nextNode.setprev(newNode);
				newNode.setNext(nextNode);
			}
			
			
			count++;
			modCount++;
			itrModCount++;
			itrModCount = modCount;
			canRemove = false;
			nextIndex++;
			size++;
		}

		@Override
		public boolean hasNext() {
			try {
				if (nextNode.getElement() != null && nextNode != null && count > 0)
					return true;
			} catch (NullPointerException e) {
				return false;
			}
			return false;
		}

		@Override
		public boolean hasPrevious() {
			try {
				if (nextNode.getPrev().getElement() != null && count > 0 && nextNode.getPrev() != null)

					return true;
			} catch (NullPointerException e) {
				return false;
			}
			return false;
		}

		@Override
		public T next() {
			try {
				if (hasNext() == true) {
					this.lastReturnedNode = nextNode;
					T nextElement = nextNode.getElement();
					this.nextNode = nextNode.getNext();
					modCount++;
					itrModCount++;
					nextIndex++;
					canRemove = true;

					return nextElement;
				} else {
					throw new NoSuchElementException();
				}
			} catch (NullPointerException e) {
				throw new NoSuchElementException();
			}
			

		}

		@Override
		public int nextIndex() {
			// TODO Auto-generated method stub
			return nextIndex;
		}

		@Override
		public T previous() {
			try {
				if (hasPrevious() == true) {
					this.lastReturnedNode = nextNode.getPrev();
					T previousElement = nextNode.getPrev().getElement();
					this.nextNode = nextNode.getPrev();
					modCount++;
					itrModCount++;
					nextIndex--;
					canRemove = true;
					return previousElement;
				} else {
					throw new NoSuchElementException();
				}
			} catch (NullPointerException e) {
				throw new NoSuchElementException();
			}

		}

		@Override
		public int previousIndex() {
			// TODO Auto-generated method stub
			return nextIndex-1;
		}
		@Override
		public void remove() {
			modCheck();
			if (canRemove == false) {
			throw new IllegalStateException();
			}
			if (lastReturnedNode == null) {
				throw new IllegalStateException();
			}
			if(count == 1) {
				head = null;
				lastReturnedNode.setNext(null);
				lastReturnedNode.setprev(null);
				nextNode.setNext(null);
				nextNode.setprev(null);
				nextIndex --;
				count--;
				modCount++;
				itrModCount++;
				canRemove = false;
			}
			//removing first node in list thats not empty
			else if(lastReturnedNode == head && count >=2) {
				DLLNode<T> after = lastReturnedNode.getNext();
				lastReturnedNode.setNext(null);
				lastReturnedNode.setprev(null);
				head = after;
				nextIndex --;
				count--;
				modCount++;
				itrModCount++;
				canRemove = false;
				
			}
			//general case with 3 elements and 2 calls to next() remove after call to next();
			else if(lastReturnedNode.getNext() != null && lastReturnedNode.getPrev() != null) {
			DLLNode<T> before  = lastReturnedNode.getPrev();
			DLLNode<T> after = lastReturnedNode.getNext();
			before.setNext(after);
			after.setprev(before);
		
			lastReturnedNode.setNext(null);
			lastReturnedNode.setprev(null);
			//nextNode = nextNode.getPrev();
			nextIndex --;
			count--;
			modCount++;
			itrModCount++;
			canRemove = false;}
						
		}
		public DLLNode<T> getLastReturned(){
			return this.lastReturnedNode;
		}
		private void modCheck() {
			if(modCount != itrModCount) {
				throw new ConcurrentModificationException();
				//throw new concurrent modification exception
			}
		}

		@Override
		public void set(T e) {
			if(isEmpty() == true || canRemove == false) {
				throw new IllegalStateException();
			}
			lastReturnedNode.setElement(e);
			//System.out.println(size);
			//System.out.println(nextNode.getElement());
			//System.out.println(count);
		}

	}
}