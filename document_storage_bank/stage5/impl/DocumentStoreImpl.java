package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.*;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

public class DocumentStoreImpl implements DocumentStore
{
    //Instance variables
    BTreeImpl<URI, Document> documentBTree = new BTreeImpl<>();
    private Set<URI> docsOnDisk = new HashSet<>();
    TrieImpl<URI> documentSearchTrie = new TrieImpl<>();
    StackImpl<Undoable> commandStack = new StackImpl<>();
    MinHeapImpl<HeapNode> documentPriorityQueue = new MinHeapImpl<>();
    int pQDocSize = 0;
    int maxDocLimit = 50; //Default
    int pQByteSize = 0;
    int maxByteLimit = 10000; //Default

    //Constructor
    public DocumentStoreImpl()
    {   //Remeber to fix the File path to set it to the current user.dir
        this.documentBTree.setPersistenceManager(new DocumentPersistenceManager(new File(System.getProperty("user.dir"))));
    }

    public DocumentStoreImpl(File baseDir)
    {
        this.documentBTree.setPersistenceManager(new DocumentPersistenceManager(baseDir));
    }


    @Override
    public int put(InputStream input, URI uri, DocumentFormat format) throws IOException
    {
        this.validate(uri, format);
        //If the input (value) is null, then call the delete method to delete that key
        if(input == null)
        {
            return this.deleteDoc(input, uri, format);
        }

        DocumentImpl tempDocument = createDoc(input, uri, format);

        if(this.documentBTree.get(uri) != null) //This is a new put, so add 1 to the size (if this were true, then it means the key is already contained and thus no need to update pqSize bc it's just a doc swap
        {
            this.removeDocFromPQ(this.documentBTree.get(uri).getKey());
            //Delete the oldDocument from the Trie
            this.removeDocFromTrie(this.documentBTree.get(uri));
        }

        this.checkMemoryLimitForPut(this.getByteAmount(tempDocument));
        //Insert the Document object into the hash table with URI as the key and the Document object as the value
        DocumentImpl oldDocument = (DocumentImpl) this.documentBTree.put(uri, tempDocument);

        this.setDocTime(tempDocument);
        HeapNode tempHNode = new HeapNode(uri, this.documentBTree);
        this.documentPriorityQueue.insert(tempHNode);
        this.pQDocSize++;
        this.pQByteSize += this.getByteAmount(tempDocument);


        //Add this doc to every collection in the trie that corresponds to the words in the doc
        this.addWordsToTrie(tempDocument);
        //Return the hashCode of the previous document that was stored in the hashTable at that URI, or zero if there was none
        if(oldDocument != null)
        {   //Make an undo function to replace the currentDoc with the oldDoc
            Function<URI, Boolean> tempFunction = (currentURI) ->
                {   //Remove currentDoc from PQ
                    this.removeDocFromPQ(currentURI);
                    //Add oldDoc to PQ
                    this.checkMemoryLimitForPut(this.getByteAmount(oldDocument));
                    this.setDocTime(oldDocument);
                    HeapNode tempHeapNode = new HeapNode(uri, this.documentBTree);
                    this.documentPriorityQueue.insert(tempHeapNode);
                    this.pQDocSize++;
                    this.pQByteSize += this.getByteAmount(oldDocument);

                    this.documentBTree.put(uri, oldDocument);
                    //Remove the current doc from Trie and replace it with oldDoc
                    this.removeDocFromTrie(tempDocument);
                    this.addWordsToTrie(oldDocument);
                    return true;
                };
            this.commandStack.push(new GenericCommand<URI>(uri, tempFunction));

            return oldDocument.hashCode();
        }
        //This was a new put for this key
        //Make a command to undo the current put
        Function<URI, Boolean> tempFunction = (currentUri) ->
        {
            this.removeDocFromTrie(tempDocument);
            this.removeDocFromPQ(currentUri);
            this.documentBTree.put(currentUri, null);
            return true;
        };

        this.commandStack.push(new GenericCommand<URI>(uri, tempFunction));
        return 0;
    }

