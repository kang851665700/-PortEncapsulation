package com.liyujie.portencapsulation.connector;

public interface Port {

    public abstract void  onDataSuccess(byte[] bytes);

    public abstract void onDataFailure();
}
