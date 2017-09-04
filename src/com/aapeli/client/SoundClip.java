package com.aapeli.client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

class SoundClip {
    private URL location;
    private String filename;
    private boolean debug;
    private AudioClip clip;
    private boolean clipLoaded;

    protected SoundClip(Applet var1, URL var2, String var3, boolean debug) {
        this.location = var2;
        this.filename = var3;
        this.debug = debug;
        this.clip = null;
        this.clipLoaded = false;
    }

    protected boolean isLoaded() {
        return this.clipLoaded;
    }

    protected void defineClip() {
        if (!clipLoaded) {
            if (this.debug) {
                System.out.println("SoundClip.defineClip(): \'dir\'=\"" + this.location + "\", \'file\'=\"" + this.filename + "\"");
            }

            //todo this.anAudioClip1477 = this.anApplet1473.getAudioClip(this.anURL1474, this.aString1475);
            URL url = location;
            try {
                url = new URL(location, filename);
            } catch (Exception ex) {
            }
            clip = Applet.newAudioClip(url);
            clipLoaded = true;
        }
    }

    protected AudioClip getClip() {
        return clip;
    }
}
