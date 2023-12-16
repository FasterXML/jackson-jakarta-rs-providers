package com.fasterxml.jackson.jakarta.rs.base.util;

import java.util.LinkedHashMap;
import java.util.Map;

// TO BE REMOVED FROM 2.18 or later
/**
 * Helper for simple bounded LRU maps used for reusing lookup values.
 *
 * @deprecated Since 2.16.1 use {@code LRUMap} from {@code jackson-databind}.
 */
@Deprecated // since 2.16.1
@SuppressWarnings("serial")
public class LRUMap<K,V> extends LinkedHashMap<K,V>
{
    protected final int _maxEntries;
    
    public LRUMap(int initialEntries, int maxEntries)
    {
        super(initialEntries, 0.8f, true);
        _maxEntries = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest)
    {
        return size() > _maxEntries;
    }
}
