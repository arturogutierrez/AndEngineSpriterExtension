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

package org.andengine.extension.spriter;

import java.util.ArrayList;
import java.util.Collection;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.spriter.data.SpriterAnimation;
import org.andengine.extension.spriter.data.SpriterFolder;
import org.andengine.extension.spriter.data.SpriterMainlineKey;
import org.andengine.extension.spriter.data.SpriterObject;
import org.andengine.extension.spriter.data.SpriterObjectRef;
import org.andengine.extension.spriter.data.SpriterTimelineKey;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;

public class SpriterEntity extends Entity {

    private ArrayList<SpriterFolder>    mFolders;
    private ArrayList<SpriterAnimation> mAnimations;
    private SpriterAnimation            mCurrentAnimation;
    private int                         mCurrentAnimationIndex;
    private SpriterMainlineKey          mCurrentMainlineKey;
    private SpriterMainlineKey          mNextMainlineKey;
    // AnimationId / TimelineId / KeyId
    private Sprite                      mSprites[][][];
    private float                       mTime;

    public SpriterEntity() {
        mFolders = new ArrayList<SpriterFolder>();
        mAnimations = new ArrayList<SpriterAnimation>();
        mCurrentAnimation = null;
        mCurrentAnimationIndex = -1;
    }

    public void addFolder(SpriterFolder pFolder) {
        mFolders.add(pFolder);
    }

    public Collection<SpriterFolder> getFolders() {
        return mFolders;
    }

    public void addAnimation(SpriterAnimation pAnimation) {
        mAnimations.add(pAnimation);
    }

    public int getNumAnimations() {
        return mAnimations.size();
    }

    public void setAnimation(int pAnimationIndex) {
        if (pAnimationIndex < mAnimations.size()) {
            mCurrentAnimation = mAnimations.get(pAnimationIndex);
            mCurrentAnimationIndex = pAnimationIndex;

            // Get current and next key
            mCurrentMainlineKey = mCurrentAnimation.getMainlineKey(0);
            mNextMainlineKey = mCurrentAnimation.getMainlineKey(1 % mCurrentAnimation.getNumMainlineKeys());
            // Initialize time
            mTime = 0.0f;
        }
    }

    public void setAnimation(String pAnimationName) {
        for (int i = 0; i < mAnimations.size(); i++) {
            if (mAnimations.get(i).getName().equalsIgnoreCase(pAnimationName)) {
                setAnimation(i);
                break;
            }
        }
    }

    public void loadResources(Context context, TextureManager textureManager, VertexBufferObjectManager pVertexBufferObjectManager) {
        for (SpriterFolder folder : mFolders) {
            folder.loadTextures(context, textureManager);
        }

        // Create sprites
        mSprites = new Sprite[mAnimations.size()][][];
        for (int i = 0; i < mAnimations.size(); i++) {
            mSprites[i] = new Sprite[mAnimations.get(i).getNumTimelines()][];

            for (int j = 0; j < mSprites[i].length; j++) {
                mSprites[i][j] = new Sprite[mAnimations.get(i).getTimeline(j).getNumKeys()];

                for (int k = 0; k < mSprites[i][j].length; k++) {
                    // Get first object from this key (only one object per key
                    // in Spriter Alpha1)
                    SpriterObject object = mAnimations.get(i).getTimeline(j).getTimelineKey(k).getObject(0);
                    // Create Sprite
                    mSprites[i][j][k] = new Sprite(0.0f, 0.0f, mFolders.get(object.getFolder()).getFiles().get(object.getFile()).getTextureRegion(),
                            pVertexBufferObjectManager);
                }
            }
        }
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);

        // We need to quit if there is not one animation runing
        if (mCurrentAnimation == null) {
            return;
        }

        // Increment time
        mTime += pSecondsElapsed;
        // Convert to milliseconds
        float milliseconds = mTime * 1000.0f;
        float startTime = mCurrentMainlineKey.getTime();
        float endTime = mNextMainlineKey.getTime() == 0 ? mCurrentAnimation.getLength() : mNextMainlineKey.getTime();

        // Swap keys if we passed the time
        if (milliseconds > endTime) {
            // Module time only when key is triggered (to save some cpu cycles
            // per frame)
            if (milliseconds > mCurrentAnimation.getLength()) {
                mTime -= mCurrentAnimation.getLength() * 0.001f;
                milliseconds = (int) (mTime * 1000.0f);
            }

            // Search current key starting by last key
            for (int i = mCurrentAnimation.getNumMainlineKeys() - 1; i >= 0; i--) {
                SpriterMainlineKey key = mCurrentAnimation.getMainlineKey(i);
                // Check time
                if (milliseconds >= key.getTime()) {
                    // Set new current and new next keys
                    mCurrentMainlineKey = key;
                    mNextMainlineKey = mCurrentAnimation.getMainlineKey((i + 1) % mCurrentAnimation.getNumMainlineKeys());
                    // Update start and end times
                    startTime = mCurrentMainlineKey.getTime();
                    endTime = mNextMainlineKey.getTime() == 0 ? mCurrentAnimation.getLength() : mNextMainlineKey.getTime();
                    break;
                }
            }
        }

