/*
 * Spriter Extension for AndEngine
 *
 * Copyright (c) 2012 Arturo Gutiérrez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package org.andengine.extension.spriter.data;

import java.util.ArrayList;

public class SpriterAnimation {

    private String  mName;
    private int     mLength;
    private boolean mLooping;
    private ArrayList<SpriterMainlineKey> mMainlineKeys;
    private ArrayList<SpriterTimeline> mTimelines;


    public SpriterAnimation(String name, int length) {
        this(name, length, false);
    }

    public SpriterAnimation(String name) {
        this(name, 0, false);
    }

    public SpriterAnimation(String name, int length, boolean looping) {
        this.mName = name;
        this.mLength = length;
        this.mLooping = looping;
        this.mMainlineKeys = new ArrayList<SpriterMainlineKey>();
        this.mTimelines = new ArrayList<SpriterTimeline>();
    }
    
    public String getName() {
        return mName;
    }
    
    public int getLength() {
        return mLength;
    }
    
    public boolean isLooping() {
        return mLooping;
    }
    
    public void addMainlineKey(SpriterMainlineKey pMainlineKey) {
        mMainlineKeys.add(pMainlineKey);
    }
    
    public int getNumMainlineKeys() {
        return mMainlineKeys.size();
    }
    
    public SpriterMainlineKey getMainlineKey(int pMainlineKeyIndex) {
        return mMainlineKeys.get(pMainlineKeyIndex);
    }
    
    public void addTimeline(SpriterTimeline pTimeline) {
        mTimelines.add(pTimeline);
    }
    
    public SpriterTimeline getTimeline(int pTimelineIndex) {
        return mTimelines.get(pTimelineIndex);
    }
    
    public int getNumTimelines() {
        return mTimelines.size();
    }
}
