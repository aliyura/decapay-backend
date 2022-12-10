package com.dcagon.decapay.utils;

import lombok.RequiredArgsConstructor;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class LocalStorage {

    private final MemcachedClient memcachedClient;

    public void save(String key,String value, int expiryInSeconds) {

        try {

            memcachedClient.set(key, expiryInSeconds, value);

        } catch (TimeoutException | InterruptedException | MemcachedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String getValueByKey(String key) {
        try {
            return memcachedClient.get(key);
        } catch (Exception e) {
            return null;
        }
    }
    public Boolean keyExist(String key) {
        try {
            return memcachedClient.get(key)!=null;
        } catch (Exception e) {
            return null;
        }
    }



    public void clear(String key) {
        try {
            memcachedClient.delete(key);
        } catch (TimeoutException | InterruptedException | MemcachedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
