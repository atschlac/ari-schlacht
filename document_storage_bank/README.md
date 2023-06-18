
# Document Store/ Search Engine Project

The Document Store project is a Java application that provides storage and retrieval capabilities for documents. It allows users to store plain text and binary data documents in an in-memory hash table, perform keyword searches, implement undo functionality, and manage document usage using a min heap. The project also introduces memory management using a two-tier storage system (RAM and Disk) implemented with a BTree.

## Functionality Overview / Project Components

This section provides an overview of the different components and functionalities implemented in the Document Store project. Click on the links below to navigate to each section:

- [In-Memory Document Store (HashTable)](#in-memory-document-store-hashtable)
- [Undo Support using a Stack](#undo-support-using-a-stack)
- [Keyword Search using a Trie](#keyword-search-using-a-trie)
- [Memory Management, Part 1: Tracking Document Usage via a Heap](#memory-management-part-1-tracking-document-usage-via-a-heap)
- [Memory Management, Part 2: Two Tier Storage (RAM and Disk) Using a BTree](#memory-management-part-2-two-tier-storage-ram-and-disk-using-a-btree)


## Build an In-Memory Document Store (HashTable)

- Implement a hash table using separate chaining to handle collisions.
- The hash table is fixed in size with an array length of 5.
- Documents can be stored as plain text (String) or binary data (byte[]).
- The hash table class is called `HashTableImpl` and implements the `HashTable` interface.

## Add Undo Support to the Document Store Using a Stack

- Implement array doubling to support unlimited entries in the hash table.
- Introduce a command stack to track document operations.
- Users can undo the last action or undo the last action on a specific document.
- Undo is achieved by calling the `undo` method on the corresponding command.

## Keyword Search Using a Trie

- Implement a Trie data structure for keyword searching.
- Case-sensitive keyword search and word counting.
- Search results are returned in descending order based on word occurrences.
- Trie implementation is done in the `TrieImpl` class.

## Memory Management, Part 1: Tracking Document Usage via a Heap

- Use a min heap to track document usage based on last access time.
- Only a fixed number of documents can be stored in memory.
- When the limit is reached, the least recently used document is evicted.
- Documents extend the `Comparable` interface based on last use time.
- The min heap implementation is done in the `MinHeapImpl` class.

## Memory Management, Part 2: Two Tier Storage (RAM and Disk) Using a BTree

- Implement a two-tier storage system using RAM and Disk.
- Use a BTree data structure to store documents in both tiers.
- Documents that don't fit in RAM are written to the disk and accessed when needed.
- BTree implementation is done in the `BTreeImpl` class.

## Getting Started

To use the Document Store project, follow these steps:

1. Clone the repository or download the source code.
2. Build and compile the Java classes.
3. Use the provided interfaces and classes to interact with the document store functionality.
4. Refer to the JavaDoc comments in the code for detailed information on each class and method.

## Contributing

Contributions to the Document Store project are welcome! If you have any improvements, bug fixes, or new features to add, please submit a pull request.

## License

The Document Store project is open-source and available under the [MIT License](LICENSE.md).

---
# Document Store Project

The Document Store project is a Java application that provides storage and retrieval capabilities for documents. It allows users to store plain text and binary data documents in an in-memory hash table, perform keyword searches, implement undo functionality, and manage document usage using a min heap. The project also introduces memory management using a two-tier storage system (RAM and Disk) implemented with a BTree.
