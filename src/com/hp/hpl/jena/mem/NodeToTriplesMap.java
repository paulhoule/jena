/*
  (c) Copyright 2003, 2004 Hewlett-Packard Development Company, LP, all rights reserved.
  [See end of file]
  $Id: NodeToTriplesMap.java,v 1.8 2004-07-07 15:42:27 chris-dollin Exp $
*/

package com.hp.hpl.jena.mem;

import java.util.*;

import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.util.HashUtils;
import com.hp.hpl.jena.util.iterator.NiceIterator;
import com.hp.hpl.jena.util.iterator.NullIterator;

/**
	NodeToTriplesMap: a map from nodes to sets of triples.
	@author kers
*/
public final class NodeToTriplesMap 
    {
    private Map map = HashUtils.createMap();
    
    private int size = 0;
    
    public Iterator domain()
        { return map.keySet().iterator(); }
    
    public void add( Node o, Triple t ) 
        {
        Set s = (Set) map.get( o );
        if (s == null) map.put( o, s = HashUtils.createSet() );
        if (s.add( t )) size += 1; 
        }

    public void remove( Node o, Triple t ) 
        {
        Set s = (Set) map.get( o );
        if (s != null) 
            {
            if (s.remove( t )) size -= 1;
            if (s.isEmpty()) map.put( o, null );
        	}
        }

    public Iterator iterator(Node o) 
        {
        Set s = (Set) map.get( o );
        return s == null ? NullIterator.instance :  s.iterator();
        }
    
    public Iterator iterator()
        {
        final Iterator nodes = domain();
        return new NiceIterator()
        	{
            private Iterator current = NullIterator.instance;
            
            public Object next()
                {
                if (hasNext() == false) noElements( "NodeToTriples iterator" );
                return current.next();
                }
            
            public boolean hasNext()
                {
                while (true)
                    {
                    if (current.hasNext()) return true;
                    if (nodes.hasNext() == false) return false;
                    current = iterator( (Node) nodes.next() );
                    }
                }
            
            public void remove()
                {
                current.remove();
                size -= 1;
                }
        	};
        }
    
    public void clear() 
        { map.clear(); size = 0; }
    
    public int size()
        { return size; }
    
    public boolean isEmpty()
        { return size == 0; }
    }
/*
    (c) Copyright 2003, 2004 Hewlett-Packard Development Company, LP
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:

    1. Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.

    2. Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.

    3. The name of the author may not be used to endorse or promote products
       derived from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
    IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
    NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
    THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/