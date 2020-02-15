package sequential;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stack<Item> implements Iterable<Item> {
	private int N;
	private Node first;

	private class Node {
		private Item item;
		private Node next;
	}

	public Stack() {
		first = null;
		N = 0;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return N;
	}

	public void push(Item item) {
		Node oldfirst = first;
		first = new Node();
		first.item = item;
		first.next = oldfirst;
		N++;
	}

	public Item pop() {
		if (isEmpty())
			throw new RuntimeException("Stack underflow");
		Item item = first.item; // save item to return
		first = first.next; // delete first node
		N--;
		return item; // return the saved item
	}

	public Item peek() {
		if (isEmpty())
			throw new RuntimeException("Stack underflow");
		return first.item;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Item item : this)
			s.append(item + " ");
		return s.toString();
	}

	public Iterator<Item> iterator() {
		return new ListIterator();
	}

	private class ListIterator implements Iterator<Item> {
		private Node current = first;

		public boolean hasNext() {
			return current != null;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Item item = current.item;
			current = current.next;
			return item;
		}
	}
}
