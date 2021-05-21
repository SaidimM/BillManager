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
@Table(database = DBFlowDataBase.class)
public class FileBean extends BaseModel implements Parcelable {
    @PrimaryKey(autoincrement = true)
    private int id;
    @Column private String fileName;
    @Column private String filePath;
    @Column private long fileSize;
    @Column private String time;
    @Column private boolean isSelect;

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
    }

    public FileBean() {
    }

    protected FileBean(Parcel in) {
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.fileSize = in.readLong();
        this.time = in.readString();
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

    @NonNull
    @Override
    public String toString() {
        return "FileInfo{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", time='" + time + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
