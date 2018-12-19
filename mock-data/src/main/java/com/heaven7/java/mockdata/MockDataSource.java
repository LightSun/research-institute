package com.heaven7.java.network;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heaven7
 */
public interface MockDataSource<T> {

    T getData();

    List<T> getBatchData(int count);


    abstract class SimpleMockDataSource<T> implements MockDataSource<T>{

        @Override
        public T getData() {
            return onCreateData(-1);
        }

        @Override
        public List<T> getBatchData(int count) {
            List<T> list = new ArrayList<>();
            for (int i = 0 ; i < count ; i ++){
                list.add(onCreateData(i));
            }
            return list;
        }

        /**
         * called on create data
         * @param index the index . often from 0. -1 means don't care it.
         * @return the data
         */
        protected abstract T onCreateData(int index);
    }
}
