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

public class SpriterObject {

    private int mFolder;
    private int mFile;
    private float mX;
    private float mY;
    private float mPivotX;
    private float mPivotY;
    private float mAngle;
    
    public SpriterObject(int pFolder, int pFile, float pX, float pY, float pPivotX, float pPivotY, float pAngle) {
        this.mFolder = pFolder;
        this.mFile = pFile;
        this.mX = pX;
        this.mY = pY;
        this.mPivotX = pPivotX;
        this.mPivotY = pPivotY;
        this.mAngle = pAngle;
    }

    public int getFolder() {
        return mFolder;
    }

    public int getFile() {
        return mFile;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public float getPivotX() {
        return mPivotX;
    }

    public float getPivotY() {
        return mPivotY;
    }
    
    public float getAngle() {
        return mAngle;
    }
}
