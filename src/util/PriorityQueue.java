package util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A priority queue class implemented using a min heap. Priorities cannot be
 * negative.
 * 
 * @author Madison Sanchez-Forman
 * @version October 23, 2022
 *
 */
public class PriorityQueue {

	protected Map<Integer, Integer> location;
	protected List<Pair<Integer, Integer>> heap;

	/**
	 * Constructs an empty priority queue
	 */
	public PriorityQueue() {
		this.heap = new ArrayList<Pair<Integer, Integer>>();
		this.location = new HashMap<Integer, Integer>();

	}

	/**
	 * Insert a new element into the queue with the given priority.
	 *
	 * @param priority priority of element to be inserted
	 * @param element  element to be inserted <br>
	 */
	public void push(int priority, int element) {
		if (priority < 0 || isPresent(element)) {
			throw new AssertionError();
		}
		Pair<Integer, Integer> temp = new Pair<Integer, Integer>(priority, element);
		heap.add(temp);
		percolateUp(size() - 1);
		location.put(element, heap.indexOf(temp));
	}

	/**
	 * Remove the highest priority element
	 * 
	 * @return the element with the highest priority
	 */
	public Integer pop() {
		if (!isEmpty()) {
			Pair<Integer, Integer> root = heap.get(0);
			if(heap.size() == 1) {
				location.clear();
				heap.clear();
				return root.element;
			}
			swap(heap.size() - 1, 0);
			heap.remove(root);
			location.remove(root.element);
			pushDown(0);
			return root.element;
		}
		throw new AssertionError("Cannot pop() on empty queue");
	}

	/**
	 * Find element with highest priority
	 * 
	 * @return element with highest priority
	 */
	public int topPriority() {
		if (!isEmpty()) {
			return heap.get(0).priority;
		} else {
			throw new AssertionError("queue is empty, no node with topPriority()");
		}
	}

	/**
	 * Find element on top of queue
	 * 
	 * @return element on top of queue
	 */
	public int topElement() {
		if (!isEmpty()) {
			return heap.get(0).element;
		} else {
			throw new AssertionError("queue is empty, no node with topPriority()");
		}
	}

	/**
	 * Change priority of element on top of queue
	 * 
	 * @param newpriority n
	 * @param element
	 */
	public void changePriority(int newpriority, int element) {
		if (isPresent(element) && newpriority > 0) {
			int indx = location.get(element);
			Pair<Integer, Integer> temp = heap.get(indx);
			int temp_p = temp.priority;
			temp.priority = newpriority;
			if (newpriority > temp_p) {
				pushDown(indx);
			} else {
				percolateUp(indx);
			}
		} else {
			if(!isPresent(element)) {
				throw new AssertionError(element + " is not present, cannot changePriority()");
			} else if (newpriority > 0) {
				throw new AssertionError("new priority is less than 0, cannot changePrioriy()");
			}
		}
	}

	/**
	 * get priority of element
	 * 
	 * @param element
	 * @return element priority
	 */
	public int getPriority(int element) {
		if (isPresent(element)) {
			int indx = location.get(element);
			return heap.get(indx).priority;
		} else {
			throw new AssertionError("element does not exist, cannot changePriority()");
		}
	}

	/**
	 * asks if queue is empty
	 * 
	 * @return true if so false if not
	 */
	public boolean isEmpty() {
		if (heap.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Searches for element in queue
	 * 
	 * @param element
	 * @return true if element exists false otherwise
	 */
	public boolean isPresent(int element) {
		Integer indx = location.get(element);
		if (indx == null) {
			return false;
		}
		return true;
	}

	/**
	 * clears all elements from queue
	 */
	public void clear() {
		heap.clear();
		location.clear();
	}

	/**
	 * Returns number of elements in queue
	 * 
	 * @return size of queue
	 */
	public int size() {
		return heap.size();
	}

	/**
	 * Push down element at given index
	 * 
	 * @param start_index
	 * @return index of where element is finally stored
	 */
	private int pushDown(int start_index) {
		int left = left(start_index);
		int right = right(start_index);

		int smallest = start_index;

		if (left < size() && heap.get(left).priority < heap.get(start_index).priority) {
			smallest = left;
		} else if (right < size() && heap.get(right).priority < heap.get(start_index).priority) {
			smallest = right;
		}
		if (smallest != start_index) {
			swap(start_index, smallest);
			pushDown(smallest);
		}
		return smallest;
	}

	/**
	 * percolate up element at given index
	 * 
	 * @param start_index
	 * @return final index of element
	 */
	private int percolateUp(int start_index) {
		while (heap.get(parent(start_index)).priority > heap.get(start_index).priority) {
			swap(parent(start_index), start_index);
			start_index = parent(start_index);
		}
		return start_index;
	}

	/**
	 * swap two elments in the queue and heap
	 * 
	 * @param i index of element one
	 * @param j index of elment two
	 */
	private void swap(int i, int j) {
		Pair<Integer, Integer> first = heap.get(i);
		Pair<Integer, Integer> second = heap.get(j);

		heap.set(i, second);
		heap.set(j, first);

		location.put(first.element, j);
		location.put(second.element, i);
	}

	/**
	 * Get index of left child of node
	 * 
	 * @param parent index in list
	 * @return index of left child
	 */
	private int left(int parent) {
		return (parent * 2) + 1;
	}

	/**
	 * Get index of right child of node
	 * 
	 * @param parent index in list
	 * @return index of right child
	 */
	private int right(int parent) {
		return (parent * 2) + 2;
	}

	/**
	 * Find parent index of child
	 * 
	 * @param child index
	 * @return parent index
	 */
	private int parent(int child) {
		return (child - 1) / 2;
	}

	// // /*********************************************************
	// // * These are optional private methods that may be useful
	// // * If you don't use them, please delete them!
	// // *********************************************************/
	// /**
	// * Print the underlying list representation
	//*/
	 public void printMap() {
	 System.out.println(location);
	 }

	// // /**
	// // * Print the entries in the location map
	// // */
	 public void printHeap() {
		for (int i = 0; i < size(); i++) {
		 System.out.println("Element is: " + heap.get(i).element + " Priority is: " +
		 heap.get(i).priority);
	    }

	}
}
