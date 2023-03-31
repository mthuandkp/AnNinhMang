/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import Utils.DateUtils;

/**
 *
 * @author mthuan
 */
public class Keys {
    private String key;
    private String createAt;
    private String getKeyAt;

    public Keys(String key) {
        this.key = key;
        this.createAt = DateUtils.getLocalTimeEpoch();
        this.getKeyAt = DateUtils.getLocalTimeEpoch();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getGetKeyAt() {
        return getKeyAt;
    }

    public void setGetKeyAt(String getKeyAt) {
        this.getKeyAt = getKeyAt;
    }

    @Override
    public String toString() {
        return "Keys{" + "key=" + key + ", createAt=" + createAt + ", getKeyAt=" + getKeyAt + '}';
    }
}