        private void checkMemoryLimitForPut(int newBytes)
        {
            //System.out.println("Checked memory limit");
            //System.out.println("Size of PQ + 1: " + (this.pQDocSize + 1) + " Limit: " + this.maxDocLimit);
            if(this.pQDocSize + 1 > this.maxDocLimit || this.pQByteSize + newBytes > this.maxByteLimit)
            {
                //Keep remove docs until it this
                while(this.pQDocSize + 1 > this.maxDocLimit || this.pQByteSize + newBytes > this.maxByteLimit)
                {
                    Document docToRemove = this.documentPriorityQueue.remove().getCorrespondingDoc();
                    this.pQDocSize--;
                    this.pQByteSize -= this.getByteAmount(docToRemove);

                    //this.removeDocFromTrie(docToRemove);

                    try
                    {
                        this.documentBTree.moveToDisk(docToRemove.getKey()); //This should move docs to disk instead of delete them
                    }
                    catch (Exception ignored)
                    { }

                    this.docsOnDisk.add(docToRemove.getKey());

                    //this.removeFromCommandStack(docToRemove); //TEST THIS -------------------------------------------------------
                }
            }
        }

        private void validate(URI uri, DocumentFormat format)
        {
            //Determine if need to throw an exception
            if(uri == null || format == null)
            {
                //System.out.println("Chapped Execption");
                throw new IllegalArgumentException("uri or format is null");
            }
        }

        private int deleteDoc(InputStream input, URI uri, DocumentFormat format)
        {
            //System.out.println("Input is null, deleting");
            if(this.documentBTree.get(uri) == null)
            {
                //System.out.println("Doesn't contain uri");
                return 0;
            }
            else
            {
                DocumentImpl oldDoc = (DocumentImpl) this.documentBTree.get(uri);
                //Add an undo command
                Function<URI, Boolean> tempFunction = (currentURI) ->
                {   //Command to do a new put
                    this.checkMemoryLimitForPut(this.getByteAmount(oldDoc));
                    this.documentBTree.put(uri, oldDoc);

                    this.setDocTime(oldDoc);
                    HeapNode tempHNode = new HeapNode(oldDoc.getKey(), this.documentBTree);
                    this.documentPriorityQueue.insert(tempHNode);
                    this.pQDocSize++;
                    this.pQByteSize += this.getByteAmount(oldDoc);

                    this.addWordsToTrie(oldDoc);
                    return true; //What value am I supposed to be returning???
                };
                this.commandStack.push(new GenericCommand<URI>(uri, tempFunction));

                this.removeDocFromTrie(oldDoc);
                this.removeDocFromPQ(uri);
                this.documentBTree.put(uri, null);

                return oldDoc.hashCode();
            }
        }

        private void removeDocFromPQ(URI uri)
        {
            //Make a new PQ
            MinHeapImpl<HeapNode> newPQ = new MinHeapImpl<>();
            int newPQSize = 0;
            int newPQByteSize = 0;
            while(0 < this.pQDocSize)
            {
                //Remove from PQ
                Document tempDoc = this.documentPriorityQueue.remove().getCorrespondingDoc();
                this.pQDocSize--;
                this.pQByteSize -= this.getByteAmount(tempDoc);
                //If doc removed from PQ != uri (doc we not are supposed to delete)
                if(tempDoc.getKey() != uri) {//Put the doc into the new PQ
                    newPQ.insert(new HeapNode(tempDoc.getKey(), this.documentBTree));
                    newPQSize++;
                    newPQByteSize += this.getByteAmount(tempDoc);
                }
            }
            this.documentPriorityQueue = newPQ;
            this.pQDocSize = newPQSize;
            this.pQByteSize = newPQByteSize;
        }

        private void addWordsToTrie(Document tempDoc)
        {
            for(String tempWord: tempDoc.getWords())
            {
                this.documentSearchTrie.put(tempWord, tempDoc.getKey());
            }
        }

        private DocumentImpl createDoc(InputStream input, URI uri, DocumentFormat format) throws IOException
        {
            //Read the bytes from input and put them into a byte array named binaryData
            byte[] binaryData = input.readAllBytes();
            DocumentImpl tempDocument = null;

            //Create an instance of DocumentImpl with the URI and the String or byte[]that was passed to you.
            //If the format is TXT, convert the byte array into a String
            if(format.equals(DocumentFormat.TXT))
            {
                //System.out.println("Creating a TXT doc");
                String txt = new String(binaryData, StandardCharsets.UTF_8);
                tempDocument = new DocumentImpl(uri, txt, null);
            }
            else
            {
                //System.out.println("Creating a byte[] doc");
                //If the format is byte[], then make a new Document with the byte[]
                tempDocument = new DocumentImpl(uri, binaryData);
            }
            return tempDocument;
        }

