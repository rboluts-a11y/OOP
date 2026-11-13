package org.example;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();
        System.out.println("Is empty: " + hashTable.isEmpty());
        hashTable.put("one", 1);
        hashTable.put("two", 2.5);
        hashTable.put("six-seven", 67);
        System.out.println("Value for 'one': " + hashTable.get("one"));
        hashTable.update("one", 1.0);
        System.out.println("Contains 'two': " + hashTable.hasKey("two"));
        System.out.println("Size: " + hashTable.size());
        System.out.println("Hash table: " + hashTable);
        System.out.println("Iterating over elements:");
        for (Map.Entry<String, Number> entry : hashTable) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println("Removing ('six-seven', 67): " + hashTable.remove("six-seven", 67));
        System.out.println("Removing 'one': " + hashTable.remove("one"));
        HashTable<String, Number> anotherTable = new HashTable<>();
        anotherTable.put("two", 2.5);
        System.out.println("Tables are equal: " + hashTable.equals(anotherTable));
        hashTable.remove("two");
        System.out.println("Is empty: " + hashTable.isEmpty());
    }
}