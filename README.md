# AndEngine Spriter Extension

[Spriter](http://www.brashmonkey.com/spriter.htm) is an animation tool for 2d games. This extension allow to use SCML files with  [`AndEngine`](https://github.com/nicolasgramlich/AndEngine). It has been developed thinking the best easiest way to use it. Example:

```java
// Create sprite from a 'scml' file
SpriterEntity sprite = SpriterLoader.createSpriterFrom(this, "example.scml");
// Load textures
sprite.loadResources(this, getTextureManager(), getVertexBufferObjectManager());
```

```java
// Add to main scene
mMainScene.attachChild(sprite)
```

SpriterEntity extends from Entity so you can change its position, rotation, etc.


## Known limitations

* This version only support AndEngine GLES2 branch. I don't need GLES2-AnchorCenter support right now so I think the extension will not work on it. If you need GLES2-AnchorCenter support you are free to port this extension.
* It doesn't support textures in TexturePacker format yet.


## Example

You can download and play with my [`AndEngineSpriterExtensionExample`](https://github.com/arturogutierrez/AndEngineSpriterExtensionExample) project or, simply, you can download the [APK file](https://github.com/downloads/arturogutierrez/AndEngineSpriterExtensionExample/AndEngineSpriterExtensionExample.apk) resultant for testing in your device.

## Version History

Version 1.0 - Oct 14, 2012

* Initial release

## Developer:

* Arturo Gutiérrez <arturo.gutierrez@gmail.com>