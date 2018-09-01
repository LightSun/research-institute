package com.heaven7.ve.colorgap;

import com.heaven7.ve.SimpleCopyDelegate;

/**
 * @author heaven7
 */
public class Scores extends SimpleCopyDelegate{

    private float shotTypeScore;
    private float personNumberScore;
    private float highLightScore;

    public float getShotTypeScore() {
        return shotTypeScore;
    }
    public void setShotTypeScore(float shotTypeScore) {
        this.shotTypeScore = shotTypeScore;
    }

    public float getPersonNumberScore() {
        return personNumberScore;
    }

    public void setPersonNumberScore(float personNumberScore) {
        this.personNumberScore = personNumberScore;
    }

    public float getHighLightScore() {
        return highLightScore;
    }

    public void setHighLightScore(float highLightScore) {
        this.highLightScore = highLightScore;
    }

    public float getTotalScore(){
        return shotTypeScore + personNumberScore + highLightScore;
    }

    @Override
    public String toString() {
        return "Scores{" +
                "shotTypeScore=" + shotTypeScore +
                ", personNumberScore=" + personNumberScore +
                ", highLightScore=" + highLightScore +
                '}';
    }

    @Override
    public void setFrom(SimpleCopyDelegate sc) {
        Scores from  = (Scores) sc;
        setHighLightScore(from.getHighLightScore());
        setPersonNumberScore(from.getPersonNumberScore());
        setShotTypeScore(from.getShotTypeScore());
    }
}
