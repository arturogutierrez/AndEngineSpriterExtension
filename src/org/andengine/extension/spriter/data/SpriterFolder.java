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

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.util.adt.io.in.AssetInputStreamOpener;
import org.andengine.util.debug.Debug;

import android.content.Context;

public class SpriterFolder {

    private String                 mName;
    private ArrayList<SpriterFile> mFiles;

    public SpriterFolder(String name) {
        this.mName = name;
        this.mFiles = new ArrayList<SpriterFile>();
    }

    public String getName() {
        return mName;
    }

    public ArrayList<SpriterFile> getFiles() {
        return mFiles;
    }

    public void addFile(SpriterFile file) {
        mFiles.add(file);
    }

    public void loadTextures(Context context, TextureManager textureManager) {
        loadTextures(context, textureManager, BitmapTextureFormat.RGBA_8888);
    }

    public void loadTextures(Context context, TextureManager textureManager, BitmapTextureFormat pBitmapTextureFormat) {
        for (SpriterFile file : mFiles) {
            try {
                // Create textures
                BitmapTexture bitmapTexture = new BitmapTexture(textureManager, new AssetInputStreamOpener(context.getAssets(),
                        BitmapTextureAtlasTextureRegionFactory.getAssetBasePath() + file.getName()), pBitmapTextureFormat);
                // Mark it as pending to load
                bitmapTexture.load();

                // Create texture Region
                file.setTextureRegion(TextureRegionFactory.extractFromTexture(bitmapTexture));
            } catch (Exception e) {
                Debug.e("SpriterExtension: Unable to load '" + file.getName() + "'", e);
            }
        }
    }

    public void unloadTextures() {
        for (SpriterFile file : mFiles) {
            if (file.getTextureRegion() != null && file.getTextureRegion().getTexture() != null) {
                file.getTextureRegion().getTexture().unload();
            }
        }
    }
}
