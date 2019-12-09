import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * @author Robbie Gill CS-221 section 002
 * 
 * IUDoubleLinkedList is a double linked list which allows a user to use
 * all methods in the indexUnsortedList interface.
 *
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {

	private DLLNode<T> head;// represents the first node in the list
	private DLLNode<T> tail;// represents the last node in the list
	// *Note if the list is of size 1 the head and tail will both point to the same
	// node.
	private int size;// counts the number of Nodes in the list
	private int modCount; // keeps track of the number of modifications that have been made to the list

	/**
	 * Default constructor, empty list Instantiates the head and tail to null, and
	 * the size and modCount to 0, because the list is empty and we have not made
	 * any changes to the list.
	 */
	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	/*
	 * Adds The specified Element to the front of the list.
	 * 
	 * @param element - the element the user is adding to the front of the list.
	 */
	@Override
	public void addToFront(T element) {
		DLLNode<T> newNode = new DLLNode<T>(element);

		// empty list case
		if (isEmpty() == true) {
			head = tail = newNode;

		} else {

			// general case
			newNode.setNext(head); // connect the n
			head.setPrevious(newNode);
			head = newNode;

		}
		size++;
		modCount++;

	}

	/**
	 * Adds the specified element to the rear of the list
	 * 
	 * @param element
	 *            - the element being added to the rear of the list.
	 */
	@Override
	public void addToRear(T element) {
		add(element);
	}

	/**
	 * adds the element to the rear of the list
	 * 
	 * @param element- element being added to the rear of the list.
	 */
	@Override
	public void add(T element) {
		DLLNode<T> newNode = new DLLNode<T>(element);
		// emptyList case
		if (isEmpty() == true) {
			head = tail = newNode;
		}
		// adds newNode to the end of the list then updates the tail
		else {
			tail.setNext(newNode); // doesn't work if empty
			newNode.setPrevious(tail);
			tail = newNode;
		}
		size++;
		modCount++;

	}

	/*
	 * adds the specified element after the target element in the list.
	 * 
	 * @param T element - new Element to insert into the list
	 * 
	 * @param T target - indicates which target element you wish to add the element
	 * after
	 */
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

	/*
	 * Appends the specified element to the end of this list.
	 * 
	 * @param index - the position in the list you wish to add the element
	 * 
	 * @param element - the element you wish to add to a node at that position
	 */
	@Override
	public void add(int index, T element) {
		// case 1: index out of bounds
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}

		DLLNode<T> newNode = new DLLNode<T>(element);
		DLLNode<T> oneBefore = head; // saves the reference for the previous node
		DLLNode<T> oneAfter = null; // saves reference of one node after where we want to add

		// case when adding to the front of the list
		if (index == 0) {
			addToFront(element);
		}
		// case when adding to back of the list
		else if (index == size) {
			addToRear(element);
		}

		// case when the tail needs to be updated
		else if (index == size - 1) {

			oneBefore = tail.getPrevious();

			oneBefore.setNext(newNode); // connects newNode to node before it
			newNode.setPrevious(oneBefore); // connects node before back to newNode
			newNode.setNext(tail); // connects newNode to node after it.
			tail.setPrevious(newNode); // connects node after (which is tail) back to newNode
		}

		// general case
		else {

			for (int i = 0; i < index - 1; i++) {
				oneBefore = oneBefore.getNext();
			}

			oneAfter = oneBefore.getNext();
			oneBefore.setNext(newNode); // connects newNode to node before it
			newNode.setPrevious(oneBefore); // connects node before back to newNode
			newNode.setNext(oneAfter); // connects newNode to node after it
			oneAfter.setPrevious(newNode); // connects node after back to newNode
			size++;
			modCount++;
		}
		}
	

	/*
	 * removes the first Node from the list and returns the element it contains
	 * 
	 */
	@Override
	public T removeFirst() {
		// case when the list is empty
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		DLLNode<T> after = head.getNext();// second node in the list
		T firstElement = head.getElement();

		// case when there is one node in the list
		if (size == 1) {
			head = tail = null;

			// case general
		} else {

			head.setNext(null);
			after.setPrevious(null);
			head = after;
		}
		size--;
		return firstElement;
	}

	/*
	 * removes the last node from the list and returns the element it contains
	 * 
	 */
	@Override
	public T removeLast() {
		// case 1: empty case
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		// case 2: when size = 1
		else if (size == 1) {
			return removeFirst(); // uses same logic for the same case in removeFirst()
		}

		DLLNode<T> secondLast = tail.getPrevious(); // stores reference to previous node so the list doesn't break
		T element = tail.getElement();

		// case 3: when size (node size) = 2
		if (size == 2) {
			head.setNext(null);
			tail = head;

		}

		// case 4: general case (if size > 2)
		else {
			tail.setPrevious(null);
			secondLast.setNext(null);
			tail = secondLast;
		}
		size--;
		modCount++;
		return element;
	}

	/*
	 * removes a node based on the element passed into the method from the list and returns the nodes element.
	 * returns - its element to the user.
	 * 
	 */
	@Override
	public T remove(T element) {
		int targetIndex = indexOf(element);

		if (targetIndex == -1) {
			throw new NoSuchElementException();
		}

		return remove(targetIndex);
	}

	/*
	 * 
	 * removes a node at the specified index and returns the nodes element to the
	 * user/
	 */
	@Override
	public T remove(int index) {
		DLLNode<T> before = head;
		DLLNode<T> toRemove = null;
		DLLNode<T> after = toRemove;
		T element = null;

		// case 1: list is empty so nothing can be removed this will
		// throw an IndexOutOfBoundsException
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		// case 2: if the index is at the beginning of the list.
		else if (index == 0) {
			return removeFirst();
		}

		// case 3: if index is at tail
		else if (index == size - 1) {
			return removeLast();
		}

		// general case
		else {

			for (int i = 0; i < index - 1; i++) {
				before = before.getNext();
			}

			toRemove = before.getNext();
			element = toRemove.getElement();
			after = before.getNext().getNext();
			before.setNext(after);
			after.setPrevious(before);
			toRemove.setNext(null);
			toRemove.setPrevious(null);

			size--;
			return element;

		}

	}

	/*
	 * 
	 * sets the element of a node in the list based on the index and element passed
	 * into the method.
	 */
	@Override
	public void set(int index, T element) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		DLLNode<T> lastReturnedNode = head;

		for (int i = 0; i < index; i++) {
			lastReturnedNode = lastReturnedNode.getNext();
		}

		lastReturnedNode.setElement(element);
		modCount++;
	}

	/*
	 * gets a node in the list at a given index.
	 * 
	 */
	@Override
	public T get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		DLLNode<T> lastReturnedNode = head;

		for (int i = 0; i < index; i++) {
			lastReturnedNode = lastReturnedNode.getNext();
		}

		return lastReturnedNode.getElement();
	}

	/*
	 * 
	 * finds the index of a certain element in the list
	 */
	@Override
	public int indexOf(T element) {
		DLLNode<T> lastReturnedNode = head;
		int index = 0;

		for (int i = 0; i < size; i++) {
			if (element.equals(lastReturnedNode.getElement())) {
				index = i;
				return index;
			}

			lastReturnedNode = lastReturnedNode.getNext();
		}

		return -1;
	}

	/*
	 * 
	 * returns the element of the first node in the list.
	 * 
	 * @Throws NoSuchElementException() - when the list is empty.
	 * 
	 * @return the element in the first Node in the list.
	 */
	@Override
	public T first() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		return head.getElement();
	}

	/*
	 * returns the element of the last node in the list.
	 * 
	 * @return - the last Element in the list.
	 * 
	 * @Throws NoSuchElementException() - when the list is empty.
	 */
	@Override
	public T last() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		return tail.getElement();
	}

	/*
	 * checks if the list contains a certain element.
	 * 
	 * @return true - the list contains the target element.
	 * 
	 * @return false - the list does not contain the target element.
	 */
	@Override
	public boolean contains(T target) {
		int exists = indexOf(target);

		if (exists == -1) {
			return false;
		}

		return true;
	}

	/*
	 * checks if the list contains any elements
	 * 
	 * @return true - the list is empty.
	 * 
	 * @return false - the list contains elements.
	 */
	@Override
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		}

		return false;
	}

	/*
	 * returns the number of nodes in the list
	 * 
	 * @return size - the number of nodes in the list.
	 */
	@Override
	public int size() {
		return size;
	}

	/*
	 * default iterator() for the list
	 * 
	 */
	@Override
	public Iterator<T> iterator() {
		return new DLLIterator();
	}

	/*
	 * @return DLLIterator - default iterator for LinkedList
	 * 
	 */
	@Override
	public ListIterator<T> listIterator() {
		return new DLLIterator();
	}

	/*
	 * returns a DLLIterator whos index differs from zero this occurs when a list
	 * has already been iterated on.
	 * 
	 * @param startingIndex - represents the index in which the listIterator should
	 * begin
	 * 
	 * @return DLLIterator - ListIterator at current position of the cursor.
	 */
	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLLIterator(startingIndex);
	}

	/**
	 * Iterator for the IUDoubleLinkedList class, implements optional remove method
	 *
	 * @author Robbie Gill
	 *
	 */

	private class DLLIterator implements ListIterator<T> {
		public DLLNode<T> nextNode = head;// first node that the cursor will move over in the default case.
		public DLLNode<T> lastReturnedNode = null;// A reference to the last Node that the cursor has moved over in a
													// call to next() or previous()
		public int nextIndex;// The index that will be moved to in a call to next()
		public int itrModCount;// number of modifications the Iterator has made to the list.
	

		/**
		 * Default constructor, iterator a beginning of list.
		 */
		public DLLIterator() {
			this(0);

		}

		public DLLIterator(int nextIndex) {
			nextValidIndex(nextIndex);

			this.nextIndex = nextIndex;
			itrModCount = modCount;

			for (int i = 0; i < nextIndex; i++) {
				nextNode = nextNode.getNext();
			}
			

		}

		/**
		 * checks whether the passed index is valid
		 * 
		 * @param nextIndex-The
		 *            position the cursor would move to in a call to next()
		 * @throws IndexOutOfBoundsException
		 *             - when the nextIndex parameter is invalid for the list.
		 */
		public void nextValidIndex(int nextIndex) {
			if (nextIndex < 0 || nextIndex > size) {
				throw new IndexOutOfBoundsException();
			}
		}

		/**
		 * checks if an illegal modification has been made to the list.
		 * 
		 * @throws ConcurrentModificationException()
		 *             - when an invalid modification has been made to the list
		 */
		public void modCheck() {
			if (itrModCount != modCount) {
				throw new ConcurrentModificationException();
			}

		}

		/*
		 * inserts the specified element into the list (optional operation). The element
		 * is inserted immediately before the element that would be returned by next(),
		 * if any, and after the element that would be returned by previous(), if any.
		 * (If the list contains no elements, the new element becomes the sole element
		 * on the list.) The new element is inserted before the implicit cursor: a
		 * subsequent call to next would be unaffected, and a subsequent call to
		 * previous would return the new element. (This call increases by one the value
		 * that would be returned by a call to nextIndex or previousIndex.)
		 * 
		 * @param e - The new Element to be added to the list
		 */
		@Override
		public void add(T e) {
			modCheck();

			lastReturnedNode = null;
			DLLNode<T> newNode = new DLLNode<T>(e);
			// after tail
			if (nextNode == null) {
				newNode.setPrevious(tail);
				tail = newNode;
			} else {
				newNode.setNext(nextNode);
				newNode.setPrevious(nextNode.getPrevious());
				nextNode.setPrevious(newNode);
			}

			if (nextNode == head) {
				head = newNode;
			} else {

				newNode.getPrevious().setNext(newNode);
			}
			size++;
			modCount++;
			itrModCount++;
			nextIndex++;

		}

		/*
		 * Returns true if this list iterator has more elements when traversing the list
		 * in the forward direction. (In other words, returns true if next() would
		 * return an element rather than throwing an exception.)
		 * 
		 * @return true - if the list has a nextNode from the current cursor position.
		 * 
		 * @ return false -if the list doesn't have a nextNode from the current cursor
		 * position.
		 */
		@Override
		public boolean hasNext() {

			if (nextNode != null) {
				return true;
			} else
				return false;
		}

		/*
		 * Returns true if this list iterator has more elements when traversing the list
		 * in the reverse direction. (In other words, returns true if previous() would
		 * return an element rather than throwing an exception.)
		 * 
		 * @return true - when the list has a previous node from the current cursor
		 * position
		 * 
		 * @return false - when the list does not have a previous node from the current
		 * cursor position
		 */
		@Override
		public boolean hasPrevious() {

			return ((nextNode != head));
		}

		/*
		 * Returns the next element in the list and advances the cursor position. This
		 * method may be called repeatedly to iterate through the list, or intermixed
		 * with calls to previous() to go back and forth. (Note that alternating calls
		 * to next and previous will return the same element repeatedly.)
		 * 
		 * @return nextElement - the element of the node you move over with a call to
		 * next()
		 * 
		 * @Throws NoSuchElementException(); when there is no next element or the list
		 * is empty.
		 */
		@Override
		public T next() {
			if (hasNext() == false) {
				throw new NoSuchElementException();
			}

			T nextElement = nextNode.getElement();

			lastReturnedNode = nextNode;
			nextNode = nextNode.getNext();

			nextIndex++;
			

			return nextElement;

		}

		/*
		 * 
		 * Returns the index of the element that would be returned by a subsequent call
		 * to next(). (Returns list size if the list iterator is at the end of the
		 * list.)
		 * 
		 * @return nextIndex - the next index from the current cursor position.
		 */
		@Override
		public int nextIndex() {

			return nextIndex;
		}

		/*
		 * Returns the previous element in the list and moves the cursor position
		 * backwards. This method may be called repeatedly to iterate through the list
		 * backwards, or intermixed with calls to next() to go back and forth. (Note
		 * that alternating calls to next and previous will return the same element
		 * repeatedly.)
		 * 
		 * @return the last Node that the cursor moved over.
		 * 
		 * @Throws NoSuchElementException()-when the list does not contain a previous
		 * element
		 */
		@Override
		public T previous() {
			// case where there is no previous element
			if (hasPrevious() == false) {
				throw new NoSuchElementException();
			}

			if (nextNode == null) {
				lastReturnedNode = tail;
				nextNode = lastReturnedNode;
			} else {
				nextNode = nextNode.getPrevious();
				lastReturnedNode = nextNode;
			}
			nextIndex--;
			return lastReturnedNode.getElement();

		}

		/*
		 * Returns the index of the element that would be returned by a subsequent call
		 * to previous(). (Returns -1 if the list iterator is at the beginning of the
		 * list.)
		 * 
		 * @return- The previous index from the current cursor position.
		 */
		@Override
		public int previousIndex() {

			return (nextIndex - 1);
		}

		/*
		 * Removes from the list the last element that was returned by next() or
		 * previous() (optional operation). This call can only be made once per call to
		 * next or previous. It can be made only if add(E) has not been called after the
		 * last call to next or previous.
		 * 
		 * @Throws IllegalStateExeception() - Occurs when the last Node the cursor moved
		 * over is null
		 */
		@Override
		public void remove() {
			modCheck();

			if (lastReturnedNode == null) {
				throw new IllegalStateException();
			}
			// case where we are removing the first Node in the list
			if (lastReturnedNode == head) {
				head = head.getNext();
			} else {
				lastReturnedNode.getPrevious().setNext(lastReturnedNode.getNext());
			}
			// case where we are removing the last Node in the list
			if (lastReturnedNode == tail) {
				tail = tail.getPrevious();
			} else {
				lastReturnedNode.getNext().setPrevious(lastReturnedNode.getPrevious());
			}

			if (nextNode == lastReturnedNode) {
				nextNode = nextNode.getNext();
			} else {
				nextIndex--;
			}
			lastReturnedNode = null;
			size--;
			modCount++;
			itrModCount++;

		}

		/*
		 * Replaces the last element returned by next() or previous() with the specified
		 * element (optional operation). This call can be made only if neither remove()
		 * nor add(E) have been called after the last call to next or previous.
		 * 
		 * @param e - the T element that will be placed in the last node modified by a
		 * call to next() or previous()
		 * 
		 * @Throws IllegalStateException()- when the node being set is invalid
		 */
		@Override
		public void set(T e) {
			modCheck();// check that no illegal modifications have been made
			if (lastReturnedNode == null) {
				throw new IllegalStateException();
			}
			lastReturnedNode.setElement(e);
			modCount++;
			itrModCount = modCount;

		}

	}

}