        private void setDocTime(Document tempDoc)
        {
            this.setDocTime(tempDoc, System.nanoTime());
        }

        private void setDocTime(Document tempDoc, long currentTime)
        {
            tempDoc.setLastUseTime(currentTime);
        }


    @Override
    public Document get(URI uri)
    {
        boolean isOnDisk = false;
        if(this.docsOnDisk.contains(uri))
            isOnDisk = true;

        Document tempDoc = this.documentBTree.get(uri);
        if(tempDoc != null) {
            if(isOnDisk)
            {
                this.setDocTime(tempDoc);
                this.documentPriorityQueue.insert(new HeapNode(tempDoc.getKey(), this.documentBTree));
                this.pQDocSize++;
                this.pQByteSize += this.getByteAmount(tempDoc);
                this.enforceMemoryLimit();
            }
            else
            {
                this.setDocTime(tempDoc);
                this.documentPriorityQueue.reHeapify(new HeapNode(tempDoc.getKey(), this.documentBTree));
            }
        }
        this.docsOnDisk.remove(uri);
        return tempDoc;
    }

    @Override
    public boolean delete(URI uri)
    {
        if(this.documentBTree.get(uri) == null)
        {
            return false;
        }
        //Make a Command to undo this deletes and add it to this.commandStack
        DocumentImpl docToBeDeleted = (DocumentImpl) this.documentBTree.get(uri);
        Function<URI, Boolean> tempFunction = (currentURI) ->
            {
                //Put the doc back in the HashMap
                this.documentBTree.put(uri, docToBeDeleted);
                //Set a new doc into the PQ
                this.checkMemoryLimitForPut(this.getByteAmount(docToBeDeleted));
                this.setDocTime(docToBeDeleted);
                this.documentPriorityQueue.insert(new HeapNode(docToBeDeleted.getKey(), this.documentBTree));
                this.pQDocSize++;
                this.pQByteSize += this.getByteAmount(docToBeDeleted);

                //Put the doc back in the Trie
                this.addWordsToTrie(docToBeDeleted);

                return true; //What am I returning here?
            };
        this.commandStack.push(new GenericCommand<URI>(uri,tempFunction));

        Document oldDoc = this.get(uri);
        this.removeDocFromTrie(oldDoc);
        this.removeDocFromPQ(uri);
        this.documentBTree.put(uri, null);


        return true;
    }
        private void removeDocFromTrie(Document tempDoc)
        {
            for(String tempWord: tempDoc.getWords())
                {
                    this.documentSearchTrie.delete(tempWord, tempDoc.getKey());
                }
        }

    @Override
    public void undo() throws IllegalStateException
    {
        this.isEmpty();
        this.commandStack.pop().undo();
        //System.out.println("Size of commandStack: " + this.commandStack.size());
    }

    @Override
    public void undo(URI uri) throws IllegalStateException
    {
        this.isEmpty();
        StackImpl<Undoable> tempStack = new StackImpl<>();

        while(this.commandStack.size() != 0)
        {
            if(commandStack.peek() instanceof GenericCommand)
            {
                if (this.undoGCLogic(uri, tempStack))
                    break;
            }

            else//commandStack.peek() is an instanceof CommandSet
            {
                if (this.undoCSLogic(uri, tempStack))
                    break;
            }
        }
        //Put the tempStack back into this.commandStack
        this.refillCommandStack(tempStack);
    }

        private void refillCommandStack(StackImpl<Undoable> tempStack)
        {
            while(tempStack.peek() != null)
            {
                this.commandStack.push(tempStack.pop());
            }
        }

        private boolean undoCSLogic(URI uri, StackImpl<Undoable> tempStack)
        {
            CommandSet<URI> tempCommandSet = (CommandSet<URI>) commandStack.peek();
            if(tempCommandSet.containsTarget(uri))
            {
                tempCommandSet.undo(uri);
                if(tempCommandSet.size() == 0) //If commandSet is now empty
                {
                    this.commandStack.pop();
                }
                return true;
            }

            else
            {
                tempStack.push(this.commandStack.pop());
                this.isEmpty();
            }
            return false;
        }

