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

public class SpriterMainlineKey {
    
    private int mTime;
    private ArrayList<SpriterObjectRef> mObjectsRef;
    
    public SpriterMainlineKey() {
        this(0);
    }
    
    public SpriterMainlineKey(int pTime) {
        this.mTime = pTime;
        this.mObjectsRef = new ArrayList<SpriterObjectRef>();
    }
    
    public int getTime() {
        return mTime;
    }
    
    public void addObjectRef(SpriterObjectRef pObjectRef) {
        mObjectsRef.add(pObjectRef);
    }
    
    public SpriterObjectRef getObjectRef(int pObjectRefIndex) {
        return mObjectsRef.get(pObjectRefIndex);
    }
    
    public ArrayList<SpriterObjectRef> getObjectRefs() {
        return mObjectsRef;
    }
    
    public int getNumObjectsRefs() {
        return mObjectsRef.size();
    }
}
