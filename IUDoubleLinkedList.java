import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	public DLLNode<T> head;
	private DLLNode<T> tail;
	private int count;
	private int modCount; // keeps track of the number of modification
	public DLLNode<T> end;

	public IUDoubleLinkedList() {
		end = new DLLNode<T>(null);
		head = tail = null;
		
		count = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		DLLNode<T> newNode = new DLLNode<T>(element);
		// case 1: empty case
		if (isEmpty()) {
			head = tail = newNode;
			count++;
			modCount++;
			head.setprev(end);
			tail.setNext(end);
		} else {
			// case 2: general case
			newNode.setNext(head); // right link to head
			head.setprev(newNode); // left link back to newNode
			head = newNode;
			head.setprev(end);
			tail.setNext(end);
			count++;
			modCount++;
		}

	}

	@Override
	public void addToRear(T element) {
		add(element);
	}

	@Override
	public void add(T element) {
		DLLNode<T> newNode = new DLLNode<T>(element);
		if (isEmpty()) {
			head = tail = newNode;
			head.setprev(end);
			tail.setNext(end);
			count++;
			modCount++;
		} else {
			tail.setNext(newNode); // doesn't work if empty
			newNode.setprev(tail);
			tail = newNode;
			head.setprev(end);
			tail.setNext(end);
			count++;
			modCount++;
		}
	}

	@Override
	public void addAfter(T element, T target) {
		int targetIndex = indexOf(target);
		if (targetIndex == -1) {
			throw new NoSuchElementException();
		} else {
			add(targetIndex + 1, element);
		}

	}
	

	public void addBefore(T element, int index) {
		int targetIndex =index;
		if (targetIndex == -1) {
			throw new NoSuchElementException();
		} else {
			add(targetIndex, element);
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
			modCount++;
			head.setprev(end);
			tail.setNext(end);
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
			modCount++;
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
			head = tail = null;
			count--;
			head.setprev(end);
			tail.setNext(end);
			return temp;
		} else {
			// case 3: general case
			head.setNext(null);
			secondNode.setprev(null);
			head = secondNode;
			count--;
			head.setprev(end);
			tail.setNext(end);
			return temp;
		}
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
			modCount++;
			head.setprev(end);
			tail.setNext(end);
			return temp;
		}
		// case 4: general case (if count > 2)
		else {
			tail.setprev(null);
			secondLast.setNext(null);
			tail = secondLast;
			count--;
			modCount++;
			head.setprev(end);
			tail.setNext(end);
			return temp;
		}
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

			return temp;

		}

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

		modCount++;

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

	public DLLNode<T> getNode(int index) {

		if (index < 0 || index >= count) {

			throw new IndexOutOfBoundsException();

		}

		DLLNode<T> current = head;

		for (int i = 0; i < index; i++) {

			current = current.getNext();

		}

		return current;

	}

	@Override

	public Iterator<T> iterator() {

		return new DLLIterator();

	}

	@Override

	public ListIterator<T> listIterator() {

		return new DLLIterator();

	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLLIterator(startingIndex);
	}
	
	/**
	 * 
	 /**

	 * Iterator for the IUDoubleLinkedList class, implements optional remove method

	 *

	 * @author Robbie Gill

	 *

	 */



	private class DLLIterator implements ListIterator<T> {

		public DLLNode<T> nextNode = IUDoubleLinkedList.this.head;
		public DLLNode<T> dummyNode;

		public DLLNode<T> current = null;
		DLLNode<T> newNode;

		public int nextIndex;

		public int itrModCount;

		public boolean canSet;

		public boolean canRemove;



		public DLLIterator() {

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

			// Assignment or jump to that index.

			for (int i = 0; i < nextIndex; i++) {

				nextNode = nextNode.getNext();

			}

			canSet = false;

			

			

		}
		public DLLNode<T> previousNode() {
			if(hasPrevious() == true) {
			T element = nextNode.getPrev().getElement();
			nextNode = nextNode.getPrev();
			return nextNode = nextNode.getPrev();}
			else
				throw new NoSuchElementException();



		}
		public <T> void setPrev(DLLNode<T> Nextnode,DLLNode<T> newNode) {
			// B<->A
			// B<->Z<->A
			if(Nextnode.getPrev() != null) {
			newNode.setprev(Nextnode.getPrev());
			Nextnode.getPrev().setNext(newNode);}
			Nextnode.setprev(newNode);
			newNode.setNext(Nextnode);
			
			//System.out.println("dd");
			
		}

		/**

		 * A simple method to check ConcurrentModificationException which can be used is

		 * other list iterator methods by just calling this method

		 */

		public DLLNode<T> getNode(int index) {

			if (index < 0 || index >= count) {

				throw new IndexOutOfBoundsException();

			}



			DLLNode<T> current = head;



			for (int i = 0; i < index; i++) {

				current = current.getNext();

			}



			return current;

		}
		
		public void modCheck() {

			if (itrModCount != modCount) {

				throw new ConcurrentModificationException();

			}



		}
		

		@Override

		public void add(T e) {
			final Character M = new Character('|');
			DLLNode<T> dummy = new DLLNode<T>((T)M);
			newNode = new DLLNode<T>(e);
			boolean trigger = false;
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
			  }
//			if(isEmpty()) {
//				nextNode = newNode;
//				modCount++;
//
//				itrModCount++;
//
//				itrModCount = modCount;
//
//				nextIndex++;
//				trigger = true;
//				
//			}
			if(isEmpty() == false ) {
				if(nextNode.getPrev() != null) {
					newNode.setprev(nextNode.getPrev());
					nextNode.getPrev().setNext(newNode);}
				
					nextNode.setprev(newNode);
					newNode.setNext(nextNode);}
		
				//setPrev(nextNode,newNode);
				modCount++;

				itrModCount++;

				itrModCount = modCount;

				nextIndex++;
			}
		
		



		

		

		@Override

		public boolean hasNext() {

			if (nextNode != null) {
				return true;
			}
				return false;
		}



		@Override

		public boolean hasPrevious() {



			return ((nextNode != null));

		}



		@Override

		public T next() {



			if (!hasNext()) {

				throw new NoSuchElementException();

			}
			T Element =  nextNode.getElement();
			nextNode = nextNode.getNext();
			return Element;



		}



		@Override

		public int nextIndex() {



			return nextIndex;

		}



		@Override
		public T previous() {
			if(hasPrevious() == true) {
				System.out.println(nextNode);
			T element = nextNode.getPrev().getElement();
			nextNode = nextNode.getPrev();
			return element;}
			else
				throw new NoSuchElementException();

		}
		


		@Override

		public int previousIndex() {



			return (nextIndex - 1);

		}



		@Override

		public void remove() {
			if (!canRemove) {

				throw new IllegalStateException();

			}
			// TODO
			if (itrModCount != modCount)
				throw new ConcurrentModificationException();

			if (!canRemove) {
				throw new IllegalStateException();
			}

			DLLNode<T> previous = null;
			DLLNode<T> current = head;

			for (int i = 0; i < nextIndex - 1; i++) {
				previous = current;
				current = current.getNext();
			}

			if (current == head) {
				head = nextNode;
			} else if (current == tail) {
				tail = previous;
				previous.setNext(null);
			} else if (current == head && current == tail) {
				head = tail = null;
			} else {
				previous.setNext(current.getNext());
			}

			


			

			

			

			

			

			

			

//			DLLNode <T> oneBefore = current.getPrev();

//			current.setNext(null);

//			current.setPrev(null);

//			current = oneBefore;

//			current.setNext(nextNode);

//			nextNode.setPrev(current);





			count--;

			canRemove = false;

			modCount++;

			itrModCount = modCount;

	

		}



		@Override

		public void set(T e) {

			modCheck();

			if(!canSet) {

				throw new IllegalStateException();

			}

			

			current.setElement(e);

			

			canSet = false;

			modCount++;

			itrModCount = modCount;

			

			



		}



	}
}