/*
 * Spriter Extension for AndEngine
 *
 * Copyright (c) 2012 Arturo GutiŽrrez
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

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.andengine.extension.spriter.data.SpriterAnimation;
import org.andengine.extension.spriter.data.SpriterFile;
import org.andengine.extension.spriter.data.SpriterFolder;
import org.andengine.extension.spriter.data.SpriterMainlineKey;
import org.andengine.extension.spriter.data.SpriterObject;
import org.andengine.extension.spriter.data.SpriterObjectRef;
import org.andengine.extension.spriter.data.SpriterTimeline;
import org.andengine.extension.spriter.data.SpriterTimelineKey;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.util.adt.io.in.AssetInputStreamOpener;
import org.andengine.util.debug.Debug;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

public class SpriterLoader extends DefaultHandler {
    // Properties to access the file
    private String             mName;
    private Context            mContext;
    // Resultant entity
    private SpriterEntity      mSprite;
    // Parsing variables
    private SpriterFolder      mCurrentFolder;
    private SpriterAnimation   mCurrentAnimation;
    private SpriterMainlineKey mCurrentMainlineKey;
    private SpriterTimeline    mCurrentTimeline;
    private SpriterTimelineKey mCurrentTimelineKey;
    private boolean            mInMainline;

    private SpriterLoader(Context context, String name) {
        this.mContext = context;
        this.mName = name;
        this.mSprite = new SpriterEntity();
    }

    private SpriterEntity getSpriterEntity() {
        return mSprite;
    }

    private void startParse() {
        try {
            // Create Parser
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            // Prepare to open file
            AssetInputStreamOpener assetOpener = new AssetInputStreamOpener(mContext.getAssets(),
                    BitmapTextureAtlasTextureRegionFactory.getAssetBasePath() + mName);
            // Start parsing
            parser.parse(assetOpener.open(), this);
            // Free memory
            mCurrentFolder = null;
            mCurrentAnimation = null;
            mCurrentMainlineKey = null;
            mCurrentTimeline = null;
            mCurrentTimelineKey = null;
        } catch (Exception e) {
            Debug.e("SpriterExtension: Unable to load '" + mName + "' file!", e);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (localName.equalsIgnoreCase("folder")) {
            String name = attributes.getValue("name");

            if (name != null) {
                // Create new folder
                mCurrentFolder = new SpriterFolder(name);
                // Add to list
                mSprite.addFolder(mCurrentFolder);
            }
        } else if (localName.equalsIgnoreCase("file")) {
            String name = attributes.getValue("name");

            if (name != null) {
                // Add to current folder
                mCurrentFolder.addFile(new SpriterFile(name));
            }
        } else if (localName.equalsIgnoreCase("animation")) {
            String name = attributes.getValue("name");
            int length = Integer.parseInt(attributes.getValue("length"));

            if (name != null && length >= 0) {
                // Create new animation
                mCurrentAnimation = new SpriterAnimation(name, length);
                // Add to actual entity
                mSprite.addAnimation(mCurrentAnimation);
            }
        } else if (localName.equalsIgnoreCase("mainline")) {
            // Following keys belong to mainline
            mInMainline = true;
        } else if (localName.equalsIgnoreCase("key")) {
            // Time is 0 if the attribute is missing
            int time = 0;
            // Retrieve the right time
            if (attributes.getValue("time") != null) {
                time = Integer.parseInt(attributes.getValue("time"));
            }

            if (mInMainline) {
                // Create new key and add to mainline
                mCurrentMainlineKey = new SpriterMainlineKey(time);
                mCurrentAnimation.addMainlineKey(mCurrentMainlineKey);
            } else {
                int spin = 1;

                if (attributes.getValue("spin") != null) {
                    spin = Integer.parseInt(attributes.getValue("spin"));
                }

                // Create new key and add to current timeline
                mCurrentTimelineKey = new SpriterTimelineKey(time, spin);
                mCurrentTimeline.addKey(mCurrentTimelineKey);
            }
        } else if (localName.equalsIgnoreCase("object_ref")) {
            int timelineId = Integer.parseInt(attributes.getValue("timeline"));
            int key = Integer.parseInt(attributes.getValue("key"));

            // Add new object ref to mainline
            mCurrentMainlineKey.addObjectRef(new SpriterObjectRef(timelineId, key));
        } else if (localName.equalsIgnoreCase("timeline")) {
            // Following keys belongs to current timeline
            mInMainline = false;
            // Create timeline and add it to sprite
            mCurrentTimeline = new SpriterTimeline();
            mCurrentAnimation.addTimeline(mCurrentTimeline);
        } else if (localName.equalsIgnoreCase("object")) {
            int folder = Integer.parseInt(attributes.getValue("folder"));
            int file = Integer.parseInt(attributes.getValue("file"));
            float x = 0;
            float y = 0;
            float pivotX = 0.0f;
            float pivotY = 1.0f;
            float angle = 0.0f;
            float scaleX = 1.0f;
            float scaleY = 1.0f;
            float alpha = 1.0f;

            if (attributes.getValue("x") != null) {
                x = Float.parseFloat(attributes.getValue("x"));
            }
            if (attributes.getValue("y") != null) {
                y = Float.parseFloat(attributes.getValue("y"));
            }
            if (attributes.getValue("pivot_x") != null) {
                pivotX = Float.parseFloat(attributes.getValue("pivot_x"));
            }
            if (attributes.getValue("pivot_y") != null) {
                pivotY = Float.parseFloat(attributes.getValue("pivot_y"));
            }
            if (attributes.getValue("scale_x") != null) {
            	scaleX = Float.parseFloat(attributes.getValue("scale_x"));
            }
            if (attributes.getValue("scale_y") != null) {
            	scaleY = Float.parseFloat(attributes.getValue("scale_y"));
            }
            if (attributes.getValue("angle") != null) {
                angle = Float.parseFloat(attributes.getValue("angle"));
            }
            if (attributes.getValue("a") != null) {
                alpha = Float.parseFloat(attributes.getValue("a"));
            }

            // Create new spritet object and add it to current timeline key
            mCurrentTimelineKey.addObject(new SpriterObject(folder, file, x, y, pivotX, pivotY, scaleX, scaleY, angle, alpha));
        }
    }

    public static SpriterEntity createSpriterFrom(Context context, String name) {
        // Create Sprite Loader
        SpriterLoader loader = new SpriterLoader(context, name);
        // Parse file
        loader.startParse();

        // Return the spriter entity created
        return loader.getSpriterEntity();
    }
}
