package cop3530;

import java.util.Map;
import java.util.Iterator;

/**
 * Creates a hash map using a hash table. This hash table will use two hash 
 * functions to keep the lists inside the table shorter.
 * 
 * @author David Romero PID: 3624439
 * @param <KeyType> The key for the map. This can be any type
 * @param <ValueType> The value associated with the key. This can be any type
 */
public class MyHashMap<KeyType, ValueType> implements
        Iterable<Map.Entry<KeyType, ValueType>>
{
    //The size of the array
    int theSize = 0;                                  
    //The starting size of the table
    public static final int DEFAULT_ARRAY_SIZE = 11;  
    //Array containing the number of nodes in each index of the array
    int lengths[] = null;                       
    //The array used for the hash table
    private Node<KeyType, ValueType>[] arr = null;  
    private HashFunction<KeyType> hash1;        //One of the two hash functions
    private HashFunction<KeyType> hash2;        //One of the two hash functions


    /**
     * Null constructor for the myHashMap object
     */
    public MyHashMap()
    {
        this(null, null);
    }

    /**
     * Accepts two hash functions to generate a hash code
     *
     * @param h1 The first hash function
     * @param h2 The second hash function
     */
    public MyHashMap(HashFunction<KeyType> h1, HashFunction<KeyType> h2)
    {
        hash1 = h1;
        hash2 = h2;

        doClear();
    }

    /**
     * Returns the size of the table
     */
    public int size()
    {
        return theSize;
    }

    /**
     * Performs the clear private routine
     */
    public void clear()
    {
        doClear();
    }

    /**
     * Clears the table
     */
    private void doClear()
    {
        theSize = 0;
        arr = new Node[DEFAULT_ARRAY_SIZE];
        lengths = new int[DEFAULT_ARRAY_SIZE];
    }

    /**
     * Resizes the hash table if the size passes the default size
     */
    private void rehash()
    {
        MyHashMap<KeyType, ValueType> bigger
                = new MyHashMap<KeyType, ValueType>(hash1, hash2);

        bigger.arr = new Node[arr.length * 2];
        bigger.lengths = new int[bigger.arr.length];

        for (Node<KeyType, ValueType> lst : arr)
        {
            for (Node<KeyType, ValueType> p = lst; p != null; p = p.next)
            {
                bigger.put(p.key, p.value);
            }
        }

        arr = bigger.arr;
        lengths = bigger.lengths;

        bigger = null;
    }

    /**
     * Generates a hash code in order to determine where on the table the item
     * goes
     *
     * @param k The key that will be used to generate the code
     * @param h The hash function to be used
     * @return The index
     */
    private int myHash(KeyType k, HashFunction<KeyType> hash)
    {
        if(hash == null)
        {
            return Math.abs( k.hashCode() % arr.length);
        }
        else
        {
            return Math.abs ( hash.hashCode(k) % arr.length);
        }
    }
    
    /**
     * Places an item into the hash map. If the key matches an existing one, the
     * value is then replaced with the new incoming value
     *
     * @param k The incoming key
     * @param v The incoming value
     * @return The the old value removed.
     */
    public ValueType put(KeyType k, ValueType v)
    {
        //If the size of the table exceeds the default array size, rehash
        if (size() > arr.length)
        {
            rehash();
        }

        //The hash codes obtained from the key
        int thisList1 = myHash(k, hash1);
        int thisList2 = myHash(k, hash2);

        //Checking if the incoming key matches an existing key, if it does
        //replace the value and return the old one
        for (Node<KeyType, ValueType> p = arr[thisList1]; p != null; p = p.next)
        {
            if (p.key.equals(k))
            {
                ValueType old = p.value;
                p.value = v;
                return old;
            }
        }

        for (Node<KeyType, ValueType> p = arr[thisList2]; p != null; p = p.next)
        {
            if (p.key.equals(k))
            {
                ValueType old = p.value;
                p.value = v;
                return old;
            }
        }

        //Adding the incoming data to the index with the least amount of 
        //nodes in it.
        if (numOfNodes(arr[thisList1]) <= numOfNodes(arr[thisList2]))
        {
            arr[thisList1] = new Node<>(k, v, arr[thisList1]);
            ++lengths[thisList1];
            ++theSize;
        }
        //Else the second list is the smaller of the two. Add it to this one
        else
        {
            arr[thisList2] = new Node<>(k, v, arr[thisList2]);
            ++lengths[thisList2];
            ++theSize;
        }
        
        //No old value to return
        return null;
    }

    /**
     * Counts the number of nodes that exist in a space of the table
     *
     * @param n The start space
     * @return The number of nodes
     */
    private int numOfNodes(Node n)
    {
        int size = 0;

        //If the space is not empty, then it will step through and count the 
        //number of nodes present
        for (Node<KeyType, ValueType> p = n; p != null; p = p.next)
        {
            ++size;
        }

        return size;
    }

    /**
     * Removes a specified key from the map
     * @param k The key that is to be removed
     * @return True if the object has been removed, false if it has not
     */
    public boolean remove(KeyType k)
    {
        //Getting the hash codes for the list
        int thisList1 = myHash(k, hash1);
        int thisList2 = myHash(k, hash2);

        //If the position specified by hash1 is found, check first element
        if (arr[thisList1] != null )
        {
            if(arr[thisList1].key.equals(k))
            {
                arr[thisList1] = arr[thisList1].next;
                --theSize;
                return true;
            }
            for (Node<KeyType, ValueType> p = arr[thisList1]; p.next != null;
                    p = p.next)
            {
                if (p.next.key.equals(k))
                {
                    p.next = p.next.next;
                    --theSize;
                    return true;
                }
            }
            
        }

        //If the position specified by hash2 is found, check first element
        if (arr[thisList2] != null)
        {
            if(arr[thisList2].key.equals(k))
            {
                arr[thisList2] = arr[thisList2].next;
                --theSize;
                return true;
            }
            for (Node<KeyType, ValueType> p = arr[thisList2]; p.next != null;
                    p = p.next)
            {
                if (p.next.key.equals(k))
                {
                    p.next = p.next.next;
                    --theSize;
                    return true;
                }
            }
        }

        //Else the item that is to removed is never found, therefore return
        //false
        return false;
    }

    /**
     * Returns the desired value associated with the specified key
     *
     * @param k The key
     * @return The value if found, null if not
     */
    public ValueType get(KeyType k)
    {

        //The hash codes obtained from the key
        int thisList1 = myHash(k, hash1);
        int thisList2 = myHash(k, hash2);

        //Using the first hash code, searching the nodes
        for (Node<KeyType, ValueType> p = arr[thisList1]; p != null; p = p.next)
        {
            if (p.key.equals(k))
            {
                return p.value;
            }
        }

        //Using the second hash code, seraching the nodes
        for (Node<KeyType, ValueType> p = arr[thisList2]; p != null; p = p.next)
        {
            if (p.key.equals(k))
            {
                return p.value;
            }
        }
        return null;
    }

    /**
     * Prints out the hashMap
     *
     * @return The printed hashMap
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("[");
        for(Map.Entry<KeyType, ValueType> n : this)
        {
            sb.append(n);
            sb.append(",");
        }
        
        sb.append("]");
        
        return new String(sb);
    }

    /**
     * Creates an iterator for traversing the list.
     *
     * @return An iterator to traverse the list
     */
    public Iterator<Map.Entry<KeyType, ValueType>> iterator()
    {
        return new Iterator<Map.Entry<KeyType, ValueType>>()
        {
            /**
             * Checks if the node has a next link
             * @return True if it has a node, false if it does not
             */
            public boolean hasNext()
            {
                return current != null;
            }

            /**
             * Gets the next item on the list
             * @return The next node
             */
            public Map.Entry<KeyType, ValueType> next()
            {
                final Node<KeyType, ValueType> theCurrent = current;

                current = current.next;

                //If the current list has no more nodes, move to the next list
                if (current == null)
                {
                    ++listNum;
                    advanceToNewList();
                }

                //Creates a new entry that will hold the next node
                Map.Entry<KeyType, ValueType> nextItem = new Map.Entry<KeyType, ValueType>()
                {

                    /**
                     * Returns the current node's key
                     * @return 
                     */
                    @Override
                    public KeyType getKey()
                    {
                        return theCurrent.key;
                    }

                    /**
                     * Returns the current node's value
                     * @return 
                     */
                    @Override
                    public ValueType getValue()
                    {
                        return theCurrent.value;
                    }

                    /**
                     * Sets a new value to the current node
                     * @param value The new value that will be placed
                     * @return The old value that was overwritten 
                     */
                    @Override
                    public ValueType setValue(ValueType value)
                    {
                        ValueType temp = theCurrent.value;
                        theCurrent.value = value;
                        return temp;
                    }
                };

                return nextItem;
            }

            /**
             * Advances to the next index of the table
             */
            private void advanceToNewList()
            {
                while (listNum < arr.length && arr[ listNum ] == null)
                {
                    listNum++;
                }

                if (listNum != arr.length)
                {
                    current = arr[ listNum ];
                }
            }

            //Initializes the advanceToNewList method
            {
                advanceToNewList();
            }

            Node<KeyType, ValueType> current;   // current node
            int listNum;                        // current list #
        };

    }

    /**
     * Creates a node class for the linked list
     * @param <KeyType> The key for the map. Can be any type
     * @param <ValueType> The value associated with the key. Can be any type
     */
    class Node<KeyType, ValueType>
    {

        /**
         * Constructs a node object using a key, a value, and a link to the next
         * node
         * @param k The key
         * @param v The value
         * @param n The next node
         */
        Node(KeyType k, ValueType v, Node<KeyType, ValueType> n)
        {
            key = k;
            value = v;
            next = n;
        }

        /**
         * Prints out the node 
         * @return A string containing the key and the value
         */
        public String toString()
        {
            return key + "=" + value;
        }

        KeyType key;                   //The key of the node
        ValueType value;               //The value of the node
        Node<KeyType, ValueType> next; //The next node
    }

    /**
     * Creates an array that contains the number of entries that have a
     * certain length. For example: The number of lists of length 0, 1, 2, etc. 
     * @return The array containing the distribution of list lengths
     */
    public int[] getLengths()
    {
        int sizeList[] = new int[20];
        
        //Incrementing the count at the index in the array sizeList. It will
        //increment for every list of size at lengths[i].
        for(int i = 0; i < lengths.length; ++i)
        {
            ++sizeList[lengths[i]];
        }

        return sizeList;
    }
}
