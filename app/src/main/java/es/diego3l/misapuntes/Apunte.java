package es.diego3l.misapuntes;

/**
 * Created by dlolh on 11/08/2016.
 */

//---------Denominado POJO--------------
public class Apunte {
    private int mId;
    private String mContent;
    private int mImportant;

    public Apunte(int id, String content, int important){
        mId = id;
        mContent = content;
        mImportant = important;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getImportant() {
        return mImportant;
    }

    public void setImportant(int important) {
        mImportant = important;
    }
}
