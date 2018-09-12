package com.heaven7.ve.utils;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.List;

/**
 * @author heaven7
 */
public class Flags {

    public static boolean hasFlags(int totalFlogs, int requireFlags){
        return (totalFlogs & requireFlags) == requireFlags;
    }

    public static String getFlagsString(int flags, IFlagDelegate delegate){
        List<Integer> list = MathUtil.parseFlags(flags);
        if(Predicates.isEmpty(list)){
            return null;
        }
        return VisitServices.from(list)
                .map(new ResultVisitor<Integer, String>() {
                    @Override
                    public String visit(Integer flag, Object param) {
                        return delegate.getFlagString(flag);
                    }
                }).asListService().joinToString(", ");
    }

    public interface IFlagDelegate{
        /**
         * get flag string
         * @param flag the flag
         * @return the str
         */
        String getFlagString(int flag);
    }

}
