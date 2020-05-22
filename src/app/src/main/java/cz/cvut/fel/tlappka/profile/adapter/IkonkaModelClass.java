package cz.cvut.fel.tlappka.profile.adapter;

import android.net.Uri;

public class IkonkaModelClass {
    private Uri image;
    private String title;
    private String UID;

    public IkonkaModelClass(Uri image, String title, String UID) {
        this.image = image;
        this.title = title;
        this.UID = UID;
    }

    public Uri getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}

