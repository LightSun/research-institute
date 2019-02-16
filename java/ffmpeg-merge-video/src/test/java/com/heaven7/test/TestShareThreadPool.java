package com.heaven7.test;

import com.heaven7.ve.utils.SharedThreadPool;
import org.junit.Test;

/**
 * @author heaven7
 */
public class TestShareThreadPool {

    private final SharedThreadPool mPool = new SharedThreadPool();

    @Test
    public void test1(){
        TestOnwner o2 = new TestOnwner();
        mPool.addMember(o2);
        assert mPool.getAliveMembers().size() == 1;
        {
            TestOnwner o1 = new TestOnwner();
            mPool.addMember(o1);
            assert mPool.getAliveMembers().size() == 2;
            mPool.removeMember(o1);
        }
        assert mPool.getAliveMembers().size() == 1;
    }

    private static class TestOnwner{

    }
}
