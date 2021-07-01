package com.example.background.module;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.example.background.Database.DBFlowDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 *
 */
public class FileBean implements Parcelable {
    public static final int ZIP_FILE = 0;
    public static final int CSV_FILE = 1;

    private int id;
    private String fileName;
    private String filePath;
    private long fileSize;
    private String time;
    private boolean isSelect;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean select) {
        isSelect = select;
    }

    public String getFilePath() {
        return filePath == null ? "" : filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName == null ? "" : fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileName);
        dest.writeString(this.filePath);
        dest.writeLong(this.fileSize);
        dest.writeString(this.time);
        dest.writeInt(this.type);
    }

    public FileBean() {
    }

    protected FileBean(Parcel in) {
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.fileSize = in.readLong();
        this.time = in.readString();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<FileBean> CREATOR = new Parcelable.Creator<FileBean>() {
        @Override
        public FileBean createFromParcel(Parcel source) {
            return new FileBean(source);
        }

        @Override
        public FileBean[] newArray(int size) {
            return new FileBean[size];
        }
    };
}