        private boolean undoGCLogic(URI uri, StackImpl<Undoable> tempStack)
        {
            GenericCommand<URI> tempGenericCommand = (GenericCommand<URI>) commandStack.peek();
            if(tempGenericCommand.getTarget().equals(uri))
            {
                //Once its found, call undo on it, but don't add it to the tempStack
                this.commandStack.pop().undo();
                //System.out.println("Size of commandStack: " + this.commandStack.size());

                return true;
            }
            else
            {
                //System.out.println("Size of commandStack: " + this.commandStack.size());
                tempStack.push(this.commandStack.pop());
                this.isEmpty(); //See if next peek is null
            }
            return false;
        }

    @Override
    public List<Document> search(String keyword) //TEST REHEAPIFY
    {

        Comparator<URI> wordCountComparator = (URI uri1, URI uri2) ->
        {
            Document doc1 = this.documentBTree.get(uri1);
            Document doc2 = this.documentBTree.get(uri2);
            int wordCount1 = doc1.wordCount(keyword);
            int wordCount2 = doc2.wordCount(keyword);
            int numberToReturn = wordCount1 - wordCount2;
            this.checkIfURIWasOnDisk(uri1);
            this.checkIfURIWasOnDisk(uri2);
            return numberToReturn;
        };

        //Get a sorted List of the docs that contain this keyword
        List<URI> uriSearchResults = this.documentSearchTrie.getAllSorted(keyword, wordCountComparator);
        List<Document> searchResults = this.convertUriToDocList(uriSearchResults, keyword);
        //Update times on all the searchResults
        if(keyword.length() > 0)
        {
            this.setListOfDocsTimes(searchResults);
            this.reHeapifyDocsInPQ(searchResults);
        }

        return searchResults;
    }

