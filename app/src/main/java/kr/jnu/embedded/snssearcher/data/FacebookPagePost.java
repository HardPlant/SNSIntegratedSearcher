package kr.jnu.embedded.snssearcher.data;

import android.media.Image;

/**
 * Created by KANG on 2017-12-01.
 */

public class FacebookPagePost {
    Image icon;
    String name;
    String message;

    public FacebookPagePost(Image icon, String name, String message) {
        this.icon = icon;
        this.name = name;
        this.message = message;
    }
}
