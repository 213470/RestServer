package client;

import java.util.LinkedList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
	List<String> list = new LinkedList<String>();
	List<String> collection = new LinkedList<String>();
	list.add("one");
	list.add("two");
//	list.add("three");
	list.add("four");
	
	collection.add("one");
//	collection.add("two");
//	collection.add("three");
//	collection.add("four");
	collection.add("five");
//	collection.add("six");
	
	list.retainAll(collection);
	System.out.println(list);
	System.out.println(collection);
	}

}
