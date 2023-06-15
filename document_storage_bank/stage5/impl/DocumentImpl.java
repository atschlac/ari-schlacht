package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.stage5.Document;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Character.getNumericValue;

public class DocumentImpl implements Document
{
    //Instance variables
    byte[] binaryData;
    String text;
    URI uri;
    HashMap<String, Integer> wordCountMap;

    long lastTimeUsed;

    //Constructors
     public DocumentImpl(URI uri, String text, Map<String, Integer> wordCountMap)
     {
         //Check for valid input (add a check for an empty uri)
         if(uri == null || text == null || text.equals(""))
         {
             throw new IllegalArgumentException("One of the parameters was either null or empty");
         }

         this.uri = uri;
         this.text = text;
         this.wordCountMap = (HashMap<String, Integer>) wordCountMap;
         if(wordCountMap == null)
         {
             this.wordCountMap = new HashMap<>();
             this.mapWords(text);
         }
     }

     public DocumentImpl(URI uri, byte[] binaryData)
     {
          //Check for valid input (add a check for an empty uri and make sure to check that the array isn't empty)
         if(uri == null || binaryData == null || binaryData.length == 0)
         {
             throw new IllegalArgumentException("One of the parameters was either null or empty");
         }

         this.uri = uri;
         this.binaryData = binaryData;
         this.wordCountMap = new HashMap<>();
        }

        private void mapWords(String txt)
        {
         //Make a new String that removes all the non numbers and letts from the old string
         txt = this.removeNon_AlphanumericChars(txt);
         //Split the String into an array of Strings
         String[] txtArray = txt.split(" ");
         for (String currentWord : txtArray)
         {
             this.wordCountMap.merge(currentWord, 1, Integer::sum);
         }
        }

            private static String removeNon_AlphanumericChars (String str)
            {
            String newStr = "";
            for(int i = 0; i < str.length(); i++)
            {
                //System.out.print(str.charAt(i));
                //If charAt i == a num or char then add it to the newStr
                char currentChar = str.charAt(i);

                if((currentChar >= 65 && currentChar <= 90) || //UPPERCASE LETTERS
                    (currentChar >= 97 && currentChar <= 122) || //LOWERCASE LETTERS
                    (currentChar >= 48 && currentChar <= 57) || //NUMBERS
                        currentChar == 32) //ASCII FOR A SPACE
                {
                    newStr += str.charAt(i);
                }
            }
            return newStr;
        }



    @Override
    public String getDocumentTxt()
    {
        return this.text;
    }

    @Override
    public byte[] getDocumentBinaryData()
    {
        return this.binaryData;
    }

    @Override
    public URI getKey()
    {
        return this.uri;
    }

    @Override
    public int wordCount(String word)
    {
        return this.wordCountMap.get(word) != null ? this.wordCountMap.get(word): 0;
    }

    @Override
    public Set<String> getWords()
    {
        //If the doc is has binary data, then the keySet should be an empty set
        return this.wordCountMap.keySet();
    }

    @Override
    public long getLastUseTime()
    {
        return this.lastTimeUsed;
    }

    @Override
    public void setLastUseTime(long timeInNanoseconds)
    {
        this.lastTimeUsed = timeInNanoseconds;
    }

    @Override
    public Map<String, Integer> getWordMap()
    {
        return this.wordCountMap;
    }

    @Override
    public void setWordMap(Map<String, Integer> wordMap)
    {
        this.wordCountMap = (HashMap<String, Integer>) wordMap;
    }

    @Override
    public int hashCode() //Method done
    {
        int result = uri.hashCode();
        result = 31 * result + (text != null ? text.hashCode() : 0); result = 31 * result + Arrays.hashCode(binaryData);
        return Math.abs(result);
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == null)
        {
            //System.out.println("o is null");
            return false;
        }

        if(this == o)
        {
            //System.out.println("o is pointing to the same reference");
            return true;
        }

        if(this.getClass() != o.getClass())
        {
            //System.out.println("o is a different class");
            return false;
        }

        Document otherDocument = (Document)o;
        if(this.hashCode() == otherDocument.hashCode())
        {
            //System.out.println("Hashcodes are the same");
            return true;
        }

        //System.out.println("Hashcodes are not the same");
        return false;
    }

    @Override
    public int compareTo(Document o)
    {
        if(o == null)
            throw new NullPointerException();
        return (int)(this.getLastUseTime() - o.getLastUseTime());
    }
}