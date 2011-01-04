/*
 * $Id: CatReader.java 3442 2010-12-25 16:28:07Z kredel $
 */

package edu.jas.util;


import java.io.Reader;
import java.io.IOException;

//import org.apache.log4j.Logger;



/**
 * Reader to concat two readers.
 * Read from first reader until it is empty, then read from second reader.
 * @author Heinz Kredel
 */

public class CatReader extends Reader {


    // private static final Logger logger = Logger.getLogger(CatReader.class);
    // private static boolean debug = logger.isDebugEnabled();


    private final Reader first;


    private final Reader second;


    private boolean doFirst;


    /** 
     * Constructor.
     * @param f first Reader.
     * @param s second Reader.
     */
    public CatReader(Reader f, Reader s) {
        first = f;
        second = s;
        doFirst = true;
    }


    /** 
     * Read char array.
     * @param cbuf array.
     * @param off start offset.
     * @param len number of chars to read.
     * @return number of chars read, or -1.
     */
    public int read(char[] cbuf, int off, int len) throws IOException {
        int i = -1;
        if ( doFirst ) {
            i = first.read(cbuf,off,len);
            if ( i < 0 ) {
                doFirst = false;
                i = second.read(cbuf,off,len);
            }
        } else {
            i = second.read(cbuf,off,len);
        }
        //System.out.println("i = " + i);
        return i;
    }


    /** 
     * Close this Reader.
     */
    public void close() throws IOException {
        try {
            first.close();
        } finally {
            second.close();
        }
    }

}
