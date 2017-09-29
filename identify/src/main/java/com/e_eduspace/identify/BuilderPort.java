package com.e_eduspace.identify;

/**
 * Created by Administrator on 2017-08-08.
 */

public interface BuilderPort {
    IDentify build();
    BuilderPort setTimeout(int retryCount, int retryTime);
    void closeDB();
}