        // Calculate interpolation factor
        float interpolationFactor = (milliseconds - startTime) / (endTime - startTime);
        // Check extreme ranges
        if (interpolationFactor == Float.POSITIVE_INFINITY) {
            interpolationFactor = 0.0f;
        } else if (interpolationFactor < 0) {
            interpolationFactor = 0.0f;
        } else if (interpolationFactor > 1) {
            interpolationFactor = 1.0f;
        } else if (interpolationFactor == Float.NaN) {
            interpolationFactor = 1.0f;
        }

        // Iterate through objects ref within current key
        for (int keyIndex = 0; keyIndex < mCurrentMainlineKey.getNumObjectsRefs(); keyIndex++) {
            // Get current object reference
            SpriterObjectRef currentObjectRef = mCurrentMainlineKey.getObjectRef(keyIndex);
            SpriterObjectRef nextObjectRef = mNextMainlineKey.getObjectRef(keyIndex);
            // Get key from current object ref timeline
            SpriterTimelineKey currentTimelineKey = mCurrentAnimation.getTimeline(currentObjectRef.getTimelineId()).getTimelineKey(
                    currentObjectRef.getTimelineKey());
            // Get key from next Object ref timeline
            SpriterTimelineKey nextTimelineKey = mCurrentAnimation.getTimeline(nextObjectRef.getTimelineId()).getTimelineKey(
                    nextObjectRef.getTimelineKey());

            // Get sprite created previously
            Sprite sprite = mSprites[mCurrentAnimationIndex][currentObjectRef.getTimelineId()][currentObjectRef.getTimelineKey()];
            // Add sprite to main node if it was not added previously or change
            // it for the new sprite in that position
            if (keyIndex >= getChildCount()) {
                // Add to node
                attachChild(sprite);
            } else {
                // Get sprite added at this key index position
                Sprite actualSpriteAdded = (Sprite) getChildByIndex(keyIndex);

                // We need to swap sprite (AndEngine doesn't allow change set a
                // new texture to one sprite created so we need to remove old
                // sprite and add the new one and exactly same position
                if (actualSpriteAdded.getTextureRegion() != sprite.getTextureRegion()) {
                    // Remove old sprite from node
                    mChildren.remove(keyIndex);
                    // And add new sprite with texture changed
                    mChildren.add(keyIndex, sprite);
                } else {
                    // Change to sprite added
                    sprite = actualSpriteAdded;
                }
            }

            // Get first object from this key (only one object per key in
            // Spriter Alpha1)
            SpriterObject object = currentTimelineKey.getObject(0);
            SpriterObject nextObject = nextTimelineKey.getObject(0);

            // Calculate Anchor point
            float anchorPointX = sprite.getWidth() * interpolate(object.getPivotX(), nextObject.getPivotX(), interpolationFactor);
            float anchorPointY = sprite.getHeight() * (1 - interpolate(object.getPivotY(), nextObject.getPivotY(), interpolationFactor));
            // Set new position
            sprite.setPosition(interpolate(object.getX(), nextObject.getX(), interpolationFactor) - anchorPointX,
                    -interpolate(object.getY(), nextObject.getY(), interpolationFactor) - anchorPointY);

            // Convert rotation to AndEngine rotation coord
            float nextRotation = 360 - nextObject.getAngle();
            float curRotation = 360 - object.getAngle();

            // Adapt current rotation depending spin
            if (currentTimelineKey.getSpin() == -1 && (nextRotation - curRotation) < 0) {
                curRotation -= 360;
            } else if (currentTimelineKey.getSpin() == 1 && (nextRotation - curRotation) > 0) {
                curRotation += 360;
            }

            // Interpolate rotation
            float rotation = interpolate(curRotation, nextRotation, interpolationFactor);
            // Set new rotation
            sprite.setRotationCenter(anchorPointX, anchorPointY);
            sprite.setRotation(rotation);
        }

    }

    private float interpolate(float a, float b, float factor) {
        // Check basic case to save some cpu
        if (a == b) {
            return a;
        }
        // Ease linear
        return a + (b - a) * factor;
    }

    public void destroy() {
        // Delete children
        clearEntityModifiers();
        clearUpdateHandlers();
        detachChildren();

        // Delete all sprites unloading textures
        for (int i = 0; i < mSprites.length; i++) {
            for (int j = 0; j < mSprites[i].length; j++) {
                for (int k = 0; k < mSprites[i][j].length; k++) {
                    // Mark for unload
                    mSprites[i][j][k].getTextureRegion().getTexture().unload();
                    // Free Sprite
                    mSprites[i][j][k].dispose();
                    mSprites[i][j][k] = null;
                }

                // Free array
                mSprites[i][j] = null;
            }
            // Free array
            mSprites[i] = null;
        }

        // Free array
        mSprites = null;
    }
}
