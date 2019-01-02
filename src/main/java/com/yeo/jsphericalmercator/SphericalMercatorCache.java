/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.yeo.jsphericalmercator;

/**
 *
 * @author yeozkaya
 */
public class SphericalMercatorCache {
    
    private long[] Ac;
    private double[] Bc;
    private double[] Cc;
    private long[] zc;

    public SphericalMercatorCache() {
        Ac = new long[30];
        Bc = new double[30];
        Cc = new double[30];
        zc = new long[30];
    }
    
    public long[] getAc() {
        return Ac;
    }

    public void setAc(long[] Ac) {
        this.Ac = Ac;
    }

    public double[] getBc() {
        return Bc;
    }

    public void setBc(double[] Bc) {
        this.Bc = Bc;
    }

    public double[] getCc() {
        return Cc;
    }

    public void setCc(double[] Cc) {
        this.Cc = Cc;
    }

    public long[] getZc() {
        return zc;
    }

    public void setZc(long[] zc) {
        this.zc = zc;
    }
}