        private void checkIfURIWasOnDisk(URI uri)
        {
            if(this.docsOnDisk.contains(uri)) {
                try
                {
                    this.documentBTree.moveToDisk(uri);
                } catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        }

        private List<Document> convertUriToDocList(List<URI> uriList, String keyword)
        {
            LinkedList<Document> docList = new LinkedList<>();
            for(URI uri: uriList)
            {
                docList.add(this.documentBTree.get(uri));
                this.checkIfURIWasOnDisk(uri);
            }
            //Create a comparator
            Comparator<Document> docWordCountComparator =
		    (Document doc1, Document doc2 )-> doc2.wordCount(keyword) - (doc1.wordCount(keyword));
            docList.sort(docWordCountComparator);
            return  docList;
        }

        private void reHeapifyDocsInPQ(List<Document> searchResults)
        {
            for(Document doc: searchResults)
            {
                this.documentPriorityQueue.reHeapify(new HeapNode(doc.getKey(), this.documentBTree));
            }
        }

        private void setListOfDocsTimes(List<Document> listOfDocs)
        {
            long currentTime = System.nanoTime();
            for(Document doc: listOfDocs)
            {
                this.setDocTime(doc, currentTime);
            }
        }

    @Override
    public List<Document> searchByPrefix(String keywordPrefix)
    {
        //Create a comparator
        Comparator<URI> wordCountComparator = (URI uri1, URI uri2) ->
        {
            Document doc1 = this.documentBTree.get(uri1);
            Document doc2 = this.documentBTree.get(uri2);
            int wordCount1 = doc1.wordCount(keywordPrefix);
            int wordCount2 = doc2.wordCount(keywordPrefix);
            int numberToReturn = wordCount1 - wordCount2;
            this.checkIfURIWasOnDisk(uri1);
            this.checkIfURIWasOnDisk(uri2);
            return numberToReturn;
        };

        List<URI> uriSearchResults = this.documentSearchTrie.getAllWithPrefixSorted(keywordPrefix, wordCountComparator);
        List<Document> searchResults = this.convertUriToDocList(uriSearchResults, keywordPrefix);

        if(keywordPrefix.length() > 0)
        {
            this.setListOfDocsTimes(searchResults);
            this.reHeapifyDocsInPQ(searchResults);
        }

        this.enforceMemoryLimit();

        return searchResults;
    }

    @Override
    public Set<URI> deleteAll(String keyword)
    {
        //Get all the docs that contain this keyword
        List<Document> docsToDelete = this.getDocsToDelete(keyword);
        //Remove these docs from the trie at every collection
        this.documentSearchTrie.deleteAll(keyword);
        this.removeDocsFromTrie(docsToDelete);
        //Remove these docs from the minHeap
        this.removeMultipleDocsFromPQ(docsToDelete);
        //Remove these docs from the hashtable
        this.removeFromDocTree(docsToDelete);
        //Create undo commandSet
        this.createUndoLogic(docsToDelete);
        //Get the URI's of docToDelete and return them
        return this.getDeletedURIs(docsToDelete);
    }

        private void removeMultipleDocsFromPQ(List<Document> docsToDelete)
        {
            for(Document doc : docsToDelete)
            {
                this.removeDocFromPQ(doc.getKey());
            }
        }

        private void createUndoLogic(List<Document> docsToDelete)
        {
            CommandSet<URI> tempCommandSet = new CommandSet<>();
            for(Document docToBeDeleted: docsToDelete)
            {
                Function<URI, Boolean> tempFunction = (currentURI) ->
                {
                    //Put the doc back in the HashMap
                    this.documentBTree.put(docToBeDeleted.getKey(), docToBeDeleted);
                    //Set a new docTime
                    this.checkMemoryLimitForPut(this.getByteAmount(docToBeDeleted));
                    this.setDocTime(docToBeDeleted);
                    this.documentPriorityQueue.insert(new HeapNode(docToBeDeleted.getKey(), this.documentBTree));
                    this.pQDocSize++;
                    this.pQByteSize += this.getByteAmount(docToBeDeleted);

                    //Put the doc back in the Trie
                    this.addWordsToTrie(docToBeDeleted);
                    return true; //What am I returning here?
                };
                tempCommandSet.addCommand(new GenericCommand<>(docToBeDeleted.getKey(),tempFunction));
            }
            this.commandStack.push(tempCommandSet);
        }

        private List<Document> getDocsToDelete(String keyword)
        {
            //Create a comparator
            Comparator<URI> wordCountComparator =
		    (URI uri1, URI uri2 )-> this.documentBTree.get(uri1).wordCount(keyword) - (this.documentBTree.get(uri2).wordCount(keyword));

            List<URI> uriSearchResults = this.documentSearchTrie.getAllSorted(keyword, wordCountComparator);

            return this.convertUriToDocList(uriSearchResults, keyword);
        }

        private void removeDocsFromTrie(List<Document> docsToDelete)
        {
            for(Document tempDoc: docsToDelete)
            {
                for(String tempWord: tempDoc.getWords())
                {
                    this.documentSearchTrie.delete(tempWord, tempDoc.getKey());
                }
            }
        }

        private Set<URI> getDeletedURIs(List<Document> docToDelete)
        {
            Set<URI> tempSet = new HashSet<>();
            for(Document tempDoc: docToDelete)
            {
                tempSet.add(tempDoc.getKey());
            }

            return tempSet;
        }

        private void removeFromDocTree(List<Document> docsToDelete)
        {
            for(Document currentDoc: docsToDelete)
            {
               this.documentBTree.put(currentDoc.getKey(), null);
            }
        }


    @Override
    public Set<URI> deleteAllWithPrefix(String keywordPrefix)
    {
        List<Document> docsToDelete = getDocsWithPreToDelete(keywordPrefix);
        //Remove these docs from the Trie
        this.documentSearchTrie.deleteAll(keywordPrefix);
        this.removeDocsFromTrie(docsToDelete);
        this.removeMultipleDocsFromPQ(docsToDelete);
        //Remove these docs from the hashtable
        this.removeFromDocTree(docsToDelete);
        //Create undo commandSet
        this.createUndoLogic(docsToDelete);
        //Get the URI's of docToDelete and return them
        return this.getDeletedURIs(docsToDelete);
    }
        private List<Document> getDocsWithPreToDelete(String keyword)
        {
            //Create a comparator
            Comparator<URI> wordCountComparator =
		    (URI uri1, URI uri2 )-> this.documentBTree.get(uri1).wordCount(keyword) - (this.documentBTree.get(uri2).wordCount(keyword));

            List<URI> uriSearchResults = this.documentSearchTrie.getAllWithPrefixSorted(keyword, wordCountComparator);
            return this.convertUriToDocList(uriSearchResults, keyword);
        }

        private void isEmpty()
        {
           if(this.commandStack.peek() == null)
            {
                throw new IllegalStateException();
            }
        }

    @Override
    public void setMaxDocumentCount(int limit)
    {
        if(limit < 0)
            throw new IllegalArgumentException();
        this.maxDocLimit = limit;
        this.enforceMemoryLimit();
    }
        private void enforceMemoryLimit()
        {
            //System.out.println("Checked memory limit");
            if(this.pQDocSize > this.maxDocLimit || this.pQByteSize > this.maxByteLimit)
            {
                //Keep remove docs until it this
                while(this.pQDocSize > this.maxDocLimit || this.pQByteSize > this.maxByteLimit)
                {
                    Document docToRemove = this.documentPriorityQueue.remove().getCorrespondingDoc();
                    this.pQDocSize--;
                    this.pQByteSize -= this.getByteAmount(docToRemove);

                    //this.documentBTree.put(docToRemove.getKey(), null);
                    /*this.documentBTree.moveToDisk(docToRemove.getKey());*/ //This should move docs to disk instead of delete them

                    //this.removeDocFromTrie(docToRemove);

                    try
                    {
                        this.documentBTree.moveToDisk(docToRemove.getKey()); //This should move docs to disk instead of delete them
                    }
                    catch (Exception ignored)
                    { }

                    this.docsOnDisk.add(docToRemove.getKey());

                    //this.removeFromCommandStack(docToRemove); //TEST THIS -------------------------------------------------------
                    //NEED TO WRITE LOGIC TO REMOVE THE DOCTOREMOVE FROM THE UNDO STACK
                }
            }
        }

        private int getByteAmount(Document doc)
        {
            int byteAmount = (doc.getDocumentBinaryData() == null) ? doc.getDocumentTxt().getBytes().length : doc.getDocumentBinaryData().length;
            return byteAmount;
        }

        private void removeFromCommandStack(Document docToRemove)
        {
            if(this.commandStack.peek() == null)
                return;

            StackImpl<Undoable> tempStack = new StackImpl<>();

            while(this.commandStack.size() != 0)
            {
                if(commandStack.peek() instanceof GenericCommand)
                {
                    GenericCommand<URI> tempGenericCommand = (GenericCommand<URI>) commandStack.peek();
                    if(tempGenericCommand.getTarget().equals(docToRemove.getKey()))
                    {
                        //Once its found, pop it off and don't add it to the tempStack
                        this.commandStack.pop();
                        //System.out.println("Size of commandStack: " + this.commandStack.size());
                    }
                    else
                    {
                        //System.out.println("Size of commandStack: " + this.commandStack.size());
                        tempStack.push(this.commandStack.pop());
                        if(this.commandStack.peek() == null)
                            return; //See if next peek is null
                    }
                }

                else//commandStack.peek() is an instanceof CommandSet
                {
                    CommandSet<URI> tempCommandSet = (CommandSet<URI>) commandStack.peek();
                    if(tempCommandSet.containsTarget(docToRemove.getKey()))
                    {
                        tempCommandSet.remove(docToRemove.getKey());
                        if(tempCommandSet.size() == 0) //If commandSet is now empty
                        {
                            this.commandStack.pop();
                        }
                    }

                    else
                    {
                        tempStack.push(this.commandStack.pop());
                        if(this.commandStack.peek() == null)
                            return;
                    }
                }
            }
        //Put the tempStack back into this.commandStack
        this.refillCommandStack(tempStack);
        }

    @Override
    public void setMaxDocumentBytes(int limit)
    {
        if(limit < 0)
            throw new IllegalArgumentException();
        this.maxByteLimit = limit;
        this.enforceMemoryLimit();
    }

            private class HeapNode implements Comparable<HeapNode>
            {
                private URI uri;
                private BTreeImpl<URI, Document> Btree;
                private HeapNode(URI uri, BTreeImpl<URI, Document> bTree)
                {
                    this.uri = uri;
                    this.Btree = bTree;
                }

                private URI getUri()
                {
                    return this.uri;
                }

                private BTreeImpl<URI, Document> getBtree()
                {
                    return this.Btree;
                }

                private Document getCorrespondingDoc()
                {
                    return this.Btree.get(this.uri);
                }
                @Override
                public int compareTo(HeapNode o)
                {
                    if(o == null)
                        throw new NullPointerException();
                    return (int) (this.getCorrespondingDoc().getLastUseTime() - o.getCorrespondingDoc().getLastUseTime());
                }

                @Override
                public boolean equals(Object o)
                {
                    if(o == null)
                        return false;
                    if(!this.getClass().equals(o.getClass()))
                        return false;
                    HeapNode otherHeapNode = (HeapNode)o;
                    if(this.getUri().equals(otherHeapNode.getUri()))
                        return true;
                    return false;
                }

            }

}