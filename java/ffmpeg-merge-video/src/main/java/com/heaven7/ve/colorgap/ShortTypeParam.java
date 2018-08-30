package com.heaven7.ve.colorgap;

/**
 * the short type param of 'medium, near, long'
 * @author heaven7
 */
public class ShortTypeParam {

    private int mediumCount;
    private int nearCount  ;
    private int longCount  ;

    private ShortTypeParam(int mediumCount, int nearCount, int longCount) {
        this.mediumCount = mediumCount;
        this.nearCount = nearCount;
        this.longCount = longCount;
    }

    public int getMediumCount() {
        return mediumCount;
    }
    public int getNearCount() {
        return nearCount;
    }
    public int getLongCount() {
        return longCount;
    }

    public static ShortTypeParam fromTotalCount(int totalCount){
        int mediumCount = 0;
        int nearCount   = 0;
        int longCount   = 0;
        int more = 0;
        if(totalCount > 6){ //medium =3, near = 2, Long = 1
            more = totalCount % 6;
            mediumCount = totalCount / 6 * 3;
            nearCount = totalCount / 6 * 2;
            longCount = totalCount / 6; //totalCount / 6 * 1
        }else if(totalCount == 6){
            mediumCount =  3;
            nearCount = 2;
            longCount =  1;
        }else{
            more = totalCount;
        }
        //has more
        if(more > 0) {
            switch (more) {
                case 1:
                    mediumCount += 1;
                    break;
                case 2:
                    nearCount += 1;
                    mediumCount += 1;
                    break;
                case 3:
                    nearCount += 1;
                    mediumCount += 1;
                    longCount += 1;
                    break;
                case 4:
                    nearCount += 1;
                    mediumCount += 2;
                    longCount += 1;
                    break;
                case 5:
                    nearCount += 2;
                    mediumCount += 2;
                    longCount += 1;
                    break;
                default:
                    throw new UnsupportedOperationException("more = " + more);
            }
        }
        return new ShortTypeParam(mediumCount, nearCount, longCount);
    }

    @Override
    public String toString() {
        return "ShortTypeParam{" +
                "mediumCount=" + mediumCount +
                ", nearCount=" + nearCount +
                ", longCount=" + longCount +
                '}';
    }
}
