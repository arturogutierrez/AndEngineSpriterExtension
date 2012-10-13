/*
 * Spriter Extension for AndEngine
 *
 * Copyright (c) 2012 Arturo Guti�rrez
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
import java.util.Collection;

public class SpriterTimelineKey {
    
    private int mTime;
    private int mSpin;
    private ArrayList<SpriterObject> mObjects;
    
    public SpriterTimelineKey() {
        this(0);
    }
    
    public SpriterTimelineKey(int pTime) {
        this(0, 1);
    }    
    
    public SpriterTimelineKey(int pTime, int pSpin) {
        this.mTime = pTime;
        this.mSpin = pSpin;
        this.mObjects = new ArrayList<SpriterObject>();
    }
    
    public int getTime() {
        return mTime;
    }
    
    public int getSpin() {
        return mSpin;
    }
    
    public void addObject(SpriterObject pObject) {
        mObjects.add(pObject);
    }
    
    public SpriterObject getObject(int pObjectIndex) {
        if (pObjectIndex < mObjects.size()) {
            return mObjects.get(pObjectIndex);
        }
        
        return null;
    }
    
    public Collection<SpriterObject> getObjects() {
        return mObjects;
    }
}
