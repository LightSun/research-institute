package com.heaven7.ve.cross_os;

import com.heaven7.java.base.anno.NeedAndroidImpl;
import com.heaven7.java.base.util.Platforms;

import com.heaven7.ve.cross_os.pc.PCVEFactory;

import static com.heaven7.ve.cross_os.VEFactory.ANDROID_NAME;

/**
 * @author heaven7
 */
@NeedAndroidImpl(ANDROID_NAME)
public abstract class VEFactory {
    public static final String ANDROID_NAME = "com.heaven7.ve.android.AndroidVEFactory";

    private static final VEFactory INSTANCE;

    static {
        String name = Platforms.isAndroid() ? ANDROID_NAME : PCVEFactory.class.getName();
        try {
            INSTANCE = (VEFactory) Class.forName(name).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static VEFactory getDefault(){
        return INSTANCE;
    }

    public abstract IMediaResourceItem newMediaResourceItem();
    public abstract ITimeTraveller newTimeTraveller();
    public abstract IPathTimeTraveller newPathTimeTraveller();
    public abstract IEffectInfo newEffectInfo();
    public abstract ISpecialEffectInfo newSpecialEffectInfo();
    public abstract IFilterInfo newFilterInfo();
    public abstract ITextureInfo newTextureInfo();
    public abstract ITransitionInfo newTransitionInfo();
    public abstract IPlaidInfo newPlaidInfo();

    //------------------------non -abstract =====================
    public ITimeTraveller createTimeTraveller(long start, long end, long maxDuration){
        ITimeTraveller tt = newTimeTraveller();
        tt.setStartTime(start);
        tt.setEndTime(end);
        tt.setMaxDuration(maxDuration);
        return tt;
    }
}
