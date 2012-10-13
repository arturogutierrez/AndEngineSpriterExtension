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

import org.andengine.opengl.texture.region.ITextureRegion;

public class SpriterFile {

    private String mName;
    private int mWidth;
    private int mHeight;
    private ITextureRegion mTextureRegion;
    
    public SpriterFile(String name, int width, int height) {
        this.mName = name;
        this.mWidth = width;
        this.mHeight = height;
    }
    
    public SpriterFile(String name) {
        this(name, 0, 0);
    }
    
    public String getName() {
        return mName;
    }
    
    public int getWidth() {
        return mWidth;
    }
    
    public int getHeight() {
        return mHeight;
    }

    public void setTextureRegion(ITextureRegion pTextureRegion) {
        this.mTextureRegion = pTextureRegion;
    }
    
    public ITextureRegion getTextureRegion() {
        return mTextureRegion;
    }
}
